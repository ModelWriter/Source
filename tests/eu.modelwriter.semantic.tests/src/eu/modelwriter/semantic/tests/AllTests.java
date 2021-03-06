/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package eu.modelwriter.semantic.tests;

import eu.modelwriter.semantic.tests.internal.BaseRegistryTests;
import eu.modelwriter.semantic.tests.internal.SemanticAnnotatorTests;
import eu.modelwriter.semantic.tests.internal.SemanticProviderRegistryTests;
import eu.modelwriter.semantic.tests.internal.SemanticSimilarityProviderRegistryTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the eu.modelwriter.semantic plug-in.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = {IdentitySimilarityProviderTests.class, SemanticAnnotatorTests.class,
		StringSemanticProviderTests.class, BaseRegistryTests.class, SemanticUtilsTests.class,
		SemanticProviderRegistryTests.class, SemanticSimilarityProviderRegistryTests.class, })
public class AllTests {

}
