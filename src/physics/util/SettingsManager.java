package physics.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The SettingsManager singleton can load and parse configuration files using
 * the Properties class. Settings can be looked up by their String value.
 * 
 * @author Ben Ruijl
 * 
 * @see Properties
 * 
 */
public final class SettingsManager {
	/** Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SettingsManager.class);
	/** Reference object. */
	private static SettingsManager ref = null;

	/** The parsed configuration data. */
	private final Properties config;

	/** Creates a new entity manager. */
	private SettingsManager() {
		config = new Properties();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Return the instance of the settings manager singleton.
	 * 
	 * @return Settings manager
	 */
	public static SettingsManager getInstance() {
		if (ref == null) {
			ref = new SettingsManager();
		}

		return ref;
	}

	/**
	 * Retrieves an Object from the settings list.
	 * 
	 * @param setting
	 *            Name of the setting
	 * @return An Object or null when the setting does not exist.
	 */
	public Object get(final String setting) {
		return config.get(setting);
	}

	/**
	 * Retrieves an integer from the settings list.
	 * 
	 * @param setting
	 *            Name of the setting
	 * @return An integer or null when the setting does not exist.
	 */
	public Integer getInteger(final String setting) {
		return Integer.valueOf(config.getProperty(setting));
	}

	/**
	 * Retrieves a string from the settings list.
	 * 
	 * @param setting
	 *            Name of the setting
	 * @return A string or null when the setting does not exist.
	 */
	public String getString(final String setting) {
		return config.getProperty(setting);
	}

	/**
	 * Retrieves a float from the settings list.
	 * 
	 * @param setting
	 *            Name of the setting
	 * @return A float or null when the setting does not exist.
	 */
	public Float getFloat(final String setting) {
		return Float.valueOf(config.getProperty(setting));
	}

	/**
	 * Retrieves a boolean from the settings list.
	 * 
	 * @param setting
	 *            Name of the setting
	 * @return A boolean or null when the setting does not exist.
	 */
	public Boolean getBoolean(final String setting) {
		return Boolean.valueOf(config.getProperty(setting));
	}

	/**
	 * Loads the settings from an URL.
	 * 
	 * @param filename
	 *            Name of configuration file
	 * @throws IOException
	 *             Throws an error when the loading or parsing fails.
	 */
	public void loadSettings(final String filename) throws IOException {
		final Reader reader = new FileReader(filename);
		config.load(reader);
		reader.close();
	}
}
