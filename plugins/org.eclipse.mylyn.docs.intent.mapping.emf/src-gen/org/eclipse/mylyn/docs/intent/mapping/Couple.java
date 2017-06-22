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

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Couple</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.Couple#getKey <em>Key</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.Couple#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCouple()
 * @model
 * @extends CDOObject
 * @generated NOT
 */
public interface Couple extends CDOObject, ICouple {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCouple_Key()
	 * @model required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.Couple#getKey <em>Key</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCouple_Value()
	 * @model required="true"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.Couple#getValue <em>Value</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Couple
