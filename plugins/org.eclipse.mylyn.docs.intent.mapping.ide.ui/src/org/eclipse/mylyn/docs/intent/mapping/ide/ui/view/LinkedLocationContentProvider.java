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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener.Stub;

/**
 * Provide {@link ILink linked} {@link ILocation} for a given {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LinkedLocationContentProvider extends AbstractLocationContentProvider {

	/**
	 * Recursively listen to {@link ILocation#getContents() content} changes.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class ContentLocationListener extends Stub {

		/**
		 * Link listener for content listener. It basically attach to an {@link ILink} when its
		 * {@link ILink#getSource() source} or {@link ILink#getTarget() target} is not setted yet.
		 *
		 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
		 */
		private final class LinkListener extends org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener.Stub {

			/**
			 * The link we are attached to.
			 */
			private final ILink link;

			/**
			 * Constructor.
			 * 
			 * @param link
			 *            the {@link ILink} to attach to.
			 */
			public LinkListener(ILink link) {
				this.link = link;
			}

			@Override
			public void sourceChanged(ILocation oldSource, ILocation newSource) {
				update();
				currentViewer.refresh();
				link.removeListener(this);
			}

			@Override
			public void targetChanged(ILocation oldTarget, ILocation newTarget) {
				update();
				currentViewer.refresh();
				link.removeListener(this);
			}
		}

		@Override
		public void contentsAdded(ILocation location) {
			update();
			currentViewer.refresh();
			addListener(location);
		}

		@Override
		public void contentsRemoved(ILocation location) {
			removeListener(location);
		}

		@Override
		public void sourceLinkAdded(ILink link) {
			if (direction == SOURCE) {
				if (link.getSource() == null) {
					link.addListener(new LinkListener(link));
				} else {
					update();
					currentViewer.refresh();
				}
			}
		}

		@Override
		public void targetLinkAdded(ILink link) {
			if (direction == TARGET) {
				if (link.getTarget() == null) {
					link.addListener(new LinkListener(link));
				} else {
					update();
					currentViewer.refresh();
				}
			}
		}

		/**
		 * Recursively add itself to the content of the given {@link ILocation}.
		 * 
		 * @param location
		 *            the {@link ILocation} to listen
		 */
		private void addListener(ILocation location) {
			if (!listeners.containsKey(location)) {
				location.addListener(this);
				listeners.put(location, this);
				for (ILocation child : location.getContents()) {
					addListener(child);
				}
			}
		}

		/**
		 * Recursively remove itself from the content of the given {@link ILocation}.
		 * 
		 * @param location
		 *            the {@link ILocation} to listen
		 */
		private void removeListener(ILocation location) {
			location.removeListener(this);
			listeners.remove(location);
			for (ILocation child : location.getContents()) {
				removeListener(child);
			}
		}
	}

	/**
	 * {@link ILocation#getSourceLinks() source} direction.
	 */
	public static final int SOURCE = 0;

	/**
	 * {@link ILocation#getTargetLinks() target} direction.
	 */
	public static final int TARGET = 1;

	/**
	 * The direction either {@link #SOURCE source} or {@link #TARGET target}.
	 */
	private final int direction;

	/**
	 * Tells if the input location should be walked recursively for {@link ILink}.
	 */
	private final boolean recursive;

	/**
	 * Tells if we should provide {@link ILink}.
	 */
	private final boolean provideLinks;

	/**
	 * Mapping from an {@link ILocation} and its {@link ContentLocationListener}.
	 */
	private final Map<ILocation, ContentLocationListener> listeners = new HashMap<ILocation, ContentLocationListener>();

	/**
	 * Constructor.
	 * 
	 * @param provideLinks
	 *            tells if we should provide {@link ILink}
	 * @param direction
	 *            the direction either {@link #SOURCE source} or {@link #TARGET target}
	 * @param recursive
	 *            tells if the input location should be walked recursively for {@link ILink}
	 */
	public LinkedLocationContentProvider(boolean provideLinks, int direction, boolean recursive) {
		this.provideLinks = provideLinks;
		this.direction = direction;
		this.recursive = recursive;
	}

	@Override
	protected void setLeavesAndLinks(Object input, List<ILocation> locationLeaves, List<ILink> links) {
		if (input instanceof ILocation) {
			final ILocation location = (ILocation)input;
			if (direction == SOURCE) {
				for (ILink link : location.getSourceLinks()) {
					links.add(link);
					locationLeaves.add(link.getSource());
				}
			} else if (direction == TARGET) {
				for (ILink link : location.getTargetLinks()) {
					links.add(link);
					locationLeaves.add(link.getTarget());
				}
			}
			if (recursive) {
				final ContentLocationListener listener = new ContentLocationListener();
				location.addListener(listener);
				listeners.put((ILocation)input, listener);
				for (ILocation child : location.getContents()) {
					setLeavesAndLinks(child, locationLeaves, links);
				}
			}
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		final boolean res;

		if (provideLinks) {
			if (element instanceof ILocation) {
				res = !getchildLinks((ILocation)element).isEmpty() || super.hasChildren(element);
			} else {
				res = false;
			}
		} else {
			res = super.hasChildren(element);
		}

		return res;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		final Object[] res;

		final Object[] childLocations = super.getChildren(parentElement);
		if (provideLinks && parentElement instanceof ILocation) {
			final List<ILink> childlinks = getchildLinks((ILocation)parentElement);
			res = concat(childLocations, childlinks.toArray());
		} else {
			res = childLocations;
		}

		return res;
	}

	/**
	 * Gets child {@link ILink} for the given parent {@link ILocation}.
	 * 
	 * @param parentlocation
	 *            the parent {@link ILocation}
	 * @return child {@link ILink} for the given parent {@link ILocation}
	 */
	private List<ILink> getchildLinks(ILocation parentlocation) {
		final List<ILink> links = new ArrayList<ILink>();
		final List<ILink> linksToCheck;
		if (direction == SOURCE) {
			linksToCheck = parentlocation.getTargetLinks();
		} else if (direction == TARGET) {
			linksToCheck = parentlocation.getSourceLinks();
		} else {
			throw new IllegalStateException("should not happend");
		}
		for (ILink link : linksToCheck) {
			if (listenedLinks.contains(link)) {
				links.add(link);
			}
		}
		return links;
	}

	/**
	 * Concatenates two given arrays of {@link Object}.
	 * 
	 * @param first
	 *            the first array
	 * @param second
	 *            the second array
	 * @return a new array composed of the two given arrays
	 */
	private Object[] concat(Object[] first, Object[] second) {
		final Object[] result = Arrays.copyOf(first, first.length + second.length);

		System.arraycopy(second, 0, result, first.length, second.length);

		return result;
	}

	@Override
	public Object getParent(Object element) {
		final Object res;

		if (provideLinks && element instanceof ILink) {
			if (direction == SOURCE) {
				res = ((ILink)element).getTarget();
			} else if (direction == TARGET) {
				res = ((ILink)element).getSource();
			} else {
				throw new IllegalStateException("should not happend");
			}
		} else {
			res = super.getParent(element);
		}

		return res;
	}

	@Override
	protected void update() {
		for (Entry<ILocation, ContentLocationListener> entry : listeners.entrySet()) {
			entry.getKey().removeListener(entry.getValue());
		}
		listeners.clear();
		super.update();
	}

}
