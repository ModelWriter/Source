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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector.ResourceConnectorTests.TestResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector.TextFileConnectorDelegateTests.TestTextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests {@lin org.eclipse.mylyn.docs.intent.mapping.ide.connector.ResourceLocationListenerk
 * ResourceLocationListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ResourceLocationListenerTests {

	/**
	 * Count calls to {@link TestSetFullPathResourceLocation#setFullPath(String)}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class TestSetFullPathResourceLocation extends TestResourceLocation {

		/**
		 * Calls to {@link TestSetFullPathResourceLocation#setFullPath(String)} counter.
		 */
		private int setFullPathCount;

		@Override
		public void setFullPath(String path) {
			super.setFullPath(path);
			setFullPathCount++;
		}
	}

	/**
	 * Count calls to {@link TestSetFullPathResourceLocation#setFullPath(String)}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class TestSetFullPathTextFileLocation extends TestTextFileLocation {

		/**
		 * Calls to {@link TestSetFullPathResourceLocation#setFullPath(String)} counter.
		 */
		private int setFullPathCount;

		@Override
		public void setFullPath(String path) {
			super.setFullPath(path);
			setFullPathCount++;
		}
	}

	/**
	 * Make sure org.eclipse.mylyn.docs.intent.mapping.ide is activated.
	 */
	private static final Activator ACTIVATOR = org.eclipse.mylyn.docs.intent.mapping.ide.Activator
			.getDefault();

	/**
	 * Creates an {@link IFile} that have no {@link IResourceLocation} in the given {@link IContainer}.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 * @return the created {@link IFile} if any, <code>null</code> otherwise
	 */
	private IFile createFile(IContainer container) {
		IFile res = container.getFile(new Path("test.txt"));

		try {
			res.create(new ByteArrayInputStream("text content".getBytes()), true, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = null;
		}

		return res;
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

	/**
	 * Creates an {@link IProject} and initializes the given {@link IResourceLocation}.
	 * 
	 * @param location
	 *            the {@link IResourceLocation}
	 * @return the created {@link IProject}
	 * @throws CoreException
	 */
	private IProject createProject(IResourceLocation location) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		if (project.exists()) {
			project.delete(true, new NullProgressMonitor());
		}
		location.setFullPath(project.getFullPath().toPortableString());
		project.create(new NullProgressMonitor());
		project.open(new NullProgressMonitor());
		return project;
	}

	/**
	 * Creates an {@link IFile} and initializes the given {@link IResourceLocation} in the given
	 * {@link IContainer}.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 * @param location
	 *            the {@link IResourceLocation}
	 * @return the created {@link IFile} if any, <code>null</code> otherwise
	 */
	private IFile createFile(IContainer container, IResourceLocation location) {
		final String fileName = "test.txt";
		IFile res = container.getFile(new Path(fileName));

		location.setFullPath(res.getFullPath().toPortableString());

		try {
			res.create(new ByteArrayInputStream("text content".getBytes()), true, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = null;
		}

		return res;
	}

	/**
	 * Clears the workspace after each test.
	 */
	@Before
	public void beforeTest() {
		final TestBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);
		IdeMappingUtils.setCurrentBase(base);
		try {
			for (IResource resource : ResourcesPlugin.getWorkspace().getRoot().members()) {
				((IProject)resource).open(new NullProgressMonitor());
				resource.delete(true, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void resourceChangedAddProjectNoLocation() {
		try {
			final IProject project = createProject();

			assertNotNull(project);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedAddProject() {
		try {
			final TestSetFullPathResourceLocation location = new TestSetFullPathResourceLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IProject project = createProject(location);

			assertNotNull(project);
			assertEquals(3, location.setFullPathCount);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedAddFileNoLocation() {
		try {
			final IProject project = createProject();
			final IFile file = createFile(project);

			assertNotNull(file);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedAddFile() {
		try {
			final IProject project = createProject();
			final TestSetFullPathTextFileLocation location = new TestSetFullPathTextFileLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IFile file = createFile(project, location);

			assertNotNull(file);
			assertEquals(2, location.setFullPathCount);
			assertEquals("text content", location.getText());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedRemoveProjectNoLocation() {
		try {
			final IProject project = createProject();

			assertNotNull(project);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedRemoveProject() {
		try {
			final TestSetFullPathResourceLocation location = new TestSetFullPathResourceLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IProject project = createProject(location);

			assertNotNull(project);
			assertEquals(3, location.setFullPathCount);

			project.delete(true, new NullProgressMonitor());

			assertEquals(3, location.setFullPathCount);
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedRemoveFileNoLocation() {
		try {
			final IProject project = createProject();
			final IFile file = createFile(project);

			assertNotNull(file);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedRemoveFile() {
		try {
			final IProject project = createProject();
			final TestSetFullPathTextFileLocation location = new TestSetFullPathTextFileLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IFile file = createFile(project, location);

			assertNotNull(file);
			assertEquals(2, location.setFullPathCount);
			assertEquals("text content", location.getText());

			project.delete(true, new NullProgressMonitor());

			assertEquals(2, location.setFullPathCount);
			assertEquals("text content", location.getText());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedMoveProjectNoLocation() {
		try {
			final IProject project = createProject();
			project.move(new Path("/project"), true, new NullProgressMonitor());
			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedMoveProject() {
		try {
			final TestSetFullPathResourceLocation location = new TestSetFullPathResourceLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IProject project = createProject(location);

			assertNotNull(project);
			assertEquals(3, location.setFullPathCount);

			project.move(new Path("/project"), true, new NullProgressMonitor());

			assertEquals(4, location.setFullPathCount);
			assertEquals("/project", location.getFullPath());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedMoveFileNoLocation() {
		try {
			final IProject project = createProject();
			final IFile file = createFile(project);

			assertNotNull(file);

			file.move(new Path("/test/file.txt"), true, new NullProgressMonitor());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedMoveFile() {
		try {
			final IProject project = createProject();
			final TestSetFullPathTextFileLocation location = new TestSetFullPathTextFileLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IFile file = createFile(project, location);

			assertNotNull(file);
			assertEquals(2, location.setFullPathCount);

			file.move(new Path("/test/file.txt"), true, new NullProgressMonitor());

			assertEquals(3, location.setFullPathCount);

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedOpenProjectNoLocation() {
		try {
			final IProject project = createProject();

			assertNotNull(project);

			project.close(new NullProgressMonitor());

			project.open(new NullProgressMonitor());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedOpenProject() {
		try {
			final TestSetFullPathResourceLocation location = new TestSetFullPathResourceLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IProject project = createProject(location);

			assertNotNull(project);
			assertEquals(3, location.setFullPathCount);

			project.close(new NullProgressMonitor());

			assertEquals(3, location.setFullPathCount);

			project.open(new NullProgressMonitor());

			assertEquals(4, location.setFullPathCount);
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedOpenFileNoLocation() {
		try {
			final IProject project = createProject();
			final IFile file = createFile(project);

			assertNotNull(file);

			project.close(new NullProgressMonitor());

			project.open(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedOpenFile() {
		try {
			final IProject project = createProject();
			final TestSetFullPathTextFileLocation location = new TestSetFullPathTextFileLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IFile file = createFile(project, location);

			assertNotNull(file);
			assertEquals(2, location.setFullPathCount);

			project.close(new NullProgressMonitor());

			assertEquals(2, location.setFullPathCount);

			project.open(new NullProgressMonitor());

			assertEquals(3, location.setFullPathCount);
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedCloseProjectNoLocation() {
		try {
			final IProject project = createProject();

			assertNotNull(project);

			project.close(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedCloseProject() {
		try {
			final TestSetFullPathResourceLocation location = new TestSetFullPathResourceLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IProject project = createProject(location);

			assertNotNull(project);
			assertEquals(3, location.setFullPathCount);

			project.close(new NullProgressMonitor());

			assertEquals(3, location.setFullPathCount);
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedCloseFileNoLocation() {
		try {
			final IProject project = createProject();
			final IFile file = createFile(project);

			assertNotNull(file);

			project.close(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceChangedCloseFile() {
		try {
			final IProject project = createProject();
			final TestSetFullPathTextFileLocation location = new TestSetFullPathTextFileLocation();
			IdeMappingUtils.getCurrentBase().getContents().add(location);
			final IFile file = createFile(project, location);

			assertNotNull(file);
			assertEquals(2, location.setFullPathCount);

			project.close(new NullProgressMonitor());

			assertEquals(2, location.setFullPathCount);
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
