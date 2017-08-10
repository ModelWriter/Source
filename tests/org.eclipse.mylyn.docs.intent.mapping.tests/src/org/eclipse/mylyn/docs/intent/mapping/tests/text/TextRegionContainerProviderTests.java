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
package org.eclipse.mylyn.docs.intent.mapping.tests.text;

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegionContainerProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link TextRegionContainerProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextRegionContainerProviderTests {

	@Test
	public void getContainerNull() {
		final TextRegionContainerProvider provider = new TextRegionContainerProvider();

		final Object result = provider.getContainer(null);

		assertNull(result);
	}

	@Test
	public void getContainerNotTextRegion() {
		final TextRegionContainerProvider provider = new TextRegionContainerProvider();

		final Object element = new Object();
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainer() {
		final TextRegionContainerProvider provider = new TextRegionContainerProvider();

		final Object container = new Object();
		final TextRegion region = new TextRegion(container, "text", 10, 14);
		final Object result = provider.getContainer(region);

		assertEquals(container, result);
	}

	@Test
	public void isRegistered() {
		final IBase base = new TestBase();

		final Object container = new Object();
		final TextRegion region = new TextRegion(container, "text", 10, 14);
		final Object result = base.getContainerProviderRegistry().getContainer(region);

		assertEquals(container, result);
	}

}
