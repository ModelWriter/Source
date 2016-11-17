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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.ide.IDE;

/**
 * UI Ide mapping utilities.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class UiIdeMappingUtils {

	/**
	 * Constructor.
	 */
	private UiIdeMappingUtils() {
		// nothing to do here
	}

	/**
	 * Shows the given {@link ILocation} in an editor.
	 * 
	 * @param location
	 *            the {@link ILocation} to show
	 */
	public static void showLocation(ILocation location) {
		if (!location.isMarkedAsDeleted()) {
			final IMarker marker = IdeMappingUtils.getOrCreateMarker(location);
			if (marker != null) {
				showMarker(marker);
			} else {
				final IMarker createdMarker = IdeMappingUtils.getOrCreateMarker(location);
				if (createdMarker != null) {
					showMarker(createdMarker);
				}
				IdeMappingUtils.deleteMarker(location);
			}
		}
	}

	/**
	 * Shows the given {@link IMarker}.
	 * 
	 * @param marker
	 *            the {@link IMarker} to show
	 */
	private static void showMarker(final IMarker marker) {
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage();
		try {
			IDE.openEditor(activePage, marker, true);
		} catch (PartInitException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, "unable to open location marker: ", e));
		}
	}

	/**
	 * Gets the {@link IFile} from the given {@link IEditorInput}.
	 * 
	 * @param editorInput
	 *            the {@link IEditorInput}
	 * @return the {@link IFile} from the given {@link IEditorInput} if any, <code>null</code> otherwise
	 */
	public static IFile getFile(final IEditorInput editorInput) {
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
	public static IPath getPath(final IEditorInput editorInput) {
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

}
