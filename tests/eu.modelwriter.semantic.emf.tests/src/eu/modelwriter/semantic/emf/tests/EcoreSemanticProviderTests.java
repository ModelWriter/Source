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
package eu.modelwriter.semantic.emf.tests;

import eu.modelwriter.semantic.emf.EcoreSemanticProvider;

import java.util.Set;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link EcoreSemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EcoreSemanticProviderTests {

	/**
	 * The {@link EcoreSemanticProvider} to test.
	 */
	private final EcoreSemanticProvider provider = new EcoreSemanticProvider();

	@Test
	public void getConceptType() {
		assertEquals(ENamedElement.class, provider.getConceptType());
	}

	@Test
	public void getSemanticLabelsNull() {
		assertEquals(null, provider.getSemanticLabels(null));
	}

	@Test
	public void getSemanticLabelsNotENamedElement() {
		assertEquals(null, provider.getSemanticLabels(new Object()));
	}

	@Test
	public void getSemanticLabelsENamedElement() {
		final Set<String> semanticLabels = provider.getSemanticLabels(EcorePackage.eINSTANCE.getEClass());

		assertEquals(1, semanticLabels.size());
		assertEquals("EClass", semanticLabels.iterator().next());
	}

}
