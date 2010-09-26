package physics;

import physics.math.Vector2f;

public class Particle {
	private final boolean infiniteMass;
	private final float inverseMass;
	private Vector2f position = new Vector2f();
	private Vector2f oldPosition = new Vector2f();
	private Vector2f acceleration = new Vector2f();

	public Particle(Vector2f position, float mass) {
		this(mass);
		this.position = position;
		this.oldPosition = position;
	}

	/**
	 * Creates a new particle. If the mass is less or equal to zero, the mass is
	 * said to be infinite.
	 * 
	 * @param mass
	 *            Mass
	 */
	public Particle(float mass) {
		if (mass <= 0) {
			inverseMass = 0;
			infiniteMass = true;
		} else {
			inverseMass = 1f / mass;
			infiniteMass = false;
		}
	}

	/**
	 * Do Verlet integration.
	 * 
	 * @param timeStep
	 *            Time step
	 */
	public void integrate(float timeStep) {
		Vector2f curPos = position;
		position = position.add(position.sub(oldPosition).add(
				acceleration.scale(timeStep * timeStep)));
		oldPosition = curPos;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getPosition() {
		return position;
	}
}
