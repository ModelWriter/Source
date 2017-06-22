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
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
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
public class ReportImpl extends CDOObjectImpl implements Report {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
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
		return (String)eGet(MappingPackage.Literals.REPORT__DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		eSet(MappingPackage.Literals.REPORT__DESCRIPTION, newDescription);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Link getLink() {
		return (Link)eGet(MappingPackage.Literals.REPORT__LINK, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLink(Link newLink) {
		eSet(MappingPackage.Literals.REPORT__LINK, newLink);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setLink(ILink newLink) {
		eSet(MappingPackage.Literals.REPORT__LINK, newLink);
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
