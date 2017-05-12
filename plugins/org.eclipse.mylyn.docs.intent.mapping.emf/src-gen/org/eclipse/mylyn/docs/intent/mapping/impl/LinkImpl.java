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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Link</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getDescription <em>Description</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getReports <em>Reports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LinkImpl extends MinimalEObjectImpl.Container implements Link {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected Location target;

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
	 * The cached value of the '{@link #getReports() <em>Reports</em>}' reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getReports()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<IReport> reports;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public LinkImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.LINK;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LINK__DESCRIPTION,
					oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Location getSource() {
		if (eContainerFeatureID() != MappingPackage.LINK__SOURCE)
			return null;
		return (Location)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public NotificationChain basicSetSource(Location newSource, NotificationChain msgs) {
		final ILocation oldSource = getSource();

		msgs = eBasicSetContainer((InternalEObject)newSource, MappingPackage.LINK__SOURCE, msgs);

		if (oldSource != null) {
			final ILocationContainer container = oldSource.getContainer();
			if (container != null && oldSource.getTargetLinks().isEmpty() && oldSource.getSourceLinks()
					.isEmpty()) {
				container.getContents().remove(oldSource);
			}
		}

		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSource(Location newSource) {
		if (newSource != eInternalContainer() || (eContainerFeatureID() != MappingPackage.LINK__SOURCE
				&& newSource != null)) {
			if (EcoreUtil.isAncestor(this, newSource))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSource != null)
				msgs = ((InternalEObject)newSource).eInverseAdd(this, MappingPackage.LOCATION__TARGET_LINKS,
						Location.class, msgs);
			msgs = basicSetSource(newSource, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LINK__SOURCE, newSource,
					newSource));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Location getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = (Location)eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.LINK__TARGET,
							oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Location basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public NotificationChain basicSetTarget(Location newTarget, NotificationChain msgs) {
		final Location oldTarget = target;

		target = newTarget;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					MappingPackage.LINK__TARGET, oldTarget, newTarget);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}

		if (oldTarget != null) {
			final ILocationContainer container = oldTarget.getContainer();
			if (container != null && oldTarget.getSourceLinks().isEmpty() && oldTarget.getTargetLinks()
					.isEmpty()) {
				container.getContents().remove(oldTarget);
			}
		}

		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTarget(Location newTarget) {
		if (newTarget != target) {
			NotificationChain msgs = null;
			if (target != null)
				msgs = ((InternalEObject)target).eInverseRemove(this, MappingPackage.LOCATION__SOURCE_LINKS,
						Location.class, msgs);
			if (newTarget != null)
				msgs = ((InternalEObject)newTarget).eInverseAdd(this, MappingPackage.LOCATION__SOURCE_LINKS,
						Location.class, msgs);
			msgs = basicSetTarget(newTarget, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LINK__TARGET, newTarget,
					newTarget));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.LINK__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<IReport> getReports() {
		if (reports == null) {
			reports = new EObjectWithInverseResolvingEList<IReport>(Report.class, this,
					MappingPackage.LINK__REPORTS, MappingPackage.REPORT__LINK);
		}
		return reports;
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
			case MappingPackage.LINK__SOURCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSource((Location)otherEnd, msgs);
			case MappingPackage.LINK__TARGET:
				if (target != null)
					msgs = ((InternalEObject)target).eInverseRemove(this,
							MappingPackage.LOCATION__SOURCE_LINKS, Location.class, msgs);
				return basicSetTarget((Location)otherEnd, msgs);
			case MappingPackage.LINK__REPORTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReports()).basicAdd(otherEnd,
						msgs);
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
			case MappingPackage.LINK__SOURCE:
				return basicSetSource(null, msgs);
			case MappingPackage.LINK__TARGET:
				return basicSetTarget(null, msgs);
			case MappingPackage.LINK__REPORTS:
				return ((InternalEList<?>)getReports()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.LINK__SOURCE:
				return eInternalContainer().eInverseRemove(this, MappingPackage.LOCATION__TARGET_LINKS,
						Location.class, msgs);
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
			case MappingPackage.LINK__DESCRIPTION:
				return getDescription();
			case MappingPackage.LINK__SOURCE:
				return getSource();
			case MappingPackage.LINK__TARGET:
				if (resolve)
					return getTarget();
				return basicGetTarget();
			case MappingPackage.LINK__TYPE:
				return getType();
			case MappingPackage.LINK__REPORTS:
				return getReports();
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
			case MappingPackage.LINK__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case MappingPackage.LINK__SOURCE:
				setSource((Location)newValue);
				return;
			case MappingPackage.LINK__TARGET:
				setTarget((Location)newValue);
				return;
			case MappingPackage.LINK__TYPE:
				setType((Serializable)newValue);
				return;
			case MappingPackage.LINK__REPORTS:
				getReports().clear();
				getReports().addAll((Collection<? extends Report>)newValue);
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
			case MappingPackage.LINK__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case MappingPackage.LINK__SOURCE:
				setSource((Location)null);
				return;
			case MappingPackage.LINK__TARGET:
				setTarget((Location)null);
				return;
			case MappingPackage.LINK__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case MappingPackage.LINK__REPORTS:
				getReports().clear();
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
			case MappingPackage.LINK__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null
						: !DESCRIPTION_EDEFAULT.equals(description);
			case MappingPackage.LINK__SOURCE:
				return getSource() != null;
			case MappingPackage.LINK__TARGET:
				return target != null;
			case MappingPackage.LINK__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case MappingPackage.LINK__REPORTS:
				return reports != null && !reports.isEmpty();
		}
		return super.eIsSet(featureID);
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
		result.append(" (description: ");
		result.append(description);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILink#setSource(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 * @generated NOT
	 */
	public void setSource(ILocation location) {
		assert location instanceof Location;

		setSource((Location)location);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILink#setTarget(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 * @generated NOT
	 */
	public void setTarget(ILocation location) {
		assert location instanceof Location;

		setTarget((Location)location);
	}

	/**
	 * {@link ILink} adapter.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class LinkAdapter extends AdapterImpl {

		/**
		 * The {@link ILinkListener} to forward notification to.
		 */
		private final ILinkListener listener;

		/**
		 * Constructor.
		 * 
		 * @param listener
		 *            the {@link ILinkListener} to forward notification to
		 */
		private LinkAdapter(ILinkListener listener) {
			this.listener = listener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void notifyChanged(Notification msg) {
			final List<Adapter> eAdapters = new ArrayList<Adapter>(eAdapters());
			switch (msg.getFeatureID(Link.class)) {
				case MappingPackage.LINK__DESCRIPTION:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof LinkAdapter) {
							((LinkAdapter)adapter).listener.descriptionChanged((String)msg.getOldValue(),
									(String)msg.getNewValue());
						}
					}
					break;
				case MappingPackage.LINK__SOURCE:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof LinkAdapter) {
							((LinkAdapter)adapter).listener.sourceChanged((Location)msg.getOldValue(),
									(Location)msg.getNewValue());
						}
					}
					break;
				case MappingPackage.LINK__TARGET:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof LinkAdapter) {
							((LinkAdapter)adapter).listener.targetChanged((Location)msg.getOldValue(),
									(Location)msg.getNewValue());
						}
					}
					break;
				case MappingPackage.LINK__REPORTS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LinkAdapter) {
									((LinkAdapter)adapter).listener.reportAdded((IReport)msg.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LinkAdapter) {
									for (IReport report : (List<IReport>)msg.getNewValue()) {
										((LinkAdapter)adapter).listener.reportAdded(report);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LinkAdapter) {
									((LinkAdapter)adapter).listener.reportRemoved((IReport)msg.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof LinkAdapter) {
									for (IReport report : (List<IReport>)msg.getOldValue()) {
										((LinkAdapter)adapter).listener.reportRemoved(report);
									}
								}
							}
					}
					break;
			}
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILink#addListener(org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener)
	 * @generated NOT
	 */
	public void addListener(ILinkListener listener) {
		eAdapters().add(new LinkAdapter(listener));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILink#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener)
	 * @generated NOT
	 */
	public void removeListener(ILinkListener listener) {
		final Iterator<Adapter> it = eAdapters().iterator();
		while (it.hasNext()) {
			final Adapter adapter = it.next();
			if (adapter instanceof LinkAdapter && ((LinkAdapter)adapter).listener == listener) {
				it.remove();
				break;
			}
		}
	}

} // LinkImpl
