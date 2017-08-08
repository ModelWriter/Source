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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ContainerProviderFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link IBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractBaseTests extends AbstractMappingTests {

	/**
	 * A test {@link IContainerProvider}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestContainerProvider1 implements IContainerProvider {

		/**
		 * The container.
		 */
		public static final Object CONTAINER = new Object();

		/**
		 * The child.
		 */
		public static final Object CHILD = new Object();

		public Object getContainer(Object element) {
			final Object res;

			if (element == CHILD) {
				res = CONTAINER;
			} else {
				res = null;
			}

			return res;
		}

		public Class<? extends ILocation> getType() {
			return TestLocation.class;
		}

	}

	/**
	 * A test {@link IContainerProvider}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestContainerProvider2 implements IContainerProvider {

		/**
		 * The container.
		 */
		public static final Object CONTAINER = new Object();

		/**
		 * The child.
		 */
		public static final Object CHILD = new Object();

		public Object getContainer(Object element) {
			final Object res;

			if (element == CHILD) {
				res = CONTAINER;
			} else {
				res = null;
			}

			return res;
		}

		public Class<? extends ILocation> getType() {
			return TestLocation.class;
		}

	}

	@Test
	public void setNameNull() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		base.setName(null);

		assertNull(base.getName());

		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void setName() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		base.setName("base");

		assertEquals("base", base.getName());

		assertTestBaseListener(listener, 1, 0, 0, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void getNameDefault() {
		final IBase base = getBase();

		assertNull(base.getName());
	}

	@Test
	public void addRootLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location = base.getFactory().createElement(ITextLocation.class);

		base.getContents().add(location);

		assertEquals(1, base.getContents().size());
		assertEquals(location, base.getContents().get(0));

		assertTestBaseListener(listener, 0, 1, 0, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyRootLocations() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location1 = base.getFactory().createElement(ITextLocation.class);
		final ILocation location2 = base.getFactory().createElement(ITextLocation.class);
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		base.getContents().addAll(locations);

		assertEquals(2, base.getContents().size());
		assertEquals(location1, base.getContents().get(0));
		assertEquals(location2, base.getContents().get(1));

		assertTestBaseListener(listener, 0, 2, 0, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeRootLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location = base.getFactory().createElement(ITextLocation.class);

		base.getContents().add(location);

		assertEquals(1, base.getContents().size());
		assertEquals(location, base.getContents().get(0));

		base.getContents().remove(location);

		assertEquals(0, base.getContents().size());

		assertTestBaseListener(listener, 0, 1, 1, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyRootLocations() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location1 = base.getFactory().createElement(ITextLocation.class);
		final ILocation location2 = base.getFactory().createElement(ITextLocation.class);
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		base.getContents().addAll(locations);

		assertEquals(2, base.getContents().size());
		assertEquals(location1, base.getContents().get(0));
		assertEquals(location2, base.getContents().get(1));

		base.getContents().removeAll(locations);

		assertEquals(0, base.getContents().size());

		assertTestBaseListener(listener, 0, 2, 2, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report = base.getFactory().createElement(IReport.class);

		base.getReports().add(report);

		assertEquals(1, base.getReports().size());
		assertEquals(report, base.getReports().get(0));

		assertTestBaseListener(listener, 0, 0, 0, 1, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report1 = base.getFactory().createElement(IReport.class);
		final IReport report2 = base.getFactory().createElement(IReport.class);
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		base.getReports().addAll(reports);

		assertEquals(2, base.getReports().size());
		assertEquals(report1, base.getReports().get(0));
		assertEquals(report2, base.getReports().get(1));

		assertTestBaseListener(listener, 0, 0, 0, 2, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report = base.getFactory().createElement(IReport.class);

		base.getReports().add(report);

		assertEquals(1, base.getReports().size());
		assertEquals(report, base.getReports().get(0));

		base.getReports().remove(report);

		assertEquals(0, base.getReports().size());

		assertTestBaseListener(listener, 0, 0, 0, 1, 1, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report1 = base.getFactory().createElement(IReport.class);
		final IReport report2 = base.getFactory().createElement(IReport.class);
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		base.getReports().addAll(reports);

		assertEquals(2, base.getReports().size());
		assertEquals(report1, base.getReports().get(0));
		assertEquals(report2, base.getReports().get(1));

		base.getReports().removeAll(reports);

		assertEquals(0, base.getReports().size());

		assertTestBaseListener(listener, 0, 0, 0, 2, 2, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addContainerProvider() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider1.class));

		final String containerProvider = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider1";

		base.getContainerProviders().add(containerProvider);

		assertEquals(1, base.getContainerProviders().size());
		assertEquals(containerProvider, base.getContainerProviders().get(0));
		assertEquals(TestContainerProvider1.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider1.CHILD));

		assertTestBaseListener(listener, 0, 0, 0, 0, 0, 1, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);

		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider1.class
				.getCanonicalName());
	}

	@Test
	public void addManyContainerProviders() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider1.class));
		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider2.class));

		final String containerProvider1 = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider1";
		final String containerProvider2 = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider2";
		final List<String> containerProviders = new ArrayList<String>();
		containerProviders.add(containerProvider1);
		containerProviders.add(containerProvider2);

		base.getContainerProviders().addAll(containerProviders);

		assertEquals(2, base.getContainerProviders().size());
		assertEquals(containerProvider1, base.getContainerProviders().get(0));
		assertEquals(containerProvider2, base.getContainerProviders().get(1));
		assertEquals(TestContainerProvider1.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider1.CHILD));
		assertEquals(TestContainerProvider2.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider2.CHILD));

		assertTestBaseListener(listener, 0, 0, 0, 0, 0, 2, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);

		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider1.class
				.getCanonicalName());
		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider2.class
				.getCanonicalName());
	}

	@Test
	public void removeContainerProvider() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider1.class));

		final String containerProvider = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider1";

		base.getContainerProviders().add(containerProvider);

		assertEquals(1, base.getContainerProviders().size());
		assertEquals(containerProvider, base.getContainerProviders().get(0));
		assertEquals(TestContainerProvider1.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider1.CHILD));

		base.getContainerProviders().remove(containerProvider);

		assertEquals(0, base.getContainerProviders().size());

		assertEquals(null, base.getContainerProviderRegistry().getContainer(TestContainerProvider1.CHILD));

		assertTestBaseListener(listener, 0, 0, 0, 0, 0, 1, 1);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);

		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider1.class
				.getCanonicalName());
	}

	@Test
	public void removeManyContainerProviders() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider1.class));
		MappingUtils.getContainerProviderFactory().addDescriptor(
				new ContainerProviderFactory.FactoryDescriptor(TestContainerProvider2.class));

		final String containerProvider1 = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider1";
		final String containerProvider2 = "org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractBaseTests.TestContainerProvider2";
		final List<String> containerProviders = new ArrayList<String>();
		containerProviders.add(containerProvider1);
		containerProviders.add(containerProvider2);

		base.getContainerProviders().addAll(containerProviders);

		assertEquals(2, base.getContainerProviders().size());
		assertEquals(containerProvider1, base.getContainerProviders().get(0));
		assertEquals(containerProvider2, base.getContainerProviders().get(1));
		assertEquals(TestContainerProvider1.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider1.CHILD));
		assertEquals(TestContainerProvider2.CONTAINER, base.getContainerProviderRegistry().getContainer(
				TestContainerProvider2.CHILD));

		base.getContainerProviders().removeAll(containerProviders);

		assertEquals(0, base.getContainerProviders().size());

		assertEquals(null, base.getContainerProviderRegistry().getContainer(TestContainerProvider1.CHILD));
		assertEquals(null, base.getContainerProviderRegistry().getContainer(TestContainerProvider2.CHILD));

		assertTestBaseListener(listener, 0, 0, 0, 0, 0, 2, 2);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0, 0, 0);

		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider1.class
				.getCanonicalName());
		MappingUtils.getContainerProviderFactory().removeDescriptor(TestContainerProvider2.class
				.getCanonicalName());
	}

	@Test
	public void testCreateCouple() throws Exception {
		final IBase base = getBase();
		final ICouple element = base.getFactory().createElement(ICouple.class);

		assertTrue(element instanceof ICouple);
	}

	@Test
	public void testCreateLink() throws Exception {
		final IBase base = getBase();
		final ILink element = base.getFactory().createElement(ILink.class);

		assertTrue(element instanceof ILink);
	}

	@Test
	public void testCreateReport() throws Exception {
		final IBase base = getBase();
		final IReport element = base.getFactory().createElement(IReport.class);

		assertTrue(element instanceof IReport);
	}

	@Test
	public void testCreateTextLocation() throws Exception {
		final IBase base = getBase();
		final ITextLocation element = base.getFactory().createElement(ITextLocation.class);

		assertTrue(element instanceof ITextLocation);
	}

}
