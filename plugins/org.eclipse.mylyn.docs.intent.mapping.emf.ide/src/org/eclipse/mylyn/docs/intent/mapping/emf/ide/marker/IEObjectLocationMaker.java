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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.marker;

import org.eclipse.emf.ecore.EValidator;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.Activator;

/**
 * Place holder for location marker constants.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectLocationMaker {

	/**
	 * The EObject location marker ID.
	 */
	String EOBJECT_LOCATION_ID = Activator.PLUGIN_ID + ".eobjectLocationMarker";

	/**
	 * The URI attribute.
	 */
	String URI_ATTRIBUTE = EValidator.URI_ATTRIBUTE;

}
