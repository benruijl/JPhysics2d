package physics.math;

import physics.util.Utils;


/**
 * 2d vector class using floats.
 * 
 * @author Ben Ruijl
 */
public final class Vector2f {
    /** X component of the vector. */
    private final float x;
    /** Y component of the vector. */
    private final float y;

    public Vector2f() {
        x = 0;
        y = 0;
    }

    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(final Vector2f vector) {
        x = vector.x;
        y = vector.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * Returns a vector with only the x component.
     * 
     * @return Vector
     */
    public Vector2f getXVector() {
        return new Vector2f(x, 0);
    }

    /**
     * Returns a vector with only the y component.
     * 
     * @return Vector
     */
    public Vector2f getYVector() {
        return new Vector2f(0, y);
    }

    public Vector2i asVector2i() {
        return new Vector2i((int) x, (int) y);
    }

    public Vector2f add(final Vector2f vec) {
        return new Vector2f(x + vec.x, y + vec.y);
    }

    public Vector2f sub(final Vector2f vec) {
        return new Vector2f(x - vec.x, y - vec.y);
    }

    public float dot(final Vector2f vec) {
        return x * vec.x + y * vec.y;
    }

    /**
     * Returns the <b>length</b> of the cross product.
     * 
     * @param vec
     *            Vector2
     * @return Length of cross product
     */
    public float cross(final Vector2f vec) {
        return x * vec.y - y * vec.x;
    }

    public Vector2f scaleTo(final float size) {
        return scale(size / length());
    }

    public float lengthSquared() {
        return dot(this);
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector2f scale(final float amount) {
        return new Vector2f(x * amount, y * amount);
    }

    public Vector2f normalize() {
        final float invLength = 1.00f / (float) Math.sqrt(lengthSquared());
        return new Vector2f(x * invLength, y * invLength);
    }

    // FIXME .. breaks the general contract of hashcode
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector2f other = (Vector2f) obj;
        if (!Utils.equals(x, other.x)) {
            return false;
        }
        if (!Utils.equals(y, other.y)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
