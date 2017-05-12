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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.adapter.MarkerToEObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.marker.IEObjectLocationMaker;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.adapter.MarkerToLocationDescriptorAdapterFactory;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link MarkerToEObjectLocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerToEObjectLocationTests {

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.ide is activated.
	 */
	private static final MarkerToLocationDescriptorAdapterFactory FACTORY = new MarkerToLocationDescriptorAdapterFactory();

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.emf.ide is activated.
	 */
	private static final MarkerToEObjectLocationDescriptor ADAPTER = new MarkerToEObjectLocationDescriptor();

	@Test
	@Ignore
	// TODO fix this test
	public void getAdapter() throws CoreException, IOException {
		final IProject project = createProject();
		final ResourceSet rs = new ResourceSetImpl();
		final org.eclipse.emf.ecore.resource.Resource r = rs.createResource(URI.createPlatformResourceURI(
				"/test/test.xmi", true));
		final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
		ePackage.setName("testPackage");
		r.getContents().add(ePackage);
		r.save(null);
		project.refreshLocal(1, new NullProgressMonitor());
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/test/test.xmi"));
		final IMarker marker = file.createMarker(EValidator.MARKER);
		marker.setAttribute(IEObjectLocationMaker.URI_ATTRIBUTE, EcoreUtil.getURI(ePackage).toString());

		final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
		MappingUtils.getMappingRegistry().register(base);
		IdeMappingUtils.setCurrentBase(base);

		ILocationDescriptor locationDescriptor = (ILocationDescriptor)Platform.getAdapterManager().getAdapter(
				marker, ILocationDescriptor.class);

		assertNotNull(locationDescriptor);
		assertTrue(locationDescriptor.getElement() instanceof EPackage);
		assertEquals("testPackage", ((EPackage)locationDescriptor.getElement()).getName());
		assertTrue(locationDescriptor.getContainerDescriptor().getElement() instanceof IFile);
		final IFile container = (IFile)locationDescriptor.getContainerDescriptor().getElement();
		assertEquals("/test/test.xmi", container.getFullPath().toString());

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
