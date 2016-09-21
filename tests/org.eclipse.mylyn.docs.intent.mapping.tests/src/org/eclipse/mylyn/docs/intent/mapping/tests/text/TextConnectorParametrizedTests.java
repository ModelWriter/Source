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
package org.eclipse.mylyn.docs.intent.mapping.tests.text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link TextConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Parameterized.class)
public class TextConnectorParametrizedTests {

	/**
	 * Test {@link ITextContainer}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestTextContainerLocation extends TestLocation implements ITextContainer {

		/**
		 * The {@link List} of children.
		 */
		private final List<ILocation> children = new ArrayList<ILocation>();

		/**
		 * The containing text.
		 */
		private String text;

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		@Override
		public List<ILocation> getContents() {
			return children;
		}

	}

	/**
	 * Test {@link ITextLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestTextLocation extends TestLocation implements ITextLocation {

		/**
		 * the container.
		 */
		private ILocationContainer container;

		/**
		 * The start offset.
		 */
		private int startOffset;

		/**
		 * The end offset.
		 */
		private int endOffset;

		@Override
		public void setContainer(ILocationContainer container) {
			this.container = container;
		}

		@Override
		public ILocationContainer getContainer() {
			return container;
		}

		public void setStartOffset(int offset) {
			startOffset = offset;
		}

		public int getStartOffset() {
			return startOffset;
		}

		public void setEndOffset(int offset) {
			endOffset = offset;
		}

		public int getEndOffset() {
			return endOffset;
		}

	}

	/**
	 * The original {@link ITextLocation#getText() text}.
	 */
	private final String original;

	/**
	 * The altered {@link ITextLocation#getText() text}.
	 */
	private final String altered;

	/**
	 * The text to test.
	 */
	private String testText;

	public TextConnectorParametrizedTests(String original, String altered) {
		this.original = original;
		this.altered = altered;
		try {
			testText = getContent(TextConnectorParametrizedTests.class.getResourceAsStream("/text.txt"),
					"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Parameters(name = "{0}")
	public static Iterable<String[]> data() {
		return Arrays
				.asList(new String[][] {
						{"or Fragment may be", "or Fragment may not be", },
						{
								"FEATURE LICENSES, AND FEATURE UPDATE LICENSES MAY REFER TO THE EPL OR",
								"FEATURE LICENSES, AND FEATURE UPDATE LICENSES MAY REFER TO THE LICENSES MAY REFER TO THE EPL OR", },
						{
								"Eclipse Public License Version 1.0 (\"EPL\"). A copy of the EPL is provided with this Content and is also available at http://www.eclipse.org/legal/epl-v10.html.",
								"Eclipse Public License Version 1.0 (\"EPL\"). A copy of the EPL is provided with this  Some new text at the Content and is also available at http://www.eclipse.org/legal/epl-v10.html.", },
						{"Each Feature may be packaged as a sub-directory",
								"Each Some new text at the Feature may be packaged as a sub-directory", },

						{"is a bundle of one or more Plug-ins and/or Fragments",
								"is a bundle of one or more Plug-ins and", },

						{
								"(available at http://www.opengroup.org/openmotif/supporters/metrolink/license.html)",
								"www.opengroup.org/openmotif/supporters/metrolink/license.html)", }, });
	}

	/**
	 * Gets the {@link ITextLocation} to test.
	 * 
	 * @param container
	 *            the {@link TestTextContainerLocation}that will contain created {@link ITextLocation}
	 * @param useAltered
	 *            if <code>true</code> {@link AbstractTextConnectorTests#altered} is used,
	 *            {@link AbstractTextConnectorTests#original} is used otherwise
	 * @param shift
	 *            the value to add to {@link ITextLocation#getStartOffset() text offset}.
	 * @return the {@link ITextLocation} to test
	 */
	protected ITextLocation createTextLocations(TestTextContainerLocation container) {
		final ITextLocation res = new TestTextLocation();

		res.setStartOffset(container.getText().indexOf(original));
		res.setEndOffset(container.getText().indexOf(original) + original.length());
		res.setContainer(container);
		container.getContents().add(res);

		return res;
	}

	protected String createString(int length) {
		final StringBuilder builder = new StringBuilder(length);

		for (int i = 0; i < length; ++i) {
			builder.append(' ');
		}

		return builder.toString();
	}

	/**
	 * Gets the content of the given {@link InputStream}.
	 * 
	 * @param stream
	 *            the {@link InputStream}
	 * @param charsetName
	 *            The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	 * @return a {@link CharSequence} of the content of the given {@link InputStream}
	 * @throws IOException
	 *             if the {@link InputStream} can't be read
	 */
	public static String getContent(InputStream stream, String charsetName) throws IOException {
		final int len = 8192;
		StringBuilder res = new StringBuilder(len);
		if (len != 0) {
			InputStreamReader input = new InputStreamReader(new BufferedInputStream(stream), charsetName);
			char[] buffer = new char[len];
			int length = input.read(buffer);
			while (length != -1) {
				res.append(buffer, 0, length);
				length = input.read(buffer);
			}
			input.close();
		}
		return res.toString();
	}

	@Test
	public void updateText() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset();
		final int expectedEndOffset = location.getEndOffset();

		connector.update(container, testText);

		assertEquals(testText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextPlusShift() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() + 20;
		final int expectedEndOffset = location.getEndOffset() + 20;

		final String newText = createString(20) + container.getText();
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextMinusShift() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() - 20;
		final int expectedEndOffset = location.getEndOffset() - 20;

		final String newText = container.getText().substring(20);
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextAltered() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset();
		final int expectedEndOffset = location.getStartOffset() + altered.length();

		final String newText = container.getText().replace(original, altered);
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextPlusShiftAltered() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() + 20;
		final int expectedEndOffset = location.getStartOffset() + altered.length() + 20;

		final String newText = createString(20) + container.getText().replace(original, altered);
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextMinusShiftAltered() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() - 20;
		final int expectedEndOffset = location.getStartOffset() + altered.length() - 20;

		final String newText = container.getText().replace(original, altered).substring(20);
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	private void assertTextLocation(ITextLocation location, int expectedStartOffset, int expectedEndOffset) {
		assertEquals(expectedStartOffset, location.getStartOffset());
		assertEquals(expectedEndOffset, location.getEndOffset());
	}

	@Test
	public void updateTextRemoved() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset();
		final int expectedEndOffset = expectedStartOffset;

		final String newText = container.getText().replace(original, "");
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextPlusShiftRemoved() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() + 20;
		final int expectedEndOffset = expectedStartOffset;

		final String newText = createString(20) + container.getText().replace(original, "");
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

	@Test
	public void updateTextMinusShiftRemoved() {
		final TestTextContainerLocation container = new TestTextContainerLocation();
		final TextConnector connector = new TextConnector();
		connector.update(container, testText);
		final ITextLocation location = createTextLocations(container);

		final int expectedStartOffset = location.getStartOffset() - 20;
		final int expectedEndOffset = expectedStartOffset;

		final String newText = container.getText().replace(original, "").substring(20);
		connector.update(container, newText);

		assertEquals(newText, container.getText());
		assertTextLocation(location, expectedStartOffset, expectedEndOffset);
	}

}
