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
package eu.modelwriter.ide.ui.command;

import eu.modelwriter.ide.ui.Activator;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * Extract text from a office document.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ExtractTextHandler extends AbstractHandler {

	/**
	 * Error message.
	 */
	private static final String UNABLE_TO_EXTRACT_TEXT_FROM = "unable to extract text from: ";

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			for (Object selected : ((IStructuredSelection)selection).toList()) {
				if (selected instanceof IFile) {
					final IFile file = (IFile)selected;
					if ("docx".equals(file.getFullPath().getFileExtension())) {
						exctractDocx(file);
					} else if ("doc".equals(file.getFullPath().getFileExtension())) {
						exctractDoc(file);
					}
				}
			}
		}

		return null;
	}

	/**
	 * Extracts text from the given .docx {@link IFile}.
	 * 
	 * @param file
	 *            the .docx {@link IFile}
	 */
	private void exctractDocx(final IFile file) {
		try {
			FileInputStream fis = new FileInputStream(file.getLocation().toFile());
			XWPFDocument docx = new XWPFDocument(fis);
			XWPFWordExtractor we = new XWPFWordExtractor(docx);
			final IPath textPath = file.getFullPath().removeFileExtension().addFileExtension("txt");
			final IFile textFile = ResourcesPlugin.getWorkspace().getRoot().getFile(textPath);
			if (textFile.exists()) {
				textFile.delete(true, new NullProgressMonitor());
			}
			textFile.create(new ByteArrayInputStream(we.getText().getBytes()), true,
					new NullProgressMonitor());
			we.close();
			docx.close();
			fis.close();
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_EXTRACT_TEXT_FROM + file.getFullPath(), e));
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_EXTRACT_TEXT_FROM + file.getFullPath(), e));
		}
	}

	/**
	 * Extracts text from the given .doc {@link IFile}.
	 * 
	 * @param file
	 *            the .doc {@link IFile}
	 */
	private void exctractDoc(final IFile file) {
		try {
			FileInputStream fis = new FileInputStream(file.getLocation().toFile());
			HWPFDocument doc = new HWPFDocument(fis);
			WordExtractor we = new WordExtractor(doc);
			final IPath textPath = file.getFullPath().removeFileExtension().addFileExtension("txt");
			final IFile textFile = ResourcesPlugin.getWorkspace().getRoot().getFile(textPath);
			if (textFile.exists()) {
				textFile.delete(true, new NullProgressMonitor());
			}
			textFile.create(new ByteArrayInputStream(we.getText().getBytes()), true,
					new NullProgressMonitor());
			we.close();
			fis.close();
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_EXTRACT_TEXT_FROM + file.getFullPath(), e));
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_EXTRACT_TEXT_FROM + file.getFullPath(), e));
		}
	}

}
