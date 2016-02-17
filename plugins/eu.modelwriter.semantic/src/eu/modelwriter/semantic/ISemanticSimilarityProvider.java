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

import java.util.Map;
import java.util.Set;

/**
 * Provides similarity semantic.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticSimilarityProvider {

	/**
	 * Gets the provided type of {@link Object similarity}.
	 * 
	 * @return the provided type of {@link Object similarity}
	 */
	Object getType();

	/**
	 * Gets semantic similarities for the given mapping of a
	 * {@link ISemanticProvider#getSemanticLabels(Object) label} to its {@link Object concepts}.
	 * 
	 * @param labels
	 *            the mapping of a {@link ISemanticProvider#getSemanticLabels(Object) label} to its
	 *            {@link Object concepts}
	 * @return the mapping of a similarity to its {@link Object concepts} if any, <code>null</code> otherwise
	 */
	Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels);

}
