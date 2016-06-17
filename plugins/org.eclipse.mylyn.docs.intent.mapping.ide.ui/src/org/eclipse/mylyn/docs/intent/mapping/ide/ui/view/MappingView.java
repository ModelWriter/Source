package org.eclipse.mylyn.docs.intent.mapping.ide.ui.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
	 * Constructor.
	 */
	public MappingView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Composite headerComposite = new Composite(composite, SWT.NONE);
		headerComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		headerComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		addMappingCombo(headerComposite);

		addConceptCombo(headerComposite);

		Button scopeButton = new Button(headerComposite, SWT.CENTER);
		scopeButton.setText("Scope");

		TabFolder bodyTabFolder = new TabFolder(composite, SWT.NONE);
		bodyTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addSelectionTabItem(bodyTabFolder);
		addDocumentTabItem(bodyTabFolder);
		addSynchronizationTabItem(bodyTabFolder);
		addValidationTabItem(bodyTabFolder);

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
		final ComboViewer mappingCombo = new ComboViewer(headerComposite, SWT.READ_ONLY);
		Combo combo = mappingCombo.getCombo();
		combo.setLayoutData(new RowData(100, SWT.DEFAULT));
		mappingCombo.setContentProvider(new BaseRegistryContentProvider());
		mappingCombo.setLabelProvider(new BaseLabelProvider());
		mappingCombo.setComparator(new ViewerComparator());
		mappingCombo.setInput(MappingUtils.getBaseRegistry());
	}

	/**
	 * Add the concept {@link ComboViewer} to the given header {@link Composite}.
	 * 
	 * @param headerComposite
	 *            the header {@link Composite}
	 */
	private void addConceptCombo(Composite headerComposite) {
		final ComboViewer conceptCombo = new ComboViewer(headerComposite, SWT.READ_ONLY);
		Combo combo = conceptCombo.getCombo();
		combo.setLayoutData(new RowData(100, SWT.DEFAULT));
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

		FilteredTree referencingTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);

		FilteredTree referencedTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);
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

		FilteredTree referencingTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);

		FilteredTree referencedTree = new FilteredTree(sashForm, SWT.BORDER, new PatternFilter(), false);
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

		FilteredTree linkTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(), false);
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

		FilteredTree linkTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(), false);
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
}
