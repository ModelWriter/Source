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
import org.eclipse.mylyn.docs.intent.mapping.EObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EObject Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getURIFragment <em>URI Fragment
 * </em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getFeatureName <em>Feature Name
 * </em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.EObjectLocationImpl#getIndex <em>Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EObjectLocationImpl extends LocationImpl implements EObjectLocation {
	/**
	 * The default value of the '{@link #getURIFragment() <em>URI Fragment</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getURIFragment()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_FRAGMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getURIFragment() <em>URI Fragment</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getURIFragment()
	 * @generated
	 * @ordered
	 */
	protected String uriFragment = URI_FRAGMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected String featureName = FEATURE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
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
		return uriFragment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setURIFragment(String newURIFragment) {
		String oldURIFragment = uriFragment;
		uriFragment = newURIFragment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MappingPackage.EOBJECT_LOCATION__URI_FRAGMENT, oldURIFragment, uriFragment));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFeatureName(String newFeatureName) {
		String oldFeatureName = featureName;
		featureName = newFeatureName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MappingPackage.EOBJECT_LOCATION__FEATURE_NAME, oldFeatureName, featureName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIndex(int newIndex) {
		int oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.EOBJECT_LOCATION__INDEX,
					oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.EOBJECT_LOCATION__URI_FRAGMENT:
				return getURIFragment();
			case MappingPackage.EOBJECT_LOCATION__FEATURE_NAME:
				return getFeatureName();
			case MappingPackage.EOBJECT_LOCATION__INDEX:
				return getIndex();
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
			case MappingPackage.EOBJECT_LOCATION__URI_FRAGMENT:
				setURIFragment((String)newValue);
				return;
			case MappingPackage.EOBJECT_LOCATION__FEATURE_NAME:
				setFeatureName((String)newValue);
				return;
			case MappingPackage.EOBJECT_LOCATION__INDEX:
				setIndex((Integer)newValue);
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
			case MappingPackage.EOBJECT_LOCATION__URI_FRAGMENT:
				setURIFragment(URI_FRAGMENT_EDEFAULT);
				return;
			case MappingPackage.EOBJECT_LOCATION__FEATURE_NAME:
				setFeatureName(FEATURE_NAME_EDEFAULT);
				return;
			case MappingPackage.EOBJECT_LOCATION__INDEX:
				setIndex(INDEX_EDEFAULT);
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
			case MappingPackage.EOBJECT_LOCATION__URI_FRAGMENT:
				return URI_FRAGMENT_EDEFAULT == null ? uriFragment != null : !URI_FRAGMENT_EDEFAULT
						.equals(uriFragment);
			case MappingPackage.EOBJECT_LOCATION__FEATURE_NAME:
				return FEATURE_NAME_EDEFAULT == null ? featureName != null : !FEATURE_NAME_EDEFAULT
						.equals(featureName);
			case MappingPackage.EOBJECT_LOCATION__INDEX:
				return index != INDEX_EDEFAULT;
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
		result.append(" (URIFragment: ");
		result.append(uriFragment);
		result.append(", featureName: ");
		result.append(featureName);
		result.append(", index: ");
		result.append(index);
		result.append(')');
		return result.toString();
	}

} // EObjectLocationImpl
