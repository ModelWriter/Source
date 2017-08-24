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

import anydsl.AnydslPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;
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
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestCouple;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests;
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
	public static class TestEObjectContainerLocation extends TestLocation implements IEObjectContainer {

		/**
		 * The XMI content.
		 */
		private String xmiContent;

		/**
		 * The {@link Resource}.
		 */
		private Resource resource;

		/**
		 * The mapping from {@link EObject} {@link URI} fragments to saved {@link URI} fragment.
		 */
		private List<ICouple> uriFragments = new ArrayList<ICouple>();

		public void setResource(Resource res) {
			resource = res;
		}

		public Resource getResource() {
			return resource;
		}

		public String getXMIContent() {
			return xmiContent;
		}

		public void setXMIContent(String content) {
			this.xmiContent = content;
		}

		public List<ICouple> getSavedURIFragments() {
			return uriFragments;
		}

	}

	/**
	 * Connector for {@link TestEObjectContainerLocation}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectContainerConnector extends AbstractConnector {

		public ILocationDescriptor getLocationDescriptor(IBase base, Object element) {
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
	public static class TestEObjectLocation extends TestLocation implements IEObjectLocation {

		/**
		 * The URI fragment.
		 */
		private String uriFragment;

		/**
		 * The saved URI fragment.
		 */
		private String savedUriFragment;

		/**
		 * The {@link EStructuralFeature#getName() feature name}.
		 */
		private String featureName;

		/**
		 * The index in the {@link EStructuralFeature#getName() feature}.
		 */
		private int index;

		public String getURIFragment() {
			return uriFragment;
		}

		public void setURIFragment(String fragment) {
			this.uriFragment = fragment;
		}

		public String getSavedURIFragment() {
			return savedUriFragment;
		}

		public void setSavedURIFragment(String fragment) {
			savedUriFragment = fragment;
		}

		public void setFeatureName(String featureName) {
			this.featureName = featureName;
		}

		public String getFeatureName() {
			return featureName;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
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

	@Parameters
	public static Iterable<Object[][]> data() {
		return Arrays.asList(new Object[][][] {{{AnydslPackage.eINSTANCE.getEClassifiers().get(7), null,
				null, }, {createEClass(), null, null, }, }, {{AnydslPackage.eINSTANCE.getEClassifiers().get(
						10), null, null, }, {createEClass(), null, null, }, }, {{
								((EClass)AnydslPackage.eINSTANCE.getEClassifiers().get(10)).getEOperations()
										.get(4), null, null, }, {createEOperation(), null, null, }, }, {{
												AnydslPackage.eINSTANCE.getEClassifiers().get(7),
												EcorePackage.eINSTANCE.getENamedElement_Name(),
												"Restaurant", }, {AnydslPackage.eINSTANCE.getEClassifiers()
														.get(7), EcorePackage.eINSTANCE
																.getENamedElement_Name(),
														"ESomethingElse", }, },

				{{((EClass)AnydslPackage.eINSTANCE.getEClassifiers().get(10)).getEOperations().get(4),
						EcorePackage.eINSTANCE.getENamedElement_Name(), "setCaliber", }, {
								((EClass)AnydslPackage.eINSTANCE.getEClassifiers().get(10)).getEOperations()
										.get(4), EcorePackage.eINSTANCE.getENamedElement_Name(),
								"someEOperation", }, },

				{{AnydslPackage.eINSTANCE.getFood(), EcorePackage.eINSTANCE.getEClass_EOperations(),
						AnydslPackage.eINSTANCE.getFood__PreferredColor(), }, {AnydslPackage.eINSTANCE
								.getFood(), EcorePackage.eINSTANCE.getEClass_EOperations(),
								createEOperation(), }, },

				{{AnydslPackage.eINSTANCE.getNamedElement_Name(), EcorePackage.eINSTANCE
						.getETypedElement_EType(), AnydslPackage.eINSTANCE.getSingleString(), }, {
								AnydslPackage.eINSTANCE.getNamedElement_Name(), EcorePackage.eINSTANCE
										.getETypedElement_EType(), AnydslPackage.eINSTANCE
												.getCountryData(), }, }, });
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

	/**
	 * Creates a new {@link Resource}.
	 * 
	 * @param name
	 *            the resource name
	 * @return the created {@link Resource}
	 */
	protected Resource createResource(String name) {
		return new XMIResourceImpl(URI.createURI(name));
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

	protected IEObjectLocation createEObjectLocation(Copier copier, TestEObjectContainerLocation container)
			throws Exception {
		final TestEObjectLocation res;

		final EObject eObject = copier.get(original[0]);
		if (original[1] != null) {
			final EStructuralFeature feature = (EStructuralFeature)original[1];
			final Object value;
			if (original[2] instanceof EObject) {
				value = copier.get(original[2]);
			} else {
				value = original[2];
			}
			res = (TestEObjectLocation)MappingUtils.getConnectorRegistry().createLocation(container,
					new Setting() {
						public void unset() {
							// nothing to do here
						}

						public void set(Object newValue) {
							// nothing to do here
						}

						public boolean isSet() {
							return false;
						}

						public EStructuralFeature getEStructuralFeature() {
							return feature;
						}

						public EObject getEObject() {
							return eObject;
						}

						public Object get(boolean resolve) {
							return value;
						}
					});
		} else {
			res = (TestEObjectLocation)MappingUtils.getConnectorRegistry().createLocation(container, eObject);
		}
		res.setObject(eObject);

		return res;
	}

	public void assertEObjectLocation(IEObjectLocation location, EObject expectedEObject,
			EStructuralFeature expectedFeature, Object expectedValue) {
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
	public void updateEObjects() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final EObject expectedEObject = copier.get(original[0]);
		final EStructuralFeature expectedFeature = (EStructuralFeature)original[1];
		final Object expectedValue;
		if (original[2] instanceof EObject) {
			expectedValue = copier.get(original[2]);
		} else {
			expectedValue = original[2];
		}

		List<EObject> newEObjects = testEObjects;
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsPlusShift() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsMinusShift() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsAltered() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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

		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftAltered() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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

		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftAltered() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		if (!copier.containsKey(altered[0])) {
			copier.copy((EObject)altered[0]);
		}
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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

		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsRemoved() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

		final EObject expectedEObject = null;
		final EStructuralFeature expectedFeature = null;
		final Object expectedValue = null;

		List<EObject> newEObjects = testEObjects;
		if (testEObjects.contains(copier.get(original[0]))) {
			testEObjects.remove(copier.get(original[0]));
		} else {
			EcoreUtil.delete(copier.get(original[0]));
		}
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsPlusShiftRemoved() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
	}

	@Test
	public void updateEObjectsMinusShiftRemoved() throws Exception {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IEObjectLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestEObjectLocation>(TestEObjectLocation.class));
		base.getFactory().addDescriptor(ICouple.class, new BaseElementFactory.FactoryDescriptor<TestCouple>(
				TestCouple.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final Copier copier = new Copier();
		EPackage copy = (EPackage)copier.copy(AnydslPackage.eINSTANCE);
		copier.copyReferences();
		final List<EObject> testEObjects = new ArrayList<EObject>(copy.getEClassifiers());
		final Resource initialResource = createResource("initialResource");
		container.setResource(initialResource);
		initialResource.getContents().addAll(testEObjects);
		EObjectConnector.updateEObjectContainer(container.getContainer(), container, initialResource);
		final IEObjectLocation location = createEObjectLocation(copier, container);

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
		final Resource newResource = createResource("newResource");
		newResource.getContents().addAll(newEObjects);

		EObjectConnector.updateEObjectContainer(container.getContainer(), container, newResource);
		container.setResource(newResource);

		assertEObjectLocation(location, expectedEObject, expectedFeature, expectedValue);
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
				list.add(index, altered[2]);
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
