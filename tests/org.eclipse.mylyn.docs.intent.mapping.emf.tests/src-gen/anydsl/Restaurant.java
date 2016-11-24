/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Restaurant</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Restaurant#getChefs <em>Chefs</em>}</li>
 * <li>{@link anydsl.Restaurant#getMenu <em>Menu</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getRestaurant()
 * @model
 * @generated
 */
public interface Restaurant extends Company {
	/**
	 * Returns the value of the '<em><b>Chefs</b></em>' containment reference list. The list contents are of
	 * type {@link anydsl.Chef}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chefs</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Chefs</em>' containment reference list.
	 * @see anydsl.AnydslPackage#getRestaurant_Chefs()
	 * @model containment="true"
	 * @generated
	 */
	EList<Chef> getChefs();

	/**
	 * Returns the value of the '<em><b>Menu</b></em>' map. The key is of type {@link java.lang.String}, and
	 * the value is of type {@link anydsl.Recipe}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Menu</em>' map isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Menu</em>' map.
	 * @see anydsl.AnydslPackage#getRestaurant_Menu()
	 * @model mapType="anydsl.EStringToRecipeMap<org.eclipse.emf.ecore.EString, anydsl.Recipe>"
	 * @generated
	 */
	EMap<String, Recipe> getMenu();

} // Restaurant
