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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * {@link com.hp.hpl.jena.rdf.model.Resource Resource}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IRdfLocation extends ILocation {

	/**
	 * Sets the {@link com.hp.hpl.jena.rdf.model.Resource#getURI() uri}.
	 * 
	 * @param uri
	 *            the {@link com.hp.hpl.jena.rdf.model.Resource#getURI() uri}
	 */
	void setURI(String uri);

	/**
	 * Gets the {@link com.hp.hpl.jena.rdf.model.Resource#getURI() uri}.
	 * 
	 * @return the {@link com.hp.hpl.jena.rdf.model.Resource#getURI() uri}
	 */
	String getURI();

}
