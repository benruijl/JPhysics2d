package physics.math;

import physics.util.Utils;


public abstract class AbstractGeometry {
    public abstract boolean intersects(AbstractGeometry geometry);

    public abstract boolean intersects(Rectangle rect);

    public abstract boolean intersects(Circle circ);

    public static boolean intersects(final Rectangle rect, final Circle circ) {
        final float closestX = Utils.clamp(circ.getPos().getX(),
                rect.getLeft(), rect.getRight());
        final float closestY = Utils.clamp(circ.getPos().getY(), rect.getTop(),
                rect.getBottom());

        final Vector2f dist = circ.getPos().sub(
                new Vector2f(closestX, closestY));

        return dist.lengthSquared() < circ.getRadius() * circ.getRadius();
    }

    public abstract Rectangle asRectangle();

    public abstract Circle asCircumscribedCircle();

    public abstract Circle asInscribedCircle();

    public abstract AbstractGeometry translate(final Vector2f pos);

    public abstract boolean containsPoint(final Vector2f point);
}
