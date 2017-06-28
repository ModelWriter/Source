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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage
 * @generated
 */
public interface IdeFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	IdeFactory eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Resource Location</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Resource Location</em>'.
	 * @generated
	 */
	ResourceLocation createResourceLocation();

	/**
	 * Returns a new object of class '<em>File Location</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>File Location</em>'.
	 * @generated
	 */
	FileLocation createFileLocation();

	/**
	 * Returns a new object of class '<em>Text File Location</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Text File Location</em>'.
	 * @generated
	 */
	TextFileLocation createTextFileLocation();

	/**
	 * Returns a new object of class '<em>EObject File Location</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>EObject File Location</em>'.
	 * @generated
	 */
	EObjectFileLocation createEObjectFileLocation();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	IdePackage getIdePackage();

} // IdeFactory
