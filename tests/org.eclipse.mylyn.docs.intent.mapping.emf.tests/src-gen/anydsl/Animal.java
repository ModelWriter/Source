/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Animal</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Animal#getPart <em>Part</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getAnimal()
 * @model
 * @generated
 */
public interface Animal extends Source {
	/**
	 * Returns the value of the '<em><b>Part</b></em>' attribute list. The list contents are of type
	 * {@link anydsl.Part}. The literals are from the enumeration {@link anydsl.Part}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Part</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Part</em>' attribute list.
	 * @see anydsl.Part
	 * @see anydsl.AnydslPackage#getAnimal_Part()
	 * @model
	 * @generated
	 */
	EList<Part> getPart();

} // Animal
