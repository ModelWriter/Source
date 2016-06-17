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
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;
import org.eclipse.mylyn.docs.intent.mapping.base.IScope;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link BaseElementFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LocationFactoryTests {

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
			// nothing to do here
			return null;
		}

		public List<ILink> getTargetLinks() {
			// nothing to do here
			return null;
		}

		public void setScope(IScope scope) {
			// nothing to do here
		}

		public IScope getScope() {
			// nothing to do here
			return null;
		}

		public void setContainer(ILocationContainer container) {
			// nothing to do here
		}

		public ILocationContainer getContainer() {
			// nothing to do here
			return null;
		}

		public List<ILocation> getContents() {
			// nothing to do here
			return null;
		}

		public ILocation getContentLocation(String name) {
			// nothing to do here
			return null;
		}

		public List<IScope> getReferencingScopes() {
			// nothing to do here
			return null;
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
