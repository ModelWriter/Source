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

import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link IEObjectLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractEObjectLocationTest extends AbstractLocationTests {

	@Override
	protected IEObjectLocation createLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return getBase().getFactory().createElement(IEObjectLocation.class);
	}

	@Test
	public void setURIFragmentNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		location.setURIFragment(null);

		assertNull(location.getURIFragment());
	}

	@Test
	public void setURIFragment() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		location.setURIFragment("someFeature");

		assertEquals("someFeature", location.getURIFragment());
	}

	@Test
	public void getURIFragmentDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		assertNull(location.getURIFragment());
	}

	@Test
	public void setFeatureNameNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		location.setFeatureName(null);

		assertNull(location.getFeatureName());
	}

	@Test
	public void setFeatureName() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		location.setFeatureName("someFeature");

		assertEquals("someFeature", location.getFeatureName());
	}

	@Test
	public void getFeatureNameDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		IEObjectLocation location = createLocation();

		assertNull(location.getFeatureName());
	}

}
