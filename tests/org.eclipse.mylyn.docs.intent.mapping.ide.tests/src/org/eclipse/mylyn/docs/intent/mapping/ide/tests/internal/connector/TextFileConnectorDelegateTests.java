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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector;

import java.io.ByteArrayInputStream;

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
import org.eclipse.intent.mapping.ide.connector.TextFileConnectorDelegate;
import org.eclipse.intent.mapping.ide.resource.ITextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.tests.internal.connector.ResourceConnectorTests.TestResourceLocation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextFileConnectorDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextFileConnectorDelegateTests {

	/**
	 * Test {@link ITextFileLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestTextFileLocation extends TestResourceLocation implements ITextFileLocation {

		/**
		 * The text content.
		 */
		private String text;

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer#setText(java.lang.String)
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer#getText()
		 */
		public String getText() {
			return text;
		}

	}

	/**
	 * The {@link TextFileConnectorDelegate} to test.
	 */
	private final TextFileConnectorDelegate delegate = new TextFileConnectorDelegate();

	@BeforeClass
	public static void beforeClass() throws CoreException {
		final NullProgressMonitor monitor = new NullProgressMonitor();

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject");
		project.create(monitor);
		project.open(monitor);

		final IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
				new Path("TestProject/TestFolder"));
		folder.create(true, true, monitor);

		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.txt"));
		file.create(new ByteArrayInputStream("text content".getBytes()), true, monitor);
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
				new Path("TestProject/TestFolder/TestFile.txt"));
		file.delete(true, monitor);
	}

	@Test
	public void getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		final IContentType expected = contentTypeManager.getContentType(IContentTypeManager.CT_TEXT);

		assertEquals(expected, delegate.getContentType());
	}

	@Test
	public void getFileLocationType() {
		assertEquals(ITextFileLocation.class, delegate.getFileLocationType());
	}

	@Test
	public void initLocation() {
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path("TestProject/TestFolder/TestFile.txt"));
		TestTextFileLocation location = new TestTextFileLocation();

		delegate.initLocation(location, file);

		assertEquals("text content", location.getText());
	}

}
