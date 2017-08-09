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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
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
	 * Unable to get the concept attribute on the marker message.
	 */
	private static final String UNABLE_TO_GET_THE_CONCEPT_ATTRIBUTE_ON_THE_MARKER = "Unable to get the concept attribute on the marker";

	/**
	 * Creates an {@link ILink} from the {@link ILocationDescriptor source} and the {@link ILocationDescriptor
	 * target}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static final class SemanticLinkMarkerResolution implements IMarkerResolution2 {

		/**
		 * The unable to create link message.
		 */
		private static final String UNABLE_TO_CREATE_LINK = "Unable to create link.";

		/**
		 * The source {@link ILocationDescriptor}.
		 */
		private final ILocationDescriptor sourceDescriptor;

		/**
		 * The target {@link ILocationDescriptor}.
		 */
		private final ILocationDescriptor targetDescriptor;

		/**
		 * Constructor.
		 * 
		 * @param sourceDescriptor
		 *            the {@link ILocationDescriptor source}
		 * @param targetDescriptor
		 *            the {@link ILocationDescriptor target}
		 */
		public SemanticLinkMarkerResolution(ILocationDescriptor sourceDescriptor,
				ILocationDescriptor targetDescriptor) {
			this.sourceDescriptor = sourceDescriptor;
			this.targetDescriptor = targetDescriptor;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution#getLabel()
		 */
		public String getLabel() {
			final StringBuilder res = new StringBuilder();

			res.append("Create link from ");
			res.append(sourceDescriptor.getName());
			res.append(" to ");
			res.append(targetDescriptor.getName());
			res.append(".");

			return res.toString();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
		 */
		public void run(IMarker marker) {
			final IBase base = IdeMappingUtils.getCurrentBase();

			try {
				final ILink link = base.getFactory().createElement(ILink.class);
				link.setSource(sourceDescriptor.getOrCreate());
				sourceDescriptor.dispose();
				link.setTarget(targetDescriptor.getOrCreate());
				targetDescriptor.dispose();
			} catch (InstantiationException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_CREATE_LINK, e));
			} catch (IllegalAccessException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_CREATE_LINK, e));
			} catch (ClassNotFoundException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_CREATE_LINK, e));
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

		final ILocationDescriptor sourceDescriptor = IdeMappingUtils.adapt(marker, ILocationDescriptor.class);
		if (sourceDescriptor != null) {
			try {
				final Object concept = marker.getAttribute(
						ISemanticAnnotationMarker.SEMANTIC_CONCEPT_ATTRIBUTE);
				final IFile file = IdeMappingUtils.adapt(concept, IFile.class);
				if (file != null) {
					final IBase currentBase = IdeMappingUtils.getCurrentBase();
					final ILocationDescriptor targetDescriptor = MappingUtils.getConnectorRegistry()
							.getLocationDescriptor(currentBase, concept);
					if (!sourceDescriptor.exists() || !targetDescriptor.exists()) {
						res.add(new SemanticLinkMarkerResolution(sourceDescriptor, targetDescriptor));
					} else if (MappingUtils.getLink(sourceDescriptor.getOrCreate(), targetDescriptor
							.getOrCreate()) == null) {
						res.add(new SemanticLinkMarkerResolution(sourceDescriptor, targetDescriptor));
					}
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_GET_THE_CONCEPT_ATTRIBUTE_ON_THE_MARKER, e));
			} catch (InstantiationException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_GET_THE_CONCEPT_ATTRIBUTE_ON_THE_MARKER, e));
			} catch (IllegalAccessException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_GET_THE_CONCEPT_ATTRIBUTE_ON_THE_MARKER, e));
			} catch (ClassNotFoundException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						UNABLE_TO_GET_THE_CONCEPT_ATTRIBUTE_ON_THE_MARKER, e));
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
