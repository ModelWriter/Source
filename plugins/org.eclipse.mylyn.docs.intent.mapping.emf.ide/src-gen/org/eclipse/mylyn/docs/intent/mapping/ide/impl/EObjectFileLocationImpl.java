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
import org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl#getXMIContent <em>XMI
 * Content</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EObjectFileLocationImpl extends FileLocationImpl implements EObjectFileLocation {

	/**
	 * The default value of the '{@link #getXMIContent() <em>XMI Content</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getXMIContent()
	 * @generated
	 * @ordered
	 */
	protected static final String XMI_CONTENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getXMIContent() <em>XMI Content</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getXMIContent()
	 * @generated
	 * @ordered
	 */
	protected String xmiContent = XMI_CONTENT_EDEFAULT;

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
	 * @generated
	 */
	public String getXMIContent() {
		return xmiContent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setXMIContent(String newXMIContent) {
		String oldXMIContent = xmiContent;
		xmiContent = newXMIContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT, oldXMIContent, xmiContent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT:
				return getXMIContent();
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
			case IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT:
				setXMIContent((String)newValue);
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
			case IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT:
				setXMIContent(XMI_CONTENT_EDEFAULT);
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
			case IdePackage.EOBJECT_FILE_LOCATION__XMI_CONTENT:
				return XMI_CONTENT_EDEFAULT == null ? xmiContent != null
						: !XMI_CONTENT_EDEFAULT.equals(xmiContent);
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
		result.append(" (XMIContent: ");
		result.append(xmiContent);
		result.append(')');
		return result.toString();
	}

} // EObjectFileLocationImpl
