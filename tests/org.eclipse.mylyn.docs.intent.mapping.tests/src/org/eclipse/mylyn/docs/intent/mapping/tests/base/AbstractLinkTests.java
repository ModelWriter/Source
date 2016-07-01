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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link ILink}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLinkTests extends AbstractMappingTests {

	@Test
	public void setDescriptionNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setDescription(null);

		assertNull(link.getDescription());

		assertTestLinkListener(listener, 1, 0, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void setDescription() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setDescription("description");

		assertEquals("description", link.getDescription());

		assertTestLinkListener(listener, 1, 0, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void getDescriptionDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILink link = createLink();

		assertNull(link.getDescription());
	}

	@Test
	public void setSourceNull() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setSource(null);

		assertNull(link.getSource());

		assertTestLinkListener(listener, 0, 1, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void setSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final ILocation source = createLocation();

		link.setSource(source);

		assertEquals(source, link.getSource());

		assertTestLinkListener(listener, 0, 1, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void getSourceDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILink link = createLink();

		assertNull(link.getSource());
	}

	@Test
	public void setTargetNull() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setTarget(null);

		assertNull(link.getTarget());

		assertTestLinkListener(listener, 0, 0, 1, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void setTarget() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final ILocation target = createLocation();

		link.setTarget(target);

		assertEquals(target, link.getTarget());

		assertTestLinkListener(listener, 0, 0, 1, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void getTargetDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILink link = createLink();

		assertNull(link.getTarget());
	}

	@Test
	public void getTypeDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILink link = createLink();

		assertEquals(null, link.getType());
	}

	@Test
	public void setType() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILink link = createLink();
		final String type = "Type";

		link.setType(type);

		assertEquals(type, link.getType());
	}

	@Test
	public void setTypeNull() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILink link = createLink();

		link.setType(null);

		assertEquals(null, link.getType());
	}

	@Test
	public void addReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final IReport report = createReport();

		link.getReports().add(report);

		assertEquals(1, link.getReports().size());
		assertEquals(report, link.getReports().get(0));

		assertTestLinkListener(listener, 0, 0, 0, 1, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final IReport report1 = createReport();
		final IReport report2 = createReport();
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		link.getReports().addAll(reports);

		assertEquals(2, link.getReports().size());
		assertEquals(report1, link.getReports().get(0));
		assertEquals(report2, link.getReports().get(1));

		assertTestLinkListener(listener, 0, 0, 0, 2, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final IReport report = createReport();

		link.getReports().add(report);

		assertEquals(1, link.getReports().size());
		assertEquals(report, link.getReports().get(0));

		link.getReports().remove(report);

		assertEquals(0, link.getReports().size());

		assertTestLinkListener(listener, 0, 0, 0, 1, 1);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final IReport report1 = createReport();
		final IReport report2 = createReport();
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		link.getReports().addAll(reports);

		assertEquals(2, link.getReports().size());
		assertEquals(report1, link.getReports().get(0));
		assertEquals(report2, link.getReports().get(1));

		link.getReports().removeAll(reports);

		assertEquals(0, link.getReports().size());

		assertTestLinkListener(listener, 0, 0, 0, 2, 2);
		assertTestLinkListener(removedListener, 0, 0, 0, 0, 0);
	}

}
