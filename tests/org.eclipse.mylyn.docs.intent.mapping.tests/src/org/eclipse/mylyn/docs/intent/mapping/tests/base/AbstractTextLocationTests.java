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
package org.eclipse.mylyn.docs.intent.mapping.tests.base;

import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test {@link ITextLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractTextLocationTests extends AbstractLocationTests {

	@Override
	protected ITextLocation createLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return getBase().getFactory().createElement(ITextLocation.class);
	}

	@Test
	public void setStartOffset() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ITextLocation location = createLocation();

		location.setStartOffset(50);

		assertEquals(50, location.getStartOffset());
	}

	@Test
	public void getStartOffsetDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ITextLocation location = createLocation();

		assertEquals(-1, location.getStartOffset());
	}

	@Test
	public void setEndOffset() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ITextLocation location = createLocation();

		location.setEndOffset(50);

		assertEquals(50, location.getEndOffset());
	}

	@Test
	public void getEndOffsetDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ITextLocation location = createLocation();

		assertEquals(-1, location.getEndOffset());
	}

}
