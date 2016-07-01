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
package org.eclipse.mylyn.docs.intent.mapping.tests.base;

import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link IReport}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractReportTests extends AbstractMappingTests {

	@Test
	public void setDescriptionNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestReportListener listener = new TestReportListener();
		final TestReportListener removedListener = new TestReportListener();

		final IReport report = createReport();
		report.addListener(listener);
		report.addListener(removedListener);
		report.removeListener(removedListener);
		report.setDescription(null);

		assertNull(report.getDescription());

		assertTestReportListener(listener, 1, 0);
		assertTestReportListener(removedListener, 0, 0);
	}

	@Test
	public void setDescription() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestReportListener listener = new TestReportListener();
		final TestReportListener removedListener = new TestReportListener();

		final IReport report = createReport();
		report.addListener(listener);
		report.addListener(removedListener);
		report.removeListener(removedListener);
		report.setDescription("description");

		assertEquals("description", report.getDescription());

		assertTestReportListener(listener, 1, 0);
		assertTestReportListener(removedListener, 0, 0);
	}

	@Test
	public void getDescriptionDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IReport link = createReport();

		assertNull(link.getDescription());
	}

	@Test
	public void setLinkNull() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestReportListener listener = new TestReportListener();
		final TestReportListener removedListener = new TestReportListener();

		final IReport report = createReport();
		report.addListener(listener);
		report.addListener(removedListener);
		report.removeListener(removedListener);
		report.setLink(null);

		assertNull(report.getLink());

		assertTestReportListener(listener, 0, 1);
		assertTestReportListener(removedListener, 0, 0);
	}

	@Test
	public void setLink() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestReportListener listener = new TestReportListener();
		final TestReportListener removedListener = new TestReportListener();

		final IReport report = createReport();
		report.addListener(listener);
		report.addListener(removedListener);
		report.removeListener(removedListener);
		final ILink link = createLink();
		report.setLink(link);

		assertEquals(link, report.getLink());

		assertTestReportListener(listener, 0, 1);
		assertTestReportListener(removedListener, 0, 0);
	}

	@Test
	public void getLinkDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final IReport link = createReport();

		assertNull(link.getLink());
	}

}
