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
package eu.modelwriter.semantic.jena.tests;

import eu.modelwriter.semantic.jena.JenaUtils;

import org.junit.Test;

/**
 * Tests {@link JenaUtils}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class JenaUtilsTests {

	@Test(expected = java.lang.NullPointerException.class)
	public void getLabelsForPropertyNullNull() {
		JenaUtils.getLabelsForProperty(null, null);
	}

	@Test
	public void getLabelsForProperty() {
		// TODO
	}

}
