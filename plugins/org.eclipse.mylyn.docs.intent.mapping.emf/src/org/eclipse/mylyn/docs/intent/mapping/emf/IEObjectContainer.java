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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;

/**
 * {@link EObject} container.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectContainer extends ITextContainer {

	/**
	 * Sets the {@link List} of contained {@link EObject}.
	 * 
	 * @param eObjects
	 *            the {@link List} of contained {@link EObject}
	 */
	void setEObjects(List<EObject> eObjects);

	/**
	 * Gets the {@link List} of contained {@link EObject}.
	 * 
	 * @return the {@link List} of contained {@link EObject}
	 */
	List<EObject> getEObjects();

}
