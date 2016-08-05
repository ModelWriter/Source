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
package org.eclipse.mylyn.docs.intent.mapping.jena.ide;

import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer;

/**
 * {@link com.hp.hpl.jena.rdf.model.Resource Resource} {@link IFileLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IRdfFileLocation extends IFileLocation, IRdfContainer {

}
