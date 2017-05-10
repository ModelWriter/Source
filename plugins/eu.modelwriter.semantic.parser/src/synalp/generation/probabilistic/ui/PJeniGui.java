package synalp.generation.probabilistic.ui;

import java.awt.EventQueue;

import synalp.commons.utils.loggers.LoggerConfiguration;

public class PJeniGui
{
	static
	{
		LoggerConfiguration.init();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AppWindow frame = new AppWindow();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}
}
