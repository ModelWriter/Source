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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl#getEObjects <em>EObjects
 * </em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EObjectFileLocationImpl extends TextFileLocationImpl implements EObjectFileLocation {

	/**
	 * The {@link List} of {@link EObject}.
	 */
	private List<EObject> eObjects = new ArrayList<EObject>();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
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
	 * @generated NOT
	 */
	public List<EObject> getEObjects() {
		return eObjects;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer#setEObjects(java.util.List)
	 * @generated NOT
	 */
	public void setEObjects(List<EObject> eObjects) {
		this.eObjects = eObjects;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IdePackage.EOBJECT_FILE_LOCATION__EOBJECTS:
				return getEObjects();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IdePackage.EOBJECT_FILE_LOCATION__EOBJECTS:
				getEObjects().clear();
				getEObjects().addAll((Collection<? extends EObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case IdePackage.EOBJECT_FILE_LOCATION__EOBJECTS:
				getEObjects().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case IdePackage.EOBJECT_FILE_LOCATION__EOBJECTS:
				return !getEObjects().isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // EObjectFileLocationImpl
