/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;

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
 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_ideFactory
 * @model kind="package"
 * @generated
 */
public interface Jena_idePackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "jena_ide";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.modelwriter.eu/mapping/jena/ide";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "jena_ide";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	Jena_idePackage eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_idePackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.RdfFileLocationImpl
	 * <em>Rdf File Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.RdfFileLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_idePackageImpl#getRdfFileLocation()
	 * @generated
	 */
	int RDF_FILE_LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__CONTENTS = IdePackage.FILE_LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__SOURCE_LINKS = IdePackage.FILE_LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__TARGET_LINKS = IdePackage.FILE_LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__CONTAINER = IdePackage.FILE_LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__TYPE = IdePackage.FILE_LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__FULL_PATH = IdePackage.FILE_LOCATION__FULL_PATH;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' attribute list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION__RESOURCES = IdePackage.FILE_LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Rdf File Location</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION_FEATURE_COUNT = IdePackage.FILE_LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Rdf File Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RDF_FILE_LOCATION_OPERATION_COUNT = IdePackage.FILE_LOCATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '<em>Resource</em>' data type. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see com.hp.hpl.jena.rdf.model.Resource
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_idePackageImpl#getResource()
	 * @generated
	 */
	int RESOURCE = 1;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation <em>Rdf File Location</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Rdf File Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation
	 * @generated
	 */
	EClass getRdfFileLocation();

	/**
	 * Returns the meta object for the attribute list '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation#getResources <em>Resources</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Resources</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation#getResources()
	 * @see #getRdfFileLocation()
	 * @generated
	 */
	EAttribute getRdfFileLocation_Resources();

	/**
	 * Returns the meta object for data type '{@link com.hp.hpl.jena.rdf.model.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Resource</em>'.
	 * @see com.hp.hpl.jena.rdf.model.Resource
	 * @model instanceClass="com.hp.hpl.jena.rdf.model.Resource" serializeable="false"
	 * @generated
	 */
	EDataType getResource();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Jena_ideFactory getJena_ideFactory();

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
		 * {@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.RdfFileLocationImpl
		 * <em>Rdf File Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.RdfFileLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_idePackageImpl#getRdfFileLocation()
		 * @generated
		 */
		EClass RDF_FILE_LOCATION = eINSTANCE.getRdfFileLocation();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>' attribute list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RDF_FILE_LOCATION__RESOURCES = eINSTANCE.getRdfFileLocation_Resources();

		/**
		 * The meta object literal for the '<em>Resource</em>' data type. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see com.hp.hpl.jena.rdf.model.Resource
		 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.Jena_idePackageImpl#getResource()
		 * @generated
		 */
		EDataType RESOURCE = eINSTANCE.getResource();

	}

} // Jena_idePackage
