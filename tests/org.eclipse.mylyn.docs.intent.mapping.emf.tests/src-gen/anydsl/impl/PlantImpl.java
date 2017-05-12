/**
 */
package anydsl.impl;

import anydsl.AnydslPackage;
import anydsl.Food;
import anydsl.Kind;
import anydsl.Plant;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Plant</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link anydsl.impl.PlantImpl#getName <em>Name</em>}</li>
 * <li>{@link anydsl.impl.PlantImpl#getFoods <em>Foods</em>}</li>
 * <li>{@link anydsl.impl.PlantImpl#getOrigin <em>Origin</em>}</li>
 * <li>{@link anydsl.impl.PlantImpl#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlantImpl extends MinimalEObjectImpl.Container implements Plant {
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected EList<String> name;

	/**
	 * The cached value of the '{@link #getFoods() <em>Foods</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getFoods()
	 * @generated
	 * @ordered
	 */
	protected EList<Food> foods;

	/**
	 * The cached value of the '{@link #getOrigin() <em>Origin</em>}' attribute list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected EList<anydsl.Country> origin;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final Kind KIND_EDEFAULT = Kind.OTHER;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected Kind kind = KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PlantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnydslPackage.Literals.PLANT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<String> getName() {
		if (name == null) {
			name = new EDataTypeUniqueEList<String>(String.class, this, AnydslPackage.PLANT__NAME);
		}
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Food> getFoods() {
		if (foods == null) {
			foods = new EObjectWithInverseResolvingEList<Food>(Food.class, this, AnydslPackage.PLANT__FOODS,
					AnydslPackage.FOOD__SOURCE);
		}
		return foods;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<anydsl.Country> getOrigin() {
		if (origin == null) {
			origin = new EDataTypeUniqueEList<anydsl.Country>(anydsl.Country.class, this,
					AnydslPackage.PLANT__ORIGIN);
		}
		return origin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setKind(Kind newKind) {
		Kind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnydslPackage.PLANT__KIND, oldKind, kind));
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
			case AnydslPackage.PLANT__FOODS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getFoods()).basicAdd(otherEnd,
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
			case AnydslPackage.PLANT__FOODS:
				return ((InternalEList<?>)getFoods()).basicRemove(otherEnd, msgs);
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
			case AnydslPackage.PLANT__NAME:
				return getName();
			case AnydslPackage.PLANT__FOODS:
				return getFoods();
			case AnydslPackage.PLANT__ORIGIN:
				return getOrigin();
			case AnydslPackage.PLANT__KIND:
				return getKind();
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
			case AnydslPackage.PLANT__NAME:
				getName().clear();
				getName().addAll((Collection<? extends String>)newValue);
				return;
			case AnydslPackage.PLANT__FOODS:
				getFoods().clear();
				getFoods().addAll((Collection<? extends Food>)newValue);
				return;
			case AnydslPackage.PLANT__ORIGIN:
				getOrigin().clear();
				getOrigin().addAll((Collection<? extends anydsl.Country>)newValue);
				return;
			case AnydslPackage.PLANT__KIND:
				setKind((Kind)newValue);
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
			case AnydslPackage.PLANT__NAME:
				getName().clear();
				return;
			case AnydslPackage.PLANT__FOODS:
				getFoods().clear();
				return;
			case AnydslPackage.PLANT__ORIGIN:
				getOrigin().clear();
				return;
			case AnydslPackage.PLANT__KIND:
				setKind(KIND_EDEFAULT);
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
			case AnydslPackage.PLANT__NAME:
				return name != null && !name.isEmpty();
			case AnydslPackage.PLANT__FOODS:
				return foods != null && !foods.isEmpty();
			case AnydslPackage.PLANT__ORIGIN:
				return origin != null && !origin.isEmpty();
			case AnydslPackage.PLANT__KIND:
				return kind != KIND_EDEFAULT;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", origin: ");
		result.append(origin);
		result.append(", kind: ");
		result.append(kind);
		result.append(')');
		return result.toString();
	}

} // PlantImpl
