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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Mapping key up listener for {@link Tree}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
final class MappingKeyUpListener implements Listener {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.character == SWT.DEL) {
			deleteReports(((Tree)event.widget).getSelection());
			deleteLinks(((Tree)event.widget).getSelection());
			deleteLocations(((Tree)event.widget).getSelection());
		}
	}

	/**
	 * Deletes selected {@link IReport}.
	 * 
	 * @param selection
	 *            the selection
	 */
	private void deleteReports(TreeItem[] selection) {
		final List<IReport> reportsToDelete = new ArrayList<IReport>();
		for (TreeItem item : selection) {
			final Object data = item.getData();
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
	private void deleteLinks(TreeItem[] selection) {
		final List<ILink> linksToDelete = new ArrayList<ILink>();
		for (TreeItem item : selection) {
			final Object data = item.getData();
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
	private void deleteLocations(TreeItem[] selection) {
		final List<ILocation> locationsToDelete = new ArrayList<ILocation>();

		for (TreeItem item : selection) {
			final Object data = item.getData();
			if (data instanceof ILocation && MappingUtils.canDeleteLocation((ILocation)data)) {
				locationsToDelete.add((ILocation)data);
			}
		}

		for (ILocation location : locationsToDelete) {
			MappingUtils.deleteLocation(location);
		}
	}

}
