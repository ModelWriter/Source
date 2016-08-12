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
package eu.modelwriter.semantic.jena;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import eu.modelwriter.semantic.IBase;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Jena implementation of {@link IBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaBase implements IBase {

	/**
	 * The {@link Model}.
	 */
	private final Model model;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            the {@link Model}
	 */
	public JenaBase(Model model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getName()
	 */
	public String getName() {
		// TODO double check that
		return model.getProperty("baseURI").toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getConcepts()
	 */
	public Set<?> getConcepts() {
		Set<RDFNode> concepts = new LinkedHashSet<RDFNode>();

		final NodeIterator it = model.listObjects();
		while (it.hasNext()) {
			concepts.add(it.next());

		}

		return concepts;
	}

	/**
	 * Gets the {@link Model}.
	 * 
	 * @return the {@link Model}
	 */
	public Model getModel() {
		return model;
	}

}
