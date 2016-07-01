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
package org.eclipse.mylyn.docs.intent.mapping.emf.tests;

import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFBaseTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFLinkTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.EMFReportTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the org.eclipse.mylyn.docs.intent.mapping.emf plug-in.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = {EMFBaseTests.class, EMFLinkTests.class, EMFTextLocationTests.class,
		EMFEObjectLocationTests.class, EObjectConnectorTests.class, EObjectConnectorParametrizedTests.class,
		EMFReportTests.class, })
public class AllTests {

}
