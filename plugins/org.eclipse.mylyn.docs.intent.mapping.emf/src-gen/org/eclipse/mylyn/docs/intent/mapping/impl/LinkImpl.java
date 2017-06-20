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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Link</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getDescription <em>Description</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.LinkImpl#getReports <em>Reports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LinkImpl extends CDOObjectImpl implements Link {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
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
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescription() {
		return (String)eGet(MappingPackage.Literals.LINK__DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		eSet(MappingPackage.Literals.LINK__DESCRIPTION, newDescription);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Location getSource() {
		return (Location)eGet(MappingPackage.Literals.LINK__SOURCE, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSource(ILocation newSource) {
		eSet(MappingPackage.Literals.LINK__SOURCE, newSource);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecore.impl.BasicEObjectImpl#eInverseRemove(org.eclipse.emf.ecore.InternalEObject,
	 *      int, org.eclipse.emf.common.notify.NotificationChain)
	 * @generated NOT
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		final ILocation oldLocation;
		if (featureID == MappingPackage.LINK__SOURCE) {
			oldLocation = getSource();
		} else if (featureID == MappingPackage.LINK__TARGET) {
			oldLocation = getTarget();
		} else {
			oldLocation = null;
		}

		final NotificationChain result = super.eInverseRemove(otherEnd, featureID, msgs);

		if (oldLocation != null) {
			final ILocationContainer container = oldLocation.getContainer();
			if (container != null && oldLocation.getTargetLinks().isEmpty() && oldLocation.getSourceLinks()
					.isEmpty()) {
				container.getContents().remove(oldLocation);
			}
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Location getTarget() {
		return (Location)eGet(MappingPackage.Literals.LINK__TARGET, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTarget(ILocation newTarget) {
		eSet(MappingPackage.Literals.LINK__TARGET, newTarget);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Serializable getType() {
		return (Serializable)eGet(MappingPackage.Literals.LINK__TYPE, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(Serializable newType) {
		eSet(MappingPackage.Literals.LINK__TYPE, newType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<IReport> getReports() {
		return (EList<IReport>)eGet(MappingPackage.Literals.LINK__REPORTS, true);
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
