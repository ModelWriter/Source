/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.jena.JenaFactory;
import org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage;
import org.eclipse.mylyn.docs.intent.mapping.jena.RdfLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class JenaPackageImpl extends EPackageImpl implements JenaPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass rdfLocationEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.JenaPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JenaPackageImpl() {
		super(eNS_URI, JenaFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it
	 * depends.
	 * <p>
	 * This method is used to initialize {@link JenaPackage#eINSTANCE} when that field is accessed. Clients
	 * should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static JenaPackage init() {
		if (isInited)
			return (JenaPackage)EPackage.Registry.INSTANCE.getEPackage(JenaPackage.eNS_URI);

		// Obtain or create and register package
		JenaPackageImpl theJenaPackage = (JenaPackageImpl)(EPackage.Registry.INSTANCE.get(
				eNS_URI) instanceof JenaPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new JenaPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MappingPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theJenaPackage.createPackageContents();

		// Initialize created meta-data
		theJenaPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJenaPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(JenaPackage.eNS_URI, theJenaPackage);
		return theJenaPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRdfLocation() {
		return rdfLocationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRdfLocation_URI() {
		return (EAttribute)rdfLocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public JenaFactory getJenaFactory() {
		return (JenaFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any
	 * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		rdfLocationEClass = createEClass(RDF_LOCATION);
		createEAttribute(rdfLocationEClass, RDF_LOCATION__URI);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect
	 * on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MappingPackage theMappingPackage = (MappingPackage)EPackage.Registry.INSTANCE.getEPackage(
				MappingPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		rdfLocationEClass.getESuperTypes().add(theMappingPackage.getLocation());

		// Initialize classes, features, and operations; add parameters
		initEClass(rdfLocationEClass, RdfLocation.class, "RdfLocation", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRdfLocation_URI(), ecorePackage.getEString(), "URI", null, 1, 1, RdfLocation.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // JenaPackageImpl
