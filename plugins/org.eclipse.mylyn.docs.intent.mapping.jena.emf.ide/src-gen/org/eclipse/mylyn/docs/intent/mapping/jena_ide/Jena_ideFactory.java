/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage
 * @generated
 */
public interface Jena_ideFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	Jena_ideFactory eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_ideFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Rdf File Location</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Rdf File Location</em>'.
	 * @generated
	 */
	RdfFileLocation createRdfFileLocation();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	Jena_idePackage getJena_idePackage();

} // Jena_ideFactory
