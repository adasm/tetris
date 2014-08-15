package com.adasm.tetris;

import java.awt.*;

public class Block {
    public Shape shape = null;
    public int x, y;
    public Color color;
    public boolean scored = false;
    public boolean fallingState = false;
    public long fallingStart = 0;
    public float falling = 0, fallingSpeed = 0.0025f;

    public Block(int x, int y, Color color, Shape shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public Block moveDown() {
        y++;
        startFalling();
        return this;
    }

    public void startFalling() {
        fallingStart = System.currentTimeMillis();
        falling = 1;
        fallingState = true;
    }

    public void update() {
        if(fallingState) {
            falling -= fallingSpeed * ((float)(System.currentTimeMillis() - fallingStart));
            if(falling <= 0) fallingState = false;
        }
    }

    public int getX() {
        return shape != null ? shape.x + x : x;
    }

    public int getY() { return (shape != null ? shape.y + y : y); }

    public void addToBoard() {
        x = getX();
        y = getY();
        shape = null;
        if(y >= 0)
            Game.blocks[x][y] = this;
        else {
           Game.over = true;
        }
    }
}
