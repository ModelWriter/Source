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

import eu.modelwriter.semantic.StringSemanticProvider;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StringSemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class StringSemanticProviderTests {

	/**
	 * The {@link TestSemanticAnnotator} to test.
	 */
	private StringSemanticProvider provider = new StringSemanticProvider();

	@Test
	public void getType() {
		assertEquals(String.class, provider.getConceptType());
	}

	@Test
	public void getSemanticLabelNull() {
		assertEquals(null, provider.getSemanticLabels(null));
	}

	@Test
	public void getSemanticLabelNotString() {
		assertEquals(null, provider.getSemanticLabels(new Object()));
	}

	@Test
	public void getSemanticLabel() {
		final Set<String> expected = new LinkedHashSet<String>();
		expected.add("SomeString");

		assertEquals(expected, provider.getSemanticLabels("SomeString"));
	}

	@Test
	public void getRelatedConceptsNull() {
		assertEquals(null, provider.getRelatedConcepts(null));
	}

	@Test
	public void getRelatedConceptsNotString() {
		assertEquals(null, provider.getRelatedConcepts(new Object()));
	}

	@Test
	public void getRelatedConcepts() {
		assertEquals(null, provider.getRelatedConcepts("SomeString"));
	}

}
