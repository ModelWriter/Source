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

import eu.modelwriter.semantic.internal.BaseRegistry;
import eu.modelwriter.semantic.internal.SemanticAnnotator;

/**
 * Semantic utility class.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class SemanticUtils {

	/**
	 * The {@link IBaseRegistry}.
	 */
	private static final BaseRegistry BASE_REGISTRY = new BaseRegistry();

	/**
	 * Constructor.
	 */
	private SemanticUtils() {
		// nothing to do here
	}

	/**
	 * Gets a new {@link ISemanticAnnotator}.
	 * 
	 * @return a new {@link ISemanticAnnotator}
	 */
	public static ISemanticAnnotator getSemanticAnnotator() {
		return new SemanticAnnotator();
	}

	/**
	 * Gets the {@link IBaseRegistry}.
	 * 
	 * @return the {@link IBaseRegistry}
	 */
	public static IBaseRegistry getSemanticRegistry() {
		return BASE_REGISTRY;
	}

}
