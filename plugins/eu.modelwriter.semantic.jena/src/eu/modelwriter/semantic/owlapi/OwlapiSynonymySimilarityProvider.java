/*******************************************************************************
 * Copyright (c) 2016 {INITIAL COPYRIGHT OWNER} {OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {INITIAL AUTHOR} - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package eu.modelwriter.semantic.owlapi;

import java.util.Map;
import java.util.Set;

import eu.modelwriter.semantic.ISemanticSimilarityProvider;

// Look examples of JenaSynonymySimilarityProvider and 

//Implement this class which loads a morphology similarity
// Each EntrySet(similarity) will be name of the (synonym+morphology+equivalence) followed by the
// concept objects that are linked by THIS object.

public class OwlapiSynonymySimilarityProvider implements ISemanticSimilarityProvider {

	public Object getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
		// TODO Auto-generated method stub
		return null;
	}

}
