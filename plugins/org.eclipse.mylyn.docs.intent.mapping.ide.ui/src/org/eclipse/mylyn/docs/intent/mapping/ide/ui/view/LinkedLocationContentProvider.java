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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;

/**
 * Provide {@link ILink linked} {@link ILocation} for a given {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LinkedLocationContentProvider implements ITreeContentProvider {

	/**
	 * {@link ILocation#getSourceLinks() source} direction.
	 */
	public static final int SOURCE = 0;

	/**
	 * {@link ILocation#getTargetLinks() target} direction.
	 */
	public static final int TARGET = 1;

	/**
	 * The {@link List} of root {@link ILocation}.
	 */
	private final List<ILocation> roots = new ArrayList<ILocation>();

	/**
	 * the tree of {@link ILocation}.
	 */
	private final Map<ILocation, List<ILocation>> locationForest = new HashMap<ILocation, List<ILocation>>();

	/**
	 * The direction either {@link #SOURCE source} or {@link #TARGET target}.
	 */
	private final int direction;

	/**
	 * The current {@link Viewer}.
	 */
	private Viewer currentViewer;

	/**
	 * The current input {@link ILocation}.
	 */
	private ILocation currentInput;

	/**
	 * Mapping of {@link ILinkListener}.
	 */
	private Map<ILink, ILinkListener> linkListeners = new HashMap<ILink, ILinkListener>();

	/**
	 * Mapping of {@link ILocationListener}.
	 */
	private Map<ILocation, ILocationListener> locationListeners = new HashMap<ILocation, ILocationListener>();

	/**
	 * Constructor.
	 * 
	 * @param direction
	 *            the direction either {@link #SOURCE source} or {@link #TARGET target}
	 */
	public LinkedLocationContentProvider(int direction) {
		this.direction = direction;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		clearLocations();
		clearLinks();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		update(viewer, newInput);
	}

	/**
	 * Update the content with the given {@link Viewer} and {@link Object input}.
	 * 
	 * @param viewer
	 *            the {@link Viewer}
	 * @param input
	 *            the {@link Object input}
	 */
	private void update(Viewer viewer, Object input) {
		this.currentViewer = viewer;
		this.currentInput = (ILocation)input;
		update();
	}

	/**
	 * Update the content.
	 */
	private void update() {
		clearLocations();
		clearLinks();

		final List<ILocation> locationLeaves = getLeafLocations();

		roots.addAll(setLocationForest(currentViewer, locationLeaves));
	}

	/**
	 * Gets the {@link List} of leaf {@link ILocation}.
	 * 
	 * @return the {@link List} of leaf {@link ILocation}
	 */
	private List<ILocation> getLeafLocations() {
		final List<ILocation> locationLeaves = new ArrayList<ILocation>();

		if (currentInput instanceof ILocation) {
			if (direction == SOURCE) {
				for (ILink link : ((ILocation)currentInput).getSourceLinks()) {
					final ILinkListener linkListener = new ILinkListener.Stub() {

						public void sourceChanged(ILocation oldSource, ILocation newSource) {
							update();
							currentViewer.refresh();
						}

						public void targetChanged(ILocation oldTarget, ILocation newTarget) {
							update();
							currentViewer.refresh();
						}
					};
					linkListeners.put(link, linkListener);
					link.addListener(linkListener);
					locationLeaves.add(link.getSource());
				}
			} else if (direction == TARGET) {
				for (ILink link : ((ILocation)currentInput).getTargetLinks()) {
					final ILinkListener linkListener = new ILinkListener.Stub() {

						public void sourceChanged(ILocation oldSource, ILocation newSource) {
							update();
							currentViewer.refresh();
						}

						public void targetChanged(ILocation oldTarget, ILocation newTarget) {
							update();
							currentViewer.refresh();
						}
					};
					linkListeners.put(link, linkListener);
					link.addListener(linkListener);
					locationLeaves.add(link.getTarget());
				}
			}
		}

		return locationLeaves;
	}

	/**
	 * Clears the {@link ILocation} and removes {@link ILocationListener}.
	 */
	private void clearLocations() {
		Set<ILocation> currentLocations = new HashSet<ILocation>(roots);

		while (!currentLocations.isEmpty()) {
			Set<ILocation> nextLocations = new HashSet<ILocation>();
			for (ILocation current : currentLocations) {
				current.removeListener(locationListeners.remove(current));
				nextLocations.addAll(locationForest.get(current));
			}
			currentLocations = nextLocations;
		}

		roots.clear();
		locationForest.clear();
	}

	/**
	 * Clears the {@link ILinkListener}.
	 */
	private void clearLinks() {
		for (Entry<ILink, ILinkListener> entry : linkListeners.entrySet()) {
			entry.getKey().removeListener(entry.getValue());
		}
		linkListeners.clear();
	}

	/**
	 * Sets the {@link ILocation} forest.
	 * 
	 * @param viewer
	 *            the {@link Viewer}.
	 * @param locationLeaves
	 *            the {@link List} of {@link ILocation} leaves.
	 * @return the {@link List} of {@link ILocation} roots.
	 */
	private List<ILocation> setLocationForest(Viewer viewer, List<ILocation> locationLeaves) {
		final List<ILocation> res = new ArrayList<ILocation>();

		Set<ILocation> currentLocations = new LinkedHashSet<ILocation>(locationLeaves);
		for (ILocation leaf : locationLeaves) {
			locationForest.put(leaf, new ArrayList<ILocation>());
		}
		while (!currentLocations.isEmpty()) {
			final Set<ILocation> nextLocations = new LinkedHashSet<ILocation>();
			for (ILocation current : currentLocations) {
				if (locationListeners.get(current) == null) {
					ILocationListener listener = new ILocationListener.Stub() {

						@Override
						public void containerChanged(ILocationContainer oldContainer,
								ILocationContainer newContainer) {
							update();
							currentViewer.refresh();
						}

					};
					locationListeners.put(current, listener);
					current.addListener(listener);
				}
				if (current.getContainer() instanceof ILocation) {
					final ILocation container = (ILocation)current.getContainer();
					List<ILocation> children = locationForest.get(container);
					if (children == null) {
						children = new ArrayList<ILocation>();
						locationForest.put(container, children);
					}
					children.add(current);
					nextLocations.add(container);
				} else {
					res.add(current);
				}

			}
			currentLocations = nextLocations;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return roots.toArray();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		return locationForest.get(parentElement).toArray();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		final ILocation res;

		final ILocationContainer container = ((ILocation)element).getContainer();
		if (container instanceof ILocation) {
			res = (ILocation)container;
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		return !locationForest.get(element).isEmpty();
	}

}
