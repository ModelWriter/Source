/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * A CDO repository {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ICDORepositoryLocation extends ILocation {

	/**
	 * Gets the CDO repository URL.
	 * 
	 * @return the CDO repository URL
	 */
	String getURL();

	/**
	 * Sets the CDO repository URL.
	 * 
	 * @param url
	 *            the new CDO repository URL
	 */
	void setURL(String url);

	/**
	 * Gets the CDO repository UUID.
	 * 
	 * @return the CDO repository UUID
	 */
	String getUUID();

	/**
	 * Sets the CDO repository UUID.
	 * 
	 * @param uuid
	 *            the new CDO repository UUID
	 */
	void setUUID(String uuid);

	/**
	 * Gets the CDO repository name.
	 * 
	 * @return the CDO repository name
	 */
	String getName();

	/**
	 * Sets the CDO repository name.
	 * 
	 * @param name
	 *            the new CDO repository name
	 */
	void setName(String name);

}
