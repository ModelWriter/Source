/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf.tests.base;

import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOBinaryResourceLocation;

/**
 * Test {@link ICDOBinaryResourceLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractCDOBinaryResourceLocationTests extends AbstractCDOFileLocationTests {

	@Override
	protected ICDOBinaryResourceLocation createLocation() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return getBase().getFactory().createElement(ICDOBinaryResourceLocation.class);
	}

}
