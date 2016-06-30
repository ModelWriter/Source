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
 * Abstract {@link ILocation} {@link ITreeContentProvider}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLocationContentProvider implements ITreeContentProvider {

	/**
	 * The {@link List} of root {@link ILocation}.
	 */
	protected final List<ILocation> roots = new ArrayList<ILocation>();

	/**
	 * the tree of {@link ILocation}.
	 */
	protected final Map<ILocation, List<ILocation>> locationForest = new HashMap<ILocation, List<ILocation>>();

	/**
	 * The current {@link Viewer}.
	 */
	protected Viewer currentViewer;

	/**
	 * The current input.
	 */
	protected Object currentInput;

	/**
	 * Mapping of {@link ILinkListener}.
	 */
	protected Map<ILink, ILinkListener> linkListeners = new HashMap<ILink, ILinkListener>();

	/**
	 * Mapping of {@link ILocationListener}.
	 */
	protected Map<ILocation, ILocationListener> locationListeners = new HashMap<ILocation, ILocationListener>();

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

		final List<ILocation> locationLeaves = new ArrayList<ILocation>();
		final List<ILink> links = new ArrayList<ILink>();

		setLeavesAndLinks(currentInput, locationLeaves, links);

		installLinkListeners(links);

		roots.addAll(setLocationForest(currentViewer, locationLeaves));
	}

	/**
	 * Sets {@link ILocation} leaves and {@link ILink}.
	 * 
	 * @param input
	 *            the current input
	 * @param locationLeaves
	 *            the {@link List} of {@link ILocation} leaves to set
	 * @param links
	 *            the {@link List} of {@link ILink} to set
	 */
	protected abstract void setLeavesAndLinks(Object input, List<ILocation> locationLeaves, List<ILink> links);

	/**
	 * Installs {@link ILinkListener} on the given {@link ILink}.
	 * 
	 * @param links
	 *            the {@link List} of {@link ILink}
	 */
	private void installLinkListeners(final List<ILink> links) {
		for (ILink link : links) {
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
		}
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
