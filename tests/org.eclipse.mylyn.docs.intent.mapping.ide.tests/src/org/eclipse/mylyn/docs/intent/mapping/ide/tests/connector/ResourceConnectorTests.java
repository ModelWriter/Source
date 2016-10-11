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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.ResourceConnector;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.FileDelegateRegistryTests;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.FileDelegateRegistryTests.FileConnectorDelegateA.FileLocationA;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.FileDelegateRegistryTests.FileConnectorDelegateB.FileLocationB;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.FileDelegateRegistryTests.FileConnectorDelegateC.FileLocationC;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ResourceConnector} class.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ResourceConnectorTests {

	/**
	 * Test {@link ResourceConnector}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class TestResourceConnector extends ResourceConnector {
		@Override
		public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
				Object element) {
			return super.getLocationType(containerType, element);
		}

		@Override
		public void initLocation(ILocation location, Object element) {
			super.initLocation(location, element);
		}

	}

	/**
	 * Test {@link IFileLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestResourceLocation extends TestLocation implements IFileLocation {

		/**
		 * The full path.
		 */
		private String fullPath;

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String path) {
			fullPath = path;
		}

	}

	/**
	 * The {@link ResourceConnector} to test.
	 */
	private final TestResourceConnector connector = new TestResourceConnector();

	@BeforeClass
	public static void beforeClass() throws CoreException {
		final NullProgressMonitor monitor = new NullProgressMonitor();

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		project.create(monitor);
		project.open(monitor);

		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		folder.create(true, true, monitor);

		final IFile fileA = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.aaa"));
		fileA.create(new ByteArrayInputStream(new byte[0]), true, monitor);

		final IFile fileB = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.bbb"));
		fileB.create(new ByteArrayInputStream(new byte[0]), true, monitor);

		final IFile fileC = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.ccc"));
		fileC.create(new ByteArrayInputStream(new byte[0]), true, monitor);
	}

	@AfterClass
	public static void afterClass() throws CoreException {
		final NullProgressMonitor monitor = new NullProgressMonitor();

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		project.delete(true, monitor);

		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		folder.delete(true, monitor);

		final IFile fileA = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.aaa"));
		fileA.delete(true, monitor);

		final IFile fileB = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.bbb"));
		fileB.delete(true, monitor);

		final IFile fileC = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.ccc"));
		fileC.delete(true, monitor);
	}

	@Before
	public void before() {
		IdeMappingUtils.getFileConectorDelegateRegistry().register(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_A);
		IdeMappingUtils.getFileConectorDelegateRegistry().register(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_B);
		IdeMappingUtils.getFileConectorDelegateRegistry().register(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_C);
	}

	@After
	public void after() {
		IdeMappingUtils.getFileConectorDelegateRegistry().unregister(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_A);
		IdeMappingUtils.getFileConectorDelegateRegistry().unregister(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_B);
		IdeMappingUtils.getFileConectorDelegateRegistry().unregister(
				FileDelegateRegistryTests.FILE_CONNECTOR_DELEGATE_C);
	}

	@Test
	public void getLocationTypeNull() {
		final Class<? extends ILocation> locationType = connector.getLocationType(null, null);

		assertEquals(null, locationType);
	}

	@Test
	public void getLocationTypeIProject() {
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");

		final Class<? extends ILocation> locationType = connector.getLocationType(null, project);

		assertEquals(IResourceLocation.class, locationType);
	}

	@Test
	public void getLocationTypeIFolder() {
		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));

		final Class<? extends ILocation> locationType = connector.getLocationType(null, folder);

		assertEquals(IResourceLocation.class, locationType);
	}

	@Test
	public void getLocationTypeIFileA() {
		final IFile fileA = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.aaa"));

		final Class<? extends ILocation> locationType = connector.getLocationType(null, fileA);

		assertEquals(FileLocationA.class, locationType);
	}

	@Test
	public void getLocationTypeIFileB() {
		final IFile fileB = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.bbb"));

		final Class<? extends ILocation> locationType = connector.getLocationType(null, fileB);

		assertEquals(FileLocationB.class, locationType);
	}

	@Test
	public void getLocationTypeIFileC() {
		final IFile fileC = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.ccc"));

		final Class<? extends ILocation> locationType = connector.getLocationType(null, fileC);

		assertEquals(FileLocationC.class, locationType);
	}

	@Test
	public void initLocationIProject() {
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		TestResourceLocation location = new TestResourceLocation();

		connector.initLocation(location, project);

		assertEquals("/TestProject", location.getFullPath());
	}

	@Test
	public void initLocationIFolder() {
		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		TestResourceLocation location = new TestResourceLocation();

		connector.initLocation(location, folder);

		assertEquals("/TestProject/TestFolder", location.getFullPath());
	}

	@Test
	public void initLocationIFileA() {
		final IFile fileA = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.aaa"));
		TestResourceLocation location = new TestResourceLocation();

		connector.initLocation(location, fileA);

		assertEquals("/TestProject/TestFolder/TestFile.aaa", location.getFullPath());
	}

	@Test
	public void initLocationIFileB() {
		final IFile fileB = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.bbb"));
		TestResourceLocation location = new TestResourceLocation();

		connector.initLocation(location, fileB);

		assertEquals("/TestProject/TestFolder/TestFile.bbb", location.getFullPath());
	}

	@Test
	public void initLocationIFileC() {
		final IFile fileC = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.ccc"));
		TestResourceLocation location = new TestResourceLocation();

		connector.initLocation(location, fileC);

		assertEquals("/TestProject/TestFolder/TestFile.ccc", location.getFullPath());
	}

	@Test
	public void isRegistred() {
		assertTrue(MappingUtils.getConnectorRegistry().getConnector(IResourceLocation.class) instanceof ResourceConnector);
	}

	@Test
	public void getLocationIProject() {
		final TestLocation container = new TestLocation();
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		TestResourceLocation location = new TestResourceLocation();
		container.getContents().add(location);

		connector.initLocation(location, project);

		assertEquals(location, connector.getLocation(container, project));
	}

	@Test
	public void getLocationIFolder() {
		final TestLocation container = new TestLocation();
		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		TestResourceLocation location = new TestResourceLocation();
		container.getContents().add(location);

		connector.initLocation(location, folder);

		assertEquals(location, connector.getLocation(container, folder));
	}

	@Test
	public void getLocationIFileB() {
		final TestLocation container = new TestLocation();
		final IFile fileB = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.bbb"));
		TestResourceLocation location = new TestResourceLocation();
		container.getContents().add(location);

		connector.initLocation(location, fileB);

		assertEquals(location, connector.getLocation(container, fileB));
	}

}
