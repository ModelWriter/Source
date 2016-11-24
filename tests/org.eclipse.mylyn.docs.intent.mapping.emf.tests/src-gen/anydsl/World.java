/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>World</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.World#getCompanies <em>Companies</em>}</li>
 * <li>{@link anydsl.World#getFoods <em>Foods</em>}</li>
 * <li>{@link anydsl.World#getSources <em>Sources</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getWorld()
 * @model annotation="http://www.obeo.fr/dsl/dnc/archetype archetype='MomentInterval'"
 * @generated
 */
public interface World extends EObject {
	/**
	 * Returns the value of the '<em><b>Companies</b></em>' containment reference list. The list contents are
	 * of type {@link anydsl.Company}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Companies</em>' containment reference list isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Companies</em>' containment reference list.
	 * @see anydsl.AnydslPackage#getWorld_Companies()
	 * @model containment="true"
	 * @generated
	 */
	EList<Company> getCompanies();

	/**
	 * Returns the value of the '<em><b>Foods</b></em>' containment reference list. The list contents are of
	 * type {@link anydsl.Food}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foods</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Foods</em>' containment reference list.
	 * @see anydsl.AnydslPackage#getWorld_Foods()
	 * @model containment="true"
	 * @generated
	 */
	EList<Food> getFoods();

	/**
	 * Returns the value of the '<em><b>Sources</b></em>' containment reference list. The list contents are of
	 * type {@link anydsl.Source}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sources</em>' containment reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Sources</em>' containment reference list.
	 * @see anydsl.AnydslPackage#getWorld_Sources()
	 * @model containment="true"
	 * @generated
	 */
	EList<Source> getSources();

} // World
