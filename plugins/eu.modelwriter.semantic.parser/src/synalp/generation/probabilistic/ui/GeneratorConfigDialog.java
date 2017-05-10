package synalp.generation.probabilistic.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import synalp.generation.probabilistic.common.AppConfiguration;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FilenameFilter;
import javax.swing.SwingConstants;

public class GeneratorConfigDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();

	private JTextField grammarTextField;
	private JTextField lexiconTextField;
	private JTextField testsuiteTextField;

	private AppConfiguration appConfig;
	private JTextField beamSizeField;


	/**
	 * Create the dialog.
	 */
	public GeneratorConfigDialog(AppConfiguration appConfig)
	{
		this.appConfig = appConfig;

		setTitle("Generator Configuration");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		setGrammarTextField(new JTextField());
		getGrammarTextField().setText(appConfig.getGrammarSource());
		getGrammarTextField().setEditable(false);
		getGrammarTextField().setColumns(10);

		setLexiconTextField(new JTextField());
		getLexiconTextField().setText(appConfig.getLexiconSource());
		
		getLexiconTextField().setEditable(false);
		getLexiconTextField().setColumns(10);

		setTestsuiteTextField(new JTextField());
		getTestsuiteTextField().setText(appConfig.getTestsuiteSource());
		getTestsuiteTextField().setEditable(false);
		getTestsuiteTextField().setColumns(10);

		JLabel lblGrammarFile = new JLabel("Grammar");

		JLabel lblLexiconFile = new JLabel("Lexicon");

		JLabel lblTestSuite = new JLabel("Testsuite");

		JButton btnBrowse = new JButton("Browse");
		GeneratorConfigDialog thisDialog = this;
		btnBrowse.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
				FileDialog fd = new FileDialog(thisDialog, "Choose a grammar file", FileDialog.LOAD);

				fd.setVisible(true);
				if (fd.getDirectory()!= null &&  fd.getFile()!=null)
					getGrammarTextField().setText(fd.getDirectory() + fd.getFile());
				
			}
		});

		JButton button = new JButton("Browse");

		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
				FileDialog fd = new FileDialog(thisDialog, "Choose a lexicon file", FileDialog.LOAD);

				fd.setVisible(true);
				if (fd.getDirectory()!= null &&  fd.getFile()!=null)
				getLexiconTextField().setText(fd.getDirectory() + fd.getFile());
				
				

			}
		});

		JButton button_1 = new JButton("Browse");

		button_1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
				FileDialog fd = new FileDialog(thisDialog, "Choose a lexicon file", FileDialog.LOAD);

				fd.setVisible(true);
				if (fd.getDirectory()!= null &&  fd.getFile()!=null)
				getTestsuiteTextField().setText(fd.getDirectory() + fd.getFile());
			}
		});
		
		JLabel lblBeamSize = new JLabel("Beam size");
		
		beamSizeField = new JTextField();
		beamSizeField.setHorizontalAlignment(SwingConstants.RIGHT);
		beamSizeField.setText("5");
		beamSizeField.setColumns(2);
		
	

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(getLexiconTextField(), GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
								.addComponent(getGrammarTextField())
								.addComponent(getTestsuiteTextField()))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBrowse)))
						.addComponent(lblGrammarFile)
						.addComponent(lblLexiconFile)
						.addComponent(lblTestSuite)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblBeamSize)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(beamSizeField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(17)
					.addComponent(lblGrammarFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(getGrammarTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse))
					.addGap(24)
					.addComponent(lblLexiconFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(getLexiconTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblTestSuite)
					.addGap(4)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(getTestsuiteTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_1))
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBeamSize)
						.addComponent(beamSizeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Close");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if (!getGrammarTextField().getText().isEmpty() && !getLexiconTextField().getText().isEmpty())
						{
							if (!getTestsuiteTextField().getText().isEmpty()) {
								
								appConfig.setConfiguration(getGrammarTextField().getText(), getLexiconTextField().getText(), getTestsuiteTextField().getText());
							}
							else
								appConfig.setConfiguration(getGrammarTextField().getText(), getLexiconTextField().getText());
						}
						appConfig.setBeamSize(beamSizeField.getText());
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}


	/**
	 * @return the grammarTextField
	 */
	public JTextField getGrammarTextField()
	{
		return grammarTextField;
	}


	/**
	 * @param grammarTextField the grammarTextField to set
	 */
	private void setGrammarTextField(JTextField grammarTextField)
	{
		this.grammarTextField = grammarTextField;
	}


	/**
	 * @return the lexiconTextField
	 */
	public JTextField getLexiconTextField()
	{
		return lexiconTextField;
	}


	/**
	 * @param lexiconTextField the lexiconTextField to set
	 */
	private void setLexiconTextField(JTextField lexiconTextField)
	{
		this.lexiconTextField = lexiconTextField;
	}


	/**
	 * @return the testsuiteTextField
	 */
	public JTextField getTestsuiteTextField()
	{
		return testsuiteTextField;
	}


	/**
	 * @param testsuiteTextField the testsuiteTextField to set
	 */
	private void setTestsuiteTextField(JTextField testsuiteTextField)
	{
		this.testsuiteTextField = testsuiteTextField;
	}
}
