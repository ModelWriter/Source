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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.FactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Base</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getContents <em>Contents</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl#getReports <em>Reports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BaseImpl extends MinimalEObjectImpl.Container implements Base {
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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReports() <em>Reports</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReports()
	 * @generated NOT
	 * @ordered
	 */
	protected EList<IReport> reports;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BaseImpl() {
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
	 * @generated NOT
	 */
	public EList<ILocation> getContents() {
		if (contents == null) {
			contents = new EObjectContainmentWithInverseEList<ILocation>(Location.class, this,
					MappingPackage.BASE__CONTENTS, MappingPackage.LOCATION__CONTAINER);
		}
		return contents;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.BASE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<IReport> getReports() {
		if (reports == null) {
			reports = new EObjectContainmentEList<IReport>(Report.class, this, MappingPackage.BASE__REPORTS);
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
			case MappingPackage.BASE__CONTENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getContents()).basicAdd(otherEnd,
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
			case MappingPackage.BASE__CONTENTS:
				return ((InternalEList<?>)getContents()).basicRemove(otherEnd, msgs);
			case MappingPackage.BASE__REPORTS:
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.BASE__CONTENTS:
				return getContents();
			case MappingPackage.BASE__NAME:
				return getName();
			case MappingPackage.BASE__REPORTS:
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
			case MappingPackage.BASE__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection<? extends Location>)newValue);
				return;
			case MappingPackage.BASE__NAME:
				setName((String)newValue);
				return;
			case MappingPackage.BASE__REPORTS:
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
			case MappingPackage.BASE__CONTENTS:
				getContents().clear();
				return;
			case MappingPackage.BASE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MappingPackage.BASE__REPORTS:
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
			case MappingPackage.BASE__CONTENTS:
				return contents != null && !contents.isEmpty();
			case MappingPackage.BASE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MappingPackage.BASE__REPORTS:
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
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
							((BaseAdapter)adapter).listener.nameChanged((String)msg.getOldValue(),
									(String)msg.getNewValue());
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
		res.addDescriptor(ITextLocation.class,
				new FactoryDescriptor<TextLocationImpl>(TextLocationImpl.class));
		res.addDescriptor(IEObjectLocation.class, new FactoryDescriptor<EObjectLocationImpl>(
				EObjectLocationImpl.class));

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
