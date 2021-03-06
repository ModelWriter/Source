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

import eu.modelwriter.semantic.IBase;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;

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
	 * The base name.
	 */
	private final String name;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            the {@link Model}
	 * @param name
	 *            the base name
	 */
	public JenaBase(Model model, String name) {
		this.model = model;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getConcepts()
	 */
	public Set<RDFNode> getConcepts() {
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
