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
package org.eclipse.mylyn.docs.intent.mapping.ide.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeFactory;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;
import org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class IdeFactoryImpl extends EFactoryImpl implements IdeFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static IdeFactory init() {
		try {
			IdeFactory theIdeFactory = (IdeFactory)EPackage.Registry.INSTANCE.getEFactory(IdePackage.eNS_URI);
			if (theIdeFactory != null) {
				return theIdeFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new IdeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IdeFactoryImpl() {
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
			case IdePackage.RESOURCE_LOCATION:
				return createResourceLocation();
			case IdePackage.FILE_LOCATION:
				return createFileLocation();
			case IdePackage.TEXT_FILE_LOCATION:
				return createTextFileLocation();
			case IdePackage.EOBJECT_FILE_LOCATION:
				return createEObjectFileLocation();
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
	public ResourceLocation createResourceLocation() {
		ResourceLocationImpl resourceLocation = new ResourceLocationImpl();
		return resourceLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FileLocation createFileLocation() {
		FileLocationImpl fileLocation = new FileLocationImpl();
		return fileLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TextFileLocation createTextFileLocation() {
		TextFileLocationImpl textFileLocation = new TextFileLocationImpl();
		return textFileLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObjectFileLocation createEObjectFileLocation() {
		EObjectFileLocationImpl eObjectFileLocation = new EObjectFileLocationImpl();
		return eObjectFileLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IdePackage getIdePackage() {
		return (IdePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IdePackage getPackage() {
		return IdePackage.eINSTANCE;
	}

} // IdeFactoryImpl
