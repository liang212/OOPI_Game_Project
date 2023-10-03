import java.util.Arrays;
public class Map1 extends MapType {
    private final static GameObject[][] OBJECT = objects();
    private final int[][][] TERRITORIES = setTerritories();

    public Map1() {
        super(Icon.MAP_1, getCanMoveTo(), OBJECT[0], OBJECT[1], shields());
    }

    private static boolean[][] getCanMoveTo(){
        boolean[][] canMoveTo = new boolean[17][16];
        for (boolean[] b : canMoveTo){
            Arrays.fill(b, true);
        }
        int[][] obstacle = {{ 1, 0 }, { 4, 0 }, { 7, 0 }, { 12, 0 }, { 15, 0 }, { 1, 1 }, { 4, 1 },
        { 8, 1 }, { 9, 1 }, { 12, 1 }, { 15, 1 }, { 3, 2 }, { 13, 2 }, { 6, 3 }, { 8, 3 }, { 10, 3 }, { 2, 4 },
        { 6, 4 }, { 10, 4 }, { 14, 4 }, { 2, 5 }, { 3, 5 }, { 4, 5 }, { 7, 5 }, { 8, 5 }, { 12, 5 }, { 13, 5 },
        { 14, 5 }, { 9, 6 }, { 1, 7 }, { 4, 7 }, { 12, 7 }, { 15, 7 }, { 2, 8 }, { 5, 8 }, { 7, 8 }, { 9, 8 },
        { 11, 8 }, { 14, 8 }, { 7, 9 }, { 2, 10 }, { 3, 10 }, { 4, 10 }, { 8, 10 }, { 9, 10 }, { 12, 10 },
        { 13, 10 }, { 14, 10 }, { 2, 11 }, { 14, 11 }, { 6, 12 }, { 8, 12 }, { 10, 12 }, { 3, 13 }, { 6, 13 },
        { 10, 13 }, { 13, 13 }, { 1, 14 }, { 4, 14 }, { 7, 14 }, { 8, 14 }, { 12, 14 }, { 15, 14 }, { 1, 15 },
        { 4, 15 }, { 9, 15 }, { 12, 15 }, { 15, 15 }};
        for (int[] o : obstacle) {
            canMoveTo[o[0]][o[1]] = false;
        }
        return canMoveTo;
    }
    private static Shield[] shields(){
        int[][] s = {{0, 4}, {16, 4}, {0, 11}, {16, 11}};
        Shield[] shields = new Shield[4];
        for (int i = 0; i < s.length; i++){
            shields[i] = new Shield(s[i][0], s[i][1]);
        }
        return shields;
    }

    private static GameObject[][] objects(){
        int[][][] o =   {{{4, 3}, {12, 3}, {4, 12}, {12, 12}},    //home
        {{3, 0}, {13, 15}, {3, 15}, {13, 0}}};   //portal
        GameObject[][] obj = new GameObject[2][4];
        for (int i = 0; i < o.length; i++) {
            for (int j = 0; j < o[i].length; j++){
                int x = o[i][j][0],
                y = o[i][j][1];
                obj[i][j] = new GameObject(x, y, ((i == 0) ? Icon.HOME[j] : ((j < 2) ? Icon.PORTAL[0] : Icon.PORTAL[1])));
            }
        }
        return obj;
    }

    private int[][][] setTerritories(){
        int[][] publicT = new int[32][2];    //17 * 16 - 43 * 4 - obstacle(68)
        int[][][] t = new int[5][][];
        t[0] = publicT;
        for (int i = 0; i < 4; i++){
            t[i + 1] = new int[43][2];      //56 - 13
        }
        int[] count = {0, 0, 0, 0, 0};
        int x = 0, y = 0;
        for (int i = 0; i < 272; i++){
            if (i % 17 == 0)
                x = 0;
            y = i / 17;
            int owner = ownerOf(x, y);
            if (owner != -1)
                t[owner][count[owner]++] = new int[]{x, y};
            x++;
            y++;
        }
        return t;
    }

    @Override
    public int ownerOf(int x, int y) {
        if (!getCanMoveTo()[x][y])
            return -1;  //obstacle
        else
            return (x == 8 || y == 7 || y == 8) ? 0 : ((x <= 7) ? ((y <= 6) ? 1 : 3) : ((y <= 6) ? 2 : 4));
    }

    @Override
    public int[][] territoryOf(int number){
        return TERRITORIES[number];
    }
}
