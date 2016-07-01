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

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link IBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractBaseTests extends AbstractMappingTests {

	@Test
	public void setNameNull() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		base.setName(null);

		assertNull(base.getName());

		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void setName() {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);

		base.setName("base");

		assertEquals("base", base.getName());

		assertTestBaseListener(listener, 1, 0, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void getNameDefault() {
		final IBase base = getBase();

		assertNull(base.getName());
	}

	@Test
	public void addRootLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location = base.getFactory().createElement(ITextLocation.class);

		base.getContents().add(location);

		assertEquals(1, base.getContents().size());
		assertEquals(location, base.getContents().get(0));

		assertTestBaseListener(listener, 0, 1, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyRootLocations() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location1 = base.getFactory().createElement(ITextLocation.class);
		final ILocation location2 = base.getFactory().createElement(ITextLocation.class);
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		base.getContents().addAll(locations);

		assertEquals(2, base.getContents().size());
		assertEquals(location1, base.getContents().get(0));
		assertEquals(location2, base.getContents().get(1));

		assertTestBaseListener(listener, 0, 2, 0, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeRootLocation() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location = base.getFactory().createElement(ITextLocation.class);

		base.getContents().add(location);

		assertEquals(1, base.getContents().size());
		assertEquals(location, base.getContents().get(0));

		base.getContents().remove(location);

		assertEquals(0, base.getContents().size());

		assertTestBaseListener(listener, 0, 1, 1, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyRootLocations() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final ILocation location1 = base.getFactory().createElement(ITextLocation.class);
		final ILocation location2 = base.getFactory().createElement(ITextLocation.class);
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		base.getContents().addAll(locations);

		assertEquals(2, base.getContents().size());
		assertEquals(location1, base.getContents().get(0));
		assertEquals(location2, base.getContents().get(1));

		base.getContents().removeAll(locations);

		assertEquals(0, base.getContents().size());

		assertTestBaseListener(listener, 0, 2, 2, 0, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void addReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report = base.getFactory().createElement(IReport.class);

		base.getReports().add(report);

		assertEquals(1, base.getReports().size());
		assertEquals(report, base.getReports().get(0));

		assertTestBaseListener(listener, 0, 0, 0, 1, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report1 = base.getFactory().createElement(IReport.class);
		final IReport report2 = base.getFactory().createElement(IReport.class);
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		base.getReports().addAll(reports);

		assertEquals(2, base.getReports().size());
		assertEquals(report1, base.getReports().get(0));
		assertEquals(report2, base.getReports().get(1));

		assertTestBaseListener(listener, 0, 0, 0, 2, 0);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeReport() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report = base.getFactory().createElement(IReport.class);

		base.getReports().add(report);

		assertEquals(1, base.getReports().size());
		assertEquals(report, base.getReports().get(0));

		base.getReports().remove(report);

		assertEquals(0, base.getReports().size());

		assertTestBaseListener(listener, 0, 0, 0, 1, 1);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyReports() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestBaseListener listener = new TestBaseListener();
		final TestBaseListener removedListener = new TestBaseListener();

		final IBase base = getBase();
		base.addListener(listener);
		base.addListener(removedListener);
		base.removeListener(removedListener);
		final IReport report1 = base.getFactory().createElement(IReport.class);
		final IReport report2 = base.getFactory().createElement(IReport.class);
		final List<IReport> reports = new ArrayList<IReport>();
		reports.add(report1);
		reports.add(report2);

		base.getReports().addAll(reports);

		assertEquals(2, base.getReports().size());
		assertEquals(report1, base.getReports().get(0));
		assertEquals(report2, base.getReports().get(1));

		base.getReports().removeAll(reports);

		assertEquals(0, base.getReports().size());

		assertTestBaseListener(listener, 0, 0, 0, 2, 2);
		assertTestBaseListener(removedListener, 0, 0, 0, 0, 0);
	}

}
