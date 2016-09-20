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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.modelwriter.semantic.jena.JenaSemanticProvider;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link JenaSemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaSemanticProviderTests {

	/**
	 * The {@link JenaSemanticProvider} to test.
	 */
	private final JenaSemanticProvider provider = new JenaSemanticProvider();

	@Test
	public void getConceptType() {
		assertEquals(Resource.class, provider.getConceptType());
	}

	@Test
	public void getSemanticLabelsNull() {
		assertEquals(null, provider.getSemanticLabels(null));
	}

	@Test
	public void getSemanticLabelsNotResource() {
		assertEquals(null, provider.getSemanticLabels(new Object()));
	}

	@Test
	public void getSemanticLabelsResourceNoLabel() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource resource = model.createResource();

		final Set<String> semanticLabels = provider.getSemanticLabels(resource);

		assertEquals(null, semanticLabels);
	}

	@Test
	public void getSemanticLabelsResourceWithLocalName() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource resource = model.createResource("/name");
		resource.addProperty(RDFS.label, "something");

		final Set<String> semanticLabels = provider.getSemanticLabels(resource);

		assertEquals(2, semanticLabels.size());
		final Iterator<String> it = semanticLabels.iterator();
		assertEquals("something", it.next());
		assertEquals("name", it.next());
	}

	@Test
	public void getSemanticLabelsResource() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource resource = model.createResource();
		resource.addProperty(RDFS.label, "something");

		final Set<String> semanticLabels = provider.getSemanticLabels(resource);

		assertEquals(1, semanticLabels.size());
		final Iterator<String> it = semanticLabels.iterator();
		assertEquals("something", it.next());
	}

}
