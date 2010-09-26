package physics.util;


/**
 * A helper library.
 * 
 * @author Ben Ruijl
 * 
 */
public final class Utils {
	/** A small value to prevent rounding errors. */
	private static final float EPSILON = 0.00001f;

	/** Private constructor. Never called. */
	private Utils() {

	}
	/**
	 * Safe float comparison. Uses a small error, defined by
	 * <code>EPSILON</code>.
	 * 
	 * @param a
	 *            First float
	 * @param b
	 *            Second float
	 * @return Returns true if equal within the error, else false.
	 */
	public static boolean equals(final float a, final float b) {
		return a == b ? true : Math.abs(a - b) < EPSILON;
	}

	/**
	 * Clamps an integer to a range.
	 * 
	 * @param x
	 *            Integer to clamp
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value
	 * @return Clamped integer
	 */
	public static float clamp(final float x, final float min, final float max) {
		return x < min ? min : x > max ? max : x;
	}

	/**
	 * Creates an independent copy of the boolean array. This involves cloning
	 * the structure and the data itself.
	 * 
	 * @param array
	 *            The array to be cloned
	 * @return An independent 'deep' structure clone of the array
	 */
	public static boolean[][] clone2DArray(final boolean[][] array) {
		final int rows = array.length;

		final boolean[][] newArray = array.clone();

		for (int row = 0; row < rows; row++) {
			newArray[row] = array[row].clone();
		}

		return newArray;
	}
}
