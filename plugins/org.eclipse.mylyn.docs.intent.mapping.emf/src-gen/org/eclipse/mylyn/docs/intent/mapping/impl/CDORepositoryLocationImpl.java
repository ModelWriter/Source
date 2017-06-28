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
package org.eclipse.mylyn.docs.intent.mapping.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>CDO Repository Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.CDORepositoryLocationImpl#getURL <em>URL</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.CDORepositoryLocationImpl#getUUID <em>UUID</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.CDORepositoryLocationImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CDORepositoryLocationImpl extends LocationImpl implements CDORepositoryLocation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CDORepositoryLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.CDO_REPOSITORY_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getURL() {
		return (String)eGet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__URL, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setURL(String newURL) {
		eSet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__URL, newURL);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getUUID() {
		return (String)eGet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__UUID, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUUID(String newUUID) {
		eSet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__UUID, newUUID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return (String)eGet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__NAME, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		eSet(MappingPackage.Literals.CDO_REPOSITORY_LOCATION__NAME, newName);
	}

} // CDORepositoryLocationImpl
