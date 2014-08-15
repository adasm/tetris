package com.adasm.tetris;

import java.awt.*;

public class Shape {
    public static int nextType = 0;
    public static final int shapes[][][][] = {
            // O
            { { { 0, 0}, { 1, 0}, { 0,-1}, { 1,-1} }, { { 0, 0}, { 1, 0}, { 0,-1}, { 1,-1} }, { { 0, 0}, { 1, 0}, { 0,-1}, { 1,-1} }, { { 0, 0}, { 1, 0}, { 0,-1}, { 1,-1} } },
            // T
            { { { 0, 0}, {-1, 0}, { 1, 0}, { 0, 1} }, { { 0, 0}, { 0,-1}, { 0, 1}, {-1, 0} }, { { 0, 0}, {-1, 0}, { 1, 0}, { 0,-1} }, { { 0, 0}, { 0, 1}, { 0,-1}, { 1, 0} } },
            // I
            { { {-1, 0}, { 0, 0}, { 1, 0}, { 2, 0} }, { { 0,-1}, { 0, 0}, { 0, 1}, { 0, 2} }, { {-1, 0}, { 0, 0}, { 1, 0}, { 2, 0} }, { { 0,-1}, { 0, 0}, { 0, 1}, { 0, 2} } },
            // S
            { { { 0, 0}, { 1, 0}, { 0, 1}, {-1, 1} }, { { 0, 0}, { 0,-1}, { 1, 0}, { 1, 1} }, { { 0, 0}, { 1, 0}, { 0, 1}, {-1, 1} }, { { 0, 0}, { 0,-1}, { 1, 0}, { 1, 1} }, },
            // Z
            { { { 0, 0}, { 0,-1}, {-1,-1}, { 1, 0} }, { { 0, 0}, { 0, 1}, { 1, 0}, { 1,-1} }, { { 0, 0}, { 0,-1}, {-1,-1}, { 1, 0} }, { { 0, 0}, { 0, 1}, { 1, 0}, { 1,-1} } },
            // L
            { { { 0, 0}, { 1, 0}, {-1, 0}, {-1, 1} }, { { 0, 0}, { 0, 1}, { 0,-1}, {-1,-1} }, { { 0, 0}, {-1, 0}, { 1, 0}, { 1,-1} }, { { 0, 0}, { 0,-1}, { 0, 1}, { 1, 1} } },
            // J
            { { { 0, 0}, {-1, 0}, { 1, 0}, { 1, 1} }, { { 0, 0}, { 0, 1}, { 0,-1}, {-1, 1} }, { { 0, 0}, { 1, 0}, {-1, 0}, {-1,-1} }, { { 0, 0}, { 0, 1}, { 0,-1}, { 1,-1} } }
    };

    public int type = 0;
    public Color color;
    public int x, y, rotation;
    public Block blocks[];

    public Shape(int x, int y, float alpha) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.type = nextType;
        nextType = Util.randomInteger() % 7;


        color = Util.randomTransColor(alpha);
        buildShape();
    }

    public void buildShape() {
        blocks = new Block[4];
        for(int i = 0; i < 4; i++) {
            blocks[i] = new Block(shapes[type][rotation][i][0], shapes[type][rotation][i][1], color, this);
        }
    }

    public void rebuildShape() {
        for(int i = 0; i < 4; i++) {
            blocks[i].x = shapes[type][rotation][i][0];
            blocks[i].y = shapes[type][rotation][i][1];
        }
    }


    public void rotate(boolean right) {
        int prevRotation = rotation;
        rotation = (rotation + (right ? 1 : -1)) % 4;
        rebuildShape();
        if(collide()) {
            rotation = prevRotation;
            rebuildShape();
        }
    }

    public boolean collide() {
        for(int i = 0; i < 4; i++) {
            int tx = blocks[i].getX();
            int ty = blocks[i].getY();

            if(tx < 0 || tx > Game.boardSizeX - 1)
                return true;
            if(ty < 0) continue;
            if(ty > Game.boardSizeY - 1 || Game.blocks[tx][ty] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean collideOnGravity() {
        y++;
        boolean ret = collide();
        y--;
        return ret;
    }

    public void shiftLeft() {
        x--;
        boolean ret = collide();
        if(ret == true)
            x++;
    }

    public void shiftRight() {
        x++;
        boolean ret = collide();
        if(ret == true)
            x--;
    }

    public void applyGravity() {
        //if(y < 3)
        y++;
    }


    public boolean update(int tick) {
        if(tick % Loop.gravityTick == 0 || Window.keyDown) {
            if (!collideOnGravity()) {
                applyGravity();
                return true;
            }
            return false;
        }
        return true;
    }
}
