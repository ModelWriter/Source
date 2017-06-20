/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.mapping.impl.LocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage;
import org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rdf Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.jena.impl.RdfLocationImpl#getURI <em>URI</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RdfLocationImpl extends LocationImpl implements RdfLocation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public RdfLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JenaPackage.Literals.RDF_LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getURI() {
		return (String)eGet(JenaPackage.Literals.RDF_LOCATION__URI, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setURI(String newURI) {
		eSet(JenaPackage.Literals.RDF_LOCATION__URI, newURI);
	}

} // RdfLocationImpl
