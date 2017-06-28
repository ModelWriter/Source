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

import org.eclipse.mylyn.docs.intent.mapping.EObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation()
 * @model
 * @generated NOT
 */
public interface EObjectFileLocation extends FileLocation, EObjectContainer, IEObjectFileLocation {

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
