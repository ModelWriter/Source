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

/**
 * A text region.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextRegion {

	/**
	 * The start offset.
	 */
	private final int startOffset;

	/**
	 * The end offset.
	 */
	private final int endOffset;

	/**
	 * The text.
	 */
	private final String text;

	/**
	 * The containing {@link Object} should
	 * {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer, Object)
	 * be located} by a {@link ITextContainer}.
	 */
	private final Object container;

	/**
	 * Constructor.
	 * 
	 * @param container
	 *            the containing {@link Object} should
	 *            {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer, Object)
	 *            be located} by a {@link ITextContainer}
	 * @param text
	 *            the text
	 * @param startOffset
	 *            the start offset
	 * @param endOffset
	 *            the end offset
	 */
	public TextRegion(Object container, String text, int startOffset, int endOffset) {
		this.container = container;
		this.text = text;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	/**
	 * Gets the start offset.
	 * 
	 * @return the start offset
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Gets the end offset.
	 * 
	 * @return the end offset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * Get the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the containing {@link Object} should
	 * {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer, Object)
	 * be located} by a {@link ITextContainer}.
	 * 
	 * @return the containing {@link Object} should
	 *         {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer, Object)
	 *         be located} by a {@link ITextContainer}
	 */
	public Object getContainer() {
		return container;
	}

	@Override
	public int hashCode() {
		return (text.hashCode() ^ endOffset) - startOffset ^ container.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TextRegion && startOffset == ((TextRegion)obj).startOffset
				&& endOffset == ((TextRegion)obj).endOffset && text.equals(((TextRegion)obj).text)
				&& container.equals(((TextRegion)obj).container);
	}

}
