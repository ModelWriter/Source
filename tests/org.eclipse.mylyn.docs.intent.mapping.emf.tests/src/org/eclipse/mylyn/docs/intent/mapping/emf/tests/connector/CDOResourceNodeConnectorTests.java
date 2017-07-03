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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector;

import org.eclipse.emf.cdo.eresource.CDOBinaryResource;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOTextResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOBinaryResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOFolderLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOTextResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOResourceNodeConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOViewConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.AllTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOServer;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOUtils;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOViewConnectorTests.TestCDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests;
import org.eclipse.net4j.connector.IConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link CDOResourceNodeConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOResourceNodeConnectorTests {

	/**
	 * Test {@link ICDOResourceNodeLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public abstract static class AbstractTestCDOResourceNodeLocation extends TestLocation implements ICDOResourceNodeLocation {

		/**
		 * The path.
		 */
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

	}

	/**
	 * A test {@link ICDOFolderLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestCDOFolderLocation extends AbstractTestCDOResourceNodeLocation implements ICDOFolderLocation {

	}

	/**
	 * A test {@link ICDOBinaryResourceLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestCDOBinaryResourceLocation extends AbstractTestCDOResourceNodeLocation implements ICDOBinaryResourceLocation {

	}

	/**
	 * A test {@link ICDOTextResourceLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestCDOTextResourceLocation extends AbstractTestCDOResourceNodeLocation implements ICDOTextResourceLocation {

		/**
		 * The containing text.
		 */
		private String text;

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

	}

	/**
	 * A test {@link ICDOResourceLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestCDOResourceLocation extends TestEObjectContainerLocation implements ICDOResourceLocation {

		/**
		 * The path.
		 */
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

	}

	/**
	 * Test {@link CDOViewConnector}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class TestCDOResourceNodeConnector extends CDOResourceNodeConnector {
		@Override
		public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
				Object element) {
			return super.getLocationType(containerType, element);
		}

		@Override
		public void initLocation(ILocationContainer container, ILocation location, Object element) {
			super.initLocation(container, location, element);
		}

		@Override
		public boolean match(ILocation location, Object element) {
			return super.match(location, element);
		}

	}

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction transaction;

	/**
	 * A {@link CDOResourceFolder}.
	 */
	private static CDOResourceFolder folder;

	/**
	 * A {@link CDOTextResource}.
	 */
	private static CDOTextResource textResource;

	/**
	 * A {@link CDOBinaryResource}.
	 */
	private static CDOBinaryResource binaryResource;

	/**
	 * A {@link CDOResource}.
	 */
	private static CDOResource resource;

	/**
	 * The {@link CDOViewConnector}.
	 */
	private static CDOViewConnector viewConnector = new CDOViewConnector();

	/**
	 * The connector to test.
	 */
	private TestCDOResourceNodeConnector connector = new TestCDOResourceNodeConnector();

	@BeforeClass
	public static void beforeClass() throws Exception {
		AllTests.startCDOServer();
		IConnector connector = CDOUtils.getConnector(CDOServer.PROTOCOL + "://" + CDOServer.IP + ":"
				+ CDOServer.PORT);
		CDOSession session = CDOUtils.openSession(connector, CDOServer.REPOSITORY_NAME);
		transaction = CDOUtils.openTransaction(session);
		viewConnector.addSessionToCache(transaction);

		folder = transaction.createResourceFolder(CDOResourceNodeConnectorTests.class.getCanonicalName()
				+ "/test/");
		textResource = transaction.createTextResource(CDOResourceNodeConnectorTests.class.getCanonicalName()
				+ "/test/test.txt");
		binaryResource = transaction.createBinaryResource(CDOResourceNodeConnectorTests.class
				.getCanonicalName() + "/test/test.bin");
		resource = transaction.createResource(CDOResourceNodeConnectorTests.class.getCanonicalName()
				+ "/test/test.resource");
		transaction.commit();

		viewConnector.addSessionToCache(transaction);
		MappingUtils.getConnectorRegistry().register(viewConnector);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		viewConnector.removeSessionFromCache(transaction);

		resource.delete(null);
		binaryResource.delete(null);
		textResource.delete(null);
		folder.delete(null);
		transaction.commit();

		MappingUtils.getConnectorRegistry().unregister(viewConnector);
		transaction.close();
		AllTests.stopCDOServer();
	}

	@Test
	public void getLocationTypeFolder() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(folder.getPath());

		assertEquals(ICDOFolderLocation.class, connector.getLocationType(ICDORepositoryLocation.class,
				folder));
	}

	@Test
	public void getLocationTypeBinaryFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(binaryResource.getPath());

		assertEquals(ICDOBinaryResourceLocation.class, connector.getLocationType(ICDORepositoryLocation.class,
				binaryResource));
	}

	@Test
	public void getLocationTypeTextFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(textResource.getPath());

		assertEquals(ICDOTextResourceLocation.class, connector.getLocationType(ICDORepositoryLocation.class,
				textResource));
	}

	@Test
	public void getLocationTypeResource() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(resource.getPath());

		assertEquals(ICDOResourceLocation.class, connector.getLocationType(ICDORepositoryLocation.class,
				resource));
	}

	@Test
	public void getElementFolder() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(folder.getPath());

		final Object element = connector.getElement(location);

		assertTrue(element instanceof CDOResourceFolder);
		assertEquals(folder.getPath(), ((CDOResourceFolder)element).getPath());
	}

	@Test
	public void getElementBinaryFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(binaryResource.getPath());

		final Object element = connector.getElement(location);

		assertTrue(element instanceof CDOBinaryResource);
		assertEquals(binaryResource.getPath(), ((CDOBinaryResource)element).getPath());
	}

	@Test
	public void getElementTextFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(textResource.getPath());

		final Object element = connector.getElement(location);

		assertTrue(element instanceof CDOTextResource);
		assertEquals(textResource.getPath(), ((CDOTextResource)element).getPath());
	}

	@Test
	public void getElementResource() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(resource.getPath());

		final Object element = connector.getElement(location);

		assertTrue(element instanceof CDOResource);
		assertEquals(resource.getPath(), ((CDOResource)element).getPath());
	}

	@Test
	public void getNameFolder() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(folder.getPath());

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test",
				connector.getName(location));
	}

	@Test
	public void getNameBinaryFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(binaryResource.getPath());

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.bin",
				connector.getName(location));
	}

	@Test
	public void getNameTextFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(textResource.getPath());

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.txt",
				connector.getName(location));
	}

	@Test
	public void getNameResource() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		location.setContainer(container);
		location.setPath(resource.getPath());

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.resource",
				connector.getName(location));
	}

	@Test
	public void initLocationFolder() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, folder);
		location.setContainer(container);

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test",
				location.getPath());
	}

	@Test
	public void initLocationBinaryFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, binaryResource);
		location.setContainer(container);

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.bin",
				location.getPath());
	}

	@Test
	public void initLocationTextFile() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, textResource);

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.txt",
				location.getPath());
	}

	@Test
	public void initLocationResource() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, resource);
		location.setContainer(container);

		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests/test/test.resource",
				location.getPath());
	}

	@Test
	public void matchFolder() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, folder);
		location.setContainer(container);

		assertTrue(connector.match(location, folder));
	}

	@Test
	public void matchFolderDifferenteBranch() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setBranchID(container.getBranchID() + 42);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, folder);
		location.setContainer(container);

		assertFalse(connector.match(location, folder));
	}

	@Test
	public void matchFolderDifferenteRepository() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setUUID(container.getUUID() + "42");

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, folder);
		location.setContainer(container);

		assertFalse(connector.match(location, folder));
	}

	@Test
	public void matchFolderDifferentePath() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, folder);
		location.setContainer(container);
		location.setPath(location.getPath() + "42");

		assertFalse(connector.match(location, folder));
	}

	@Test
	public void matchBinaryResourceFile() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, binaryResource);
		location.setContainer(container);

		assertTrue(connector.match(location, binaryResource));
	}

	@Test
	public void matchBinaryResourceFileDifferenteBranch() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setBranchID(container.getBranchID() + 42);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, binaryResource);
		location.setContainer(container);

		assertFalse(connector.match(location, binaryResource));
	}

	@Test
	public void matchBinaryResourceFileDifferenteRepository() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setUUID(container.getUUID() + "42");

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, binaryResource);
		location.setContainer(container);

		assertFalse(connector.match(location, binaryResource));
	}

	@Test
	public void matchBinaryResourceFileDifferentePath() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, binaryResource);
		location.setContainer(container);
		location.setPath(location.getPath() + "42");

		assertFalse(connector.match(location, binaryResource));
	}

	@Test
	public void matchTextFile() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, textResource);
		location.setContainer(container);

		assertTrue(connector.match(location, textResource));
	}

	@Test
	public void matchTextFileDifferenteBranch() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setBranchID(container.getBranchID() + 42);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, textResource);
		location.setContainer(container);

		assertFalse(connector.match(location, textResource));
	}

	@Test
	public void matchTextFileDifferenteRepository() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setUUID(container.getUUID() + "42");

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, textResource);
		location.setContainer(container);

		assertFalse(connector.match(location, textResource));
	}

	@Test
	public void matchTextFileDifferentePath() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, textResource);
		location.setContainer(container);
		location.setPath(location.getPath() + "42");

		assertFalse(connector.match(location, textResource));
	}

	@Test
	public void matchResource() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, resource);
		location.setContainer(container);

		assertTrue(connector.match(location, resource));
	}

	@Test
	public void matchResourceDifferenteBranch() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setBranchID(container.getBranchID() + 42);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, resource);
		location.setContainer(container);

		assertFalse(connector.match(location, resource));
	}

	@Test
	public void matchResourceDifferenteRepository() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);
		container.setUUID(container.getUUID() + "42");

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, resource);
		location.setContainer(container);

		assertFalse(connector.match(location, resource));
	}

	@Test
	public void matchResourceDifferentePath() throws Exception {

		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));

		final ICDORepositoryLocation container = (ICDORepositoryLocation)MappingUtils.getConnectorRegistry()
				.createLocation(base, transaction);

		final TestCDOFolderLocation location = new TestCDOFolderLocation();
		connector.initLocation(container, location, resource);
		location.setContainer(container);
		location.setPath(location.getPath() + "42");

		assertFalse(connector.match(location, resource));
	}

}
