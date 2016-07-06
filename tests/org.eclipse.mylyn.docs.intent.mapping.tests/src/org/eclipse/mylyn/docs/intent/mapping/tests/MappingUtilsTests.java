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
package org.eclipse.mylyn.docs.intent.mapping.tests;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.LocationFactoryTests.TestLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link MappingUtils}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingUtilsTests {

	@Test(expected = NullPointerException.class)
	public void getBaseNull() {
		MappingUtils.getBase(null);
	}

	@Test
	public void getBaseNoBase() {
		final TestLocation location = new TestLocation();
		final IBase base = MappingUtils.getBase(location);

		assertNull(base);
	}

	@Test
	public void getBase() {
		final TestLocation location = new TestLocation();
		final TestBase container = new TestBase();
		location.setContainer(container);

		final IBase base = MappingUtils.getBase(location);

		assertEquals(container, base);
	}

	@Test
	public void getConnectorRegistry() {
		assertNotNull(MappingUtils.getConnectorRegistry());
	}

	@Test
	public void getMappingRegistry() {
		assertNotNull(MappingUtils.getMappingRegistry());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchNullNull() {
		MappingUtils.getDiffMatch(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchStringNull() {
		MappingUtils.getDiffMatch("", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchNullString() {
		MappingUtils.getDiffMatch(null, "");
	}

	@Test
	public void getDiffMatch() {
		assertNotNull(MappingUtils.getDiffMatch("", ""));
	}

	@Test
	public void registerLocationImplementationBeforeBase() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());
	}

	@Test
	public void registerLocationImplementationAfterBase() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());
	}

	@Test
	public void unregisterLocationImplementation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());

		MappingUtils.unregisterLocationImplementation(TestBase.class, ILocation.class);

		assertNull(base.getFactory().createElement(ILocation.class));
	}

}