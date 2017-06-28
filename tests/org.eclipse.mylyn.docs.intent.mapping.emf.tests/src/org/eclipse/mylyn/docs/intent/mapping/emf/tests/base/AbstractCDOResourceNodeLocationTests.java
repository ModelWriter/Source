/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf.tests.base;

import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test {@link ICDOResourceNodeLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractCDOResourceNodeLocationTests extends AbstractLocationTests {

	@Override
	protected ICDOResourceNodeLocation createLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return getBase().getFactory().createElement(ICDOResourceNodeLocation.class);
	}

	@Test
	public void setPath() throws Exception {
		final ICDOResourceNodeLocation location = createLocation();

		location.setPath("somePath");

		assertEquals("somePath", location.getPath());
	}

	@Test
	public void getPathDefault() throws Exception {
		final ICDOResourceNodeLocation location = createLocation();

		assertEquals(null, location.getPath());
	}

}
