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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>CDO Resource Node Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.CDOResourceNodeLocation#getPath <em>Path</em>}</li>
 * </ul>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDOResourceNodeLocation()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface CDOResourceNodeLocation extends Location {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getCDOResourceNodeLocation_Path()
	 * @model required="true"
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.CDOResourceNodeLocation#getPath
	 * <em>Path</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

} // CDOResourceNodeLocation
