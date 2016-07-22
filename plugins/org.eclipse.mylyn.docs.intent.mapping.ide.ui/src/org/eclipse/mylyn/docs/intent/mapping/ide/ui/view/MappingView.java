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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
	 * Source column label.
	 */
	private static final String SOURCE_LABEL = "Source";

	/**
	 * Target column label.
	 */
	private static final String TARGET_LABEL = "Target";

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
		headerComposite.setLayout(new GridLayout(3, false));
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		final ComboViewer mappingBaseCombo = addMappingBaseCombo(headerComposite);
		addScopeButton(headerComposite);

		TabFolder bodyTabFolder = new TabFolder(composite, SWT.NONE);
		bodyTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addSelectionTabItem(bodyTabFolder, mappingBaseCombo);
		addDocumentTabItem(bodyTabFolder, mappingBaseCombo);
		addReportTabItem(bodyTabFolder, mappingBaseCombo);

		getSite().setSelectionProvider(selectionProvider);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Add the mapping base {@link ComboViewer} to the given header {@link Composite}.
	 * 
	 * @param headerComposite
	 *            the header {@link Composite}
	 * @return the mapping base {@link ComboViewer}
	 */
	private ComboViewer addMappingBaseCombo(Composite headerComposite) {

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
				IdeMappingUtils.setCurrentBase((IBase)((IStructuredSelection)event.getSelection())
						.getFirstElement());
			}
		});

		return mappingCombo;
	}

	/**
	 * Adds the selection {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 * @param mappingBaseCombo
	 *            the mapping base {@link ComboViewer}
	 */
	private void addSelectionTabItem(TabFolder bodyTabFolder, ComboViewer mappingBaseCombo) {
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

		mappingBaseCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				final IBase base = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
				final ILocation location = (ILocation)referencedTree.getViewer().getInput();
				if (location != null && areSameBase(MappingUtils.getBase(location), base)) {
					referencingTree.getViewer().setInput(null);
					referencedTree.getViewer().setInput(null);
				}
			}
		});

		selectionListener = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (part != MappingView.this) {
					final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
					final IBase currentBase = IdeMappingUtils.getCurentBase();
					if (location != null && currentBase != null
							&& areSameBase(currentBase, MappingUtils.getBase(location))) {
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
	 * @param mappingBaseCombo
	 *            the mapping base {@link ComboViewer}
	 */
	private void addDocumentTabItem(TabFolder bodyTabFolder, ComboViewer mappingBaseCombo) {
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
		referencingTreeSourceColumn.getColumn().setText(SOURCE_LABEL);
		referencingTreeSourceColumn.getColumn().setWidth(WIDTH);
		referencingTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.SOURCE)));

		TreeViewerColumn referencingTreeTargetColumn = new TreeViewerColumn(referencingTree.getViewer(),
				SWT.NONE);
		referencingTreeTargetColumn.getColumn().setResizable(true);
		referencingTreeTargetColumn.getColumn().setText(TARGET_LABEL);
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
		referencedTreeSourceColumn.getColumn().setText(SOURCE_LABEL);
		referencedTreeSourceColumn.getColumn().setWidth(WIDTH);
		referencedTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.SOURCE)));

		TreeViewerColumn referencedTreeTargetColumn = new TreeViewerColumn(referencedTree.getViewer(),
				SWT.NONE);
		referencedTreeTargetColumn.getColumn().setResizable(true);
		referencedTreeTargetColumn.getColumn().setText(TARGET_LABEL);
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

		mappingBaseCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				final IBase base = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
				final ILocation location = (ILocation)referencedTree.getViewer().getInput();
				if (location != null && areSameBase(MappingUtils.getBase(location), base)) {
					referencingTree.getViewer().setInput(null);
					referencedTree.getViewer().setInput(null);
				}
			}
		});

		// TODO remove this test purpose only use the current Editor input instead
		selectionListener2 = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (part != MappingView.this) {
					final ILocation location = IdeMappingUtils.adapt(selection, ILocation.class);
					final IBase currentBase = IdeMappingUtils.getCurentBase();
					if (location != null && currentBase != null
							&& areSameBase(currentBase, MappingUtils.getBase(location))) {
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
	 * Adds the report {@link TabItem} to the given body {@link TabFolder}.
	 * 
	 * @param bodyTabFolder
	 *            the body {@link TabFolder}
	 * @param mappingBaseCombo
	 *            the mapping base {@link ComboViewer}
	 */
	private void addReportTabItem(TabFolder bodyTabFolder, ComboViewer mappingBaseCombo) {
		TabItem selectionTabItem = new TabItem(bodyTabFolder, SWT.NONE);
		selectionTabItem.setText("Report");

		Composite treeComposite = new Composite(bodyTabFolder, SWT.NONE);
		selectionTabItem.setControl(treeComposite);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		final FilteredTree linkTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(), false);
		linkTree.getViewer().setContentProvider(new SyncronizationLocationContentProvider());

		linkTree.getViewer().getTree().setHeaderVisible(true);
		TreeViewerColumn linkTreeSourceColumn = new TreeViewerColumn(linkTree.getViewer(), SWT.NONE);
		linkTreeSourceColumn.getColumn().setResizable(true);
		linkTreeSourceColumn.getColumn().setText(SOURCE_LABEL);
		linkTreeSourceColumn.getColumn().setWidth(WIDTH);
		linkTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new MappingLabelProvider(
				MappingLabelProvider.SOURCE)));

		TreeViewerColumn linkTreeTargetColumn = new TreeViewerColumn(linkTree.getViewer(), SWT.NONE);
		linkTreeTargetColumn.getColumn().setResizable(true);
		linkTreeTargetColumn.getColumn().setText(TARGET_LABEL);
		linkTreeTargetColumn.getColumn().setWidth(WIDTH);
		linkTreeTargetColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new MappingLabelProvider(
				MappingLabelProvider.TARGET)));

		linkTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(linkTree.getViewer());
			}
		});

		mappingBaseCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				final IBase base = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
				linkTree.getViewer().setInput(base);
			}
		});

	}

	/**
	 * Adds the scope {@link Button} to the given body {@link Composite}.
	 * 
	 * @param headerComposite
	 *            the body {@link Composite}
	 */
	private void addScopeButton(Composite headerComposite) {
		final Button scopeButton = new Button(headerComposite, SWT.PUSH);
		scopeButton.setText("Scope");
		scopeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
		scopeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				WizardDialog wd = new WizardDialog(scopeButton.getShell(), new ScopeWizard());
				wd.open();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
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
		getSite().setSelectionProvider(null);
	}

	/**
	 * Tells if the two given {@link IBase} are the same.
	 * 
	 * @param firstBase
	 *            the first {@link IBase}
	 * @param secondBase
	 *            the second {@link IBase}
	 * @return <code>true</code> if the two givnen {@link IBase} are the same, <code>false</code> otherwise
	 */
	private boolean areSameBase(IBase firstBase, IBase secondBase) {
		// TODO change this when we work on same instances of IBase
		return firstBase.getName().equals(secondBase.getName());
	}

}
