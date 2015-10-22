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
import org.eclipse.mylyn.docs.intent.mapping.base.ILink.LinkStatus;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
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
	public void setDescriptionNull() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setDescription(null);

		assertNull(link.getDescription());

		assertTestLinkListener(listener, 1, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void setDescription() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setDescription("description");

		assertEquals("description", link.getDescription());

		assertTestLinkListener(listener, 1, 0, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void getDescriptionDefault() throws InstantiationException, IllegalAccessException {
		final ILink link = createLink();

		assertNull(link.getDescription());
	}

	@Test
	public void markAsValid() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.markAsValid();

		assertEquals(LinkStatus.VALID, link.getLinkStatus());

		assertTestLinkListener(listener, 0, 1, 0, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void getLinkStatusDefault() throws InstantiationException, IllegalAccessException {
		final ILink link = createLink();

		assertEquals(LinkStatus.VALID, link.getLinkStatus());
	}

	@Test
	public void setSourceNull() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setSource(null);

		assertNull(link.getSource());

		assertTestLinkListener(listener, 0, 0, 1, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void setSource() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final ILocation source = createLocation();

		link.setSource(source);

		assertEquals(source, link.getSource());

		assertTestLinkListener(listener, 0, 0, 1, 0);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void getSourceDefault() throws InstantiationException, IllegalAccessException {
		final ILink link = createLink();

		assertNull(link.getSource());
	}

	@Test
	public void setTargetNull() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		link.setTarget(null);

		assertNull(link.getTarget());

		assertTestLinkListener(listener, 0, 0, 0, 1);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void setTarget() throws InstantiationException, IllegalAccessException {
		final TestLinkListener listener = new TestLinkListener();
		final TestLinkListener removedListener = new TestLinkListener();

		final ILink link = createLink();
		link.addListener(listener);
		link.addListener(removedListener);
		link.removeListener(removedListener);
		final ILocation target = createLocation();

		link.setTarget(target);

		assertEquals(target, link.getTarget());

		assertTestLinkListener(listener, 0, 0, 0, 1);
		assertTestLinkListener(removedListener, 0, 0, 0, 0);
	}

	@Test
	public void getTargetDefault() throws InstantiationException, IllegalAccessException {
		final ILink link = createLink();

		assertNull(link.getTarget());
	}

}
