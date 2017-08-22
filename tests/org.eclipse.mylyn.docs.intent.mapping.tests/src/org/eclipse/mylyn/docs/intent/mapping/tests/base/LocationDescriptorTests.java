/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.LocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.connector.ConnectorRegistryTests.TestLocation1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link LocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LocationDescriptorTests {

	@Test
	public void equalsNull() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor1 = new LocationDescriptor(location);

		// CHECKSTYLE:OFF
		assertFalse(descriptor1.equals(null));
		// CHECKSTYLE:ON
	}

	@Test
	public void equalsFalse() {
		final TestLocation1 location1 = new TestLocation1();
		final TestLocation1 location2 = new TestLocation1();
		final LocationDescriptor descriptor1 = new LocationDescriptor(location1);
		final LocationDescriptor descriptor2 = new LocationDescriptor(location2);

		assertFalse(descriptor1.equals(descriptor2));
		assertFalse(descriptor2.equals(descriptor1));
	}

	@Test
	public void equalsTrue() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor1 = new LocationDescriptor(location);
		final LocationDescriptor descriptor2 = new LocationDescriptor(location);

		assertTrue(descriptor1.equals(descriptor2));
		assertTrue(descriptor2.equals(descriptor1));
	}

	@Test
	public void getLocationNull() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);

		assertTrue(location == descriptor.getLocation());
	}

	@Test
	public void getLocationExisting() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);
		final IBase base = new TestBase();
		base.getContents().add(location);

		final ILocation found = descriptor.getLocation();

		assertTrue(location == found);
	}

	@Test
	public void getName() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);

		assertEquals(null, descriptor.getName());
	}

	@Test
	public void getOrCreateNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);

		assertTrue(location == descriptor.getOrCreate());
	}

	@Test
	public void getOrCreateExcisting() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);
		final IBase base = new TestBase();
		base.getContents().add(location);

		final ILocation res = descriptor.getOrCreate();

		assertTrue(location == res);
	}

	@Test
	public void hashCodeTest() {
		final TestLocation1 location = new TestLocation1();
		final LocationDescriptor descriptor = new LocationDescriptor(location);
		final IBase base = new TestBase();
		base.getContents().add(location);

		assertEquals(location.hashCode(), descriptor.hashCode());
	}

}
