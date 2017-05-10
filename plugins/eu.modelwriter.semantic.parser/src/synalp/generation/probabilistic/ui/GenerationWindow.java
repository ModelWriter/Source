package synalp.generation.probabilistic.ui;

import java.awt.BorderLayout;

import synalp.commons.input.TestSuiteEntry;
import synalp.commons.output.MorphRealization;
import synalp.commons.output.SyntacticRealization;
import synalp.commons.utils.Utils;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.generation.Generator;
import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorConfigurations;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import synalp.generation.configuration.GeneratorConfigurations;
import synalp.generation.jeni.JeniGenerator;
import synalp.generation.jeni.JeniRealization;
import synalp.generation.probabilistic.common.AppConfiguration;

import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JProgressBar;
import javax.swing.JLabel;

import org.xml.sax.SAXException;

public class GenerationWindow extends JFrame
{

	private JPanel contentPane;

	JTextArea genTextArea;
	JScrollPane scroll;
	AppConfiguration appConfig;
	boolean generationDone = false;
	boolean stopButtonPressed = false;
	private JButton btnClose_1;
	int entryNum;
	int totalEntries;
	JProgressBar progressBar;
	JLabel lblGenerationInProcess;
	GeneratorThread genThread=null;

	/**
	 * Create the frame.
	 */
	public GenerationWindow(AppConfiguration appConfig)
	{
		this.appConfig = appConfig;
		setTitle("Generation results");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 706, 600);

		genTextArea = new JTextArea();
		genTextArea.setEditable(false);
		genTextArea.setLineWrap(true);
		//genTextArea.setText("");
		genTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		scroll = new JScrollPane(genTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GenerationWindow thisWindow = this;
		JButton btnSave = new JButton("Save ");
		btnSave.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showSaveDialog(thisWindow);

				if (userSelection == JFileChooser.APPROVE_OPTION)
				{
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					try
					{
						thisWindow.saveTextAreaToFile(fileToSave);
					}
					catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

		btnClose_1 = new JButton("Close");
		btnClose_1.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				setVisible(false);
				if (genThread != null) {
					
					genThread.interrupt();;
				}
				dispose();

			}
		});

		progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);

		lblGenerationInProcess = new JLabel("Generation progress:");

		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				 genThread = new GeneratorThread();
				genThread.setWidgetsToUpdate(genTextArea, progressBar, lblGenerationInProcess);
			
					genThread.setConfig(appConfig);
		
				genThread.start();
			}
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
										groupLayout	.createParallelGroup(Alignment.LEADING)
													.addGroup(groupLayout	.createSequentialGroup()
																			.addContainerGap()
																			.addGroup(groupLayout	.createParallelGroup(Alignment.LEADING)
																									.addComponent(lblGenerationInProcess)
																									.addGroup(groupLayout	.createSequentialGroup()
																															.addGroup(groupLayout	.createParallelGroup(Alignment.TRAILING,
																																										false)
																																					.addComponent(	progressBar,
																																									Alignment.LEADING,
																																									GroupLayout.DEFAULT_SIZE,
																																									GroupLayout.DEFAULT_SIZE,
																																									Short.MAX_VALUE)
																																					.addComponent(	scroll,
																																									Alignment.LEADING,
																																									GroupLayout.DEFAULT_SIZE,
																																									575,
																																									Short.MAX_VALUE))
																															.addPreferredGap(ComponentPlacement.RELATED)
																															.addGroup(groupLayout	.createParallelGroup(Alignment.TRAILING,
																																										false)
																																					.addComponent(	btnNewButton,
																																									GroupLayout.DEFAULT_SIZE,
																																									89,
																																									Short.MAX_VALUE)
																																					.addComponent(	btnSave,
																																									GroupLayout.DEFAULT_SIZE,
																																									GroupLayout.DEFAULT_SIZE,
																																									Short.MAX_VALUE)
																																					.addComponent(	btnClose_1,
																																									GroupLayout.DEFAULT_SIZE,
																																									GroupLayout.DEFAULT_SIZE,
																																									Short.MAX_VALUE))))
																			.addContainerGap(29, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(
										groupLayout	.createParallelGroup(Alignment.TRAILING)
													.addGroup(groupLayout	.createSequentialGroup()
																			.addGap(33)
																			.addComponent(lblGenerationInProcess)
																			.addPreferredGap(ComponentPlacement.UNRELATED)
																			.addGroup(groupLayout	.createParallelGroup(Alignment.BASELINE)
																									.addComponent(	progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																													GroupLayout.PREFERRED_SIZE)
																									.addComponent(btnNewButton))
																			.addGap(18)
																			.addGroup(groupLayout	.createParallelGroup(Alignment.BASELINE)
																									.addGroup(groupLayout	.createSequentialGroup()
																															.addComponent(btnSave)
																															.addPreferredGap(	ComponentPlacement.RELATED, 435,
																																				Short.MAX_VALUE)
																															.addComponent(btnClose_1))
																									.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE))
																			.addContainerGap()));
		getContentPane().setLayout(groupLayout);

		JButton btnSaveToFIle = new JButton("Save results as...");
		btnSaveToFIle.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showSaveDialog(thisWindow);

				if (userSelection == JFileChooser.APPROVE_OPTION)
				{
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					try
					{
						thisWindow.saveTextAreaToFile(fileToSave);
					}
					catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

	}


	void saveTextAreaToFile(File file) throws IOException
	{
		String text = genTextArea.getText();
		FileOutputStream output = new FileOutputStream(file);
		for(char c : text.toCharArray())
		{
			output.write((int) c);
		}
		output.close();

	}



}
