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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests;

import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFBaseTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFLinkTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFReportTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOViewConnectorTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorCDOTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedCDOTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.MappingCDOListenerTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the org.eclipse.mylyn.docs.intent.mapping.emf plug-in.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = {CDOMappingBaseListenerTests.class, EMFBaseTests.class, EMFLinkTests.class,
		EMFTextLocationTests.class, EMFEObjectLocationTests.class, EObjectConnectorTests.class,
		EObjectConnectorCDOTests.class, EObjectConnectorParametrizedTests.class,
		EObjectConnectorParametrizedCDOTests.class, EMFReportTests.class, CDOViewConnectorTests.class,
		CDOResourceNodeConnectorTests.class, EMFCDORepositoryLocationTests.class,
		EMFCDOFolderLocationTests.class, EMFCDOBinaryResourceLocationTests.class,
		EMFCDOTextResourceLocationTests.class, MappingCDOListenerTest.class, })
public class AllTests {
	/**
	 * The {@link CDOServer}.
	 */
	private static CDOServer server;

	/**
	 * Counts calls to {@link #startCDOServer()}.
	 */
	private static int startCount;

	@BeforeClass
	public static void startCDOServer() {
		startCount++;
		if (server == null) {
			server = new CDOServer(false);
			server.start();
		}
	}

	@AfterClass
	public static void stopCDOServer() {
		if (startCount == 0) {
			server.stop();
		} else {
			startCount--;
		}
	}

}
