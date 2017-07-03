/**
 *  Copyright (c) 2015 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Obeo - initial API and implementation and/or initial documentation
 *     ...
 * 
 */
package org.eclipse.mylyn.docs.intent.mapping.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.mylyn.docs.intent.mapping.Base;
import org.eclipse.mylyn.docs.intent.mapping.CDOBinaryResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDOFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDOFolderLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDORepositoryLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDOResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDOResourceNodeLocation;
import org.eclipse.mylyn.docs.intent.mapping.CDOTextResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.Couple;
import org.eclipse.mylyn.docs.intent.mapping.EObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.EObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement;
import org.eclipse.mylyn.docs.intent.mapping.Link;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;
import org.eclipse.mylyn.docs.intent.mapping.Report;
import org.eclipse.mylyn.docs.intent.mapping.TextContainer;
import org.eclipse.mylyn.docs.intent.mapping.TextLocation;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the
 * model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
 * non-null result is returned, which is the result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage
 * @generated
 */
public class MappingSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static MappingPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MappingSwitch() {
		if (modelPackage == null) {
			modelPackage = MappingPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param ePackage
	 *            the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields
	 * that result. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case MappingPackage.LOCATION: {
				Location location = (Location)theEObject;
				T result = caseLocation(location);
				if (result == null)
					result = caseIEMFBaseElement(location);
				if (result == null)
					result = caseLocationContainer(location);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.LINK: {
				Link link = (Link)theEObject;
				T result = caseLink(link);
				if (result == null)
					result = caseIEMFBaseElement(link);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.BASE: {
				Base base = (Base)theEObject;
				T result = caseBase(base);
				if (result == null)
					result = caseIEMFBaseElement(base);
				if (result == null)
					result = caseLocationContainer(base);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.IEMF_BASE_ELEMENT: {
				IEMFBaseElement iemfBaseElement = (IEMFBaseElement)theEObject;
				T result = caseIEMFBaseElement(iemfBaseElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.TEXT_LOCATION: {
				TextLocation textLocation = (TextLocation)theEObject;
				T result = caseTextLocation(textLocation);
				if (result == null)
					result = caseLocation(textLocation);
				if (result == null)
					result = caseIEMFBaseElement(textLocation);
				if (result == null)
					result = caseLocationContainer(textLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.TEXT_CONTAINER: {
				TextContainer textContainer = (TextContainer)theEObject;
				T result = caseTextContainer(textContainer);
				if (result == null)
					result = caseLocation(textContainer);
				if (result == null)
					result = caseIEMFBaseElement(textContainer);
				if (result == null)
					result = caseLocationContainer(textContainer);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.EOBJECT_LOCATION: {
				EObjectLocation eObjectLocation = (EObjectLocation)theEObject;
				T result = caseEObjectLocation(eObjectLocation);
				if (result == null)
					result = caseLocation(eObjectLocation);
				if (result == null)
					result = caseIEMFBaseElement(eObjectLocation);
				if (result == null)
					result = caseLocationContainer(eObjectLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.LOCATION_CONTAINER: {
				LocationContainer locationContainer = (LocationContainer)theEObject;
				T result = caseLocationContainer(locationContainer);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.REPORT: {
				Report report = (Report)theEObject;
				T result = caseReport(report);
				if (result == null)
					result = caseIEMFBaseElement(report);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.COUPLE: {
				Couple couple = (Couple)theEObject;
				T result = caseCouple(couple);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.EOBJECT_CONTAINER: {
				EObjectContainer eObjectContainer = (EObjectContainer)theEObject;
				T result = caseEObjectContainer(eObjectContainer);
				if (result == null)
					result = caseLocation(eObjectContainer);
				if (result == null)
					result = caseIEMFBaseElement(eObjectContainer);
				if (result == null)
					result = caseLocationContainer(eObjectContainer);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_RESOURCE_NODE_LOCATION: {
				CDOResourceNodeLocation cdoResourceNodeLocation = (CDOResourceNodeLocation)theEObject;
				T result = caseCDOResourceNodeLocation(cdoResourceNodeLocation);
				if (result == null)
					result = caseLocation(cdoResourceNodeLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoResourceNodeLocation);
				if (result == null)
					result = caseLocationContainer(cdoResourceNodeLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_FILE_LOCATION: {
				CDOFileLocation cdoFileLocation = (CDOFileLocation)theEObject;
				T result = caseCDOFileLocation(cdoFileLocation);
				if (result == null)
					result = caseCDOResourceNodeLocation(cdoFileLocation);
				if (result == null)
					result = caseLocation(cdoFileLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoFileLocation);
				if (result == null)
					result = caseLocationContainer(cdoFileLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_FOLDER_LOCATION: {
				CDOFolderLocation cdoFolderLocation = (CDOFolderLocation)theEObject;
				T result = caseCDOFolderLocation(cdoFolderLocation);
				if (result == null)
					result = caseCDOResourceNodeLocation(cdoFolderLocation);
				if (result == null)
					result = caseLocation(cdoFolderLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoFolderLocation);
				if (result == null)
					result = caseLocationContainer(cdoFolderLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_REPOSITORY_LOCATION: {
				CDORepositoryLocation cdoRepositoryLocation = (CDORepositoryLocation)theEObject;
				T result = caseCDORepositoryLocation(cdoRepositoryLocation);
				if (result == null)
					result = caseLocation(cdoRepositoryLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoRepositoryLocation);
				if (result == null)
					result = caseLocationContainer(cdoRepositoryLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_BINARY_RESOURCE_LOCATION: {
				CDOBinaryResourceLocation cdoBinaryResourceLocation = (CDOBinaryResourceLocation)theEObject;
				T result = caseCDOBinaryResourceLocation(cdoBinaryResourceLocation);
				if (result == null)
					result = caseCDOFileLocation(cdoBinaryResourceLocation);
				if (result == null)
					result = caseCDOResourceNodeLocation(cdoBinaryResourceLocation);
				if (result == null)
					result = caseLocation(cdoBinaryResourceLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoBinaryResourceLocation);
				if (result == null)
					result = caseLocationContainer(cdoBinaryResourceLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_TEXT_RESOURCE_LOCATION: {
				CDOTextResourceLocation cdoTextResourceLocation = (CDOTextResourceLocation)theEObject;
				T result = caseCDOTextResourceLocation(cdoTextResourceLocation);
				if (result == null)
					result = caseCDOFileLocation(cdoTextResourceLocation);
				if (result == null)
					result = caseTextContainer(cdoTextResourceLocation);
				if (result == null)
					result = caseCDOResourceNodeLocation(cdoTextResourceLocation);
				if (result == null)
					result = caseLocation(cdoTextResourceLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoTextResourceLocation);
				if (result == null)
					result = caseLocationContainer(cdoTextResourceLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CDO_RESOURCE_LOCATION: {
				CDOResourceLocation cdoResourceLocation = (CDOResourceLocation)theEObject;
				T result = caseCDOResourceLocation(cdoResourceLocation);
				if (result == null)
					result = caseCDOFileLocation(cdoResourceLocation);
				if (result == null)
					result = caseEObjectContainer(cdoResourceLocation);
				if (result == null)
					result = caseCDOResourceNodeLocation(cdoResourceLocation);
				if (result == null)
					result = caseLocation(cdoResourceLocation);
				if (result == null)
					result = caseIEMFBaseElement(cdoResourceLocation);
				if (result == null)
					result = caseLocationContainer(cdoResourceLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			default:
				return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocation(Location object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Link</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Link</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLink(Link object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBase(Base object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEMF Base Element</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEMF Base Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEMFBaseElement(IEMFBaseElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextLocation(TextLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text Container</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextContainer(TextContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectLocation(EObjectLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Location Container</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Location Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocationContainer(LocationContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Report</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Report</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReport(Report object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Couple</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Couple</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCouple(Couple object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject Container</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectContainer(EObjectContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Resource Node Location</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Resource Node Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOResourceNodeLocation(CDOResourceNodeLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO File Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO File Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOFileLocation(CDOFileLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Folder Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Folder Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOFolderLocation(CDOFolderLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Repository Location</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Repository Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDORepositoryLocation(CDORepositoryLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Binary Resource
	 * Location</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result
	 * will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Binary Resource
	 *         Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOBinaryResourceLocation(CDOBinaryResourceLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Text Resource Location</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Text Resource Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOTextResourceLocation(CDOTextResourceLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CDO Resource Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CDO Resource Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCDOResourceLocation(CDOResourceLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch, but this
	 * is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // MappingSwitch
