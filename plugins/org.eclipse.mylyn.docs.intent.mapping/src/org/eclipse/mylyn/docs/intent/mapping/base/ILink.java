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

import java.io.Serializable;
import java.util.List;

/**
 * Connect two {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILink extends IMappingElement, IBaseElement {

	/**
	 * Sets the {@link ILink#getDescription(String) description} of the {@link ILink}.
	 * 
	 * @param description
	 *            the {@link ILink#getDescription(String) description}
	 */
	void setDescription(String description);

	/**
	 * Gets the description of the {@link ILink}.
	 * 
	 * @return the description of the {@link ILink}
	 */
	String getDescription();

	/**
	 * Sets the {@link ILink#getSource() source} {@link ILocation} of the {@link ILink}.
	 * 
	 * @param location
	 *            the {@link ILink#getSource() source} {@link ILocation}
	 */
	void setSource(ILocation location);

	/**
	 * Gets the source {@link ILocation}.
	 * 
	 * @return the source {@link ILocation}s
	 */
	ILocation getSource();

	/**
	 * Sets the {@link ILink#getTarget() target} {@link ILocation} of the {@link ILink}.
	 * 
	 * @param location
	 *            the {@link ILink#getTarget() target} {@link ILocation}
	 */
	void setTarget(ILocation location);

	/**
	 * Gets the target {@link ILocation} if the {@link ILink}.
	 * 
	 * @return the target {@link ILocation} if the {@link ILink}
	 */
	ILocation getTarget();

	/**
	 * Adds a {@link ILinkListener}.
	 * 
	 * @param listener
	 *            the {@link ILinkListener} to add
	 */
	void addListener(ILinkListener listener);

	/**
	 * Removes a {@link ILinkListener}.
	 * 
	 * @param listener
	 *            the {@link ILinkListener} to remove
	 */
	void removeListener(ILinkListener listener);

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	Serializable getType();

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type
	 */
	void setType(Serializable type);

	/**
	 * Gets the {@link List} of {@link IReport} for this {@link ILink}.
	 * 
	 * @return the {@link List} of {@link IReport} for this {@link ILink}
	 */
	List<IReport> getReports();

}
