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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.AbstractFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer;
import org.eclipse.mylyn.docs.intent.mapping.jena.connector.RdfConnector;
import org.eclipse.mylyn.docs.intent.mapping.jena.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.jena.ide.IRdfFileLocation;

/**
 * {@link IRdfFileLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RdfFileConnectorDelegate extends AbstractFileConnectorDelegate {

	/**
	 * The file extension to {@link Lang} mapping.
	 */
	private static final Map<String, Lang> EXTENSION_TO_LANG = initExtensionToLang();

	/**
	 * Initializes the file extension to {@link Lang} mapping.
	 * 
	 * @return the file extension to {@link Lang} mapping
	 */
	private static Map<String, Lang> initExtensionToLang() {
		final Map<String, Lang> res = new HashMap<String, Lang>();

		for (String extension : Lang.JSONLD.getFileExtensions()) {
			res.put(extension, Lang.JSONLD);
		}
		for (String extension : Lang.N3.getFileExtensions()) {
			res.put(extension, Lang.N3);
		}
		for (String extension : Lang.NQ.getFileExtensions()) {
			res.put(extension, Lang.NQ);
		}
		for (String extension : Lang.NQUADS.getFileExtensions()) {
			res.put(extension, Lang.NQUADS);
		}
		for (String extension : Lang.NT.getFileExtensions()) {
			res.put(extension, Lang.NT);
		}
		for (String extension : Lang.NTRIPLES.getFileExtensions()) {
			res.put(extension, Lang.NTRIPLES);
		}
		for (String extension : Lang.RDFJSON.getFileExtensions()) {
			res.put(extension, Lang.RDFJSON);
		}
		for (String extension : Lang.RDFNULL.getFileExtensions()) {
			res.put(extension, Lang.RDFNULL);
		}
		for (String extension : Lang.RDFXML.getFileExtensions()) {
			res.put(extension, Lang.RDFXML);
		}
		for (String extension : Lang.TRIG.getFileExtensions()) {
			res.put(extension, Lang.TRIG);
		}
		for (String extension : Lang.TTL.getFileExtensions()) {
			res.put(extension, Lang.TTL);
		}
		for (String extension : Lang.TURTLE.getFileExtensions()) {
			res.put(extension, Lang.TURTLE);
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getContentType()
	 */
	public IContentType getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

		return contentTypeManager.getContentType("org.eclipse.mylyn.docs.intent.mapping.jena.ide.onthology");
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
		final Lang lang = EXTENSION_TO_LANG.get(element.getFileExtension());
		final Model model = RDFDataMgr.loadModel(element.getLocation().toFile().getAbsolutePath(), lang);

		final List<Resource> concepts = new ArrayList<Resource>();
		final NodeIterator it = model.listObjects();
		while (it.hasNext()) {
			final RDFNode node = it.next();
			if (node.isResource()) {
				concepts.add(node.asResource());
			}
		}

		try {
			RdfConnector.updateRdfContainer((IRdfContainer)location, concepts);
		} catch (InstantiationException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (IllegalAccessException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (ClassNotFoundException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getElement(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation)
	 */
	public Object getElement(IFileLocation location) {
		final IFile file = (IFile)super.getElement(location);
		final Lang lang = EXTENSION_TO_LANG.get(file.getFileExtension());
		final Model res = RDFDataMgr.loadModel(file.getLocation().toFile().getAbsolutePath(), lang);

		return res;
	}

}
