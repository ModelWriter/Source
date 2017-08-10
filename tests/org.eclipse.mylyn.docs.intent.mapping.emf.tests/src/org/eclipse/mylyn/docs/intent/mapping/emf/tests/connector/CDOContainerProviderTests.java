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

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.AllTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOServer;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOUtils;
import org.eclipse.net4j.connector.IConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link CDOContainerProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOContainerProviderTests {

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction transaction;

	/**
	 * The {@link CDOResource}.
	 */
	private static CDOResource resource;

	/**
	 * The {@link EObject}.
	 */
	private static EObject eObject;

	@BeforeClass
	public static void beforeClass() throws ConcurrentAccessException, CommitException {
		AllTests.startCDOServer();
		IConnector connector = CDOUtils.getConnector(CDOServer.PROTOCOL + "://" + CDOServer.IP + ":"
				+ CDOServer.PORT);
		CDOSession session = CDOUtils.openSession(connector, CDOServer.REPOSITORY_NAME);
		transaction = CDOUtils.openTransaction(session);
		resource = transaction.createResource(CDOContainerProviderTests.class.getCanonicalName());
		eObject = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		resource.getContents().add(eObject);
		transaction.commit();
	}

	@AfterClass
	public static void afterClass() {
		transaction.close();
		AllTests.stopCDOServer();
	}

	@Test
	public void getContainerNull() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final Object result = provider.getContainer(null);

		assertNull(result);
	}

	@Test
	public void getContainerObject() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final Object element = new Object();
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainerEObjectNoResource() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final EObject element = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainerEObjectNoCDOResource() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final EObject element = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		final Resource r = new XMIResourceImpl();
		r.getContents().add(element);
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainerEOject() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final Object result = provider.getContainer(eObject);

		assertEquals(resource, result);
	}

	@Test
	public void getContainerCDOResource() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final Object result = provider.getContainer(resource);

		assertEquals(transaction, result);
	}

	@Test
	public void getContainerCDOView() {
		final CDOContainerProvider provider = new CDOContainerProvider();

		final Object result = provider.getContainer(transaction);

		assertNull(result);
	}

}
