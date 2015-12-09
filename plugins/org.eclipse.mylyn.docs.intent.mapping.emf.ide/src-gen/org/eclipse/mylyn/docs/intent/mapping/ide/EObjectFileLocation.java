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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EObject File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getEObjects <em>EObjects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation()
 * @model
 * @generated NOT
 */
public interface EObjectFileLocation extends TextFileLocation, IEObjectFileLocation {
	/**
	 * Returns the value of the '<em><b>EObjects</b></em>' reference list. The list contents are of type
	 * {@link org.eclipse.emf.ecore.EObject}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EObjects</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>EObjects</em>' reference list.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage#getEObjectFileLocation_EObjects()
	 * @model volatile="true"
	 * @generated NOT
	 */
	List<EObject> getEObjects();

} // EObjectFileLocation
