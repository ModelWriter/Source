/*******************************************************************************
 * Copyright (c) 2017 Obeo.
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * A {@link IContainerProvider} selection dialog.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ContainerProviderSelectionDialog extends Dialog {

	/**
	 * The {@link IBase} to edit.
	 */
	protected final IBase base;

	/**
	 * Selected {@link List} of {@link IContainerProvider} names.
	 */
	protected final List<String> selected = new ArrayList<String>();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 *            the parent {@link Shell}
	 * @param base
	 *            the {@link IBase} to edit
	 */
	public ContainerProviderSelectionDialog(Shell parentShell, IBase base) {
		super(parentShell);
		this.base = base;
		selected.addAll(base.getContainerProviders());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite)super.createDialogArea(parent);

		CheckboxTableViewer tableViewer = new CheckboxTableViewer(new Table(container, SWT.BORDER
				| SWT.V_SCROLL | SWT.CHECK));
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.setLabelProvider(new LabelProvider());
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {
				return ((java.util.List<Object>)inputElement).toArray();
			}

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
			 */
			public void dispose() {
				// nothing to do here
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// nothing to do here
			}

		});
		tableViewer.setCheckStateProvider(new ICheckStateProvider() {

			public boolean isGrayed(Object element) {
				return false;
			}

			public boolean isChecked(Object element) {
				return selected.contains(element);
			}
		});
		tableViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					selected.add((String)event.getElement());
				} else {
					selected.remove((String)event.getElement());
				}
			}
		});
		tableViewer.setInput(getContainerProviderNames());

		return container;
	}

	/**
	 * Gets the {@link java.util.List List} of possible {@link IContainerProvider} names.
	 * 
	 * @return the {@link java.util.List List} of possible {@link IContainerProvider} names
	 */
	protected java.util.List<String> getContainerProviderNames() {
		final java.util.List<String> res;

		final Set<String> providerNames = new HashSet<String>();
		providerNames.addAll(base.getContainerProviders());
		providerNames.addAll(MappingUtils.getContainerProviderFactory().getProviderNames());

		res = new ArrayList<String>(providerNames);
		Collections.sort(res);

		return res;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 300);
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		base.getContainerProviders().clear();
		base.getContainerProviders().addAll(selected);
	}

}
