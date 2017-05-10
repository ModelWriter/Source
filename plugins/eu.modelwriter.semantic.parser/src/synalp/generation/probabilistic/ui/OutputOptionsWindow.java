package synalp.generation.probabilistic.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import synalp.generation.probabilistic.common.AppConfiguration;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OutputOptionsWindow extends JFrame
{

	private JPanel contentPane;
AppConfiguration appConfig;

	

	/**
	 * Create the frame.
	 */
	public OutputOptionsWindow(AppConfiguration appConfig)
	{
		this.appConfig = appConfig;
		setTitle("Choose type of output");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 222, 121);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JRadioButton radioButtonCompact = new JRadioButton("Compact");
		
		JRadioButton radioButtonVerbose = new JRadioButton("Verbose");
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (radioButtonCompact.isSelected())
					appConfig.setVerboseOutput(false);
				else if (radioButtonVerbose.isSelected())
					appConfig.setVerboseOutput(true);
				else {
					System.err.println("ERROR: No radio button was chosen!");
				}
				setVisible(false);
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(radioButtonCompact);
		group.add(radioButtonVerbose);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnClose)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(radioButtonCompact)
							.addGap(18)
							.addComponent(radioButtonVerbose)))
					.addContainerGap(244, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(radioButtonCompact)
						.addComponent(radioButtonVerbose))
					.addGap(18)
					.addComponent(btnClose)
					.addContainerGap(188, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
