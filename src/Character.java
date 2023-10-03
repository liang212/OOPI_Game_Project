import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;

public class Character {
    protected final Map MAP;
    private final int NUMBER;
    private final ImageIcon ICON;
    private int qualityOfLife, x, y, rank;
    private List<Boolean> garbageBag = Arrays.asList(false, false, false, false, false); //false:無，true:有

    public Character(Map m, int number) {
        this.MAP = m;
        this.NUMBER = number;
        this.ICON = Icon.CHARACTER[number - 1];
        reset(number);
    }

    public void place() {
        int index = MAP.indexOfGarbageAt(x, y);
        if (index == -1) {  //該座標沒有垃圾
            if (garbageBag.contains(true)){ //持有垃圾
                garbageBag.set(garbageBag.indexOf(true), false);
                MAP.addGarbage(x, y); //放置垃圾
            }
        } else if (garbageBag.contains(false)){
            garbageBag.set(garbageBag.indexOf(false), true);
            MAP.removeGarbage(index);
        }
        MAP.repaint();
    }

    public void reset(int number){
        MAP.canMoveTo(x, y, true); //避免重開局後卡住
        qualityOfLife = 10;
        x = (number % 2 != 0) ? 0 : MAP.width() - 1;
        y = (number <= 2) ? 0 : MAP.height() - 1;
        MAP.canMoveTo(x, y, false);
        rank = 0;
        garbageBag.replaceAll(e -> false);
    }

    public ImageIcon icon() {
        return ICON;
    }

    public void qualityOfLife(int n) {
        if (n >= 0)
            qualityOfLife = n;
        else if (n < 0 && qualityOfLife > 0)
            qualityOfLife--;
    }

    public int qualityOfLife() {
        return qualityOfLife;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public int number(){
        return NUMBER;
    }

    public void rank(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }

    public List<Boolean> garbageBag() {
        return garbageBag;
    }

    public synchronized void move(boolean isHorizontal, boolean isPositive) {
        int[] xy = {x, y};
        int index = (isHorizontal) ? 0 : 1, bound = (isHorizontal) ? MAP.width() : MAP.height();
        xy[index] += (isPositive) ? 1 : -1;
        if (xy[index] >= 0 && xy[index] < bound && MAP.canMoveTo(xy[0], xy[1])){  //將要移動至的位置未超出邊界且可以通行
            MAP.canMoveTo(x, y, true);
            if (MAP.portalIsOpen())
                xy = MAP.transportFrom(xy[0], xy[1]);
            x = xy[0];
            y = xy[1];
            if (MAP.pickShieldAt(x, y))
                qualityOfLife += 3;
            //更改位置
            MAP.canMoveTo(x, y, false);  //設定更改後的位置為(除自身外)不可通行
        }
        MAP.repaint();
    }
}