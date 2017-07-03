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
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.TextContainer;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;
import org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Text File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl#getText <em>Text</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TextFileLocationImpl extends FileLocationImpl implements TextFileLocation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TextFileLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IdePackage.Literals.TEXT_FILE_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText() {
		return (String)eGet(MappingPackage.Literals.TEXT_CONTAINER__TEXT, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setText(String newText) {
		eSet(MappingPackage.Literals.TEXT_CONTAINER__TEXT, newText);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == TextContainer.class) {
			switch (derivedFeatureID) {
				case IdePackage.TEXT_FILE_LOCATION__TEXT:
					return MappingPackage.TEXT_CONTAINER__TEXT;
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
		if (baseClass == TextContainer.class) {
			switch (baseFeatureID) {
				case MappingPackage.TEXT_CONTAINER__TEXT:
					return IdePackage.TEXT_FILE_LOCATION__TEXT;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} // TextFileLocationImpl
