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
package eu.modelwriter.semantic.jena.tests;

import eu.modelwriter.semantic.jena.JenaBase;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link JenaBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaBaseTests {

	@Test
	public void getNameNullModel() {
		final JenaBase base = new JenaBase(null, "test");

		assertEquals("test", base.getName());
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getConceptsNullModel() {
		final JenaBase base = new JenaBase(null, "test");

		base.getConcepts();
	}

	@Test
	public void getModelNullModel() {
		final JenaBase base = new JenaBase(null, "test");

		assertNull(base.getModel());
	}

	@Test
	public void getName() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource johnSmith = model.createResource("http://somewhere/JohnSmith");
		johnSmith.addProperty(VCARD.FN, "John Smith");
		final JenaBase base = new JenaBase(model, "test");

		assertEquals("test", base.getName());
	}

	@Test
	public void getConcepts() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource johnSmith = model.createResource("http://somewhere/JohnSmith");
		johnSmith.addProperty(VCARD.FN, "John Smith");
		final JenaBase base = new JenaBase(model, "test");

		assertEquals(1, base.getConcepts().size());
	}

	@Test
	public void getModel() {
		final Model model = ModelFactory.createDefaultModel();
		final Resource johnSmith = model.createResource("http://somewhere/JohnSmith");
		johnSmith.addProperty(VCARD.FN, "John Smith");
		final JenaBase base = new JenaBase(model, "test");

		assertEquals(model, base.getModel());
	}

}
