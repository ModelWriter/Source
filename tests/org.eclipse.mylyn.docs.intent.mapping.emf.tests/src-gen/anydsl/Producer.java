/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Producer</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Producer#getAdress <em>Adress</em>}</li>
 * <li>{@link anydsl.Producer#getCompany <em>Company</em>}</li>
 * <li>{@link anydsl.Producer#getFoods <em>Foods</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getProducer()
 * @model
 * @generated
 */
public interface Producer extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Adress</b></em>' containment reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adress</em>' containment reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Adress</em>' containment reference.
	 * @see #setAdress(Adress)
	 * @see anydsl.AnydslPackage#getProducer_Adress()
	 * @model containment="true"
	 * @generated
	 */
	Adress getAdress();

	/**
	 * Sets the value of the '{@link anydsl.Producer#getAdress <em>Adress</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Adress</em>' containment reference.
	 * @see #getAdress()
	 * @generated
	 */
	void setAdress(Adress value);

	/**
	 * Returns the value of the '<em><b>Company</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Company</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Company</em>' reference.
	 * @see #setCompany(Company)
	 * @see anydsl.AnydslPackage#getProducer_Company()
	 * @model
	 * @generated
	 */
	Company getCompany();

	/**
	 * Sets the value of the '{@link anydsl.Producer#getCompany <em>Company</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Company</em>' reference.
	 * @see #getCompany()
	 * @generated
	 */
	void setCompany(Company value);

	/**
	 * Returns the value of the '<em><b>Foods</b></em>' reference list. The list contents are of type
	 * {@link anydsl.Food}. It is bidirectional and its opposite is '{@link anydsl.Food#getProducers
	 * <em>Producers</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foods</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Foods</em>' reference list.
	 * @see anydsl.AnydslPackage#getProducer_Foods()
	 * @see anydsl.Food#getProducers
	 * @model opposite="producers"
	 * @generated
	 */
	EList<Food> getFoods();

} // Producer
