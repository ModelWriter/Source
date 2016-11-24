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

import eu.modelwriter.semantic.IBaseRegistry;
import eu.modelwriter.semantic.ISemanticAnnotator;
import eu.modelwriter.semantic.ISemanticProviderRegistry;
import eu.modelwriter.semantic.ISemanticSimilarityProviderRegistry;
import eu.modelwriter.semantic.SemanticUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link SemanticUtils}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticUtilsTests {

	@Test
	public void getSemanticAnnotatorNotNull() {
		final ISemanticAnnotator annotator = SemanticUtils.getSemanticAnnotator();

		assertNotNull(annotator);
	}

	@Test
	public void getSemanticAnnotatorDifferentInstances() {
		final ISemanticAnnotator annotator1 = SemanticUtils.getSemanticAnnotator();
		final ISemanticAnnotator annotator2 = SemanticUtils.getSemanticAnnotator();

		assertTrue(annotator1 != annotator2);
	}

	@Test
	public void getSemanticBaseRegistryNotNull() {
		final IBaseRegistry registry = SemanticUtils.getSemanticBaseRegistry();

		assertNotNull(registry);
	}

	@Test
	public void getSemanticBaseRegistrySameInstances() {
		final IBaseRegistry registry1 = SemanticUtils.getSemanticBaseRegistry();
		final IBaseRegistry registry2 = SemanticUtils.getSemanticBaseRegistry();

		assertEquals(registry1, registry2);
	}

	@Test
	public void getSemanticProviderRegistryNotNull() {
		final ISemanticProviderRegistry registry = SemanticUtils.getSemanticProviderRegistry();

		assertNotNull(registry);
	}

	@Test
	public void getSemanticProviderRegistrySameInstances() {
		final ISemanticProviderRegistry registry1 = SemanticUtils.getSemanticProviderRegistry();
		final ISemanticProviderRegistry registry2 = SemanticUtils.getSemanticProviderRegistry();

		assertEquals(registry1, registry2);
	}

	@Test
	public void getSemanticSimilarityProviderRegistryNotNull() {
		final ISemanticSimilarityProviderRegistry registry = SemanticUtils
				.getSemanticSimilarityProviderRegistry();

		assertNotNull(registry);
	}

	@Test
	public void getSemanticSimilarityProviderRegistrySameInstances() {
		final ISemanticSimilarityProviderRegistry registry1 = SemanticUtils
				.getSemanticSimilarityProviderRegistry();
		final ISemanticSimilarityProviderRegistry registry2 = SemanticUtils
				.getSemanticSimilarityProviderRegistry();

		assertEquals(registry1, registry2);
	}

}
