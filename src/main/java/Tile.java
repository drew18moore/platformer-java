import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;

    public Tile(BufferedImage image) {
        this.image = image;
    }
}
