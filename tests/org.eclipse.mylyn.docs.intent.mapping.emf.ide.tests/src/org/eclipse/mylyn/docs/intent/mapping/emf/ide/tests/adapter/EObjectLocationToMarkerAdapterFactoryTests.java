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

import org.eclipse.core.resources.IMarker;
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
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.adapter.EObjectLocationToMarkerAdapterFactory;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.marker.IEObjectLocationMaker;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.connector.EObjectFileConnectorDelegateTests.TestEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.tests.connector.EObjectConnectorParametrizedTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link EObjectLocationToMarkerAdapterFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectLocationToMarkerAdapterFactoryTests {

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.emf.ide is activated.
	 */
	private static final EObjectLocationToMarkerAdapterFactory FACTORY = new EObjectLocationToMarkerAdapterFactory();

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

		TestEObjectFileLocation container = new TestEObjectFileLocation();
		container.setEObjects(r.getContents());
		container.setFullPath("/test/test.xmi");

		final IEObjectLocation location = new EObjectConnectorParametrizedTests.TestEObjectLocation();
		location.setEObject(ePackage);
		container.getContents().add(location);
		location.setContainer(container);

		IMarker marker = (IMarker)Platform.getAdapterManager().getAdapter(location, IMarker.class);

		assertNotNull(marker);
		assertEquals(IEObjectLocationMaker.EOBJECT_LOCATION_ID, marker.getType());
		assertEquals("/test/test.xmi", marker.getResource().getFullPath().toString());
		assertEquals("platform:/resource/test/test.xmi#/", marker
				.getAttribute(IEObjectLocationMaker.URI_ATTRIBUTE));

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
