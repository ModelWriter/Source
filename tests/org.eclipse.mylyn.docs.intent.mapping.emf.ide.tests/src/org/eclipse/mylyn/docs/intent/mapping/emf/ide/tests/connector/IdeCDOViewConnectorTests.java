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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.connector;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOViewConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector.IdeCDOViewConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOViewConnectorTests;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdeCDOViewConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeCDOViewConnectorTests extends CDOViewConnectorTests {

	@BeforeClass
	public static void beforeClass() {
		CDOViewConnectorTests.beforeClass();
	}

	@AfterClass
	public static void afterClass() {
		CDOViewConnectorTests.afterClass();
	}

	@Test
	public void isRegistred() {
		assertTrue(MappingUtils.getConnectorRegistry().getConnector(
				ICDORepositoryLocation.class) instanceof CDOViewConnector);
	}

}
