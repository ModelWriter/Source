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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

/**
 * Pool of {@link ILocation} view.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LocationPoolView extends ViewPart {

	/**
	 * A flat content provider for the {@link IdeMappingUtils#getLocationsPool() location pool}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class LocationPoolContentProvider implements ITreeContentProvider {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return IdeMappingUtils.getLocationsPool().toArray();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parentElement) {
			return new Object[0];
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			return false;
		}

	}

	/**
	 * {@link ILocationsPoolListener} refreshing a {@link Viewer} with the
	 * {@link IdeMappingUtils#getLocationsPool() location pool}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class LocationsPoolListener implements ILocationsPoolListener {

		/**
		 * The {@link TreeViewer} to refresh.
		 */
		private final TreeViewer viewer;

		/**
		 * Constructor.
		 * 
		 * @param viewer
		 *            the {@link TreeViewer} to refresh
		 */
		public LocationsPoolListener(TreeViewer viewer) {
			this.viewer = viewer;
			viewer.setInput(IdeMappingUtils.getLocationsPool());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainerListener#contentsAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsAdded(ILocation location) {
			viewer.setInput(IdeMappingUtils.getLocationsPool());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainerListener#contentsRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsRemoved(ILocation location) {
			viewer.setInput(IdeMappingUtils.getLocationsPool());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#locationActivated(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void locationActivated(ILocation location) {
			viewer.setInput(IdeMappingUtils.getLocationsPool());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see orgmenu.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#locationDeactivated(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void locationDeactivated(ILocation location) {
			viewer.setInput(IdeMappingUtils.getLocationsPool());
		}

	}

	/**
	 * The view ID.
	 */
	public static final String ID = "org.eclipse.mylyn.docs.intent.mapping.ide.ui.view.LocationPoolView"; //$NON-NLS-1$

	/**
	 * The {@link LocationsPoolListener} refreshing the viewer.
	 */
	private LocationsPoolListener locationsPoolListener;

	/**
	 * The {@link MenuManager}.
	 */
	private final MenuManager menuManager = new MenuManager();

	/**
	 * The {@link Menu}.
	 */
	private Menu menu;

	/**
	 * Constructor.
	 */
	public LocationPoolView() {
		// nothing to do here
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		final FilteredCheckboxTree locationsList = new FilteredCheckboxTree(container, SWT.CHECK | SWT.MULTI
				| SWT.BORDER, new PatternFilter(), false);
		locationsList.getViewer().setContentProvider(new LocationPoolContentProvider());
		locationsList.getViewer().setLabelProvider(new MappingLabelProvider(MappingLabelProvider.SOURCE));
		locationsList.getViewer().setCheckStateProvider(new ICheckStateProvider() {

			public boolean isGrayed(Object element) {
				return false;
			}

			public boolean isChecked(Object element) {
				return IdeMappingUtils.isActive((ILocation)element);
			}
		});
		locationsPoolListener = new LocationsPoolListener(locationsList.getViewer());
		IdeMappingUtils.addLocationToPoolListener(locationsPoolListener);
		locationsList.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(locationsList.getViewer().getTree()));
		locationsList.getViewer().getTree().addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				if (event.character == SWT.DEL) {
					final List<ILocation> toDelete = new ArrayList<ILocation>();
					for (TreeItem item : ((Tree)event.widget).getSelection()) {
						if (item.getData() instanceof ILocation) {
							toDelete.add((ILocation)item.getData());
						}
					}
					for (ILocation location : toDelete) {
						IdeMappingUtils.removeLocationFromPool(location);
					}
				}
			}
		});
		locationsList.getViewer().getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (((TreeItem)event.item).getChecked()) {
						IdeMappingUtils.activateLocation((ILocation)event.item.getData());
					} else {
						IdeMappingUtils.deactivateLocation((ILocation)event.item.getData());
					}
				}
			}
		});

		getSite().setSelectionProvider(locationsList.getViewer());

		createActions();
		initializeToolBar();

		menu = menuManager.createContextMenu(locationsList.getViewer().getControl());
		locationsList.getViewer().getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, locationsList.getViewer());
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void dispose() {
		super.dispose();
		IdeMappingUtils.removeLocationFromPoolListener(locationsPoolListener);
		menu.dispose();
		menuManager.dispose();
	}

}
