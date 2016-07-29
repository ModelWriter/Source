/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage
 * @generated
 */
public class Jena_ideAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static Jena_idePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Jena_ideAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Jena_idePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This
	 * implementation returns <code>true</code> if the object is either the model's package or is an instance
	 * object of the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected Jena_ideSwitch<Adapter> modelSwitch = new Jena_ideSwitch<Adapter>() {
		@Override
		public Adapter caseRdfFileLocation(RdfFileLocation object) {
			return createRdfFileLocationAdapter();
		}

		@Override
		public Adapter caseIEMFBaseElement(IEMFBaseElement object) {
			return createIEMFBaseElementAdapter();
		}

		@Override
		public Adapter caseLocationContainer(LocationContainer object) {
			return createLocationContainerAdapter();
		}

		@Override
		public Adapter caseLocation(Location object) {
			return createLocationAdapter();
		}

		@Override
		public Adapter caseResourceLocation(ResourceLocation object) {
			return createResourceLocationAdapter();
		}

		@Override
		public Adapter caseFileLocation(FileLocation object) {
			return createFileLocationAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation <em>Rdf File Location</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation
	 * @generated
	 */
	public Adapter createRdfFileLocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement <em>IEMF Base Element</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement
	 * @generated
	 */
	public Adapter createIEMFBaseElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.LocationContainer <em>Location Container</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.LocationContainer
	 * @generated
	 */
	public Adapter createLocationContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.mylyn.docs.intent.mapping.Location
	 * <em>Location</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.Location
	 * @generated
	 */
	public Adapter createLocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation <em>Resource Location</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation
	 * @generated
	 */
	public Adapter createResourceLocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation <em>File Location</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation
	 * @generated
	 */
	public Adapter createFileLocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns
	 * null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // Jena_ideAdapterFactory
