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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.TextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Text Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.TextLocationImpl#getStartOffset <em>Start Offset
 * </em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.TextLocationImpl#getEndOffset <em>End
 * Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextLocationImpl extends LocationImpl implements TextLocation {
	/**
	 * The default value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected static final int START_OFFSET_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected int startOffset = START_OFFSET_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected static final int END_OFFSET_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected int endOffset = END_OFFSET_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public TextLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.TEXT_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStartOffset(int newStartOffset) {
		int oldStartOffset = startOffset;
		startOffset = newStartOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.TEXT_LOCATION__START_OFFSET,
					oldStartOffset, startOffset));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEndOffset(int newEndOffset) {
		int oldEndOffset = endOffset;
		endOffset = newEndOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.TEXT_LOCATION__END_OFFSET,
					oldEndOffset, endOffset));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.TEXT_LOCATION__START_OFFSET:
				return getStartOffset();
			case MappingPackage.TEXT_LOCATION__END_OFFSET:
				return getEndOffset();
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
			case MappingPackage.TEXT_LOCATION__START_OFFSET:
				setStartOffset((Integer)newValue);
				return;
			case MappingPackage.TEXT_LOCATION__END_OFFSET:
				setEndOffset((Integer)newValue);
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
			case MappingPackage.TEXT_LOCATION__START_OFFSET:
				setStartOffset(START_OFFSET_EDEFAULT);
				return;
			case MappingPackage.TEXT_LOCATION__END_OFFSET:
				setEndOffset(END_OFFSET_EDEFAULT);
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
			case MappingPackage.TEXT_LOCATION__START_OFFSET:
				return startOffset != START_OFFSET_EDEFAULT;
			case MappingPackage.TEXT_LOCATION__END_OFFSET:
				return endOffset != END_OFFSET_EDEFAULT;
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
		result.append(" (startOffset: ");
		result.append(startOffset);
		result.append(", endOffset: ");
		result.append(endOffset);
		result.append(')');
		return result.toString();
	}

} // TextLocationImpl
