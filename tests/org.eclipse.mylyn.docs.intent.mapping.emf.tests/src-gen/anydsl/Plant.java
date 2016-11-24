/**
 */
package anydsl;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Plant</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Plant#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getPlant()
 * @model
 * @generated
 */
public interface Plant extends Source {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute. The literals are from the enumeration
	 * {@link anydsl.Kind}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see anydsl.Kind
	 * @see #setKind(Kind)
	 * @see anydsl.AnydslPackage#getPlant_Kind()
	 * @model
	 * @generated
	 */
	Kind getKind();

	/**
	 * Sets the value of the '{@link anydsl.Plant#getKind <em>Kind</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Kind</em>' attribute.
	 * @see anydsl.Kind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(Kind value);

} // Plant
