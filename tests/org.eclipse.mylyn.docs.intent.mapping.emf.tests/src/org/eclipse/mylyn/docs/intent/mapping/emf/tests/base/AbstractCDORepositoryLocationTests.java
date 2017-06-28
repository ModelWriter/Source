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

import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test {@link ICDORepositoryLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractCDORepositoryLocationTests extends AbstractLocationTests {

	@Override
	protected ICDORepositoryLocation createLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return getBase().getFactory().createElement(ICDORepositoryLocation.class);
	}

	@Test
	public void setURL() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		location.setURL("someURL");

		assertEquals("someURL", location.getURL());
	}

	@Test
	public void getURLDefault() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		assertEquals(null, location.getURL());
	}

	@Test
	public void setUUID() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		location.setUUID("someUUID");

		assertEquals("someUUID", location.getUUID());
	}

	@Test
	public void getUUIDDefault() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		assertEquals(null, location.getUUID());
	}

	@Test
	public void setName() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		location.setName("someName");

		assertEquals("someName", location.getName());
	}

	@Test
	public void getNameDefault() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		assertEquals(null, location.getName());
	}

	@Test
	public void setBranchID() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		location.setBranchID(42);

		assertEquals(42, location.getBranchID());
	}

	@Test
	public void getBranchIDDefault() throws Exception {
		final ICDORepositoryLocation location = createLocation();

		assertEquals(-1, location.getBranchID());
	}

}
