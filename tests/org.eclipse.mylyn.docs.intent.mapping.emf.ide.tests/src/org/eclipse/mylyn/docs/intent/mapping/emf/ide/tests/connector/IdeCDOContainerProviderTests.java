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
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.CDOContainerProviderTests;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link CDOContainerProvider} in Ide environment.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeCDOContainerProviderTests extends CDOContainerProviderTests {

	@Test
	public void isRegistered() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final IContainerProvider provider = MappingUtils.getContainerProviderFactory().createProvider(
				CDOContainerProvider.class.getCanonicalName());
		assertTrue(provider instanceof CDOContainerProvider);
	}

}
