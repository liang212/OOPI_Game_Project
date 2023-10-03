import javax.swing.ImageIcon;

public class GameObject {
    private final ImageIcon ICON;
    private final int X, Y;

    public GameObject(int x, int y, ImageIcon icon) {
        this.X = x;
        this.Y = y;
        this.ICON = icon;
    }

    public ImageIcon icon() {
        return ICON;
    }

    public int X() {
        return X;
    }

    public int Y() {
        return Y;
    }
}
