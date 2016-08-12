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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.LocationFactoryTests.ITestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link TextConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextConnectorTests extends TextConnector {

	@Test
	public void getLocationTypeNotTypeContainer() {
		final Class<? extends ILocation> type = getLocationType(ITestLocation.class, "");

		assertNull(type);
	}

	@Test
	public void getLocationTypeNotElement() {
		final Class<? extends ILocation> type = getLocationType(TestTextLocation.class, new Object());

		assertNull(type);
	}

	@Test
	public void getLocationTypeTest() {
		final Class<? extends ILocation> type = getLocationType(TestTextContainerLocation.class,
				new TextRegion(0, 0));

		assertEquals(ITextLocation.class, type);
	}

	@Test
	public void initLocation() {
		final ITextContainer container = new TestTextContainerLocation();
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);

		super.initLocation(location, new TextRegion(2, 4));

		assertEquals(2, location.getStartOffset());
		assertEquals(4, location.getEndOffset());
	}

	@Test
	public void getLocation() {
		final ITextContainer container = new TestTextContainerLocation();
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);
		container.getContents().add(location);

		super.initLocation(location, new TextRegion(2, 4));

		assertEquals(location, super.getLocation(container, new TextRegion(2, 4)));
	}

	@Test
	public void getName() {
		final ITextContainer container = new TestTextContainerLocation();
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);
		container.getContents().add(location);

		super.initLocation(location, new TextRegion(2, 4));

		assertEquals("\"cd\"", super.getName(location));
	}
}
