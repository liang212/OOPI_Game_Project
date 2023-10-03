import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Threads extends JFrame implements Runnable {
    private final int WIDTH = 17, HEIGHT = 16;  //地圖寬高以方格計，每方格50px * 50px
    private final List<JButton> TITLE_BUTTONS = Arrays.asList(new JButton(Icon.TITLE_BUTTONS[0]), new JButton(Icon.TITLE_BUTTONS[1]), new JButton(Icon.TITLE_BUTTONS[2])),
                                PAGE_BUTTONS = Arrays.asList(new JButton(Icon.RULE_BUTTONS[0]), new JButton(Icon.RULE_BUTTONS[1]), new JButton(Icon.RULE_BUTTONS[2])),
                                OVER_BUTTONS = Arrays.asList(new JButton(), new JButton());
    private final GamePanel TITLE = new GamePanel("title");
    private final ButtonListener BUTTON_LISTENER = new ButtonListener();
    private final Timer TIMER = new Timer();

    private boolean inProgress = false, ruleGarbageIsPicked;
    private int gameTime, rulePage;
    private Integer[] rulePosition;
    private Audio backgroundMusic, portal;
    private Character[] characters;
    private AI_Thread[] AI_Threads = new AI_Thread[3];
    private Map map;
    private MapType maptype;
    private JLabel[]    status = new JLabel[6], //status(遊戲上方狀態列): time, portal, QOL(4 個)
                        hand = new JLabel[5];
    private GamePanel game, rule, gameOver;
    private JTextArea rankingMessage;
    private List<Integer> qualityOfLife;  //生活品質（血量）
    private Task countdown;

    public Threads() {
        super("潔境");
        backgroundMusic = new Audio("Title");
        backgroundMusic.loop();
        setIconImage(Icon.ICON.getImage()); //縮圖
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        TITLE.setPreferredSize(new Dimension(WIDTH * 50, HEIGHT * 50 + 30));
        for (int i = 0; i < TITLE_BUTTONS.size(); i++) {
            TITLE_BUTTONS.get(i).setBounds(i * 250 + 100, HEIGHT * 50 - 140, 150, 50);
            TITLE.add(TITLE_BUTTONS.get(i));
            TITLE_BUTTONS.get(i).setContentAreaFilled(false);
            TITLE_BUTTONS.get(i).setRolloverIcon(Icon.TITLE_BUTTONS_ROLLOVER[i]);
            TITLE_BUTTONS.get(i).addMouseListener(BUTTON_LISTENER);
        }
        add(TITLE);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        setFocusable(true);
        addKeyListener(new Keyboard());
    }

    private void rulePositionMove(boolean isHorizontal, boolean isPositive){
        int[] xy = {rulePosition[0], rulePosition[1]};
        xy[(isHorizontal) ? 0 : 1] += (isPositive) ? 1 : -1;
        if (xy[0] >= 0 && xy[0] <= 4)
            rulePosition[0] = xy[0];
        if (xy[1] >= 0 && xy[1] <= 4)
            rulePosition[1] = xy[1];
    }

    private void gameStart() {
        maptype = new Map1();
        map = new Map(this, maptype, WIDTH, HEIGHT);
        map.setOpaque(false);
        characters = new Character[] {new Character(map, 1), new AI(map, 2), new AI(map, 3), new AI(map, 4)};
        map.setCharacters(characters);
        qualityOfLife = Arrays.asList(new Integer[characters.length]);
        game = new GamePanel("game");
        game.setBounds(0, 0, WIDTH * 50, 30);
        map.setBounds(0, 30, WIDTH * 50, HEIGHT * 50);
        
        for (int i = 0; i < status.length; i++) {
            status[i] = new JLabel("");
            game.add(status[i]);
            status[i].setBounds(((i == 0) ? 80 : ((i == 1) ? 210 : (255 + i * 69))), 0, ((i == 0) ? 40 : ((i == 1) ? 30 : 20)), 30);
        }

        for (int i = 0; i < hand.length; i++){
            hand[i] = new JLabel(Icon.NO_GARBAGE_ICON);
            game.add(hand[i]);
            hand[i].setBounds((695 + 25 * i), 5, 25, 25);
        }

        rankingMessage = new JTextArea();
        rankingMessage.setEditable(false);
        rankingMessage.setOpaque(false);
        rankingMessage.setFont(new Font("Sans", Font.PLAIN, 15));
        rankingMessage.setBounds(50, 70, 300, 270);
        gameOver = new GamePanel("gameOver");
        gameOver.add(rankingMessage);
        for (int i = 0; i < OVER_BUTTONS.size(); i++){
            OVER_BUTTONS.get(i).setBounds((190 + 97 * i), 330, 60, 30);
            OVER_BUTTONS.get(i).setContentAreaFilled(false);
            OVER_BUTTONS.get(i).addMouseListener(BUTTON_LISTENER);
            gameOver.add(OVER_BUTTONS.get(i));
        }
        
        game.add(map);
        game.add(gameOver);
        gameOver.setBounds(WIDTH * 25 - 200, HEIGHT * 25 - 200, 400, 400);
        add(game);
        TITLE.setVisible(false);
        setVisible(true);

        initialize();
        
        new Thread(this).start();
    }

    private void initialize() {
        backgroundMusic.stop();
        backgroundMusic = new Audio("BGM");
        backgroundMusic.loop();
        portal = new Audio("WhenPortalIsOpen");
        portal.play();
        gameTime = 300;
        map.initialize();
        for (Character c : characters){
            c.reset(c.number());
        }
        for (JLabel l : hand){
            l.setIcon(Icon.NO_GARBAGE_ICON);
        }
        for (int i = 0; i < status.length; i++){
            setLabel(i);
        }
        game.repaint();
        gameOver.setVisible(false);
        map.setVisible(true);
        inProgress = true;
        countdown = new Task();
        TIMER.schedule(countdown, 1000, 1000);
        for (int i = 0; i < AI_Threads.length; i++){
            AI_Threads[i] = new AI_Thread(i + 1);
            AI_Threads[i].start();
        }
    }

    @Override
    public void run() {
        while (inProgress) {
            for (int i = 2; i < status.length; i++){
                setLabel(i);
            }
            qualityOfLife = Arrays.asList(characters[0].qualityOfLife(), characters[1].qualityOfLife(), characters[2].qualityOfLife(), characters[3].qualityOfLife());
            if (Collections.frequency(qualityOfLife, Integer.valueOf(0)) >= 3|| gameTime == 0) {    // 0 ，代表只剩一名角色存活
                countdown.cancel();
                inProgress = false;
                if (map.portalIsOpen()){
                    map.setPortal();
                    portal.stop();
                }
                map.setVisible(false);
                Collections.sort(qualityOfLife, Collections.reverseOrder());
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < characters.length ; i++){
                    if (characters[i].rank() == 0)
                        characters[i].rank(1 + qualityOfLife.indexOf(Integer.valueOf(characters[i].qualityOfLife())));
                }
                qualityOfLife = Arrays.asList(characters[0].rank(), characters[1].rank(), characters[2].rank(), characters[3].rank());

                for (int i = 1; i <= characters.length; i ++) {
                    if (qualityOfLife.contains(Integer.valueOf(i))){
                        s.append(String.format("第%d名：　", i));
                        for (int j = 0; j < characters.length; j++) {
                            if (characters[j].rank() == i)
                                s.append(String.format("角色%d　", (j + 1)));
                        }
                        s.append("\n");
                    }
                }
                s.append((characters[0].rank() == 1) ? "\n雖然你贏了遊戲，但卻可能是因為將汙染轉嫁\n給了別人。雖然不必對遊戲太認真，但現實中\n還是不該亂丟垃圾喔" : "\n己所不欲，勿施於人。\n如果你會因為遊戲角色亂丟垃圾到你家（害你\n輸了）而生氣，那希望你能知道亂丟垃圾真的\n是個不好的行為。\n真的忍不住就在遊戲裡丟吧！");
                rankingMessage.setText(s.toString());
                gameOver.setVisible(true);
                for (int i = 0; i < 3; i ++){
                    AI_Threads[i].interrupt();
                }
                backgroundMusic.stop();
                backgroundMusic = new Audio("Ranking");
                backgroundMusic.loop();
            }
        }
    }

    public int getTime() {
        return gameTime;
    }

    public void setLabel(int i) {
        if (i >= 0 && i < status.length)
            status[i].setText((i == 0) ? String.format("%d",gameTime) : ((i == 1) ? ((map.portalIsOpen()) ? "開啟" : "關閉") : String.format("%d", characters[i - 2].qualityOfLife())));
    }

    public boolean isInProgress() {
        return inProgress;
    }

    private class AI_Thread extends Thread {
        private final int NUMBER;

        public AI_Thread(int number) {
            this.NUMBER = number;
        }

        @Override
        public void run() {
            try {
                while (characters[NUMBER].qualityOfLife() > 0){
                    ((AI) characters[NUMBER]).act();
                    Thread.sleep(333);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ButtonListener extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e){
            new Audio("ButtonRollOver").play();
        }
        @Override
        public void mousePressed(MouseEvent e){
            new Audio("Button").play();
        }
        @Override
        public void mouseReleased(MouseEvent e){
            Object button = e.getSource();
            int i = TITLE_BUTTONS.indexOf(button);
            switch (i){
                case -1 :
                    break;
                case 0 :
                    gameStart();
                    break;
                case 1:
                    rulePage = 1;
                    rulePosition = new Integer[]{2, 2, 2, 3};
                    ruleGarbageIsPicked = false;
                    if (rule == null){
                        rule = new GamePanel("rule");
                        for (int j = 0; j < PAGE_BUTTONS.size(); j++){
                            rule.add(PAGE_BUTTONS.get(j));
                            PAGE_BUTTONS.get(j).setContentAreaFilled(false);
                            PAGE_BUTTONS.get(j).setBounds(WIDTH * 25 - 190 + 150 * j, HEIGHT * 50 - 40, 100, 50);
                            PAGE_BUTTONS.get(j).addMouseListener(this);
                        }
                        add(rule);
                    }
                    rule.repaint();
                    PAGE_BUTTONS.get(0).setVisible(false);
                    PAGE_BUTTONS.get(2).setVisible(true);
                    TITLE.setVisible(false);
                    rule.setVisible(true);
                    break;
                case 2 :
                    System.exit(0);
            }
            if (i == -1 && rule != null){
                i = PAGE_BUTTONS.indexOf(button);
                if (i != -1){
                    if (i == 1){
                        rule.setVisible(false);
                        TITLE.setVisible(true);
                        rulePage = -1;
                    } else{
                        rulePage += (i == 2) ? 1 : -1;
                        PAGE_BUTTONS.get(2 - i).setVisible(true);
                        if (rulePage == Icon.RULE.length)
                            PAGE_BUTTONS.get(2).setVisible(false);
                        else if (rulePage == 1)
                            PAGE_BUTTONS.get(0).setVisible(false);
                    }
                    rule.repaint();
                }
            }
            if (i == -1 && OVER_BUTTONS != null){
                i = OVER_BUTTONS.indexOf(button);
                if (i == 0){
                    initialize();
                    new Thread(Threads.this).start();
                }
                else
                    System.exit(0);
            }
        }
    }

    private class GamePanel extends JPanel{
        private final String KIND;
        public GamePanel(String kind){
            this.KIND = kind;
            setLayout(null);
        }
        @Override
        public void paintComponent(Graphics g){
            switch (KIND){
                case "title":
                    Icon.TITLE_SCREEN.paintIcon(this, g, 0, 0); 
                    break;
                case "game":
                    Icon.STATUS_BAR.paintIcon(this, g, 0, 0);
                    maptype.background().paintIcon(this, g, 0, 30);
                    break;
                case "gameOver":
                    Icon.GAMEOVER.paintIcon(this, g, 0, 0);
                    break;
                case "rule":
                    Icon.RULE[rulePage - 1].paintIcon(this, g, 0, 0);
                    if (rulePage == 1){
                        Icon.CHARACTER[0].paintIcon(this, g, 300 + 50 * rulePosition[0], 100 + 50 * rulePosition[1]);
                        if (!ruleGarbageIsPicked)
                            Icon.GARBAGE.paintIcon(this, g, 300 + 50 * rulePosition[2], 100 + 50 * rulePosition[3]);
                    }
                    break;
            }
        }
    }

    private class Keyboard extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            if ((inProgress && characters[0].qualityOfLife() > 0) || rulePage == 1){
                int keyCode = e.getKeyCode();
                switch (keyCode){
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        boolean isHorizontal = (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D),
                                isPositive = (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S);
                        if (rulePage == 1)
                            rulePositionMove(isHorizontal, isPositive);
                        else
                            characters[0].move(isHorizontal, isPositive);
                        break;
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                        if (rulePage == 1){
                            if (ruleGarbageIsPicked){
                                rulePosition[2] = rulePosition[0];
                                rulePosition[3] = rulePosition[1];
                                ruleGarbageIsPicked = false;
                                new Audio("Place").play();
                            }else if (rulePosition[0] == rulePosition[2] && rulePosition[1] == rulePosition[3])
                                ruleGarbageIsPicked = true;
                        } else {
                            int n = Collections.frequency(characters[0].garbageBag(), true);
                            characters[0].place();
                            if (n > Collections.frequency(characters[0].garbageBag(), true))
                                new Audio("Place").play();
                            List<Boolean> garbageBag = characters[0].garbageBag();
                            for (int i = 0; i < hand.length; i++){
                                hand[i].setIcon((garbageBag.get(i)) ? Icon.GARBAGE_ICON : Icon.NO_GARBAGE_ICON);
                            }
                        }
                }
                if (rulePage == 1)
                    rule.repaint();
            }
        }
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            if (gameTime > 0){
                gameTime--;
                if (gameTime != 300 && gameTime % 10 == 0){  //每過 10 秒切換傳送門開關狀態(此處寫法為當時間為10的倍數時切換)
                    map.setPortal();
                    if (!map.portalIsOpen())
                        portal.stop();
                    else if (gameTime != 0){
                        portal = new Audio("WhenPortalIsOpen");
                        portal.loop();
                    }
                    setLabel(1);
                }
                setLabel(0);
            }
        }
    }
}