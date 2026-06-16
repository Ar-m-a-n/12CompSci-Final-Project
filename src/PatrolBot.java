import java.util.ArrayList;

public class PatrolBot extends Bot {

    private ArrayList<int[]> path;
    private int targetIndex;
    private int moveTimer;
    private int moveDelay;

    public PatrolBot(Position startPos, Size size, int moveDelay, ArrayList<int[]> path) {
        super(startPos, size);
        this.moveDelay   = moveDelay;
        this.moveTimer   = 0;
        this.targetIndex = 0;
        this.path = path;
        position.setPosition(path.get(0)[0], path.get(0)[1]);
    }

    @Override
    public void update(MazeMap map) {
        moveTimer++;
        if (moveTimer < moveDelay) return;
        moveTimer = 0;

        int[] target = path.get(targetIndex);
        int tx = target[0];
        int ty = target[1];
        int cx = position.X();
        int cy = position.Y();

        // Reached current target — advance to next waypoint
        if (cx == tx && cy == ty) {
            targetIndex = (targetIndex + 1) % path.size();
            target = path.get(targetIndex);
            tx = target[0];
            ty = target[1];
        }

        // Step ONE cell toward target (horizontal first, then vertical)
        int dx = Integer.signum(tx - cx);
        int dy = Integer.signum(ty - cy);

        if (dx != 0 && !map.isWall(cx + dx, cy)) {
            position.setPosition(cx + dx, cy);
            return;
        }
        if (dy != 0 && !map.isWall(cx, cy + dy)) {
            position.setPosition(cx, cy + dy);
            return;
        }

        // Blocked — skip to next waypoint
        targetIndex = (targetIndex + 1) % path.size();
    }

    public static ArrayList<int[]> buildTopPath() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int x = 1; x <= 30; x++) p.add(new int[]{x, 1});
        for (int x = 30; x >= 1; x--) p.add(new int[]{x, 1});
        return p;
    }

    public static ArrayList<int[]> buildLeftPath() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int y = 1; y <= 30; y++) p.add(new int[]{1, y});
        for (int y = 30; y >= 1; y--) p.add(new int[]{1, y});
        return p;
    }

    public static ArrayList<int[]> buildRightPath() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int y = 1; y <= 30; y++) p.add(new int[]{30, y});
        for (int y = 30; y >= 1; y--) p.add(new int[]{30, y});
        return p;
    }

    public static ArrayList<int[]> buildMiddlePath() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int x = 1; x <= 30; x++) p.add(new int[]{x, 17});
        for (int x = 30; x >= 1; x--) p.add(new int[]{x, 17});
        return p;
    }

    public static ArrayList<int[]> buildBottomPath() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int x = 1; x <= 30; x++) p.add(new int[]{x, 30});
        for (int x = 30; x >= 1; x--) p.add(new int[]{x, 30});
        return p;
    }
}