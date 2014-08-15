package com.adasm.tetris;

import java.awt.*;

public class Particle {
    public float x, y;
    public float vx, vy;
    public float alpha = 1, alphaSpeed = 0.08f;
    public float gx = 0, gy = 0;
    public Color color;

    public Particle(int w, int h) {
        x = w / 2;
        y = h / 2;
        double deg = 2 * Math.PI * Math.random();
        vx = (float)Math.cos(deg) * (50 + (float)Math.random() * 40);
        vy = (float)Math.sin(deg) * (50 + (float)Math.random() * 40);
        alphaSpeed = 0.08f + (float)Math.random() * 0.03f;
        color = Util.randomSolidColor();

    }

    public void update(float dt) {
        x += dt * (vx + gx);
        y += dt * (vy + gy);
        alpha -= alphaSpeed * dt;
    }
}
