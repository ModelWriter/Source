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

import org.eclipse.mylyn.docs.intent.mapping.base.IBaseElement;

/**
 * A key and a value.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ICouple extends IBaseElement {

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	String getKey();

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the new key
	 */
	void setKey(String key);

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	String getValue();

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	void setValue(String value);

}
