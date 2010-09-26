package physics.math;

/**
 * A class for 2d matrices.
 * 
 * @author ben
 */
public class Matrix2f {
    private final float[] m = new float[4];

    public Matrix2f() {
    }

    public Matrix2f(final float a, final float b, final float c, final float d) {
        m[0] = a;
        m[1] = b;
        m[2] = c;
        m[3] = d;
    }

    /**
     * Create a rotation matrix
     * 
     * @param rot
     *            Rotation in <b>radians</b>
     */
    public Matrix2f(final double rot) {
        m[0] = (float) Math.cos(rot);
        m[1] = (float) Math.sin(rot);
        m[2] = -m[1];
        m[3] = m[0];
    }

    public Vector2f apply(final Vector2f vec) {
        return new Vector2f(vec.getX() * m[0] + vec.getY() * m[2], vec.getX()
                * m[1] + vec.getY() * m[3]);
    }

    public Matrix2f transpose() {
        return new Matrix2f(m[0], m[2], m[1], m[3]);
    }

    public float determinant() {
        return m[0] * m[3] - m[1] * m[2];
    }

}
