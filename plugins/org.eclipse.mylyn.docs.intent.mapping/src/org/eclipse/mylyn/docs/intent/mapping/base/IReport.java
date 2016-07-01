/*******************************************************************************
 * Copyright (c) 2016 Ober.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

/**
 * Reports a problem on a {@link ILink}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IReport extends IBaseElement {

	/**
	 * Sets the {@link ILink} this {@link IReport report} applies on.
	 * 
	 * @param link
	 *            the {@link ILink}
	 */
	void setLink(ILink link);

	/**
	 * Gets the {@link ILink} this {@link IReport report} applies on.
	 * 
	 * @return the {@link ILink} this {@link IReport report} applies on
	 */
	ILink getLink();

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description
	 */
	void setDescription(String description);

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	String getDescription();

	/**
	 * Adds a {@link IReportListener}.
	 * 
	 * @param listener
	 *            the {@link IReportListener} to add
	 */
	void addListener(IReportListener listener);

	/**
	 * Removes a {@link IReportListener}.
	 * 
	 * @param listener
	 *            the {@link IReportListener} to remove
	 */
	void removeListener(IReportListener listener);

}
