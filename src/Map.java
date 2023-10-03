import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Map extends JPanel{
    private static final Random RANDOM = new Random();
    private static final Timer TIMER = new Timer();
    private final Threads MAIN_THREAD;
    private final int WIDTH, HEIGHT;
    private final MapType MAP_TYPE;
    private final GameObject[] HOME, PORTAL;
    private final Shield[] SHIELD;
    private boolean portalIsOpen;
    private boolean[][] canMoveTo;
    private Character[] characters;
    private List<Boolean> alive;
    private List<Garbage> garbages;


    public Map(Threads t, MapType mt, int width, int height) {
        this.MAIN_THREAD = t;
        this.MAP_TYPE = mt;
        this.WIDTH = width;
        this.HEIGHT = height;
        HOME = mt.getObjects(0);
        PORTAL = mt.getObjects(1);
        SHIELD = mt.getObjects(2);
        TIMER.schedule(new TimerTask(){
            @Override
            public void run() {
                for (int i = 0; i < garbages.size(); i++){
                    if (checkGarbage(i)){    //有垃圾造成汙染時，將從地圖中移除並回傳 true
                        new Audio("GarbageTimeUp").play();
                        t.setLabel(2);
                        i--;    //避免 exception
                        repaint();
                    }
                }
            }
        }, 1000, 1000);
        TIMER.schedule(new Task(), 2000);
        initialize();
    }

    public void setCharacters(Character[] characters){
        this.characters = characters;
    }

    public void initialize() {
        alive = Arrays.asList(true, true, true, true);
        portalIsOpen = true;
        for (Shield s : SHIELD){
            s.reset();
        }
        canMoveTo = MAP_TYPE.canMoveTo();
        garbages = new CopyOnWriteArrayList<>();
        repaint();
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            int delay = (5000 + RANDOM.nextInt(3) * 1000);   //5 ~ 7秒生成一次
            if (MAIN_THREAD.getTime() > 0){
                int rX, rY;
                do{ //設定生成位置(限制於地圖上)
                    rX = RANDOM.nextInt(WIDTH);
                    rY = RANDOM.nextInt(HEIGHT);
                } while (!canMoveTo[rX][rY]); //若位置為障礙物或任何角色當前位置則重抽位置
                addGarbage(rX, rY);   //生成炸彈
                repaint();
            }
            TIMER.schedule(new Task(), delay);
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        if (MAIN_THREAD.isInProgress()) {
            super.paintComponent(g);
            for (int i = 0; i < 4; i++) {
                HOME[i].icon().paintIcon(this, g, HOME[i].X() * 50, HOME[i].Y() * 50);
                PORTAL[i].icon().paintIcon(this, g, PORTAL[i].X() * 50, PORTAL[i].Y() * 50);
                if (!SHIELD[i].isUsed())
                    SHIELD[i].icon().paintIcon(this, g, SHIELD[i].X() * 50, SHIELD[i].Y() * 50);
            }
            for (Character c : characters) {
                if (c.qualityOfLife() == 0)
                    canMoveTo[c.X()][c.Y()] = true;
                else {
                    canMoveTo[c.X()][c.Y()] = false;
                    c.icon().paintIcon(this, g, c.X() * 50, c.Y() * 50);
                }
            }
            for (Garbage garbage : garbages) {
                garbage.icon().paintIcon(this, g, garbage.X() * 50, garbage.Y() * 50);
            }
        }
    }

    private synchronized boolean checkGarbage(int index) {
        Garbage g = (index < 0) ? null : garbages.get(index);
        if (g != null){
            if (g.endTime() > 0 && g.endTime() > MAIN_THREAD.getTime()) {
                int count = Collections.frequency(alive, Boolean.valueOf(true));
                Character owner = g.positionOwner();
                if (owner == null || owner.qualityOfLife() == 0){
                    for (int i = 0; i < characters.length; i++) {
                        if (characters[i].rank() == 0)
                            characters[i].qualityOfLife(-1);
                        if (characters[i].qualityOfLife() == 0)
                            alive.set(i, Boolean.valueOf(false));
                    }
                    if (count != Collections.frequency(alive, Boolean.valueOf(true))){
                        for (int i = 0; i < characters.length; i++) {
                            if (characters[i].rank() == 0 && characters[i].qualityOfLife() == 0)
                                characters[i].rank(1 + Collections.frequency(alive, Boolean.valueOf(true)));
                        }
                    }
                } else {    //單體 -1
                    owner.qualityOfLife(-1);
                    int i = owner.number() - 1;
                    if (owner.qualityOfLife() != 0 && g.X() >= HOME[i].X() - 1 && g.X() <= HOME[i].X() + 1
                            && g.Y() >= HOME[i].Y() - 1 && g.Y() <= HOME[i].Y() + 1)  //汙染位於家的九宮格內，總共-2
                        owner.qualityOfLife(-1);
                    if (owner.qualityOfLife() == 0 && owner.rank() == 0){
                        alive.set(i, false);
                        owner.rank(1 + Collections.frequency(alive, Boolean.valueOf(true)));
                    }
                }
                garbages.remove(index);
                return true;
            }
        }
        return false;
    }

    public int indexOfGarbageAt(int x, int y){
        for (Garbage g : garbages){
            if (g.X() == x && g.Y() == y)
                return garbages.indexOf(g);
        }
        return -1;
    }

    public int[] transportFrom(int x, int y){
        for (int i = 0; i < PORTAL.length; i++){
            if (PORTAL[i].X() == x && PORTAL[i].Y() == y){
                new Audio("Transport").play();
                return new int[]{(i % 2 == 0) ? PORTAL[i + 1].X() : PORTAL[i - 1].X(), (i % 2 == 0) ? PORTAL[i + 1].Y() : PORTAL[i - 1].Y()};
            }
        }
        return new int[]{x, y};
    }

    public boolean pickShieldAt(int x, int y){
        for (int i = 0; i < SHIELD.length; i++){
            if (SHIELD[i].X() == x && SHIELD[i].Y() == y && !SHIELD[i].isUsed()){
                SHIELD[i].use();
                new Audio("Shield").play();
                return true;
            }
        }
        return false;
    }

    public int[] shieldNotUsed(int number){
        Shield s = SHIELD[number - 1];
        synchronized(s){
            if (s.isUsed()){
                s = null;
                for (int i = 0; i < SHIELD.length; i++){
                    if (i != number && !SHIELD[i].isUsed())
                        s = SHIELD[i];
                }
            }
            return (s != null) ? new int[]{s.X(), s.Y()} : null;
        }
    }

    public int[] garbageInTerritoryOf(int number){
        for (Garbage g : garbages){
            int[] pos = {g.X(), g.Y()};
                    int owner = g.positionOwnerNumber() - 1;
                    boolean b = (owner == number - 1);
                    b = ((number == 0 && (b || !alive.get(owner))) || (number != 0 && b && alive.get(owner)));
                    if (canMoveTo(pos[0], pos[1]) && b)
                        return pos;
        }
        return null;
    }

    public void canMoveTo(int x, int y, boolean b) {
        canMoveTo[x][y] = b;
    }

    public boolean canMoveTo(int x, int y) {
        if (x >= 0  && x < WIDTH && y >= 0 && y < HEIGHT)
            return canMoveTo[x][y];
        return false;
    }

    public boolean portalIsOpen() {
        return portalIsOpen;
    }

    public void setPortal() {
        new Audio("SetPortal").play();
        portalIsOpen = !portalIsOpen;
    }

    public int width() {//以方格為單位
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }

    public int ownerOf(int x, int y){
        return MAP_TYPE.ownerOf(x, y);
    }

    public int[][] territoryOf(int n){
        return MAP_TYPE.territoryOf(n);
    }

    public synchronized void addGarbage(int x, int y) {
        int number = MAP_TYPE.ownerOf(x, y);
        Character owner = (number == 0) ? null : characters[number - 1];
        garbages.add(new Garbage(owner , x, y, MAIN_THREAD.getTime()));
    }

    public synchronized void removeGarbage(int index) {
        if (index < garbages.size())
            garbages.remove(index);
    }

    public boolean isAlive(int number){
        return alive.get(number - 1);
    }
}