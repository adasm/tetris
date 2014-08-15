package com.adasm.tetris;

import java.awt.*;

public class Block {
    public Shape shape = null;
    public int x, y;
    public Color color;
    public boolean scored = false;

    public Block() {
        this.x = this.y = 0;
        this.color = Util.randomSolidColor();
    }

    public Block(int x, int y, Color color, Shape shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public Block moveDown() {
        y++;
        return this;
    }

    public int getX() {
        return shape != null ? shape.x + x : x;
    }

    public int getY() {
        return shape != null ? shape.y + y : y;
    }

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
