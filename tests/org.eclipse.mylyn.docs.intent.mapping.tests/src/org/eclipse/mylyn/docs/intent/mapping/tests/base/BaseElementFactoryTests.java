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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link BaseElementFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class BaseElementFactoryTests {

	/**
	 * A test location interface.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface ITestLocation extends ILocation {

	}

	/**
	 * A test location interface.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestLocation implements ITestLocation {

		/**
		 * The {@link ILocationContainer}.
		 */
		private ILocationContainer container;

		/**
		 * The {@link List} of source {@link ILink}.
		 */
		private List<ILink> sourceLinks = new ArrayList<ILink>();

		/**
		 * The {@link List} of target {@link ILink}.
		 */
		private List<ILink> targetLinks = new ArrayList<ILink>();

		/**
		 * The contents.
		 */
		private final List<ILocation> contents = new ArrayList<ILocation>();

		public String getName() {
			// nothing to do here
			return null;
		}

		public void markAsChanged() {
			// nothing to do here
		}

		public void markAsDeleted() {
			// nothing to do here
		}

		public List<ILink> getSourceLinks() {
			return sourceLinks;
		}

		public List<ILink> getTargetLinks() {
			return targetLinks;
		}

		public void setContainer(ILocationContainer container) {
			container.getContents().add(this);
			this.container = container;
		}

		public ILocationContainer getContainer() {
			return container;
		}

		public List<ILocation> getContents() {
			return contents;
		}

		public void addListener(ILocationListener listener) {
			// nothing to do here
		}

		public void removeListener(ILocationListener listener) {
			// nothing to do here
		}

		public Serializable getType() {
			// nothing to do here
			return null;
		}

		public void setType(Serializable type) {
			// nothing to do here
		}

	}

	/**
	 * Test implementation of {@link ILink}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestLink implements ILink {

		/**
		 * The source {@link ILocation}.
		 */
		private ILocation target;

		/**
		 * The target {@link ILocation}.
		 */
		private ILocation source;

		public void setType(Serializable type) {
			// nothing to do here
		}

		public void setTarget(ILocation location) {
			target = location;
		}

		public void setSource(ILocation location) {
			source = location;
		}

		public void setDescription(String description) {
			// nothing to do here
		}

		public void removeListener(ILinkListener listener) {
			// nothing to do here
		}

		public Serializable getType() {
			// nothing to do here
			return null;
		}

		public ILocation getTarget() {
			return target;
		}

		public ILocation getSource() {
			return source;
		}

		public List<IReport> getReports() {
			// nothing to do here
			return null;
		}

		public String getDescription() {
			// nothing to do here
			return null;
		}

		public void addListener(ILinkListener listener) {
			// nothing to do here
		}

	}

	@Test
	public void addClassInstance() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final BaseElementFactory factory = new BaseElementFactory();

		factory.addDescriptor(ITestLocation.class, new FactoryDescriptor<TestLocation>(TestLocation.class));

		final ITestLocation location = factory.createElement(ITestLocation.class);

		assertTrue(location instanceof TestLocation);
	}

	@Test
	public void removeClassInstance() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final BaseElementFactory factory = new BaseElementFactory();

		factory.addDescriptor(ITestLocation.class, new FactoryDescriptor<TestLocation>(TestLocation.class));
		factory.removeDescriptor(ITestLocation.class);

		final ITestLocation location = factory.createElement(ITestLocation.class);

		assertNull(location);
	}

}
