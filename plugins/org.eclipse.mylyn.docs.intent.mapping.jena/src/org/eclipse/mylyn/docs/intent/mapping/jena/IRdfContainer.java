/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.jena;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * {@link Resource} container.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IRdfContainer extends ILocation {

	/**
	 * Sets the {@link List} of {@link Resource}.
	 * 
	 * @param resources
	 *            the {@link List} of {@link Resource}
	 */
	void setResources(List<Resource> resources);

	/**
	 * Gets the {@link List} of {@link Resource}.
	 * 
	 * @return the {@link List} of {@link Resource}
	 */
	List<Resource> getResources();

}
