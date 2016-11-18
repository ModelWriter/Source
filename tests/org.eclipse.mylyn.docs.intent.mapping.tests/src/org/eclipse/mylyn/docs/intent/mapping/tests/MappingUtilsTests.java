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
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLink;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestReport;
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
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		source.setContainer(parent);
		target.setContainer(parent);
		link.setSource(source);
		link.setTarget(target);

		MappingUtils.deleteLink(link);

		assertNull(link.getSource());
		assertNull(link.getTarget());
		assertEquals(parent, source.getContainer());
		assertEquals(parent, target.getContainer());
	}

	@Test
	public void deleteLinkWithSourceMarkedAsDeleted() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		source.setContainer(parent);
		source.setMarkedAsDeleted(true);
		target.setContainer(parent);
		link.setSource(source);
		link.setTarget(target);

		MappingUtils.deleteLink(link);

		assertNull(link.getSource());
		assertNull(link.getTarget());
		assertNull(source.getContainer());
		assertEquals(parent, target.getContainer());
	}

	@Test
	public void deleteLinkWithTargetMarkedAsDeleted() {
		final ILink link = new BaseElementFactoryTests.TestLink();
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		source.setContainer(parent);
		target.setContainer(parent);
		target.setMarkedAsDeleted(true);
		link.setSource(source);
		link.setTarget(target);

		MappingUtils.deleteLink(link);

		assertNull(link.getSource());
		assertNull(link.getTarget());
		assertEquals(parent, source.getContainer());
		assertNull(target.getContainer());
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
		final ILocation root = new BaseElementFactoryTests.TestLocation();
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		parent.setContainer(root);
		location.setContainer(parent);

		MappingUtils.deleteLocation(location);

		assertNull(location.getContainer());
		assertEquals(root, parent.getContainer());
	}

	@Test
	public void deleteLocationWithContainerMarkedAsDeleted() {
		final ILocation root = new BaseElementFactoryTests.TestLocation();
		final ILocation parent = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		parent.setContainer(root);
		parent.setMarkedAsDeleted(true);
		location.setContainer(parent);

		MappingUtils.deleteLocation(location);

		assertNull(location.getContainer());
		assertNull(parent.getContainer());
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

	@Test
	public void canCreateLinkSourceMarkedAsDeleted() {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();

		source.setMarkedAsDeleted(true);

		assertFalse(MappingUtils.canCreateLink(source, target));
	}

	@Test
	public void canCreateLinkTargetMarkedAsDeleted() {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();

		target.setMarkedAsDeleted(true);

		assertFalse(MappingUtils.canCreateLink(source, target));
	}

	@Test
	public void canCreateLinkSameSourceAndTarget() {
		final ILocation location = new BaseElementFactoryTests.TestLocation();

		assertFalse(MappingUtils.canCreateLink(location, location));
	}

	@Test
	public void canCreateLinkExistingLink() {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();

		source.getTargetLinks().add(link);
		link.setSource(source);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		assertFalse(MappingUtils.canCreateLink(source, target));
	}

	@Test
	public void canCreateLink() {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();

		assertTrue(MappingUtils.canCreateLink(source, target));
	}

	@Test(expected = IllegalStateException.class)
	public void createLinkSourceMarkedAsDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();

		source.setMarkedAsDeleted(true);

		MappingUtils.createLink(source, target);
	}

	@Test(expected = IllegalStateException.class)
	public void createLinkTargetMarkedAsDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();

		target.setMarkedAsDeleted(true);

		MappingUtils.createLink(source, target);
	}

	@Test(expected = IllegalStateException.class)
	public void createLinkSameSourceAndTarget() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation location = new BaseElementFactoryTests.TestLocation();

		MappingUtils.createLink(location, location);
	}

	@Test(expected = IllegalStateException.class)
	public void createLinkExistingLink() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		final ILink link = new BaseElementFactoryTests.TestLink();

		source.getTargetLinks().add(link);
		link.setSource(source);
		target.getSourceLinks().add(link);
		link.setTarget(target);

		MappingUtils.createLink(source, target);
	}

	@Test
	public void createLink() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		final IBase base = new TestBase();
		base.getContents().add(source);
		source.setContainer(base);
		base.getContents().add(target);
		target.setContainer(base);
		base.getFactory().addDescriptor(ILink.class,
				new BaseElementFactory.FactoryDescriptor<TestLink>(TestLink.class));

		final ILink link = MappingUtils.createLink(source, target);

		assertEquals(source, link.getSource());
		assertEquals(target, link.getTarget());
	}

	@Test
	public void markAsDeletedOrDeleteCanDelete() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final IBase base = new TestBase();
		location.setContainer(base);

		MappingUtils.markAsDeletedOrDelete(location, "Test report.");

		assertNull(location.getContainer());
		assertEquals(0, base.getReports().size());
	}

	@Test
	public void markAsDeletedOrDeleteCanTDelete() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation child = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final IBase base = new TestBase();
		location.setContainer(base);
		location.getContents().add(child);

		MappingUtils.markAsDeletedOrDelete(location, "Test report.");

		assertEquals(base, location.getContainer());
		assertEquals(0, base.getReports().size());
	}

	@Test
	public void markAsDeletedOrDeleteCanTDeleteWithLinks() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final ILocation child = new BaseElementFactoryTests.TestLocation();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		final IBase base = new TestBase();
		base.getFactory().addDescriptor(IReport.class,
				new BaseElementFactory.FactoryDescriptor<TestReport>(TestReport.class));
		source.setContainer(base);
		location.setContainer(base);
		target.setContainer(base);
		location.getContents().add(child);
		final ILink incomingLink = new BaseElementFactoryTests.TestLink();
		incomingLink.setSource(source);
		incomingLink.setTarget(location);
		location.getSourceLinks().add(incomingLink);
		final ILink outgoingLink = new BaseElementFactoryTests.TestLink();
		outgoingLink.setSource(location);
		outgoingLink.setTarget(target);
		location.getTargetLinks().add(outgoingLink);

		MappingUtils.markAsDeletedOrDelete(location, "Test report.");

		assertEquals(base, location.getContainer());
		assertEquals(2, base.getReports().size());
		assertEquals("Test report.", base.getReports().get(0).getDescription());
		assertEquals(incomingLink, base.getReports().get(0).getLink());
		assertEquals("Test report.", base.getReports().get(1).getDescription());
		assertEquals(outgoingLink, base.getReports().get(1).getLink());
	}

	@Test
	public void markAsChanged() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation child = new BaseElementFactoryTests.TestLocation();
		final ILocation source = new BaseElementFactoryTests.TestLocation();
		final ILocation location = new BaseElementFactoryTests.TestLocation();
		final ILocation target = new BaseElementFactoryTests.TestLocation();
		final IBase base = new TestBase();
		base.getFactory().addDescriptor(IReport.class,
				new BaseElementFactory.FactoryDescriptor<TestReport>(TestReport.class));
		source.setContainer(base);
		location.setContainer(base);
		target.setContainer(base);
		location.getContents().add(child);
		final ILink incomingLink = new BaseElementFactoryTests.TestLink();
		incomingLink.setSource(source);
		incomingLink.setTarget(location);
		location.getSourceLinks().add(incomingLink);
		final ILink outgoingLink = new BaseElementFactoryTests.TestLink();
		outgoingLink.setSource(location);
		outgoingLink.setTarget(target);
		location.getTargetLinks().add(outgoingLink);

		MappingUtils.markAsChanged(location, "Test report.");

		assertEquals(base, location.getContainer());
		assertEquals(2, base.getReports().size());
		assertEquals("Test report.", base.getReports().get(0).getDescription());
		assertEquals(incomingLink, base.getReports().get(0).getLink());
		assertEquals("Test report.", base.getReports().get(1).getDescription());
		assertEquals(outgoingLink, base.getReports().get(1).getLink());
	}

}
