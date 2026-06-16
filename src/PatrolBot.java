import java.util.ArrayList;


//PatrolBot — follows a fixed waypoint path one cell per step.

// Six snake-pattern bots are pre-built that together cover most of
// the maze's open cells (verified wall-free via the actual grid).
//
//  buildPath1  — snake rows  1, 3, 5, 7, 9   (top section)
//  buildPath2  — snake rows 11,13,15,17       (upper-mid)
//  buildPath3  — snake rows 19,21,23,25       (lower-mid)
//  buildPath4  — snake rows 27,29,30          (bottom)
//  buildPath5  — column x=1  up/down          (left edge)
//  buildPath6  — column x=30 up/down          (right edge)

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


    //Bot paths — rows 1,3,5,7,9 (very annoying)

    public static ArrayList<int[]> buildPath1() {
        ArrayList<int[]> p = new ArrayList<>();
        p.add(new int[]{1, 1});p.add(new int[]{2, 1});p.add(new int[]{3, 1});p.add(new int[]{4, 1});p.add(new int[]{5, 1});
        p.add(new int[]{6, 1});p.add(new int[]{7, 1});p.add(new int[]{8, 1});p.add(new int[]{9, 1});p.add(new int[]{10, 1});
        p.add(new int[]{11, 1});p.add(new int[]{12, 1});p.add(new int[]{13, 1});p.add(new int[]{14, 1});p.add(new int[]{15, 1});
        p.add(new int[]{16, 1});p.add(new int[]{17, 1});p.add(new int[]{18, 1});p.add(new int[]{19, 1});p.add(new int[]{20, 1});
        p.add(new int[]{21, 1});p.add(new int[]{22, 1});p.add(new int[]{23, 1});p.add(new int[]{24, 1});p.add(new int[]{25, 1});
        p.add(new int[]{26, 1});p.add(new int[]{27, 1});p.add(new int[]{28, 1});p.add(new int[]{29, 1});p.add(new int[]{30, 1});
        p.add(new int[]{30, 3});p.add(new int[]{28, 3});p.add(new int[]{27, 3});p.add(new int[]{26, 3});p.add(new int[]{24, 3});
        p.add(new int[]{22, 3});p.add(new int[]{21, 3});p.add(new int[]{20, 3});p.add(new int[]{19, 3});p.add(new int[]{17, 3});
        p.add(new int[]{14, 3});p.add(new int[]{12, 3});p.add(new int[]{11, 3});p.add(new int[]{10, 3});p.add(new int[]{9, 3});
        p.add(new int[]{7, 3});p.add(new int[]{5, 3});p.add(new int[]{4, 3});p.add(new int[]{3, 3});p.add(new int[]{1, 3});
        p.add(new int[]{1, 5});p.add(new int[]{3, 5});p.add(new int[]{5, 5});p.add(new int[]{6, 5});p.add(new int[]{7, 5});
        p.add(new int[]{8, 5});p.add(new int[]{9, 5});p.add(new int[]{12, 5});p.add(new int[]{13, 5});p.add(new int[]{14, 5});
        p.add(new int[]{17, 5});p.add(new int[]{18, 5});p.add(new int[]{19, 5});p.add(new int[]{22, 5});p.add(new int[]{23, 5});
        p.add(new int[]{24, 5});p.add(new int[]{25, 5});p.add(new int[]{26, 5});p.add(new int[]{28, 5});p.add(new int[]{30, 5});
        p.add(new int[]{30, 7});p.add(new int[]{28, 7});p.add(new int[]{27, 7});p.add(new int[]{26, 7});p.add(new int[]{25, 7});
        p.add(new int[]{24, 7});p.add(new int[]{23, 7});p.add(new int[]{22, 7});p.add(new int[]{21, 7});p.add(new int[]{20, 7});
        p.add(new int[]{19, 7});p.add(new int[]{18, 7});p.add(new int[]{17, 7});p.add(new int[]{16, 7});p.add(new int[]{15, 7});
        p.add(new int[]{14, 7});p.add(new int[]{13, 7});p.add(new int[]{12, 7});p.add(new int[]{11, 7});p.add(new int[]{10, 7});
        p.add(new int[]{9, 7});p.add(new int[]{8, 7});p.add(new int[]{7, 7});p.add(new int[]{6, 7});p.add(new int[]{5, 7});
        p.add(new int[]{4, 7});p.add(new int[]{3, 7});p.add(new int[]{1, 7});
        p.add(new int[]{1, 9});p.add(new int[]{2, 9});p.add(new int[]{3, 9});p.add(new int[]{4, 9});p.add(new int[]{5, 9});
        p.add(new int[]{6, 9});p.add(new int[]{7, 9});p.add(new int[]{8, 9});p.add(new int[]{9, 9});p.add(new int[]{10, 9});
        p.add(new int[]{11, 9});p.add(new int[]{13, 9});p.add(new int[]{14, 9});p.add(new int[]{15, 9});p.add(new int[]{16, 9});
        p.add(new int[]{17, 9});p.add(new int[]{18, 9});p.add(new int[]{20, 9});p.add(new int[]{21, 9});p.add(new int[]{22, 9});
        p.add(new int[]{23, 9});p.add(new int[]{24, 9});p.add(new int[]{25, 9});p.add(new int[]{26, 9});p.add(new int[]{27, 9});
        p.add(new int[]{28, 9});p.add(new int[]{29, 9});p.add(new int[]{30, 9});
        return p;
    }


    //Bot paths — rows 11,13,15,17 (upper-mid section)

    public static ArrayList<int[]> buildPath2() {
        ArrayList<int[]> p = new ArrayList<>();
        p.add(new int[]{1, 11});p.add(new int[]{2, 11});p.add(new int[]{3, 11});p.add(new int[]{5, 11});p.add(new int[]{6, 11});
        p.add(new int[]{7, 11});p.add(new int[]{8, 11});p.add(new int[]{9, 11});p.add(new int[]{10, 11});p.add(new int[]{11, 11});
        p.add(new int[]{12, 11});p.add(new int[]{13, 11});p.add(new int[]{15, 11});p.add(new int[]{16, 11});p.add(new int[]{17, 11});
        p.add(new int[]{18, 11});p.add(new int[]{19, 11});p.add(new int[]{20, 11});p.add(new int[]{21, 11});p.add(new int[]{22, 11});
        p.add(new int[]{23, 11});p.add(new int[]{24, 11});p.add(new int[]{25, 11});p.add(new int[]{26, 11});p.add(new int[]{27, 11});
        p.add(new int[]{29, 11});p.add(new int[]{30, 11});
        p.add(new int[]{30, 13});p.add(new int[]{29, 13});p.add(new int[]{28, 13});p.add(new int[]{27, 13});p.add(new int[]{26, 13});
        p.add(new int[]{24, 13});p.add(new int[]{23, 13});p.add(new int[]{22, 13});p.add(new int[]{21, 13});p.add(new int[]{20, 13});
        p.add(new int[]{18, 13});p.add(new int[]{16, 13});p.add(new int[]{15, 13});p.add(new int[]{14, 13});p.add(new int[]{13, 13});
        p.add(new int[]{11, 13});p.add(new int[]{10, 13});p.add(new int[]{9, 13});p.add(new int[]{8, 13});p.add(new int[]{7, 13});
        p.add(new int[]{5, 13});p.add(new int[]{4, 13});p.add(new int[]{3, 13});p.add(new int[]{1, 13});
        p.add(new int[]{1, 15});p.add(new int[]{2, 15});p.add(new int[]{3, 15});p.add(new int[]{5, 15});p.add(new int[]{6, 15});
        p.add(new int[]{7, 15});p.add(new int[]{9, 15});p.add(new int[]{10, 15});p.add(new int[]{11, 15});p.add(new int[]{12, 15});
        p.add(new int[]{13, 15});p.add(new int[]{14, 15});p.add(new int[]{15, 15});p.add(new int[]{16, 15});p.add(new int[]{17, 15});
        p.add(new int[]{18, 15});p.add(new int[]{19, 15});p.add(new int[]{20, 15});p.add(new int[]{22, 15});p.add(new int[]{23, 15});
        p.add(new int[]{24, 15});p.add(new int[]{25, 15});p.add(new int[]{26, 15});p.add(new int[]{28, 15});p.add(new int[]{29, 15});
        p.add(new int[]{30, 15});
        p.add(new int[]{30, 17});p.add(new int[]{28, 17});p.add(new int[]{27, 17});p.add(new int[]{26, 17});p.add(new int[]{25, 17});
        p.add(new int[]{24, 17});p.add(new int[]{23, 17});p.add(new int[]{22, 17});p.add(new int[]{21, 17});p.add(new int[]{20, 17});
        p.add(new int[]{19, 17});p.add(new int[]{18, 17});p.add(new int[]{17, 17});p.add(new int[]{16, 17});p.add(new int[]{15, 17});
        p.add(new int[]{14, 17});p.add(new int[]{13, 17});p.add(new int[]{12, 17});p.add(new int[]{11, 17});p.add(new int[]{10, 17});
        p.add(new int[]{9, 17});p.add(new int[]{8, 17});p.add(new int[]{7, 17});p.add(new int[]{6, 17});p.add(new int[]{5, 17});
        p.add(new int[]{4, 17});p.add(new int[]{3, 17});p.add(new int[]{2, 17});p.add(new int[]{1, 17});
        return p;
    }


    //Bot paths — rows 19,21,23,25 (lower-mid section)

    public static ArrayList<int[]> buildPath3() {
        ArrayList<int[]> p = new ArrayList<>();
        p.add(new int[]{1, 19});p.add(new int[]{3, 19});p.add(new int[]{4, 19});p.add(new int[]{5, 19});p.add(new int[]{7, 19});
        p.add(new int[]{9, 19});p.add(new int[]{10, 19});p.add(new int[]{11, 19});p.add(new int[]{12, 19});p.add(new int[]{13, 19});
        p.add(new int[]{14, 19});p.add(new int[]{15, 19});p.add(new int[]{16, 19});p.add(new int[]{17, 19});p.add(new int[]{18, 19});
        p.add(new int[]{19, 19});p.add(new int[]{20, 19});p.add(new int[]{22, 19});p.add(new int[]{23, 19});p.add(new int[]{24, 19});
        p.add(new int[]{26, 19});p.add(new int[]{28, 19});p.add(new int[]{29, 19});p.add(new int[]{30, 19});
        p.add(new int[]{30, 21});p.add(new int[]{29, 21});p.add(new int[]{28, 21});p.add(new int[]{26, 21});p.add(new int[]{25, 21});
        p.add(new int[]{24, 21});p.add(new int[]{23, 21});p.add(new int[]{22, 21});p.add(new int[]{21, 21});p.add(new int[]{20, 21});
        p.add(new int[]{19, 21});p.add(new int[]{18, 21});p.add(new int[]{17, 21});p.add(new int[]{15, 21});p.add(new int[]{14, 21});
        p.add(new int[]{13, 21});p.add(new int[]{11, 21});p.add(new int[]{10, 21});p.add(new int[]{9, 21});p.add(new int[]{7, 21});
        p.add(new int[]{6, 21});p.add(new int[]{5, 21});p.add(new int[]{3, 21});p.add(new int[]{2, 21});p.add(new int[]{1, 21});
        p.add(new int[]{1, 23});p.add(new int[]{3, 23});p.add(new int[]{4, 23});p.add(new int[]{5, 23});p.add(new int[]{6, 23});
        p.add(new int[]{7, 23});p.add(new int[]{8, 23});p.add(new int[]{9, 23});p.add(new int[]{10, 23});p.add(new int[]{11, 23});
        p.add(new int[]{12, 23});p.add(new int[]{13, 23});p.add(new int[]{14, 23});p.add(new int[]{15, 23});p.add(new int[]{16, 23});
        p.add(new int[]{17, 23});p.add(new int[]{18, 23});p.add(new int[]{19, 23});p.add(new int[]{20, 23});p.add(new int[]{21, 23});
        p.add(new int[]{22, 23});p.add(new int[]{23, 23});p.add(new int[]{24, 23});p.add(new int[]{26, 23});p.add(new int[]{27, 23});
        p.add(new int[]{28, 23});p.add(new int[]{30, 23});
        p.add(new int[]{30, 25});p.add(new int[]{29, 25});p.add(new int[]{28, 25});p.add(new int[]{27, 25});p.add(new int[]{26, 25});
        p.add(new int[]{25, 25});p.add(new int[]{24, 25});p.add(new int[]{22, 25});p.add(new int[]{21, 25});p.add(new int[]{20, 25});
        p.add(new int[]{18, 25});p.add(new int[]{17, 25});p.add(new int[]{16, 25});p.add(new int[]{15, 25});p.add(new int[]{14, 25});
        p.add(new int[]{13, 25});p.add(new int[]{12, 25});p.add(new int[]{11, 25});p.add(new int[]{9, 25});p.add(new int[]{8, 25});
        p.add(new int[]{7, 25});p.add(new int[]{6, 25});p.add(new int[]{5, 25});p.add(new int[]{3, 25});p.add(new int[]{2, 25});
        p.add(new int[]{1, 25});
        return p;
    }


    //Bot paths — rows 27,29,30 (bottom section)

    public static ArrayList<int[]> buildPath4() {
        ArrayList<int[]> p = new ArrayList<>();
        p.add(new int[]{1, 27});p.add(new int[]{3, 27});p.add(new int[]{4, 27});p.add(new int[]{5, 27});p.add(new int[]{7, 27});
        p.add(new int[]{8, 27});p.add(new int[]{9, 27});p.add(new int[]{10, 27});p.add(new int[]{11, 27});p.add(new int[]{13, 27});
        p.add(new int[]{15, 27});p.add(new int[]{17, 27});p.add(new int[]{18, 27});p.add(new int[]{19, 27});p.add(new int[]{20, 27});
        p.add(new int[]{21, 27});p.add(new int[]{22, 27});p.add(new int[]{23, 27});p.add(new int[]{24, 27});p.add(new int[]{25, 27});
        p.add(new int[]{26, 27});p.add(new int[]{27, 27});p.add(new int[]{29, 27});p.add(new int[]{30, 27});
        p.add(new int[]{30, 29});p.add(new int[]{29, 29});p.add(new int[]{28, 29});p.add(new int[]{27, 29});p.add(new int[]{25, 29});
        p.add(new int[]{24, 29});p.add(new int[]{23, 29});p.add(new int[]{21, 29});p.add(new int[]{20, 29});p.add(new int[]{19, 29});
        p.add(new int[]{17, 29});p.add(new int[]{16, 29});p.add(new int[]{15, 29});p.add(new int[]{13, 29});p.add(new int[]{12, 29});
        p.add(new int[]{11, 29});p.add(new int[]{10, 29});p.add(new int[]{9, 29});p.add(new int[]{7, 29});p.add(new int[]{6, 29});
        p.add(new int[]{5, 29});p.add(new int[]{3, 29});p.add(new int[]{2, 29});p.add(new int[]{1, 29});
        p.add(new int[]{1, 30});p.add(new int[]{2, 30});p.add(new int[]{3, 30});p.add(new int[]{4, 30});p.add(new int[]{5, 30});
        p.add(new int[]{6, 30});p.add(new int[]{7, 30});p.add(new int[]{8, 30});p.add(new int[]{9, 30});p.add(new int[]{10, 30});
        p.add(new int[]{11, 30});p.add(new int[]{12, 30});p.add(new int[]{13, 30});p.add(new int[]{14, 30});p.add(new int[]{15, 30});
        p.add(new int[]{16, 30});p.add(new int[]{17, 30});p.add(new int[]{18, 30});p.add(new int[]{19, 30});p.add(new int[]{20, 30});
        p.add(new int[]{21, 30});p.add(new int[]{22, 30});p.add(new int[]{23, 30});p.add(new int[]{24, 30});p.add(new int[]{25, 30});
        p.add(new int[]{26, 30});p.add(new int[]{27, 30});p.add(new int[]{28, 30});p.add(new int[]{29, 30});p.add(new int[]{30, 30});
        return p;
    }


    //Column x=1 — left edge patrol (up and back down)

    public static ArrayList<int[]> buildPath5() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int y = 1; y <= 30; y++) p.add(new int[]{1, y});
        for (int y = 29; y >= 1; y--) p.add(new int[]{1, y});
        return p;
    }


    //Column x=30 — right edge patrol (up and back down)

    public static ArrayList<int[]> buildPath6() {
        ArrayList<int[]> p = new ArrayList<>();
        for (int y = 1; y <= 30; y++) p.add(new int[]{30, y});
        for (int y = 29; y >= 1; y--) p.add(new int[]{30, y});
        return p;
    }
}