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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICouple;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Base</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getContents <em>Contents</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getReports <em>Reports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseImpl extends CDOObjectImpl implements Base {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BaseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.BASE;
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
	public EList<ILocation> getContents() {
		return (EList<ILocation>)eGet(MappingPackage.Literals.LOCATION_CONTAINER__CONTENTS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return (String)eGet(MappingPackage.Literals.BASE__NAME, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		eSet(MappingPackage.Literals.BASE__NAME, newName);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public EList<IReport> getReports() {
		return (EList<IReport>)eGet(MappingPackage.Literals.BASE__REPORTS, true);
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
				case MappingPackage.BASE__CONTENTS:
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
					return MappingPackage.BASE__CONTENTS;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * {@link IBase} adapter.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class BaseAdapter extends AdapterImpl {

		/**
		 * The {@link IBaseListener} to forward notification to.
		 */
		private final IBaseListener listener;

		/**
		 * Constructor.
		 * 
		 * @param listener
		 *            the {@link IBaseListener} to forward notification to
		 */
		private BaseAdapter(IBaseListener listener) {
			this.listener = listener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void notifyChanged(Notification msg) {
			final List<Adapter> eAdapters = new ArrayList<Adapter>(eAdapters());
			switch (msg.getFeatureID(Base.class)) {
				case MappingPackage.BASE__NAME:
					for (Adapter adapter : eAdapters) {
						if (adapter instanceof BaseAdapter) {
							((BaseAdapter)adapter).listener.nameChanged((String)msg.getOldValue(), (String)msg
									.getNewValue());
						}
					}
					break;
				case MappingPackage.BASE__CONTENTS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									((BaseAdapter)adapter).listener.contentsAdded((ILocation)msg
											.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									for (ILocation location : (List<ILocation>)msg.getNewValue()) {
										((BaseAdapter)adapter).listener.contentsAdded(location);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									((BaseAdapter)adapter).listener.contentsRemoved((ILocation)msg
											.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									for (ILocation location : (List<ILocation>)msg.getOldValue()) {
										((BaseAdapter)adapter).listener.contentsRemoved(location);
									}
								}
							}
							break;
					}
					break;
				case MappingPackage.BASE__REPORTS:
					switch (msg.getEventType()) {
						case Notification.ADD:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									((BaseAdapter)adapter).listener.reportAdded((IReport)msg.getNewValue());
								}
							}
							break;
						case Notification.ADD_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									for (IReport report : (List<IReport>)msg.getNewValue()) {
										((BaseAdapter)adapter).listener.reportAdded(report);
									}
								}
							}
							break;
						case Notification.REMOVE:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									((BaseAdapter)adapter).listener.reportRemoved((IReport)msg.getOldValue());
								}
							}
							break;
						case Notification.REMOVE_MANY:
							for (Adapter adapter : eAdapters) {
								if (adapter instanceof BaseAdapter) {
									for (IReport report : (List<IReport>)msg.getOldValue()) {
										((BaseAdapter)adapter).listener.reportRemoved(report);
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBase#addListener(org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener)
	 * @generated NOT
	 */
	public void addListener(IBaseListener listener) {
		eAdapters().add(new BaseAdapter(listener));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBase#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener)
	 * @generated NOT
	 */
	public void removeListener(IBaseListener listener) {
		final Iterator<Adapter> it = eAdapters().iterator();
		while (it.hasNext()) {
			final Adapter adapter = it.next();
			if (adapter instanceof BaseAdapter && ((BaseAdapter)adapter).listener == listener) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * The {@link BaseElementFactory}.
	 * 
	 * @generated NOT
	 */
	private final BaseElementFactory factory = initElementFactory();

	private BaseElementFactory initElementFactory() {
		BaseElementFactory res = new BaseElementFactory();

		res.addDescriptor(ILink.class, new FactoryDescriptor<LinkImpl>(LinkImpl.class));
		res.addDescriptor(IReport.class, new FactoryDescriptor<ReportImpl>(ReportImpl.class));
		res.addDescriptor(ITextLocation.class, new FactoryDescriptor<TextLocationImpl>(
				TextLocationImpl.class));
		res.addDescriptor(IEObjectLocation.class, new FactoryDescriptor<EObjectLocationImpl>(
				EObjectLocationImpl.class));
		res.addDescriptor(ICouple.class, new FactoryDescriptor<CoupleImpl>(CoupleImpl.class));

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBase#getFactory()
	 * @generated NOT
	 */
	public BaseElementFactory getFactory() {
		return factory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBase#save()
	 * @generated NOT
	 */
	public void save() throws IOException {
		final Resource eResource = this.eResource();
		if (eResource != null) {
			eResource.save(null);
		} else {
			throw new IOException("mapping base " + getName()
					+ " is not contained in a resource and can't be saved.");
		}
	}
} // BaseImpl
