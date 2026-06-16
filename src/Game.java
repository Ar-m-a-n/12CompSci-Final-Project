import java.awt.*;
import java.util.ArrayList;

public class Game {

    private GridCanvas canvas;
    private InputState inputState;
    private Boundary boundary;

    private MazeMap mazeMap;
    private Player player;

    // Six PatrolBots — snake-pattern paths covering ~82 % of the maze
    private ArrayList<Bot> bots;
    private ArrayList<Color> botColors;

    private ArrayList<KeyItem> keys;
    private KeyInventory inventory;

    private boolean doorUnlocked;
    private boolean gameWon;
    private boolean gameOver;

    private int playerMoveTimer = 0;
    private int playerMoveDelay = 3;

    private int invincibleTimer    = 0;
    private int invincibleDuration = 25;

    private boolean ePressedLastFrame = false;

    public Game() {
        boundary = new Boundary(32, 32);
        mazeMap  = new MazeMap();

        player = new Player(new Position(1, 1), new Size(1, 1));

        // Six snake-pattern bots
        bots      = new ArrayList<>();
        botColors = new ArrayList<>();

        //Bot 1 top section (rows 1,3,5,7,9), fast
        bots.add(new PatrolBot(new Position(1, 1),  new Size(1, 1), 2, PatrolBot.buildPath1()));
        botColors.add(Color.RED);

        //Bot 2 upper-mid (rows 11,13,15,17)
        bots.add(new PatrolBot(new Position(1, 11), new Size(1, 1), 3, PatrolBot.buildPath2()));
        botColors.add(Color.RED);

        //Bot 3 lower-mid (rows 19,21,23,25)
        bots.add(new PatrolBot(new Position(1, 19), new Size(1, 1), 3, PatrolBot.buildPath3()));
        botColors.add(Color.RED);

        //Bot 4 bottom (rows 27,29,30)
        bots.add(new PatrolBot(new Position(1, 27), new Size(1, 1), 2, PatrolBot.buildPath4()));
        botColors.add(Color.RED);

        //Bot 5 left column x=1
        bots.add(new PatrolBot(new Position(1, 1),  new Size(1, 1), 2, PatrolBot.buildPath5()));
        botColors.add(Color.RED);

        //Bot 6 right column x=30
        bots.add(new PatrolBot(new Position(30, 1), new Size(1, 1), 2, PatrolBot.buildPath6()));
        botColors.add(Color.RED);

        //Keys
        keys = new ArrayList<>();
        keys.add(new KeyItem(1, new Position(1,  3)));
        keys.add(new KeyItem(2, new Position(30, 3)));
        keys.add(new KeyItem(3, new Position(1,  12)));
        keys.add(new KeyItem(4, new Position(30, 12)));
        keys.add(new KeyItem(5, new Position(1,  21)));
        keys.add(new KeyItem(6, new Position(30, 21)));

        inventory = new KeyInventory(keys.size());

        doorUnlocked = false;
        gameWon      = false;
        gameOver     = false;

        //Smaller window: 14 px per cell
        canvas = new GridCanvas(boundary, 14, "House Maze — Sort the Keys!");
        canvas.showInWindow();
    }

    public void run() {
        while (true) {
            updateGameState();
            redrawVisuals();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateGameState() {
        if (gameOver || gameWon) return;

        inputState = canvas.getInputState();
        inventory.tickFeedback();

        //Player movement
        playerMoveTimer++;
        if (playerMoveTimer >= playerMoveDelay) {
            playerMoveTimer = 0;
            movePlayer();
        }

        int px = player.getPosition().X();
        int py = player.getPosition().Y();

        //Key pickups
        for (KeyItem key : keys) {
            if (!key.isCollected()
                    && key.getPosition().X() == px
                    && key.getPosition().Y() == py) {
                key.collect();
                inventory.pickUp(key.getNumber());
            }
        }

        //Deposit slot  press E
        boolean eNow = inputState.isEPressed();
        if (mazeMap.isDeposit(px, py) && eNow && !ePressedLastFrame) {
            KeyInventory.DepositResult result = inventory.tryDeposit();
            if (result == KeyInventory.DepositResult.WRONG && invincibleTimer == 0) {
                player.loseLife();
                invincibleTimer = invincibleDuration;
                if (!player.isAlive()) { gameOver = true; return; }
            }
            if (inventory.isDoorUnlocked()) doorUnlocked = true;
        }
        ePressedLastFrame = eNow;

        //Door entry
        if (doorUnlocked && mazeMap.isDoor(px, py)) {
            gameWon = true;
            return;
        }

        //Update all bots and check collisions
        if (invincibleTimer > 0) invincibleTimer--;

        for (Bot bot : bots) {
            bot.update(mazeMap);
            if (invincibleTimer == 0 && bot.collidesWith(player)) {
                player.loseLife();
                invincibleTimer = invincibleDuration;
                if (!player.isAlive()) { gameOver = true; return; }
            }
        }
    }

    private void movePlayer() {
        if (inputState.isLeftPressed())  player.moveLeft(boundary, mazeMap);
        if (inputState.isRightPressed()) player.moveRight(boundary, mazeMap);
        if (inputState.isUpPressed())    player.moveUp(boundary, mazeMap);
        if (inputState.isDownPressed())  player.moveDown(boundary, mazeMap);
    }

    private void redrawVisuals() {
        canvas.clear();

        //Maze
        int[][] grid = mazeMap.getGrid();
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int cell = grid[y][x];
                if (cell == MazeMap.WALL) {
                    canvas.drawRectangle(new Position(x, y), new Size(1, 1),
                            new Color(50, 50, 110), GridCanvas.DrawStyle.FILLED);
                } else if (cell == MazeMap.DOOR) {
                    Color dc = doorUnlocked ? new Color(0, 210, 90) : new Color(160, 40, 40);
                    canvas.drawRectangle(new Position(x, y), new Size(1, 1), dc, GridCanvas.DrawStyle.FILLED);
                } else if (cell == MazeMap.DEPOSIT) {
                    canvas.drawRectangle(new Position(x, y), new Size(1, 1),
                            new Color(160, 80, 220), GridCanvas.DrawStyle.FILLED);
                }
            }
        }

        //Keys
        for (KeyItem key : keys) {
            if (!key.isCollected()) {
                canvas.drawOval(key.getPosition(), new Size(1, 1),
                        new Color(255, 210, 0), GridCanvas.DrawStyle.FILLED);
            }
        }

        //Player (flickers while invincible)
        boolean showPlayer = (invincibleTimer == 0) || (invincibleTimer % 4 < 2);
        if (showPlayer) {
            canvas.drawRectangle(player.getPosition(), player.getSize(),
                    Color.CYAN, GridCanvas.DrawStyle.FILLED);
        }

        //Bots with individual colors
        for (int i = 0; i < bots.size(); i++) {
            Bot bot = bots.get(i);
            canvas.drawOval(bot.getPosition(), bot.getSize(),
                    botColors.get(i), GridCanvas.DrawStyle.FILLED);
        }

        drawHUD();
        drawDepositFeedback();

        if (gameWon) {
            drawOverlay(new Color(0, 180, 80, 200));
        } else if (gameOver) {
            drawOverlay(new Color(180, 0, 0, 200));
        }

        canvas.redraw();
    }

    private void drawHUD() {
        for (int i = 0; i < inventory.getTotalKeys(); i++) {
            int keyNum = i + 1;
            boolean deposited = inventory.getDeposited().contains(keyNum);
            boolean inHand    = inventory.getInHand().contains(keyNum);
            Color c;
            GridCanvas.DrawStyle style;
            if (deposited) {
                c = new Color(255, 210, 0); style = GridCanvas.DrawStyle.FILLED;
            } else if (inHand) {
                c = new Color(200, 160, 0); style = GridCanvas.DrawStyle.OUTLINED;
            } else {
                c = new Color(80, 70, 30);  style = GridCanvas.DrawStyle.OUTLINED;
            }
            canvas.drawOval(new Position(i * 2 + 1, 0), new Size(1, 1), c, style);
        }

        for (int i = 0; i < player.getLives(); i++) {
            canvas.drawRectangle(new Position(29 - i, 0), new Size(1, 1),
                    Color.RED, GridCanvas.DrawStyle.FILLED);
        }

        int next = inventory.getNextExpected();
        if (next <= inventory.getTotalKeys()) {
            canvas.drawOval(new Position(14, 0), new Size(1, 1),
                    Color.CYAN, GridCanvas.DrawStyle.OUTLINED);
        }
    }

    private void drawDepositFeedback() {
        if (inventory.getFeedbackTimer() <= 0) return;
        KeyInventory.DepositResult result = inventory.getLastResult();
        Color flash;
        if      (result == KeyInventory.DepositResult.SUCCESS) flash = new Color(0, 255, 100, 160);
        else if (result == KeyInventory.DepositResult.WRONG)   flash = new Color(255, 50,  50,  160);
        else return;
        for (int dx = -1; dx <= 1; dx++) {
            canvas.drawRectangle(new Position(17 + dx, 30), new Size(1, 1),
                    flash, GridCanvas.DrawStyle.FILLED);
        }
    }

    private void drawOverlay(Color color) {
        for (int y = 13; y <= 18; y++)
            for (int x = 2; x <= 29; x++)
                canvas.drawRectangle(new Position(x, y), new Size(1, 1),
                        color, GridCanvas.DrawStyle.FILLED);
    }
}
