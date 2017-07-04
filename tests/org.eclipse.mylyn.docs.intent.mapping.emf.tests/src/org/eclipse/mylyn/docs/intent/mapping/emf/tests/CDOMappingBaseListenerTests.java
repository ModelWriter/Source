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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests;

import anydsl.AnydslPackage;
import anydsl.ProductionCompany;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.emf.CDOMappingBaseListener;
import org.eclipse.net4j.connector.IConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link CDOMappingBaseListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOMappingBaseListenerTests {

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction transaction;

	@BeforeClass
	public static void beforeClass() {
		AllTests.startCDOServer();
		IConnector c = CDOUtils.getConnector(CDOServer.PROTOCOL + "://" + CDOServer.IP + ":"
				+ CDOServer.PORT);
		CDOSession session = CDOUtils.openSession(c, CDOServer.REPOSITORY_NAME);
		transaction = CDOUtils.openTransaction(session);
	}

	@AfterClass
	public static void afterClass() {
		transaction.close();
		AllTests.stopCDOServer();
	}

	@Test
	public void attacheObject() throws Exception {
		final CDOMappingBaseListener listener = new CDOMappingBaseListener();

		final String path = this.getClass().getCanonicalName() + "/attacheObject.resource";
		final CDOResource resource = transaction.createResource(path);
		resource.save(null);
		transaction.commit();

		transaction.getSession().addListener(listener);
		final ProductionCompany eObject = AnydslPackage.eINSTANCE.getAnydslFactory()
				.createProductionCompany();
		resource.getContents().add(eObject);
		eObject.setName("MyCompany");
		resource.save(null);
		transaction.commit();
		transaction.getSession().removeListener(listener);

		assertEquals(0, MappingUtils.getMappingRegistry().getBases().size());
	}

	@Test
	public void detacheObject() throws Exception {
		final CDOMappingBaseListener listener = new CDOMappingBaseListener();

		final String path = this.getClass().getCanonicalName() + "/detacheObject.resource";
		final CDOResource resource = transaction.createResource(path);
		final ProductionCompany eObject = AnydslPackage.eINSTANCE.getAnydslFactory()
				.createProductionCompany();
		resource.getContents().add(eObject);
		eObject.setName("MyCompany");
		resource.save(null);
		transaction.commit();

		transaction.getSession().addListener(listener);
		resource.getContents().clear();
		resource.save(null);
		transaction.commit();
		transaction.getSession().removeListener(listener);

		assertEquals(0, MappingUtils.getMappingRegistry().getBases().size());
	}

	@Test
	public void attacheBase() throws Exception {
		final CDOMappingBaseListener listener = new CDOMappingBaseListener();

		final String path = this.getClass().getCanonicalName() + "/attacheBase.resource";
		final CDOResource resource = transaction.createResource(path);
		resource.save(null);
		transaction.commit();

		transaction.getSession().addListener(listener);
		final Base eObject = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		resource.getContents().add(eObject);
		eObject.setName("MyBase");
		resource.save(null);
		transaction.commit();
		transaction.getSession().removeListener(listener);

		assertEquals(1, MappingUtils.getMappingRegistry().getBases().size());
		assertEquals("MyBase", MappingUtils.getMappingRegistry().getBases().iterator().next().getName());
		MappingUtils.getMappingRegistry().unregister(eObject);
	}

	@Test
	public void detacheBase() throws Exception {
		final CDOMappingBaseListener listener = new CDOMappingBaseListener();

		transaction.getSession().addListener(listener);
		final String path = this.getClass().getCanonicalName() + "/detacheBase.resource";
		final CDOResource resource = transaction.createResource(path);
		final Base eObject = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		resource.getContents().add(eObject);
		eObject.setName("MyBase");
		resource.save(null);
		transaction.commit();

		assertEquals(1, MappingUtils.getMappingRegistry().getBases().size());
		assertEquals("MyBase", MappingUtils.getMappingRegistry().getBases().iterator().next().getName());

		resource.getContents().clear();
		resource.save(null);
		transaction.commit();
		transaction.getSession().removeListener(listener);

		assertEquals(0, MappingUtils.getMappingRegistry().getBases().size());
	}

}
