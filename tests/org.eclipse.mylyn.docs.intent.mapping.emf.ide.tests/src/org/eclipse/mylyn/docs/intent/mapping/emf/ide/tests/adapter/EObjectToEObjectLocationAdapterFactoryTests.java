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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.adapter;

import java.io.IOException;

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
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.adapter.EObjectToEObjectLocationAdapterFactory;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link EObjectToEObjectLocationAdapterFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectToEObjectLocationAdapterFactoryTests {

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.emf.ide is activated.
	 */
	private static final EObjectToEObjectLocationAdapterFactory FACTORY = new EObjectToEObjectLocationAdapterFactory();

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

		IEObjectLocation location = (IEObjectLocation)Platform.getAdapterManager().getAdapter(ePackage,
				ILocation.class);

		assertNotNull(location);
		assertEquals(ePackage, location.getEObject());
		assertTrue(location.getContainer() instanceof IEObjectFileLocation);
		final IEObjectFileLocation container = (IEObjectFileLocation)location.getContainer();
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
