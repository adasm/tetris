package com.adasm.tetris;

import java.awt.*;
import java.util.Random;

public class Util {
    public static int sign(float x) {
        if(x > 0) return 1;
        else if(Math.abs(x) < 0.00001f) return 0;
        else return -1;
    }

    public static void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int lerp(int a, int b, float x) {
        return a + (int)((b - a) * x);
    }

    public static Color lerp(Color a, Color b, float x) {
        return new Color(lerp(a.getRed(), b.getRed(), x),
                lerp(a.getGreen(), b.getGreen(), x),
                lerp(a.getBlue(), b.getBlue(), x),
                lerp(a.getAlpha(), b.getAlpha(), x));
    }

    public static int randomInteger() {
        return Math.abs((new Random()).nextInt());
    }

    public static float random() {
        return (new Random()).nextFloat();
    }

    public static Color randomSolidColor() {
        return new Color(random(), random(), random(), 1);
    }

    public static Color randomTransColor(float alpha) {
        return new Color(random(), random(), random(), alpha);
    }
}