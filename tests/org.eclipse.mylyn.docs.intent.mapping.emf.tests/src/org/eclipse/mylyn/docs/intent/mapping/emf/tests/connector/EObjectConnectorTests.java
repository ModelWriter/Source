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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectContainerConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests.TestEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.ITestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestReport;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextLocation;
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
	public void getLocationTypeEObject() {
		final Class<? extends ILocation> type = getLocationType(TestEObjectContainerLocation.class,
				EcorePackage.eINSTANCE);

		assertEquals(IEObjectLocation.class, type);
	}

	@Test
	public void getLocationTypeESetting() {
		final Class<? extends ILocation> type = getLocationType(TestEObjectContainerLocation.class,
				((InternalEObject)EcorePackage.eINSTANCE.getEClass()).eSetting(EcorePackage.eINSTANCE
						.getENamedElement_Name()));

		assertEquals(IEObjectLocation.class, type);
	}

	@Test
	public void initLocationEObject() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);

		final TestEObjectContainerConnector testEObjectContainerConnector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(testEObjectContainerConnector);
		final EObjectConnector eObjectConnector = new EObjectConnector();
		MappingUtils.getConnectorRegistry().register(eObjectConnector);
		super.initLocation(container, location, EcorePackage.eINSTANCE.getEClass());

		assertEquals(6860, location.getStartOffset());
		assertEquals(22773, location.getEndOffset());
		final Object element = eObjectConnector.getElement(location);
		assertTrue(element instanceof EObject);
		assertEquals(EcorePackage.eINSTANCE.getEClass(), element);
		assertNull(location.getFeatureName());

		MappingUtils.getConnectorRegistry().unregister(testEObjectContainerConnector);
		MappingUtils.getConnectorRegistry().unregister(eObjectConnector);
	}

	@Test
	public void initLocationSetting() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);

		final TestEObjectContainerConnector testEObjectContainerConnector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(testEObjectContainerConnector);
		final EObjectConnector eObjectConnector = new EObjectConnector();
		MappingUtils.getConnectorRegistry().register(eObjectConnector);
		super.initLocation(container, location, ((InternalEObject)EcorePackage.eINSTANCE.getEClass())
				.eSetting(EcorePackage.eINSTANCE.getENamedElement_Name()));

		assertEquals("name:EClass", container.getText().substring(location.getStartOffset(),
				location.getEndOffset()));
		assertEquals(6866, location.getStartOffset());
		final Object element = eObjectConnector.getElement(location);
		assertTrue(element instanceof Setting);
		assertEquals(EcorePackage.eINSTANCE.getEClass(), ((Setting)element).getEObject());
		assertEquals(EcorePackage.eINSTANCE.getENamedElement_Name(), ((Setting)element)
				.getEStructuralFeature());
		assertEquals("EClass", ((Setting)element).get(true));
		assertEquals(EcorePackage.eINSTANCE.getENamedElement_Name().getName(), location.getFeatureName());

		MappingUtils.getConnectorRegistry().unregister(testEObjectContainerConnector);
		MappingUtils.getConnectorRegistry().unregister(eObjectConnector);
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

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedStartLonger() {
		final String text = "yyyyyyyyyyyyyyyyyyyyname:xxxx:\n";
		final int startOffset = 20;
		final int endOffset = 29;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedStartSameLength() {
		final String text = "yname:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureShorter() {
		final String text = "::\n";
		final int startOffset = 1;
		final int endOffset = 1;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureLonger() {
		final String text = ":yyyyyyyyyyyyyyyyyyyy:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 26;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedFeatureSameLenght() {
		final String text = ":yyyyyxxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndShorter() {
		final String text = ":name:xxxx";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndLonger() {
		final String text = ":name:xxxxyyyyyyyyyyyyyyyyyyyy";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSettingMismatchedEndSameLenght() {
		final String text = ":name:xxxxyy";
		final int startOffset = 1;
		final int endOffset = 10;

		assertFalse(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void isValidOffsetsSetting() {
		final String text = ":name:xxxx:\n";
		final int startOffset = 1;
		final int endOffset = 10;

		assertTrue(isValidOffsets(text, EcorePackage.eINSTANCE.getENamedElement_Name().getName(),
				startOffset, endOffset));
	}

	@Test
	public void getLocationEObject() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);
		container.getContents().add(location);

		final TestEObjectContainerConnector connector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(connector);
		super.initLocation(container, location, EcorePackage.eINSTANCE.getEClass());

		assertEquals(location, super.getLocation(container, EcorePackage.eINSTANCE.getEClass()));
		assertEquals(null, super.getLocation(container, ((InternalEObject)EcorePackage.eINSTANCE.getEClass())
				.eSetting(EcorePackage.eINSTANCE.getENamedElement_Name())));

		MappingUtils.getConnectorRegistry().unregister(connector);
	}

	@Test
	public void getLocationSetting() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);
		container.getContents().add(location);

		final TestEObjectContainerConnector connector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(connector);
		super.initLocation(container, location, ((InternalEObject)EcorePackage.eINSTANCE.getEClass())
				.eSetting(EcorePackage.eINSTANCE.getENamedElement_Name()));

		assertEquals(null, super.getLocation(container, EcorePackage.eINSTANCE.getEClass()));
		assertEquals(location, super.getLocation(container, ((InternalEObject)EcorePackage.eINSTANCE
				.getEClass()).eSetting(EcorePackage.eINSTANCE.getENamedElement_Name())));

		MappingUtils.getConnectorRegistry().unregister(connector);
	}

	@Test
	public void updateEObjectContainerDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IReport.class,
				new BaseElementFactory.FactoryDescriptor<TestReport>(TestReport.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);
		container.getContents().add(location);
		final ILocation target = new TestTextLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);
		link.setSource(location);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		final TestEObjectContainerConnector connector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(connector);

		super.initLocation(container, location, EcorePackage.eINSTANCE.getEClass());

		eObjects.remove(EcorePackage.eINSTANCE.getEClass());
		super.updateEObjectContainer(container, eObjects);

		assertTrue(location.isMarkedAsDeleted());
		assertEquals(1, base.getReports().size());
		final IReport report = base.getReports().get(0);
		assertEquals(link, report.getLink());
		assertTrue(report.getDescription().contains("org.eclipse.emf.ecore.EClass"));
		assertTrue(report.getDescription().contains("at (6860, 22773) has been deleted."));

		MappingUtils.getConnectorRegistry().unregister(connector);
	}

	@Test
	public void updateEObjectContainerChanged() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IReport.class,
				new BaseElementFactory.FactoryDescriptor<TestReport>(TestReport.class));
		final TestEObjectContainerLocation container = new TestEObjectContainerLocation();
		container.setContainer(base);
		final List<EObject> eObjects = new ArrayList<EObject>(EcorePackage.eINSTANCE.eContents());
		final Resource resource = new XMIResourceImpl();
		resource.getContents().addAll(eObjects);
		container.setResource(resource);
		updateEObjectContainer(container, resource);
		final IEObjectLocation location = new TestEObjectLocation();
		location.setContainer(container);
		container.getContents().add(location);
		final ILocation target = new TestTextLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);
		link.setSource(location);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		final TestEObjectContainerConnector connector = new TestEObjectContainerConnector();
		MappingUtils.getConnectorRegistry().register(connector);

		super.initLocation(container, location, EcorePackage.eINSTANCE.getEClass());

		EcorePackage.eINSTANCE.getEClass().setName("NewName");
		super.updateEObjectContainer(container, eObjects);

		assertEquals(1, base.getReports().size());
		final IReport report = base.getReports().get(0);
		assertEquals(link, report.getLink());
		assertTrue(report.getDescription().contains("org.eclipse.emf.ecore.EClass"));
		assertTrue(report.getDescription().contains(" at (6860, 22774) has been changed to "));
		assertTrue(report.getDescription().contains(" at (6860, 22774)."));

		EcorePackage.eINSTANCE.getEClass().setName("EClass");
		MappingUtils.getConnectorRegistry().unregister(connector);
	}

}
