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
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;

/**
 * Text {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (ITextContainer.class.isAssignableFrom(containerType) && element instanceof TextRegion) {
			res = getType();
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		final TextRegion res;

		final ITextLocation textLocation = (ITextLocation)location;
		final int startOffset = textLocation.getStartOffset();
		final int endOffset = textLocation.getEndOffset();
		final String text = ((ITextContainer)location.getContainer()).getText().substring(startOffset,
				endOffset);
		res = new TextRegion(text, startOffset, endOffset);

		return res;
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object element) {
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

	@Override
	protected boolean canUpdate(Object element) {
		return element instanceof TextRegion;
	}

	/**
	 * Updates the given {@link ITextContainer} with the given {@link ITextContainer#getText() text}.
	 * 
	 * @param container
	 *            the {@link ITextContainer}
	 * @param text
	 *            the {@link ITextContainer#getText() text}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void updateTextContainer(ITextContainer container, String text)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final String oldText = container.getText();
		container.setText(text);
		if (oldText != null) {
			final DiffMatch diff = MappingUtils.getDiffMatch(oldText, text);
			for (ILocation child : container.getContents()) {
				if (child instanceof ITextLocation && !child.isMarkedAsDeleted()) {
					final ITextLocation location = (ITextLocation)child;
					final int newStartOffset = diff.getIndex(location.getStartOffset());
					final int newEndOffset = diff.getIndex(location.getEndOffset());
					final String oldValue = oldText.substring(location.getStartOffset(), location
							.getEndOffset());
					final String newValue = text.substring(newStartOffset, newEndOffset);
					if (newStartOffset == newEndOffset) {
						MappingUtils.markAsDeletedOrDelete(location, String.format(
								"\"%s\" at (%d, %d) has been deleted.", oldValue, location.getStartOffset(),
								location.getEndOffset()));
					} else if (!oldValue.equals(newValue)) {
						MappingUtils.markAsChanged(location, String.format(
								"\"%s\" at (%d, %d) has been changed to \"%s\" at (%d, %d).", oldValue,
								location.getStartOffset(), location.getEndOffset(), newValue, newStartOffset,
								newEndOffset));
					}
					location.setStartOffset(newStartOffset);
					location.setEndOffset(newEndOffset);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		final String text = ((ITextContainer)location.getContainer()).getText();
		final int start = ((ITextLocation)location).getStartOffset();
		final int end = ((ITextLocation)location).getEndOffset();
		res = String.format("\"%s\"", text.substring(start, end));

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationDescriptor(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor,
	 *      java.lang.Object)
	 */
	public ILocationDescriptor getLocationDescriptor(ILocationDescriptor containerDescriptor, Object element) {
		final ILocationDescriptor res;

		if (element instanceof TextRegion) {
			res = new ObjectLocationDescriptor(this, containerDescriptor, element, ((TextRegion)element)
					.getText());
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getType()
	 */
	public Class<? extends ILocation> getType() {
		return ITextLocation.class;
	}

}
