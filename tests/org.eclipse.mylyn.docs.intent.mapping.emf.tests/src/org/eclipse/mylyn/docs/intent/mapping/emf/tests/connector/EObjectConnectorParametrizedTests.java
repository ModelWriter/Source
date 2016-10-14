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
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link EObjectConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Parameterized.class)
public class EObjectConnectorParametrizedTests {

	/**
	 * Test {@link IEObjectContainer}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectContainerLocation extends TestTextContainerLocation implements IEObjectContainer {

		/**
		 * The {@link List} of {@link EObject}.
		 */
		private List<EObject> eObjects;

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer#setEObjects(java.util.List)
		 */
		public void setEObjects(List<EObject> eObjs) {
			eObjects = eObjs;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer#getEObjects()
		 */
		public List<EObject> getEObjects() {
			return eObjects;
		}

	}

	/**
	 * Test {@link IEObjectLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectLocation extends TestTextLocation implements IEObjectLocation {

		/**
		 * The {@link EObject}.
		 */
		private EObject eObj;

		/**
		 * The {@link EStructuralFeature}.
		 */
		private EStructuralFeature eFeature;

		/**
		 * Should we use the feature.
		 */
		private boolean setting;

		/**
		 * The value.
		 */
		private Object val;

		public EObject getEObject() {
			return eObj;
		}

		public void setEObject(EObject eObject) {
			eObj = eObject;
		}

		public EStructuralFeature getEStructuralFeature() {
			return eFeature;
		}

		public void setEStructuralFeature(EStructuralFeature feature) {
			eFeature = feature;
		}

		public Object getValue() {
			return val;
		}

		public void setValue(Object value) {
			val = value;
		}

		public void setSetting(boolean setting) {
			this.setting = setting;
		}

		public boolean isSetting() {
			return setting;
		}

	}

	/**
	 * The original [{@link IEObjectLocation#getEObject() EObject},
	 * {@link IEObjectLocation#getEStructuralFeature() feature}, {@link IEObjectLocation#getValue() value}].
	 */
	private final Object[] original;

	/**
	 * The altered [{@link IEObjectLocation#getEObject() EObject},
	 * {@link IEObjectLocation#getEStructuralFeature() feature}, {@link IEObjectLocation#getValue() value}].
	 */
	private final Object[] altered;

	public EObjectConnectorParametrizedTests(Object[] original, Object[] altered) {
		this.original = original;
		this.altered = altered;
	}

	@Parameters(name = "{index}")
	public static Iterable<Object[][]> data() {
		return Arrays.asList(new Object[][][] {
				{ {EcorePackage.eINSTANCE.getEClassifiers().get(7), null, null, },
						{createEClass(), null, null, }, },
				{ {EcorePackage.eINSTANCE.getEClassifiers().get(10), null, null, },
						{createEClass(), null, null, }, },
				{
						{((EClass)EcorePackage.eINSTANCE.getEClassifiers().get(10)).getEOperations().get(4),
								null, null, }, {createEOperation(), null, null, }, },
				{
						{EcorePackage.eINSTANCE.getEClassifiers().get(7),
								EcorePackage.eINSTANCE.getENamedElement_Name(), "EFactory", },
						{EcorePackage.eINSTANCE.getEClassifiers().get(7),
								EcorePackage.eINSTANCE.getENamedElement_Name(), "ESomethingElse", }, },

				{
						{((EClass)EcorePackage.eINSTANCE.getEClassifiers().get(10)).getEOperations().get(4),
								EcorePackage.eINSTANCE.getENamedElement_Name(), "eContainingFeature", },
						{((EClass)EcorePackage.eINSTANCE.getEClassifiers().get(10)).getEOperations().get(4),
								EcorePackage.eINSTANCE.getENamedElement_Name(), "someEOperation", }, },

				{
						{EcorePackage.eINSTANCE.getEClass_Abstract(),
								EcorePackage.eINSTANCE.getETypedElement_EType(),
								EcorePackage.eINSTANCE.getEBoolean(), },
						{EcorePackage.eINSTANCE.getEClass_Abstract(),
								EcorePackage.eINSTANCE.getETypedElement_EType(),
								EcorePackage.eINSTANCE.getEBooleanObject(), }, }, });
	}

	private static EClass createEClass() {
		final EClass res = EcorePackage.eINSTANCE.getEcoreFactory().createEClass();

		res.setName("SomeTestEClass");

		return res;
	}

	private static EOperation createEOperation() {
		final EOperation res = EcorePackage.eINSTANCE.getEcoreFactory().createEOperation();

		res.setName("someTestEOperation");

		return res;
	}

	protected IEObjectLocation createEObjectLocation(Copier copier, TestEObjectContainerLocation container) {
		final IEObjectLocation res = new TestEObjectLocation();

		res.setEObject(copier.get(original[0]));
		res.setSetting(original[1] != null);
		res.setEStructuralFeature((EStructuralFeature)original[1]);
		if (original[2] instanceof EObject) {
			res.setValue(copier.get(original[2]));
		} else {
			res.setValue(original[2]);
		}
		final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(res.getEObject());
		textAdapter.setLocationFromEObject(res);
		container.getContents().add(res);
		res.setContainer(container);

		return res;
	}

	public void assertEObjectLocation(IEObjectLocation location, String expectedText, int expectedTextOffset,
			EObject expectedEObject, EStructuralFeature expectedFeature, Object expectedValue) {
		if (location.getStartOffset() < 0 && location.getEndOffset() < 0) {
			assertEquals(expectedText, "");
		} else {
			assertEquals(expectedText, ((ITextContainer)location.getContainer()).getText().substring(
					location.getStartOffset(), location.getEndOffset()));
		}
		assertEquals(expectedTextOffset, location.getStartOffset());
		assertEquals(expectedEObject, location.getEObject());
		assertEquals(expectedFeature, location.getEStructuralFeature());
		assertEquals(expectedValue, location.getValue());
	}

	@Test
	public void updateEObjects() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = ((ITextContainer)location.getContainer()).getText().substring(
				location.getStartOffset(), location.getEndOffset());
		final int expectedTextOffset = location.getStartOffset();
		final EObject expectedEObject = copier.get(original[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (original[2] instanceof EObject) {
			expectedValue = copier.get(original[2]);
		} else {
			expectedValue = original[2];
		}

		List<EObject> newEObjects = testEObjects;
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShift() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = ((ITextContainer)location.getContainer()).getText().substring(
				location.getStartOffset(), location.getEndOffset());
		final int offset = getText(new ArrayList<EObject>(MappingPackage.eINSTANCE.getEClassifiers()))
				.length();
		final int expectedTextOffset = location.getStartOffset() + offset;
		final EObject expectedEObject = copier.get(original[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (original[2] instanceof EObject) {
			expectedValue = copier.get(original[2]);
		} else {
			expectedValue = original[2];
		}

		List<EObject> newEObjects = new ArrayList<EObject>(MappingPackage.eINSTANCE.getEClassifiers());
		newEObjects.addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShift() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = ((ITextContainer)location.getContainer()).getText().substring(
				location.getStartOffset(), location.getEndOffset());
		ArrayList<EObject> removedEObjects = new ArrayList<EObject>();
		removedEObjects.add(testEObjects.get(0));
		removedEObjects.add(testEObjects.get(1));
		final int offset = getText(removedEObjects).length();
		final int expectedTextOffset = location.getStartOffset() - offset;
		final EObject expectedEObject = copier.get(original[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (original[2] instanceof EObject) {
			expectedValue = copier.get(original[2]);
		} else {
			expectedValue = original[2];
		}

		List<EObject> newEObjects = new ArrayList<EObject>(testEObjects);
		newEObjects.remove(0);
		newEObjects.remove(0);
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsAltered() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final int expectedTextOffset = location.getStartOffset();
		final EObject expectedEObject = copier.get(altered[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)altered[1];
		final Object expectedValue;
		if (altered[2] instanceof EObject) {
			expectedValue = copier.get(altered[2]);
		} else {
			expectedValue = altered[2];
		}

		List<EObject> newEObjects = testEObjects;
		newEObjects = alterEObjects(copier, newEObjects);

		final String expectedText;
		if (original[1] == null) {
			final ArrayList<EObject> alteredEObjects = new ArrayList<EObject>();
			alteredEObjects.add(copier.get(altered[0]));
			expectedText = getText(alteredEObjects);
		} else if (altered[2] instanceof EObject) {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], copier
					.get(altered[2]));
		} else {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], altered[2]);
		}

		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftAltered() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final int offset = getText(new ArrayList<EObject>(MappingPackage.eINSTANCE.getEClassifiers()))
				.length();
		final int expectedTextOffset = location.getStartOffset() + offset;
		final EObject expectedEObject = copier.get(altered[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (altered[2] instanceof EObject) {
			expectedValue = copier.get(altered[2]);
		} else {
			expectedValue = altered[2];
		}

		List<EObject> newEObjects = new ArrayList<EObject>(MappingPackage.eINSTANCE.getEClassifiers());
		newEObjects.addAll(testEObjects);
		newEObjects = alterEObjects(copier, newEObjects);

		final String expectedText;
		if (original[1] == null) {
			final ArrayList<EObject> alteredEObjects = new ArrayList<EObject>();
			alteredEObjects.add(copier.get(altered[0]));
			expectedText = getText(alteredEObjects);
		} else if (altered[2] instanceof EObject) {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], copier
					.get(altered[2]));
		} else {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], altered[2]);
		}

		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftAltered() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		ArrayList<EObject> removedEObjects = new ArrayList<EObject>();
		removedEObjects.add(testEObjects.get(0));
		removedEObjects.add(testEObjects.get(1));
		final int offset = getText(removedEObjects).length();
		final int expectedTextOffset = location.getStartOffset() - offset;
		final EObject expectedEObject = copier.get(altered[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (altered[2] instanceof EObject) {
			expectedValue = copier.get(altered[2]);
		} else {
			expectedValue = altered[2];
		}

		List<EObject> newEObjects = new ArrayList<EObject>(testEObjects);
		newEObjects.remove(0);
		newEObjects.remove(0);
		newEObjects = alterEObjects(copier, newEObjects);

		final String expectedText;
		if (original[1] == null) {
			final ArrayList<EObject> alteredEObjects = new ArrayList<EObject>();
			alteredEObjects.add(copier.get(altered[0]));
			expectedText = getText(alteredEObjects);
		} else if (altered[2] instanceof EObject) {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], copier
					.get(altered[2]));
		} else {
			expectedText = getText(copier.get(altered[0]), (EStructuralFeature)altered[1], altered[2]);
		}

		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsRemoved() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = -1;
		final EObject expectedEObject = null;
		final EStructuralFeature expectedFeature = null;
		final Object expectedValue = null;

		List<EObject> newEObjects = testEObjects;
		if (testEObjects.contains(copier.get(original[0]))) {
			testEObjects.remove(copier.get(original[0]));
		} else {
			EcoreUtil.delete(copier.get(original[0]));
		}
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftRemoved() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = -1;
		final EObject expectedEObject = null;
		final EStructuralFeature expectedFeature = null;
		final Object expectedValue = null;

		List<EObject> newEObjects = testEObjects;
		if (testEObjects.contains(copier.get(original[0]))) {
			testEObjects.remove(copier.get(original[0]));
		} else {
			EcoreUtil.delete(copier.get(original[0]));
		}

		newEObjects.addAll(0, MappingPackage.eINSTANCE.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftRemoved() {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = -1;
		final EObject expectedEObject = null;
		final EStructuralFeature expectedFeature = null;
		final Object expectedValue = null;

		List<EObject> newEObjects = testEObjects;
		if (testEObjects.contains(copier.get(original[0]))) {
			testEObjects.remove(copier.get(original[0]));
		} else {
			EcoreUtil.delete(copier.get(original[0]));
		}

		newEObjects.remove(0);
		newEObjects.remove(0);
		EObjectConnector.updateEObjectContainer(container, newEObjects);

		assertEquals(newEObjects, container.getEObjects());
		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	private String getText(List<EObject> eObjects) {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		EObjectConnector.updateEObjectContainer(container, eObjects);

		return container.getText();
	}

	private String getText(EObject eObject, EStructuralFeature feature, Object value) {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		List<EObject> eObjects = new ArrayList<EObject>();
		eObjects.add(eObject);
		EObjectConnector.updateEObjectContainer(container, eObjects);
		final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(eObject);
		final int[] offsets = textAdapter.getOffsets(feature, value);

		return container.getText().substring(offsets[0], offsets[1]);
	}

	private List<EObject> alterEObjects(Copier copier, List<EObject> eObjects) {
		final List<EObject> res = eObjects;

		// no feature
		if (original[1] == null) {
			final int index = res.indexOf(copier.get(original[0]));
			if (index != -1) {
				// replace the object in the list
				res.remove(index);
				res.add(index, copier.get(altered[0]));
			} else {
				// replace the object in its container
				EcoreUtil.replace(copier.get(original[0]), copier.get(altered[0]));
			}
		} else {
			// alter the feature
			final EStructuralFeature feature = (EStructuralFeature)altered[1];
			if (feature.isMany()) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>)copier.get(original[0]).eGet(feature);
				final int index = list.indexOf(copier.get(original[2]));
				list.remove(index);
				list.add(index, copier.get(altered[2]));
			} else {
				if (altered[2] instanceof EObject) {
					copier.get(original[0]).eSet(feature, copier.get(altered[2]));
				} else {
					copier.get(original[0]).eSet(feature, altered[2]);
				}
			}
		}

		return res;
	}
}
