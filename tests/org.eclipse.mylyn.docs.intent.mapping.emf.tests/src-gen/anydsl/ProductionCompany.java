/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Production Company</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.ProductionCompany#getProducers <em>Producers</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getProductionCompany()
 * @model
 * @generated
 */
public interface ProductionCompany extends Company {
	/**
	 * Returns the value of the '<em><b>Producers</b></em>' containment reference list. The list contents are
	 * of type {@link anydsl.Producer}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Producers</em>' containment reference list isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Producers</em>' containment reference list.
	 * @see anydsl.AnydslPackage#getProductionCompany_Producers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Producer> getProducers();

} // ProductionCompany
