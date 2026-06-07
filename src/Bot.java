import java.util.ArrayList;

/**
 * Bot patrols a fixed looping path through the maze.
 * Path is defined as a list of waypoints it travels between in order.
 */
public class Bot {
    private Position position;
    private Size size;
    private ArrayList<int[]> path; // list of [x, y] waypoints
    private int pathIndex;
    private int moveTimer;
    private int moveDelay; // frames between moves

    public Bot(Position startPos, Size size, int moveDelay) {
        this.position = startPos;
        this.size = size;
        this.moveDelay = moveDelay;
        this.moveTimer = 0;
        this.pathIndex = 0;
        this.path = buildPath();
    }

    /**
     * A looping patrol route through open corridors of the maze.
     */
    private ArrayList<int[]> buildPath() {
        ArrayList<int[]> p = new ArrayList<>();

        // Outer perimeter patrol + inner loops
        // Top corridor
        for (int x = 1; x <= 14; x++) p.add(new int[]{x, 1});
        // Right of top
        for (int x = 17; x <= 30; x++) p.add(new int[]{x, 1});
        // Right corridor down
        for (int y = 1; y <= 6; y++) p.add(new int[]{30, y});
        // Middle right
        for (int y = 6; y <= 9; y++) p.add(new int[]{30, y});
        // Cross right side
        for (int x = 30; x >= 23; x--) p.add(new int[]{x, 9});
        // Down middle-right
        for (int y = 9; y <= 15; y++) p.add(new int[]{23, y});
        // Bottom-right area
        for (int x = 23; x <= 30; x++) p.add(new int[]{x, 15});
        for (int y = 15; y <= 30; y++) p.add(new int[]{30, y});
        // Bottom corridor
        for (int x = 30; x >= 17; x--) p.add(new int[]{x, 30});
        for (int x = 14; x >= 1; x--) p.add(new int[]{x, 30});
        // Left corridor up
        for (int y = 30; y >= 23; y--) p.add(new int[]{1, y});
        // Inner left segment
        for (int x = 1; x <= 9; x++) p.add(new int[]{x, 23});
        for (int y = 23; y >= 17; y--) p.add(new int[]{9, y});
        for (int x = 9; x >= 1; x--) p.add(new int[]{x, 17});
        // Continue left up
        for (int y = 17; y >= 9; y--) p.add(new int[]{1, y});
        for (int x = 1; x <= 9; x++) p.add(new int[]{x, 9});
        for (int y = 9; y >= 1; y--) p.add(new int[]{9, y});
        for (int x = 9; x >= 1; x--) p.add(new int[]{x, 1});

        return p;
    }

    public void update() {
        moveTimer++;
        if (moveTimer >= moveDelay) {
            moveTimer = 0;
            advanceAlongPath();
        }
    }

    private void advanceAlongPath() {
        pathIndex = (pathIndex + 1) % path.size();
        int[] next = path.get(pathIndex);
        position.setPosition(next[0], next[1]);
    }

    public Position getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public boolean collidesWith(Player player) {
        Position pp = player.getPosition();
        Size ps = player.getSize();
        return position.X() < pp.X() + ps.width()
                && position.X() + size.width() > pp.X()
                && position.Y() < pp.Y() + ps.height()
                && position.Y() + size.height() > pp.Y();
    }
}