//Arman

public class Player {
    private Position position;
    private Size size;
    private int lives;
    private Position spawnPosition;

    public Player(Position pos, Size size) {
        this.position = pos;
        this.size = size;
        this.lives = 3;
        this.spawnPosition = new Position(pos.X(), pos.Y());
    }

    public void moveLeft(Boundary boundary, MazeMap map) {
        int newX = position.X() - 1;
        if (newX >= 0 && !map.isWall(newX, position.Y())) {
            position.setPosition(newX, position.Y());
        }
    }

    public void moveRight(Boundary boundary, MazeMap map) {
        int newX = position.X() + 1;
        if (newX + size.width() <= boundary.getGridWidth() && !map.isWall(newX, position.Y())) {
            position.setPosition(newX, position.Y());
        }
    }

    public void moveUp(Boundary boundary, MazeMap map) {
        int newY = position.Y() - 1;
        if (newY >= 0 && !map.isWall(position.X(), newY)) {
            position.setPosition(position.X(), newY);
        }
    }

    public void moveDown(Boundary boundary, MazeMap map) {
        int newY = position.Y() + 1;
        if (newY + size.height() <= boundary.getGridHeight() && !map.isWall(position.X(), newY)) {
            position.setPosition(position.X(), newY);
        }
    }

    public void loseLife() {
        lives--;
        respawn();
    }

    public void respawn() {
        position.setPosition(spawnPosition.X(), spawnPosition.Y());
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public int getLives() {
        return lives;
    }

    public Position getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public boolean collidesWith(Position otherPos, Size otherSize) {
        return position.X() < otherPos.X() + otherSize.width()
                && position.X() + size.width() > otherPos.X()
                && position.Y() < otherPos.Y() + otherSize.height()
                && position.Y() + size.height() > otherPos.Y();
    }
}