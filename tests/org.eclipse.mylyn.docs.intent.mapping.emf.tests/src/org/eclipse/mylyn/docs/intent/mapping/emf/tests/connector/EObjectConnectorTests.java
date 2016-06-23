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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.LocationFactoryTests.ITestLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

		assertEquals(12772, location.getStartOffset());
		assertEquals(39773, location.getEndOffset());
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
		assertEquals(12778, location.getStartOffset());
		assertEquals(EcorePackage.eINSTANCE.getEClass(), location.getEObject());
		assertEquals(EcorePackage.eINSTANCE.getENamedElement_Name(), location.getEStructuralFeature());
		assertEquals("EClass", location.getValue());
		assertEquals(true, location.isSetting());
	}

	@Test
	public void isValidOffsetsEObjectMismatchedStartShorter() {
		final String text = "xxxx<<<<\n";
		final int startOffset = 0;
		final int endOffset = 9;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObjectMismatchedStartLonger() {
		final String text = "yyyyyyyyyyyyyyyyyyyyxxxx<<<<\n";
		final int startOffset = 20;
		final int endOffset = 29;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObjectMismatchedStartSameLength() {
		final String text = "yyyyyxxxx<<<<\n";
		final int startOffset = 0;
		final int endOffset = 14;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObjectMismatchedEndShorter() {
		final String text = ">>>>\nxxxx";
		final int startOffset = 0;
		final int endOffset = 9;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObjectMismatchedEndLonger() {
		final String text = ">>>>\nxxxxyyyyyyyyyyyyyyyyyyyy";
		final int startOffset = 0;
		final int endOffset = 14;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObjectMismatchedEndSameLenght() {
		final String text = ">>>>\nxxxxyyyyy";
		final int startOffset = 0;
		final int endOffset = 14;

		assertFalse(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsEObject() {
		final String text = ">>>>\nxxxx<<<<\n";
		final int startOffset = 0;
		final int endOffset = 14;

		assertTrue(isValidOffsets(text, null, startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedStartShorter() {
		final String text = "name:xxxx:\n";
		final int startOffset = 0;
		final int endOffset = 9;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedStartLonger() {
		final String text = "yyyyyyyyyyyyyyyyyyyyname:xxxx:\n";
		final int startOffset = 20;
		final int endOffset = 29;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedStartSameLength() {
		final String text = "yname:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureShorter() {
		final String text = "::\n";
		final int startOffset = 1;
		final int endOffset = 1;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureLonger() {
		final String text = ":yyyyyyyyyyyyyyyyyyyy:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 26;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureSameLenght() {
		final String text = ":yyyyyxxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndShorter() {
		final String text = ":name:xxxx";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndLonger() {
		final String text = ":name:xxxxyyyyyyyyyyyyyyyyyyyy";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndSameLenght() {
		final String text = ":name:xxxxyy";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

	@Test
	public void isValidOffsetsSetting() {
		final String text = ":name:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertTrue(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name(), startOffset,
				endOffset));
	}

}
