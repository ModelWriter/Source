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
package eu.modelwriter.semantic.emf.ide.tests.adapter;

import eu.modelwriter.semantic.emf.ide.adapter.EObjectToFileAdapterFactory;

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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests {@link EObjectToFileAdapterFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectToFileAdapterFactoryTests {

	/**
	 * Makes sure eu.modelwriter.semantic.emf.ide is activated.
	 */
	private static final EObjectToFileAdapterFactory FACTORY = new EObjectToFileAdapterFactory();

	@Test
	public void getAdapter() {
		try {
			final IProject project = createProject();
			final ResourceSet rs = new ResourceSetImpl();
			final org.eclipse.emf.ecore.resource.Resource r = rs.createResource(URI
					.createPlatformResourceURI("/test/test.xmi", true));
			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			r.getContents().add(ePackage);
			r.save(null);
			project.refreshLocal(1, new NullProgressMonitor());

			final IFile file = (IFile)Platform.getAdapterManager().getAdapter(ePackage, IFile.class);

			assertNotNull(file);
			assertEquals("/test/test.xmi", file.getFullPath().toString());
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * Creates an {@link IProject}.
	 * 
	 * @return
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
