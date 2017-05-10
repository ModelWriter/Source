package synalp.generation.probabilistic.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomOutputDialog2 extends JDialog
{

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public CustomOutputDialog2()
	{
		setTitle("Output options");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JRadioButton rdbtnFullOutput = new JRadioButton("Full output");
		rdbtnFullOutput.setSelected(true);

		JRadioButton rdbtnShowJustSentences = new JRadioButton("Show sentences and info about:");
		rdbtnShowJustSentences.setEnabled(false);

		JCheckBox chckbxProbabilities = new JCheckBox("Probabilities");
		chckbxProbabilities.setSelected(true);

		JCheckBox chckbxTimeElapsed = new JCheckBox("Input benchmark");

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnFullOutput);
		group.add(rdbtnShowJustSentences);

		JCheckBox chckbxDebugInfo = new JCheckBox("Sources files");

		JCheckBox chckbxInputSemantics = new JCheckBox("Input semantics");
		chckbxInputSemantics.setSelected(true);

		JCheckBox chckbxTimeDay = new JCheckBox("Time & day");
		chckbxTimeDay.setSelected(true);

		JCheckBox chckbxBeamSize = new JCheckBox("Beam size");
		chckbxBeamSize.setSelected(true);

		JCheckBox chckbxNInitialItems = new JCheckBox("Nº initial items");

		JCheckBox chckbxTestItem = new JCheckBox("Test item label");
		chckbxTestItem.setSelected(true);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
						gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(	gl_contentPanel.createSequentialGroup()
																	.addContainerGap()
																	.addGroup(	gl_contentPanel.createParallelGroup(Alignment.LEADING)
																								.addGroup(	gl_contentPanel.createSequentialGroup()
																															.addGroup(	gl_contentPanel.createParallelGroup(Alignment.LEADING)
																																						.addComponent(	rdbtnFullOutput)
																																						.addComponent(	rdbtnShowJustSentences))
																															.addContainerGap(239, Short.MAX_VALUE))
																								.addGroup(	gl_contentPanel.createSequentialGroup()
																															.addGap(29)
																															.addGroup(	gl_contentPanel.createParallelGroup(Alignment.LEADING)
																																						.addComponent(	chckbxProbabilities)
																																						.addComponent(	chckbxTimeElapsed)
																																						.addComponent(	chckbxDebugInfo)
																																						.addComponent(	chckbxInputSemantics))
																															.addPreferredGap(ComponentPlacement.RELATED, 39,
																																				Short.MAX_VALUE)
																															.addGroup(	gl_contentPanel.createParallelGroup(Alignment.LEADING)
																																						.addComponent(chckbxTimeDay)
																																						.addComponent(	chckbxBeamSize)
																																						.addComponent(	chckbxNInitialItems)
																																						.addComponent(	chckbxTestItem))
																															.addContainerGap(48, Short.MAX_VALUE))))
						);
		gl_contentPanel.setVerticalGroup(
						gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPanel.createSequentialGroup()
																	.addContainerGap()
																	.addComponent(rdbtnFullOutput)
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addComponent(rdbtnShowJustSentences)
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
																								.addComponent(chckbxProbabilities)
																								.addComponent(chckbxTimeDay))
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
																								.addComponent(chckbxTimeElapsed)
																								.addComponent(chckbxBeamSize))
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
																								.addComponent(chckbxDebugInfo)
																								.addComponent(chckbxNInitialItems))
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
																								.addComponent(chckbxInputSemantics)
																								.addComponent(chckbxTestItem))
																	.addContainerGap(25, Short.MAX_VALUE))
						);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						setVisible(false);
					}
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
			}
		}
	}
}
