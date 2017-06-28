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

import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>CDO Repository Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getURL <em>URL</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getUUID <em>UUID</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getBranchID <em>Branch ID</em>}</li>
 * </ul>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDORepositoryLocation()
 * @model
 * @generated NOT
 */
public interface CDORepositoryLocation extends Location, ICDORepositoryLocation {
	/**
	 * Returns the value of the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>URL</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>URL</em>' attribute.
	 * @see #setURL(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDORepositoryLocation_URL()
	 * @model
	 * @generated
	 */
	String getURL();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getURL
	 * <em>URL</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>URL</em>' attribute.
	 * @see #getURL()
	 * @generated
	 */
	void setURL(String value);

	/**
	 * Returns the value of the '<em><b>UUID</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>UUID</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>UUID</em>' attribute.
	 * @see #setUUID(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDORepositoryLocation_UUID()
	 * @model required="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getUUID
	 * <em>UUID</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDORepositoryLocation_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Branch ID</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Branch ID</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Branch ID</em>' attribute.
	 * @see #setBranchID(int)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDORepositoryLocation_BranchID()
	 * @model required="true"
	 * @generated
	 */
	int getBranchID();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation#getBranchID
	 * <em>Branch ID</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Branch ID</em>' attribute.
	 * @see #getBranchID()
	 * @generated
	 */
	void setBranchID(int value);

} // CDORepositoryLocation
