package demo.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import physics.math.Vector2i;

/**
 * A low-level input manager.
 * 
 * @author Ben Ruijl
 */
public final class Input implements KeyListener, MouseListener,
        MouseMotionListener {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(Input.class);
    private static Input ref = null;
    private final Set<Integer> keysDown;
    private final Set<Integer> buttonsDown;
    private Vector2i mousePos;
    private final List<MouseEventListener> listeners;

    private Input() {
        keysDown = new HashSet<Integer>();
        buttonsDown = new HashSet<Integer>();
        mousePos = new Vector2i();
        listeners = new ArrayList<MouseEventListener>();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static Input getInstance() {
        if (ref == null) {
            ref = new Input();
        }

        return ref;
    }

    public void addListener(final MouseEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        keysDown.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    }

    /**
     * Set the key to the up state.
     * 
     * @param key
     *            Key
     */
    public void setKeyUp(final int key) {
        keysDown.remove(key);
    }

    /**
     * Check if a given key is down.
     * 
     * @param key
     *            Keycode of the key
     * @return True if down, else false
     */
    public boolean isKeyDown(final int key) {
        return keysDown.contains(key);
    }

    /**
     * Check if a given button is down.
     * 
     * @param button
     *            Button number
     * @return True if down, else false
     */
    public boolean isButtonDown(final int button) {
        return buttonsDown.contains(button);
    }

    public Set<Integer> getKeysDown() {
        return new HashSet<Integer>(keysDown);
    }

    public Set<Integer> getButtonsDown() {
        return new HashSet<Integer>(buttonsDown);
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        // do not differentiate between moved and dragged
        mousePos = new Vector2i(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        mousePos = new Vector2i(e.getX(), e.getY());
    }

    public Vector2i getMousePos() {
        return mousePos;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        mousePos = new Vector2i(e.getX(), e.getY());

        for (final MouseEventListener listener : listeners) {
            listener.onMouseClicked(new demo.input.MouseEvent(
                    mousePos));
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        mousePos = new Vector2i(e.getX(), e.getY());
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        buttonsDown.add(e.getButton());
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        buttonsDown.remove(e.getButton());
    }

}
