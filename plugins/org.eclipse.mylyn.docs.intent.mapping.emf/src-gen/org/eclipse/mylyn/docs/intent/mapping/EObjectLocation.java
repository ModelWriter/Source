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
package org.eclipse.mylyn.docs.intent.mapping;

import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EObject Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#isSetting <em>Setting</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation()
 * @model
 * @generated NOT
 */
public interface EObjectLocation extends TextLocation, IEObjectLocation {
	/**
	 * Returns the value of the '<em><b>Setting</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Setting</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Setting</em>' attribute.
	 * @see #setSetting(boolean)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation_Setting()
	 * @model required="true"
	 * @generated
	 */
	boolean isSetting();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#isSetting
	 * <em>Setting</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Setting</em>' attribute.
	 * @see #isSetting()
	 * @generated
	 */
	void setSetting(boolean value);

} // EObjectLocation
