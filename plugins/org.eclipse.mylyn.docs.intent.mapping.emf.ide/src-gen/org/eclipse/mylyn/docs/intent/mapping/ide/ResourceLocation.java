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
package org.eclipse.mylyn.docs.intent.mapping.ide;

import org.eclipse.intent.mapping.ide.resource.IResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.Location;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation#getFullPath <em>Full Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getResourceLocation()
 * @model
 * @generated NOT
 */
public interface ResourceLocation extends Location, IResourceLocation {
	/**
	 * Returns the value of the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Full Path</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Full Path</em>' attribute.
	 * @see #setFullPath(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getResourceLocation_FullPath()
	 * @model required="true"
	 * @generated
	 */
	String getFullPath();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation#getFullPath
	 * <em>Full Path</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Full Path</em>' attribute.
	 * @see #getFullPath()
	 * @generated
	 */
	void setFullPath(String value);

} // ResourceLocation
