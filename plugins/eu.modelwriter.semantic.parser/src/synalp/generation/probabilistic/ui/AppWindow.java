package synalp.generation.probabilistic.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JMenu;

import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorConfigurations;
import synalp.generation.probabilistic.common.AppConfiguration;

public class AppWindow extends JFrame
{

	private JPanel contentPane;
	//different configuration dialogs
	OutputOptionsWindow displayOpt;
	GeneratorConfigDialog resConfig;

	AppConfiguration appConfig;
	Map<String, String> predefinedInputs;
	

	/**
	 * Create the frame.
	 */
	public AppWindow()
	{
		predefinedInputs = new HashMap();
	/*	try
		{
			loadSample("sample.geni");
		}
		catch (FileNotFoundException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
*/
		appConfig = new AppConfiguration();

		setTitle("Probabilistic Jeni Generator Demo");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 410);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu AppMenu = new JMenu("Application");
		menuBar.add(AppMenu);
		resConfig = new GeneratorConfigDialog(appConfig);

		//create windows
		displayOpt = new OutputOptionsWindow(appConfig);
		AppWindow thisWindow = this;
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{

				thisWindow.dispose();
			}
		});
		AppMenu.add(mntmQuit);

		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);

		JMenuItem mntmDisplayOptions = new JMenuItem("Customize output");
		optionsMenu.add(mntmDisplayOptions);
		mntmDisplayOptions.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				displayOpt.setVisible(true);

			}
		});

		JMenuItem mntmResourcesSuite = new JMenuItem("Generator config");
		optionsMenu.add(mntmResourcesSuite);

		mntmResourcesSuite.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				resConfig.setVisible(true);

			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JRadioButton radiobuttonInputFile = new JRadioButton("Chosen testsuite");
		radiobuttonInputFile.setSelected(true);

		JLabel lblGenerateFrom = new JLabel("Generate from...");

		JRadioButton rdbtnUserGivenInput = new JRadioButton("or write the input semantics. E.g.: rel(r x y) s(x) o(y)");

		JRadioButton rdbtnPreparatedInputItems = new JRadioButton("or choose a sample in the list below:");
		rdbtnPreparatedInputItems.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		ButtonGroup group = new ButtonGroup();

		group.add(radiobuttonInputFile);
		group.add(rdbtnUserGivenInput);
		group.add(rdbtnPreparatedInputItems);

		JButton btnGenerate = new JButton("Use this configuration");
		JEditorPane editorPane = new JEditorPane();

		JComboBox comboBox = new JComboBox(this.predefinedInputs.keySet().toArray());

		

		
		//button handlers for opening the corresponding windows 
		btnGenerate.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (btnGenerate.isEnabled())
				{
					

					if (rdbtnUserGivenInput.isSelected())
					{
						try
						{

							thisWindow.appConfig.setUserInput(2, editorPane.getText());
						}
						catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (rdbtnPreparatedInputItems.isSelected())
					{
						try
						{

							System.out.println((String) comboBox.getSelectedItem());
							System.out.println(predefinedInputs.get((String) comboBox.getSelectedItem()));
							thisWindow.appConfig.setUserInputForPredefinedSample(1, (String) comboBox.getSelectedItem(), predefinedInputs.get((String) comboBox.getSelectedItem()));
						}
						catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
					else if (radiobuttonInputFile.isSelected())
					{
						thisWindow.appConfig.setUserInputType(0);
						thisWindow.appConfig.setConfiguration(	resConfig.getGrammarTextField().getText(), resConfig.getLexiconTextField().getText(),
																resConfig.getTestsuiteTextField().getText());

					}
					else {
						System.err.println("error");
						System.exit(1);
					}

					GenerationWindow resultWin = new GenerationWindow(appConfig);
					resultWin.setVisible(true);
				}

			}

		});

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
									gl_panel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panel	.createSequentialGroup()
																.addContainerGap()
																.addGroup(gl_panel	.createParallelGroup(Alignment.LEADING)
																					.addGroup(gl_panel	.createSequentialGroup()
																										.addComponent(radiobuttonInputFile)
																										.addPreferredGap(ComponentPlacement.RELATED, 319, Short.MAX_VALUE))
																					.addGroup(gl_panel	.createSequentialGroup()
																										.addGap(21)
																										.addComponent(comboBox, 0, 445, Short.MAX_VALUE))
																					.addComponent(rdbtnPreparatedInputItems)
																					.addComponent(rdbtnUserGivenInput)
																					.addComponent(editorPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																					.addComponent(lblGenerateFrom))
																.addContainerGap()));
		gl_panel.setVerticalGroup(
									gl_panel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panel	.createSequentialGroup()
																.addComponent(lblGenerateFrom)
																.addGap(20)
																.addComponent(radiobuttonInputFile)
																.addGap(18)
																.addComponent(rdbtnPreparatedInputItems)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(rdbtnUserGivenInput)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(editorPane, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
																.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
											gl_contentPane	.createParallelGroup(Alignment.LEADING)
															.addGroup(gl_contentPane.createSequentialGroup()
																					.addGap(282)
																					.addComponent(btnGenerate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																					.addGap(59))
															.addGroup(gl_contentPane.createSequentialGroup()
																					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 488, GroupLayout.PREFERRED_SIZE)
																					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(
										gl_contentPane	.createParallelGroup(Alignment.TRAILING)
														.addGroup(gl_contentPane.createSequentialGroup()
																				.addGap(3)
																				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
																				.addComponent(btnGenerate)
																				.addContainerGap()));
		contentPane.setLayout(gl_contentPane);

	}


	private void loadSample(String filename) throws FileNotFoundException
	{
		InputStream file = this.getClass().getResourceAsStream("resources/sample.geni");
		Scanner sampleFile = new Scanner(file);
		while(sampleFile.hasNextLine())
		{
			String sampleName = sampleFile.nextLine();

			String semantics = sampleFile.nextLine();
			semantics = semantics.split("\\[")[1];
			semantics = semantics.split("\\]")[0];
			predefinedInputs.put(sampleName, semantics);
			sampleFile.nextLine();//skip "\n"

		}
		sampleFile.close();
	}

}
