/**
 */
package anydsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Adress</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Adress#getZipCode <em>Zip Code</em>}</li>
 * <li>{@link anydsl.Adress#getCity <em>City</em>}</li>
 * <li>{@link anydsl.Adress#getCountry <em>Country</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getAdress()
 * @model
 * @generated
 */
public interface Adress extends EObject {
	/**
	 * Returns the value of the '<em><b>Zip Code</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zip Code</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Zip Code</em>' attribute.
	 * @see #setZipCode(String)
	 * @see anydsl.AnydslPackage#getAdress_ZipCode()
	 * @model
	 * @generated
	 */
	String getZipCode();

	/**
	 * Sets the value of the '{@link anydsl.Adress#getZipCode <em>Zip Code</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Zip Code</em>' attribute.
	 * @see #getZipCode()
	 * @generated
	 */
	void setZipCode(String value);

	/**
	 * Returns the value of the '<em><b>City</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>City</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>City</em>' attribute.
	 * @see #setCity(String)
	 * @see anydsl.AnydslPackage#getAdress_City()
	 * @model
	 * @generated
	 */
	String getCity();

	/**
	 * Sets the value of the '{@link anydsl.Adress#getCity <em>City</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>City</em>' attribute.
	 * @see #getCity()
	 * @generated
	 */
	void setCity(String value);

	/**
	 * Returns the value of the '<em><b>Country</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Country</em>' attribute.
	 * @see #setCountry(anydsl.Country)
	 * @see anydsl.AnydslPackage#getAdress_Country()
	 * @model dataType="anydsl.CountryData"
	 * @generated
	 */
	anydsl.Country getCountry();

	/**
	 * Sets the value of the '{@link anydsl.Adress#getCountry <em>Country</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Country</em>' attribute.
	 * @see #getCountry()
	 * @generated
	 */
	void setCountry(anydsl.Country value);

} // Adress
