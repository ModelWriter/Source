package synalp.commons.utils.loggers;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

/**
 * A ConsoleLogHandler is a ConsoleHandler that logs its outputs to System.out.
 * @author adenis
 *
 */
public class ConsoleLogHandler extends ConsoleHandler
{

	@Override
	protected synchronized void setOutputStream(OutputStream out) throws SecurityException
	{
		super.setOutputStream(System.out);
	}
	
	
}
