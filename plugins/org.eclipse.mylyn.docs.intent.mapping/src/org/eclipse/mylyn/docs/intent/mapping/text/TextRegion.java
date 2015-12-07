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
	 * Constructor.
	 * 
	 * @param startOffset
	 *            the start offset
	 * @param endOffset
	 *            the end offset
	 */
	public TextRegion(int startOffset, int endOffset) {
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

}
