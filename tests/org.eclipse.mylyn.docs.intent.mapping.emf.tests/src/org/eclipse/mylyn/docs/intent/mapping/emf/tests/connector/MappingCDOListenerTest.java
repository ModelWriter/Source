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

import anydsl.AnydslPackage;
import anydsl.ProductionCompany;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.emf.cdo.common.lob.CDOBlob;
import org.eclipse.emf.cdo.common.lob.CDOClob;
import org.eclipse.emf.cdo.common.util.CDOException;
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
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOBinaryResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOFolderLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOTextResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOResourceNodeConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOViewConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.MappingCDOListener;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.AllTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOServer;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOUtils;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests.TestCDOBinaryResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests.TestCDOFolderLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests.TestCDOResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests.TestCDOTextResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOViewConnectorTests.TestCDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestCouple;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests;
import org.eclipse.net4j.connector.IConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test {@link MappingCDOListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingCDOListenerTest {

	/**
	 * The buffer size.
	 */
	private static final int BUFFZE_SIZE = 8192;

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction transaction;

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction otherTransaction;

	/**
	 * The {@link CDOViewConnector}.
	 */
	private static CDOViewConnector viewConnector = new CDOViewConnector();

	/**
	 * The {@link CDOResourceNodeConnector}.
	 */
	private static CDOResourceNodeConnector resourceConnector = new CDOResourceNodeConnector();

	/**
	 * The {@link EObjectConnector}.
	 */
	private static EObjectConnector eObjectConnector = new EObjectConnector();

	@BeforeClass
	public static void beforeClass() {
		AllTests.startCDOServer();
		IConnector connector = CDOUtils.getConnector(CDOServer.PROTOCOL + "://" + CDOServer.IP + ":"
				+ CDOServer.PORT);
		CDOSession session = CDOUtils.openSession(connector, CDOServer.REPOSITORY_NAME);
		transaction = CDOUtils.openTransaction(session);
		otherTransaction = CDOUtils.openTransaction(session);

		viewConnector.addSessionToCache(transaction);

		MappingUtils.getConnectorRegistry().register(viewConnector);
		MappingUtils.getConnectorRegistry().register(resourceConnector);
		MappingUtils.getConnectorRegistry().register(eObjectConnector);
	}

	@AfterClass
	public static void afterClass() {
		viewConnector.removeSessionFromCache(transaction);

		MappingUtils.getConnectorRegistry().unregister(viewConnector);
		MappingUtils.getConnectorRegistry().unregister(resourceConnector);
		MappingUtils.getConnectorRegistry().unregister(eObjectConnector);

		transaction.close();
		otherTransaction.close();
		AllTests.stopCDOServer();
	}

	@Test
	public void deleteResourceNode() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final String path = this.getClass().getCanonicalName() + "/test1/";
		final CDOResourceFolder folder = otherTransaction.createResourceFolder(path);
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ILocation location = MappingUtils.getConnectorRegistry().createLocation(container, folder);

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());

		viewConnector.addBaseToUpdate(base);
		folder.delete(null);
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		try {
			transaction.getResourceNode(path);
			fail("the resource should be deleted by now.");
		} catch (CDOException e) {
			// nothing to do here
		}
		assertNull(location.getContainer());
	}

	@Test
	public void renameResourceNode() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final String path = this.getClass().getCanonicalName() + "/test2/";
		final CDOResourceFolder folder = otherTransaction.createResourceFolder(path);
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ICDOResourceNodeLocation location = (ICDOResourceNodeLocation)MappingUtils
				.getConnectorRegistry().createLocation(container, folder);

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());

		viewConnector.addBaseToUpdate(base);
		folder.setName("someNewName");
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		try {
			transaction.getResourceNode(path);
			fail("the resource should be deleted by now.");
		} catch (CDOException e) {
			// nothing to do here
		}
		assertEquals(
				"/org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.MappingCDOListenerTest/someNewName",
				location.getPath());
	}

	@Test
	public void renameParentResourceNode() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOFolderLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOFolderLocation>(TestCDOFolderLocation.class));

		final String path = this.getClass().getCanonicalName() + "/test3/";
		final CDOResourceFolder folder = otherTransaction.createResourceFolder(path);
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ICDOResourceNodeLocation location = (ICDOResourceNodeLocation)MappingUtils
				.getConnectorRegistry().createLocation(container, folder);

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());

		viewConnector.addBaseToUpdate(base);
		folder.getFolder().setName("someNewName");
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		try {
			transaction.getResourceNode(path);
			fail("the resource should be deleted by now.");
		} catch (CDOException e) {
			// nothing to do here
		}
		assertEquals("/someNewName/test3", location.getPath());
	}

	@Test
	public void resourceContentChanged() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOResourceLocation>(
						TestCDOResourceLocation.class));
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));

		final String path = this.getClass().getCanonicalName() + "/test.resource";
		final CDOResource resource = otherTransaction.createResource(path);
		final ProductionCompany eObject = AnydslPackage.eINSTANCE.getAnydslFactory()
				.createProductionCompany();
		resource.getContents().add(eObject);
		eObject.setName("MyCompany");
		resource.save(null);
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ICDOResourceLocation resourceLocation = (ICDOResourceLocation)MappingUtils
				.getConnectorRegistry().createLocation(container, resource);
		final IEObjectLocation eObjectLocation = (IEObjectLocation)MappingUtils.getConnectorRegistry()
				.createLocation(resourceLocation, eObject);

		final String initialXMIContent = resourceLocation.getXMIContent();
		final String initialURIFragment = eObjectLocation.getURIFragment();

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());
		assertFalse(initialXMIContent == null || initialXMIContent.isEmpty());
		assertFalse(initialURIFragment == null || initialURIFragment.isEmpty());

		viewConnector.addBaseToUpdate(base);
		eObject.setName("someNewName");
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		assertFalse(resourceLocation.getXMIContent().isEmpty());
		assertFalse(initialXMIContent.equals(resourceLocation.getXMIContent()));
		assertTrue(initialURIFragment.equals(eObjectLocation.getURIFragment()));
	}

	@Test
	public void textResourceContentChanged() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOTextResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOTextResourceLocation>(
						TestCDOTextResourceLocation.class));
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));

		final String path = this.getClass().getCanonicalName() + "/test.txt";
		final CDOTextResource resource = otherTransaction.createTextResource(path);
		resource.setContents(new CDOClob(new StringReader("some text")));
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ICDOTextResourceLocation resourceLocation = (ICDOTextResourceLocation)MappingUtils
				.getConnectorRegistry().createLocation(container, resource);

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());
		final String initialContent = getContent(resource.getContents().getContents());
		assertEquals(initialContent, resourceLocation.getText());

		viewConnector.addBaseToUpdate(base);
		resource.setContents(new CDOClob(new StringReader("some other text blah !")));
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		final String newContent = getContent(resource.getContents().getContents());
		assertFalse(initialContent.equals(newContent));
		assertEquals(newContent, resourceLocation.getText());
	}

	@Test
	@Ignore
	public void binaryResourceContentChanged() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(ICDORepositoryLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDORepositoryLocation>(
						TestCDORepositoryLocation.class));
		base.getFactory().addDescriptor(ICDOBinaryResourceLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestCDOBinaryResourceLocation>(
						TestCDOBinaryResourceLocation.class));
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));

		final String path = this.getClass().getCanonicalName() + "/test.bin";
		final CDOBinaryResource resource = otherTransaction.createBinaryResource(path);
		resource.setContents(new CDOBlob(new ByteArrayInputStream("some binary data".getBytes())));
		otherTransaction.commit();

		final ILocation container = MappingUtils.getConnectorRegistry().createLocation(base, transaction);
		final ICDOBinaryResourceLocation resourceLocation = (ICDOBinaryResourceLocation)MappingUtils
				.getConnectorRegistry().createLocation(container, resource);

		assertNotNull(otherTransaction.getResourceNode(path));
		assertFalse(container.getContents().isEmpty());

		viewConnector.addBaseToUpdate(base);
		resource.setContents(new CDOBlob(new ByteArrayInputStream("some other binary data blah !"
				.getBytes())));
		otherTransaction.commit();
		viewConnector.removeBaseToUpdate(base);

		// TODO assert resourceLocation
	}

	/**
	 * Gets the content of the given {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader}
	 * @return the content of the given {@link Reader}
	 * @throws IOException
	 *             if the {@link Reader} can't be read
	 */
	private String getContent(Reader reader) throws IOException {
		final StringBuilder builder = new StringBuilder();

		char[] buffer = new char[BUFFZE_SIZE];
		while (reader.read(buffer) != -1) {
			builder.append(buffer);
		}

		return builder.toString();
	}

}
