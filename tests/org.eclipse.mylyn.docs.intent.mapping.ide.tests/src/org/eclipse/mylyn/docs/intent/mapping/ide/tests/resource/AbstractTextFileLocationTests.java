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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.resource;

import org.eclipse.intent.mapping.ide.resource.ITextFileLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ITextFileLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractTextFileLocationTests extends AbstractResourceLocationTests {

	protected ITextFileLocation createLocation() throws InstantiationException, IllegalAccessException {
		return getBase().getFactory().createElement(ITextFileLocation.class);
	}

	@Test
	public void setTextNull() throws InstantiationException, IllegalAccessException {
		ITextFileLocation location = createLocation();

		location.setText(null);

		assertEquals(null, location.getText());
	}

	@Test
	public void setText() throws InstantiationException, IllegalAccessException {
		ITextFileLocation location = createLocation();

		location.setText("test text");

		assertEquals("test text", location.getText());
	}

	@Test
	public void getTextDefault() throws InstantiationException, IllegalAccessException {
		ITextFileLocation location = createLocation();

		assertEquals(null, location.getText());
	}

}
