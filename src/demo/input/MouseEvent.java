package demo.input;

import physics.math.Vector2i;

public class MouseEvent {
    private final Vector2i position;

    public MouseEvent(final Vector2i position) {
        super();
        this.position = position;
    }

    public Vector2i getPosition() {
        return position;
    }
}
