package synalp.commons.utils.console;

/**
 * Static methods for the console
 * @author Céline Moro
 *
 */
public class ConsoleUtils{


	/**
	 * Method to display the user's prompt string
	 * @param prompt
	 */
	public static void printPrompt(String prompt) {
		System.out.print(prompt + " ");
		System.out.flush();
	}

	/**
	 * Method to make sure no data is available in the input stream
	 */
	public static void inputFlush() {
		try {
			while ((System.in.available()) != 0)
				System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input error");
		}
	}

	/**
	 * Data input methods for String
	 * @param prompt
	 * @return input in String
	 */
	public static String inString(String prompt) {
		inputFlush();
		printPrompt(prompt);
		return inString();
	}

	/**
	 * Data input methods for String
	 * @return input in String
	 */
	public static String inString() {
		int aChar;
		String s = "";
		boolean finished = false;

		while (!finished) {
			try {
				aChar = System.in.read();
				if (aChar < 0 || (char) aChar == '\n')
					finished = true;
				else if ((char) aChar != '\r')
					s = s + (char) aChar; // Enter into string
			}

			catch (java.io.IOException e) {
				System.out.println("Input error");
				finished = true;
			}
		}
		return s;
	}

	/**
	 * Data input methods for int
	 * @param prompt
	 * @return input in int
	 */
	public static int inInt(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Integer.valueOf(inString().trim()).intValue();
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Not an integer");
			}
		}
	}

	/**
	 * Data input methods for char
	 * @param prompt
	 * @return input in char
	 */
	public static char inChar(String prompt) {
		int aChar = 0;

		inputFlush();
		printPrompt(prompt);

		try {
			aChar = System.in.read();
		}

		catch (java.io.IOException e) {
			System.out.println("Input error");
		}
		inputFlush();
		return (char) aChar;
	}

	/**
	 * Data input methods for double
	 * @param prompt
	 * @return in double
	 */
	public static double inDouble(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Double.valueOf(inString().trim()).doubleValue();
			}

			catch (NumberFormatException e) {
				System.out
						.println("Invalid input. Not a floating point number");
			}
		}
	}
}
