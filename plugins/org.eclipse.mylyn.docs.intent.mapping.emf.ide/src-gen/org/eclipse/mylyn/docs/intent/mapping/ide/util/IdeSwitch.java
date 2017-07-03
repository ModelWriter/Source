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
package org.eclipse.mylyn.docs.intent.mapping.ide.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.mylyn.docs.intent.mapping.EObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.IEMFBaseElement;
import org.eclipse.mylyn.docs.intent.mapping.Location;
import org.eclipse.mylyn.docs.intent.mapping.LocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.TextContainer;
import org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage;
import org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the
 * model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
 * non-null result is returned, which is the result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdePackage
 * @generated
 */
public class IdeSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static IdePackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IdeSwitch() {
		if (modelPackage == null) {
			modelPackage = IdePackage.eINSTANCE;
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
			case IdePackage.RESOURCE_LOCATION: {
				ResourceLocation resourceLocation = (ResourceLocation)theEObject;
				T result = caseResourceLocation(resourceLocation);
				if (result == null)
					result = caseLocation(resourceLocation);
				if (result == null)
					result = caseIEMFBaseElement(resourceLocation);
				if (result == null)
					result = caseLocationContainer(resourceLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case IdePackage.FILE_LOCATION: {
				FileLocation fileLocation = (FileLocation)theEObject;
				T result = caseFileLocation(fileLocation);
				if (result == null)
					result = caseResourceLocation(fileLocation);
				if (result == null)
					result = caseLocation(fileLocation);
				if (result == null)
					result = caseIEMFBaseElement(fileLocation);
				if (result == null)
					result = caseLocationContainer(fileLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case IdePackage.TEXT_FILE_LOCATION: {
				TextFileLocation textFileLocation = (TextFileLocation)theEObject;
				T result = caseTextFileLocation(textFileLocation);
				if (result == null)
					result = caseFileLocation(textFileLocation);
				if (result == null)
					result = caseTextContainer(textFileLocation);
				if (result == null)
					result = caseResourceLocation(textFileLocation);
				if (result == null)
					result = caseLocation(textFileLocation);
				if (result == null)
					result = caseIEMFBaseElement(textFileLocation);
				if (result == null)
					result = caseLocationContainer(textFileLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case IdePackage.EOBJECT_FILE_LOCATION: {
				EObjectFileLocation eObjectFileLocation = (EObjectFileLocation)theEObject;
				T result = caseEObjectFileLocation(eObjectFileLocation);
				if (result == null)
					result = caseFileLocation(eObjectFileLocation);
				if (result == null)
					result = caseEObjectContainer(eObjectFileLocation);
				if (result == null)
					result = caseResourceLocation(eObjectFileLocation);
				if (result == null)
					result = caseLocation(eObjectFileLocation);
				if (result == null)
					result = caseIEMFBaseElement(eObjectFileLocation);
				if (result == null)
					result = caseLocationContainer(eObjectFileLocation);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			default:
				return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceLocation(ResourceLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>File Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFileLocation(FileLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text File Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text File Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextFileLocation(TextFileLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject File Location</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject File Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectFileLocation(EObjectFileLocation object) {
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

} // IdeSwitch
