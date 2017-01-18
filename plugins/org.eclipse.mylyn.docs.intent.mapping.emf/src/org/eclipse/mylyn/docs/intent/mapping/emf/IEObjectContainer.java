/*******************************************************************************
 * Copyright (c) 2015 Obeo.
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
 * {@link org.eclipse.emf.ecore.resource.Resource Resource} container.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectContainer extends ILocation {

	/**
	 * Gets the XMI contents.
	 * 
	 * @return the XMI contents
	 */
	String getXMIContent();

	/**
	 * Sets the XMI contents.
	 * 
	 * @param xmiContent
	 *            the XMI contents
	 */
	void setXMIContent(String xmiContent);

}
