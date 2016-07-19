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
package org.eclipse.mylyn.docs.intent.mapping.tests.connector;

import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.internal.connector.ConnectorRegistry;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.LocationFactoryTests.TestLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ConnectorRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ConnectorRegistryTests {

	/**
	 * A test base.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class ConnectorRegistryTestBase extends TestBase {

		/**
		 * The {@link BaseElementFactory}.
		 */
		private final BaseElementFactory factory;

		public ConnectorRegistryTestBase() {
			super();
			factory = new BaseElementFactory();
		}

		@Override
		public BaseElementFactory getFactory() {
			return factory;
		}

	}

	/**
	 * A test {@link IConnector} implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestConnector1 extends AbstractConnector {

		@Override
		protected Class<? extends ILocation> getLocationType(
				Class<? extends ILocationContainer> containerType, Object element) {
			final Class<? extends ILocation> res;

			if (containerType == TestLocation1.class) {
				res = ITestLocation1.class;
			} else {
				res = null;
			}

			return res;
		}

		@Override
		protected void initLocation(ILocation location, Object element) {
			((ITestLocation1)location).setObject(element);
		}

		@Override
		protected boolean match(ILocation location, Object element) {
			return location.getContainer() instanceof TestLocation1
					&& ((ITestLocation1)location).getObject() == element;
		}

		public String getName(ILocation location) {
			final String res;

			if (location.getContainer() instanceof TestLocation1) {
				res = "TestLocation1 " + ((TestLocation1)location).getObject().toString();
			} else {
				res = null;
			}

			return res;
		}
	}

	/**
	 * A test location interface.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface ITestLocation1 extends ILocation {

		void setObject(Object o);

		Object getObject();

	}

	/**
	 * A test location.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestLocation1 extends TestLocation implements ITestLocation1 {

		/**
		 * The {@link Object}.
		 */
		private Object object;

		public void setObject(Object o) {
			object = o;
		}

		public Object getObject() {
			return object;
		}

	}

	/**
	 * A test {@link IConnector} implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestConnector2 extends AbstractConnector {

		@Override
		protected Class<? extends ILocation> getLocationType(
				Class<? extends ILocationContainer> containerType, Object element) {
			final Class<? extends ILocation> res;

			if (containerType == TestLocation2.class) {
				res = ITestLocation2.class;
			} else {
				res = null;
			}

			return res;
		}

		@Override
		protected void initLocation(ILocation location, Object element) {
			((ITestLocation2)location).setObject(element);
		}

		@Override
		protected boolean match(ILocation location, Object element) {
			return location.getContainer() instanceof TestLocation2
					&& ((ITestLocation2)location).getObject() == element;
		}

		public String getName(ILocation location) {
			final String res;

			if (location.getContainer() instanceof TestLocation2) {
				res = "TestLocation2 " + ((TestLocation2)location).getObject().toString();
			} else {
				res = null;
			}

			return res;
		}

	}

	/**
	 * A test location interface.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface ITestLocation2 extends ILocation {

		void setObject(Object o);

		Object getObject();

	}

	/**
	 * A test location.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestLocation2 extends TestLocation implements ITestLocation2 {

		/**
		 * The {@link Object}.
		 */
		private Object object;

		public void setObject(Object o) {
			object = o;
		}

		public Object getObject() {
			return object;
		}

	}

	/**
	 * The {@link ConnectorRegistry} to test.
	 */
	private ConnectorRegistry connectorRegistery;

	@Before
	public void before() {
		connectorRegistery = new ConnectorRegistry();
	}

	@Test
	public void register() {
		final IConnector connector = new TestConnector1();
		connectorRegistery.register(connector);

		assertEquals(1, connectorRegistery.getConnectors().size());
	}

	@Test
	public void unregister() {
		final IConnector connector = new TestConnector1();
		connectorRegistery.register(connector);

		assertEquals(1, connectorRegistery.getConnectors().size());

		connectorRegistery.unregister(connector);

		assertEquals(0, connectorRegistery.getConnectors().size());
	}

	@Test
	public void getConnectors() {
		final IConnector connector0 = new TestConnector1();
		final IConnector connector1 = new TestConnector1();
		final IConnector connector2 = new TestConnector1();
		connectorRegistery.register(connector0);
		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final Set<IConnector> bases = connectorRegistery.getConnectors();
		assertEquals(3, bases.size());

		assertTrue(bases.contains(connector0));
		assertTrue(bases.contains(connector1));
		assertTrue(bases.contains(connector2));
	}

	@Test
	public void createLocationNoConnectors() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation1();
		container.setContainer(base);
		final Object element = new Object();

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertEquals(null, location);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void createLocationNotRegisteredInBase() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation1();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertTrue(location instanceof ITestLocation1);
		assertEquals(element, ((ITestLocation1)location).getObject());
	}

	@Test
	public void createLocationFirstConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation1();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertTrue(location instanceof ITestLocation1);
		assertEquals(element, ((ITestLocation1)location).getObject());
	}

	@Test
	public void createLocationSecondConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation2();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertTrue(location instanceof ITestLocation2);
		assertEquals(element, ((ITestLocation2)location).getObject());
	}

	@Test
	public void getLocationNoConnectors() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation container = new TestLocation1();
		final Object element = new Object();

		final ILocation location = connectorRegistery.getLocation(container, element);

		assertEquals(null, location);
	}

	@Test
	public void getLocationFirstConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation1();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);
		final ILocation found = connectorRegistery.getLocation(container, element);

		assertTrue(location instanceof ITestLocation1);
		assertEquals(element, ((ITestLocation1)location).getObject());
		assertEquals(location, found);
	}

	@Test
	public void getLocationSecondConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation2();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);
		final ILocation found = connectorRegistery.getLocation(container, element);

		assertTrue(location instanceof ITestLocation2);
		assertEquals(element, ((ITestLocation2)location).getObject());
		assertEquals(location, found);
	}

	@Test
	public void getNameFirstConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation1();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertTrue(connectorRegistery.getName(location).startsWith("TestLocation1 "));
	}

	@Test
	public void getNameSecondConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new ConnectorRegistryTestBase();
		final ILocation container = new TestLocation2();
		container.setContainer(base);
		final TestConnector1 connector1 = new TestConnector1();
		final TestConnector2 connector2 = new TestConnector2();
		final Object element = new Object();

		base.getFactory().addDescriptor(ITestLocation1.class,
				new FactoryDescriptor<TestLocation1>(TestLocation1.class));
		base.getFactory().addDescriptor(ITestLocation2.class,
				new FactoryDescriptor<TestLocation2>(TestLocation2.class));

		connectorRegistery.register(connector1);
		connectorRegistery.register(connector2);

		final ILocation location = connectorRegistery.createLocation(container, element);

		assertTrue(connectorRegistery.getName(location).startsWith("TestLocation2 "));
	}

}
