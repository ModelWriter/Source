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
package eu.modelwriter.semantic.emf.ide.tests;

import eu.modelwriter.semantic.IBase;
import eu.modelwriter.semantic.IBaseRegistryListener;
import eu.modelwriter.semantic.SemanticUtils;
import eu.modelwriter.semantic.emf.EcoreBase;
import eu.modelwriter.semantic.emf.ide.SemanticBaseListener;

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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests {@link SemanticBaseListener}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticBaseListenerTests {

	/**
	 * Make sure org.eclipse.mylyn.docs.intent.mapping.emf.ide is activated.
	 */
	private static final SemanticBaseListener LISTENER = new SemanticBaseListener(false);

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
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#providerRegistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
		 */
		public void baseRegistred(IBase base) {
			bases.add(base);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#providerUnregistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
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
	 * Creates an {@link IFile} that is not a {@link EcoreBase} resource in the given {@link IContainer}.
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
	 * Creates a {@link EcoreBase} {@link IFile} in the given container containing the given {@link EcoreBase}
	 * .
	 * 
	 * @param container
	 *            the {@link IContainer}
	 * @param base
	 *            the {@link EcoreBase}
	 * @return the created {@link IFile} if any, <code>null</code> otherwise
	 */
	private IFile createBaseFile(IContainer container, EcoreBase base) {
		final String fileName = "test.ecore";
		IFile res = container.getFile(new Path(fileName));

		try {
			if (base != null) {
				final ResourceSet rs = new ResourceSetImpl();
				final org.eclipse.emf.ecore.resource.Resource r = rs.createResource(URI
						.createPlatformResourceURI(res.getFullPath().toString(), true));
				r.getContents().add(base.getEPackage());
				r.save(null);
				container.refreshLocal(1, new NullProgressMonitor());
			} else {
				res.create(new ByteArrayInputStream("".getBytes()), true, new NullProgressMonitor());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = null;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createNotBaseFile(project);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedAddBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			ePackage.setNsURI("test");
			final EcoreBase base = new EcoreBase(ePackage);
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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedRemoveBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			ePackage.setNsURI("test");
			final EcoreBase base = new EcoreBase(ePackage);
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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.move(new Path("/project"), true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();
			final IFile file = createBaseFile(project, null);

			assertTrue(file != null);
			assertEquals(0, registeryListener.getBases().size());

			file.move(new Path("/test/file.ecore"), true, new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			ePackage.setNsURI("test");
			final EcoreBase base = new EcoreBase(ePackage);
			final IProject project = createProject();
			final IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			file.move(new Path("/test/file.ecore"), true, new NullProgressMonitor());

			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedMoveBaseFileWithBaseToNoBaseThenToBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			ePackage.setNsURI("test");
			final EcoreBase base = new EcoreBase(ePackage);
			final IProject project = createProject();
			IFile file = createBaseFile(project, base);

			assertTrue(file != null);
			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			file.move(new Path("/test/file.txt"), true, new NullProgressMonitor());
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/test/file.txt"));

			assertEquals(0, registeryListener.getBases().size());

			file.move(new Path("/test/file.ecore"), true, new NullProgressMonitor());

			assertEquals(1, registeryListener.getBases().size());
			assertEquals("test", registeryListener.getBases().get(0).getName());

			project.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedOpenBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			final EcoreBase base = new EcoreBase(ePackage);
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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseProject() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final IProject project = createProject();

			assertEquals(0, registeryListener.getBases().size());

			project.close(new NullProgressMonitor());

			assertEquals(0, registeryListener.getBases().size());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseFile() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseBaseFileWithoutBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

	@Test
	public void resourceChangedCloseBaseFileWithBase() {

		final TestBaseRegistryListener registeryListener = new TestBaseRegistryListener();
		try {
			SemanticUtils.getSemanticBaseRegistry().addListener(registeryListener);

			final EPackage ePackage = EcorePackage.eINSTANCE.getEcoreFactory().createEPackage();
			final EcoreBase base = new EcoreBase(ePackage);
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
			SemanticUtils.getSemanticBaseRegistry().removeListener(registeryListener);
		}
	}

}
