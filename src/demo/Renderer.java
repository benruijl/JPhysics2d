package demo;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import org.apache.log4j.Logger;

import physics.math.Circle;
import physics.math.Matrix2f;
import physics.math.Rectangle;
import physics.math.Vector2f;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.texture.Texture;

import demo.input.Input;

/**
 * Renderer class. Takes care of rendering, window creation, context creation
 * and update and draw dispatching
 * 
 * @author Ben Ruijl
 */
public class Renderer implements GLEventListener {
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(Renderer.class);

	private Frame win;
	private boolean quitting;
	private GLCanvas mCanvas;
	private GL gl;
	private RenderListener mEvListener;
	private GLAutoDrawable mCurDrawable;
	private int width;
	private int height;
	private boolean isFullScreen;
	private Animator anim;
	private long lastUpdate;
	private Texture lastTexture;

	private boolean bulkDraw = false;

	/* HUD settings */
	private final int hudWidth = 800; // FIXME: adjust to aspect ratio
	private final int hudHeight = 600;

	/* FPS counting */
	private long prevTime;
	private int frameCount;
	private float curFPS;

	/** Standard clear color is sky blue. */
	private final ColorRGBA standardClearColor = new ColorRGBA(new ColorRGB(
			0.52f, 0.8f, 1.0f), 0);
	/** Clear color. */
	private ColorRGBA clearColor = standardClearColor;

	/**
	 * RGB color.
	 * 
	 * @author Ben Ruijl
	 * 
	 */
	private static final class ColorRGB {
		private final float r, g, b;

		public ColorRGB(final float r, final float g, final float b) {
			super();
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public float getR() {
			return r;
		}

		public float getG() {
			return g;
		}

		public float getB() {
			return b;
		}
	}

	/**
	 * RGBA color.
	 * 
	 * @author Ben Ruijl
	 * 
	 */
	private static final class ColorRGBA {
		private final ColorRGB rgb;
		private final float a;

		public ColorRGBA(final ColorRGB rgb, final float a) {
			super();
			this.rgb = rgb;
			this.a = a;
		}

		public ColorRGB getRGB() {
			return rgb;
		}

		public float getA() {
			return a;
		}
	}

	/**
	 * Initializes the renderer. It creates the window and a GL render canvas.
	 * 
	 * @param strTitle
	 *            Window title
	 * @param width
	 *            Window width
	 * @param height
	 *            Window height
	 * @param fs
	 *            Set full screen
	 */
	public final void initialize(final String strTitle, final int width,
			final int height, final boolean fs) {
		win = new Frame(strTitle);
		win.setSize(width, height);
		win.setLocation(0, 0);
		win.setLayout(new BorderLayout());
		quitting = false;

		final GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		mCanvas = new GLCanvas(caps);
		mCanvas.setIgnoreRepaint(true);
		mCanvas.addGLEventListener(this);
		win.add(mCanvas);

		final GraphicsDevice gd = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (fs) {
			win.setUndecorated(true);

			if (gd.isFullScreenSupported()) {
				gd.setFullScreenWindow(win);
			}
		}

		win.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				if (gd.getFullScreenWindow() == win) {
					gd.setFullScreenWindow(null);
				}

				mEvListener.dispose();
			}
		});

		win.setVisible(true);

		mCanvas.requestFocusInWindow();

		mCanvas.addKeyListener(Input.getInstance()); // listen to keys
		mCanvas.addMouseMotionListener(Input.getInstance()); // listen to mouse
		// moves
		mCanvas.addMouseListener(Input.getInstance());
		lastUpdate = -1;
	}

	public final void dispose() {
		LOG.info("Disposing window...");
		anim.stop();
		win.dispose();
		quitting = true; // prevent function calls to GL after this
	}

	/**
	 * Hides the hardware cursor.
	 */
	public final void hideHardwareCursor() {
		final int[] pixels = new int[16 * 16];
		final Image image = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(16, 16, pixels, 0, 16));
		final Cursor transparentCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(image, new Point(0, 0), "invisibleCursor");

		mCanvas.setCursor(transparentCursor);
	}

	/**
	 * Toggle between windowed mode and full screen mode.
	 * 
	 * FIXME: this function works on some systems. The window is recreated in
	 * dispose() and so is the GLcanvas. This means the init function is called
	 * again, so resources are loaded again.
	 */
	public final void toggleFullScreen() {
		final GraphicsDevice gd = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (isFullScreen || !gd.isFullScreenSupported()) {
			win.setVisible(false);
			win.dispose();
			win.setUndecorated(false);
			gd.setFullScreenWindow(null);
			win.setVisible(true);
			mCanvas.requestFocus();

			isFullScreen = false;
		} else {
			win.setVisible(false);
			win.dispose();
			win.setUndecorated(true);
			gd.setFullScreenWindow(win);
			win.setVisible(true);
			mCanvas.requestFocus();

			isFullScreen = true;
		}

	}

	/**
	 * Begin the render loop by starting an animator. The animator is set to run
	 * at 60 FPS.
	 */
	public final void beginLoop() {
		// setup the animator
		anim = new FPSAnimator(mCanvas, 60);
		anim.start();

	}

	public ColorRGBA getClearColor() {
		return clearColor;
	}

	public void setClearColor(final ColorRGBA clearColor) {
		this.clearColor = clearColor;
	}

	/**
	 * Get the current FPS avaraged over 10 frames.
	 * 
	 * @return Current FPS
	 */
	public final float getFPS() {
		return curFPS;
	}

	public final void setCurFPS(final float curFPS) {
		this.curFPS = curFPS;
	}

	/**
	 * This function does not only represent the display callback, but the
	 * update callback as well.
	 * 
	 * This function will call the update and draw event listeners. It also
	 * takes care of the framerate.
	 * 
	 * @param glDrawable
	 *            The current GL context
	 */
	@Override
	public final void display(final GLAutoDrawable glDrawable) {
		mCurDrawable = glDrawable;
		gl = mCurDrawable.getGL();

		/* Update FPS */
		if (frameCount > 10) {
			curFPS = 1000000000.0f * frameCount
					/ (System.nanoTime() - prevTime);
			prevTime = System.nanoTime();
			frameCount = 0;
		} else {
			frameCount++;
		}

		if (lastUpdate == -1) {
			lastUpdate = System.nanoTime();
		}

		final long currentTime = System.nanoTime();
		double delta = currentTime - lastUpdate;
		// Delta is in seconds. 10^9 nanoseconds per second
		delta /= 1000 * 1000 * 1000;
		lastUpdate = currentTime;

		if (mEvListener != null) {
			mEvListener.update(delta); // TODO: verify if delta is correct

			if (!quitting) {
				beginDraw();
				mEvListener.draw(this); // draw the frame
			}
		}

	}

	/**
	 * Starts a bulk draw mode in which only texture coordinates, color changes
	 * and vertices can be sent to the graphics card.
	 */
	public void startBulkDraw() {
		gl.glBegin(GL.GL_QUADS);
		bulkDraw = true;
	}

	/**
	 * Stops the bulk draw mode.
	 */
	public void stopBulkDraw() {
		gl.glEnd();
		bulkDraw = false;
	}

	/**
	 * Draws the outline of a rectangle. Useful for checking and debugging
	 * bounding rectangles.
	 * 
	 * @param rect
	 *            Rectangle to draw
	 */
	public final void drawRectOutline(final Rectangle rect) {

		gl.glPushAttrib(GL.GL_ENABLE_BIT);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1, 1, 1);

		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2f(rect.getLeft(), rect.getTop());
		gl.glVertex2f(rect.getLeft(), rect.getBottom());
		gl.glVertex2f(rect.getRight(), rect.getBottom());
		gl.glVertex2f(rect.getRight(), rect.getTop());
		gl.glEnd();

		gl.glPopAttrib();
	}

	/**
	 * Draws a rectangle filled with a color.
	 * 
	 * @param rect
	 *            Rectangle
	 */
	public final void drawFilledRect(final Rectangle rect) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(rect.getLeft(), rect.getTop());
		gl.glVertex2f(rect.getLeft(), rect.getBottom());
		gl.glVertex2f(rect.getRight(), rect.getBottom());
		gl.glVertex2f(rect.getRight(), rect.getTop());
		gl.glEnd();
	}

	public final void drawCircle(final Circle circle, int steps) {
		gl.glBegin(GL.GL_LINE_LOOP);

		for (int i = 0; i < steps; i++) {
			gl.glVertex2d(
					circle.getPos().getX()
							+ Math.cos(i * 2 / (double) steps * Math.PI)
							* circle.getRadius(),
					circle.getPos().getY()
							+ Math.sin(i * 2 / (double) steps * Math.PI)
							* circle.getRadius());
		}

		gl.glEnd();
	}

	public final void drawParticle(final Vector2f particle) {
		drawFilledRect(new Rectangle(particle.getX() - 1.5f,
				particle.getY() - 1.5f, 3, 3));
	}

	/**
	 * To be called when the rendering of the current frame starts. It clears
	 * the buffers and resets the modelview matrix.
	 */
	private void beginDraw() {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

	}

	/**
	 * This function is called on window creation and recreation. It sets the
	 * basic OpenGL settings and creates the camera.
	 */
	@Override
	public final void init(final GLAutoDrawable glDrawable) {
		mCurDrawable = glDrawable;
		gl = mCurDrawable.getGL();

		gl.glClearColor(clearColor.getRGB().getR(), clearColor.getRGB().getG(),
				clearColor.getRGB().getB(), clearColor.getA());
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glPointSize(4);

		mEvListener.initialize();
	}

	/**
	 * Called when the window is resized. It sets up a new projection matrix
	 * from the new window width and height.
	 * 
	 * @param glDrawable
	 *            The new GL context
	 * @param x
	 *            New x coordinate of window
	 * @param y
	 *            New y coordinate of window
	 * @param width
	 *            New width of the window
	 * @param height
	 *            New height of the window
	 */
	@Override
	public final void reshape(final GLAutoDrawable glDrawable, final int x,
			final int y, final int width, final int height) {
		mCurDrawable = glDrawable;
		gl = glDrawable.getGL();

		this.width = width;
		this.height = height;

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, height, 0, -1, 1);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslated(0.375, 0.375, 0.);
	}

	/**
	 * This function transforms the current projection and modelview matrix to a
	 * convenient one for the rendering of HUDs and GUIs. It saves the state of
	 * both matrices. They must be recovered by a call to
	 * <code>stopHudRendering</code>.<br/>
	 * <br/>
	 * The projection will be orthogonal and will have a fixed width and height,
	 * defined by hudWidth and hudHeight. <br/>
	 * <br/>
	 * The modelview matrix will be reset to its identity. <br/>
	 * <br/>
	 * TODO: make the fixed width and height depend on the aspect ratio
	 * 
	 * @see Renderer#stopHUDRendering()
	 */
	public final void startHUDRendering() {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix(); // push the current projection matrix
		gl.glLoadIdentity();
		gl.glOrtho(0, hudWidth, hudHeight, 0, -1, 1);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix(); // push the current modelview matrix
		gl.glLoadIdentity();
	}

	/**
	 * This function restores the projection and modelview matrices to the point
	 * just before the call to <code>startHUDRendering</code>. It is
	 * <b>mandatory</b> to call this function after a call to startHudRendering.
	 * 
	 * @see Renderer#startHUDRendering()
	 */
	public final void stopHUDRendering() {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
	}

	/**
	 * Implementation not required. Do not call.
	 */
	@Override
	public void displayChanged(final GLAutoDrawable glad,
			final boolean modeChanged, final boolean deviceChanged) {
	}

	/**
	 * Add an event listener for
	 * 
	 * @param listener
	 */
	public final void addListener(final RenderListener listener) {
		mEvListener = listener;
	}

	/**
	 * Translate the current matrix.
	 * 
	 * @param vec
	 *            Translation vector
	 */
	public final void translate(final Vector2f vec) {
		gl.glTranslatef(vec.getX(), vec.getY(), 0);
	}

	/**
	 * Rotate the current matrix.
	 * 
	 * @param rad
	 *            Angle in <b>radians</b>
	 */
	public final void rotate(final float rad) {
		gl.glRotatef((float) (rad * 180.0f / Math.PI), 0, 0, 1);

	}

	/**
	 * Scale (and mirror) the current matrix.
	 * 
	 * @param vec
	 *            Scale vector. For mirroring, use negative numbers
	 */
	public final void scale(final Vector2f vec) {
		gl.glScalef(vec.getX(), vec.getY(), 1);

	}

	/**
	 * Sets the current color.
	 * 
	 * @param r
	 *            R
	 * @param g
	 *            G
	 * @param b
	 *            B
	 */
	public final void setColorRGB(final float r, final float g, final float b) {
		gl.glColor3f(r, g, b);
	}

	/**
	 * Sets the current color.
	 * 
	 * @param r
	 *            R
	 * @param g
	 *            G
	 * @param b
	 *            B
	 * @param a
	 *            A
	 */
	public final void setColorRGBA(final float r, final float g, final float b,
			final float a) {
		gl.glColor4f(r, g, b, a);
	}

	/**
	 * Checks is a rectangle is in the current view frustum.
	 * 
	 * @param rect
	 *            Rectangle to check
	 * @return Returns true if the rectangle is fully or partially in the
	 *         frustum.
	 */
	public final boolean inFrustum(final Rectangle rect) {
		final float[] mvmat = new float[16];
		gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, mvmat, 0);

		final Matrix2f mat = new Matrix2f(mvmat[0], mvmat[1], mvmat[4],
				mvmat[5]);
		final Vector2f leftTop = mat.apply(rect.getLeftTop());
		final Vector2f rightBottom = mat.apply(rect.getRightBottom());

		return mvmat[12] + leftTop.getX() < width
				&& mvmat[12] + rightBottom.getX() > 0
				&& mvmat[13] + leftTop.getY() < height
				&& mvmat[13] + rightBottom.getY() > 0;
	}

	/**
	 * Converts screen coordinates to world coordinates. Useful for picking with
	 * the mouse. This function assumes that the view matrix is orthogonal.
	 * 
	 * @param p
	 *            Screen position in pixels
	 * @return Returns world position
	 */
	// public final Vector2f screenToWorld(final Vector2i p) {
	// final Vector2f fp = p.asVector2f();
	//
	// // Create a rotation matrix, calculate its inverse and apply the camera
	// // translation
	// final Matrix2f invRot = new Matrix2f(camera.getRot()).transpose();
	// Vector2f vRes = invRot.apply(fp.sub(camera.getPos()));
	// final float fSXZ = camera.getScale().getX();
	// final float fSXY = fSXZ * camera.getScale().getY();
	// final float fSYZ = 1.0f;
	// final float fInvDet = 1.0f / (fSXY * fSXZ);
	//
	// vRes = vRes.scale(fInvDet * fSYZ);
	//
	// return vRes;
	// }

	/**
	 * Save the current matrix. Useful if doing transformations.
	 */
	public final void pushMatrix() {
		gl.glPushMatrix();
	}

	/**
	 * Restore the previous matrix.
	 */
	public final void popMatrix() {
		gl.glPopMatrix();
	}

	/**
	 * Resets the current matrix (modelview, projection etc.) to its identity.
	 * The identity is a 4 dimensional unit matrix.
	 */
	public final void loadIdentity() {
		gl.glLoadIdentity();
	}

}
