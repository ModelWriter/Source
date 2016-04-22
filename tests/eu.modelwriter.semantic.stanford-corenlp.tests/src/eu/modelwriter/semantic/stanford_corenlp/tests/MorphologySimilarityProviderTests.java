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
package eu.modelwriter.semantic.stanford_corenlp.tests;

import eu.modelwriter.semantic.stanford_corenlp.MorphologySimilarityProvider;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link MorphologySimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MorphologySimilarityProviderTests {

	/**
	 * The {@link MorphologySimilarityProvider} to test.
	 */
	final MorphologySimilarityProvider provider = new MorphologySimilarityProvider();

	@Test
	public void getType() {
		assertEquals(MorphologySimilarityProvider.TYPE, provider.getType());
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getSemanticSimilaritiesNull() {
		assertNull(provider.getSemanticSimilarities(null));
	}

	@Test
	public void getSemanticSimilaritiesEmpty() {
		assertTrue(provider.getSemanticSimilarities(new LinkedHashMap<String, Set<Object>>()).isEmpty());
	}

	@Test
	public void getSemanticSimilarities() {
		final LinkedHashMap<String, Set<Object>> labels = new LinkedHashMap<String, Set<Object>>();
		Set<Object> concepts = new LinkedHashSet<Object>();
		concepts.add(this);
		labels.put("books", concepts);

		final Map<String, Set<Object>> semanticSimilarities = provider.getSemanticSimilarities(labels);

		assertEquals(1, semanticSimilarities.size());
		final Set<Object> actualConcepts = semanticSimilarities.get("book");
		assertEquals(1, actualConcepts.size());
		assertEquals(concepts, actualConcepts);
	}

}
