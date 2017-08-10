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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.connector;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector.EObjectContainerProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link EObjectContainerProvider}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectContainerProviderTests {

	@Test
	public void getContainerNull() {
		final EObjectContainerProvider provider = new EObjectContainerProvider();

		final Object result = provider.getContainer(null);

		assertNull(result);
	}

	@Test
	public void getContainerObject() {
		final EObjectContainerProvider provider = new EObjectContainerProvider();

		final Object element = new Object();
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainerEObjectNoResource() {
		final EObjectContainerProvider provider = new EObjectContainerProvider();

		final EObject element = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		final Object result = provider.getContainer(element);

		assertNull(result);
	}

	@Test
	public void getContainerEObject() throws IOException, CoreException {
		final EObjectContainerProvider provider = new EObjectContainerProvider();

		final EObject element = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				EObjectContainerProviderTests.class.getCanonicalName());
		project.create(new NullProgressMonitor());
		project.open(new NullProgressMonitor());
		final IFile file = project.getFile(new Path("test.xmi"));

		final Resource resource = new XMIResourceImpl(URI.createPlatformResourceURI(file.getFullPath()
				.toString(), true));
		resource.getContents().add(element);
		resource.save(null);
		final Object result = provider.getContainer(element);

		assertEquals(file, result);
	}

	@Test
	public void isRegistered() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final IContainerProvider provider = MappingUtils.getContainerProviderFactory().createProvider(
				EObjectContainerProvider.class.getCanonicalName());
		assertTrue(provider instanceof EObjectContainerProvider);
	}

}
