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
package org.eclipse.mylyn.docs.intent.mapping.ide;

/**
 * Place holder for location marker constants.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILocationMarker {

	/**
	 * The location marker ID.
	 */
	String LOCATION_ID = Activator.PLUGIN_ID + ".semanticAnnotation";

	/**
	 * The text location marker ID.
	 */
	String TEXT_LOCATION_ID = Activator.PLUGIN_ID + ".textLocationMarker";

	/**
	 * The location attribute.
	 */
	String LOCATION_ATTRIBUTE = "location";

}
