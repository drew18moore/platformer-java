package inputs;

import gamestates.Gamestate;
import main.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.keyPressed(e);
            case PLAYING -> Window.getWindow().playing.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.keyReleased(e);
            case PLAYING -> Window.getWindow().playing.keyReleased(e);
        }
    }
}
