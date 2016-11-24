/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Source</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Source#getFoods <em>Foods</em>}</li>
 * <li>{@link anydsl.Source#getOrigin <em>Origin</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getSource()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Source extends MultiNamedElement {
	/**
	 * Returns the value of the '<em><b>Foods</b></em>' reference list. The list contents are of type
	 * {@link anydsl.Food}. It is bidirectional and its opposite is '{@link anydsl.Food#getSource
	 * <em>Source</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foods</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Foods</em>' reference list.
	 * @see anydsl.AnydslPackage#getSource_Foods()
	 * @see anydsl.Food#getSource
	 * @model opposite="source"
	 * @generated
	 */
	EList<Food> getFoods();

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' attribute list. The list contents are of type
	 * {@link anydsl.Country}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Origin</em>' attribute list.
	 * @see anydsl.AnydslPackage#getSource_Origin()
	 * @model dataType="anydsl.CountryData"
	 * @generated
	 */
	EList<anydsl.Country> getOrigin();

} // Source
