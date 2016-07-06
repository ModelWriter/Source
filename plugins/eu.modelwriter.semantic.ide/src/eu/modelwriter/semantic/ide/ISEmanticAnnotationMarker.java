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
package eu.modelwriter.semantic.ide;

/**
 * Place holder for semantic annotation marker constants.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISEmanticAnnotationMarker {

	/**
	 * The semantic annotation marker ID.
	 */
	String SEMANTIC_ANNOTATION_ID = Activator.PLUGIN_ID + ".semanticAnnotation";

	/**
	 * The semantic annotation marker ID.
	 */
	String TEXT_SEMANTIC_ANNOTATION_ID = Activator.PLUGIN_ID + ".semanticAnnotation";

	/**
	 * The semantic concept attribute.
	 */
	String SEMANTIC_CONCEPT_ATTRIBUTE = "semanticConcept";

	/**
	 * The semantic similarity attribute.
	 */
	String SEMANTIC_SIMILARITY_ATTRIBUTE = "semanticSimilarity";

}
