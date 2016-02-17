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

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility class for Jena.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class JenaUtils {

	/**
	 * Constructor.
	 */
	private JenaUtils() {

	}

	/**
	 * Gets the labels for the given {@link Property}.
	 * 
	 * @param resource
	 *            the {@link Resource}
	 * @param property
	 *            the {@link Property}
	 * @return the labels for the given {@link Property}
	 */
	public static Set<String> getLabelsForProperty(Resource resource, Property property) {
		final Set<String> res = new LinkedHashSet<String>();

		final StmtIterator labelsIt = resource.listProperties(property);
		while (labelsIt.hasNext()) {
			final Statement statement = labelsIt.next();
			res.add(statement.getObject().asLiteral().getString());
		}

		return res;
	}

}
