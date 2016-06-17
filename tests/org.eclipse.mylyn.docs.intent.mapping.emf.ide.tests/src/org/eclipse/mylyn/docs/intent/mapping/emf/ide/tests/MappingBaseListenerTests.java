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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.MappingBaseListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests {@link org.eclipse.mylyn.docs.intent.mapping.emf.ide.MappingBaseListener MappingBaseListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingBaseListenerTests {

	/**
	 * Make sure org.eclipse.mylyn.docs.intent.mapping.emf.ide is activated.
	 */
	private static final MappingBaseListener LISTENER = new MappingBaseListener(false);

	/**
	 * Test {@link IBaseRegistryListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private class TestBaseRegistryListener implements IBaseRegistryListener {

		/**
		 * Registered {@link IBase}.
		 */
		private List<IBase> bases = new ArrayList<IBase>();

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#baseRegistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
		 */
		public void baseRegistred(IBase base) {
			bases.add(base);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#baseUnregistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
		 */
		public void baseUnregistred(IBase base) {
			bases.remove(base);
		}

		/**
		 * Gets the {@link List} of registered {@link IBase}.
		 * 
		 * @return the {@link List} of registered {@link IBase}
		 */
		public List<IBase> getBases() {
			return bases;
		}

	}

	/**
	 * Creates an {@link IFile} that is not a {@link Base} resource in the given {@link IContainer}.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 * @return the created {@link IFile} if any, <code>null</code> otherwise
	 */
	private IFile createNotBaseFile(IContainer container) {
		IFile res = container.getFile(new Path("test.txt"));

		try {
			res.create(new ByteArrayInputStream("text".getBytes()), true, new NullProgressMonitor());
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

	/**
	 * Creates a {@link Base} {@link IFile} in the given container containing the given {@link Base}.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 * @param base
	 *            the {@link Base}
	 * @return the created {@link IFile} if any, <code>null</code> otherwise
	 */
	private IFile createBaseFile(IContainer container, Base base) {
		final String fileName = "test.mapping";
		IFile res = container.getFile(new Path(fileName));

		try {
			final ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(MappingPackage.eNAME,
					new XMIResourceFactoryImpl());

			final Resource resource = resourceSet.createResource(URI.createPlatformResourceURI(container
					.getFullPath().toString()
					+ "/" + fileName, true));
			if (base != null) {
				resource.getContents().add(base);
			}
			resource.save(null);
		} catch (IOException e) {
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
	public void resourceChangedAddProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.move(new Path("/project"), true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			file.move(new Path("/test/file.txt"), true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			file.move(new Path("/test/file.mapping"), true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			file.move(new Path("/test/file.mapping"), true, new NullProgressMonitor());

			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithBaseToNoBaseThenToBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			file.move(new Path("/test/file.txt"), true, new NullProgressMonitor());
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/test/file.txt"));

			assertEquals(0, registeryListener.getBases().size());

			file.move(new Path("/test/file.mapping"), true, new NullProgressMonitor());

			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.open(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.open(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.open(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.open(new NullProgressMonitor());

			assertEquals(1, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			MappingUtils.getBaseRegistry().addListener(registeryListener);

			final Base base = MappingPackage.eINSTANCE.getMappingFactory().createBase();
			base.setName("test");
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			MappingUtils.getBaseRegistry().removeListener(registeryListener);
		}
	}

}
