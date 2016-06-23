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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IdeTextConnector;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorTests;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test {@link IdeTextConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeTextConnectorTests extends TextConnectorTests {

	@Test
	public void isRegistred() {
		boolean found = false;
		for (IConnector connect : MappingUtils.getConnectorRegistry().getConnectors()) {
			if (connect instanceof IdeTextConnector) {
				found = true;
				break;
			}
		}

		assertTrue(found);
	}

}
