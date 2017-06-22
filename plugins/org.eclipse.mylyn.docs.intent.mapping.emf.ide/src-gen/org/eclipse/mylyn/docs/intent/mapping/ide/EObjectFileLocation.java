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

import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getXMIContent <em>XMI
 * Content</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getSavedURIFragments <em>Saved URI
 * Fragments</em>}</li>
 * </ul>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation()
 * @model
 * @generated NOT
 */
public interface EObjectFileLocation extends FileLocation, IEObjectFileLocation {

	/**
	 * Returns the value of the '<em><b>XMI Content</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMI Content</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XMI Content</em>' attribute.
	 * @see #setXMIContent(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation_XMIContent()
	 * @model required="true"
	 * @generated
	 */
	String getXMIContent();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getXMIContent <em>XMI
	 * Content</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>XMI Content</em>' attribute.
	 * @see #getXMIContent()
	 * @generated
	 */
	void setXMIContent(String value);

	/**
	 * Returns the value of the '<em><b>Saved URI Fragments</b></em>' containment reference list. The list
	 * contents are of type {@link org.eclipse.mylyn.docs.intent.mapping.Couple}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Saved URI Fragments</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Saved URI Fragments</em>' containment reference list.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation_SavedURIFragments()
	 * @model containment="true"
	 * @generated NOT
	 */
	List<ICouple> getSavedURIFragments();
} // EObjectFileLocation
