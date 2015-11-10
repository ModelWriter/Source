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

import org.eclipse.intent.mapping.ide.resource.IResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IResourceLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractResourceLocationTests extends AbstractLocationTests {

	protected IResourceLocation createLocation() throws InstantiationException, IllegalAccessException {
		return getBase().getFactory().createElement(IResourceLocation.class);
	}

	@Test
	public void setFullPathNull() throws InstantiationException, IllegalAccessException {
		IResourceLocation location = createLocation();

		location.setFullPath(null);

		assertEquals(null, location.getFullPath());
	}

	@Test
	public void setFullPath() throws InstantiationException, IllegalAccessException {
		IResourceLocation location = createLocation();

		location.setFullPath("test text");

		assertEquals("test text", location.getFullPath());
	}

	@Test
	public void getFullPathDefault() throws InstantiationException, IllegalAccessException {
		IResourceLocation location = createLocation();

		assertEquals(null, location.getFullPath());
	}

}