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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.UiIdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Adapts {@link ITextSelection} to {@link ILocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextSelectionToTextLocationDescriptorAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public ILocationDescriptor getAdapter(Object adaptableObject,
			@SuppressWarnings("rawtypes") Class adapterType) {
		ILocationDescriptor res;

		final IBase currentBase = IdeMappingUtils.getCurentBase();
		if (currentBase != null) {
			if (adaptableObject instanceof ITextSelection) {
				final ITextSelection selection = (ITextSelection)adaptableObject;
				final IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActivePart();
				if (activePart instanceof IEditorPart) {
					final IEditorInput input = ((IEditorPart)activePart).getEditorInput();
					final IFile file = UiIdeMappingUtils.getFile(input);
					try {
						final String content = MappingUtils.getContent((int)file.getLocation().toFile()
								.length(), file.getContents());
						// TODO we implicitly decide to have a flat structure of location here... we probably
						// don't want to do that
						final ILocationDescriptor containerDescriptor = MappingUtils.getConnectorRegistry()
								.getLocationDescriptor(null, file);
						final int start = selection.getOffset();
						final Integer end = start + selection.getLength();
						final TextRegion region = new TextRegion(content.substring(start, end), start, end);
						res = MappingUtils.getConnectorRegistry().getLocationDescriptor(containerDescriptor,
								region);
					} catch (IOException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, "can't read content "
										+ file.getLocation().toString(), e));
						res = null;
					} catch (CoreException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, "can't read content "
										+ file.getLocation().toString(), e));
						res = null;
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
		return new Class[] {ILocationDescriptor.class };
	}

}
