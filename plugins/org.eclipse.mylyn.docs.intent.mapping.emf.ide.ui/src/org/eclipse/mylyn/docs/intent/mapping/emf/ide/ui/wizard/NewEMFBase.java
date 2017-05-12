/*******************************************************************************
 * Copyright (c) 2016 0beo.
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

import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui.Activator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Creates a {@link Base}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class NewEMFBase extends Wizard implements IWorkbenchWizard {

	/**
	 * The {@link NewEMFBasePage}.
	 */
	protected NewEMFBasePage page;

	/**
	 * The default file name.
	 */
	private String defaultFileName;

	/**
	 * Constructor.
	 */
	public NewEMFBase() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		page = new NewEMFBasePage(defaultFileName);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		boolean res = false;

		final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		base.setName(page.getMappingBaseName());
		final ResourceSet rs = new ResourceSetImpl();
		final Resource r = rs.createResource(URI.createPlatformResourceURI(page.getFileName(), true));
		r.getContents().add(base);
		try {
			r.save(null);
			res = true;
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"unable to save base " + page.getFileName()));

		}

		dispose();

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		final IContainer container;

		if (selection.getFirstElement() instanceof IFile) {
			container = ((IFile)selection.getFirstElement()).getParent();
			defaultFileName = container.getFullPath().toString() + "/";
		} else if (selection.getFirstElement() instanceof IContainer) {
			container = (IContainer)selection.getFirstElement();
			defaultFileName = container.getFullPath().toString() + "/";
		}

	}
}
