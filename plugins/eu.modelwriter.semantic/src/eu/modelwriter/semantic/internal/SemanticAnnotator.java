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
package eu.modelwriter.semantic.internal;

import eu.modelwriter.semantic.ISemanticAnnotator;
import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Default implementation of {@link ISemanticAnnotator}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticAnnotator implements ISemanticAnnotator {

	/**
	 * The {@link List} of {@link ISemanticProvider}.
	 */
	protected final List<ISemanticProvider> semanticProviders = new ArrayList<ISemanticProvider>();

	/**
	 * The {@link List} of {@link ISemanticSimilarityProvider}.
	 */
	protected final List<ISemanticSimilarityProvider> similarityProviders = new ArrayList<ISemanticSimilarityProvider>();

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#addSemanticProvider(eu.modelwriter.semantic.ISemanticProvider)
	 */
	public void addSemanticProvider(ISemanticProvider provider) {
		if (provider != null) {
			int index = 0;
			boolean added = false;
			for (ISemanticProvider semanticProvider : semanticProviders) {
				if (semanticProvider.getConceptType().isAssignableFrom(provider.getConceptType())) {
					semanticProviders.add(index, provider);
					added = true;
					break;
				} else {
					index++;
				}
			}
			if (!added) {
				semanticProviders.add(index, provider);
			}
		} else {
			throw new IllegalArgumentException("ISemanticProvider can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#removeSemanticProvider(eu.modelwriter.semantic.ISemanticProvider)
	 */
	public void removeSemanticProvider(ISemanticProvider provider) {
		if (provider != null) {
			semanticProviders.remove(provider);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#addSemanticSimilarityProvider(eu.modelwriter.semantic.ISemanticSimilarityProvider)
	 */
	public void addSemanticSimilarityProvider(ISemanticSimilarityProvider provider) {
		if (provider != null) {
			similarityProviders.add(provider);
		} else {
			throw new IllegalArgumentException("ISemanticSimilarityProvider can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#removeSemanticSimilarityProvider(eu.modelwriter.semantic.ISemanticSimilarityProvider)
	 */
	public void removeSemanticSimilarityProvider(ISemanticSimilarityProvider provider) {
		if (provider != null) {
			similarityProviders.remove(provider);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#getSemanticAnnotations(java.lang.String, java.util.Set)
	 */
	public Map<Object, Map<Object, Set<int[]>>> getSemanticAnnotations(String text, Set<Object> concepts) {
		// concept to label mapping.
		final Map<Object, String> labels = new HashMap<Object, String>();
		for (Object concept : concepts) {
			for (ISemanticProvider provider : semanticProviders) {
				final String semanticLabel = provider.getSemanticLabel(concept);
				if (semanticLabel != null) {
					labels.put(concept, semanticLabel);
					break;
				}
			}
		}

		// concept -> similarity type -> similarities
		final Map<Object, Map<Object, Set<String>>> similarities = new HashMap<Object, Map<Object, Set<String>>>();
		for (Entry<Object, String> entry : labels.entrySet()) {
			for (ISemanticSimilarityProvider provider : similarityProviders) {
				Map<Object, Set<String>> map = similarities.get(entry.getKey());
				if (map == null) {
					map = new HashMap<Object, Set<String>>();
					similarities.put(entry.getKey(), map);
				}
				final Set<String> semanticSimilatiries = provider.getSemanticSimilarities(entry.getKey(),
						entry.getValue());
				if (semanticSimilatiries != null) {
					if (map.get(provider.getType()) == null) {
						map.put(provider.getType(), semanticSimilatiries);
					} else {
						map.get(provider.getType()).addAll(semanticSimilatiries);
					}
				}
			}
		}

		final Map<Object, Map<Object, Set<int[]>>> res = new HashMap<Object, Map<Object, Set<int[]>>>();
		// TODO populate the result

		return res;
	}
}
