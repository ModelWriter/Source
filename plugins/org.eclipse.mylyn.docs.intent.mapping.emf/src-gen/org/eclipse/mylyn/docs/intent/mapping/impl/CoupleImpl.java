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
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.eclipse.mylyn.docs.intent.mapping.Couple;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Couple</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.CoupleImpl#getKey <em>Key</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.CoupleImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CoupleImpl extends CDOObjectImpl implements Couple {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CoupleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.COUPLE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getKey() {
		return (String)eGet(MappingPackage.Literals.COUPLE__KEY, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setKey(String newKey) {
		eSet(MappingPackage.Literals.COUPLE__KEY, newKey);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getValue() {
		return (String)eGet(MappingPackage.Literals.COUPLE__VALUE, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValue(String newValue) {
		eSet(MappingPackage.Literals.COUPLE__VALUE, newValue);
	}

} // CoupleImpl
