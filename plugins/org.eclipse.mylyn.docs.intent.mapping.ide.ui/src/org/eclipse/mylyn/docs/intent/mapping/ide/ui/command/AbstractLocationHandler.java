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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract implementation of handler for {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLocationHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		final List<ILocation> locations = new ArrayList<ILocation>();
		if (selection instanceof IStructuredSelection) {

			for (Object selected : ((IStructuredSelection)selection).toList()) {
				final ILocation location = IdeMappingUtils.adapt(selected, ILocation.class);
				if (location != null) {
					locations.add(location);
				}
			}

		} else {
			final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
			if (location != null) {
				locations.add(location);
			}
		}

		for (ILocation location : locations) {
			handleLocation(location);
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
				final ILocation location = IdeMappingUtils.adapt(selected, ILocation.class);
				if (location != null) {
					if (canHandleLocation(location)) {
						enable = true;
						break;
					}
				}
			}
		} else {
			final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
			enable = location != null && canHandleLocation(location);
		}

		setBaseEnabled(enable);
	}

	/**
	 * Handles the given {@link ILocation} by applying needed operation.
	 * 
	 * @param location
	 *            the {@link ILocation} to handle
	 */
	protected abstract void handleLocation(ILocation location);

	/**
	 * Tells if the given {@link ILocation} can be handled.
	 * 
	 * @param location
	 *            the {@link ILocation} to check
	 * @return <code>true</code> if the given {@link ILocation} can be handled, <code>false</code> otherwise
	 */
	protected abstract boolean canHandleLocation(ILocation location);

}
