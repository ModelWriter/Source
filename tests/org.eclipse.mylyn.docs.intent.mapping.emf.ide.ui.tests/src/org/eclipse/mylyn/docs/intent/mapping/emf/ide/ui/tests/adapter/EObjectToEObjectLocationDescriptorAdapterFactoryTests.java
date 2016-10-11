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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui.tests.adapter;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link EObjectToEObjectLocationAdapterFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectToEObjectLocationDescriptorAdapterFactoryTests {

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui is activated.
	 */
	private static final org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui.Activator ACTIVATOR = org.eclipse.mylyn.docs.intent.mapping.emf.ide.ui.Activator
			.getDefault();

	@Test
	public void getAdapter() throws CoreException, IOException {
		final IProject project = createProject();
		final ResourceSet rs = new ResourceSetImpl();
		final org.eclipse.emf.ecore.resource.Resource r = rs.createResource(URI.createPlatformResourceURI(
				"/test/test.xmi", true));
		final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
		r.getContents().add(ePackage);
		r.save(null);
		project.refreshLocal(1, new NullProgressMonitor());

		final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		MappingUtils.getMappingRegistry().register(base);
		IdeMappingUtils.setCurrentBase(base);

		ILocationDescriptor locationDescriptor = (ILocationDescriptor)Platform.getAdapterManager()
				.getAdapter(ePackage, ILocationDescriptor.class);

		assertNotNull(locationDescriptor);
		assertEquals(ePackage, locationDescriptor.getElement());
		assertTrue(locationDescriptor.getContainerDescriptor().getElement() instanceof IFile);
		assertNull(locationDescriptor.getContainerDescriptor().getContainerDescriptor());
		final IFile container = (IFile)locationDescriptor.getContainerDescriptor().getElement();
		assertEquals("/test/test.xmi", container.getFullPath());

		project.delete(true, true, new NullProgressMonitor());
	}

	/**
	 * Creates an {@link IProject}.
	 * 
	 * @return the created {@link IProject}
	 * @throws CoreException
	 */
	private IProject createProject() throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		if (project.exists()) {
			project.delete(true, new NullProgressMonitor());
		}
		project.create(new NullProgressMonitor());
		project.open(new NullProgressMonitor());
		return project;
	}

}
