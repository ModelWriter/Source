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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
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
		final IMarker marker = IdeMappingUtils.getOrCreateMarker(location);
		if (marker != null) {
			final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage();
			try {
				IDE.openEditor(activePage, marker, true);
			} catch (PartInitException e) {
				Activator.getDefault().getLog()
						.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"unable to open location marker: ", e));
			}
		}
	}

}
