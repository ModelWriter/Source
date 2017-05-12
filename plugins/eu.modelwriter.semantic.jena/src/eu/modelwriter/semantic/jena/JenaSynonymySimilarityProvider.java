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
package eu.modelwriter.semantic.jena;

import eu.modelwriter.semantic.ISemanticSimilarityProvider;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/**
 * Identity {@link ISemanticSimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaSynonymySimilarityProvider implements ISemanticSimilarityProvider {

	/**
	 * Identity similarity type.
	 */
	public static final Object TYPE = "Jena Synonymy";

	/**
	 * Default {@link Model}.
	 */
	private final Model model = ModelFactory.createDefaultModel();

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticSimilarityProvider#getName()
	 */
	public String getName() {
		return "Jena Synonymy";
	}

	public Object getType() {
		return TYPE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticSimilarityProvider#getSemanticSimilarities(java.util.Map)
	 */
	public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
		final Map<String, Set<Object>> res = new LinkedHashMap<String, Set<Object>>();

		for (Object concept : labels.keySet()) {
			if (concept instanceof Resource) {
				final Resource resource = (Resource)concept;
				final Set<String> similarities = new LinkedHashSet<String>();
				similarities.addAll(JenaUtils.getLabelsForProperty(resource, model.getProperty(
						"http://www.w3.org/2004/02/skos/core#altLabel")));
				similarities.addAll(JenaUtils.getLabelsForProperty(resource, model.getProperty(
						"http://www.w3.org/2004/02/skos/core#prefLabel")));
				for (String similarity : similarities) {
					Set<Object> similaritySet = res.get(similarity);
					if (similaritySet == null) {
						similaritySet = new LinkedHashSet<Object>();
						res.put(similarity, similaritySet);
					}
					similaritySet.add(concept);
				}
			}
		}

		return res;
	}

}
