import javax.swing.ImageIcon;
public abstract class MapType {
    private final ImageIcon BACKGROUND;
    private final boolean[][] CAN_MOVE_TO;
    private final GameObject[] HOME, PORTAL;
    private final Shield[] SHIELD;

    public MapType(ImageIcon background, boolean[][] canMoveTo, GameObject[] home, GameObject[] portal, Shield[] shield) {
        this.BACKGROUND = background;
        this.CAN_MOVE_TO = canMoveTo;
        this.HOME = home;
        this.PORTAL = portal;
        this.SHIELD = shield;
    }

    public ImageIcon background(){
        return BACKGROUND;
    }

    public boolean[][] canMoveTo() {
        return CAN_MOVE_TO;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends GameObject> T[] getObjects(int number){
        return (number == 0) ? (T[]) HOME : ((number == 1) ? (T[]) PORTAL : ((number == 2) ? (T[]) SHIELD : null));
    }
    
    abstract public int ownerOf(int x, int y);

    abstract public int[][] territoryOf(int n);
}
