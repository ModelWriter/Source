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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.ide.ILocationMarker;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.UiIdeMappingUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.part.ViewPart;

/**
 * Mapping {@link ViewPart}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingView extends ViewPart {

	/**
	 * {@link Listener} that open {@link ILocation}, {@link ILink}, and {@link IReport}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static class ShowLocationDoubleClickListener implements Listener {

		/**
		 * the {@link Tree} to listen to.
		 */
		private final Tree tree;

		/**
		 * Constructor.
		 * 
		 * @param tree
		 *            the {@link Tree} to listen to
		 */
		public ShowLocationDoubleClickListener(Tree tree) {
			this.tree = tree;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			Point pt = new Point(event.x, event.y);
			TreeItem item = tree.getItem(pt);
			final Object selected = item.getData();
			if (selected instanceof ILocation) {
				UiIdeMappingUtils.showLocation((ILocation)selected);
			} else if (selected instanceof ILink) {
				for (int i = 0; i < tree.getColumnCount(); i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						if (i == 0) {
							UiIdeMappingUtils.showLocation(((ILink)selected).getSource());
						} else {
							UiIdeMappingUtils.showLocation(((ILink)selected).getTarget());
						}
						break;
					}
				}
			} else if (selected instanceof IReport) {
				UiIdeMappingUtils.showLocation(((IReport)selected).getLink().getSource());
			}
		}
	}

	/**
	 * Adds and removes location markers according to edited locations and changes in {@link IBase}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class MarkerBaseListener extends ILocationListener.Stub implements IBaseListener, ILocationListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#contentsAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsAdded(ILocation location) {
			location.addListener(this);
			if (isOpenedInEditor(location)) {
				IdeMappingUtils.getOrCreateMarker(location);
			}
		}

		/**
		 * Tells if the given {@link ILocation} is opened in an editor.
		 * 
		 * @param location
		 *            the {@link ILocation} to check
		 * @return <code>true</code> if the given {@link ILocation} is opened in an editor, <code>false</code>
		 *         otherwise
		 */
		private boolean isOpenedInEditor(ILocation location) {
			final boolean res;

			// we assume all opened location are directly contained by the base
			if (location.getContainer() instanceof ILocation) {
				res = isOpenedInEditor((ILocation)location.getContainer());
			} else {
				res = editedLocations.contains(location);
			}

			return res;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#contentsRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsRemoved(ILocation location) {
			location.removeListener(this);
			IdeMappingUtils.deleteMarker(location);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#nameChanged(java.lang.String,
		 *      java.lang.String)
		 */
		public void nameChanged(String oldName, String newName) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#reportAdded(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportAdded(IReport report) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#reportRemoved(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportRemoved(IReport report) {
			// nothing to do here
		}

	}

	/**
	 * Listen to {@link org.eclipse.ui.IEditorPart IEditorPart}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class EditorPartListener implements IWindowListener, IPageListener, IPartListener2 {

		/**
		 * The array of {@link Viewer}.
		 */
		private final Viewer[] viewers;

		/**
		 * Constructor.
		 * 
		 * @param viewers
		 *            the array of {@link Viewer}
		 */
		public EditorPartListener(Viewer... viewers) {
			this.viewers = viewers;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
		 */
		public void windowActivated(IWorkbenchWindow window) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
		 */
		public void windowDeactivated(IWorkbenchWindow window) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
		 */
		public void windowClosed(IWorkbenchWindow window) {
			window.removePageListener(this);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
		 */
		public void windowOpened(IWorkbenchWindow window) {
			window.addPageListener(this);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPageListener#pageActivated(org.eclipse.ui.IWorkbenchPage)
		 */
		public void pageActivated(IWorkbenchPage page) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPageListener#pageClosed(org.eclipse.ui.IWorkbenchPage)
		 */
		public void pageClosed(IWorkbenchPage page) {
			page.removePartListener(this);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPageListener#pageOpened(org.eclipse.ui.IWorkbenchPage)
		 */
		public void pageOpened(IWorkbenchPage page) {
			page.addPartListener(this);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partActivated(IWorkbenchPartReference partRef) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			final IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				setInput((IEditorPart)part);
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partClosed(IWorkbenchPartReference partRef) {
			final IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				clearLocationMarker((IEditorPart)part);
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partDeactivated(IWorkbenchPartReference partRef) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partOpened(IWorkbenchPartReference partRef) {
			final IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				setInput((IEditorPart)part);
				createLocationMarker((IEditorPart)part);
			}
		}

		/**
		 * Sets input according to the given {@link IEditorPart}.
		 * 
		 * @param editorPart
		 *            the {@link IEditorPart}
		 */
		public void setInput(final IEditorPart editorPart) {
			final IEditorInput input = editorPart.getEditorInput();
			final IResource resource = (IResource)input.getAdapter(IFile.class);
			final IBase base = IdeMappingUtils.getCurentBase();
			if (base != null) {
				try {
					final ILocation location = MappingUtils.getConnectorRegistry().getOrCreateLocation(base,
							resource);
					for (Viewer viewer : viewers) {
						viewer.setInput(location);
					}
				} catch (InstantiationException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (IllegalAccessException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partHidden(IWorkbenchPartReference partRef) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partVisible(IWorkbenchPartReference partRef) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partInputChanged(IWorkbenchPartReference partRef) {
			final IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				setInput((IEditorPart)part);
				createLocationMarker((IEditorPart)part);
			}
		}

	}

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
	 * The {@link EditorPartListener}.
	 */
	private EditorPartListener editorPartListener;

	/**
	 * The {@link List} of edited {@link ILocation}.
	 */
	private final List<ILocation> editedLocations = new ArrayList<ILocation>();

	/**
	 * The listener maintaining the {@link ILocation} to {@link org.eclipse.core.resources.IMarker IMarker}
	 * mapping.
	 */
	private final MarkerBaseListener markerBaseListener = new MarkerBaseListener();

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

		final ComboViewer mappingBaseCombo = addMappingBaseCombo(headerComposite);

		TabFolder bodyTabFolder = new TabFolder(composite, SWT.NONE);
		bodyTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addSelectionTabItem(bodyTabFolder, mappingBaseCombo);
		addDocumentTabItem(bodyTabFolder, mappingBaseCombo);
		addReportTabItem(bodyTabFolder, mappingBaseCombo);

		mappingBaseCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
					for (IWorkbenchPage page : window.getPages()) {
						for (IEditorReference editorRef : page.getEditorReferences()) {
							final IEditorPart editorPart = editorRef.getEditor(false);
							if (editorPart != null) {
								clearLocationMarker(editorPart);
								createLocationMarker(editorPart);
							}
						}
					}
				}
			}

		});

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
				final IBase oldBase = IdeMappingUtils.getCurentBase();
				final IBase newBase = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
				if (oldBase != null) {
					for (ILocation child : oldBase.getContents()) {
						deleteLocation(child);
					}
				}
				IdeMappingUtils.setCurrentBase(newBase);
				for (ILocation child : newBase.getContents()) {
					addLocation(child);
				}
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
		referencingTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencingTree.getViewer().getTree()));
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
		referencedTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencedTree.getViewer().getTree()));
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

		referencingTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencingTree.getViewer().getTree()));
		referencedTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencedTree.getViewer().getTree()));

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
		referencingTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencingTree.getViewer().getTree()));

		referencingTree.getViewer().getTree().setHeaderVisible(true);
		TreeViewerColumn referencingTreeSourceColumn = new TreeViewerColumn(referencingTree.getViewer(),
				SWT.NONE);
		referencingTreeSourceColumn.getColumn().setData(LinkedLocationContentProvider.SOURCE);
		referencingTreeSourceColumn.getColumn().setResizable(true);
		referencingTreeSourceColumn.getColumn().setText(SOURCE_LABEL);
		referencingTreeSourceColumn.getColumn().setWidth(WIDTH);
		referencingTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MappingLabelProvider(MappingLabelProvider.SOURCE)));

		TreeViewerColumn referencingTreeTargetColumn = new TreeViewerColumn(referencingTree.getViewer(),
				SWT.NONE);
		referencingTreeTargetColumn.getColumn().setData(LinkedLocationContentProvider.TARGET);
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
		referencedTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(referencedTree.getViewer().getTree()));

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
				final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActiveEditor();
				if (activeEditor != null && editorPartListener != null) {
					editorPartListener.setInput(activeEditor);
				}
			}
		});

		editorPartListener = new EditorPartListener(referencingTree.getViewer(), referencedTree.getViewer());
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				final IEditorPart activeEditor = page.getActiveEditor();
				if (activeEditor != null) {
					editorPartListener.setInput(activeEditor);
				}
				page.addPartListener(editorPartListener);
			}
			window.addPageListener(editorPartListener);
		}
		PlatformUI.getWorkbench().addWindowListener(editorPartListener);

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

		final FilteredTree reportTree = new FilteredTree(treeComposite, SWT.BORDER, new PatternFilter(),
				false);
		reportTree.getViewer().getTree().addListener(SWT.MouseDoubleClick,
				new ShowLocationDoubleClickListener(reportTree.getViewer().getTree()));
		reportTree.getViewer().setContentProvider(new SyncronizationLocationContentProvider());

		reportTree.getViewer().getTree().setHeaderVisible(true);
		TreeViewerColumn linkTreeSourceColumn = new TreeViewerColumn(reportTree.getViewer(), SWT.NONE);
		linkTreeSourceColumn.getColumn().setResizable(true);
		linkTreeSourceColumn.getColumn().setText(SOURCE_LABEL);
		linkTreeSourceColumn.getColumn().setWidth(WIDTH);
		linkTreeSourceColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new MappingLabelProvider(
				MappingLabelProvider.SOURCE)));

		TreeViewerColumn linkTreeTargetColumn = new TreeViewerColumn(reportTree.getViewer(), SWT.NONE);
		linkTreeTargetColumn.getColumn().setResizable(true);
		linkTreeTargetColumn.getColumn().setText(TARGET_LABEL);
		linkTreeTargetColumn.getColumn().setWidth(WIDTH);
		linkTreeTargetColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new MappingLabelProvider(
				MappingLabelProvider.TARGET)));

		reportTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(reportTree.getViewer());
			}
		});

		mappingBaseCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				final IBase base = (IBase)((IStructuredSelection)event.getSelection()).getFirstElement();
				reportTree.getViewer().setInput(base);
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

		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				page.removePartListener(editorPartListener);
			}
			window.removePageListener(editorPartListener);
		}
		PlatformUI.getWorkbench().removeWindowListener(editorPartListener);

		IdeMappingUtils.setCurrentBase(null);
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				for (IEditorReference editorRef : page.getEditorReferences()) {
					final IEditorPart editorPart = editorRef.getEditor(false);
					if (editorPart != null) {
						clearLocationMarker(editorPart);
					}
				}
			}
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
	 * @return <code>true</code> if the two given {@link IBase} are the same, <code>false</code> otherwise
	 */
	private boolean areSameBase(IBase firstBase, IBase secondBase) {
		// TODO change this when we work on same instances of IBase
		return firstBase.getName().equals(secondBase.getName());
	}

	/**
	 * Clears all location markers for the given {@link IEditorPart}.
	 * 
	 * @param part
	 *            the {@link IEditorPart}
	 */
	private void clearLocationMarker(IEditorPart part) {
		final IEditorInput editorInput = part.getEditorInput();

		if (editorInput != null) {
			final IFile file = getFile(editorInput);
			final IBase currentBase = IdeMappingUtils.getCurentBase();
			if (file != null && currentBase != null) {
				try {
					file.deleteMarkers(ILocationMarker.LOCATION_ID, true, IResource.DEPTH_INFINITE);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"unable to clear all location markers for "
											+ file.getFullPath().toString(), e));
				}
				final ILocation fileLocation = MappingUtils.getConnectorRegistry().getLocation(currentBase,
						file);
				if (fileLocation != null) {
					editedLocations.remove(fileLocation);
					removeLocation(fileLocation);
				}
			}
		}
	}

	/**
	 * Removes {@link ILocation} to {@link org.eclipse.core.resources.IMarker IMarker} mapping recursively.
	 * 
	 * @param location
	 *            the {@link ILocation} to remove from the mapping
	 */
	private void removeLocation(ILocation location) {
		IdeMappingUtils.removeMarker(location);
		location.removeListener(markerBaseListener);
		for (ILocation child : location.getContents()) {
			removeLocation(child);
		}
	}

	/**
	 * Removes {@link ILocation} to {@link org.eclipse.core.resources.IMarker IMarker} mapping recursively and
	 * {@link org.eclipse.core.resources.IMarker#delete() deletes} the
	 * {@link org.eclipse.core.resources.IMarker IMarker}.
	 * 
	 * @param location
	 *            the {@link ILocation} to remove from the mapping
	 */
	private void deleteLocation(ILocation location) {
		IdeMappingUtils.deleteMarker(location);
		location.removeListener(markerBaseListener);
		for (ILocation child : location.getContents()) {
			removeLocation(child);
		}
	}

	/**
	 * Gets the {@link IFile} from the given {@link IEditorInput}.
	 * 
	 * @param editorInput
	 *            the {@link IEditorInput}
	 * @return the {@link IFile} from the given {@link IEditorInput} if any, <code>null</code> otherwise
	 */
	private IFile getFile(final IEditorInput editorInput) {
		final IFile file;
		if (editorInput instanceof IFileEditorInput) {
			file = ((IFileEditorInput)editorInput).getFile();
		} else {
			final IPath path = getPath(editorInput);
			if (path != null) {
				file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
			} else {
				file = null;
			}
		}
		return file;
	}

	/**
	 * Gets the {@link IPath} from the given {@link IEditorInput}.
	 * 
	 * @param editorInput
	 *            the {@link IEditorInput}
	 * @return the {@link IPath} from the given {@link IEditorInput} if any, <code>null</code> otherwise
	 */
	private IPath getPath(final IEditorInput editorInput) {
		final IPath path;
		if (editorInput instanceof ILocationProvider) {
			path = ((ILocationProvider)editorInput).getPath(editorInput);
		} else if (editorInput instanceof IURIEditorInput) {
			final URI uri = ((IURIEditorInput)editorInput).getURI();
			if (uri != null) {
				final File osFile = URIUtil.toFile(uri);
				if (osFile != null) {
					path = Path.fromOSString(osFile.getAbsolutePath());
				} else {
					path = null;
				}
			} else {
				path = null;
			}
		} else {
			path = null;
		}
		return path;
	}

	/**
	 * Creates markers for the given {@link ILocation} and its {@link ILocation#getContents() contained}
	 * {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 */
	private void addLocation(ILocation location) {
		IdeMappingUtils.getOrCreateMarker(location);
		location.addListener(markerBaseListener);
		for (ILocation child : location.getContents()) {
			addLocation(child);
		}
	}

	/**
	 * Creates all location markers for the given {@link IEditorPart}.
	 * 
	 * @param part
	 *            the {@link IEditorPart}
	 */
	private void createLocationMarker(IEditorPart part) {
		final IEditorInput editorInput = part.getEditorInput();

		if (editorInput != null) {
			final IFile file = getFile(editorInput);
			final IBase currentBase = IdeMappingUtils.getCurentBase();
			if (file != null && currentBase != null) {
				final ILocation fileLocation = MappingUtils.getConnectorRegistry().getLocation(currentBase,
						file);
				if (fileLocation != null) {
					editedLocations.add(fileLocation);
					addLocation(fileLocation);
				}
			}
		}
	}

}
