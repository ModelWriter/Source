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

import eu.modelwriter.semantic.ISemanticProvider;

import java.util.Map;
import java.util.Set;

/**
 * Test implementation of {@link ISemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TestSemanticProvider implements ISemanticProvider {

	public TestSemanticProvider() {
		// nothing to do here
	}

	public String getName() {
		// nothing to do here
		return null;
	}

	public Class<?> getConceptType() {
		// nothing to do here
		return null;
	}

	public Set<String> getSemanticLabels(Object concept) {
		// nothing to do here
		return null;
	}

	public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
		// nothing to do here
		return null;
	}

}
