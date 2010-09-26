package physics;

import physics.math.Vector2f;

public class CollisionData {
	private final boolean collided;
	private final float time;
	private final Vector2f normal;
	private final Vector2f penetration;

	public CollisionData(final boolean collided, final float time,
			final Vector2f normal, final Vector2f penetration) {
		super();
		this.collided = collided;
		this.time = time;
		this.normal = normal;
		this.penetration = penetration;
	}

	public boolean isCollided() {
		return collided;
	}

	public float getTime() {
		return time;
	}

	public Vector2f getNormal() {
		return normal;
	}

	public Vector2f getPenetration() {
		return penetration;
	}
}
