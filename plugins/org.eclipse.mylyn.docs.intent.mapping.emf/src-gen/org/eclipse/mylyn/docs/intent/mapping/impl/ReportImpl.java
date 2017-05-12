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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.base.IReportListener;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Report</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.ReportImpl#getDescription <em>Description</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.ReportImpl#getLink <em>Link</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReportImpl extends MinimalEObjectImpl.Container implements Report {
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
	 * The cached value of the '{@link #getLink() <em>Link</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLink()
	 * @generated
	 * @ordered
	 */
	protected Link link;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ReportImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.REPORT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.REPORT__DESCRIPTION,
					oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ILink getLink() {
		if (link != null && link.eIsProxy()) {
			InternalEObject oldLink = (InternalEObject)link;
			link = (Link)eResolveProxy(oldLink);
			if (link != oldLink) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.REPORT__LINK,
							oldLink, link));
			}
		}
		return link;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Link basicGetLink() {
		return link;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLink(Link newLink, NotificationChain msgs) {
		Link oldLink = link;
		link = newLink;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					MappingPackage.REPORT__LINK, oldLink, newLink);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLink(Link newLink) {
		if (newLink != link) {
			NotificationChain msgs = null;
			if (link != null)
				msgs = ((InternalEObject)link).eInverseRemove(this, MappingPackage.LINK__REPORTS, Link.class,
						msgs);
			if (newLink != null)
				msgs = ((InternalEObject)newLink).eInverseAdd(this, MappingPackage.LINK__REPORTS, Link.class,
						msgs);
			msgs = basicSetLink(newLink, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.REPORT__LINK, newLink,
					newLink));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setLink(ILink newLink) {
		if (newLink != link) {
			NotificationChain msgs = null;
			if (link != null)
				msgs = ((InternalEObject)link).eInverseRemove(this, MappingPackage.LINK__REPORTS, Link.class,
						msgs);
			if (newLink != null)
				msgs = ((InternalEObject)newLink).eInverseAdd(this, MappingPackage.LINK__REPORTS, Link.class,
						msgs);
			msgs = basicSetLink((Link)newLink, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.REPORT__LINK, newLink,
					newLink));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.REPORT__LINK:
				if (link != null)
					msgs = ((InternalEObject)link).eInverseRemove(this, MappingPackage.LINK__REPORTS,
							Link.class, msgs);
				return basicSetLink((Link)otherEnd, msgs);
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
			case MappingPackage.REPORT__LINK:
				return basicSetLink(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.REPORT__DESCRIPTION:
				return getDescription();
			case MappingPackage.REPORT__LINK:
				if (resolve)
					return getLink();
				return basicGetLink();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MappingPackage.REPORT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case MappingPackage.REPORT__LINK:
				setLink((Link)newValue);
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
			case MappingPackage.REPORT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case MappingPackage.REPORT__LINK:
				setLink((Link)null);
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
			case MappingPackage.REPORT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null
						: !DESCRIPTION_EDEFAULT.equals(description);
			case MappingPackage.REPORT__LINK:
				return link != null;
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
		result.append(')');
		return result.toString();
	}

	/**
	 * {@link IReport} adapter.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class ReportAdapter extends AdapterImpl {

		/**
		 * The {@link IReportListener} to forward notification to.
		 */
		private final IReportListener listener;

		/**
		 * Constructor.
		 * 
		 * @param listener
		 *            the {@link IReportListener} to forward notification to
		 */
		private ReportAdapter(IReportListener listener) {
			this.listener = listener;
		}

		@Override
		public void notifyChanged(Notification msg) {
			final List<Adapter> eAdapters = new ArrayList<Adapter>(eAdapters());
			switch (msg.getFeatureID(Report.class)) {
				case MappingPackage.REPORT__DESCRIPTION:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof ReportAdapter) {
							((ReportAdapter)adapter).listener.descriptionChanged((String)msg.getOldValue(),
									(String)msg.getNewValue());
						}
					}
					break;
				case MappingPackage.REPORT__LINK:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof ReportAdapter) {
							((ReportAdapter)adapter).listener.linkChanged((ILink)msg.getOldValue(), (ILink)msg
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IReport#addListener(org.eclipse.mylyn.docs.intent.mapping.base.IReportListener)
	 */
	public void addListener(IReportListener listener) {
		eAdapters().add(new ReportAdapter(listener));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IReport#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.IReportListener)
	 */
	public void removeListener(IReportListener listener) {
		final Iterator<Adapter> it = eAdapters().iterator();
		while (it.hasNext()) {
			final Adapter adapter = it.next();
			if (adapter instanceof ReportAdapter && ((ReportAdapter)adapter).listener == listener) {
				it.remove();
				break;
			}
		}
	}

} // ReportImpl
