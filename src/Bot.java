//Arman
//Bot — abstract superclass for all enemy bots.
//Handles:
//Position and size storage
// Player collision detection (AABB)
// Wall-checking utility for subclasses
//Subclasses must implement update(MazeMap map).

public abstract class Bot {

    protected Position position;
    protected Size size;

    public Bot(Position startPos, Size size) {
        this.position = startPos;
        this.size = size;
    }

    // Called every frame. Subclasses define their movement logic.
    public abstract void update(MazeMap map);

    // AABB collision check against the player.
    public boolean collidesWith(Player player) {
        Position pp = player.getPosition();
        Size ps = player.getSize();
        return position.X() < pp.X() + ps.width()
                && position.X() + size.width() > pp.X()
                && position.Y() < pp.Y() + ps.height()
                && position.Y() + size.height() > pp.Y();
    }

    // Returns true if the given grid cell is passable (not a wall).
    protected boolean canMoveTo(int x, int y, MazeMap map) {
        return !map.isWall(x, y);
    }

    public Position getPosition() { return position; }
    public Size getSize()         { return size; }
}