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
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.ui.PlatformUI;

/**
 * Deletes the selected {@link ILink}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class Delete extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			deleteReports(((IStructuredSelection)selection).toList());
			deleteLinks(((IStructuredSelection)selection).toList());
			deleteLocations(((IStructuredSelection)selection).toList());
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
				if (selected instanceof IReport) {
					enable = true;
					break;
				}
			}
			if (!enable) {
				for (Object selected : ((IStructuredSelection)selection).toList()) {
					if (selected instanceof ILink && MappingUtils.canDeleteLink((ILink)selected)) {
						enable = true;
						break;
					}
				}
			}
			if (!enable) {
				for (Object selected : ((IStructuredSelection)selection).toList()) {
					if (selected instanceof ILocation && MappingUtils.canDeleteLocation((ILocation)selected)) {
						enable = true;
						break;
					}
				}
			}
		}

		setBaseEnabled(enable);
	}

	/**
	 * Deletes selected {@link IReport}.
	 * 
	 * @param selection
	 *            the selection
	 */
	private void deleteReports(List<?> selection) {
		final List<IReport> reportsToDelete = new ArrayList<IReport>();
		for (Object data : selection) {
			if (data instanceof IReport) {
				reportsToDelete.add((IReport)data);
			}
		}

		for (IReport report : reportsToDelete) {
			MappingUtils.deleteReport(report);
		}
	}

	/**
	 * Deletes selected {@link ILink}.
	 * 
	 * @param selection
	 *            the selection
	 */
	private void deleteLinks(List<?> selection) {
		final List<ILink> linksToDelete = new ArrayList<ILink>();
		for (Object data : selection) {
			if (data instanceof ILink && MappingUtils.canDeleteLink((ILink)data)) {
				linksToDelete.add((ILink)data);
			}
		}

		for (ILink link : linksToDelete) {
			MappingUtils.deleteLink(link);
		}
	}

	/**
	 * Deletes selected {@link ILocation}.
	 * 
	 * @param selection
	 *            the selection
	 */
	private void deleteLocations(List<?> selection) {
		final List<ILocation> locationsToDelete = new ArrayList<ILocation>();

		for (Object data : selection) {
			if (data instanceof IReport) {
				locationsToDelete.add((ILocation)data);
			}
		}

		for (ILocation location : locationsToDelete) {
			MappingUtils.deleteLocation(location);
		}
	}

}
