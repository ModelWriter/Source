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
package eu.modelwriter.semantic;

import java.util.Map;
import java.util.Set;

/**
 * Annotates a given text with concepts.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticAnnotator {

	/**
	 * Add the given {@link ISemanticProvider}.
	 * 
	 * @param provider
	 *            the {@link ISemanticProvider} to add.
	 */
	void addSemanticProvider(ISemanticProvider provider);

	/**
	 * Removes the given {@link ISemanticProvider}.
	 * 
	 * @param provider
	 *            the {@link ISemanticProvider} to remove
	 */
	void removeSemanticProvider(ISemanticProvider provider);

	/**
	 * Add the given {@link ISemanticSimilarityProvider}.
	 * 
	 * @param provider
	 *            the {@link ISemanticSimilarityProvider} to add
	 */
	void addSemanticSimilarityProvider(ISemanticSimilarityProvider provider);

	/**
	 * Removes the given {@link ISemanticSimilarityProvider}.
	 * 
	 * @param provider
	 *            the {@link ISemanticSimilarityProvider} to remove
	 */
	void removeSemanticSimilarityProvider(ISemanticSimilarityProvider provider);

	/**
	 * Gets the mapping {@link Object concept} -> {@link Object
	 * ISemanticSimilarityProvider#getSemanticSimilatiry(Object, String) semantic similarity} -> positions.
	 * 
	 * @param text
	 *            the text to annotate
	 * @param concepts
	 *            the {@link Set} of concepts
	 * @return the mapping {@link Object concept} ->
	 *         {@link ISemanticSimilarityProvider#getSemanticSimilarities(Object, String) semantic similarity}
	 *         -> positions
	 */
	// TODO change the return type... its ugly.
	Map<Object, Map<Object, Set<int[]>>> getSemanticAnnotations(String text, Set<Object> concepts);

}
