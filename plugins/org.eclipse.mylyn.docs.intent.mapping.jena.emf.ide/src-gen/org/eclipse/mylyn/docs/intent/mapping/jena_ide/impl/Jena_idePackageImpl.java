/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_ideFactory;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class Jena_idePackageImpl extends EPackageImpl implements Jena_idePackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass rdfFileLocationEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Jena_idePackageImpl() {
		super(eNS_URI, Jena_ideFactory.eINSTANCE);
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
	 * This method is used to initialize {@link Jena_idePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the
	 * package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Jena_idePackage init() {
		if (isInited)
			return (Jena_idePackage)EPackage.Registry.INSTANCE.getEPackage(Jena_idePackage.eNS_URI);

		// Obtain or create and register package
		Jena_idePackageImpl theJena_idePackage = (Jena_idePackageImpl)(EPackage.Registry.INSTANCE.get(
				eNS_URI) instanceof Jena_idePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new Jena_idePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		IdePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theJena_idePackage.createPackageContents();

		// Initialize created meta-data
		theJena_idePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJena_idePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Jena_idePackage.eNS_URI, theJena_idePackage);
		return theJena_idePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRdfFileLocation() {
		return rdfFileLocationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Jena_ideFactory getJena_ideFactory() {
		return (Jena_ideFactory)getEFactoryInstance();
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
		rdfFileLocationEClass = createEClass(RDF_FILE_LOCATION);
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
		IdePackage theIdePackage = (IdePackage)EPackage.Registry.INSTANCE.getEPackage(IdePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		rdfFileLocationEClass.getESuperTypes().add(theIdePackage.getFileLocation());

		// Initialize classes, features, and operations; add parameters
		initEClass(rdfFileLocationEClass, RdfFileLocation.class, "RdfFileLocation", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} // Jena_idePackageImpl
