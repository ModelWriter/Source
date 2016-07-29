/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage
 * @generated
 */
public interface JenaFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	JenaFactory eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.jena.impl.JenaFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Rdf Location</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Rdf Location</em>'.
	 * @generated
	 */
	RdfLocation createRdfLocation();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	JenaPackage getJenaPackage();

} // JenaFactory
