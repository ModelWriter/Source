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
 * Provides semantic informations for a {@link Object concept}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticProvider {

	/**
	 * Gets the handled concept {@link Class type}.
	 * 
	 * @return the handled concept {@link Class type}
	 */
	Class<?> getConceptType();

	/**
	 * Gets the semantic label for a given {@link Object concept}.
	 * 
	 * @param concept
	 *            the {@link Object concept}
	 * @return the semantic label for a given {@link Object concept} if any, <code>null</code> otherwise
	 */
	String getSemanticLabel(Object concept);

	/**
	 * Gets the mapping of {@link Object relation} to the {@link Set} of related concepts.
	 * 
	 * @param concept
	 *            the source concept
	 * @return the mapping of {@link Object relation} to the {@link Set} of related concepts if any,
	 *         <code>null</code> otherwise
	 */
	Map<Object, Set<Object>> getRelatedConcepts(Object concept);

}
