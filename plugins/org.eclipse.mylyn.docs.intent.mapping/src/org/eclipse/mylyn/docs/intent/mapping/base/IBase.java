/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

import java.io.IOException;
import java.util.List;

/**
 * A base stores root {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IBase extends IMappingElement, ILocationContainer {

	/**
	 * Sets the {@link IBase#getName() name} of the {@link IBase}.
	 * 
	 * @param name
	 *            the name of the IBase
	 */
	void setName(String name);

	/**
	 * Gets the name of the {@link IBase}.
	 * 
	 * @return the name of the {@link IBase}
	 */
	String getName();

	/**
	 * Adds a {@link IBaseListener}.
	 * 
	 * @param listener
	 *            the {@link IBaseListener} to add
	 */
	void addListener(IBaseListener listener);

	/**
	 * Removes a {@link IBaseListener}.
	 * 
	 * @param listener
	 *            the {@link IBaseListener} to remove
	 */
	void removeListener(IBaseListener listener);

	/**
	 * Gets the {@link BaseElementFactory} use to create instances of {@link ILocation} for this {@link IBase}
	 * .
	 * 
	 * @return the {@link BaseElementFactory} use to create instances of {@link ILocation} for this
	 *         {@link IBase}
	 */
	BaseElementFactory getFactory();

	/**
	 * Gets the {@link List} of {@link IReport}.
	 * 
	 * @return the {@link List} of {@link IReport}
	 */
	List<IReport> getReports();

	/**
	 * Saves the base.
	 * 
	 * @throws IOException
	 *             if serialization went wrong
	 */
	void save() throws IOException;

}
