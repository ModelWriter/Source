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
package eu.modelwriter.semantic;

import java.util.Map;
import java.util.Set;

/**
 * Identity {@link ISemanticSimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdentitySimilarityProvider implements ISemanticSimilarityProvider {

	/**
	 * Identity similarity type.
	 */
	public static final Object TYPE = new Object();

	public Object getType() {
		return TYPE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticSimilarityProvider#getSemanticSimilarities(java.util.Map)
	 */
	public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
		return labels;
	}

}
