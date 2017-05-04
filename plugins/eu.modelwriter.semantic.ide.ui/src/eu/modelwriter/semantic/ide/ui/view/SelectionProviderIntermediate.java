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
package eu.modelwriter.semantic.ide.ui.view;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * An {@link IPostSelectionProvider} dynamically delegating to an other {@link ISelectionProvider}. From the
 * example of Marc R. Hoffmann.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SelectionProviderIntermediate implements IPostSelectionProvider {

	/**
	 * The list of {@link ISelectionChangedListener} for selection.
	 */
	@SuppressWarnings("rawtypes")
	private final ListenerList selectionListeners = new ListenerList();

	/**
	 * The list of {@link ISelectionChangedListener} for post selection.
	 */
	@SuppressWarnings("rawtypes")
	private final ListenerList postSelectionListeners = new ListenerList();

	/**
	 * The current {@link IPostSelectionProvider}.
	 */
	private ISelectionProvider delegate;

	/**
	 * {@link ISelectionChangedListener} for selection.
	 */
	private ISelectionChangedListener selectionListener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			if (event.getSelectionProvider() == delegate) {
				fireSelectionChanged(event.getSelection());
			}
		}
	};

	/**
	 * {@link ISelectionChangedListener} for post selection.
	 */
	private ISelectionChangedListener postSelectionListener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			if (event.getSelectionProvider() == delegate) {
				firePostSelectionChanged(event.getSelection());
			}
		}
	};

	/**
	 * Sets a new selection provider to delegate to. Selection listeners registered with the previous delegate
	 * are removed before.
	 * 
	 * @param newDelegate
	 *            new selection provider
	 */
	public void setSelectionProviderDelegate(ISelectionProvider newDelegate) {
		if (delegate != newDelegate) {
			if (delegate != null) {
				delegate.removeSelectionChangedListener(selectionListener);
				if (delegate instanceof IPostSelectionProvider) {
					((IPostSelectionProvider)delegate).removePostSelectionChangedListener(
							postSelectionListener);
				}
			}
			delegate = newDelegate;
			if (newDelegate != null) {
				newDelegate.addSelectionChangedListener(selectionListener);
				if (newDelegate instanceof IPostSelectionProvider) {
					((IPostSelectionProvider)newDelegate).addPostSelectionChangedListener(
							postSelectionListener);
				}
				fireSelectionChanged(newDelegate.getSelection());
				firePostSelectionChanged(newDelegate.getSelection());
			}
		}
	}

	/**
	 * Fires {@link ISelection} change.
	 * 
	 * @param selection
	 *            the {@link ISelection}
	 */
	protected void fireSelectionChanged(ISelection selection) {
		fireSelectionChanged(selectionListeners, selection);
	}

	/**
	 * Fires {@link ISelection} post change.
	 * 
	 * @param selection
	 *            the {@link ISelection}
	 */
	protected void firePostSelectionChanged(ISelection selection) {
		fireSelectionChanged(postSelectionListeners, selection);
	}

	/**
	 * Fires the {@link ISelection} change to the given list of {@link ISelectionChangedListener}.
	 * 
	 * @param list
	 *            the list of {@link ISelectionChangedListener}
	 * @param selection
	 *            the {@link ISelection}
	 */
	private void fireSelectionChanged(@SuppressWarnings("rawtypes") ListenerList list, ISelection selection) {
		SelectionChangedEvent event = new SelectionChangedEvent(delegate, selection);
		Object[] listeners = list.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			ISelectionChangedListener listener = (ISelectionChangedListener)listeners[i];
			listener.selectionChanged(event);
		}
	}

	// IPostSelectionProvider Implementation

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@SuppressWarnings("unchecked")
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IPostSelectionProvider#addPostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@SuppressWarnings("unchecked")
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IPostSelectionProvider#removePostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		final ISelection res;

		if (delegate == null) {
			res = null;
		} else {
			res = delegate.getSelection();
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		if (delegate != null) {
			delegate.setSelection(selection);
		}
	}

}
