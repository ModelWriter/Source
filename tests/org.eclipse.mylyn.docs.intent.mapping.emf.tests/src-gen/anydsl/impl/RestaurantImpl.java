/**
 */
package anydsl.impl;

import anydsl.Adress;
import anydsl.AnydslPackage;
import anydsl.Chef;
import anydsl.Recipe;
import anydsl.Restaurant;
import anydsl.World;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Restaurant</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link anydsl.impl.RestaurantImpl#getName <em>Name</em>}</li>
 * <li>{@link anydsl.impl.RestaurantImpl#getAdress <em>Adress</em>}</li>
 * <li>{@link anydsl.impl.RestaurantImpl#getWorld <em>World</em>}</li>
 * <li>{@link anydsl.impl.RestaurantImpl#getChefs <em>Chefs</em>}</li>
 * <li>{@link anydsl.impl.RestaurantImpl#getMenu <em>Menu</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RestaurantImpl extends MinimalEObjectImpl.Container implements Restaurant {
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
	 * The cached value of the '{@link #getAdress() <em>Adress</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAdress()
	 * @generated
	 * @ordered
	 */
	protected Adress adress;

	/**
	 * The cached value of the '{@link #getWorld() <em>World</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getWorld()
	 * @generated
	 * @ordered
	 */
	protected World world;

	/**
	 * The cached value of the '{@link #getChefs() <em>Chefs</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getChefs()
	 * @generated
	 * @ordered
	 */
	protected EList<Chef> chefs;

	/**
	 * The cached value of the '{@link #getMenu() <em>Menu</em>}' map. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getMenu()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Recipe> menu;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected RestaurantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnydslPackage.Literals.RESTAURANT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnydslPackage.RESTAURANT__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Adress getAdress() {
		return adress;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetAdress(Adress newAdress, NotificationChain msgs) {
		Adress oldAdress = adress;
		adress = newAdress;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AnydslPackage.RESTAURANT__ADRESS, oldAdress, newAdress);
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
	public void setAdress(Adress newAdress) {
		if (newAdress != adress) {
			NotificationChain msgs = null;
			if (adress != null)
				msgs = ((InternalEObject)adress).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
						- AnydslPackage.RESTAURANT__ADRESS, null, msgs);
			if (newAdress != null)
				msgs = ((InternalEObject)newAdress).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
						- AnydslPackage.RESTAURANT__ADRESS, null, msgs);
			msgs = basicSetAdress(newAdress, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnydslPackage.RESTAURANT__ADRESS, newAdress,
					newAdress));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public World getWorld() {
		if (world != null && world.eIsProxy()) {
			InternalEObject oldWorld = (InternalEObject)world;
			world = (World)eResolveProxy(oldWorld);
			if (world != oldWorld) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnydslPackage.RESTAURANT__WORLD,
							oldWorld, world));
			}
		}
		return world;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public World basicGetWorld() {
		return world;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWorld(World newWorld) {
		World oldWorld = world;
		world = newWorld;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnydslPackage.RESTAURANT__WORLD, oldWorld,
					world));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Chef> getChefs() {
		if (chefs == null) {
			chefs = new EObjectContainmentEList<Chef>(Chef.class, this, AnydslPackage.RESTAURANT__CHEFS);
		}
		return chefs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, Recipe> getMenu() {
		if (menu == null) {
			menu = new EcoreEMap<String, Recipe>(AnydslPackage.Literals.ESTRING_TO_RECIPE_MAP,
					EStringToRecipeMapImpl.class, this, AnydslPackage.RESTAURANT__MENU);
		}
		return menu;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnydslPackage.RESTAURANT__ADRESS:
				return basicSetAdress(null, msgs);
			case AnydslPackage.RESTAURANT__CHEFS:
				return ((InternalEList<?>)getChefs()).basicRemove(otherEnd, msgs);
			case AnydslPackage.RESTAURANT__MENU:
				return ((InternalEList<?>)getMenu()).basicRemove(otherEnd, msgs);
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
			case AnydslPackage.RESTAURANT__NAME:
				return getName();
			case AnydslPackage.RESTAURANT__ADRESS:
				return getAdress();
			case AnydslPackage.RESTAURANT__WORLD:
				if (resolve)
					return getWorld();
				return basicGetWorld();
			case AnydslPackage.RESTAURANT__CHEFS:
				return getChefs();
			case AnydslPackage.RESTAURANT__MENU:
				if (coreType)
					return getMenu();
				else
					return getMenu().map();
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
			case AnydslPackage.RESTAURANT__NAME:
				setName((String)newValue);
				return;
			case AnydslPackage.RESTAURANT__ADRESS:
				setAdress((Adress)newValue);
				return;
			case AnydslPackage.RESTAURANT__WORLD:
				setWorld((World)newValue);
				return;
			case AnydslPackage.RESTAURANT__CHEFS:
				getChefs().clear();
				getChefs().addAll((Collection<? extends Chef>)newValue);
				return;
			case AnydslPackage.RESTAURANT__MENU:
				((EStructuralFeature.Setting)getMenu()).set(newValue);
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
			case AnydslPackage.RESTAURANT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnydslPackage.RESTAURANT__ADRESS:
				setAdress((Adress)null);
				return;
			case AnydslPackage.RESTAURANT__WORLD:
				setWorld((World)null);
				return;
			case AnydslPackage.RESTAURANT__CHEFS:
				getChefs().clear();
				return;
			case AnydslPackage.RESTAURANT__MENU:
				getMenu().clear();
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
			case AnydslPackage.RESTAURANT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnydslPackage.RESTAURANT__ADRESS:
				return adress != null;
			case AnydslPackage.RESTAURANT__WORLD:
				return world != null;
			case AnydslPackage.RESTAURANT__CHEFS:
				return chefs != null && !chefs.isEmpty();
			case AnydslPackage.RESTAURANT__MENU:
				return menu != null && !menu.isEmpty();
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
		result.append(')');
		return result.toString();
	}

} // RestaurantImpl
