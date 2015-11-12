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
package eu.modelwriter.semantic.jena;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.modelwriter.semantic.ISemanticProvider;

import java.util.Map;
import java.util.Set;

/**
 * <a href="https://jena.apache.org/">Apache Jena</a> {@link ISemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaSemanticProvider implements ISemanticProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getConceptType()
	 */
	public Class<?> getConceptType() {
		return OntResource.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getSemanticLabel(java.lang.Object)
	 */
	public String getSemanticLabel(Object concept) {
		final String res;

		if (concept instanceof Resource) {
			res = ((Resource)concept).getLocalName();
		} else {
			res = null;
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getRelatedConcepts(java.lang.Object)
	 */
	public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
		// TODO Auto-generated method stub
		return null;
	}

}
