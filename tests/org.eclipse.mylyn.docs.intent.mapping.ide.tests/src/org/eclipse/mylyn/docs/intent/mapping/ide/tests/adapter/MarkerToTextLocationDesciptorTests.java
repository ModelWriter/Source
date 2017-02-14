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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.adapter.MarkerToLocationDescriptorAdapterFactory;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.ITextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.connector.TextFileConnectorDelegateTests.TestTextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link org.eclipse.mylyn.docs.intent.mapping.ide.adapter.MarkerToTextLocationDescriptor
 * MarkerToTextLocationDescriptor}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerToTextLocationDesciptorTests {

	/**
	 * Makes sure org.eclipse.mylyn.docs.intent.mapping.ide is activated.
	 */
	private static final MarkerToLocationDescriptorAdapterFactory ADAPTER = new MarkerToLocationDescriptorAdapterFactory();

	@Ignore
	@Test
	public void getAdapter() throws CoreException, IOException {
		final IProject project = createProject();

		project.refreshLocal(1, new NullProgressMonitor());
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/test/test.txt"));
		file.create(new ByteArrayInputStream("123456".getBytes()), true, new NullProgressMonitor());
		final IMarker marker = file.createMarker(IMarker.TEXT);
		marker.setAttribute(IMarker.CHAR_START, 3);
		marker.setAttribute(IMarker.CHAR_END, 4);

		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);
		IdeMappingUtils.setCurrentBase(base);
		base.getFactory().addDescriptor(ITextFileLocation.class,
				new BaseElementFactory.FactoryDescriptor<TestTextFileLocation>(TestTextFileLocation.class));
		base.getFactory().addDescriptor(
				ITextLocation.class,
				new BaseElementFactory.FactoryDescriptor<TextConnectorParametrizedTests.TestTextLocation>(
						TextConnectorParametrizedTests.TestTextLocation.class));

		ILocationDescriptor locationDescriptor = (ILocationDescriptor)Platform.getAdapterManager()
				.getAdapter(marker, ILocationDescriptor.class);

		assertNotNull(locationDescriptor);
		assertTrue(locationDescriptor.getElement() instanceof TextRegion);
		final TextRegion textRegion = (TextRegion)locationDescriptor.getElement();
		assertEquals(3, textRegion.getStartOffset());
		assertEquals(4, textRegion.getEndOffset());
		assertEquals("4", textRegion.getText());
		assertTrue(locationDescriptor.getContainerDescriptor().getElement() instanceof IFile);
		assertNull(locationDescriptor.getContainerDescriptor().getContainerDescriptor());
		final IFile container = (IFile)locationDescriptor.getContainerDescriptor().getElement();
		assertEquals("/test/test.txt", container.getFullPath().toString());

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
