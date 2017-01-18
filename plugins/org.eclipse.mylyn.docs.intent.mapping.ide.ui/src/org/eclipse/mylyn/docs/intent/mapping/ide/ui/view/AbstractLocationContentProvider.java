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
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;
import org.eclipse.swt.widgets.Display;

/**
 * Abstract {@link ILocation} {@link ITreeContentProvider}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLocationContentProvider implements ITreeContentProvider {

	/**
	 * The {@link ILinkListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class LinkListener extends ILinkListener.Stub {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener.Stub#sourceChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void sourceChanged(ILocation oldSource, ILocation newSource) {
			update();
			refresh();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener.Stub#targetChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void targetChanged(ILocation oldTarget, ILocation newTarget) {
			update();
			refresh();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener.Stub#reportAdded(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportAdded(org.eclipse.mylyn.docs.intent.mapping.base.IReport report) {
			if (showReport) {
				update();
				refresh();
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener.Stub#reportRemoved(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportRemoved(org.eclipse.mylyn.docs.intent.mapping.base.IReport report) {
			if (showReport) {
				update();
				refresh();
			}
		}

	}

	/**
	 * Empty {@link Object} array.
	 */
	private static final Object[] EMPTY = new Object[0];

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
	 * The {@link ILinkListener}.
	 */
	protected final ILinkListener linkListener = new LinkListener();

	/**
	 * The {@link Set} of listened {@link ILink}.
	 */
	protected Set<ILink> listenedLinks = new HashSet<ILink>();

	/**
	 * Should we show {@link org.eclipse.mylyn.docs.intent.mapping.base.IReport IReport}. Default to
	 * <code>false</code>.
	 */
	protected boolean showReport;

	/**
	 * The {@link ILocationListener}.
	 */
	protected ILocationListener locationListener = new ILocationListener.Stub() {

		@Override
		public void containerChanged(ILocationContainer oldContainer, ILocationContainer newContainer) {
			update();
			refresh();
		}

		@Override
		public void markedAsDeletedChanged(boolean newValue) {
			update();
			refresh();
		}

		public void changed(String reportDescription) {
			update();
			refresh();
		}

	};

	/**
	 * The {@link Set} of listened {@link ILocation}.
	 */
	protected Set<ILocation> listenedLocations = new HashSet<ILocation>();

	/**
	 * Refresh the {@link Viewer}.
	 */
	private void refresh() {
		if (!currentViewer.getControl().isDisposed()) {
			if (Display.getDefault().getThread() == Thread.currentThread()) {
				currentViewer.refresh();
			} else {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						currentViewer.refresh();
					}
				});
			}
		}
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
		this.currentInput = input;
		update();
	}

	/**
	 * Update the content.
	 */
	protected void update() {
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
			listenedLinks.add(link);
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
				listenedLocations.remove(current);
				current.removeListener(locationListener);
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
		for (ILink link : listenedLinks) {
			link.removeListener(linkListener);
		}
		listenedLinks.clear();
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
				if (current != null) {
					if (!listenedLocations.contains(current)) {
						listenedLocations.add(current);
						current.addListener(locationListener);
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
		final List<Object> res = new ArrayList<Object>(roots.size());

		for (ILocation location : roots) {
			if (inputElement == location) {
				final Object[] children = getChildren(inputElement);
				for (int i = 0; i < children.length; i++) {
					res.add(i, children[i]);
				}
			} else {
				res.add(location);
			}
		}

		return res.toArray();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		final Object[] res;

		final List<ILocation> children = locationForest.get(parentElement);
		if (children != null) {
			res = children.toArray();
		} else {
			res = EMPTY;
		}

		return res;
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
