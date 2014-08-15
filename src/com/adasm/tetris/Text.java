package com.adasm.tetris;

public class Text {
    public String str;
    public float alpha = 1;
    public float speed = 1;
    public float y = 0.25f;

    public Text(String s, float speed) {
        this.str = s;
        alpha = 1;
        this.speed = speed;
    }

    public Text(String s, float speed, float y) {
        this.str = s;
        alpha = 1;
        this.y = y;
        this.speed = speed;
    }

    public void update(float dt) {
        alpha -= speed * dt;
    }

}
