/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.text;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils.DiffMatch;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;

/**
 * Text {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextConnector extends AbstractConnector {

	@Override
	protected Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (ITextContainer.class.isAssignableFrom(containerType) && element instanceof TextRegion) {
			res = ITextLocation.class;
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocation location, Object element) {
		final ITextLocation toInit = (ITextLocation)location;
		final TextRegion region = (TextRegion)element;

		toInit.setStartOffset(region.getStartOffset());
		toInit.setEndOffset(region.getEndOffset());
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final ITextLocation textLocation = (ITextLocation)location;
		final TextRegion region = (TextRegion)element;

		return textLocation.getStartOffset() == region.getStartOffset()
				&& textLocation.getEndOffset() == region.getEndOffset();
	}

	/**
	 * Updates the given {@link ITextContainer} with the given {@link ITextContainer#getText() text}.
	 * 
	 * @param container
	 *            the {@link ITextContainer}
	 * @param text
	 *            the {@link ITextContainer#getText() text}
	 */
	public void update(ITextContainer container, String text) {
		final String oldText = container.getText();
		container.setText(text);
		if (oldText != null) {
			final DiffMatch diff = MappingUtils.getDiffMatch(oldText, text);
			for (ILocation child : container.getContents()) {
				if (child instanceof ITextLocation) {
					final ITextLocation location = (ITextLocation)child;
					final int newStartOffset = diff.getIndex(location.getStartOffset());
					final int newEndOffset = diff.getIndex(location.getEndOffset());
					location.setStartOffset(newStartOffset);
					location.setEndOffset(newEndOffset);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		if (location instanceof ITextLocation) {
			final String text = ((ITextContainer)location.getContainer()).getText();
			final int start = ((ITextLocation)location).getStartOffset();
			final int end = ((ITextLocation)location).getEndOffset();
			res = "\"" + text.substring(start, end) + "\"";
		} else {
			res = null;
		}

		return res;
	}

}
