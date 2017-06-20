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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
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
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getSourceLinks <em>Source
 * Links</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getTargetLinks <em>Target
 * Links</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getContents <em>Contents</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getContainer <em>Container</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl#getReferencingScopes <em>Referencing
 * Scopes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class LocationImpl extends CDOObjectImpl implements Location {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LocationImpl() {
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
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<ILink> getSourceLinks() {
		return (EList<ILink>)eGet(MappingPackage.Literals.LOCATION__SOURCE_LINKS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<ILink> getTargetLinks() {
		return (EList<ILink>)eGet(MappingPackage.Literals.LOCATION__TARGET_LINKS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<ILocation> getContents() {
		return (EList<ILocation>)eGet(MappingPackage.Literals.LOCATION_CONTAINER__CONTENTS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LocationContainer getContainer() {
		return (LocationContainer)eGet(MappingPackage.Literals.LOCATION__CONTAINER, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setContainer(ILocationContainer newContainer) {
		eSet(MappingPackage.Literals.LOCATION__CONTAINER, newContainer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Serializable getType() {
		return (Serializable)eGet(MappingPackage.Literals.LOCATION__TYPE, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(Serializable newType) {
		eSet(MappingPackage.Literals.LOCATION__TYPE, newType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isMarkedAsDeleted() {
		return (Boolean)eGet(MappingPackage.Literals.LOCATION__MARKED_AS_DELETED, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMarkedAsDeleted(boolean newMarkedAsDeleted) {
		eSet(MappingPackage.Literals.LOCATION__MARKED_AS_DELETED, newMarkedAsDeleted);
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
		result.append(getType());
		result.append(", markedAsDeleted: ");
		result.append(isMarkedAsDeleted());
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
			final List<Adapter> eAdapters = new ArrayList<Adapter>(eAdapters());
			switch (msg.getFeatureID(Location.class)) {
				case MappingPackage.LOCATION__SOURCE_LINKS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.sourceLinkAdded((ILink)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.sourceLinkAdded(link);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.sourceLinkRemoved((ILink)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
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
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.targetLinkAdded((ILink)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									for (ILink link : (List<ILink>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.targetLinkAdded(link);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.targetLinkRemoved((ILink)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
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
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.contentsAdded((ILocation)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									for (ILocation location : (List<ILocation>)msg.getNewValue()) {
										((LocationAdapter)adapter).listener.contentsAdded(location);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									((LocationAdapter)adapter).listener.contentsRemoved((ILocation)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LocationAdapter) {
									for (ILocation location : (List<ILocation>)msg.getOldValue()) {
										((LocationAdapter)adapter).listener.contentsRemoved(location);
									}
								}
							}
							break;
					}
					break;
				case MappingPackage.LOCATION__CONTAINER:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof LocationAdapter) {
							((LocationAdapter)adapter).listener.containerChanged((ILocationContainer)msg
									.getOldValue(), (ILocationContainer)msg.getNewValue());
						}
					}
					break;
				case MappingPackage.LOCATION__MARKED_AS_DELETED:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof LocationAdapter) {
							((LocationAdapter)adapter).listener.markedAsDeletedChanged((Boolean)msg
									.getNewValue());
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

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocation#change(java.lang.String)
	 * @generated NOT
	 */
	public void change(String reportDescription) {
		final List<Adapter> eAdapters = new ArrayList<Adapter>(eAdapters());
		for (Adapter adapter : eAdapters) {
			if (adapter instanceof LocationAdapter) {
				((LocationAdapter)adapter).listener.changed(reportDescription);
			}
		}
	}

} // LocationImpl
