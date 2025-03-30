package com.drewm.inputs;

import com.drewm.gamestates.Gamestate;
import com.drewm.main.Window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.mouseClicked(e);
            case OPTIONS -> Window.getWindow().options.mouseClicked(e);
            case PLAYING -> Window.getWindow().playing.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.mousePressed(e);
            case OPTIONS -> Window.getWindow().options.mousePressed(e);
            case PLAYING -> Window.getWindow().playing.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.mouseReleased(e);
            case OPTIONS -> Window.getWindow().options.mouseReleased(e);
            case PLAYING -> Window.getWindow().playing.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU -> Window.getWindow().menu.mouseMoved(e);
            case OPTIONS -> Window.getWindow().options.mouseMoved(e);
            case PLAYING -> Window.getWindow().playing.mouseMoved(e);
        }
    }
}
