/**
 *  Copyright (c) 2015 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Obeo - initial API and implementation and/or initial documentation
 *     ...
 * 
 */
package org.eclipse.mylyn.docs.intent.mapping.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.preferences.IScope;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Location</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getScope <em>Scope</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getSourceLinks <em>Source Links</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getTargetLinks <em>Target Links</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getContents <em>Contents</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getContainer <em>Container</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getReferencingScopes <em>Referencing
 * Scopes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class LocationImpl extends MinimalEObjectImpl.Container implements Location {
	/**
	 * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getContents()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<ILocation> contents;

	/**
	 * The cached value of the '{@link #getSourceLinks() <em>Source Links</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSourceLinks()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<ILink> sourceLinks;

	/**
	 * The cached value of the '{@link #getTargetLinks() <em>Target Links</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTargetLinks()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<ILink> targetLinks;

	/**
	 * The cached value of the '{@link #getReferencingScopes() <em>Referencing Scopes</em>}' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReferencingScopes()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<IScope> referencingScopes;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final Serializable TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Serializable type = TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected LocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<ILink> getSourceLinks() {
		if (sourceLinks == null) {
			sourceLinks = new EObjectWithInverseResolvingEList<ILink>(Link.class, this,
					MappingPackage.LOCATION__SOURCE_LINKS, MappingPackage.LINK__TARGET);
		}
		return sourceLinks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<ILink> getTargetLinks() {
		if (targetLinks == null) {
			targetLinks = new EObjectContainmentWithInverseEList<ILink>(Link.class, this,
					MappingPackage.LOCATION__TARGET_LINKS, MappingPackage.LINK__SOURCE);
		}
		return targetLinks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<ILocation> getContents() {
		if (contents == null) {
			contents = new EObjectContainmentWithInverseEList<ILocation>(Location.class, this,
					MappingPackage.LOCATION__CONTENTS, MappingPackage.LOCATION__CONTAINER);
		}
		return contents;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LocationContainer getContainer() {
		if (eContainerFeatureID() != MappingPackage.LOCATION__CONTAINER)
			return null;
		return (LocationContainer)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetContainer(LocationContainer newContainer, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newContainer, MappingPackage.LOCATION__CONTAINER, msgs);
		return msgs;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocation#setContainer(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer)
	 * @generated NOT
	 */
	public void setContainer(ILocationContainer container) {
		assert container instanceof LocationContainer;
		setContainer((LocationContainer)container);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setContainer(LocationContainer newContainer) {
		if (newContainer != eInternalContainer()
				|| (eContainerFeatureID() != MappingPackage.LOCATION__CONTAINER && newContainer != null)) {
			if (EcoreUtil.isAncestor(this, newContainer))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newContainer != null)
				msgs = ((InternalEObject)newContainer).eInverseAdd(this,
						MappingPackage.LOCATION_CONTAINER__CONTENTS, LocationContainer.class, msgs);
			msgs = basicSetContainer(newContainer, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LOCATION__CONTAINER,
					newContainer, newContainer));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Serializable getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(Serializable newType) {
		Serializable oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LOCATION__TYPE, oldType,
					type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getContents()).basicAdd(otherEnd,
						msgs);
			case MappingPackage.LOCATION__SOURCE_LINKS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSourceLinks()).basicAdd(
						otherEnd, msgs);
			case MappingPackage.LOCATION__TARGET_LINKS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTargetLinks()).basicAdd(
						otherEnd, msgs);
			case MappingPackage.LOCATION__CONTAINER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetContainer((LocationContainer)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				return ((InternalEList<?>)getContents()).basicRemove(otherEnd, msgs);
			case MappingPackage.LOCATION__SOURCE_LINKS:
				return ((InternalEList<?>)getSourceLinks()).basicRemove(otherEnd, msgs);
			case MappingPackage.LOCATION__TARGET_LINKS:
				return ((InternalEList<?>)getTargetLinks()).basicRemove(otherEnd, msgs);
			case MappingPackage.LOCATION__CONTAINER:
				return basicSetContainer(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case MappingPackage.LOCATION__CONTAINER:
				return eInternalContainer().eInverseRemove(this, MappingPackage.LOCATION_CONTAINER__CONTENTS,
						LocationContainer.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				return getContents();
			case MappingPackage.LOCATION__SOURCE_LINKS:
				return getSourceLinks();
			case MappingPackage.LOCATION__TARGET_LINKS:
				return getTargetLinks();
			case MappingPackage.LOCATION__CONTAINER:
				return getContainer();
			case MappingPackage.LOCATION__TYPE:
				return getType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection<? extends Location>)newValue);
				return;
			case MappingPackage.LOCATION__SOURCE_LINKS:
				getSourceLinks().clear();
				getSourceLinks().addAll((Collection<? extends Link>)newValue);
				return;
			case MappingPackage.LOCATION__TARGET_LINKS:
				getTargetLinks().clear();
				getTargetLinks().addAll((Collection<? extends Link>)newValue);
				return;
			case MappingPackage.LOCATION__CONTAINER:
				setContainer((LocationContainer)newValue);
				return;
			case MappingPackage.LOCATION__TYPE:
				setType((Serializable)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				getContents().clear();
				return;
			case MappingPackage.LOCATION__SOURCE_LINKS:
				getSourceLinks().clear();
				return;
			case MappingPackage.LOCATION__TARGET_LINKS:
				getTargetLinks().clear();
				return;
			case MappingPackage.LOCATION__CONTAINER:
				setContainer((LocationContainer)null);
				return;
			case MappingPackage.LOCATION__TYPE:
				setType(TYPE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MappingPackage.LOCATION__CONTENTS:
				return contents != null && !contents.isEmpty();
			case MappingPackage.LOCATION__SOURCE_LINKS:
				return sourceLinks != null && !sourceLinks.isEmpty();
			case MappingPackage.LOCATION__TARGET_LINKS:
				return targetLinks != null && !targetLinks.isEmpty();
			case MappingPackage.LOCATION__CONTAINER:
				return getContainer() != null;
			case MappingPackage.LOCATION__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == LocationContainer.class) {
			switch (derivedFeatureID) {
				case MappingPackage.LOCATION__CONTENTS:
					return MappingPackage.LOCATION_CONTAINER__CONTENTS;
				default:
					return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == LocationContainer.class) {
			switch (baseFeatureID) {
				case MappingPackage.LOCATION_CONTAINER__CONTENTS:
					return MappingPackage.LOCATION__CONTENTS;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@link ILocation} adapter.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class LocationAdapter extends AdapterImpl {

		/**
		 * The {@link ILocationListener} to forward notification to.
		 */
		private final ILocationListener listener;

		/**
		 * Constructor.
		 * 
		 * @param listener
		 *            the {@link ILocationListener} to forward notification to
		 */
		private LocationAdapter(ILocationListener listener) {
			this.listener = listener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void notifyChanged(Notification msg) {
			switch (msg.getFeatureID(Location.class)) {
				case MappingPackage.LOCATION__SOURCE_LINKS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.sourceLinkAdded((ILink)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.sourceLinkAdded(link);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.sourceLinkRemoved((ILink)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getOldValue()) {
										((LocationAdapter)adapter).listener.sourceLinkRemoved(link);
									}
								}
							}
							break;
					}
					break;
				case MappingPackage.LOCATION__TARGET_LINKS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.targetLinkAdded((ILink)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.targetLinkAdded(link);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.targetLinkRemoved((ILink)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getOldValue()) {
										((LocationAdapter)adapter).listener.targetLinkRemoved(link);
									}
								}
							}
							break;
					}
					break;
				case MappingPackage.LOCATION__CONTENTS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.contentLocationAdded((ILocation)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILocation location : (List<ILocation>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.contentLocationAdded(location);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.contentLocationRemoved((ILocation)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters()) {
								if (adapter instanceof LocationAdapter) {
									for (ILocation location : (List<ILocation>)msg.getOldValue()) {
										((LocationAdapter)adapter).listener.contentLocationRemoved(location);
									}
								}
							}
							break;
					}
					break;
				case MappingPackage.LOCATION__CONTAINER:
					for (Adapter adapter : eAdapters()) {
						if (adapter instanceof LocationAdapter) {
							((LocationAdapter)adapter).listener.containerChanged((ILocationContainer)msg
									.getOldValue(), (ILocationContainer)msg.getNewValue());
						}
					}
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocation#addListener(org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener)
	 * @generated NOT
	 */
	public void addListener(ILocationListener listener) {
		eAdapters().add(new LocationAdapter(listener));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocation#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener)
	 * @generated NOT
	 */
	public void removeListener(ILocationListener listener) {
		final Iterator<Adapter> it = eAdapters().iterator();
		while (it.hasNext()) {
			final Adapter adapter = it.next();
			if (adapter instanceof LocationAdapter && ((LocationAdapter)adapter).listener == listener) {
				it.remove();
				break;
			}
		}
	}

} // LocationImpl
