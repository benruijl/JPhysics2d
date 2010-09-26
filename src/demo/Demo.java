package demo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import physics.util.SettingsManager;

public class Demo implements RenderListener {
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(Demo.class);

	private boolean quitting = false;
	private final Renderer renderer;

	public Demo() {
		renderer = new Renderer();

		final SettingsManager settings = SettingsManager.getInstance();

		renderer.initialize("Physics demo",
				settings.getInteger("demo.window.width"),
				settings.getInteger("demo.window.height"),
				settings.getBoolean("demo.window.fullScreen"));
		renderer.addListener(this);
	}
	
    /**
     * Starts the render loop
     */
    public void start() {
        LOG.info("Starting renderer");
        renderer.beginLoop();
    }

	public static void main(String[] args) {
		/* Init log4j */
		DOMConfigurator.configure("log4j.xml");

		/* Load configuration */
		try {
			SettingsManager.getInstance().loadSettings("settings.ini");
		} catch (final IOException e) {
			LOG.error("Could not read configuration file.", e);
		}

		Demo demo = new Demo();
		demo.start();
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Renderer renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		if (!quitting) {
			quitting = true;
			renderer.dispose();
		}

	}

}
