/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena;

import org.eclipse.mylyn.docs.intent.mapping.Location;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Rdf Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation#getURI <em>URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage#getRdfLocation()
 * @model
 * @generated NOT
 */
public interface RdfLocation extends Location, IRdfLocation {
	/**
	 * Returns the value of the '<em><b>URI</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>URI</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>URI</em>' attribute.
	 * @see #setURI(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage#getRdfLocation_URI()
	 * @model required="true"
	 * @generated
	 */
	String getURI();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation#getURI
	 * <em>URI</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>URI</em>' attribute.
	 * @see #getURI()
	 * @generated
	 */
	void setURI(String value);

} // RdfLocation
