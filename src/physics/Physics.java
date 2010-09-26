package physics;

import java.util.ArrayList;
import java.util.List;

public class Physics {
	/** Fixed timestep for the physics updates. */
	private final float timeStep;
	private final List<Particle> particles;

	public Physics(float timeStep) {
		this.timeStep = timeStep;
		particles = new ArrayList<Particle>();
	}

	public void addParticle(Particle particle) {
		particles.add(particle);
	}

	public void update() {
		/* Do a position Verlet integration. */
		for (Particle particle : particles) {
			particle.integrate(timeStep);
		}
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
}