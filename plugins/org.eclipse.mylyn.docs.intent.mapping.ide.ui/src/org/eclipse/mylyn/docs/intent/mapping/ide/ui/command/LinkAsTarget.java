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

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * Links the selection as {@link org.eclipse.mylyn.docs.intent.mapping.base.ILink#getTarget() target} of the
 * active synchronization palette.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LinkAsTarget extends AbstractLocationHandler {

	@Override
	protected void handleLocationDescriptor(ILocationDescriptor targetDescriptor) {
		final ILocation target = createLocation(targetDescriptor, UNABLE_TO_CREATE_TARGET_LOCATION);
		targetDescriptor.dispose();
		if (target != null) {
			for (ILocationDescriptor sourceDescriptor : IdeMappingUtils.getSynchronizationPalette()) {
				if (IdeMappingUtils.isActive(sourceDescriptor)) {
					final ILocation source = createLocation(sourceDescriptor,
							UNABLE_TO_CREATE_SOURCE_LOCATION);
					if (!source.equals(target) && MappingUtils.getLink(source, target) == null) {
						createLink(source, target);
					}
				}
			}
		}
	}

	@Override
	protected boolean canHandleLocation(ILocationDescriptor targetDescriptor) {
		boolean res = false;

		if (targetDescriptor.exists()) {
			for (ILocationDescriptor sourceDescriptor : IdeMappingUtils.getSynchronizationPalette()) {
				if (IdeMappingUtils.isActive(sourceDescriptor)) {
					if (MappingUtils.canCreateLink(sourceDescriptor, targetDescriptor)) {
						res = true;
						break;
					}
				}
			}
		} else {
			res = IdeMappingUtils.asActiveLocationDescriptor();
		}

		return res;
	}
}
