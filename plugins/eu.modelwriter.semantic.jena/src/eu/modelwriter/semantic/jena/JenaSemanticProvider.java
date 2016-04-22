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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.modelwriter.semantic.ISemanticProvider;

import java.util.LinkedHashSet;
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
		return Resource.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getSemanticLabels(java.lang.Object)
	 */
	public Set<String> getSemanticLabels(Object concept) {
		final Set<String> res;

		if (concept instanceof Resource) {
			Set<String> labels = new LinkedHashSet<String>();
			final Resource resource = (Resource)concept;

			labels.addAll(JenaUtils.getLabelsForProperty(resource, RDFS.label));
			if (resource.getLocalName() != null) {
				labels.add(resource.getLocalName());
			}

			if (labels.isEmpty()) {
				res = null;
			} else {
				res = labels;
			}
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
