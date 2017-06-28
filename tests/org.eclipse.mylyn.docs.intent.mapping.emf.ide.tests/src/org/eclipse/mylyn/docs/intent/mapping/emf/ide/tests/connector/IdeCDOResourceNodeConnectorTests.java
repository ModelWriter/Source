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
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOResourceNodeConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector.IdeCDOResourceNodeConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOResourceNodeConnectorTests;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdeCDOResourceNodeConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeCDOResourceNodeConnectorTests extends CDOResourceNodeConnectorTests {

	@BeforeClass
	public static void beforeClass() throws Exception {
		CDOResourceNodeConnectorTests.beforeClass();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		CDOResourceNodeConnectorTests.afterClass();
	}

	@Test
	public void isRegistred() {
		assertTrue(MappingUtils.getConnectorRegistry().getConnector(
				ICDOResourceNodeLocation.class) instanceof CDOResourceNodeConnector);
	}

}
