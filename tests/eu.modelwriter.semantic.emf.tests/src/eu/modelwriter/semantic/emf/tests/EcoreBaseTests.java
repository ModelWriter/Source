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

import eu.modelwriter.semantic.emf.EcoreBase;

import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link EcoreBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EcoreBaseTests {

	@Test(expected = java.lang.NullPointerException.class)
	public void getNameNullEPackage() {
		final EcoreBase base = new EcoreBase(null);

		base.getName();
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getConceptsNullEPackage() {
		final EcoreBase base = new EcoreBase(null);

		base.getConcepts();
	}

	@Test
	public void getEPackageNullEPackage() {
		final EcoreBase base = new EcoreBase(null);

		assertNull(base.getEPackage());
	}

	@Test
	public void getName() {
		final EcoreBase base = new EcoreBase(EcorePackage.eINSTANCE);

		assertEquals("http://www.eclipse.org/emf/2002/Ecore", base.getName());
	}

	@Test
	public void getConcepts() {
		final EcoreBase base = new EcoreBase(EcorePackage.eINSTANCE);

		assertTrue(base.getConcepts().size() > 200);
	}

	@Test
	public void getEPackage() {
		final EcoreBase base = new EcoreBase(EcorePackage.eINSTANCE);

		assertEquals(EcorePackage.eINSTANCE, base.getEPackage());
	}

}
