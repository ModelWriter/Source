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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests.connector;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector.EObjectFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector.ResourceConnectorTests.TestResourceLocation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link EObjectFileConnectorDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectFileConnectorDelegateTests {

	/**
	 * Test {@link IEObjectFileLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestEObjectFileLocation extends TestResourceLocation implements IEObjectFileLocation {

		/**
		 * The XMI content.
		 */
		private String xmiContent;

		/**
		 * The {@link Resource}.
		 */
		private Resource resource;

		public void setResource(Resource resource) {
			this.resource = resource;
		}

		public Resource getResrouce() {
			return resource;
		}

		public String getXMIContent() {
			return xmiContent;
		}

		public void setXMIContent(String content) {
			this.xmiContent = content;
		}

	}

	/**
	 * The {@link EObjectFileConnectorDelegate} to test.
	 */
	private final EObjectFileConnectorDelegate delegate = new EObjectFileConnectorDelegate();

	@BeforeClass
	public static void beforeClass() throws CoreException, IOException {
		final NullProgressMonitor monitor = new NullProgressMonitor();

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		project.create(monitor);
		project.open(monitor);

		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		folder.create(true, true, monitor);

		final Resource resource = new XMIResourceImpl(URI.createPlatformResourceURI(
				"TestProject/TestFolder/TestFile.xmi", true));
		resource.getContents().add(EcorePackage.eINSTANCE.getEcoreFactory().createEClass());
		resource.getContents().add(EcorePackage.eINSTANCE.getEcoreFactory().createEClass());
		resource.getContents().add(EcorePackage.eINSTANCE.getEcoreFactory().createEClass());
		resource.save(null);
	}

	@AfterClass
	public static void afterClass() throws CoreException {
		final NullProgressMonitor monitor = new NullProgressMonitor();

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		project.delete(true, monitor);

		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		folder.delete(true, monitor);

		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.xmi"));
		file.delete(true, monitor);
	}

	@Test
	public void getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		final IContentType expected = contentTypeManager.getContentType("org.eclipse.emf.ecore.xmi");

		assertEquals(expected, delegate.getContentType());
	}

	@Test
	public void getFileLocationType() {
		assertEquals(IEObjectFileLocation.class, delegate.getFileLocationType());
	}

	@Test
	public void initLocation() {
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.xmi"));
		TestEObjectFileLocation location = new TestEObjectFileLocation();

		delegate.initLocation(location, file);

		assertEquals(180, location.getXMIContent().length());
	}

	@Test
	public void registred() {
		boolean registred = false;
		for (IFileConnectorDelegate d : IdeMappingUtils.getFileConectorDelegateRegistry()
				.getConnectorDelegates()) {
			if (d instanceof EObjectFileConnectorDelegate) {
				registred = true;
				break;
			}
		}
		assertTrue(registred);
	}

}
