package demo;

/**
 * This interface contains the listener functions for a renderer.
 * 
 * @author Ben Ruijl
 */
public interface RenderListener {

    /**
     * Called when the renderer is initializing.
     */
    void initialize();

    /**
     * Called when the renderer should update.
     * 
     * @param delta
     *            Delta time
     */
    void update(double delta);

    /**
     * Called when the renderer should draw.
     * 
     * @param renderer
     *            Renderer
     */
    void draw(Renderer renderer);

    /**
     * Called when the renderer is disposed of.
     */
    void dispose();
}
