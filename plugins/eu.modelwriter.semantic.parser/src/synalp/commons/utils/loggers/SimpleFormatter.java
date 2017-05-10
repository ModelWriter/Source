package synalp.commons.utils.loggers;

import java.util.logging.*;

/**
 * Formats log record by displaying the short class name followed by the message.
 * @author Alexandre Denis
 */
public class SimpleFormatter extends Formatter
{

	@Override
	public String format(LogRecord arg0)
	{
		String className = arg0.getSourceClassName();
		className = className.substring(className.lastIndexOf('.') + 1);
		return className + ": " + arg0.getMessage() + "\n";
	}
	
}
