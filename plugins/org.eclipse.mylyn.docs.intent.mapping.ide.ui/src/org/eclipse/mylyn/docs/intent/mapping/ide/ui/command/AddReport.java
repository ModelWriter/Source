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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Add an {@link org.eclipse.mylyn.docs.intent.mapping.base.IReport IReport} to the selected {@link ILink}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class AddReport extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			final Object element = ((IStructuredSelection)selection).getFirstElement();
			if (element instanceof ILink) {
				final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				MultiLineTextDialog dialog = new MultiLineTextDialog(shell, "");
				if (dialog.open() == Window.OK) {
					try {
						MappingUtils.createReport((ILink)element, dialog.getText());
					} catch (InstantiationException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					} catch (IllegalAccessException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					} catch (ClassNotFoundException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
				}
			}
		}

		return null;
	}

}
