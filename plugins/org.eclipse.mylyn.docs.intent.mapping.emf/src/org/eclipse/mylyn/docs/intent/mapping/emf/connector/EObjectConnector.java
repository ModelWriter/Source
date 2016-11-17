/*******************************************************************************
 * Copyright (c) 2015 Obeo.
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

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils.DiffMatch;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.internal.TextAdapter;

/**
 * {@link EObject} {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnector extends AbstractConnector {

	/**
	 * The factory used to retrieve providers.
	 */
	private static final AdapterFactory FACTORY = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (IEObjectContainer.class.isAssignableFrom(containerType)
				&& (element instanceof EObject || element instanceof Setting)) {
			res = getType();
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object element) {
		final IEObjectLocation toInit = (IEObjectLocation)location;

		final EObject eObject;
		final EStructuralFeature feature;
		final Object value;
		final String featureName;
		final String text;
		final int textStartOffset;
		if (element instanceof Setting) {
			eObject = getEObject((IEObjectContainer)container, ((Setting)element).getEObject());
			feature = ((Setting)element).getEStructuralFeature();
			value = ((Setting)element).get(true);
			featureName = feature.getName();
			final ITextAdapter adapter = EObjectConnector.getTextAdapter(eObject);
			final String containerText = adapter.getText();
			final int[] offsets = adapter.getOffsets(feature, value);
			text = containerText.substring(offsets[0] - adapter.getTextOffset(), offsets[1]
					- adapter.getTextOffset());
			textStartOffset = offsets[0];
		} else {
			eObject = getEObject((IEObjectContainer)container, (EObject)element);
			feature = null;
			value = null;
			featureName = null;
			final ITextAdapter adapter = EObjectConnector.getTextAdapter(eObject);
			text = adapter.getText();
			textStartOffset = adapter.getTextOffset();
		}

		toInit.setFeatureName(featureName);
		toInit.setStartOffset(textStartOffset);
		toInit.setEndOffset(textStartOffset + text.length());
	}

	/**
	 * Gets the instance of the given {@link EObject} in the given {@link IEObjectContainer}.
	 * 
	 * @param container
	 *            the {@link IEObjectContainer}
	 * @param eObject
	 *            the original {@link EObject}
	 * @return the instance of the given {@link EObject} in the given {@link IEObjectContainer} if any, the
	 *         <code>null</code> otherwise
	 */
	private EObject getEObject(IEObjectContainer container, EObject eObject) {
		final EObject res;

		final Resource resource = (Resource)MappingUtils.getConnectorRegistry().getElement(container);
		final String uriFragment = eObject.eResource().getURIFragment(eObject);
		res = resource.getEObject(uriFragment);

		return res;
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final boolean res;

		final IEObjectLocation eObjectLocation = (IEObjectLocation)location;
		if (eObjectLocation.getFeatureName() != null) {
			if (element instanceof Setting) {
				final Setting locationSetting = (Setting)getElement(location);
				final Setting setting = (Setting)element;
				res = locationSetting.getEStructuralFeature() == setting.getEStructuralFeature()
						&& EcoreUtil.getURI(locationSetting.getEObject()).equals(
								EcoreUtil.getURI(setting.getEObject()));
			} else {
				res = false;
			}
		} else {
			if (element instanceof EObject) {
				final EObject locationElement = (EObject)getElement(location);
				final EObject eObject = (EObject)element;
				res = EcoreUtil.getURI(locationElement).equals(EcoreUtil.getURI(eObject));
			} else {
				res = false;
			}
		}

		return res;
	}

	/**
	 * Updates the given {@link IEObjectContainer} with the given {@link Resource}.
	 * 
	 * @param container
	 *            the {@link IEObjectContainer}
	 * @param resource
	 *            the {@link Resource}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void updateEObjectContainer(IEObjectContainer container, Resource resource)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		updateEObjectContainer(container, resource.getContents());
	}

	/**
	 * Updates the given {@link IEObjectContainer} with the given {@link List} of {@link EObject}.
	 * 
	 * @param container
	 *            the {@link IEObjectContainer}
	 * @param eObjects
	 *            the {@link List} of {@link EObject}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void updateEObjectContainer(IEObjectContainer container, List<EObject> eObjects)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final String newText = serialize(eObjects);
		final String oldText = container.getText();
		container.setText(newText);
		if (oldText != null) {
			final DiffMatch diff = MappingUtils.getDiffMatch(oldText, newText);
			for (ILocation child : container.getContents()) {
				if (child instanceof IEObjectLocation && !child.isMarkedAsDeleted()) {
					final IEObjectLocation location = (IEObjectLocation)child;
					final int newStartOffset = diff.getIndex(location.getStartOffset());
					final int newEndOffset = diff.getIndex(location.getEndOffset());
					if (isValidOffsets(newText, location.getFeatureName(), newStartOffset, newEndOffset)) {
						location.setStartOffset(newStartOffset);
						location.setEndOffset(newEndOffset);
					} else {
						MappingUtils.markAsDeletedOrDelete(location, String.format(
								"\"%s\" at (%d, %d) has been deleted.", oldText.substring(location
										.getStartOffset(), location.getEndOffset()), location
										.getStartOffset(), location.getEndOffset()));
					}
				}
			}
		}
	}

	/**
	 * Tell if the start and end offsets are valid in the given text according to given
	 * {@link EStructuralFeature}.
	 * 
	 * @param text
	 *            the containing text
	 * @param featureName
	 *            the {@link EStructuralFeature#getName() feature name} if any, <code>null</code> otherwise
	 * @param startOffset
	 *            the {@link IEObjectLocation#getStartOffset() start offset}
	 * @param endOffset
	 *            the {@link IEObjectLocation#getEndOffset() end offset}
	 * @return <code>true</code> if the start and end offsets are valid in the given text according to given
	 *         {@link EStructuralFeature}, <code>false</code> otherwise
	 */
	protected static boolean isValidOffsets(String text, String featureName, int startOffset, int endOffset) {
		final boolean res;

		if (featureName != null) {
			final boolean startMatched = startOffset - TextAdapter.START_SETTING.length() >= 0
					&& TextAdapter.START_SETTING.equals(text.substring(startOffset
							- TextAdapter.START_SETTING.length(), startOffset));
			final boolean featureMatched = text.substring(startOffset).startsWith(
					featureName + TextAdapter.MIDDLE_SETTING);
			final boolean endMatched = endOffset + TextAdapter.END_SETTING.length() <= text.length()
					&& text.substring(endOffset).startsWith(TextAdapter.END_SETTING);
			res = startMatched && featureMatched && endMatched;
		} else {
			final boolean startMatched = text.substring(startOffset).startsWith(TextAdapter.START_EOBJECT);
			final boolean endMatched = text.substring(endOffset - TextAdapter.END_EOBJECT.length())
					.startsWith(TextAdapter.END_EOBJECT);
			res = startMatched && endMatched;
		}

		return res;
	}

	/**
	 * Serializes the given {@link List} of {@link EObject}.
	 * 
	 * @param eObjects
	 *            the {@link List} of {@link EObject} to serialize
	 * @return the {@link String} representing the given {@link List} of {@link EObject}
	 */
	private static String serialize(List<EObject> eObjects) {
		final StringBuilder builder = new StringBuilder();

		for (EObject eObj : eObjects) {
			final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(eObj);
			textAdapter.setTextOffset(builder.length());
			builder.append(textAdapter.getText());
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		final Object element = getElement(location);
		if (element instanceof Setting) {
			final Setting setting = (Setting)element;
			res = getName(setting.getEObject(), setting.getEStructuralFeature());
		} else if (element instanceof EObject) {
			res = getName((EObject)element, null);
		} else {
			// the located element has been deleted...
			res = null;
		}

		return res;
	}

	/**
	 * Gets the name for the given {@link EObject} and {@link EStructuralFeature}.
	 * 
	 * @param eObj
	 *            the {@link EObject}
	 * @param feature
	 *            the {@link EStructuralFeature} can be <code>null</code>
	 * @return the name for the given {@link EObject} and {@link EStructuralFeature}
	 */
	private String getName(EObject eObj, EStructuralFeature feature) {
		final StringBuilder res = new StringBuilder();

		final IItemLabelProvider itemProvider = (IItemLabelProvider)FACTORY.adapt(eObj,
				IItemLabelProvider.class);
		res.append(itemProvider.getText(eObj));
		if (feature != null) {
			res.append(" ");
			res.append(feature.getEContainingClass().getName());
			res.append(".");
			res.append(feature.getName());

		}

		return res.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationDescriptor(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor,
	 *      java.lang.Object)
	 */
	public ILocationDescriptor getLocationDescriptor(ILocationDescriptor containerDescriptor, Object element) {
		final ILocationDescriptor res;

		if (element instanceof EObject) {
			final EObject eObj = (EObject)element;
			res = new ObjectLocationDescriptor(this, containerDescriptor, element, getName(eObj, null));
		} else if (element instanceof Setting) {
			final EObject eObj = ((Setting)element).getEObject();
			final EStructuralFeature feature = ((Setting)element).getEStructuralFeature();
			res = new ObjectLocationDescriptor(this, containerDescriptor, element, getName(eObj, feature));
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected boolean canUpdate(Object element) {
		return element instanceof EObject || element instanceof Setting;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		final Object res;

		final IEObjectLocation eObjLocation = (IEObjectLocation)location;
		if (location.getContainer() != null) {
			final Resource resource = (Resource)MappingUtils.getConnectorRegistry().getElement(
					(ILocation)location.getContainer());
			ITextAdapter nearestAdapter = null;
			for (EObject root : resource.getContents()) {
				final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(root);
				if (textAdapter.getTextOffset() > eObjLocation.getStartOffset()) {
					break;
				} else {
					nearestAdapter = textAdapter;
				}
			}
			if (nearestAdapter != null) {
				res = nearestAdapter.getElement(eObjLocation);
			} else {
				res = null;
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationType()
	 */
	public Class<? extends ILocation> getType() {
		return IEObjectLocation.class;
	}

	/**
	 * Gets a {@link ITextAdapter} for the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject}
	 * @return a {@link ITextAdapter} for the given {@link EObject}
	 */
	public static ITextAdapter getTextAdapter(EObject eObject) {
		ITextAdapter res = (ITextAdapter)EcoreUtil.getAdapter(eObject.eAdapters(), ITextAdapter.class);

		if (res == null) {
			res = new TextAdapter();
			eObject.eAdapters().add(res);
		}

		return res;
	}

}
