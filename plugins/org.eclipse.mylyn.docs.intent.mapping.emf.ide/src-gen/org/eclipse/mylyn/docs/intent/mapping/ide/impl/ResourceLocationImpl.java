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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;
import org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Resource Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl#getFullPath <em>Full Path
 * </em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceLocationImpl extends LocationImpl implements ResourceLocation {
	/**
	 * The default value of the '{@link #getFullPath() <em>Full Path</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getFullPath()
	 * @generated
	 * @ordered
	 */
	protected static final String FULL_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFullPath() <em>Full Path</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getFullPath()
	 * @generated
	 * @ordered
	 */
	protected String fullPath = FULL_PATH_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ResourceLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IdePackage.Literals.RESOURCE_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFullPath(String newFullPath) {
		String oldFullPath = fullPath;
		fullPath = newFullPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IdePackage.RESOURCE_LOCATION__FULL_PATH,
					oldFullPath, fullPath));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IdePackage.RESOURCE_LOCATION__FULL_PATH:
				return getFullPath();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IdePackage.RESOURCE_LOCATION__FULL_PATH:
				setFullPath((String)newValue);
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
			case IdePackage.RESOURCE_LOCATION__FULL_PATH:
				setFullPath(FULL_PATH_EDEFAULT);
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
			case IdePackage.RESOURCE_LOCATION__FULL_PATH:
				return FULL_PATH_EDEFAULT == null ? fullPath != null : !FULL_PATH_EDEFAULT.equals(fullPath);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fullPath: ");
		result.append(fullPath);
		result.append(')');
		return result.toString();
	}

} // ResourceLocationImpl
