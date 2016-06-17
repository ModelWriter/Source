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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.resource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.resource.AbstractResourceLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link IEObjectFileLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractEObjectFileLocationTests extends AbstractResourceLocationTests {

	protected IEObjectFileLocation createLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return getBase().getFactory().createElement(IEObjectFileLocation.class);
	}

	@Test
	public void setEObjectsNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IEObjectFileLocation location = createLocation();

		location.setEObjects(null);

		assertEquals(null, location.getEObjects());
	}

	@Test
	public void setEObjects() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final IEObjectFileLocation location = createLocation();
		final List<EObject> eObjects = new ArrayList<EObject>();

		location.setEObjects(eObjects);

		assertEquals(eObjects, location.getEObjects());
	}

	@Test
	public void getEObjectsDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IEObjectFileLocation location = createLocation();

		location.getEObjects();

		assertTrue(location.getEObjects() instanceof List);
		assertEquals(0, location.getEObjects().size());
	}

}
