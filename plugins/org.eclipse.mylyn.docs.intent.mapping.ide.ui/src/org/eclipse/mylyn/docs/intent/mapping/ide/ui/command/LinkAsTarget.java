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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;

/**
 * Links the selection as {@link org.eclipse.mylyn.docs.intent.mapping.base.ILink#getTarget() target} of the
 * active locations pool.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LinkAsTarget extends AbstractLocationHandler {

	/**
	 * Unable to create link message.
	 */
	private static final String UNABLE_TO_CREATE_LINK = "unable to create link";

	@Override
	protected void handleLocation(ILocation target) {
		for (ILocation source : IdeMappingUtils.getLocationsPool()) {
			if (IdeMappingUtils.isActive(source) && MappingUtils.getLink(source, target) == null) {
				try {
					MappingUtils.createLink(source, target);
				} catch (InstantiationException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, UNABLE_TO_CREATE_LINK, e));
				} catch (IllegalAccessException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, UNABLE_TO_CREATE_LINK, e));
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, UNABLE_TO_CREATE_LINK, e));
				}
			}
		}
	}

	@Override
	protected boolean canHandleLocation(ILocation target) {
		boolean res = false;

		for (ILocation source : IdeMappingUtils.getLocationsPool()) {
			if (IdeMappingUtils.isActive(source) && MappingUtils.getLink(source, target) == null) {
				res = true;
				break;
			}
		}

		return res;
	}

}
