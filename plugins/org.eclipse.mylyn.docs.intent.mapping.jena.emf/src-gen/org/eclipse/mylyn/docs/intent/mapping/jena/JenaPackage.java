/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to
 * represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.jena.JenaFactory
 * @model kind="package"
 * @generated
 */
public interface JenaPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "jena";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.modelwriter.eu/mapping/jena";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "jena";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	JenaPackage eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.jena.impl.JenaPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.mylyn.docs.intent.mapping.jena.impl.RdfLocationImpl
	 * <em>Rdf Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.impl.RdfLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.impl.JenaPackageImpl#getRdfLocation()
	 * @generated
	 */
	int RDF_LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__CONTENTS = MappingPackage.LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__SOURCE_LINKS = MappingPackage.LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__TARGET_LINKS = MappingPackage.LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__CONTAINER = MappingPackage.LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__TYPE = MappingPackage.LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>URI</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION__URI = MappingPackage.LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Rdf Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION_FEATURE_COUNT = MappingPackage.LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Rdf Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_LOCATION_OPERATION_COUNT = MappingPackage.LOCATION_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation
	 * <em>Rdf Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Rdf Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation
	 * @generated
	 */
	EClass getRdfLocation();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation#getURI <em>URI</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>URI</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation#getURI()
	 * @see #getRdfLocation()
	 * @generated
	 */
	EAttribute getRdfLocation_URI();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JenaFactory getJenaFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.mylyn.docs.intent.mapping.jena.impl.RdfLocationImpl <em>Rdf Location</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.jena.impl.RdfLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.jena.impl.JenaPackageImpl#getRdfLocation()
		 * @generated
		 */
		EClass RDF_LOCATION = eINSTANCE.getRdfLocation();

		/**
		 * The meta object literal for the '<em><b>URI</b></em>' attribute feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RDF_LOCATION__URI = eINSTANCE.getRdfLocation_URI();

	}

} // JenaPackage
