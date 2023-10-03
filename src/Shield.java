public class Shield extends GameObject {
    private boolean used = false;

    public Shield(int x, int y) {
        super(x, y, Icon.SHIELD);
    }

    public boolean isUsed() {
        return used;
    }

    public void use() {
        used = true;
    }

    public void reset(){
        used = false;
    }
}