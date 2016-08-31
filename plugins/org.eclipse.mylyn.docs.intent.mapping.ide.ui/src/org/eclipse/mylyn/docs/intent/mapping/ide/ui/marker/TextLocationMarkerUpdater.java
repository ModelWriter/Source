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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.mylyn.docs.intent.mapping.ide.ILocationMarker;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.ui.texteditor.IMarkerUpdater;

/**
 * Updates {@link ITextLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextLocationMarkerUpdater implements IMarkerUpdater {

	/**
	 * The ID of this updater.
	 */
	public static final String ID = "org.eclipse.mylyn.docs.intent.mapping.ide.ui.marker.textLocationMarkerUpdater";

	/**
	 * Updated attributes.
	 */
	private static final String[] ATTRIBUTES = {ILocationMarker.LOCATION_ATTRIBUTE, };

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#getMarkerType()
	 */
	public String getMarkerType() {
		return ILocationMarker.TEXT_LOCATION_ID;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#getAttribute()
	 */
	public String[] getAttribute() {
		return ATTRIBUTES;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#updateMarker(org.eclipse.core.resources.IMarker,
	 *      org.eclipse.jface.text.IDocument, org.eclipse.jface.text.Position)
	 */
	public boolean updateMarker(IMarker marker, IDocument document, Position position) {
		final boolean res;

		if (position != null) {
			res = updateLocation(marker, position);
		} else {
			res = true;
		}

		return res;
	}

	/**
	 * Updates the {@link ITextLocation} corresponding to the given {@link IMarker} witht he given
	 * {@link Position}.
	 * 
	 * @param marker
	 *            the {@link IMarker} to update
	 * @param position
	 *            the new {@link Position}
	 * @return <code>false</code> if the {@link IMarker} should be deleted, <code>true</code> otherwise
	 */
	private boolean updateLocation(IMarker marker, Position position) {
		boolean res;

		try {
			final ITextLocation location = (ITextLocation)marker
					.getAttribute(ILocationMarker.LOCATION_ATTRIBUTE);
			if (position.isDeleted()) {
				// TODO mark location as deleted
				res = false;
			} else {
				final int locationStart = location.getStartOffset();
				final int locationEnd = location.getEndOffset();

				if (locationStart != -1 && locationEnd != -1) {
					int offset = position.getOffset();
					if (locationStart != offset) {
						location.setStartOffset(offset);
					}
					offset += position.getLength();
					if (locationEnd != offset) {
						location.setEndOffset(offset);
					}
				}
				// TODO if the length is 0 then mark location as deleted and remove the marker ?
				res = true;
			}
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			res = true;
		}

		return res;
	}
}
