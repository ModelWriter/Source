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

import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.UiIdeMappingUtils;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * {@link Listener} that open {@link ILocation}, {@link ILink}, and {@link IReport}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ShowLocationDoubleClickListener implements Listener {

	/**
	 * the {@link Tree} to listen to.
	 */
	private final Tree tree;

	/**
	 * Constructor.
	 * 
	 * @param tree
	 *            the {@link Tree} to listen to
	 */
	public ShowLocationDoubleClickListener(Tree tree) {
		this.tree = tree;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		Point pt = new Point(event.x, event.y);
		TreeItem item = tree.getItem(pt);
		if (item != null) {
			final Object selected = item.getData();
			if (selected instanceof ILocation) {
				UiIdeMappingUtils.showLocation((ILocation)selected);
			} else if (selected instanceof ILink) {
				showLink(pt, item, (ILink)selected);
			} else if (selected instanceof IReport) {
				UiIdeMappingUtils.showLocation(((IReport)selected).getLink().getSource());
			}
		}
	}

	/**
	 * Shows the given {@link ILink} {@link ILink#getSource() source} or {@link ILink#getTarget() target}
	 * according to the double click position.
	 * 
	 * @param point
	 *            the {@link Point}
	 * @param item
	 *            the {@link TreeItem}
	 * @param link
	 *            the {@link ILink}
	 */
	private void showLink(Point point, TreeItem item, ILink link) {
		for (int i = 0; i < tree.getColumnCount(); i++) {
			Rectangle rect = item.getBounds(i);
			if (rect.contains(point)) {
				if (i == 0) {
					UiIdeMappingUtils.showLocation(link.getSource());
				} else {
					UiIdeMappingUtils.showLocation(link.getTarget());
				}
				break;
			}
		}
	}

}
