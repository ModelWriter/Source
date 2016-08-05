/**
 */
package org.eclipse.mylyn.docs.intent.mapping.jena_ide;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation;
import org.eclipse.mylyn.docs.intent.mapping.jena.ide.IRdfFileLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Rdf File Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.jena_ide.RdfFileLocation#getResources <em>Resources</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage#getRdfFileLocation()
 * @model
 * @generated NOT
 */
public interface RdfFileLocation extends FileLocation, IRdfFileLocation {
	/**
	 * Returns the value of the '<em><b>Resources</b></em>' attribute list. The list contents are of type
	 * {@link com.hp.hpl.jena.rdf.model.Resource}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resources</em>' attribute list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Resources</em>' attribute list.
	 * @see org.eclipse.mylyn.docs.intent.mapping.jena_ide.Jena_idePackage#getRdfFileLocation_Resources()
	 * @model dataType="org.eclipse.mylyn.docs.intent.mapping.jena_ide.Resource" transient="true"
	 * @generated NOT
	 */
	List<Resource> getResources();

} // RdfFileLocation
