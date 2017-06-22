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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.Couple;
import org.eclipse.mylyn.docs.intent.mapping.EObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.MappingFactory;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.TextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class MappingFactoryImpl extends EFactoryImpl implements MappingFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static MappingFactory init() {
		try {
			MappingFactory theMappingFactory = (MappingFactory)EPackage.Registry.INSTANCE.getEFactory(
					MappingPackage.eNS_URI);
			if (theMappingFactory != null) {
				return theMappingFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MappingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MappingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MappingPackage.LINK:
				return (EObject)createLink();
			case MappingPackage.BASE:
				return (EObject)createBase();
			case MappingPackage.TEXT_LOCATION:
				return (EObject)createTextLocation();
			case MappingPackage.EOBJECT_LOCATION:
				return (EObject)createEObjectLocation();
			case MappingPackage.REPORT:
				return (EObject)createReport();
			case MappingPackage.COUPLE:
				return (EObject)createCouple();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName()
						+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case MappingPackage.TYPE:
				return createTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName()
						+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case MappingPackage.TYPE:
				return convertTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName()
						+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Link createLink() {
		LinkImpl link = new LinkImpl();
		return link;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Base createBase() {
		BaseImpl base = new BaseImpl();
		return base;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TextLocation createTextLocation() {
		TextLocationImpl textLocation = new TextLocationImpl();
		return textLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObjectLocation createEObjectLocation() {
		EObjectLocationImpl eObjectLocation = new EObjectLocationImpl();
		return eObjectLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Report createReport() {
		ReportImpl report = new ReportImpl();
		return report;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Couple createCouple() {
		CoupleImpl couple = new CoupleImpl();
		return couple;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Serializable createTypeFromString(EDataType eDataType, String initialValue) {
		return (Serializable)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MappingPackage getMappingPackage() {
		return (MappingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MappingPackage getPackage() {
		return MappingPackage.eINSTANCE;
	}

} // MappingFactoryImpl
