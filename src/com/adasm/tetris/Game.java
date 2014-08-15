package com.adasm.tetris;

public class Game {
    public static final String textNewGame[] = {
        "New GAME", "START", "GO, GO, GO!", "NOW FOCUS"
    };
    public static final String textGameOver[] = {
            "Ohh noo...", "Game over...", "Soo close..."
    };
    public static final String textScoring[] = {
            "Well done!", "Wooow!", "Keep going!", "Yeeey!", "Yeees!",
            "That's it!", "Look at that!", "Impossible",
    };
    public static final String textLevelUp[] = {
            "Level up!", "And now faster!"
    };


    public static boolean over = false;
    public static final int moveSpeed = 6, rotateSpeed = 12, createSpeed = 18;
    public static int leftTick = 0, rightTick = 0, rotateTick = 0, releaseTick = 0;
    public static int score = 0, level = 0;
    public static int scoringPoints = 0;
    public static boolean scoring = false;
    public static final int boardSizeX = 10;
    public static final int boardSizeY = 19;
    public static Block blocks[][] = new Block[boardSizeX][boardSizeY];

    public static Integer shapeLock = new Integer(0);
    public static Shape shape = null, nextShape = new Shape(2, 2, 0.3f);

    public static void scoreRow(int y) {
        for (int x = 0; x < Game.boardSizeX; x++) {
            Game.blocks[x][y].scored = true;
        }
    }

    public static void checkScoring(int tick) {
        scoringPoints = 0;
        for(int y = Game.boardSizeY - 1; y >= 0; y--) {
            boolean rowScored = true;
            for (int x = 0; x < Game.boardSizeX; x++) {
                if(Game.blocks[x][y] == null){ rowScored = false; break; }
            }
            if(rowScored) { scoringPoints++; scoring = true; scoreRow(y); }
        }

        if(scoringPoints > 0) {
            Window.addText(new Text(Game.textScoring[Util.randomInteger()%Game.textScoring.length], 0.05f));
            Window.addParticles();
        }
    }

    public static void releaseShape(int tick) {
        for (int i = 0; i < 4; i++) {
            shape.blocks[i].addToBoard();
            shape.blocks[i] = null;
        }
        shape = null;
        releaseTick = 0;
        checkScoring(tick);
    }

    public static void releaseRows() {
        for(int y = Game.boardSizeY - 1; y >= 0; y--) {
            boolean rowToRelease = true;
            for (int x = 0; x < Game.boardSizeX; x++) {
                if(Game.blocks[x][y] == null){ rowToRelease = false; break; }
            }
            if(rowToRelease) { releaseRow(y); break; }
        }
    }

    public static void releaseRow(int rowY) {
        for(int y = rowY; y >= 0; y--) {
            for (int x = 0; x < Game.boardSizeX; x++) {
                Game.blocks[x][y] = y > 0 && Game.blocks[x][y-1] != null ? Game.blocks[x][y-1].moveDown() : null;
            }
        }
    }

    public static void reset() {
        Util.sleep(3000);
        score = 0;
        level = 0;
        scoring = false;
        synchronized (blocks) {
            blocks = new Block[boardSizeX][boardSizeY];
        }
        over = false;
    }

    public static void update(int tick) {

        if(scoring) {
            scoring = false;
            score += scoringPoints;
            synchronized (blocks) {
                while(scoringPoints-- > 0)
                    releaseRows();
            }

            int oldLevel = level;
            level = (int) score / 2;

            if(level > oldLevel) {
                Window.addText(new Text(Game.textLevelUp[Util.randomInteger()%Game.textLevelUp.length], 0.1f, 0.5f));
            }
        }
        Loop.setGravityTick(level);

        synchronized (shapeLock) {
            if (shape == null && releaseTick ++ > createSpeed) {
                shape = new Shape(boardSizeX / 2 - 1, -2, 1);
                nextShape.type = Shape.nextType;
                nextShape.rebuildShape();
            } else if(shape != null) {
                if(Window.keyLeft) {
                    if(leftTick % Game.moveSpeed == 0)
                        shape.shiftLeft();
                    leftTick++;

                } else leftTick = 0;
                if(Window.keyRight) {
                    if(rightTick % Game.moveSpeed == 0)
                        shape.shiftRight();
                    rightTick++;

                } else rightTick = 0;

                if(Window.keyUp) {
                    if(rotateTick % Game.rotateSpeed == 0)
                        shape.rotate(true);
                    rotateTick++;
                } else rotateTick = 0;

                boolean result = shape.update(tick);
                if (result == false) {
                    releaseShape(tick);
                }
            }
        }
    }

}
