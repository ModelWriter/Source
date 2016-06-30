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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

/**
 * Mapping {@link ViewPart}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingView extends ViewPart {

	/**
	 * The view ID.
	 */
	public static final String ID = "org.eclipse.mylyn.docs.intent.mapping.ide.ui.view.MappingView"; //$NON-NLS-1$

	/**
	 * Default width.
	 */
	private static final int WIDTH = 300;

	/**
	 * The current selected {@link IBase}.
	 */
	private IBase selectedBase;

	/**
	 * The {@link SelectionProviderIntermediate} delegating to {@link org.eclipse.jface.viewers.Viewer Viewer}
	 * .
	 */
	private final SelectionProviderIntermediate selectionProvider = new SelectionProviderIntermediate();

	/**
	 * The {@link ISelectionListener} updating selection tree viewer input.
	 */
	private ISelectionListener selectionListener;

	/**
	 * TODO remove test purpose only.
	 */
	private ISelectionListener selectionListener2;

	/**
	 * Constructor.
	 */
	public MappingView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Composite headerComposite = new Composite(composite, SWT.NONE);
		headerComposite.setLayout(new GridLayout(2, false));
		headerComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		addMappingCombo(headerComposite);

		addConceptCombo(headerComposite);

		TabFolder bodyTabFolder = new TabFolder(composite, SWT.NONE);
		bodyTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addSelectionTabItem(bodyTabFolder);
		addDocumentTabItem(bodyTabFolder);
		addSynchronizationTabItem(bodyTabFolder);
		addValidationTabItem(bodyTabFolder);

		getSite().setSelectionProvider(selectionProvider);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Add the mapping {@link ComboViewer} to the given header {@link Composite}.
	 * 
	 * @param headerComposite
	 *            the header {@link Composite}
	 */
	private void addMappingCombo(Composite headerComposite) {

		Label selectMappingBaseLabel = new Label(headerComposite, SWT.NONE);
		selectMappingBaseLabel.setToolTipText("Select a mapping base.");
		selectMappingBaseLabel.setText("Mapping base:");
		final ComboViewer mappingCombo = new ComboViewer(headerComposite, SWT.READ_ONLY);
		Combo combo = mappingCombo.getCombo();
		combo.setToolTipText("Select the mapping base to use.");
		mappingCombo.setContentProvider(new MappingBaseRegistryContentProvider());
		mappingCombo.setLabelProvider(new MappingLabelProvider(MappingLabelProvider.SOURCE));
		mappingCombo.setComparator(new ViewerComparator());
		mappingCombo.setInput(MappingUtils.getMappingRegistry());
		mappingCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				selectedBase = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
			}
		});
	}

	/**
	 * Add the concept {@link ComboViewer} to the given header {@link Composite}.
	 * 
	 * @param headerComposite
	 *            the header {@link Composite}
	 */
	private void addConceptCombo(Composite headerComposite) {
	}

	/**
	 * Adds the selection {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 */
	private void addSelectionTabItem(TabFolder bodyTabFolder) {
		TabItem selectionTabItem = new TabItem(bodyTabFolder, SWT.NONE);
		selectionTabItem.setText("Selection");

		Composite treeComposite = new Composite(bodyTabFolder, SWT.NONE);
		selectionTabItem.setControl(treeComposite);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(treeComposite, SWT.NONE);

		final FilteredTree referencingTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(),
				false);
		referencingTree.getViewer().setContentProvider(
				new LinkedLocationContentProvider(false, LinkedLocationContentProvider.SOURCE, false));
		referencingTree.getViewer().setLabelProvider(new MappingLabelProvider(MappingLabelProvider.SOURCE));
		referencingTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(referencingTree.getViewer());
			}
		});

		final FilteredTree referencedTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);
		referencedTree.getViewer().setContentProvider(
				new LinkedLocationContentProvider(false, LinkedLocationContentProvider.TARGET, false));
		referencedTree.getViewer().setLabelProvider(new MappingLabelProvider(MappingLabelProvider.SOURCE));
		referencedTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(referencedTree.getViewer());
			}
		});

		selectionListener = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (part != MappingView.this) {
					final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
					if (location != null && selectedBase != null
							&& selectedBase.getName().equals(MappingUtils.getBase(location).getName())) {
						referencingTree.getViewer().setInput(location);
						referencedTree.getViewer().setInput(location);
					}
				}
			}
		};
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);

		sashForm.setWeights(new int[] {1, 1 });
	}

	/**
	 * Adds the document {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 */
	private void addDocumentTabItem(TabFolder bodyTabFolder) {
		TabItem selectionTabItem = new TabItem(bodyTabFolder, SWT.NONE);
		selectionTabItem.setText("Document");

		Composite treeComposite = new Composite(bodyTabFolder, SWT.NONE);
		selectionTabItem.setControl(treeComposite);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(treeComposite, SWT.NONE);

		final FilteredTree referencingTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(),
				false);
		referencingTree.getViewer().setContentProvider(
				new LinkedLocationContentProvider(true, LinkedLocationContentProvider.SOURCE, true));

		referencingTree.getViewer().getTree().setHeaderVisible(true);
		TreeViewerColumn referencingTreeSourceColumn = new TreeViewerColumn(referencingTree.getViewer(),
				SWT.NONE);
		referencingTreeSourceColumn.getColumn().setResizable(true);
		referencingTreeSourceColumn.getColumn().setText("Source");
		referencingTreeSourceColumn.getColumn().setWidth(WIDTH);
		referencingTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.SOURCE)));

		TreeViewerColumn referencingTreeTargetColumn = new TreeViewerColumn(referencingTree.getViewer(),
				SWT.NONE);
		referencingTreeTargetColumn.getColumn().setResizable(true);
		referencingTreeTargetColumn.getColumn().setText("Target");
		referencingTreeTargetColumn.getColumn().setWidth(WIDTH);
		referencingTreeTargetColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.TARGET)));

		referencingTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(referencingTree.getViewer());
			}
		});

		final FilteredTree referencedTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);
		referencedTree.getViewer().setContentProvider(
				new LinkedLocationContentProvider(true, LinkedLocationContentProvider.TARGET, true));

		referencedTree.getViewer().getTree().setHeaderVisible(true);
		TreeViewerColumn referencedTreeSourceColumn = new TreeViewerColumn(referencedTree.getViewer(),
				SWT.NONE);
		referencedTreeSourceColumn.getColumn().setResizable(true);
		referencedTreeSourceColumn.getColumn().setText("Source");
		referencedTreeSourceColumn.getColumn().setWidth(WIDTH);
		referencedTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.SOURCE)));

		TreeViewerColumn referencedTreeTargetColumn = new TreeViewerColumn(referencedTree.getViewer(),
				SWT.NONE);
		referencedTreeTargetColumn.getColumn().setResizable(true);
		referencedTreeTargetColumn.getColumn().setText("Target");
		referencedTreeTargetColumn.getColumn().setWidth(WIDTH);
		referencedTreeTargetColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.TARGET)));

		referencedTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(referencedTree.getViewer());
			}
		});

		// TODO remove this test purpose only
		selectionListener2 = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (part != MappingView.this) {
					final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
					if (location != null && selectedBase != null
							&& selectedBase.getName().equals(MappingUtils.getBase(location).getName())) {
						referencingTree.getViewer().setInput(location);
						referencedTree.getViewer().setInput(location);
					}
				}
			}
		};
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener2);

		sashForm.setWeights(new int[] {1, 1 });
	}

	/**
	 * Adds the synchronization {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 */
	private void addSynchronizationTabItem(TabFolder bodyTabFolder) {
		TabItem selectionTabItem = new TabItem(bodyTabFolder, SWT.NONE);
		selectionTabItem.setText("Synchronization");

		Composite treeComposite = new Composite(bodyTabFolder, SWT.NONE);
		selectionTabItem.setControl(treeComposite);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		final FilteredTree linkTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(), false);
		linkTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(linkTree.getViewer());
			}
		});
	}

	/**
	 * Adds the validation {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 */
	private void addValidationTabItem(TabFolder bodyTabFolder) {
		TabItem selectionTabItem = new TabItem(bodyTabFolder, SWT.NONE);
		selectionTabItem.setText("Validation");

		Composite treeComposite = new Composite(bodyTabFolder, SWT.NONE);
		selectionTabItem.setControl(treeComposite);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		final FilteredTree linkTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(), false);
		linkTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(linkTree.getViewer());
			}
		});
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
	 * Gets the selected {@link IBase}.
	 * 
	 * @return the selected {@link IBase}
	 */
	public IBase getBase() {
		return selectedBase;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (selectionListener != null) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectionListener);
			selectionListener = null;
		}
		if (selectionListener2 != null) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectionListener2);
			selectionListener2 = null;
		}
	}

}
