/**
 *  Copyright (c) 2015 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Obeo - initial API and implementation and/or initial documentation
 *     ...
 * 
 */
package org.eclipse.mylyn.docs.intent.mapping.impl;

import java.io.Serializable;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.EObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingFactory;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.TextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class MappingPackageImpl extends EPackageImpl implements MappingPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass locationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass linkEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass baseEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass iemfBaseElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass textLocationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass eObjectLocationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass locationContainerEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass reportEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType typeEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MappingPackageImpl() {
		super(eNS_URI, MappingFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it
	 * depends.
	 * <p>
	 * This method is used to initialize {@link MappingPackage#eINSTANCE} when that field is accessed. Clients
	 * should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MappingPackage init() {
		if (isInited)
			return (MappingPackage)EPackage.Registry.INSTANCE.getEPackage(MappingPackage.eNS_URI);

		// Obtain or create and register package
		MappingPackageImpl theMappingPackage = (MappingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MappingPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI)
				: new MappingPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMappingPackage.createPackageContents();

		// Initialize created meta-data
		theMappingPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMappingPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MappingPackage.eNS_URI, theMappingPackage);
		return theMappingPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLocation() {
		return locationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLocation_SourceLinks() {
		return (EReference)locationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLocation_TargetLinks() {
		return (EReference)locationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLocation_Container() {
		return (EReference)locationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLocation_Type() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLink() {
		return linkEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLink_Description() {
		return (EAttribute)linkEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLink_Source() {
		return (EReference)linkEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLink_Target() {
		return (EReference)linkEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLink_Type() {
		return (EAttribute)linkEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLink_Reports() {
		return (EReference)linkEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBase() {
		return baseEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBase_Name() {
		return (EAttribute)baseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBase_Reports() {
		return (EReference)baseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getIEMFBaseElement() {
		return iemfBaseElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTextLocation() {
		return textLocationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTextLocation_StartOffset() {
		return (EAttribute)textLocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTextLocation_EndOffset() {
		return (EAttribute)textLocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getEObjectLocation() {
		return eObjectLocationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEObjectLocation_Setting() {
		return (EAttribute)eObjectLocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLocationContainer() {
		return locationContainerEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLocationContainer_Contents() {
		return (EReference)locationContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getReport() {
		return reportEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getReport_Description() {
		return (EAttribute)reportEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getReport_Link() {
		return (EReference)reportEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getType() {
		return typeEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MappingFactory getMappingFactory() {
		return (MappingFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any
	 * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		locationEClass = createEClass(LOCATION);
		createEReference(locationEClass, LOCATION__SOURCE_LINKS);
		createEReference(locationEClass, LOCATION__TARGET_LINKS);
		createEReference(locationEClass, LOCATION__CONTAINER);
		createEAttribute(locationEClass, LOCATION__TYPE);

		linkEClass = createEClass(LINK);
		createEAttribute(linkEClass, LINK__DESCRIPTION);
		createEReference(linkEClass, LINK__SOURCE);
		createEReference(linkEClass, LINK__TARGET);
		createEAttribute(linkEClass, LINK__TYPE);
		createEReference(linkEClass, LINK__REPORTS);

		baseEClass = createEClass(BASE);
		createEAttribute(baseEClass, BASE__NAME);
		createEReference(baseEClass, BASE__REPORTS);

		iemfBaseElementEClass = createEClass(IEMF_BASE_ELEMENT);

		textLocationEClass = createEClass(TEXT_LOCATION);
		createEAttribute(textLocationEClass, TEXT_LOCATION__START_OFFSET);
		createEAttribute(textLocationEClass, TEXT_LOCATION__END_OFFSET);

		eObjectLocationEClass = createEClass(EOBJECT_LOCATION);
		createEAttribute(eObjectLocationEClass, EOBJECT_LOCATION__SETTING);

		locationContainerEClass = createEClass(LOCATION_CONTAINER);
		createEReference(locationContainerEClass, LOCATION_CONTAINER__CONTENTS);

		reportEClass = createEClass(REPORT);
		createEAttribute(reportEClass, REPORT__DESCRIPTION);
		createEReference(reportEClass, REPORT__LINK);

		// Create data types
		typeEDataType = createEDataType(TYPE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect
	 * on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		locationEClass.getESuperTypes().add(this.getIEMFBaseElement());
		locationEClass.getESuperTypes().add(this.getLocationContainer());
		linkEClass.getESuperTypes().add(this.getIEMFBaseElement());
		baseEClass.getESuperTypes().add(this.getIEMFBaseElement());
		baseEClass.getESuperTypes().add(this.getLocationContainer());
		textLocationEClass.getESuperTypes().add(this.getLocation());
		eObjectLocationEClass.getESuperTypes().add(this.getTextLocation());
		reportEClass.getESuperTypes().add(this.getIEMFBaseElement());

		// Initialize classes, features, and operations; add parameters
		initEClass(locationEClass, Location.class, "Location", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLocation_SourceLinks(), this.getLink(), this.getLink_Target(), "sourceLinks", null,
				0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLocation_TargetLinks(), this.getLink(), this.getLink_Source(), "targetLinks", null,
				0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLocation_Container(), this.getLocationContainer(), this
				.getLocationContainer_Contents(), "container", null, 0, 1, Location.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocation_Type(), this.getType(), "type", null, 0, 1, Location.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkEClass, Link.class, "Link", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLink_Description(), ecorePackage.getEString(), "description", null, 1, 1,
				Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getLink_Source(), this.getLocation(), this.getLocation_TargetLinks(), "source", null,
				1, 1, Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLink_Target(), this.getLocation(), this.getLocation_SourceLinks(), "target", null,
				1, 1, Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLink_Type(), this.getType(), "type", null, 0, 1, Link.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLink_Reports(), this.getReport(), this.getReport_Link(), "reports", null, 0, -1,
				Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseEClass, Base.class, "Base", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBase_Name(), ecorePackage.getEString(), "name", null, 1, 1, Base.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getBase_Reports(), this.getReport(), null, "reports", null, 0, -1, Base.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iemfBaseElementEClass, IEMFBaseElement.class, "IEMFBaseElement", IS_ABSTRACT,
				IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(textLocationEClass, TextLocation.class, "TextLocation", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTextLocation_StartOffset(), ecorePackage.getEInt(), "startOffset", "-1", 1, 1,
				TextLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTextLocation_EndOffset(), ecorePackage.getEInt(), "endOffset", "-1", 1, 1,
				TextLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eObjectLocationEClass, EObjectLocation.class, "EObjectLocation", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEObjectLocation_Setting(), ecorePackage.getEBoolean(), "setting", null, 1, 1,
				EObjectLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(locationContainerEClass, LocationContainer.class, "LocationContainer", IS_ABSTRACT,
				IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLocationContainer_Contents(), this.getLocation(), this.getLocation_Container(),
				"contents", null, 0, -1, LocationContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(reportEClass, Report.class, "Report", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReport_Description(), ecorePackage.getEString(), "description", null, 1, 1,
				Report.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getReport_Link(), this.getLink(), this.getLink_Reports(), "link", null, 1, 1,
				Report.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(typeEDataType, Serializable.class, "Type", IS_SERIALIZABLE,
				!IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} // MappingPackageImpl
