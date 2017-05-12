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
import eu.modelwriter.semantic.ISemanticAnnotator;
import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.SemanticUtils;
import eu.modelwriter.semantic.ide.ISemanticAnnotationMarker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * The semantic {@link ViewPart}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticView extends ViewPart {

	/**
	 * Annotation {@link Job}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class AnnotationJob extends Job {
		/**
		 * The text to annotate.
		 */
		private final String text;

		/**
		 * The current {@link IEditorPart}.
		 */
		private final IEditorPart editorPart;

		/**
		 * Constructor.
		 * 
		 * @param text
		 *            the text to annotate
		 * @param editorPart
		 *            the current {@link IEditorPart}
		 */
		private AnnotationJob(String text, IEditorPart editorPart) {
			super("Semantic annotation");
			this.text = text;
			this.editorPart = editorPart;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final Map<Object, Map<Object, Set<int[]>>> annotations = getAnnotations(text);

			final IFile file = (IFile)editorPart.getEditorInput().getAdapter(IFile.class);
			try {
				file.deleteMarkers(ISemanticAnnotationMarker.SEMANTIC_ANNOTATION_ID, true,
						IResource.DEPTH_INFINITE);
				for (Entry<Object, Map<Object, Set<int[]>>> conceptEntry : annotations.entrySet()) {
					final Object concept = conceptEntry.getKey();
					for (Entry<Object, Set<int[]>> similarityEntry : conceptEntry.getValue().entrySet()) {
						final Object similarity = similarityEntry.getKey();
						for (int[] positions : similarityEntry.getValue()) {
							final IMarker marker = file.createMarker(
									ISemanticAnnotationMarker.TEXT_SEMANTIC_ANNOTATION_ID);
							marker.setAttribute(ISemanticAnnotationMarker.SEMANTIC_CONCEPT_ATTRIBUTE,
									concept);
							marker.setAttribute(ISemanticAnnotationMarker.SEMANTIC_SIMILARITY_ATTRIBUTE,
									similarity);
							marker.setAttribute(IMarker.MESSAGE, concept + "\n" + similarity);
							marker.setAttribute(IMarker.CHAR_START, positions[0]);
							marker.setAttribute(IMarker.CHAR_END, positions[1]);
						}
					}
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
						.getMessage(), e));
			}

			return Status.OK_STATUS;
		}
	}

	/**
	 * Listen to {@link org.eclipse.ui.IEditorPart IEditorPart}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class EditorPartListener implements IWindowListener, IPageListener, IPartListener2 {

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
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
		 */
		public void partClosed(IWorkbenchPartReference partRef) {
			// nothing to do here
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
				annotate((IEditorPart)part);
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
				annotate((IEditorPart)part);
			}
		}

	}

	/**
	 * The view ID.
	 */
	public static final String ID = "eu.modelwriter.semantic.ide.ui.view.SemanticView"; //$NON-NLS-1$

	/**
	 * The {@link Set} of current selected {@link IBase}.
	 */
	private Set<IBase> selectedBases = new LinkedHashSet<IBase>();

	/**
	 * The {@link ISemanticAnnotator}.
	 */
	private final ISemanticAnnotator annotator = SemanticUtils.getSemanticAnnotator();

	/**
	 * The {@link SelectionProviderIntermediate} delegating to {@link org.eclipse.jface.viewers.Viewer Viewer}
	 * .
	 */
	private final SelectionProviderIntermediate selectionProvider = new SelectionProviderIntermediate();

	/**
	 * Tells if we can use the cached call to the {@link ISemanticAnnotator}.
	 */
	private boolean useAnnotatorCache;

	/**
	 * The {@link EditorPartListener} updating annotations.
	 */
	private EditorPartListener editorPartListener;

	/**
	 * The {@link MenuManager}.
	 */
	private final MenuManager menuManager = new MenuManager();

	/**
	 * {@link List} of created {@link Menu}.
	 */
	private final List<Menu> menus = new ArrayList<Menu>();

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

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		addSemanticBaseTabItem(tabFolder);
		addSemanticProviderTabItem(tabFolder);
		addSemanticSimilarityTabItem(tabFolder);

		createActions();
		initializeToolBar();

		editorPartListener = new EditorPartListener();
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				final IEditorPart activeEditor = page.getActiveEditor();
				if (activeEditor != null) {
					annotate(activeEditor);
				}
				page.addPartListener(editorPartListener);
			}
			window.addPageListener(editorPartListener);
		}
		PlatformUI.getWorkbench().addWindowListener(editorPartListener);

		getSite().setSelectionProvider(selectionProvider);

		getSite().registerContextMenu(menuManager, selectionProvider);
	}

	/**
	 * Adds the semantic base {@link TabItem} to the given {@link TabFolder}.
	 * 
	 * @param tabFolder
	 *            the {@link TabFolder}
	 */
	private void addSemanticBaseTabItem(TabFolder tabFolder) {
		final TabItem semanticBaseTabItem = new TabItem(tabFolder, SWT.NONE);
		semanticBaseTabItem.setText("Base");

		final FilteredTree semanticBaseTree = new FilteredTree(tabFolder, SWT.CHECK | SWT.BORDER,
				new PatternFilter(), false);
		semanticBaseTabItem.setControl(semanticBaseTree);
		semanticBaseTree.getViewer().setContentProvider(new SemanticBaseRegistryContentProvider());
		semanticBaseTree.getViewer().setLabelProvider(new SemanticBaseLabelProvider());
		semanticBaseTree.getViewer().setInput(SemanticUtils.getSemanticBaseRegistry());
		semanticBaseTree.getViewer().getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (((TreeItem)event.item).getChecked()) {
						selectedBases.add((IBase)event.item.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					} else {
						selectedBases.remove((IBase)event.item.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					}
				}
			}
		});

		semanticBaseTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(semanticBaseTree.getViewer());
			}
		});

		final Menu menu = menuManager.createContextMenu(semanticBaseTree.getViewer().getControl());
		menus.add(menu);
		semanticBaseTree.getViewer().getControl().setMenu(menu);
	}

	/**
	 * Adds the semantic provider {@link TabItem} to the given {@link TabFolder}.
	 * 
	 * @param tabFolder
	 *            the {@link TabFolder}
	 */
	private void addSemanticProviderTabItem(TabFolder tabFolder) {
		final TabItem semanticProviderTabItem = new TabItem(tabFolder, SWT.NONE);
		semanticProviderTabItem.setText("Provider");

		final FilteredTree semanticProviderTree = new FilteredTree(tabFolder, SWT.CHECK | SWT.BORDER,
				new PatternFilter(), false);
		semanticProviderTabItem.setControl(semanticProviderTree);
		semanticProviderTree.getViewer().setContentProvider(new SemanticProviderRegistryContentProvider());
		semanticProviderTree.getViewer().setLabelProvider(new SemanticProviderLabelProvider());
		semanticProviderTree.getViewer().setInput(SemanticUtils.getSemanticProviderRegistry());
		semanticProviderTree.getViewer().getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (((TreeItem)event.item).getChecked()) {
						annotator.addSemanticProvider((ISemanticProvider)event.item.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					} else {
						annotator.removeSemanticProvider((ISemanticProvider)event.item.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					}
				}
			}
		});

		semanticProviderTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(semanticProviderTree.getViewer());
			}
		});

		final Menu menu = menuManager.createContextMenu(semanticProviderTree.getViewer().getControl());
		menus.add(menu);
		semanticProviderTree.getViewer().getControl().setMenu(menu);
	}

	/**
	 * Adds the semantic similarity provider {@link TabItem} to the given {@link TabFolder}.
	 * 
	 * @param tabFolder
	 *            the {@link TabFolder}
	 */
	private void addSemanticSimilarityTabItem(TabFolder tabFolder) {
		final TabItem semanticSimilarityProviderTabItem = new TabItem(tabFolder, SWT.NONE);
		semanticSimilarityProviderTabItem.setText("Similarity Provider");

		final FilteredTree semanticSimilarityProviderTree = new FilteredTree(tabFolder, SWT.CHECK
				| SWT.BORDER, new PatternFilter(), false);
		semanticSimilarityProviderTabItem.setControl(semanticSimilarityProviderTree);
		semanticSimilarityProviderTree.getViewer().setContentProvider(
				new SemanticSimilarityProviderRegistryContentProvider());
		semanticSimilarityProviderTree.getViewer().setLabelProvider(
				new SemanticSimilarityProviderLabelProvider());
		semanticSimilarityProviderTree.getViewer().setInput(SemanticUtils
				.getSemanticSimilarityProviderRegistry());
		semanticSimilarityProviderTree.getViewer().getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (((TreeItem)event.item).getChecked()) {
						annotator.addSemanticSimilarityProvider((ISemanticSimilarityProvider)event.item
								.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					} else {
						annotator.removeSemanticSimilarityProvider((ISemanticSimilarityProvider)event.item
								.getData());
						useAnnotatorCache = false;
						annotateCurrentEditor();
					}
				}
			}
		});

		semanticSimilarityProviderTree.getViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(null);
			}

			public void focusGained(FocusEvent e) {
				selectionProvider.setSelectionProviderDelegate(semanticSimilarityProviderTree.getViewer());
			}
		});

		final Menu menu = menuManager.createContextMenu(semanticSimilarityProviderTree.getViewer()
				.getControl());
		menus.add(menu);
		semanticSimilarityProviderTree.getViewer().getControl().setMenu(menu);
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

	@Override
	public void dispose() {
		super.dispose();

		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				page.removePartListener(editorPartListener);
			}
			window.removePageListener(editorPartListener);
		}
		PlatformUI.getWorkbench().removeWindowListener(editorPartListener);

		getSite().setSelectionProvider(null);

		for (Menu menu : menus) {
			menu.dispose();
		}
		menuManager.dispose();
	}

	/**
	 * Annotate the given {@link IEditorPart}.
	 * 
	 * @param editorPart
	 *            the {@link IEditorPart}
	 */
	private void annotate(final IEditorPart editorPart) {
		final ITextEditor editor = (ITextEditor)editorPart.getAdapter(ITextEditor.class);
		if (editor != null) {
			final IDocumentProvider provider = editor.getDocumentProvider();
			final IDocument document = provider.getDocument(editor.getEditorInput());
			if (document != null) {
				final String text = document.get();
				if (text != null) {
					final Job annotationJob = new AnnotationJob(text, editorPart);
					annotationJob.schedule();
				}
			}
		}
	}

	/**
	 * Gets annotations for the given text.
	 * 
	 * @param text
	 *            the text
	 * @return the annotation mapping
	 */
	private Map<Object, Map<Object, Set<int[]>>> getAnnotations(final String text) {
		final Map<Object, Map<Object, Set<int[]>>> annotations;
		if (useAnnotatorCache) {
			annotations = annotator.getSemanticAnnotations(text);
		} else {
			final Set<Object> concepts = new LinkedHashSet<Object>();
			for (IBase base : selectedBases) {
				concepts.addAll(base.getConcepts());
			}
			annotations = annotator.getSemanticAnnotations(text, concepts);
			useAnnotatorCache = true;
		}
		return annotations;
	}

	/**
	 * Annotates the current editor.
	 */
	private void annotateCurrentEditor() {
		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (activeEditor != null) {
			annotate(activeEditor);
		}
	}

}
