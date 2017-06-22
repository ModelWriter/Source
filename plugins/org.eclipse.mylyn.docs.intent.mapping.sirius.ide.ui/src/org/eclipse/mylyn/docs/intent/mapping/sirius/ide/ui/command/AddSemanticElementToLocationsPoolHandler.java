/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.sirius.ide.ui.command;

import org.eclipse.mylyn.docs.intent.mapping.ide.ui.command.AddToLocationsPoolHandler;

/**
 * Adds selected semantic {@link org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor
 * ILocationDescriptor} to the
 * {@link org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils#getLocationsPool() locations pool}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class AddSemanticElementToLocationsPoolHandler extends AddToLocationsPoolHandler {

	@Override
	protected Object getElementFromSelectedObject(Object selected) {
		return SiriusMappingUtils.getSemanticElementFromSelectedObject(selected);
	}

}