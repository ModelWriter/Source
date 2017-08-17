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
package eu.modelwriter.ide.ui.menu;

import eu.modelwriter.ide.ui.Activator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerAnnotation;

/**
 * Contributes quick fixes to a menu.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class QuickFixContributionItem extends ContributionItem {

	/**
	 * Constructor.
	 */
	public QuickFixContributionItem() {
		// nothing to do here
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the contribution item identifier, or <code>null</code>
	 */
	public QuickFixContributionItem(String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {
		super.fill(menu, index);
		final List<IMarker> markers = getMarkersForCurrentLine();
		int i = index;

		final Map<IMarker, IMarkerResolution[]> resolutions = new HashMap<IMarker, IMarkerResolution[]>();
		for (final IMarker marker : markers) {
			final IMarkerResolution[] currentResolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
			if (currentResolutions.length != 0) {
				resolutions.put(marker, currentResolutions);
			}
		}

		if (!resolutions.isEmpty()) {
			for (final Entry<IMarker, IMarkerResolution[]> entry : resolutions.entrySet()) {
				final IMarker marker = entry.getKey();
				for (final IMarkerResolution resolution : entry.getValue()) {
					final MenuItem resolutionMenuItem = new MenuItem(menu, SWT.PUSH, i++);
					resolutionMenuItem.setText(resolution.getLabel());
					resolutionMenuItem.addListener(SWT.Selection, new Listener() {

						public void handleEvent(Event event) {
							resolution.run(marker);
						}
					});
					if (resolution instanceof IMarkerResolution2) {
						final Image resolutionImage = ((IMarkerResolution2)resolution).getImage();
						if (resolutionImage != null) {
							resolutionMenuItem.setImage(resolutionImage);
						}
						// TODO description ?
					}
				}
			}
			new MenuItem(menu, SWT.SEPARATOR, i++);
		}

	}

	/**
	 * Gets the {@link List} of {@link IMarker} for the current line.
	 * 
	 * @return the {@link List} of {@link IMarker}
	 */
	private List<IMarker> getMarkersForCurrentLine() {
		final List<IMarker> res = new ArrayList<IMarker>();

		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (activeEditor instanceof ITextEditor) {
			final IVerticalRulerInfo rulerInfo = (IVerticalRulerInfo)activeEditor.getAdapter(
					IVerticalRulerInfo.class);

			// TODO check if the last right click has been done on the ruler
			final IResource resource = getResource(activeEditor);
			if (resource != null) {
				res.addAll(getMarkers((ITextEditor)activeEditor, rulerInfo));
			}
		}

		return res;
	}

	/**
	 * Gets the {@link List} of {@link IMarker} for the current line of the given {@link ITextEditor}.
	 * 
	 * @param editor
	 *            the {@link ITextEditor}
	 * @param rulerInfo
	 *            the {@link IVerticalRulerInfo}
	 * @return the {@link List} of {@link IMarker} for the current line of the given {@link ITextEditor}
	 */
	protected List<IMarker> getMarkers(final ITextEditor editor, final IVerticalRulerInfo rulerInfo) {
		final List<IMarker> res = new ArrayList<IMarker>();

		final IDocument document = (IDocument)editor.getDocumentProvider().getDocument(editor
				.getEditorInput());
		if (document != null) {
			final AbstractMarkerAnnotationModel model = getAnnotationModel(editor);
			if (model != null) {
				final int activeLine = rulerInfo.getLineOfLastMouseButtonActivity();
				res.addAll(getMarkers(document, model, activeLine));
			}
		}

		return res;
	}

	/**
	 * Gets the {@link List} of {@link IMarker}.
	 * 
	 * @param document
	 *            the {@link IDocument}
	 * @param model
	 *            the {@link AbstractMarkerAnnotationModel}
	 * @param lineNumber
	 *            the line number
	 * @return the {@link List} of {@link IMarker}
	 */
	private List<IMarker> getMarkers(final IDocument document, final AbstractMarkerAnnotationModel model,
			final int lineNumber) {
		final List<IMarker> res = new ArrayList<IMarker>();

		// TODO use the selection for non text editor
		IRegion line;
		try {
			line = document.getLineInformation(lineNumber);
		} catch (BadLocationException e) {
			line = null;
		}
		if (line != null) {
			final Iterator<Annotation> it = model.getAnnotationIterator(line.getOffset(), line.getLength()
					+ 1, true, true);
			while (it.hasNext()) {
				final Annotation annotation = it.next();
				final Position position = model.getPosition(annotation);
				try {
					if (position != null && document.getLineOfOffset(position.getOffset()) == lineNumber
							&& annotation instanceof MarkerAnnotation) {
						res.add(((MarkerAnnotation)annotation).getMarker());
					}
				} catch (BadLocationException e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"Unable to get annotations for the given position", e));
				}
			}
		}

		return res;
	}

	/**
	 * Gets the {@link IResource} from the given {@link IEditorPart}.
	 * 
	 * @param editor
	 *            the {@link IEditorPart}
	 * @return the {@link IResource} from the given {@link IEditorPart}
	 */
	protected final IResource getResource(IEditorPart editor) {
		final IResource res;

		final IEditorInput input = editor.getEditorInput();
		final IResource resource = (IResource)input.getAdapter(IFile.class);
		if (resource != null) {
			res = resource;
		} else {
			res = (IResource)input.getAdapter(IResource.class);
		}

		return res;
	}

	/**
	 * Gets the annotation model for the given {@link ITextEditor}.
	 * 
	 * @param editor
	 *            the {@link ITextEditor}
	 * @return the annotation model for the given {@link ITextEditor}
	 */
	private AbstractMarkerAnnotationModel getAnnotationModel(ITextEditor editor) {
		final AbstractMarkerAnnotationModel res;

		final IDocumentProvider provider = editor.getDocumentProvider();
		final IAnnotationModel model = provider.getAnnotationModel(editor.getEditorInput());
		if (model instanceof AbstractMarkerAnnotationModel) {
			res = (AbstractMarkerAnnotationModel)model;
		} else {
			res = null;
		}

		return res;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean isGroupMarker() {
		return true;
	}

}
