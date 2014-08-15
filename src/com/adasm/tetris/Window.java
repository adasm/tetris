package com.adasm.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Window extends JFrame implements KeyListener {
    public static int gw = 100, gh = 100;
    public static boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false;

    public static long lastTime = System.nanoTime();

    public static ArrayList<Text> texts = new ArrayList<>();
    public static ArrayList<Particle> particles = new ArrayList<>();

    public static Window mainWindow = null;

    public static float currentBoardHeight = 10;
    public static final int windowWidth = 400, windowHeight = 800;

    public Window() {
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setTitle("Tetris Game by adasm (c) 2014");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Drawer(), BorderLayout.CENTER);
        addKeyListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        mainWindow = this;
    }

    public static class Drawer extends JPanel {

        public static float offX = 0, offY = 0, offS = 1;
        @Override
        protected void paintComponent(Graphics g) {
            float dt = (System.nanoTime() - lastTime) * 0.00000001f;
            lastTime = System.nanoTime();

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            float w = getWidth();
            float h = getHeight();

            gw = (int)w;
            gh = (int)h;

            g2d.setColor(new Color(20, 20, 20, 255));
            g2d.fillRect(0, 0, (int)w, (int)h);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            currentBoardHeight = 0.9f * currentBoardHeight + 0.1f * Game.currentBoardSizeY;

            float acc = 0, maxSide = 20;
            offX *= 0.45f;
            offS = (float)Math.sqrt(offS);
            if(keyLeft) { offX -= dt * acc; }
            if(keyRight) { offX += dt * acc;  }
            //if(keyLeft || keyRight) { offS -= dt * 0.0001f; }

            if(offX < -maxSide) offX = -maxSide;
            if(offX > maxSide) offX = maxSide;


            //if(offX > 0) {
             //   offX -= dt * acc * 0.5;
            //} else {
            //    offX += dt * acc * 0.5;
            //}

            float ww = w + Math.abs(offX);
            float ts = w / ww;
            offS = 0.4f * offS + 0.6f * (ts);

            float coef = (currentBoardHeight / Game.boardSizeY);
            float dy = 0, dx = 0;//(1-coef) * windowWidth / 4;
            float dxy = maxSide, dsx = offS * w / (w + 2*dxy), dsy = (float)Math.sqrt(offS) *  h / (h + 2*dxy);
            drawFade(g2d, dx + dxy + offX, dy + dxy + offY, w * dsx, h * dsy);
            drawHints(g2d, dx + dxy + offX, dy + dxy + offY, w * dsx, h * dsy);
            drawBlocks(g2d, dx + dxy - offX, dy + dxy - offY, w * dsx, h * dsy);

            g2d.setFont(new Font("default", Font.BOLD, 14));

            g2d.setColor(new Color(20, 50, 90, 95));
            g2d.fillRect(0, 0, (int) w, 17);
            g2d.setColor(new Color(250, 100, 50, 200));
            g2d.drawString("Score: " + Game.score + "  Level: " + Game.level, 0, 14);


            drawText(g2d, 0, 0, w, h, dt);
            drawParticles(g2d, 0, 0, w, h, dt);

            Util.sleep(15);
            if(Loop.running)
                repaint();
        }

        public void drawFade(Graphics2D g2d, float ox, float oy, float w, float h, int x, int y, float alpha, float scale) {
            float blockSizeX = scale * w / Game.boardSizeX;
            float blockSizeY = scale * h / currentBoardHeight;

            float startX = ox + x * blockSizeX;
            float startY = oy + (-(Game.boardSizeY - currentBoardHeight) + y) * blockSizeY;

            int rad = (int)(blockSizeX / 2);

            g2d.setColor(new Color(0.25f, 0.25f, 0.25f, alpha));
            g2d.fillRoundRect((int)startX, (int)startY, (int)blockSizeX + 1, (int)blockSizeY + 1, rad, rad);
        }

        public void drawBlock(Graphics2D g2d, float ox, float oy, float w, float h, Block block, float alpha, float scale) {
            if(block != null) {
                block.update();

                float blockSizeX = scale * w / Game.boardSizeX;
                float blockSizeY = scale * h / currentBoardHeight;

                float startX = ox + block.getX() * blockSizeX;
                float startY = oy + (-(Game.boardSizeY - currentBoardHeight) + block.getY() - (block.fallingState ? block.falling : 0)) * blockSizeY;

                int rad = (int)(blockSizeX / 2);

                Color color;
                if(block.scored) {
                    color = new Color(80, 100 + (int)(Math.sin(System.currentTimeMillis() * 0.5) * 80), 30, (int)(255 * alpha));
                } else {
                    color = new Color(70, 50, 100 + (int)(Math.sin(System.currentTimeMillis() * 0.01) * 50), (int)(255 * alpha));
                }
                Color blockColor = Util.lerp(block.color, color, 0.5f);
                //fore
                g2d.setColor(blockColor);
                //g2d.fillRect((int)startX, (int)startY, (int)blockSizeX, (int)blockSizeY);
                g2d.fillRoundRect((int)startX, (int)startY, (int)blockSizeX + 1, (int)blockSizeY + 1, rad, rad);


                //back
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawRoundRect((int) startX, (int) startY, (int) blockSizeX, (int) blockSizeY, rad, rad);
                //
            }
        }

        public void drawHint(Graphics2D g2d, float ox, float oy, float w, float h, Block block, float alpha, float scale) {
            if(block != null) {
                float blockSizeX = scale * w / Game.boardSizeX;
                float blockSizeY = scale * h / currentBoardHeight;

                float startX = ox + block.getX() * blockSizeX;
                float startY = oy + (-(Game.boardSizeY - currentBoardHeight) + block.getY()) * blockSizeY;

                int rad = (int)(blockSizeX / 2);

                Color color;
                if(block.scored) {
                    color = new Color(255, 140 + (int)(Math.sin(System.currentTimeMillis() * 0.5) * 80), 255, (int)((135 + 125*Math.sin(System.currentTimeMillis() * 0.01f)) * alpha));
                } else {
                    color = new Color(255, 255, 170 + (int)(Math.sin(System.currentTimeMillis() * 0.01) * 50), (int)((135 + 125*Math.sin(System.currentTimeMillis() * 0.01f)) * alpha));
                }
                Color blockColor = Util.lerp(block.color, color, 0.75f);
                blockColor = new Color(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue(),(int)(255 * alpha));
                //fore
                g2d.setColor(blockColor);
                //g2d.fillRect((int)startX, (int)startY, (int)blockSizeX, (int)blockSizeY);
                g2d.fillRoundRect((int)startX, (int)startY, (int)blockSizeX + 1, (int)(2*h), rad, rad);


                //back
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(10));
                g2d.drawRoundRect((int) startX, (int) startY, (int) blockSizeX, (int)(2*h), rad, rad);
                //
            }
        }

        public void drawHints(Graphics2D g2d, float ox, float oy, float w, float h) {
            synchronized (Game.shapeLock) {
                if(Game.shape != null) {
                    for(Block block : Game.shape.blocks) {
                        drawHint(g2d, ox, oy, w, h, block, 0.009f, 1);
                    }
                }
            }
        }

        public void drawFade(Graphics2D g2d, float ox, float oy, float w, float h) {
            for(int x = 0; x < Game.boardSizeX; x++) {
                for(int y = Game.boardSizeY - Game.currentBoardSizeY; y < Game.boardSizeY; y++) {
                    drawFade(g2d, ox, oy, w, h, x, y, Game.blocksFade[x][y] * 0.35f, 1);
                }
            }
        }

        public void drawBlocks(Graphics2D g2d, float ox, float oy, float w, float h) {
            synchronized (Game.blocks) {
                for(int x = 0; x < Game.boardSizeX; x++) {
                    for(int y = Game.boardSizeY - Game.currentBoardSizeY; y < Game.boardSizeY; y++) {
                        drawBlock(g2d, ox, oy, w, h, Game.blocks[x][y], 1, 1);
                    }
                }
            }
            synchronized (Game.shapeLock) {
                if(Game.shape != null) {
                    for(Block block : Game.shape.blocks) {
                        drawBlock(g2d, ox, oy, w, h, block, 1, 1);
                    }
                }

                if(Game.nextShape != null) {
                    for(Block block : Game.nextShape.blocks) {
                        drawBlock(g2d, ox, oy, w, h, block, 0.25f, 0.5f);
                    }
                }
            }
        }

        private void printCenter(Graphics2D g2d, String s, int width, int XPos, int YPos){
            int stringLen = (int)
                    g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
            int start = width/2 - stringLen/2;
            g2d.drawString(s, start + XPos, YPos);
        }

        public void drawText(Graphics2D g2d, float ox, float oy, float w, float h, float dt) {;
            ArrayList<Text> toRemove = new ArrayList<>();
            synchronized (texts) {
                for (Text text : texts) {
                    text.update(dt);
                    if (text.alpha < 0) toRemove.add(text);
                    else {
                        g2d.setColor(new Color(255, 255, 255, (int)(255 * text.alpha)));
                        g2d.setStroke(new BasicStroke(6));

                        g2d.setFont(new Font("default", Font.BOLD, (int) ( (1.1-text.alpha) * 70)));

                        printCenter(g2d, text.str, (int) w, (int) ox, (int) (oy + h * text.y));

                    }
                }
                texts.remove(toRemove);
            }
        }


        public void drawParticles(Graphics2D g2d, float ox, float oy, float w, float h, float dt) {;
            ArrayList<Particle> toRemove = new ArrayList<>();
            synchronized (particles) {
                for (Particle particle : particles) {
                    particle.update(dt);
                    if (particle.alpha < 0) toRemove.add(particle);
                    else {
                        g2d.setColor(new Color(particle.color.getRed(), particle.color.getGreen(), particle.color.getBlue(), (int)(255 * particle.alpha)));
                        g2d.setStroke(new BasicStroke(4));

                        g2d.drawOval((int)particle.x, (int)particle.y, 2,2);
                    }
                }
                particles.remove(toRemove);
            }
        }

    }

    public static void addText(Text text) {
        synchronized (texts) {
            texts.add(text);
        }
    }

    public static void addParticles() {
        synchronized (particles) {
            for(int i = 0; i < 300; i++) {
                particles.add(new Particle(gw, gh));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                keyUp = true;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = true;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_ESCAPE:
                Loop.running = false;
                if(mainWindow != null) {
                    mainWindow.dispose();
                    mainWindow = null;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = false;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
        }
    }

    public static void init() {
        SwingUtilities.invokeLater(() -> new Window().setVisible(true));
    }

}
