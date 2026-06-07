import java.awt.*;
import java.util.ArrayList;

public class Game {

    private GridCanvas canvas;
    private InputState inputState;
    private Boundary boundary;

    private MazeMap mazeMap;
    private Player player;
    private Bot bot;

    private int totalKeys;
    private int keysCollected;
    private boolean doorUnlocked;
    private boolean gameWon;
    private boolean gameOver;

    // Controls how often the player moves (in frames)
    private int playerMoveTimer = 0;
    private int playerMoveDelay = 3;

    // Invincibility frames after getting hit
    private int invincibleTimer = 0;
    private int invincibleDuration = 20;

    public Game() {
        boundary = new Boundary(32, 32);
        mazeMap = new MazeMap();

        // Player spawns at top-left open area
        player = new Player(new Position(1, 1), new Size(1, 1));

        // Bot patrols the maze corridors, moves every 4 frames
        bot = new Bot(new Position(1, 9), new Size(1, 1), 4);

        totalKeys = mazeMap.countKeys();
        keysCollected = 0;
        doorUnlocked = false;
        gameWon = false;
        gameOver = false;

        canvas = new GridCanvas(boundary, 20, "House Maze - Collect All Keys!");
        canvas.showInWindow();
    }

    public void run() {
        while (true) {
            updateGameState();
            redrawVisuals();
            try {
                Thread.sleep(50); // ~20 FPS
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateGameState() {
        if (gameOver || gameWon) return;

        inputState = canvas.getInputState();

        // Player movement with delay (so it's not too fast)
        playerMoveTimer++;
        if (playerMoveTimer >= playerMoveDelay) {
            playerMoveTimer = 0;
            movePlayer();
        }

        // Check key pickup
        int px = player.getPosition().X();
        int py = player.getPosition().Y();
        if (mazeMap.isKey(px, py)) {
            mazeMap.setCell(px, py, MazeMap.OPEN);
            keysCollected++;
            if (keysCollected >= totalKeys) {
                doorUnlocked = true;
            }
        }

        // Check door
        if (doorUnlocked && mazeMap.isDoor(px, py)) {
            gameWon = true;
            return;
        }

        // Update bot
        bot.update();

        // Check bot collision (with invincibility frames)
        if (invincibleTimer > 0) {
            invincibleTimer--;
        } else if (bot.collidesWith(player)) {
            player.loseLife();
            invincibleTimer = invincibleDuration;
            if (!player.isAlive()) {
                gameOver = true;
            }
        }
    }

    private void movePlayer() {
        if (inputState.isLeftPressed()) {
            player.moveLeft(boundary, mazeMap);
        }
        if (inputState.isRightPressed()) {
            player.moveRight(boundary, mazeMap);
        }
        if (inputState.isUpPressed()) {
            player.moveUp(boundary, mazeMap);
        }
        if (inputState.isDownPressed()) {
            player.moveDown(boundary, mazeMap);
        }
    }

    private void redrawVisuals() {
        canvas.clear();

        // Draw maze
        int[][] grid = mazeMap.getGrid();
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int cell = grid[y][x];
                if (cell == MazeMap.WALL) {
                    canvas.drawRectangle(new Position(x, y), new Size(1, 1), new Color(60, 60, 120), GridCanvas.DrawStyle.FILLED);
                } else if (cell == MazeMap.KEY) {
                    canvas.drawOval(new Position(x, y), new Size(1, 1), new Color(255, 215, 0), GridCanvas.DrawStyle.FILLED);
                } else if (cell == MazeMap.DOOR) {
                    Color doorColor = doorUnlocked ? new Color(0, 200, 80) : new Color(180, 50, 50);
                    canvas.drawRectangle(new Position(x, y), new Size(1, 1), doorColor, GridCanvas.DrawStyle.FILLED);
                }
            }
        }

        // Draw player (flash when invincible)
        boolean showPlayer = (invincibleTimer == 0) || (invincibleTimer % 4 < 2);
        if (showPlayer) {
            canvas.drawRectangle(player.getPosition(), player.getSize(), Color.CYAN, GridCanvas.DrawStyle.FILLED);
        }

        // Draw bot
        canvas.drawOval(bot.getPosition(), bot.getSize(), Color.RED, GridCanvas.DrawStyle.FILLED);

        // Draw HUD: lives and keys
        drawHUD();

        // Draw overlay if won or game over
        if (gameWon) {
            drawOverlay("YOU ESCAPED! YOU WIN!", new Color(0, 200, 80, 180));
        } else if (gameOver) {
            drawOverlay("GAME OVER - No Lives Left!", new Color(200, 0, 0, 180));
        }

        canvas.redraw();
    }

    /**
     * Draws life hearts and key count as colored blocks in corners.
     */
    private void drawHUD() {
        // Lives: red squares top-right corner (each = 1 life)
        for (int i = 0; i < player.getLives(); i++) {
            canvas.drawRectangle(new Position(29 - i, 0), new Size(1, 1), Color.RED, GridCanvas.DrawStyle.FILLED);
        }

        // Keys collected: yellow ovals along top-left
        for (int i = 0; i < keysCollected; i++) {
            canvas.drawOval(new Position(i, 0), new Size(1, 1), new Color(255, 215, 0), GridCanvas.DrawStyle.FILLED);
        }

        // Remaining keys: dark gold outlines
        for (int i = keysCollected; i < totalKeys; i++) {
            canvas.drawOval(new Position(i, 0), new Size(1, 1), new Color(100, 80, 0), GridCanvas.DrawStyle.OUTLINED);
        }
    }

    /**
     * Draws a colored overlay rectangle as a banner across the middle.
     */
    private void drawOverlay(String message, Color color) {
        // Draw banner rows
        for (int y = 13; y <= 18; y++) {
            for (int x = 0; x < 32; x++) {
                canvas.drawRectangle(new Position(x, y), new Size(1, 1), color, GridCanvas.DrawStyle.FILLED);
            }
        }
    }
}
