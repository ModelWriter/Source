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
package org.eclipse.mylyn.docs.intent.mapping.jena.ide.connector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.AbstractFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer;
import org.eclipse.mylyn.docs.intent.mapping.jena.connector.RdfConnector;
import org.eclipse.mylyn.docs.intent.mapping.jena.ide.IRdfFileLocation;

/**
 * {@link IRdfFileLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RdfFileConnectorDelegate extends AbstractFileConnectorDelegate {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getContentType()
	 */
	public IContentType getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

		return contentTypeManager.getContentType("org.eclipse.mylyn.docs.intent.mapping.jena.ide.turtle");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getFileLocationType()
	 */
	public Class<? extends IFileLocation> getFileLocationType() {
		return IRdfFileLocation.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#initLocation(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void initLocation(IFileLocation location, IFile element) {
		final Model model = RDFDataMgr.loadModel(element.getLocation().toFile().getAbsolutePath(), Lang.TTL);

		final List<Resource> concepts = new ArrayList<Resource>();
		final NodeIterator it = model.listObjects();
		while (it.hasNext()) {
			final RDFNode node = it.next();
			if (node.isResource()) {
				concepts.add(node.asResource());
			}
		}

		RdfConnector.updateRdfContainer((IRdfContainer)location, concepts);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getElement(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation)
	 */
	public Object getElement(IFileLocation location) {
		final IFile file = (IFile)super.getElement(location);
		final Model res = RDFDataMgr.loadModel(file.getLocation().toFile().getAbsolutePath(), Lang.TTL);

		return res;
	}

}
