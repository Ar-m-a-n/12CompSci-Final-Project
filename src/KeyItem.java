/**
 * Represents a numbered key item on the map.
 * Keys must be deposited in ascending order (1, 2, 3...) to unlock the door.
 */
public class KeyItem {
    private int number;       // The key's number (1-6)
    private Position position; // Where it is on the map
    private boolean collected; // Has the player picked it up?

    public KeyItem(int number, Position position) {
        this.number = number;
        this.position = position;
        this.collected = false;
    }

    public int getNumber() { return number; }
    public Position getPosition() { return position; }
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }
}
