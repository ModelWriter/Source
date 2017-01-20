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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;

/**
 * {@link EObject} {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnector extends AbstractConnector {

	/**
	 * {@link MatchEngineFactoryImpl} {@link MatchEngineFactoryImpl#getRanking() ranking}.
	 */
	// private static final int MATCH_ENGINE_FACTORY_RANKING = 20;

	/**
	 * Locate a specific {@link Setting}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class LocationSetting implements Setting {

		/**
		 * The {@link EObject}.
		 */
		private final EObject eObject;

		/**
		 * The {@link EStructuralFeature}.
		 */
		private final EStructuralFeature feature;

		/**
		 * The index in the {@link LocationSetting#getEStructuralFeature() feature}.
		 */
		private final int index;

		/**
		 * Constructor.
		 * 
		 * @param eObject
		 *            the {@link EObject}
		 * @param feature
		 *            the {@link EStructuralFeature}
		 * @param index
		 *            the index in the {@link LocationSetting#getEStructuralFeature() feature}
		 */
		private LocationSetting(EObject eObject, EStructuralFeature feature, int index) {
			this.eObject = eObject;
			this.feature = feature;
			this.index = index;
		}

		/**
		 * Unsupported operation.
		 */
		public void unset() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported operation.
		 * 
		 * @param newValue
		 *            the new value
		 */
		public void set(Object newValue) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported operation.
		 * 
		 * @return <code>true</code> if is setn <code>false</code> otherwise
		 */
		public boolean isSet() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Gets the {@link EStructuralFeature} containing the value.
		 * 
		 * @return the {@link EStructuralFeature} containing the value
		 */
		public EStructuralFeature getEStructuralFeature() {
			return feature;
		}

		/**
		 * Gets the containing {@link EObject}.
		 * 
		 * @return the containing {@link EObject}
		 */
		public EObject getEObject() {
			return eObject;
		}

		/**
		 * Gets the value.
		 * 
		 * @param resolve
		 *            whether to resolve
		 * @return the value
		 */
		@SuppressWarnings("unchecked")
		public Object get(boolean resolve) {
			final Object res;

			final Object value = eObject.eGet(feature, resolve);
			if (feature.isMany()) {
				res = ((List<Object>)value).get(index);
			} else {
				res = value;
			}

			return res;
		}
	}

	/**
	 * UTF-8 charset.
	 */
	private static final String UTF_8 = "UTF-8";

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
		final String featureName;
		final int index;
		if (element instanceof Setting) {
			final Setting setting = (Setting)element;
			eObject = setting.getEObject();
			final EStructuralFeature feature = ((Setting)element).getEStructuralFeature();
			featureName = feature.getName();
			if (feature.isMany()) {
				index = ((List<?>)eObject.eGet(feature, true)).indexOf(setting.get(true));
			} else {
				index = 0;
			}
		} else {
			eObject = getEObject((IEObjectContainer)container, (EObject)element);
			featureName = null;
			index = 0;
		}

		final String uriFragment = eObject.eResource().getURIFragment(eObject);
		toInit.setURIFragment(uriFragment);
		toInit.setFeatureName(featureName);
		toInit.setIndex(index);
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
				final Setting setting = (Setting)element;
				final String uriFragment = setting.getEObject().eResource().getURIFragment(
						setting.getEObject());
				final boolean correctIndex;
				if (setting.getEStructuralFeature().isMany()) {
					correctIndex = eObjectLocation.getIndex() == ((List<?>)setting.getEObject().eGet(
							setting.getEStructuralFeature())).indexOf(setting.get(true));
				} else {
					correctIndex = true;
				}
				res = uriFragment != null && uriFragment.equals(eObjectLocation.getURIFragment())
						&& eObjectLocation.getFeatureName().equals(setting.getEStructuralFeature().getName())
						&& correctIndex;
			} else {
				res = false;
			}
		} else {
			if (element instanceof EObject) {
				final EObject eObject = (EObject)element;
				final String uriFragment = ((EObject)element).eResource().getURIFragment(eObject);
				res = uriFragment != null && uriFragment.equals(eObjectLocation.getURIFragment());
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
	 * @param newResource
	 *            the {@link Resource}
	 * @throws Exception
	 *             if the XMI serialization failed
	 */
	public static void updateEObjectContainer(IEObjectContainer container, Resource newResource)
			throws Exception {
		final String newXMIContent = XMLHelperImpl.saveString(new HashMap<Object, Object>(), newResource
				.getContents(), UTF_8, null);

		if (container.getXMIContent() != null) {
			final XMIResourceImpl oldResource = new XMIResourceImpl(URI.createURI(""));
			oldResource.load(new ByteArrayInputStream(container.getXMIContent().getBytes(UTF_8)),
					new HashMap<Object, Object>());
			final IComparisonScope scope = new DefaultComparisonScope(oldResource, newResource, null);
			// final IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl
			// .createStandaloneInstance();
			// final MatchEngineFactoryImpl matchEngineFactory = new
			// MatchEngineFactoryImpl(UseIdentifiers.NEVER);
			// matchEngineFactory.setRanking(MATCH_ENGINE_FACTORY_RANKING);
			// registry.add(matchEngineFactory);
			// final Comparison comparison = EMFCompare.builder().setMatchEngineFactoryRegistry(registry)
			// .build().compare(scope);
			final Comparison comparison = EMFCompare.builder().build().compare(scope);
			for (ILocation child : new ArrayList<ILocation>(container.getContents())) {
				if (child instanceof IEObjectLocation && !child.isMarkedAsDeleted()) {
					final IEObjectLocation location = (IEObjectLocation)child;
					updateEObjectLocation(oldResource, comparison, location);
				}
			}
		}
		container.setXMIContent(newXMIContent);
	}

	/**
	 * Updates the given {@link IEObjectLocation}.
	 * 
	 * @param oldResource
	 *            the old {@link Resource}
	 * @param comparison
	 *            the {@link Comparison}
	 * @param eObjectlocation
	 *            the {@link IEObjectLocation}
	 * @throws Exception
	 *             if {@link org.eclipse.mylyn.docs.intent.mapping.Report Report} can't be created
	 */
	private static void updateEObjectLocation(Resource oldResource, Comparison comparison,
			IEObjectLocation eObjectlocation) throws Exception {
		final EObject oldObject = oldResource.getEObject(eObjectlocation.getURIFragment());
		final Match match = comparison.getMatch(oldObject);

		if (match.getRight() != null) {
			final String newURIFragment = match.getRight().eResource().getURIFragment(match.getRight());
			if (!match.getDifferences().isEmpty()) {
				final EObject newObject = match.getRight();
				if (eObjectlocation.getFeatureName() == null) {
					MappingUtils.markAsChanged(eObjectlocation, getMatchMessage(oldObject, newObject, match));
				} else {
					updateLoccationSetting(comparison, eObjectlocation, oldObject, newObject, match);
				}
			}
			eObjectlocation.setURIFragment(newURIFragment);
		} else {
			MappingUtils.markAsDeletedOrDelete(eObjectlocation, String.format("%s at %s has been deleted.",
					getValueString(oldObject), eObjectlocation.getURIFragment()));
		}
	}

	/**
	 * Gets a human readable messages for the given {@link Match}.
	 * 
	 * @param oldObject
	 *            the old {@link EObject}
	 * @param newObject
	 *            the new {@link EObject}
	 * @param match
	 *            the {@link Match}
	 * @return a human readable messages for the given {@link Match}
	 */
	private static String getMatchMessage(EObject oldObject, EObject newObject, Match match) {
		final StringBuilder res = new StringBuilder();

		for (Diff diff : match.getDifferences()) {
			if (diff instanceof AttributeChange) {
				res.append("Attribure ");
				final EAttribute attribute = ((AttributeChange)diff).getAttribute();
				res.append(attribute.getName());
				res.append(" was ");
				res.append(getValueString(oldObject.eGet(attribute)));
				res.append(" changed to ");
				res.append(getValueString(newObject.eGet(attribute)));
			} else if (diff instanceof ReferenceChange) {
				res.append("Reference ");
				final EReference reference = ((ReferenceChange)diff).getReference();
				res.append(reference.getName());
				res.append(" was ");
				res.append(getValueString(oldObject.eGet(reference)));
				res.append(" changed to ");
				res.append(getValueString(newObject.eGet(reference)));
			}
			res.append(".\n");
		}

		return res.substring(0, res.length() - 1);
	}

	/**
	 * Updates the given {@link IEObjectLocation} for a {@link LocationSetting}.
	 * 
	 * @param comparison
	 *            the {@link Comparison}
	 * @param eObjectlocation
	 *            the {@link IEObjectLocation} to update
	 * @param oldObject
	 *            the old holding {@link EObject}
	 * @param newObject
	 *            the new holding {@link EObject}
	 * @param match
	 *            the {@link Match}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	private static void updateLoccationSetting(Comparison comparison, IEObjectLocation eObjectlocation,
			final EObject oldObject, EObject newObject, final Match match) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final EStructuralFeature feature = oldObject.eClass().getEStructuralFeature(
				eObjectlocation.getFeatureName());
		final boolean hasDiffForFeature = hasDiffForFeature(match, feature);
		if (hasDiffForFeature) {
			final Object oldValue;
			final Object newValue;
			if (feature.isMany()) {
				oldValue = ((List<?>)oldObject.eGet(feature)).get(eObjectlocation.getIndex());
				newValue = getNewValueInManySetting(comparison, oldValue);
				final int newIndex = ((List<?>)newObject.eGet(feature)).indexOf(newValue);
				eObjectlocation.setIndex(newIndex);
			} else {
				oldValue = oldObject.eGet(feature);
				newValue = newObject.eGet(feature);
			}
			if (eObjectlocation.getIndex() == -1) {
				MappingUtils.markAsDeletedOrDelete(eObjectlocation, String.format(
						"%s (%s) value %s has been removed from feature %s.", getLabel(oldObject),
						eObjectlocation.getURIFragment(), getValueString(oldValue), eObjectlocation
								.getFeatureName()));
			} else if (!oldValue.equals(newValue)) {
				MappingUtils.markAsChanged(eObjectlocation, String.format(
						"%s (%s) feature %s value %s has been changed to %s.", getLabel(oldObject),
						eObjectlocation.getURIFragment(), eObjectlocation.getFeatureName(),
						getValueString(oldValue), getValueString(newValue)));
			}
		} else {
			// there is a diff for the holding EObject but not the located setting
			// nothing to do here
		}
	}

	/**
	 * Gets the string representation of the given value.
	 * 
	 * @param value
	 *            the value
	 * @return the string representation of the given value
	 */
	private static String getValueString(Object value) {
		final String res;

		if (value instanceof EObject) {
			res = getLabel((EObject)value);
		} else if (value == null) {
			res = "null";
		} else {
			res = value.toString();
		}

		return res;
	}

	/**
	 * Gets the new value for a multi-valuated setting.
	 * 
	 * @param comparison
	 *            the {@link Comparison}
	 * @param oldValue
	 *            the old value
	 * @return the new value for a multi-valuated setting
	 */
	private static Object getNewValueInManySetting(Comparison comparison, final Object oldValue) {
		final Object newValue;
		if (oldValue instanceof EObject) {
			final Match valueMatch = comparison.getMatch((EObject)oldValue);
			if (valueMatch != null) {
				newValue = valueMatch.getRight();
			} else {
				newValue = oldValue;
			}
		} else {
			newValue = oldValue;
		}
		return newValue;
	}

	/**
	 * Tells if there is a difference in the given {@link Match} for the given {@link EStructuralFeature}.
	 * 
	 * @param match
	 *            the {@link Match}
	 * @param feature
	 *            the {@link EStructuralFeature}
	 * @return <code>true</code> if there is a difference in the given {@link Match} for the given
	 *         {@link EStructuralFeature}, <code>false</code> otherwise
	 */
	private static boolean hasDiffForFeature(Match match, EStructuralFeature feature) {
		boolean res = false;

		for (Diff diff : match.getDifferences()) {
			if (diff instanceof AttributeChange) {
				if (((AttributeChange)diff).getAttribute() == feature) {
					res = true;
					break;
				}
			} else if (diff instanceof ReferenceChange) {
				if (((ReferenceChange)diff).getReference() == feature) {
					res = true;
					break;
				}
			}
		}

		return res;
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
			res = getName(setting.getEObject(), setting.getEStructuralFeature(), ((IEObjectLocation)location)
					.getIndex());
		} else if (element instanceof EObject) {
			res = getName((EObject)element, null, 0);
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
	 * @param index
	 *            the index in the feature
	 * @return the name for the given {@link EObject} and {@link EStructuralFeature}
	 */
	private String getName(EObject eObj, EStructuralFeature feature, int index) {
		final StringBuilder res = new StringBuilder();

		res.append(getLabel(eObj));
		if (feature != null) {
			res.append(" ");
			res.append(feature.getEContainingClass().getName());
			res.append(".");
			res.append(feature.getName());
			res.append("[");
			res.append(index);
			res.append("]");
		}

		return res.toString();
	}

	/**
	 * Gets the label of the given {@link EObject}.
	 * 
	 * @param eObj
	 *            the {@link EObject}
	 * @return the label of the given {@link EObject}
	 */
	private static String getLabel(EObject eObj) {
		final String res;
		final IItemLabelProvider itemProvider = (IItemLabelProvider)FACTORY.adapt(eObj,
				IItemLabelProvider.class);

		if (itemProvider == null) {
			res = eObj.toString();
		} else {
			res = itemProvider.getText(eObj);
		}

		return res;
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
			res = new ObjectLocationDescriptor(this, containerDescriptor, element, getName(eObj, null, 0));
		} else if (element instanceof Setting) {
			final EObject eObj = ((Setting)element).getEObject();
			final EStructuralFeature feature = ((Setting)element).getEStructuralFeature();
			res = new ObjectLocationDescriptor(this, containerDescriptor, element, getName(eObj, feature, 0));
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
			final EObject eObject = resource.getEObject(eObjLocation.getURIFragment());
			if (eObjLocation.getFeatureName() != null) {
				final EStructuralFeature feature = eObject.eClass().getEStructuralFeature(
						eObjLocation.getFeatureName());
				final int index = eObjLocation.getIndex();
				res = new LocationSetting(eObject, feature, index);
			} else {
				res = eObject;
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

}
