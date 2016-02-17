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
package eu.modelwriter.semantic;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link ISemanticProvider} for {@link String} concepts.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class StringSemanticProvider implements ISemanticProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getConceptType()
	 */
	public Class<?> getConceptType() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getSemanticLabels(java.lang.Object)
	 */
	public Set<String> getSemanticLabels(Object concept) {
		final Set<String> res;

		if (concept instanceof String) {
			res = new LinkedHashSet<String>();
			res.add((String)concept);
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getRelatedConcepts(java.lang.Object)
	 */
	public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
		return null;
	}

}
