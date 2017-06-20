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
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.TextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Text Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.TextLocationImpl#getStartOffset <em>Start
 * Offset</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.TextLocationImpl#getEndOffset <em>End
 * Offset</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TextLocationImpl extends LocationImpl implements TextLocation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
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
		return (Integer)eGet(MappingPackage.Literals.TEXT_LOCATION__START_OFFSET, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStartOffset(int newStartOffset) {
		eSet(MappingPackage.Literals.TEXT_LOCATION__START_OFFSET, newStartOffset);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getEndOffset() {
		return (Integer)eGet(MappingPackage.Literals.TEXT_LOCATION__END_OFFSET, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEndOffset(int newEndOffset) {
		eSet(MappingPackage.Literals.TEXT_LOCATION__END_OFFSET, newEndOffset);
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
		result.append(" (startOffset: ");
		result.append(getStartOffset());
		result.append(", endOffset: ");
		result.append(getEndOffset());
		result.append(')');
		return result.toString();
	}

} // TextLocationImpl
