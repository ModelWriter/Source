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
package org.eclipse.mylyn.docs.intent.mapping.tests.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeProvider;
import org.eclipse.mylyn.docs.intent.mapping.internal.content.FileType;
import org.eclipse.mylyn.docs.intent.mapping.internal.content.FileTypeRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link FileTypeRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class FileTypeRegistryTests {

	@Test
	public void getFileTypeNull() {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType(null);

		assertNull(result);
	}

	@Test
	public void getFileTypeNotRegistered() {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType("someID");

		assertNull(result);
	}

	@Test
	public void getFileType() {
		MappingUtils.getFileTypeRegistry().registerFileType("someID", Collections.<String> emptySet());

		final IFileType result = MappingUtils.getFileTypeRegistry().getFileType("someID");

		assertEquals("someID", result.getID());

		MappingUtils.getFileTypeRegistry().unregisterFileType("someID");
	}

	@Test
	public void getFileTypeForNullNull() throws IOException {
		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, null);

		assertNull(result);
	}

	@Test
	public void getFileTypeFor() throws IOException {
		final IFileTypeProvider provider = new IFileTypeProvider() {

			public IFileType getFileTypeFor(InputStream contents, String name) throws IOException {
				final IFileType res;

				if (name != null && name.endsWith(".some")) {
					res = new FileType("someID");
				} else {
					res = null;
				}

				return res;
			}
		};
		MappingUtils.getFileTypeRegistry().getFileTypeProviders().add(provider);
		MappingUtils.getFileTypeRegistry().registerFileType("someID", Collections.<String> emptySet());

		final IFileType result = MappingUtils.getFileTypeRegistry().getFileTypeFor(null, "test.some");

		assertEquals("someID", result.getID());

		MappingUtils.getFileTypeRegistry().unregisterFileType("someID");
	}

	@Test
	public void isKindOfNullNull() {
		final boolean result = MappingUtils.getFileTypeRegistry().isKindOf(null, null);

		assertFalse(result);
	}

	@Test
	public void isKindOfNotRegistered() {
		final boolean result = MappingUtils.getFileTypeRegistry().isKindOf("someID", "someSuperID");

		assertFalse(result);
	}

	@Test
	public void isKindOfFalse() {
		MappingUtils.getFileTypeRegistry().registerFileType("someID", Collections.<String> emptySet());

		final boolean result = MappingUtils.getFileTypeRegistry().isKindOf("someID", "someSuperID");

		assertFalse(result);

		MappingUtils.getFileTypeRegistry().unregisterFileType("someID");
	}

	@Test
	public void isKindOfDirect() {
		Set<String> superTypes = new LinkedHashSet<String>();
		superTypes.add("someSuperID");
		MappingUtils.getFileTypeRegistry().registerFileType("someID", superTypes);

		final boolean result = MappingUtils.getFileTypeRegistry().isKindOf("someID", "someSuperID");

		assertTrue(result);

		MappingUtils.getFileTypeRegistry().unregisterFileType("someID");
	}

	@Test
	public void isKindOfIndirect() {
		Set<String> someSuperTypes = new LinkedHashSet<String>();
		someSuperTypes.add("someIntermediateSuperID");
		MappingUtils.getFileTypeRegistry().registerFileType("someID", someSuperTypes);
		Set<String> superTypes = new LinkedHashSet<String>();
		superTypes.add("someSuperID");
		MappingUtils.getFileTypeRegistry().registerFileType("someIntermediateSuperID", superTypes);

		final boolean result = MappingUtils.getFileTypeRegistry().isKindOf("someID", "someSuperID");

		assertTrue(result);

		MappingUtils.getFileTypeRegistry().unregisterFileType("someID");
		MappingUtils.getFileTypeRegistry().unregisterFileType("someIntermediateSuperID");
	}

}
