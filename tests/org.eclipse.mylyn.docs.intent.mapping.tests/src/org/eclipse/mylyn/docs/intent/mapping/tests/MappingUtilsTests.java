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
package org.eclipse.mylyn.docs.intent.mapping.tests;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLink;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link MappingUtils}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingUtilsTests {

	@Test(expected = NullPointerException.class)
	public void getBaseNull() {
		MappingUtils.getBase(null);
	}

	@Test
	public void getBaseNoBase() {
		final TestLocation location = new TestLocation();
		final IBase base = MappingUtils.getBase(location);

		assertNull(base);
	}

	@Test
	public void getBase() {
		final TestLocation location = new TestLocation();
		final TestBase container = new TestBase();
		location.setContainer(container);

		final IBase base = MappingUtils.getBase(location);

		assertEquals(container, base);
	}

	@Test
	public void getConnectorRegistry() {
		assertNotNull(MappingUtils.getConnectorRegistry());
	}

	@Test
	public void getMappingRegistry() {
		assertNotNull(MappingUtils.getMappingRegistry());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchNullNull() {
		MappingUtils.getDiffMatch(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchStringNull() {
		MappingUtils.getDiffMatch("", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDiffMatchNullString() {
		MappingUtils.getDiffMatch(null, "");
	}

	@Test
	public void getDiffMatch() {
		assertNotNull(MappingUtils.getDiffMatch("", ""));
	}

	@Test
	public void registerLocationImplementationBeforeBase() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());
	}

	@Test
	public void registerLocationImplementationAfterBase() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());
	}

	@Test
	public void unregisterLocationImplementation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		MappingUtils.registerLocationImplementation(TestBase.class, ILocation.class,
				new FactoryDescriptor<TestLocation>(TestLocation.class));

		final IBase base = new TestBase();
		MappingUtils.getMappingRegistry().register(base);

		final ILocation location = base.getFactory().createElement(ILocation.class);

		assertEquals(TestLocation.class, location.getClass());

		MappingUtils.unregisterLocationImplementation(TestBase.class, ILocation.class);

		assertNull(base.getFactory().createElement(ILocation.class));
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getLinkNullNull() {
		MappingUtils.getLink(null, null);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getLinkSourceNull() {
		final ILocation source = new TestLocation();

		MappingUtils.getLink(source, null);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getLinkNullTarget() {
		final ILocation target = new TestLocation();

		MappingUtils.getLink(null, target);
	}

	@Test
	public void getLinkNoLink() {
		final ILocation source = new TestLocation();
		final ILocation target = new TestLocation();

		final ILink res = MappingUtils.getLink(source, target);

		assertNull(res);
	}

	@Test
	public void getLink() {
		final ILocation source = new TestLocation();
		final ILocation target = new TestLocation();
		final ILink link = new TestLink();
		link.setSource(source);
		link.setTarget(target);
		source.getTargetLinks().add(link);
		target.getSourceLinks().add(link);

		final ILink res = MappingUtils.getLink(source, target);

		assertEquals(link, res);
	}

	@Test
	public void getLinkNotOposite() {
		final ILocation source = new TestLocation();
		final ILocation target = new TestLocation();
		final ILink link = new TestLink();
		link.setSource(source);
		link.setTarget(target);
		source.getTargetLinks().add(link);
		target.getSourceLinks().add(link);

		final ILink res = MappingUtils.getLink(target, source);

		assertNull(res);
	}

	@Test
	public void getLinkSourceMoreLinks() {
		final ILocation source = new TestLocation();
		final ILocation target = new TestLocation();
		final ILink link = new TestLink();
		final ILink link1 = new TestLink();
		link.setSource(source);
		link.setTarget(target);
		source.getTargetLinks().add(link);
		source.getTargetLinks().add(link1);
		target.getSourceLinks().add(link);

		final ILink res = MappingUtils.getLink(source, target);

		assertEquals(link, res);
	}

	@Test
	public void getLinkNotOpositeSourceMoreLinks() {
		final ILocation source = new TestLocation();
		final ILocation target = new TestLocation();
		final ILink link = new TestLink();
		final ILink link1 = new TestLink();
		link.setSource(source);
		link.setTarget(target);
		source.getTargetLinks().add(link);
		source.getTargetLinks().add(link1);
		target.getSourceLinks().add(link);

		final ILink res = MappingUtils.getLink(target, source);

		assertNull(res);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void canDeleteLinkNull() {
		MappingUtils.canDeleteLink(null);
	}

	@Test
	public void canDeleteLinkWithReports() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		link.setSource(source);
		link.setTarget(target);
		final IReport report = new BaseElementFactoryTests.TestReport();
		link.getReports().add(report);

		assertFalse(MappingUtils.canDeleteLink(link));
	}

	@Test
	public void canDeleteLink() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		link.setSource(source);
		link.setTarget(target);

		assertTrue(MappingUtils.canDeleteLink(link));
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void deleteLinkNull() {
		MappingUtils.canDeleteLink(null);
	}

	@Test(expected = IllegalStateException.class)
	public void deleteLinkWithReports() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		link.setSource(source);
		link.setTarget(target);
		final IReport report = new BaseElementFactoryTests.TestReport();
		link.getReports().add(report);

		MappingUtils.deleteLink(link);
	}

	@Test
	public void deleteLink() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		link.setSource(source);
		link.setTarget(target);

		MappingUtils.deleteLink(link);

		assertNull(link.getSource());
		assertNull(link.getTarget());
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void canDeleteLocationNull() {
		MappingUtils.canDeleteLocation(null);
	}

	@Test
	public void canDeleteLocationWithSourceLinks() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getSourceLinks().add(link);

		assertFalse(MappingUtils.canDeleteLocation(location));
	}

	@Test
	public void canDeleteLocationWithTargetLinks() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);

		assertFalse(MappingUtils.canDeleteLocation(location));
	}

	@Test
	public void canDeleteLocationWithContents() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILocation child = new BaseElementFactoryTests.TestLocation();
		location.getContents().add(child);

		assertFalse(MappingUtils.canDeleteLocation(location));
	}

	@Test
	public void canDeleteLocation() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();

		assertTrue(MappingUtils.canDeleteLocation(location));
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void deleteLocationNull() {
		MappingUtils.deleteLocation(null);
	}

	@Test(expected = IllegalStateException.class)
	public void deleteLocationWithSourceLinks() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getSourceLinks().add(link);

		MappingUtils.deleteLocation(location);
	}

	@Test(expected = IllegalStateException.class)
	public void deleteLocationWithTargetLinks() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.getTargetLinks().add(link);

		MappingUtils.deleteLocation(location);
	}

	@Test(expected = IllegalStateException.class)
	public void deleteLocationWithContents() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILocation child = new BaseElementFactoryTests.TestLocation();
		location.getContents().add(child);

		MappingUtils.deleteLocation(location);
	}

	@Test
	public void deleteLocation() {
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		location.setContainer(parent);

		MappingUtils.deleteLocation(location);

		assertNull(location.getContainer());
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void deleteReportNull() {
		MappingUtils.deleteReport(null);
	}

	@Test
	public void deleteReport() {
		final IReport report = new BaseElementFactoryTests.TestReport();
		final IBase base = new TestBase();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();
		location.setContainer(base);
		link.setSource(location);
		report.setLink(link);
		base.getReports().add(report);

		MappingUtils.deleteReport(report);

		assertFalse(base.getReports().contains(report));
		assertNull(report.getLink());
	}

}
