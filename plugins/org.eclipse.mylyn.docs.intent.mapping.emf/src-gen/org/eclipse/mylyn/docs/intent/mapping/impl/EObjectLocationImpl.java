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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.mapping.EObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EObject Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getURIFragment <em>URI
 * Fragment</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getFeatureName <em>Feature
 * Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EObjectLocationImpl extends LocationImpl implements EObjectLocation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObjectLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.EOBJECT_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getURIFragment() {
		return (String)eGet(MappingPackage.Literals.EOBJECT_LOCATION__URI_FRAGMENT, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setURIFragment(String newURIFragment) {
		eSet(MappingPackage.Literals.EOBJECT_LOCATION__URI_FRAGMENT, newURIFragment);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getFeatureName() {
		return (String)eGet(MappingPackage.Literals.EOBJECT_LOCATION__FEATURE_NAME, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFeatureName(String newFeatureName) {
		eSet(MappingPackage.Literals.EOBJECT_LOCATION__FEATURE_NAME, newFeatureName);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getIndex() {
		return (Integer)eGet(MappingPackage.Literals.EOBJECT_LOCATION__INDEX, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIndex(int newIndex) {
		eSet(MappingPackage.Literals.EOBJECT_LOCATION__INDEX, newIndex);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (URIFragment: ");
		result.append(getURIFragment());
		result.append(", featureName: ");
		result.append(getFeatureName());
		result.append(", index: ");
		result.append(getIndex());
		result.append(')');
		return result.toString();
	}

} // EObjectLocationImpl
