package main;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import inputs.KeyboardInput;
import inputs.MouseInput;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class Window extends JFrame implements Runnable {
    private static Window window = null;
    protected boolean isRunning;

    public KeyboardInput keyListener = new KeyboardInput();
    public MouseInput mouseListener = new MouseInput();

    public Menu menu = new Menu();
    public Playing playing = new Playing();

    public Window(int width, int height, String title) {
        setSize(width, height);
        setTitle(title);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        isRunning = true;
    }

    public static Window getWindow() {
        if (Window.window == null) {
            Window.window = new Window(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Constants.SCREEN_TITLE);
        }

        return Window.window;
    }

    private void update() {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        switch(Gamestate.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
        }
        getGraphics().drawImage(dbImage, 0, 0, this);
    }

    private void draw(Graphics g) {
        switch(Gamestate.state) {
            case PLAYING -> playing.draw(g);
            case MENU -> menu.draw(g);
        }
    }

    @Override
    public void run() {
        Instant lastFrameTime = Instant.now();
        try {
            while (isRunning) {
                Instant time = Instant.now();
                double deltaTime = Duration.between(lastFrameTime, time).toNanos() * 10E-10;
                lastFrameTime = Instant.now();

                double deltaWanted = 0.0167;
                update();
                long msToSleep = (long)((deltaWanted - deltaTime) * 1000);
                if (msToSleep > 0) {
                    Thread.sleep(msToSleep);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.dispose();
    }
}
