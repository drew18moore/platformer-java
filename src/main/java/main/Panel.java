package main;

import inputs.KeyboardInput;
import inputs.MouseInput;

import javax.swing.JPanel;
import java.awt.*;

public class Panel extends JPanel {
    private Window window;
    public KeyboardInput keyListener = new KeyboardInput();
    public MouseInput mouseListener = new MouseInput();

    public Panel(int width, int height, Window window) {
        this.window = window;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        window.draw(g);
    }
}
