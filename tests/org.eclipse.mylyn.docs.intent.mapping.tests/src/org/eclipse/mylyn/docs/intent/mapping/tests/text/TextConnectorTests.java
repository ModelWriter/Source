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

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.ITestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestReport;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextContainerLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.text.TextConnectorParametrizedTests.TestTextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextContainer;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link TextConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextConnectorTests extends TextConnector {

	/**
	 * The {@link TextContainerHelper}.
	 */
	private final TextContainerHelper textContainerHelper = new TextContainerHelper();

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
				new TextRegion(null, "", 0, 0));

		assertEquals(ITextLocation.class, type);
	}

	@Test
	public void initLocation() {
		final ITextContainer container = new TestTextContainerLocation();
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);

		super.initLocation(container, location, new TextRegion(null, "cd", 2, 4));

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

		super.initLocation(container, location, new TextRegion(null, "cd", 2, 4));

		assertEquals(location, super.getLocation(container, new TextRegion(null, "cd", 2, 4)));
	}

	@Test
	public void getName() {
		final ITextContainer container = new TestTextContainerLocation();
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);
		container.getContents().add(location);

		super.initLocation(container, location, new TextRegion(null, "cd", 2, 4));

		assertEquals("\"cd\"", super.getName(location));
	}

	@Test
	public void updateTextContainerDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IReport.class, new BaseElementFactory.FactoryDescriptor<TestReport>(
				TestReport.class));
		final ITextContainer container = new TestTextContainerLocation();
		container.setContainer(base);
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);
		container.getContents().add(location);
		final ILocation target = new TestTextLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);
		link.setSource(location);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		super.initLocation(container, location, new TextRegion(null, "cd", 2, 4));

		textContainerHelper.updateTextContainer(container.getContainer(), container, "abefgh");

		assertTrue(location.isMarkedAsDeleted());
		assertEquals(1, base.getReports().size());
		final IReport report = base.getReports().get(0);
		assertEquals(link, report.getLink());
		assertEquals("\"cd\" at (2, 4) has been deleted.", report.getDescription());
	}

	@Test
	public void updateTextContainerChanged() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IBase base = new BaseRegistryTests.TestBase();
		base.getFactory().addDescriptor(IReport.class, new BaseElementFactory.FactoryDescriptor<TestReport>(
				TestReport.class));
		final ITextContainer container = new TestTextContainerLocation();
		container.setContainer(base);
		container.setText("abcdefgh");
		final ITextLocation location = new TestTextLocation();
		location.setContainer(container);
		container.getContents().add(location);
		final ILocation target = new TestTextLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);
		link.setSource(location);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		super.initLocation(container, location, new TextRegion(null, "cd", 2, 4));

		textContainerHelper.updateTextContainer(container.getContainer(), container, "abc1defgh");

		assertEquals(1, base.getReports().size());
		final IReport report = base.getReports().get(0);
		assertEquals(link, report.getLink());
		assertEquals("\"cd\" at (2, 4) has been changed to \"c1d\" at (2, 5).", report.getDescription());
	}
}
