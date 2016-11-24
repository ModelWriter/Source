/**
 */
package anydsl;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Company</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Company#getAdress <em>Adress</em>}</li>
 * <li>{@link anydsl.Company#getWorld <em>World</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getCompany()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Company extends NamedElement {
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
	 * @see anydsl.AnydslPackage#getCompany_Adress()
	 * @model containment="true"
	 * @generated
	 */
	Adress getAdress();

	/**
	 * Sets the value of the '{@link anydsl.Company#getAdress <em>Adress</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Adress</em>' containment reference.
	 * @see #getAdress()
	 * @generated
	 */
	void setAdress(Adress value);

	/**
	 * Returns the value of the '<em><b>World</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>World</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>World</em>' reference.
	 * @see #setWorld(World)
	 * @see anydsl.AnydslPackage#getCompany_World()
	 * @model
	 * @generated
	 */
	World getWorld();

	/**
	 * Sets the value of the '{@link anydsl.Company#getWorld <em>World</em>}' reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>World</em>' reference.
	 * @see #getWorld()
	 * @generated
	 */
	void setWorld(World value);

} // Company
