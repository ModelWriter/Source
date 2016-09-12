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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.adapter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.UiIdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Adapts {@link ITextSelection} to {@link ITextLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextSelectionToTextLocationAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		ITextLocation res;

		final IBase currentBase = IdeMappingUtils.getCurentBase();
		if (currentBase != null) {
			if (adaptableObject instanceof ITextSelection) {
				final ITextSelection selection = (ITextSelection)adaptableObject;
				final IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActivePart();
				if (activePart instanceof IEditorPart) {
					try {
						final IEditorInput input = ((IEditorPart)activePart).getEditorInput();
						final IFile file = UiIdeMappingUtils.getFile(input);
						// TODO we implicitly decide to have a flat structure of location here... we probably
						// don't want to do that
						final ILocation container = MappingUtils.getConnectorRegistry().getOrCreateLocation(
								currentBase, file);
						final int start = selection.getOffset();
						final Integer end = start + selection.getLength();
						final TextRegion region = new TextRegion(start, end);
						res = (ITextLocation)MappingUtils.getConnectorRegistry().getOrCreateLocation(
								container, region);
					} catch (InstantiationException e) {
						res = null;
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					} catch (IllegalAccessException e) {
						res = null;
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					} catch (ClassNotFoundException e) {
						res = null;
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
				} else {
					res = null;
				}
			} else {
				res = null;
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class<?>[] getAdapterList() {
		return new Class[] {ILocation.class };
	}

}
