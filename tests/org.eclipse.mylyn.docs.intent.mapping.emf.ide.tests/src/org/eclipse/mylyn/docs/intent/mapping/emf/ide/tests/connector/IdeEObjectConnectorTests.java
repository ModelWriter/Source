/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector.IdeEObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorTests;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdeEObjectConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeEObjectConnectorTests extends EObjectConnectorTests {

	@Test
	public void isRegistred() {
		assertTrue(MappingUtils.getConnectorRegistry().getConnector(IEObjectLocation.class) instanceof IdeEObjectConnector);
	}

}
