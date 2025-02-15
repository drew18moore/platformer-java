package ui;

import gamestates.Playing;

import java.awt.image.BufferedImage;

public class WinScreen {
    private Playing playing;
    public BufferedImage backgroundImg;
    public int bgX, bgY, bgW, bgH;
    private BufferedImage headerText;

    public WinScreen(Playing playing) {
        this.playing = playing;
    }
}
