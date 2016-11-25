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
package org.eclipse.mylyn.docs.intent.mapping.ide.ui.command;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A multi line text {@link Dialog}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MultiLineTextDialog extends Dialog {

	/**
	 * Initial {@link Dialog} size.
	 */
	private static final int Y = 300;

	/**
	 * Initial {@link Dialog} size.
	 */
	private static final int X = 450;

	/**
	 * The initial text.
	 */
	private String text;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 *            the parent {@link Shell}
	 * @param initialText
	 *            the initial text
	 */
	public MultiLineTextDialog(Shell parentShell, String initialText) {
		super(parentShell);
		this.text = initialText;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite)super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		final Text textText = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
				| SWT.MULTI);
		textText.setText(text);
		textText.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				text = textText.getText();
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(X, Y);
	}

	/**
	 * Gets the {@link Dialog} text.
	 * 
	 * @return the {@link Dialog} text
	 */
	public String getText() {
		return text;
	}

}
