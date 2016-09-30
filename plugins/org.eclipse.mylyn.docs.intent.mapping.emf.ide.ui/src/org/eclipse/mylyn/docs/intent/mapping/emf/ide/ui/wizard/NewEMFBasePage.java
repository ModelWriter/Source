/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * EMF {@link org.eclipse.mylyn.docs.intent.mapping.Base Base} creation page.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class NewEMFBasePage extends WizardPage {

	/**
	 * The default file name.
	 */
	private final String defaultFileName;

	/**
	 * The base name {@link Text}.
	 */
	private Text baseNameText;

	/**
	 * The file name {@link Text}.
	 */
	private Text fileNameText;

	/**
	 * Create the wizard.
	 * 
	 * @param defaultFileName
	 *            the default file name
	 */
	public NewEMFBasePage(String defaultFileName) {
		super("Create EMF Mapping Base");
		this.defaultFileName = defaultFileName;
		setTitle("Create EMF Mapping Base");
		setDescription("Enter information to create a new EMF mapping base");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 *            the parent {@link Composite}
	 */
	public void createControl(Composite parent) {
		KeyListener listener = new KeyListener() {

			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				// nothing to do here
			}

			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
				check();
			}

		};

		final Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		final Label baseNameLabel = new Label(container, SWT.NONE);
		baseNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		baseNameLabel.setText("Mapping base name:");

		baseNameText = new Text(container, SWT.BORDER);
		baseNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		baseNameText.addKeyListener(listener);

		final Label fileNameLabel = new Label(container, SWT.NONE);
		fileNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		fileNameLabel.setText("File name:");

		fileNameText = new Text(container, SWT.BORDER);
		fileNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileNameText.addKeyListener(listener);
		if (defaultFileName != null) {
			fileNameText.setText(defaultFileName);
		}

		check();
	}

	/**
	 * Gets the {@link org.eclipse.mylyn.docs.intent.mapping.Base#getName() name}.
	 * 
	 * @return the {@link org.eclipse.mylyn.docs.intent.mapping.Base#getName() name}
	 */
	public String getMappingBaseName() {
		return baseNameText.getText();
	}

	/**
	 * Gets the file name.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return fileNameText.getText();
	}

	/**
	 * Sets the file name.
	 * 
	 * @param name
	 *            the file name
	 */
	public void setFileName(String name) {
		fileNameText.setText(name);
	}

	/**
	 * Checks the page.
	 */
	private void check() {
		final StringBuilder builder = new StringBuilder();
		if (baseNameText.getText().isEmpty()) {
			builder.append("The mapping base name can't be empty.");
			if (fileNameText.getText().isEmpty()) {
				builder.append("\n");
			}
		}
		if (fileNameText.getText().isEmpty() || !fileNameText.getText().endsWith(".mapping")) {
			builder.append("The file name can't be empty and must finish with \".mapping\"");
		}
		if (builder.length() == 0) {
			setErrorMessage(null);
			setPageComplete(true);
		} else {
			setErrorMessage(builder.toString());
			setPageComplete(false);
		}
	}

}
