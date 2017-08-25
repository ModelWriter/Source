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

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.LocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ICurrentBaseListener;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ISynchronizationPaletteListener;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.tests.base.BaseElementFactoryTests.TestLocation;
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

	/**
	 * A test {@link IFileLocation}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestFileLocation extends TestLocation implements IFileLocation {

		/**
		 * The full path.
		 */
		private String fullPath;

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String path) {
			this.fullPath = path;
		}

	}

	/**
	 * Test {@link ICurrentBaseListener}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
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
	 * Test {@link ISynchronizationPaletteListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestSynchronizationPaletteListener implements ISynchronizationPaletteListener {

		/**
		 * Counts calls to {@link ISynchronizationPaletteListener#locationActivated(ILocationDescriptor)}.
		 */
		private int locationActivatedCount;

		/**
		 * Counts calls to {@link ISynchronizationPaletteListener#locationDeactivated(ILocationDescriptor)}.
		 */
		private int locationDeactivatedCount;

		/**
		 * Counts calls to {@link ISynchronizationPaletteListener#contentsAdded(ILocationDescriptor)}.
		 */
		private int contentsAddedCount;

		/**
		 * Counts calls to {@link ISynchronizationPaletteListener#contentsRemoved(ILocationDescriptor)}.
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
		for (ILocationDescriptor descriptor : IdeMappingUtils.getSynchronizationPalette()) {
			IdeMappingUtils.removeLocationFromPalette(descriptor);
		}
	}

	public static void assertCurrentBaseListener(TestCurrentBaseListener listener,
			int currentBaseChangedCount) {
		assertEquals(currentBaseChangedCount, listener.currentBaseChangedCount);
	}

	public static void assertSynchronizationPaletteListener(TestSynchronizationPaletteListener listener,
			int locationActivatedCount, int locationDeactivatedCount, int contentsAddedCount,
			int contentsRemovedCount) {
		assertEquals(locationActivatedCount, listener.locationActivatedCount);
		assertEquals(locationDeactivatedCount, listener.locationDeactivatedCount);
		assertEquals(contentsAddedCount, listener.contentsAddedCount);
		assertEquals(contentsRemovedCount, listener.contentsRemovedCount);
	}

	@Test
	public void activateLocationNull() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.activateLocation(null);

		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);
		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void activateLocationNotInPalette() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.activateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void activateLocationInPalette() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.addLocationToPalette(descriptor);
		IdeMappingUtils.activateLocation(descriptor);

		assertTrue(IdeMappingUtils.isActive(descriptor));
		assertSynchronizationPaletteListener(listener, 1, 0, 1, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test(expected = NullPointerException.class)
	public void adaptNullNull() {
		IdeMappingUtils.adapt(null, null);
	}

	@Test
	public void adaptNullClass() {
		assertNull(IdeMappingUtils.adapt(null, ILocationDescriptor.class));
	}

	@Test
	public void adapt() {
		final Integer integer = IdeMappingUtils.adapt("17", Integer.class);

		assertNotNull(integer);
		assertEquals(Integer.valueOf(17), integer);
	}

	@Test
	public void addLocationToPaletteNull() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.addLocationToPalette(null);
		assertSynchronizationPaletteListener(listener, 0, 0, 1, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void addLocationToPaletteNotInPalette() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.addLocationToPalette(descriptor);
		assertSynchronizationPaletteListener(listener, 0, 0, 1, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void addLocationToPaletteInPalette() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPalette(descriptor);
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.addLocationToPalette(descriptor);
		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void asActiveLocationDescriptorFalse() {
		assertFalse(IdeMappingUtils.asActiveLocationDescriptor());
	}

	@Test
	public void asActiveLocationDescriptorTrue() {
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPalette(descriptor);
		IdeMappingUtils.activateLocation(descriptor);

		assertTrue(IdeMappingUtils.asActiveLocationDescriptor());
	}

	@Test
	public void deactivateLocationNull() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.deactivateLocation(null);

		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);
		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void deactivateLocationNotInPalette() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		IdeMappingUtils.addSynchronizationPaletteListener(listener);
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void deactivateLocationInPaletteNotActivated() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPalette(descriptor);
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertSynchronizationPaletteListener(listener, 0, 0, 0, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
	}

	@Test
	public void deactivateLocationInPaletteActivated() {
		final TestSynchronizationPaletteListener listener = new TestSynchronizationPaletteListener();
		final ILocation location = new TestLocation1();
		ILocationDescriptor descriptor = new LocationDescriptor(location);
		IdeMappingUtils.addLocationToPalette(descriptor);
		IdeMappingUtils.activateLocation(descriptor);
		IdeMappingUtils.addSynchronizationPaletteListener(listener);

		IdeMappingUtils.deactivateLocation(descriptor);

		assertFalse(IdeMappingUtils.isActive(descriptor));
		assertSynchronizationPaletteListener(listener, 0, 1, 0, 0);

		IdeMappingUtils.removeSynchronizationPaletteListener(listener);
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
		final ILocation container = new TestFileLocation();
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

	@Test
	public void changeCurrentBaseEmptySynchronizationPalette() {
		final IBase base = new TestBase();
		IdeMappingUtils.setCurrentBase(base);
		final ILocation location = new TestLocation1();
		base.getContents().add(location);
		location.setContainer(base);

		final ILocationDescriptor desciptor = new LocationDescriptor(location);

		IdeMappingUtils.addLocationToPalette(desciptor);

		assertFalse(IdeMappingUtils.getSynchronizationPalette().isEmpty());

		final IBase newBase = new TestBase();
		IdeMappingUtils.setCurrentBase(newBase);

		assertTrue(IdeMappingUtils.getSynchronizationPalette().isEmpty());
	}

}
