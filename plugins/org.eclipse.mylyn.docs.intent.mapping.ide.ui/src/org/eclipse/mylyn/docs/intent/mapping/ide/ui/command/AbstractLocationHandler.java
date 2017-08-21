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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.ui.Activator;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract implementation of handler for {@link ILocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractLocationHandler extends AbstractHandler {

	/**
	 * Unable to create {@link ILink} message.
	 */
	protected static final String UNABLE_TO_CREATE_LINK = "unable to create link";

	/**
	 * Unable to create source {@link ILocation} message.
	 */
	protected static final String UNABLE_TO_CREATE_SOURCE_LOCATION = "unable to create source location";

	/**
	 * Unable to create target {@link ILocation} message.
	 */
	protected static final String UNABLE_TO_CREATE_TARGET_LOCATION = "unable to create target location";

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();

		final List<ILocationDescriptor> locationDescriptors = new ArrayList<ILocationDescriptor>();
		if (selection instanceof IStructuredSelection) {

			for (Object selected : ((IStructuredSelection)selection).toList()) {
				final Object element = getElementFromSelectedObject(selected);
				final ILocationDescriptor locationDescriptor = IdeMappingUtils.adapt(element,
						ILocationDescriptor.class);
				if (locationDescriptor != null) {
					locationDescriptors.add(locationDescriptor);
				}
			}

		} else {
			final ILocationDescriptor locationDescriptor = IdeMappingUtils.adapt(selection,
					ILocationDescriptor.class);
			if (locationDescriptor != null) {
				locationDescriptors.add(locationDescriptor);
			}
		}

		for (ILocationDescriptor locationDescriptor : locationDescriptors) {
			handleLocationDescriptor(locationDescriptor);
		}

		return null;
	}

	/**
	 * Gets the element to adapt to {@link ILocationDescriptor} from the given selected {@link Object}.
	 * 
	 * @param selected
	 *            the selected {@link Object}
	 * @return the element to adapt to {@link ILocationDescriptor} from the given selected {@link Object}
	 */
	protected Object getElementFromSelectedObject(Object selected) {
		return selected;
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		super.setEnabled(evaluationContext);
		boolean enable;

		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			enable = false;
			for (Object selected : ((IStructuredSelection)selection).toList()) {
				final Object element = getElementFromSelectedObject(selected);
				final ILocationDescriptor locationDescriptor = IdeMappingUtils.adapt(element,
						ILocationDescriptor.class);
				if (locationDescriptor != null) {
					if (canHandleLocation(locationDescriptor)) {
						enable = true;
						break;
					}
				}
			}
		} else if (selection != null) {
			final ILocationDescriptor locationDescriptor = IdeMappingUtils.adapt(selection,
					ILocationDescriptor.class);
			enable = locationDescriptor != null && canHandleLocation(locationDescriptor);
		} else {
			enable = false;
		}

		setBaseEnabled(enable);
	}

	/**
	 * Creates the {@link ILocation} from {@link ILocationDescriptor}.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor}
	 * @param errorMessage
	 *            the error message
	 * @return the created {@link ILocation} if no error occurred, <code>null</code> otherwise
	 */
	protected ILocation createLocation(ILocationDescriptor locationDescriptor, String errorMessage) {
		ILocation res;

		try {
			res = locationDescriptor.getOrCreate();
		} catch (InstantiationException eLocation) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage,
					eLocation));
			res = null;
		} catch (IllegalAccessException eLocation) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage,
					eLocation));
			res = null;
		} catch (ClassNotFoundException eLocation) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage,
					eLocation));
			res = null;
		}
		return res;
	}

	/**
	 * Creates the {@link ILink}.
	 * 
	 * @param source
	 *            the {@link ILink#getSource() source} {@link ILocation}
	 * @param target
	 *            the {@link ILink#getTarget() target} {@link ILocation}
	 * @return the created {@link ILink} if any, <code>null</code> otherwise
	 */
	protected ILink createLink(final ILocation source, ILocation target) {
		ILink res;

		try {
			if (MappingUtils.canCreateLink(source, target)) {
				res = MappingUtils.createLink(source, target);
			} else {
				res = null;
			}
		} catch (InstantiationException eLink) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_CREATE_LINK, eLink));
			res = null;
		} catch (IllegalAccessException eLink) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_CREATE_LINK, eLink));
			res = null;
		} catch (ClassNotFoundException eLink) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					UNABLE_TO_CREATE_LINK, eLink));
			res = null;
		}

		return res;
	}

	/**
	 * Handles the given {@link ILocationDescriptor} by applying needed operation.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor} to handle
	 */
	protected abstract void handleLocationDescriptor(ILocationDescriptor locationDescriptor);

	/**
	 * Tells if the given {@link ILocationDescriptor} can be handled.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor} to check
	 * @return <code>true</code> if the given {@link ILocationDescriptor} can be handled, <code>false</code>
	 *         otherwise
	 */
	protected abstract boolean canHandleLocation(ILocationDescriptor locationDescriptor);

}
