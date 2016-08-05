/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage;
import org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rdf File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.impl.RdfFileLocationImpl#getResources <em>
 * Resources</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RdfFileLocationImpl extends FileLocationImpl implements RdfFileLocation {

	/**
	 * The {@link List} of {@link Resource}.
	 * 
	 * @generated NOT
	 */
	private List<Resource> resources = new ArrayList<Resource>();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public RdfFileLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Jena_idePackage.Literals.RDF_FILE_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer#setResources(java.util.List)
	 * @generated NOT
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Jena_idePackage.RDF_FILE_LOCATION__RESOURCES:
				return getResources();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Jena_idePackage.RDF_FILE_LOCATION__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends Resource>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Jena_idePackage.RDF_FILE_LOCATION__RESOURCES:
				getResources().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Jena_idePackage.RDF_FILE_LOCATION__RESOURCES:
				return resources != null && !resources.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (resources: ");
		result.append(resources);
		result.append(')');
		return result.toString();
	}

} // RdfFileLocationImpl
