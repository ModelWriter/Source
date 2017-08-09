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

import java.io.IOException;
import java.io.Reader;

import org.eclipse.emf.cdo.eresource.CDOBinaryResource;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.eresource.CDOTextResource;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOBinaryResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOFolderLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDOTextResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;

/**
 * {@link CDOResourceNode} connector.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOResourceNodeConnector extends AbstractConnector {

	/**
	 * The buffer size.
	 */
	private static final int BUFFZE_SIZE = 8192;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationDescriptor(org.eclipse.mylyn.docs.intent.mapping.base.IBase,
	 *      java.lang.Object)
	 */
	public ILocationDescriptor getLocationDescriptor(IBase base, Object element) {
		final ILocationDescriptor res;

		final Object adapted = adapt(element);
		if (adapted instanceof CDOResourceNode) {
			res = new ObjectLocationDescriptor(this, base, adapted, ((CDOResourceNode)adapted).getPath());
			// TODO register the descriptor to the CDO repository connector
		} else {
			res = null;
		}

		return res;
	}

	@Override
	public void dispose(ILocationDescriptor locationDescriptor) {
		super.dispose(locationDescriptor);
		// TODO unregister the descriptor from the CDO repository connector
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (element instanceof CDOResource) {
			res = ICDOResourceLocation.class;
		} else if (element instanceof CDOTextResource) {
			// TODO text file delegates ?
			res = ICDOTextResourceLocation.class;
		} else if (element instanceof CDOBinaryResource) {
			// TODO binary file delegates ?
			res = ICDOBinaryResourceLocation.class;
		} else if (element instanceof CDOResourceFolder) {
			res = ICDOFolderLocation.class;
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		final CDOView cdoview = (CDOView)MappingUtils.getConnectorRegistry().getElement(
				(ICDORepositoryLocation)location.getContainer());

		return cdoview.getResourceNode(((ICDOResourceNodeLocation)location).getPath());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		return ((ICDOResourceNodeLocation)location).getPath();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getType()
	 */
	public Class<? extends ILocation> getType() {
		return ICDOResourceNodeLocation.class;
	}

	@Override
	protected boolean canUpdate(Object element) {
		return element instanceof CDOResourceNode;
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final boolean res;

		// TODO we implicitly decide to have a flat structure of location here... we probably don't want to do
		// that
		final ICDORepositoryLocation container = (ICDORepositoryLocation)location.getContainer();
		final CDOResourceNode node = (CDOResourceNode)element;
		if (node.cdoView().getSession().getRepositoryInfo().getUUID().equals(container.getUUID()) && node
				.cdoView().getBranch().getID() == container.getBranchID()) {
			res = ((ICDOResourceNodeLocation)location).getPath().equals(((CDOResourceNode)element).getPath());
		} else {
			res = false;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object element) {
		final ICDOResourceNodeLocation toInit = (ICDOResourceNodeLocation)location;

		toInit.setPath(((CDOResourceNode)element).getPath());
		if (toInit instanceof ICDOResourceLocation) {
			try {
				EObjectConnector.updateEObjectContainer((ICDOResourceLocation)toInit, (CDOResource)element);
				// CHECKSTYLE:OFF
			} catch (Exception e) {
				// CHECKSTYLE:ON
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (toInit instanceof ICDOTextResourceLocation) {
			try {
				final CDOTextResource textResource = (CDOTextResource)element;
				final String text = getContent(textResource.getContents().getContents(), textResource
						.getEncoding());
				TextConnector.updateTextContainer((ICDOTextResourceLocation)toInit, text);
				// CHECKSTYLE:OFF
			} catch (Exception e) {
				// CHECKSTYLE:ON
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO binary and text file delegates ?
	}

	/**
	 * Gets the content of the given {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader}
	 * @param encoding
	 *            the encoding
	 * @return the content of the given {@link Reader}
	 * @throws IOException
	 *             if the {@link Reader} can't be read
	 */
	private String getContent(Reader reader, String encoding) throws IOException {
		final StringBuilder builder = new StringBuilder();

		char[] buffer = new char[BUFFZE_SIZE];
		while (reader.read(buffer) != -1) {
			builder.append(buffer);
		}

		return builder.toString();
	}

}
