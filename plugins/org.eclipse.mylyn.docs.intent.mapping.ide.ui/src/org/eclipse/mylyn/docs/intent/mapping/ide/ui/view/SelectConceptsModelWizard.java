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
import org.eclipse.swt.widgets.Composite;

public class SelectConceptsModelWizard extends Wizard {

	public SelectConceptsModelWizard() {
		setWindowTitle("Select Concepts Model");
	}

	@Override
	public void addPages() {
		addPage(new SelectConceptsModelPage("Select Concepts Model"));
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	private class SelectConceptsModelPage extends WizardPage {

		protected SelectConceptsModelPage(String pageName) {
			super(pageName);
			setTitle(pageName);
		}

		public void createControl(Composite parent) {
			final TreeViewer treeViewer = new TreeViewer(parent);
			setControl(parent);
			setPageComplete(true);
		}

	}
}
