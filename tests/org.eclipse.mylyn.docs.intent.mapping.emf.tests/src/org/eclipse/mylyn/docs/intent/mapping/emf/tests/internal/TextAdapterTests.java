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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests.internal;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.internal.TextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link TextAdapter}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextAdapterTests {

	@Test(expected = java.lang.NullPointerException.class)
	public void getOffsetsNull() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);

		adapter.getOffsets(null, null);
	}

	@Test
	public void getOffsetsNotFound() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final int[] res = adapter.getOffsets(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures(), eCls1);

		assertEquals(2, res.length);
		assertEquals(-1, res[0]);
		assertEquals(-1, res[1]);
	}

	@Test
	public void getOffsets() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final int[] res = adapter.getOffsets(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures(), eAttr2);

		assertEquals(2, res.length);
		assertEquals(776, res[0]);
		assertEquals(1099, res[1]);
	}

	@Test
	public void getTarget() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);

		assertEquals(eCls1, adapter.getTarget());
	}

	@Test
	public void getText() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);

		assertEquals(
				">>>>\n:name:ECls1:\n:instanceClassName:null:\n:instanceClass::\n:defaultValue:ACED000570:\n:instanceTypeName:null:\n:ePackage:null:\n:abstract:false:\n:interface:false:\n:eAllAttributes:#//eAttr1:\n:eAllAttributes:#//eAttr2:\n:eAllAttributes:#//eAttr3:\n:eAttributes:#//eAttr1:\n:eAttributes:#//eAttr2:\n:eAttributes:#//eAttr3:\n:eAllStructuralFeatures:#//eAttr1:\n:eAllStructuralFeatures:#//eAttr2:\n:eAllStructuralFeatures:#//eAttr3:\n:eIDAttribute:null:\n>>>>\n>>>>\n:name:eAttr1:\n:ordered:true:\n:unique:true:\n:lowerBound:0:\n:upperBound:1:\n:many:false:\n:required:false:\n:eType:null:\n:changeable:true:\n:volatile:false:\n:transient:false:\n:defaultValueLiteral:null:\n:defaultValue:ACED000570:\n:unsettable:false:\n:derived:false:\n:eContainingClass:#//:\n:iD:false:\n:eAttributeType:null:\n<<<<\n<<<<\n>>>>\n>>>>\n:name:eAttr2:\n:ordered:true:\n:unique:true:\n:lowerBound:0:\n:upperBound:1:\n:many:false:\n:required:false:\n:eType:null:\n:changeable:true:\n:volatile:false:\n:transient:false:\n:defaultValueLiteral:null:\n:defaultValue:ACED000570:\n:unsettable:false:\n:derived:false:\n:eContainingClass:#//:\n:iD:false:\n:eAttributeType:null:\n<<<<\n<<<<\n>>>>\n>>>>\n:name:eAttr3:\n:ordered:true:\n:unique:true:\n:lowerBound:0:\n:upperBound:1:\n:many:false:\n:required:false:\n:eType:null:\n:changeable:true:\n:volatile:false:\n:transient:false:\n:defaultValueLiteral:null:\n:defaultValue:ACED000570:\n:unsettable:false:\n:derived:false:\n:eContainingClass:#//:\n:iD:false:\n:eAttributeType:null:\n<<<<\n<<<<\n<<<<\n",
				adapter.getText());
	}

	@Test
	public void getTextOffsetNotInitialized() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);

		assertEquals(-1, adapter.getTextOffset());
	}

	@Test
	public void getTextOffset() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		assertEquals(-1, adapter.getTextOffset());

		final ITextAdapter childAdapter = (TextAdapter)EcoreUtil.getAdapter(eAttr2.eAdapters(),
				ITextAdapter.class);

		assertNotNull(childAdapter);
		assertEquals(776, childAdapter.getTextOffset());
	}

	@Test
	public void isAdapterForType() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();

		assertTrue(adapter.isAdapterForType(ITextAdapter.class));
	}

	@Test
	public void setLocationFromEObjectSetting() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final IEObjectLocation location = new TestEObjectLocation();
		location.setEObject(eCls1);
		location.setEStructuralFeature(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures());
		location.setValue(eAttr2);
		location.setSetting(true);

		adapter.setLocationFromEObject(location);

		assertEquals(776, location.getStartOffset());
		assertEquals(1099, location.getEndOffset());
	}

	@Test
	public void setLocationFromTextSetting() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final IEObjectLocation location = new TestEObjectLocation();

		location.setStartOffset(776);
		location.setEndOffset(1099);
		location.setSetting(true);

		adapter.setLocationFromText(location);

		assertEquals(eCls1, location.getEObject());
		assertEquals(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures(), location.getEStructuralFeature());
		assertEquals(eAttr2, location.getValue());
		assertEquals(true, location.isSetting());
	}

	@Test
	public void setLocationFromEObject() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final IEObjectLocation location = new TestEObjectLocation();
		location.setEObject(eAttr2);
		location.setSetting(false);

		adapter.setLocationFromEObject(location);

		assertEquals(776, location.getStartOffset());
		assertEquals(1099, location.getEndOffset());
	}

	@Test
	public void setLocationFromText() {
		final EClass eCls1 = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();
		eCls1.setName("ECls1");

		final EAttribute eAttr1 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr1.setName("eAttr1");

		final EAttribute eAttr2 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr2.setName("eAttr2");

		final EAttribute eAttr3 = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		eAttr3.setName("eAttr3");

		eCls1.getEStructuralFeatures().add(eAttr1);
		eCls1.getEStructuralFeatures().add(eAttr2);
		eCls1.getEStructuralFeatures().add(eAttr3);

		final TextAdapter adapter = new TextAdapter();
		adapter.setTarget(eCls1);
		adapter.getText();

		final IEObjectLocation location = new TestEObjectLocation();

		location.setStartOffset(776);
		location.setEndOffset(1099);
		location.setSetting(false);

		adapter.setLocationFromText(location);

		assertEquals(eAttr2, location.getEObject());
		assertEquals(null, location.getEStructuralFeature());
		assertEquals(null, location.getValue());
		assertEquals(false, location.isSetting());
	}

}
