package physics.math;

/**
 * 2D integer vector class.
 * 
 * @author Ben Ruijl
 */
public class Vector2i {
    private final int x;
    private final int y;

    public Vector2i() {
        x = 0;
        y = 0;
    }

    public Vector2i(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2i add(final Vector2i vec) {
        return new Vector2i(x + vec.x, y + vec.y);
    }

    public Vector2i sub(final Vector2i vec) {
        return new Vector2i(x - vec.x, y - vec.y);
    }

    public int dot(final Vector2i vec) {
        return x * vec.x + y * vec.y;
    }

    public int lengthSquared() {
        return dot(this);
    }

    public Vector2f asVector2f() {
        return new Vector2f(x, y);
    }

}
