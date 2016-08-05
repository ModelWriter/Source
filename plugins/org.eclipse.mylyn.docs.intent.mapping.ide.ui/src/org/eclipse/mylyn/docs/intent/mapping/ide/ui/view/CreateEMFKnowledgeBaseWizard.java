/*******************************************************************************
 * Copyright (c) 2016 UNIT Information Technologies R&D Ltd
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ferhat Erata - initial API and implementation
 *     H. Emre Kirmizi - initial API and implementation
 *     Serhat Celik - initial API and implementation
 *     U. Anil Ozturk - initial API and implementation
 *	   Yunus Emre Seker - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateEMFKnowledgeBaseWizard extends Wizard {

	public CreateEMFKnowledgeBaseWizard() {
		setWindowTitle("Create EMF Knowledge Base");
	}

	@Override
	public void addPages() {
		addPage(new CreateEMFKnowledgeBasePage("Create EMF Knowledge Base"));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public class CreateEMFKnowledgeBasePage extends WizardPage {

		protected CreateEMFKnowledgeBasePage(String pageName) {
			super(pageName);
			setTitle(pageName);
		}

		public void createControl(Composite parent) {
			Composite container = new Composite(parent, NONE);
			container.setLayout(new GridLayout(1, false));
			container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

			Composite headerContainer = new Composite(container, SWT.NONE);
			headerContainer.setLayout(new GridLayout(2, false));

			Label fileNameLabel = new Label(headerContainer, SWT.NONE);
			fileNameLabel.setText("File Name:");
			Text fileNameText = new Text(headerContainer, SWT.NONE);

			final TreeViewer treeViewer = new TreeViewer(container);

			setControl(container);
			setPageComplete(true);
		}

	}
}
