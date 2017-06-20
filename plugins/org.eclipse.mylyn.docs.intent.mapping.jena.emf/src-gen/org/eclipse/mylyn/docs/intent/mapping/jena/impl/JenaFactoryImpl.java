/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.mylyn.docs.intent.mapping.jena.JenaFactory;
import org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage;
import org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class JenaFactoryImpl extends EFactoryImpl implements JenaFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static JenaFactory init() {
		try {
			JenaFactory theJenaFactory = (JenaFactory)EPackage.Registry.INSTANCE.getEFactory(
					JenaPackage.eNS_URI);
			if (theJenaFactory != null) {
				return theJenaFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JenaFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public JenaFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case JenaPackage.RDF_LOCATION:
				return (EObject)createRdfLocation();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName()
						+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public RdfLocation createRdfLocation() {
		RdfLocationImpl rdfLocation = new RdfLocationImpl();
		return rdfLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public JenaPackage getJenaPackage() {
		return (JenaPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static JenaPackage getPackage() {
		return JenaPackage.eINSTANCE;
	}

} // JenaFactoryImpl
