/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.ide.tests.content;

import java.io.IOException;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.internal.content.IdeFileTypeRegistry;
import org.eclipse.mylyn.docs.intent.mapping.tests.content.FileTypeRegistryTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdeFileTypeRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeFileTypeRegistryTests extends FileTypeRegistryTests {

	/**
	 * Make sure is activated.
	 */
	private static final Activator ACTIVATOR = Activator.getDefault();

	@Test
	public void isRegistered() {
		assertTrue(MappingUtils.getFileTypeRegistry() instanceof IdeFileTypeRegistry);
	}

	@Test
	public void getFileTypeForNullNull() throws IOException {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, null);

		assertEquals("org.eclipse.core.runtime.text", result.getID());
	}

	@Test
	public void getFileTypeA() {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType(
				"org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA", result.getID());
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA"));
		assertFalse(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB"));
		assertFalse(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC"));
	}

	@Test
	public void getFileTypeB() {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType(
				"org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB", result.getID());
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA"));
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB"));
		assertFalse(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC"));
	}

	@Test
	public void getFileTypeC() {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType(
				"org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC", result.getID());
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA"));
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB"));
		assertTrue(result.isKindOf("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC"));
	}

	@Test
	public void getFileTypeForA() throws IOException {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, "test.aaa");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeA", result.getID());
	}

	@Test
	public void getFileTypeForB() throws IOException {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, "test.bbb");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeB", result.getID());
	}

	@Test
	public void getFileTypeForC() throws IOException {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, "test.ccc");

		assertEquals("org.eclipse.mylyn.docs.intent.mapping.ide.tests.content-typeC", result.getID());
	}

}
