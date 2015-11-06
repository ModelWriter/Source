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
	 * Gets semantic similarities for the given {@link Object concept} and/or
	 * {@link ISemanticProvider#getSemanticLabel(Object) label}.
	 * 
	 * @param concept
	 *            the {@link Object concept}
	 * @param label
	 *            the {@link ISemanticProvider#getSemanticLabel(Object) label}
	 * @return semantic similarities for the given {@link Object concept} and/or
	 *         {@link ISemanticProvider#getSemanticLabel(Object) label} if any, <code>null</code> otherwise
	 */
	Set<String> getSemanticSimilarities(Object concept, String label);

}
