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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLocationTests extends AbstractMappingTests {

	@Test
	public void addSourceLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getSourceLinks().add(link);

		assertEquals(1, location.getSourceLinks().size());
		assertEquals(link, location.getSourceLinks().get(0));

		assertTestLocationListener(listener, 0, 0, 0, 0, 0, 1, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManySourceLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getSourceLinks().addAll(links);

		assertEquals(2, location.getSourceLinks().size());
		assertEquals(link1, location.getSourceLinks().get(0));
		assertEquals(link2, location.getSourceLinks().get(1));

		assertTestLocationListener(listener, 0, 0, 0, 0, 0, 2, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeSourceLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getSourceLinks().add(link);

		assertEquals(1, location.getSourceLinks().size());
		assertEquals(link, location.getSourceLinks().get(0));

		location.getSourceLinks().remove(link);

		assertEquals(0, location.getSourceLinks().size());

		assertTestLocationListener(listener, 0, 0, 0, 0, 1, 1, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManySourceLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getSourceLinks().addAll(links);

		assertEquals(2, location.getSourceLinks().size());
		assertEquals(link1, location.getSourceLinks().get(0));
		assertEquals(link2, location.getSourceLinks().get(1));

		location.getSourceLinks().removeAll(links);

		assertEquals(0, location.getSourceLinks().size());

		assertTestLocationListener(listener, 0, 0, 0, 0, 2, 2, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeSourceLinksDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getSourceLinks().add(link);

		assertEquals(1, location.getSourceLinks().size());
		assertEquals(link, location.getSourceLinks().get(0));

		getBase().getContents().add(location);
		location.getSourceLinks().remove(link);

		assertEquals(0, location.getSourceLinks().size());
		assertEquals(0, getBase().getContents().size());

		assertTestLocationListener(listener, 0, 0, 0, 0, 1, 1, 2);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManySourceLinksDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getSourceLinks().addAll(links);

		assertEquals(2, location.getSourceLinks().size());
		assertEquals(link1, location.getSourceLinks().get(0));
		assertEquals(link2, location.getSourceLinks().get(1));

		getBase().getContents().add(location);
		location.getSourceLinks().removeAll(links);

		assertEquals(0, location.getSourceLinks().size());
		assertEquals(0, getBase().getContents().size());

		assertTestLocationListener(listener, 0, 0, 0, 0, 2, 2, 2);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addTargetLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getTargetLinks().add(link);

		assertEquals(1, location.getTargetLinks().size());
		assertEquals(link, location.getTargetLinks().get(0));

		assertTestLocationListener(listener, 0, 0, 0, 1, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyTargetLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getTargetLinks().addAll(links);

		assertEquals(2, location.getTargetLinks().size());
		assertEquals(link1, location.getTargetLinks().get(0));
		assertEquals(link2, location.getTargetLinks().get(1));

		assertTestLocationListener(listener, 0, 0, 0, 2, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeTargetLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getTargetLinks().add(link);

		assertEquals(1, location.getTargetLinks().size());
		assertEquals(link, location.getTargetLinks().get(0));

		location.getTargetLinks().remove(link);

		assertEquals(0, location.getTargetLinks().size());

		assertTestLocationListener(listener, 0, 0, 1, 1, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyTargetLinks() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getTargetLinks().addAll(links);

		assertEquals(2, location.getTargetLinks().size());
		assertEquals(link1, location.getTargetLinks().get(0));
		assertEquals(link2, location.getTargetLinks().get(1));

		location.getTargetLinks().removeAll(links);

		assertEquals(0, location.getTargetLinks().size());

		assertTestLocationListener(listener, 0, 0, 2, 2, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeTargetLinksDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link = createLink();

		location.getTargetLinks().add(link);

		assertEquals(1, location.getTargetLinks().size());
		assertEquals(link, location.getTargetLinks().get(0));

		getBase().getContents().add(location);
		location.getTargetLinks().remove(link);

		assertEquals(0, location.getTargetLinks().size());
		assertEquals(0, getBase().getContents().size());

		assertTestLocationListener(listener, 0, 0, 1, 1, 0, 0, 2);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyTargetLinksDeleted() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILink link1 = createLink();
		final ILink link2 = createLink();
		final List<ILink> links = new ArrayList<ILink>();
		links.add(link1);
		links.add(link2);

		location.getTargetLinks().addAll(links);

		assertEquals(2, location.getTargetLinks().size());
		assertEquals(link1, location.getTargetLinks().get(0));
		assertEquals(link2, location.getTargetLinks().get(1));

		getBase().getContents().add(location);
		location.getTargetLinks().removeAll(links);

		assertEquals(0, location.getTargetLinks().size());
		assertEquals(0, getBase().getContents().size());

		assertTestLocationListener(listener, 0, 0, 2, 2, 0, 0, 2);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addContents() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILocation location1 = createLocation();

		location.getContents().add(location1);

		assertEquals(1, location.getContents().size());
		assertEquals(location1, location.getContents().get(0));

		assertTestLocationListener(listener, 0, 1, 0, 0, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void addManyContents() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILocation location1 = createLocation();
		final ILocation location2 = createLocation();
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		location.getContents().addAll(locations);

		assertEquals(2, location.getContents().size());
		assertEquals(location1, location.getContents().get(0));
		assertEquals(location2, location.getContents().get(1));

		assertTestLocationListener(listener, 0, 2, 0, 0, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeContents() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILocation location1 = createLocation();

		location.getContents().add(location1);

		assertEquals(1, location.getContents().size());
		assertEquals(location1, location.getContents().get(0));

		location.getContents().remove(location1);

		assertEquals(0, location.getContents().size());

		assertTestLocationListener(listener, 1, 1, 0, 0, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void removeManyContents() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		final ILocation location1 = createLocation();
		final ILocation location2 = createLocation();
		final List<ILocation> locations = new ArrayList<ILocation>();
		locations.add(location1);
		locations.add(location2);

		location.getContents().addAll(locations);

		assertEquals(2, location.getContents().size());
		assertEquals(location1, location.getContents().get(0));
		assertEquals(location2, location.getContents().get(1));

		location.getContents().removeAll(locations);

		assertEquals(0, location.getContents().size());

		assertTestLocationListener(listener, 2, 2, 0, 0, 0, 0, 0);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void getContainerDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation location = createLocation();

		assertEquals(null, location.getContainer());
	}

	@Test
	public void setContainerNull() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		location.setContainer(null);

		assertNull(location.getContainer());

		assertTestLocationListener(listener, 0, 0, 0, 0, 0, 0, 1);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void setContainer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final TestLocationListener listener = new TestLocationListener();
		final TestLocationListener removedListener = new TestLocationListener();

		final ILocation container = createLocation();
		final ILocation location = createLocation();
		location.addListener(listener);
		location.addListener(removedListener);
		location.removeListener(removedListener);
		location.setContainer(container);

		assertEquals(container, location.getContainer());

		assertTestLocationListener(listener, 0, 0, 0, 0, 0, 0, 1);
		assertTestLocationListener(removedListener, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void getTypeDefault() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation location = createLocation();

		assertEquals(null, location.getType());
	}

	@Test
	public void setType() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation location = createLocation();
		final String type = "Type";

		location.setType(type);

		assertEquals(type, location.getType());
	}

	@Test
	public void setTypeNull() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation location = createLocation();

		location.setType(null);

		assertEquals(null, location.getType());
	}

}
