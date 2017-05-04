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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.swt.widgets.Display;

/**
 * {@link IBaseRegistry} content provider.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingBaseRegistryContentProvider implements IStructuredContentProvider {

	// Force the start of org.eclipse.intent.mapping.ide
	// TODO we should find a better way to do that
	{
		IdeMappingUtils.getFileConectorDelegateRegistry();
	}

	/**
	 * An {@link IBaseRegistryListener} that update a {@link Viewer}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class RegisteryListener implements IBaseRegistryListener {

		/**
		 * The {@link Viewer} to update.
		 */
		private final Viewer viewer;

		/**
		 * Constructor.
		 * 
		 * @param viewer
		 *            the {@link Viewer} to update
		 */
		RegisteryListener(Viewer viewer) {
			this.viewer = viewer;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#baseRegistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
		 */
		public void baseRegistred(IBase base) {
			refresh();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener#baseUnregistred(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
		 */
		public void baseUnregistred(IBase base) {
			refresh();
		}

		/**
		 * Refresh the {@link Viewer}.
		 */
		private void refresh() {
			if (!viewer.getControl().isDisposed()) {
				if (Display.getDefault().getThread() == Thread.currentThread()) {
					viewer.refresh();
				} else {
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							viewer.refresh();
						}
					});
				}
			}
		}

	}

	/**
	 * {@link Viewer} to {@link RegisteryListener} mapping.
	 */
	private final Map<Viewer, RegisteryListener> listeners = new HashMap<Viewer, RegisteryListener>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		assert listeners.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput instanceof IBaseRegistry) {
			((IBaseRegistry)oldInput).removeListener(listeners.remove(viewer));
		}
		if (newInput instanceof IBaseRegistry) {
			final RegisteryListener listener = new RegisteryListener(viewer);
			listeners.put(viewer, listener);
			((IBaseRegistry)newInput).addListener(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		final Object[] res;

		if (inputElement instanceof IBaseRegistry) {
			res = ((IBaseRegistry)inputElement).getBases().toArray();
		} else {
			res = new Object[0];
		}

		return res;
	}

}
