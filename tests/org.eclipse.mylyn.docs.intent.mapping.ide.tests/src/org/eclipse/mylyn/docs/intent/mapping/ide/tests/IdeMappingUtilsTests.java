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
package org.eclipse.mylyn.docs.intent.mapping.ide.tests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;
import org.eclipse.mylyn.docs.intent.mapping.base.LocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ICurrentBaseListener;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseRegistryTests.TestBase;
import org.eclipse.mylyn.docs.intent.mapping.tests.connector.ConnectorRegistryTests.TestLocation1;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link IdeMappingUtils}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeMappingUtilsTests {

	public static class TestCurrentBaseListener implements ICurrentBaseListener {
		/**
		 * Counts calls to {@link TestCurrentBaseListener#currentBaseChanged(IBase, IBase)}.
		 */
		private int currentBaseChangedCount;

		public void currentBaseChanged(IBase oldBase, IBase newBase) {
			currentBaseChangedCount++;
		}

	}

	/**
	 * Test {@link ILocationsPoolListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestLocationsPoolListener implements ILocationsPoolListener {

		/**
		 * Counts calls to {@link ILocationsPoolListener#locationActivated(ILocationDescriptor)}.
		 */
		private int locationActivatedCount;

		/**
		 * Counts calls to {@link ILocationsPoolListener#locationDeactivated(ILocationDescriptor)}.
		 */
		private int locationDeactivatedCount;

		/**
		 * Counts calls to {@link ILocationsPoolListener#contentsAdded(ILocationDescriptor)}.
		 */
		private int contentsAddedCount;

		/**
		 * Counts calls to {@link ILocationsPoolListener#contentsRemoved(ILocationDescriptor)}.
		 */
		private int contentsRemovedCount;

		public void locationActivated(ILocationDescriptor locationDescriptor) {
			locationActivatedCount++;
		}

		public void locationDeactivated(ILocationDescriptor locationDescriptor) {
			locationDeactivatedCount++;
		}

		public void contentsAdded(ILocationDescriptor locationDescriptor) {
			contentsAddedCount++;
		}

		public void contentsRemoved(ILocationDescriptor locationDescriptor) {
			contentsRemovedCount++;
		}

	}

	@Before
	public void before() {
		for (ILocationDescriptor descriptor : IdeMappingUtils.getLocationsPool()) {
			IdeMappingUtils.removeLocationFromPool(descriptor);
		}
	}

	public static void assertCurrentBaseListener(TestCurrentBaseListener listener, int currentBaseChangedCount) {
		assertEquals(currentBaseChangedCount, listener.currentBaseChangedCount);
	}

	public static void assertLocationPoolListener(TestLocationsPoolListener listener,
			int locationActivatedCount, int locationDeactivatedCount, int contentsAddedCount,
			int contentsRemovedCount) {
		assertEquals(locationActivatedCount, listener.locationActivatedCount);
		assertEquals(locationDeactivatedCount, listener.locationDeactivatedCount);
		assertEquals(contentsAddedCount, listener.contentsAddedCount);
		assertEquals(contentsRemovedCount, listener.contentsRemovedCount);
	}

	@Test
	public void activateLocationNull() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.activateLocation(null);

		assertLocationPoolListener(listener, 0, 0, 0, 0);
		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void activateLocationNotInPool() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.activateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertLocationPoolListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void activateLocationInPool() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.addLocationToPool(descriptor);
		IdeMappingUtils.activateLocation(descriptor);

		assertTrue(IdeMappingUtils.isActive(descriptor));
		assertLocationPoolListener(listener, 1, 0, 1, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test(expected = NullPointerException.class)
	public void adaptNullNull() {
		IdeMappingUtils.adapt(null, null);
	}

	@Test(expected = AssertionFailedException.class)
	public void adaptNullClass() {
		IdeMappingUtils.adapt(null, ILocationDescriptor.class);
	}

	@Test
	public void adapt() {
		final Integer integer = IdeMappingUtils.adapt("17", Integer.class);

		assertNotNull(integer);
		assertEquals(Integer.valueOf(17), integer);
	}

	@Test
	public void addLocationToPoolNull() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.addLocationToPool(null);
		assertLocationPoolListener(listener, 0, 0, 1, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void addLocationToPoolNotInPool() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.addLocationToPool(descriptor);
		assertLocationPoolListener(listener, 0, 0, 1, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void addLocationToPoolInPool() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPool(descriptor);
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.addLocationToPool(descriptor);
		assertLocationPoolListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void asActiveLocationDescriptorFalse() {
		assertFalse(IdeMappingUtils.asActiveLocationDescriptor());
	}

	@Test
	public void asActiveLocationDescriptorTrue() {
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPool(descriptor);
		IdeMappingUtils.activateLocation(descriptor);

		assertTrue(IdeMappingUtils.asActiveLocationDescriptor());
	}

	@Test
	public void deactivateLocationNull() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.deactivateLocation(null);

		assertLocationPoolListener(listener, 0, 0, 0, 0);
		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void deactivateLocationNotInPool() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		IdeMappingUtils.addLocationPoolListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertLocationPoolListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void deactivateLocationInPoolNotActivated() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPool(descriptor);
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertLocationPoolListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test
	public void deactivateLocationInPoolActivated() {
		final TestLocationsPoolListener listener = new TestLocationsPoolListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPool(descriptor);
		IdeMappingUtils.activateLocation(descriptor);
		IdeMappingUtils.addLocationPoolListener(listener);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertLocationPoolListener(listener, 0, 1, 0, 0);

		IdeMappingUtils.removeLocationPoolListener(listener);
	}

	@Test(expected = NullPointerException.class)
	public void getContainingFileLocationNull() {
		IdeMappingUtils.getContainingFileLocation(null);
	}

	@Test
	public void getContainingFileLocationNotContained() {
		final ILocation location = new TestLocation1();

		assertNull(IdeMappingUtils.getContainingFileLocation(location));
	}

	@Test
	public void getContainingFileLocation() {
		final ILocation container = new IFileLocation() {

			public List<ILocation> getContents() {
				return new ArrayList<ILocation>();
			}

			public void setType(Serializable type) {
				// nothing to do here
			}

			public void setContainer(ILocationContainer container) {
				// nothing to do here
			}

			public void removeListener(ILocationListener listener) {
				// nothing to do here
			}

			public Serializable getType() {
				// nothing to do here
				return null;
			}

			public List<ILink> getTargetLinks() {
				// nothing to do here
				return null;
			}

			public List<ILink> getSourceLinks() {
				// nothing to do here
				return null;
			}

			public ILocationContainer getContainer() {
				// nothing to do here
				return null;
			}

			public void addListener(ILocationListener listener) {
				// nothing to do here
			}

			public void setFullPath(String path) {
				// nothing to do here
			}

			public String getFullPath() {
				// nothing to do here
				return null;
			}
		};
		final ILocation location = new TestLocation1();
		location.setContainer(container);

		assertEquals(container, IdeMappingUtils.getContainingFileLocation(location));
	}

	@Test
	public void getFileConectorDelegateRegistry() {
		assertNotNull(IdeMappingUtils.getFileConectorDelegateRegistry());
	}

	@Test
	public void setCurrentBaseNull() {
		TestCurrentBaseListener listener = new TestCurrentBaseListener();

		IdeMappingUtils.setCurrentBase(null);

		assertCurrentBaseListener(listener, 0);

		IdeMappingUtils.removeCurrentBaseListener(listener);
	}

	@Test
	public void setCurrentBaseNotTheSame() {
		TestCurrentBaseListener listener = new TestCurrentBaseListener();
		IdeMappingUtils.addCurrentBaseListener(listener);
		final IBase base = new TestBase();

		IdeMappingUtils.setCurrentBase(base);

		assertCurrentBaseListener(listener, 1);

		IdeMappingUtils.removeCurrentBaseListener(listener);
	}

	@Test
	public void setCurrentBaseTheSame() {
		TestCurrentBaseListener listener = new TestCurrentBaseListener();
		final IBase base = new TestBase();
		IdeMappingUtils.setCurrentBase(base);
		IdeMappingUtils.addCurrentBaseListener(listener);

		IdeMappingUtils.setCurrentBase(base);

		assertCurrentBaseListener(listener, 0);

		IdeMappingUtils.removeCurrentBaseListener(listener);
	}

}
