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

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.AbstractLocationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link IEObjectLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractEObjectLocationTest extends AbstractLocationTests {

	@Override
	protected IEObjectLocation createLocation() throws InstantiationException, IllegalAccessException {
		return getBase().getFactory().createElement(IEObjectLocation.class);
	}

	@Test
	public void setEObjectNull() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setEObject(null);

		assertEquals(null, location.getEObject());
	}

	@Test
	public void setEObject() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setEObject(EcorePackage.eINSTANCE);

		assertEquals(EcorePackage.eINSTANCE, location.getEObject());
	}

	@Test
	public void getEObjectDefault() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		assertEquals(null, location.getEObject());
	}

	@Test
	public void setEStructuralFeatureNull() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setEStructuralFeature(null);

		assertEquals(null, location.getEStructuralFeature());
	}

	@Test
	public void setEStructuralFeature() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setEStructuralFeature(EcorePackage.eINSTANCE.getEClass_Interface());

		assertEquals(EcorePackage.eINSTANCE.getEClass_Interface(), location.getEStructuralFeature());
	}

	@Test
	public void getEStructuralFeatureDefault() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		assertEquals(null, location.getEStructuralFeature());
	}

	@Test
	public void setValueNull() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setValue(null);

		assertEquals(null, location.getValue());
	}

	@Test
	public void setValue() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		location.setValue(this);

		assertEquals(this, location.getValue());
	}

	@Test
	public void getValueDefault() throws InstantiationException, IllegalAccessException {
		IEObjectLocation location = createLocation();

		assertEquals(null, location.getValue());
	}

}
