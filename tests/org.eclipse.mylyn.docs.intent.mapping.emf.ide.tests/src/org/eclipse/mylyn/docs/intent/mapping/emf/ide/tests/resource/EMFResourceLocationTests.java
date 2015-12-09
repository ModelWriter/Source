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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.resource;

import org.eclipse.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.intent.mapping.ide.resource.IResourceLocation;
import org.eclipse.intent.mapping.ide.resource.ITextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.base.Factory;
import org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.resource.AbstractResourceLocationTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.IBaseFactory;

/**
 * Tests {@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation ResourceLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EMFResourceLocationTests extends AbstractResourceLocationTests {

	/**
	 * The {@link IBaseFactory}.
	 */
	private IBaseFactory factory;

	public EMFResourceLocationTests() {
		factory = new Factory() {

			@Override
			public Base createBase() {
				final Base res = super.createBase();

				res.getFactory().addClassInstance(IResourceLocation.class, ResourceLocationImpl.class);
				res.getFactory().addClassInstance(IFileLocation.class, FileLocationImpl.class);
				res.getFactory().addClassInstance(ITextFileLocation.class, TextFileLocationImpl.class);
				res.getFactory().addClassInstance(IEObjectFileLocation.class, EObjectFileLocationImpl.class);

				return res;
			}

		};
	}

	@Override
	protected IBaseFactory getFactory() {
		return factory;
	}

}
