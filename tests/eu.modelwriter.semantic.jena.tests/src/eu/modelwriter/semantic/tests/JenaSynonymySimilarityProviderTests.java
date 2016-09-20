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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.modelwriter.semantic.jena.JenaSynonymySimilarityProvider;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link JenaSynonymySimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaSynonymySimilarityProviderTests {

	@Test
	public void getName() {
		final JenaSynonymySimilarityProvider provider = new JenaSynonymySimilarityProvider();

		assertEquals("Jena Synonymy", provider.getName());
	}

	@Test
	public void getType() {
		final JenaSynonymySimilarityProvider provider = new JenaSynonymySimilarityProvider();

		assertEquals("Jena Synonymy", provider.getType());
	}

	@Test
	public void getSemanticSimilaritiesAltLabel() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource resource = model.createResource("/name");
		final Property property = model.getProperty("http://www.w3.org/2004/02/skos/core#altLabel");

		// TODO
	}
}
