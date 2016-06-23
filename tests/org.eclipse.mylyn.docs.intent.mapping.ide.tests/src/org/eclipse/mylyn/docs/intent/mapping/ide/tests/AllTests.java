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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests;

import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.FileDelegateRegistryTests;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.IdeTextConnectorTests;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.ResourceConnectorTests;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.TextFileConnectorDelegateTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the org.eclipse.mylyn.docs.intent.mapping.ide plug-in.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = {FileDelegateRegistryTests.class, ResourceConnectorTests.class,
		TextFileConnectorDelegateTests.class, IdeTextConnectorTests.class, })
public class AllTests {

}
