import java.util.Collections;
import java.util.Random;

public class AI extends Character {
    private static final Random RANDOM = new Random();
    private final int[][] TERRITORY;
    private int destinationOwner;
    private int[] destination = null;

    public AI(Map m, int number) {
        super(m, number);
        TERRITORY = MAP.territoryOf(number());
    }

    private void setDestination(int[] destination){
        this.destination = destination;
        destinationOwner = (destination == null) ? 0 : MAP.ownerOf(destination[0], destination[1]);
    }

    public synchronized void act() {
        int count = Collections.frequency(garbageBag(), Boolean.valueOf(true));
        destinationOwner = (destination == null) ? 0 : MAP.ownerOf(destination[0], destination[1]);
        int owner = MAP.ownerOf(X(), Y());
        if (owner != 0 && !MAP.isAlive(owner))
            owner = 0;
        if (destinationOwner == number() && (MAP.indexOfGarbageAt(destination[0], destination[1]) == -1 || !MAP.canMoveTo(destination[0], destination[1])))
            setDestination(null);
        
        if (count > RANDOM.nextInt(5)){
            if (owner != number() && MAP.indexOfGarbageAt(X(), Y()) == -1){
                int random = RANDOM.nextInt(100);
                if ((owner == 0 && ((count == garbageBag().size() && random < 80) || random < 20)) || (owner != 0 && (count == garbageBag().size() || random < 80)))
                    place();
            }
            if (destination == null){
                int n;
                do{
                    n = RANDOM.nextInt(4) + 1;
                } while (n == number() || !MAP.isAlive(n));
                int[][] rivalTerritory = MAP.territoryOf(n);
                do{
                    n = RANDOM.nextInt(rivalTerritory.length);
                } while (!MAP.canMoveTo(rivalTerritory[n][0], rivalTerritory[n][1]));
                setDestination(rivalTerritory[n]);
            }
        }

        if (count != garbageBag().size()){
            if (destination == null){
                if (MAP.indexOfGarbageAt(X(), Y()) != -1 && (owner == number() || (owner != number() && RANDOM.nextInt(100) < 20)))
                    place();
                setDestination(MAP.garbageInTerritoryOf(number()));
            } else if (destination[0] == X() && destination[1] == Y()){
                if (MAP.indexOfGarbageAt(X(), Y()) != -1)
                    place();
                setDestination(null);
                
            }
        }

        if (destination != null){
            if (Collections.frequency(garbageBag(), Boolean.valueOf(true)) != garbageBag().size()){
                int[] pos = MAP.garbageInTerritoryOf(0);
                if (pos != null){
                    synchronized (pos){
                        if ((destinationOwner != number() || !MAP.canMoveTo(destination[0], destination[1])))
                            setDestination(pos);
                    }
                }
                int[] check = MAP.garbageInTerritoryOf(number());
                if (check != null){
                    synchronized(check){
                        setDestination(check);
                    }
                }
            }
            moveTo(destination[0], destination[1]);
            if (destinationOwner != number() && MAP.indexOfGarbageAt(X(), Y()) == -1 && owner != number() && (owner != 0 || (owner == 0 && RANDOM.nextInt(100) < 20))){
                place();
                setDestination(null);
            }
        } else{
            int[] pos = MAP.shieldNotUsed(number());
            if (pos != null){
                synchronized (pos){
                    setDestination(pos);
                }
            } else if (Collections.frequency(garbageBag(), Boolean.valueOf(true)) != garbageBag().size()){
                pos = MAP.garbageInTerritoryOf(0);
                if (pos != null){
                    synchronized (pos){
                        if (count != garbageBag().size())
                            setDestination(pos);
                    }
                } else {
                    int n;
                    do{
                        n = RANDOM.nextInt(TERRITORY.length);
                    } while (!MAP.canMoveTo(TERRITORY[n][0], TERRITORY[n][1]));
                    setDestination(TERRITORY[n]);
                }
            }
            if (destination != null){
                synchronized(destination){
                    moveTo(destination[0], destination[1]);
                }
            }
        }
    }

    private synchronized void moveTo(int dst_x, int dst_y){
        int dx = dst_x - X(), dy = dst_y - Y();
        boolean check, isHorizontal = true, isPositive = true;
        check = (dx != 0 && dy != 0) ? ((dx * dx != dy * dy) ? (dx * dx < dy * dy) : (RANDOM.nextInt(2) == 0)) : (dx == 0);

        if (check){
            isHorizontal = false;
            isPositive = !(dy < 0);
            if (!MAP.canMoveTo(X(), ((isPositive) ? Y() + 1 : Y() - 1))){
                boolean left = MAP.canMoveTo(X() - 1, Y()),
                        right = MAP.canMoveTo(X() + 1, Y());
                if (left || right){
                    isHorizontal = true;
                    isPositive = (left && right) ? !(RANDOM.nextInt(2) == 0) : !left;
                }
                else
                    isPositive = !isPositive;
            }
        } else {
            isHorizontal = true;
            isPositive = !(dx < 0);
            if (!MAP.canMoveTo(((isPositive) ? X() + 1 : X() - 1), Y())){
                boolean up = MAP.canMoveTo(X(), Y() - 1),
                        down = MAP.canMoveTo(X(), Y() + 1);
                if (up || down){
                    isHorizontal = false;
                    isPositive = (up && down) ? !(RANDOM.nextInt(2) == 0) : !up;
                }
                else
                    isPositive = !isPositive;
            }
        }
        move(isHorizontal, isPositive);
    }
}