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
import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.SemanticUtils;
import eu.modelwriter.semantic.ide.Activator;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link eu.modelwriter.semantic.ide.IdeSemanticRegistryListener IdeSemanticRegistryListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeSemanticRegistryListenerTests {

	/**
	 * Makes sure eu.modelwriter.semantic.ide is activated.
	 */
	private static final Activator ACTIVATOR = Activator.getDefault();

	@Test
	public void semanticProviderIsRegistred() {
		boolean found = false;

		for (ISemanticProvider provider : SemanticUtils.getSemanticProviderRegistry().getProviders()) {
			if (provider instanceof TestSemanticProvider) {
				found = true;
				break;
			}
		}

		assertTrue(found);
	}

	@Test
	public void semanticSimilarityProviderIsRegistred() {
		boolean found = false;

		for (ISemanticSimilarityProvider provider : SemanticUtils.getSemanticSimilarityProviderRegistry()
				.getProviders()) {
			if (provider instanceof TestSemanticSimilarityProvider) {
				found = true;
				break;
			}
		}

		assertTrue(found);
	}

}
