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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.internal.connector.FileDelegateRegistry;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link FileDelegateRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class FileDelegateRegistryTests {

	/**
	 * Content type A {@link IContentType#getId() ID}.
	 */
	public static final String CONTENT_TYPE_A = "org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA";

	/**
	 * Content type B {@link IContentType#getId() ID}.
	 */
	public static final String CONTENT_TYPE_B = "org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB";

	/**
	 * Content type C {@link IContentType#getId() ID}.
	 */
	public static final String CONTENT_TYPE_C = "org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC";

	/**
	 * Test {@link IFileConnectorDelegate} A.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class FileConnectorDelegateA implements IFileConnectorDelegate {

		/**
		 * Test {@link IFileLocation}.
		 *
		 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
		 */
		public interface FileLocationA extends IFileLocation {

		}

		public IContentType getContentType() {
			final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

			return contentTypeManager.getContentType(CONTENT_TYPE_A);
		}

		public Class<? extends IFileLocation> getFileLocationType() {
			return FileLocationA.class;
		}

		public void initLocation(IFileLocation location, IFile element) {
			// nothing to do here
		}

	}

	/**
	 * Test {@link IFileConnectorDelegate} B.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class FileConnectorDelegateB implements IFileConnectorDelegate {

		/**
		 * Test {@link IFileLocation}.
		 *
		 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
		 */
		public interface FileLocationB extends IFileLocation {

		}

		public IContentType getContentType() {
			final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

			return contentTypeManager.getContentType(CONTENT_TYPE_B);
		}

		public Class<? extends IFileLocation> getFileLocationType() {
			return FileLocationB.class;
		}

		public void initLocation(IFileLocation location, IFile element) {
			// nothing to do here
		}

	}

	/**
	 * Test {@link IFileConnectorDelegate} C.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class FileConnectorDelegateC implements IFileConnectorDelegate {

		/**
		 * Test {@link IFileLocation}.
		 *
		 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
		 */
		public interface FileLocationC extends IFileLocation {

		}

		public IContentType getContentType() {
			final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

			return contentTypeManager.getContentType(CONTENT_TYPE_C);
		}

		public Class<? extends IFileLocation> getFileLocationType() {
			return FileLocationC.class;
		}

		public void initLocation(IFileLocation location, IFile element) {
			// nothing to do here
		}

	}

	/**
	 * The {@link FileConnectorDelegateA}.
	 */
	public static final FileConnectorDelegateA FILE_CONNECTOR_DELEGATE_A = new FileConnectorDelegateA();

	/**
	 * The {@link FileConnectorDelegateB}.
	 */
	public static final FileConnectorDelegateB FILE_CONNECTOR_DELEGATE_B = new FileConnectorDelegateB();

	/**
	 * The {@link FileConnectorDelegateC}.
	 */
	public static final FileConnectorDelegateC FILE_CONNECTOR_DELEGATE_C = new FileConnectorDelegateC();

	/**
	 * The {@link FileDelegateRegistry} to test.
	 */
	private FileDelegateRegistry registry;

	@Before
	public void before() {
		registry = new FileDelegateRegistry();
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void registerNull() {
		registry.register(null);
	}

	@Test
	public void register() {
		IFileConnectorDelegate delegate = new FileConnectorDelegateA();

		registry.register(delegate);

		assertEquals(1, registry.getConnectorDelegates().size());
		assertEquals(delegate, registry.getConnectorDelegates().get(0));
	}

	@Test
	public void registerOrder() {
		registry.register(FILE_CONNECTOR_DELEGATE_B);
		registry.register(FILE_CONNECTOR_DELEGATE_A);
		registry.register(FILE_CONNECTOR_DELEGATE_C);

		assertEquals(3, registry.getConnectorDelegates().size());
		assertEquals(FILE_CONNECTOR_DELEGATE_C, registry.getConnectorDelegates().get(0));
		assertEquals(FILE_CONNECTOR_DELEGATE_B, registry.getConnectorDelegates().get(1));
		assertEquals(FILE_CONNECTOR_DELEGATE_A, registry.getConnectorDelegates().get(2));
	}

	@Test
	public void unregisterNull() {
		registry.unregister(null);

		assertEquals(0, registry.getConnectorDelegates().size());
	}

	@Test
	public void unregister() {
		IFileConnectorDelegate delegate = new FileConnectorDelegateA();
		registry.register(delegate);

		registry.unregister(delegate);

		assertEquals(0, registry.getConnectorDelegates().size());
	}

	@Test
	public void getConnectorDelegatesDefault() {
		assertEquals(0, registry.getConnectorDelegates().size());
	}

}
