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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		 * The {@link Resource}.
		 */
		private Resource resource;

		public void setResource(Resource res) {
			resource = res;
		}

		public Resource getResource() {
			return resource;
		}

	}

	/**
	 * Connector for {@link TestEObjectContainerLocation}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectContainerConnector extends AbstractConnector {

		public ILocationDescriptor getLocationDescriptor(ILocationDescriptor containerDescriptor,
				Object element) {
			// nothing to do here
			return null;
		}

		public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
				Object element) {
			// nothing to do here
			return null;
		}

		public Object getElement(ILocation location) {
			final Resource res;

			if (location instanceof TestEObjectContainerLocation) {
				res = ((TestEObjectContainerLocation)location).getResource();
			} else {
				res = null;
			}

			return res;
		}

		public String getName(ILocation location) {
			// nothing to do here
			return null;
		}

		public Class<? extends ILocation> getType() {
			return IEObjectContainer.class;
		}

		@Override
		protected boolean canUpdate(Object element) {
			// nothing to do here
			return false;
		}

		@Override
		protected boolean match(ILocation location, Object element) {
			// nothing to do here
			return false;
		}

		@Override
		protected void initLocation(ILocationContainer container, ILocation location, Object element) {
			// nothing to do here
		}

	}

	/**
	 * Test {@link IEObjectLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectLocation extends TestTextLocation implements IEObjectLocation {

		/**
		 * The {@link EStructuralFeature#getName() feature name}.
		 */
		private String featureName;

		public void setFeatureName(String featureName) {
			this.featureName = featureName;
		}

		public String getFeatureName() {
			return featureName;
		}

	}

	/**
	 * The {@link TestEObjectContainerConnector}.
	 */
	private static TestEObjectContainerConnector testEObjectContainerConnector = new TestEObjectContainerConnector();

	/**
	 * The {@link EObjectConnector}.
	 */
	private static EObjectConnector eObjectConnector = new EObjectConnector();

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

	@BeforeClass
	public static void beforeClass() {
		MappingUtils.getConnectorRegistry().register(testEObjectContainerConnector);
		MappingUtils.getConnectorRegistry().register(eObjectConnector);
	}

	@AfterClass
	public static void afterClass() {
		MappingUtils.getConnectorRegistry().unregister(testEObjectContainerConnector);
		MappingUtils.getConnectorRegistry().unregister(eObjectConnector);
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

		final EObject eObject = copier.get(original[0]);
		final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(eObject);
		if (original[1] != null) {
			final EStructuralFeature feature = (EStructuralFeature)original[1];
			final Object value;
			if (original[2] instanceof EObject) {
				value = copier.get(original[2]);
			} else {
				value = original[2];
			}
			textAdapter.setLocationFromEObject(res, feature, value);
		} else {
			textAdapter.setLocationFromEObject(res);
		}
		container.getContents().add(res);
		res.setContainer(container);

		return res;
	}

	public void assertEObjectLocation(IEObjectLocation location, String expectedText, int expectedTextOffset,
			EObject expectedEObject, EStructuralFeature expectedFeature, Object expectedValue) {
		if (location.getContainer() == null) {
			assertEquals(expectedText, "");
		} else {
			assertEquals(expectedText, ((ITextContainer)location.getContainer()).getText().substring(
					location.getStartOffset(), location.getEndOffset()));
		}
		assertEquals(expectedTextOffset, location.getStartOffset());
		final Object element = MappingUtils.getConnectorRegistry().getElement(location);
		if (element instanceof Setting) {
			final Setting setting = (Setting)element;
			assertEquals(expectedEObject, setting.getEObject());
			assertEquals(expectedFeature, setting.getEStructuralFeature());
			assertEquals(expectedValue, setting.get(true));
		} else if (element instanceof EObject) {
			assertEquals(expectedEObject, element);
		} else {
			assertNull(location.getContainer());
		}
	}

	@Test
	public void updateEObjects() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShift() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShift() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsAltered() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftAltered() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftAltered() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsRemoved() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = location.getStartOffset();
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftRemoved() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = location.getStartOffset();
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftRemoved() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(EcorePackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		EObjectConnector.updateEObjectContainer(container, testEObjects);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final String expectedText = "";
		final int expectedTextOffset = location.getStartOffset();
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
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(newEObjects);
		container.setResource(resource);

		assertEObjectLocation(location, expectedText, expectedTextOffset, expectedEObject, expectedFeature,
				expectedValue);
	}

	private String getText(List<EObject> eObjects) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		EObjectConnector.updateEObjectContainer(container, eObjects);

		return container.getText();
	}

	private String getText(EObject eObject, EStructuralFeature feature, Object value)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
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
