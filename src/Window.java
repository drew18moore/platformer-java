import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class Window extends JFrame implements Runnable {
    protected boolean isRunning = true;

    public Player player = new Player(100, 100, null);

    public Window(int width, int height, String title) {
        setSize(width, height);
        setTitle(title);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        isRunning = true;
    }

    private void update(double dt) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        getGraphics().drawImage(dbImage, 0, 0, this);

        player.update(dt);
    }

    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
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
                update(deltaWanted);
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
