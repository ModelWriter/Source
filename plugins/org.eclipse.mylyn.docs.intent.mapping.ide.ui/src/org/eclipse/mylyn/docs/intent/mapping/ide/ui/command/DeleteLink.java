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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.ui.PlatformUI;

/**
 * Deletes the selected {@link ILink}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class DeleteLink extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			for (Object selected : ((IStructuredSelection)selection).toList()) {
				if (selected instanceof ILink) {
					final ILink link = (ILink)selected;
					if (link.getReports().isEmpty()) {
						link.setSource(null);
						link.setTarget(null);
					}
				}
			}
		}

		return null;
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		super.setEnabled(evaluationContext);
		boolean enable = false;

		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			for (Object selected : ((IStructuredSelection)selection).toList()) {
				if (selected instanceof ILink) {
					final ILink link = (ILink)selected;
					if (link.getReports().isEmpty()) {
						enable = true;
						break;
					}
				}
			}
		}

		setBaseEnabled(enable);
	}

}
