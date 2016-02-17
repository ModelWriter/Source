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
package eu.modelwriter.semantic.tests;

import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.IdentitySimilarityProvider;
import eu.modelwriter.semantic.StringSemanticProvider;
import eu.modelwriter.semantic.internal.SemanticAnnotator;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SemanticAnnotator}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticAnnotatorTests {

	/**
	 * A Test implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class TestSemanticAnnotator extends SemanticAnnotator {

		public List<ISemanticProvider> getSemanticProviders() {
			return semanticProviders;
		}

		public List<ISemanticSimilarityProvider> getSimilatiryProviders() {
			return similarityProviders;
		}

	}

	/**
	 * Test concept.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class ConceptA {

	}

	/**
	 * Test concept.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class ConceptB extends ConceptA {

	}

	/**
	 * Test concept.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class ConceptC extends ConceptB {

	}

	/**
	 * {@link ISemanticProvider} for {@link ConceptA}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class SemanticProviderA implements ISemanticProvider {

		public Class<?> getConceptType() {
			return ConceptA.class;
		}

		public Set<String> getSemanticLabels(Object concept) {
			final Set<String> res = new LinkedHashSet<String>();

			res.add("A");

			return res;
		}

		public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
			return null;
		}

	}

	/**
	 * {@link ISemanticProvider} for {@link ConceptB}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class SemanticProviderB implements ISemanticProvider {

		public Class<?> getConceptType() {
			return ConceptB.class;
		}

		public Set<String> getSemanticLabels(Object concept) {
			final Set<String> res = new LinkedHashSet<String>();

			res.add("B");

			return res;
		}

		public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
			return null;
		}

	}

	/**
	 * {@link ISemanticProvider} for {@link ConceptC}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class SemanticProviderC implements ISemanticProvider {

		public Class<?> getConceptType() {
			return ConceptC.class;
		}

		public Set<String> getSemanticLabels(Object concept) {
			final Set<String> res = new LinkedHashSet<String>();

			res.add("C");

			return res;
		}

		public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
			return null;
		}

	}

	/**
	 * The {@link TestSemanticAnnotator} to test.
	 */
	private TestSemanticAnnotator annotator;

	@Before
	public void before() {
		annotator = new TestSemanticAnnotator();
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void addSemanticProviderNull() {
		annotator.addSemanticProvider(null);
	}

	@Test
	public void addSemanticProvider() {
		ISemanticProvider provider = new SemanticProviderA();

		annotator.addSemanticProvider(provider);

		assertEquals(1, annotator.getSemanticProviders().size());
		assertEquals(provider, annotator.getSemanticProviders().get(0));
	}

	@Test
	public void addSemanticProviderProviderOrder() {
		ISemanticProvider providerA = new SemanticProviderA();
		ISemanticProvider providerB = new SemanticProviderB();
		ISemanticProvider providerC = new SemanticProviderC();

		annotator.addSemanticProvider(providerB);
		annotator.addSemanticProvider(providerC);
		annotator.addSemanticProvider(providerA);

		assertEquals(3, annotator.getSemanticProviders().size());
		assertEquals(providerC, annotator.getSemanticProviders().get(0));
		assertEquals(providerB, annotator.getSemanticProviders().get(1));
		assertEquals(providerA, annotator.getSemanticProviders().get(2));
	}

	@Test
	public void removeSemanticProviderNull() {
		annotator.removeSemanticProvider(null);

		assertEquals(0, annotator.getSemanticProviders().size());
	}

	@Test
	public void removeSemanticProvider() {
		ISemanticProvider providerA = new SemanticProviderA();
		ISemanticProvider providerB = new SemanticProviderB();
		ISemanticProvider providerC = new SemanticProviderC();

		annotator.addSemanticProvider(providerB);
		annotator.addSemanticProvider(providerC);
		annotator.addSemanticProvider(providerA);

		assertEquals(3, annotator.getSemanticProviders().size());
		assertEquals(providerC, annotator.getSemanticProviders().get(0));
		assertEquals(providerB, annotator.getSemanticProviders().get(1));
		assertEquals(providerA, annotator.getSemanticProviders().get(2));

		annotator.removeSemanticProvider(providerA);

		assertEquals(2, annotator.getSemanticProviders().size());
		assertEquals(providerC, annotator.getSemanticProviders().get(0));
		assertEquals(providerB, annotator.getSemanticProviders().get(1));

		annotator.removeSemanticProvider(providerC);

		assertEquals(1, annotator.getSemanticProviders().size());
		assertEquals(providerB, annotator.getSemanticProviders().get(0));
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void addSemanticSimilarityProviderNull() {
		annotator.addSemanticSimilarityProvider(null);
	}

	@Test
	public void addSemanticSimilarityProvider() {
		ISemanticSimilarityProvider provider = new ISemanticSimilarityProvider() {

			public Object getType() {
				return null;
			}

			public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
				return null;
			}
		};

		annotator.addSemanticSimilarityProvider(provider);

		assertEquals(1, annotator.getSimilatiryProviders().size());
		assertEquals(provider, annotator.getSimilatiryProviders().get(0));
	}

	@Test
	public void removeSemanticSimilarityProviderNull() {
		annotator.removeSemanticSimilarityProvider(null);

		assertEquals(0, annotator.getSimilatiryProviders().size());
	}

	@Test
	public void removeSemanticSimilarityProvider() {
		ISemanticSimilarityProvider provider = new ISemanticSimilarityProvider() {

			public Object getType() {
				return null;
			}

			public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
				return null;
			}
		};

		annotator.addSemanticSimilarityProvider(provider);

		assertEquals(1, annotator.getSimilatiryProviders().size());
		assertEquals(provider, annotator.getSimilatiryProviders().get(0));

		annotator.removeSemanticSimilarityProvider(provider);

		assertEquals(0, annotator.getSimilatiryProviders().size());
	}

	@Test
	public void getSemanticAnnotations() {
		final ISemanticProvider semanticProvider = new StringSemanticProvider();

		annotator.addSemanticProvider(semanticProvider);

		annotator.addSemanticSimilarityProvider(new IdentitySimilarityProvider());

		final Set<Object> concepts = new LinkedHashSet<Object>();
		concepts.add("b");
		concepts.add("e");

		Map<Object, Map<Object, Set<int[]>>> annotations = annotator.getSemanticAnnotations("a b c d e f",
				concepts);

		assertEquals(2, annotations.size());
		Set<int[]> bAnnotations = annotations.get("b").get(IdentitySimilarityProvider.TYPE);

		assertEquals(1, bAnnotations.size());

		Iterator<int[]> it = bAnnotations.iterator();
		int[] bRange = it.next();
		assertEquals(2, bRange[0]);
		assertEquals(3, bRange[1]);

		Set<int[]> eAnnotations = annotations.get("e").get(IdentitySimilarityProvider.TYPE);
		it = eAnnotations.iterator();
		int[] eRange = it.next();
		assertEquals(8, eRange[0]);
		assertEquals(9, eRange[1]);
	}

}
