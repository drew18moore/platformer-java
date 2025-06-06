package com.drewm.main;

import com.drewm.gamestates.Gamestate;
import com.drewm.gamestates.Options;
import com.drewm.gamestates.Playing;
import com.drewm.gamestates.Menu;
import com.drewm.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame implements Runnable {
    private static Window window = null;
    private Panel panel;
    protected boolean isRunning;
    private boolean SHOW_FPS_UPS = true;
    private boolean isFullscreen = false;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    public Menu menu = new Menu();
    public Options options = new Options(this);
    public Playing playing = new Playing();

    public Window(int width, int height, String title) {
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        setTitle(title);
        panel = new Panel(width, height, this);
        add(panel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setIsFullscreen(this.isFullscreen);

        setVisible(true);
        isRunning = true;
    }

    public static Window getWindow() {
        if (Window.window == null) {
            Window.window = new Window(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Constants.SCREEN_TITLE);
        }

        return Window.window;
    }

    private void update() {
        switch(Gamestate.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
            case OPTIONS -> options.update();
            case QUIT -> System.exit(0);
        }
    }

    protected void draw(Graphics g) {
        switch(Gamestate.state) {
            case PLAYING -> playing.draw(g);
            case OPTIONS -> options.draw(g);
            case MENU -> menu.draw(g);
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / Constants.FPS;
        double timePerUpdate = 1000000000.0 / Constants.UPS;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;

            }

            if (deltaF >= 1) {
                panel.repaint();
                frames++;
                deltaF--;

            }

            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {

                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;

                }

        }
    }

    public boolean getIsFullscreen() {
        return this.isFullscreen;
    }

    public void setIsFullscreen(boolean bool) {
        this.isFullscreen = bool;

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        dispose();
        if (this.isFullscreen && gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.out.println("Fullscreen not supported, running in windowed mode.");


            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int availableWidth = SCREEN_WIDTH - screenInsets.left - screenInsets.right;
            int availableHeight = SCREEN_HEIGHT - screenInsets.top - screenInsets.bottom;

            Insets windowInsets = getInsets();
            availableWidth -= windowInsets.left + windowInsets.right;
            availableHeight -= windowInsets.top + windowInsets.bottom;

            setSize(availableWidth, availableHeight);
        }
        setVisible(true);
    }
}
