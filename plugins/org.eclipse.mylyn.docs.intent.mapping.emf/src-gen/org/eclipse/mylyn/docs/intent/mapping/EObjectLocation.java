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
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getURIFragment <em>URI Fragment</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getFeatureName <em>Feature Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getIndex <em>Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation()
 * @model
 * @generated NOT
 */
public interface EObjectLocation extends Location, IEObjectLocation {
	/**
	 * Returns the value of the '<em><b>URI Fragment</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>URI Fragment</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>URI Fragment</em>' attribute.
	 * @see #setURIFragment(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation_URIFragment()
	 * @model required="true"
	 * @generated
	 */
	String getURIFragment();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getURIFragment
	 * <em>URI Fragment</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>URI Fragment</em>' attribute.
	 * @see #getURIFragment()
	 * @generated
	 */
	void setURIFragment(String value);

	/**
	 * Returns the value of the '<em><b>Feature Name</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Setting</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Feature Name</em>' attribute.
	 * @see #setFeatureName(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation_FeatureName()
	 * @model
	 * @generated
	 */
	String getFeatureName();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getFeatureName
	 * <em>Feature Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Feature Name</em>' attribute.
	 * @see #getFeatureName()
	 * @generated
	 */
	void setFeatureName(String value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute. The default value is <code>"0"</code>. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(int)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getEObjectLocation_Index()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.EObjectLocation#getIndex
	 * <em>Index</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

} // EObjectLocation
