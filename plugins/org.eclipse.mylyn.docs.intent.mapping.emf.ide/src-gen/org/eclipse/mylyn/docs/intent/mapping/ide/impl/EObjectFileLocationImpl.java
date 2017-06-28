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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.mapping.EObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl#getXMIContent <em>XMI
 * Content</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl#getSavedURIFragments
 * <em>Saved URI Fragments</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EObjectFileLocationImpl extends FileLocationImpl implements EObjectFileLocation {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObjectFileLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IdePackage.Literals.EOBJECT_FILE_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getXMIContent() {
		return (String)eGet(MappingPackage.Literals.EOBJECT_CONTAINER__XMI_CONTENT, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setXMIContent(String newXMIContent) {
		eSet(MappingPackage.Literals.EOBJECT_CONTAINER__XMI_CONTENT, newXMIContent);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public List<ICouple> getSavedURIFragments() {
		return (List<ICouple>)eGet(MappingPackage.Literals.EOBJECT_CONTAINER__SAVED_URI_FRAGMENTS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == EObjectContainer.class) {
			switch (derivedFeatureID) {
				case IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT:
					return MappingPackage.EOBJECT_CONTAINER__XMI_CONTENT;
				case IdePackage.EOBJECT_FILE_LOCATION__SAVED_URI_FRAGMENTS:
					return MappingPackage.EOBJECT_CONTAINER__SAVED_URI_FRAGMENTS;
				default:
					return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == EObjectContainer.class) {
			switch (baseFeatureID) {
				case MappingPackage.EOBJECT_CONTAINER__XMI_CONTENT:
					return IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT;
				case MappingPackage.EOBJECT_CONTAINER__SAVED_URI_FRAGMENTS:
					return IdePackage.EOBJECT_FILE_LOCATION__SAVED_URI_FRAGMENTS;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} // EObjectFileLocationImpl
