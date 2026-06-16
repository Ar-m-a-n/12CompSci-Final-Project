import java.util.ArrayList;

//
//KeyInventory — manages the player's collected keys and deposit order.
//
//Sorting mechanic:
//Keys are scattered randomly numbered 1-6.
//The player collects them in any order (stored unsorted in hand).
//At the deposit slot, the player presses E to deposit.
//The game checks if the lowest-numbered key in hand matches
// the next expected deposit number (insertion-sort style).
// If correct → deposited, next expected increments.
// If wrong    → rejected, player loses a life as a penalty.
// Door unlocks only when all 6 keys are deposited in order 1→6.

public class KeyInventory {

    private ArrayList<Integer> inHand;       // Keys the player is carrying (unsorted)
    private ArrayList<Integer> deposited;    // Keys successfully deposited in order
    private int nextExpected;                // The next key number that must be deposited
    private int totalKeys;

    // Feedback state for drawing
    public enum DepositResult { NONE, SUCCESS, WRONG }
    private DepositResult lastResult = DepositResult.NONE;
    private int feedbackTimer = 0;
    private static final int FEEDBACK_DURATION = 20; // frames to show feedback

    public KeyInventory(int totalKeys) {
        this.totalKeys = totalKeys;
        this.inHand = new ArrayList<>();
        this.deposited = new ArrayList<>();
        this.nextExpected = 1;
    }

    //Called when player walks over a key.
    public void pickUp(int keyNumber) {
        inHand.add(keyNumber);
    }


    //Called when player presses E at the deposit slot.
    //Finds the smallest key in hand and checks if it's the next expected one.
    //Returns the deposit result.

    public DepositResult tryDeposit() {
        if (inHand.isEmpty()) {
            lastResult = DepositResult.NONE;
            feedbackTimer = 0;
            return DepositResult.NONE;
        }

        //Find the minimum key in hand (insertion-sort style: always deposit smallest available)
        int minKey = inHand.get(0);
        int minIndex = 0;
        for (int i = 1; i < inHand.size(); i++) {
            if (inHand.get(i) < minKey) {
                minKey = inHand.get(i);
                minIndex = i;
            }
        }

        if (minKey == nextExpected) {
            // Correct! Deposit it.
            inHand.remove(minIndex);
            deposited.add(minKey);
            nextExpected++;
            lastResult = DepositResult.SUCCESS;
        } else {
            // Wrong order — the min key in hand is not the next expected one.
            // This means the player is missing a lower key. Penalty.
            lastResult = DepositResult.WRONG;
        }

        feedbackTimer = FEEDBACK_DURATION;
        return lastResult;
    }

    public void tickFeedback() {
        if (feedbackTimer > 0) feedbackTimer--;
        else lastResult = DepositResult.NONE;
    }

    public boolean isDoorUnlocked() {
        return deposited.size() >= totalKeys;
    }

    public int getDepositedCount() { return deposited.size(); }
    public int getInHandCount()    { return inHand.size(); }
    public int getTotalKeys()      { return totalKeys; }
    public ArrayList<Integer> getInHand()    { return inHand; }
    public ArrayList<Integer> getDeposited() { return deposited; }
    public DepositResult getLastResult()     { return lastResult; }
    public int getFeedbackTimer()            { return feedbackTimer; }
    public int getNextExpected()             { return nextExpected; }
}