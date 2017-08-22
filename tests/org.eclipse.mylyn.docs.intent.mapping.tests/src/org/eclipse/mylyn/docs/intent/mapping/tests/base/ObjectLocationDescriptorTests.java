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

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.ITestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.connector.ConnectorRegistryTests.TestLocation1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ObjectLocationDescriptor}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ObjectLocationDescriptorTests extends AbstractConnector {

	/**
	 * The element.
	 */
	private Object element;

	/**
	 * Counts the number of calls to {@link ObjectLocationDescriptorTests#dispose(ILocationDescriptor)}.
	 */
	private int disposeCount;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element
	 */
	public ObjectLocationDescriptorTests() {
		this.element = new Object();
	}

	public ILocationDescriptor getLocationDescriptor(IBase base, Object elem) {
		// nothing to do here
		return null;
	}

	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object elem) {
		return ITestLocation.class;
	}

	public Object getElement(ILocation location) {
		return element;
	}

	public String getName(ILocation location) {
		// nothing to do here
		return null;
	}

	public Class<? extends ILocation> getType() {
		return ITestLocation.class;
	}

	@Override
	protected boolean canUpdate(Object elem) {
		return true;
	}

	@Override
	protected boolean match(ILocation location, Object elem) {
		return elem == element;
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object elem) {
		((ITestLocation)location).setObject(elem);
	}

	@Override
	public void dispose(ILocationDescriptor locationDescriptor) {
		super.dispose(locationDescriptor);
		disposeCount++;
	}

	@Test
	public void dispose() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		descriptor.dispose();

		assertEquals(1, disposeCount);
	}

	@Test
	public void equalsNull() {
		final Object element1 = new Object();
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor1 = new ObjectLocationDescriptor(base, element1,
				"test descriptor1");

		// CHECKSTYLE:OFF
		assertFalse(descriptor1.equals(null));
		// CHECKSTYLE:ON
	}

	@Test
	public void equalsFalse() {
		final Object element1 = new Object();
		final Object element2 = new Object();
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor1 = new ObjectLocationDescriptor(base, element1,
				"test descriptor1");
		final ObjectLocationDescriptor descriptor2 = new ObjectLocationDescriptor(base, element2,
				"test descriptor2");

		assertFalse(descriptor1.equals(descriptor2));
		assertFalse(descriptor2.equals(descriptor1));
	}

	@Test
	public void equalsTrue() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor1 = new ObjectLocationDescriptor(base, element,
				"test descriptor1");
		final ObjectLocationDescriptor descriptor2 = new ObjectLocationDescriptor(base, element,
				"test descriptor2");

		assertTrue(descriptor1.equals(descriptor2));
		assertTrue(descriptor2.equals(descriptor1));
	}

	@Test
	public void getLocationNotExisting() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		final ILocation found = descriptor.getLocation();

		assertNull(found);
	}

	@Test
	public void getLocationExisting() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");
		final TestLocation1 location = new TestLocation1();
		location.setObject(element);
		base.getContents().add(location);

		final ILocation found = descriptor.getLocation();

		assertEquals(location, found);
	}

	@Test
	public void getName() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		assertEquals("test descriptor", descriptor.getName());
	}

	@Test
	public void getOrCreateNotExcisting() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");
		base.getFactory().addDescriptor(ITestLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestLocation1>(TestLocation1.class));

		final ILocation location = descriptor.getOrCreate();

		assertTrue(location instanceof ITestLocation);
		assertEquals(element, ((ITestLocation)location).getObject());
	}

	@Test
	public void getOrCreateExcisting() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");
		final TestLocation1 location = new TestLocation1();
		location.setObject(element);
		base.getContents().add(location);

		final ILocation res = descriptor.getOrCreate();

		assertTrue(location == res);
	}

	@Test
	public void hashCodeTest() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");
		final TestLocation1 location = new TestLocation1();
		location.setObject(element);
		base.getContents().add(location);

		assertEquals(element.hashCode(), descriptor.hashCode());
	}

	@Test
	public void setElementNull() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		descriptor.setElement(null);

		assertNull(descriptor.getElement());
	}

	@Test
	public void setElement() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		final Object otherElement = new Object();
		descriptor.setElement(otherElement);

		assertEquals(otherElement, descriptor.getElement());
	}

	@Test
	public void updateNull() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		descriptor.update(null);

		assertNull(descriptor.getElement());
	}

	@Test
	public void updateElement() {
		final IBase base = new TestBase();
		final ObjectLocationDescriptor descriptor = new ObjectLocationDescriptor(base, element,
				"test descriptor");

		final Object otherElement = new Object();
		descriptor.update(otherElement);

		assertEquals(otherElement, descriptor.getElement());
	}

}
