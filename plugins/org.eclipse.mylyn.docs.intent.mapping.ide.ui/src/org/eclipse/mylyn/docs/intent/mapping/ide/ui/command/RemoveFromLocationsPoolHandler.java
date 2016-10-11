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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * Removes selected {@link ILocationDescriptor} from the {@link IdeMappingUtils#getLocationsPool() location
 * descriptors pool}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RemoveFromLocationsPoolHandler extends AbstractLocationHandler {

	@Override
	protected void handleLocationDescriptor(ILocationDescriptor locationDescriptor) {
		IdeMappingUtils.removeLocationFromPool(locationDescriptor);
	}

	@Override
	protected boolean canHandleLocation(ILocationDescriptor locationDescriptor) {
		return IdeMappingUtils.getLocationsPool().contains(locationDescriptor);
	}

}
