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
package eu.modelwriter.semantic.ide.tests;

import eu.modelwriter.semantic.ISemanticSimilarityProvider;

import java.util.Map;
import java.util.Set;

/**
 * Test implementation of {@link ISemanticSimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TestSemanticSimilarityProvider implements ISemanticSimilarityProvider {

	public TestSemanticSimilarityProvider() {
		// nothing to do here
	}

	public String getName() {
		// nothing to do here
		return null;
	}

	public Object getType() {
		// nothing to do here
		return null;
	}

	public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
		// nothing to do here
		return null;
	}

}
