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

import java.io.IOException;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.AllTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOServer;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.CDOUtils;
import org.eclipse.net4j.connector.IConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Tests {@link EObjectConnector} with CDO.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnectorParametrizedCDOTests extends EObjectConnectorParametrizedTests {

	/**
	 * The {@link CDOTransaction}.
	 */
	private static CDOTransaction transaction;

	public EObjectConnectorParametrizedCDOTests(Object[] original, Object[] altered) {
		super(original, altered);
	}

	@BeforeClass
	public static void beforeClass() {
		AllTests.startCDOServer();
		EObjectConnectorParametrizedTests.beforeClass();
		IConnector connector = CDOUtils.getConnector(CDOServer.PROTOCOL + "://" + CDOServer.IP + ":"
				+ CDOServer.PORT);
		CDOSession session = CDOUtils.openSession(connector, CDOServer.REPOSITORY_NAME);
		transaction = CDOUtils.openTransaction(session);
	}

	@Override
	protected Resource createResource(String name) {
		final CDOResource existingResource = transaction.getResource(name, false);
		if (existingResource != null && existingResource.isExisting()) {
			try {
				existingResource.delete(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// CHECKSTYLE:OFF
		return transaction.createResource("test.xmi");
	}

	@AfterClass
	public static void afterClass() {
		transaction.close();
		EObjectConnectorParametrizedTests.afterClass();
		AllTests.stopCDOServer();
	}

}
