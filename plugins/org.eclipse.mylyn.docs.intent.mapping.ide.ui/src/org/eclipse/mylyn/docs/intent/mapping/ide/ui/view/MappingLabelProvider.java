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

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.swt.graphics.Image;

/**
 * Mapping {@link ILabelProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingLabelProvider extends BaseLabelProvider implements ILabelProvider, IStyledLabelProvider {

	/**
	 * {@link ILocation#getSourceLinks() source} column.
	 */
	public static final int SOURCE = 0;

	/**
	 * {@link ILocation#getTargetLinks() target} column.
	 */
	public static final int TARGET = 1;

	/**
	 * The column either {@link #SOURCE} or {@link #TARGET}.
	 */
	private final int column;

	/**
	 * Constructor.
	 * 
	 * @param column
	 *            the column either {@link #SOURCE} or {@link #TARGET}
	 */
	public MappingLabelProvider(int column) {
		this.column = column;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		final String res;

		if (column == SOURCE) {
			if (element instanceof ILocation) {
				res = MappingUtils.getConnectorRegistry().getName((ILocation)element);
			} else if (element instanceof ILink) {
				res = MappingUtils.getConnectorRegistry().getName(((ILink)element).getSource());
			} else if (element instanceof IBase) {
				res = ((IBase)element).getName();
			} else {
				throw new IllegalStateException("should not happend");
			}
		} else if (column == TARGET) {
			if (element instanceof ILink) {
				res = MappingUtils.getConnectorRegistry().getName(((ILink)element).getTarget());
			} else {
				res = "";
			}
		} else {
			throw new IllegalStateException("should not happend");
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public StyledString getStyledText(Object element) {
		return new StyledString(getText(element));
	}
}
