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

import eu.modelwriter.semantic.IBase;
import eu.modelwriter.semantic.SemanticUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

/**
 * The semantic {@link ViewPart}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticView extends ViewPart {

	/**
	 * The view ID.
	 */
	public static final String ID = "eu.modelwriter.semantic.ide.ui.view.SemanticView"; //$NON-NLS-1$

	/**
	 * The {@link Set} of current selected {@link IBase}.
	 */
	private Set<IBase> selectedBases = new LinkedHashSet<IBase>();

	/**
	 * Constructor.
	 */
	public SemanticView() {
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

		FilteredTree semanticBaseTree = new FilteredTree(container, SWT.CHECK | SWT.BORDER,
				new PatternFilter(), false);
		semanticBaseTree.getViewer().setContentProvider(new SemanticBaseRegistryContentProvider());
		semanticBaseTree.getViewer().setLabelProvider(new SemanticBaseLabelProvider());
		semanticBaseTree.getViewer().setInput(SemanticUtils.getSemanticRegistry());
		semanticBaseTree.getViewer().getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (((TreeItem)event.item).getChecked()) {
						selectedBases.add((IBase)event.item.getData());
					} else {
						selectedBases.remove((IBase)event.item.getData());
					}
				}
			}
		});

		getSite().setSelectionProvider(semanticBaseTree.getViewer());

		createActions();
		initializeToolBar();
		initializeMenu();
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

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	/**
	 * Gets the {@link Set} of selected {@link IBase}.
	 * 
	 * @return the {@link Set} of selected {@link IBase}
	 */
	public Set<IBase> getBase() {
		return selectedBases;
	}

}
