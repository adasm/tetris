package com.adasm.tetris;

import javax.swing.*;
import java.util.Random;

public class Loop implements Runnable {
    public static boolean running = true;
    public static int gravityTick = 25;
    public static final int speed = 50;
    public static int tick = 0;

    public static void setGravityTick(int level) {
        gravityTick = 25 - Math.min(24, level / 2);
    }
    @Override
    public void run() {
        while(running) {
            Window.addText(new Text(Game.textNewGame[Util.randomInteger()%Game.textNewGame.length], 0.1f));
            while (running && !Game.over) {
                Util.sleep(1000 / speed);
                Game.update(++tick);
                if (Game.scoring == true) {
                    Util.sleep(500);
                }
            }

            Window.addText(new Text(Game.textGameOver[Util.randomInteger()%Game.textGameOver.length], 0.05f));
            Window.addText(new Text("Earned :", 0.04f, 0.4f));
            Window.addText(new Text(Game.score + " points", 0.04f, 0.6f));
            Window.addText(new Text(Game.level + " level", 0.04f, 0.8f));
            Game.reset();
            setGravityTick(0);
        }
    }

    public static void start() {
        Window.init();
        SwingUtilities.invokeLater(() -> new Thread(new Loop()).start());
    }
}

