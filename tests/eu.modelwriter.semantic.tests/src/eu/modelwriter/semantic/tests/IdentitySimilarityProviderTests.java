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
package eu.modelwriter.semantic.tests;

import eu.modelwriter.semantic.IdentitySimilarityProvider;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdentitySimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdentitySimilarityProviderTests {

	/**
	 * The {@link IdentitySimilarityProvider} to test.
	 */
	final IdentitySimilarityProvider provider = new IdentitySimilarityProvider();

	@Test
	public void getType() {
		assertEquals(IdentitySimilarityProvider.TYPE, provider.getType());
	}

	@Test
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
		labels.put("this", concepts);

		final Map<String, Set<Object>> semanticSimilarities = provider.getSemanticSimilarities(labels);

		assertTrue(semanticSimilarities == labels);
	}

}
