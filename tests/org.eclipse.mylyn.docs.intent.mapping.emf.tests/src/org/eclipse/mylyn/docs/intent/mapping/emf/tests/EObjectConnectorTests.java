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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.EObjectConnectorParametrizedTests.TestEObjectContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.EObjectConnectorParametrizedTests.TestEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.LocationFactoryTests.ITestLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link EObjectConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnectorTests extends EObjectConnector {

	@Test
	public void getLocationTypeNotTypeContainer() {
		final Class<? extends ILocation> type = getLocationType(ITestLocation.class, "");

		assertNull(type);
	}

	@Test
	public void getLocationTypeNotElement() {
		final Class<? extends ILocation> type = getLocationType(TestEObjectLocation.class, new Object());

		assertNull(type);
	}

	@Test
	public void getLocation() {
		final Class<? extends ILocation> type = getLocationType(TestEObjectContainerLocation.class,
				EcorePackage.eINSTANCE);

		assertEquals(IEObjectLocation.class, type);
	}

	@Test
	public void initLocationEObject() {
		final IEObjectContainer container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		update(container, eObjects);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);

		super.initLocation(location, EcorePackage.eINSTANCE.getEClass());

		assertEquals(12592, location.getStartOffset());
		assertEquals(38893, location.getEndOffset());
		assertEquals(EcorePackage.eINSTANCE.getEClass(), location.getEObject());
		assertEquals(null, location.getEStructuralFeature());
		assertEquals(null, location.getValue());
		assertEquals(false, location.isSetting());
	}

	@Test
	public void initLocationSetting() {
		final IEObjectContainer container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		update(container, eObjects);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);

		super.initLocation(location, ((InternalEObject)EcorePackage.eINSTANCE.getEClass())
				.eSetting(EcorePackage.eINSTANCE.getENamedElement_Name()));

		assertEquals("name:EClass", container.getText().substring(location.getStartOffset(),
				location.getEndOffset()));
		assertEquals(12593, location.getStartOffset());
		assertEquals(EcorePackage.eINSTANCE.getEClass(), location.getEObject());
		assertEquals(EcorePackage.eINSTANCE.getENamedElement_Name(), location.getEStructuralFeature());
		assertEquals("EClass", location.getValue());
		assertEquals(true, location.isSetting());
	}

}
