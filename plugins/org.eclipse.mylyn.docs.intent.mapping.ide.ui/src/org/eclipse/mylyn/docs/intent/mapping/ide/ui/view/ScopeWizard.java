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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

public class ScopeWizard extends Wizard {

	public ScopeWizard() {
		setWindowTitle("Scope");
	}

	@Override
	public void addPages() {
		addPage(new ScopePage());
		addPage(new SelectLocationPage());
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public class ScopePage extends WizardPage {

		/**
		 * Create the wizard.
		 */
		public ScopePage() {
			super("ScopePage");
			setTitle("Scope");
		}

		/**
		 * Create contents of the wizard.
		 * 
		 * @param parent
		 */
		public void createControl(Composite parent) {
			Composite container = new Composite(parent, SWT.NULL);

			final TreeViewer treeViewer = new TreeViewer(container);
			setControl(container);
			setPageComplete(true);
		}

	}

	public class SelectLocationPage extends WizardPage {

		/**
		 * Create the wizard.
		 */
		public SelectLocationPage() {
			super("SelectLocationPage");
			setTitle("Select Location");
		}

		/**
		 * Create contents of the wizard.
		 * 
		 * @param parent
		 */
		public void createControl(Composite parent) {
			Composite container = new Composite(parent, SWT.NULL);
			final FilteredTree selectLocationTree = new FilteredTree(container, SWT.BORDER,
					new PatternFilter(), false);
			setControl(container);
			setPageComplete(true);
		}

	}

}
