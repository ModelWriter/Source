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
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;

/**
 * Text {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocation> containerType, Object element) {
		final Class<? extends ILocation> res;

		if (ITextContainer.class.isAssignableFrom(containerType) && element instanceof String) {
			res = ITextLocation.class;
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocation location, Object element) {
		final ITextLocation toInit = (ITextLocation)location;

		final String text = ((ITextContainer)location.getContainer()).getText();

		toInit.setText((String)element);
		// FIXME this is bogus we need more than the String itself to initialize the ITextLocation
		// in case of duplicates for instance
		toInit.setTextOffset(text.indexOf((String)element));
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
					final int oldLength = location.getText().length();
					int oldOffset = location.getTextOffset();
					int newStartOffset = diff.getIndex(oldOffset);
					int newEndOffset = diff.getIndex(oldOffset + oldLength);
					location.setText(text.substring(newStartOffset, newEndOffset));
					location.setTextOffset(newStartOffset);
				}
			}
		}
	}

}
