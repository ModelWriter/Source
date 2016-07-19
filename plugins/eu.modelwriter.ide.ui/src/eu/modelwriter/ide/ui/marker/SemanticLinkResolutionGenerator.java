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
package eu.modelwriter.ide.ui.marker;

import eu.modelwriter.ide.ui.Activator;
import eu.modelwriter.semantic.ide.ISemanticAnnotationMarker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator2;

/**
 * Create {@link ILink} from {@link ISemanticAnnotationMarker}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticLinkResolutionGenerator implements IMarkerResolutionGenerator2 {

	/**
	 * Creates an {@link ILink} from the {@link ILocation source} and the {@link ILocation target}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static final class SemanticLinkMarkerResolution implements IMarkerResolution2 {

		/**
		 * The unable to create link message.
		 */
		private static final String UNABLE_TO_CREATE_LINK = "Unable to create link.";

		/**
		 * The {@link ILocation source}.
		 */
		private final ILocation source;

		/**
		 * The {@link ILocation target}.
		 */
		private final ILocation target;

		/**
		 * Constructor.
		 * 
		 * @param source
		 *            the {@link ILocation source}
		 * @param target
		 *            the {@link ILocation target}
		 */
		public SemanticLinkMarkerResolution(ILocation source, ILocation target) {
			this.source = source;
			this.target = target;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution#getLabel()
		 */
		public String getLabel() {
			final StringBuilder res = new StringBuilder();

			res.append("Create link from ");
			res.append(MappingUtils.getConnectorRegistry().getName(source));
			res.append(" to ");
			res.append(MappingUtils.getConnectorRegistry().getName(target));
			res.append(".");

			return res.toString();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
		 */
		public void run(IMarker marker) {
			final IBase base = MappingUtils.getBase(source);

			try {
				final ILink link = base.getFactory().createElement(ILink.class);
				link.setSource(source);
				link.setTarget(target);
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

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution2#getDescription()
		 */
		public String getDescription() {
			return getLabel();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution2#getImage()
		 */
		public Image getImage() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.IMarkerResolutionGenerator#getResolutions(org.eclipse.core.resources.IMarker)
	 */
	public IMarkerResolution[] getResolutions(IMarker marker) {
		final List<IMarkerResolution> res = new ArrayList<IMarkerResolution>();

		// TODO we want to have a description of ILocation here not the location itself...
		final ILocation source = IdeMappingUtils.adapt(marker, ILocation.class);
		if (source != null) {
			final Object concept;
			try {
				concept = marker.getAttribute(ISemanticAnnotationMarker.SEMANTIC_CONCEPT_ATTRIBUTE);
				final ILocation target = IdeMappingUtils.adapt(concept, ILocation.class);
				if (target != null && MappingUtils.getLink(source, target) == null) {
					res.add(new SemanticLinkMarkerResolution(source, target));
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Unanble to get the concept attribute on the marker", e));
			}
		}

		return res.toArray(new IMarkerResolution[res.size()]);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.IMarkerResolutionGenerator2#hasResolutions(org.eclipse.core.resources.IMarker)
	 */
	public boolean hasResolutions(IMarker marker) {
		return getResolutions(marker).length != 0;
	}

}
