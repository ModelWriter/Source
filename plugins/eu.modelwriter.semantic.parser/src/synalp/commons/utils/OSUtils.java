package synalp.commons.utils;

/**
 * Utilities class to detect current OS
 * @author Céline Moro
 *
 */
public final class OSUtils {

	private static String OS = null;

	/**
	 * Returns the name of the current OS
	 * @return name of the current OS
	 */
	public static String getOsName()
	{
		if(OS == null) 
			OS = System.getProperty("os.name"); 
		return OS;
	}

	/**
	 * Returns <code>True</code> if The current OS is a Linux system
	 * @return <code>True</code> if The current OS is a Linux system, <code>False</code> otherwise
	 */
	public static boolean isLinux() {
		return getOsName().toLowerCase().indexOf("linux") >= 0;
	}
	
	/**
	 * Returns <code>True</code> if The current OS is a solaris system
	 * @return <code>True</code> if The current OS is a solaris system, <code>False</code> otherwise
	 */
	public static boolean isSolaris() {
	    final String os = getOsName().toLowerCase();
	    return os.indexOf("sunos") >= 0;
	  }

	/**
	 * Returns <code>True</code> if The current OS is an Unix system
	 * @return <code>True</code> if The current OS is an Unix system, <code>False</code> otherwise
	 */
	public static boolean isUnix() {
		final String os = getOsName().toLowerCase();
		if ((os.indexOf("sunos") >= 0) || (os.indexOf("linux") >= 0)) 
			return true;

		if (isMac() && (System.getProperty("os.version", "").startsWith("10."))) 
			return true;

		return false;
	}

	/**
	 * Returns <code>True</code> if The current OS is a MacOs system
	 * @return <code>True</code> if The current OS is a MacOs system, <code>False</code> otherwise
	 */
	public static boolean isMac() {
		final String os = getOsName().toLowerCase();
		return os.startsWith("mac") || os.startsWith("darwin");
	}

	/**
	 * Returns <code>True</code> if The current OS is a Windows system
	 * @return <code>True</code> if The current OS is a Windows system, <code>False</code> otherwise
	 */
	public static boolean isWindows() {
		return (getOsName().toLowerCase().indexOf("windows") >= 0);
	}


}
