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
package org.eclipse.mylyn.docs.intent.mapping.emf.connector;

import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;

/**
 * Listen to changes and update locations accordingly.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingCDOListener implements IListener {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.net4j.util.event.IListener#notifyEvent(org.eclipse.net4j.util.event.IEvent)
	 */
	public void notifyEvent(IEvent event) {
		// TODO Auto-generated method stub
	}

}
