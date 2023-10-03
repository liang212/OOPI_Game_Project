public class Garbage extends GameObject {
    private final int END_TIME;
    private final Character POSITION_OWNER;

    public Garbage(Character posOwner, int x, int y, int startTime) {
        super(x, y, Icon.GARBAGE);
        this.POSITION_OWNER = posOwner;
        END_TIME = startTime - 5;
    }

    public Character positionOwner(){
        return POSITION_OWNER;
    }

    public int positionOwnerNumber(){
        return (POSITION_OWNER == null) ? 0 : POSITION_OWNER.number();
    }

    public int endTime() {
        return END_TIME;
    }
}