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
package org.eclipse.mylyn.docs.intent.mapping;

import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Text Location</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.TextLocation#getStartOffset <em>Start Offset</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.TextLocation#getEndOffset <em>End Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getTextLocation()
 * @model
 * @generated NOT
 */
public interface TextLocation extends Location, ITextLocation {
	/**
	 * Returns the value of the '<em><b>Start Offset</b></em>' attribute. The default value is
	 * <code>"-1"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text Offset</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Start Offset</em>' attribute.
	 * @see #setStartOffset(int)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getTextLocation_StartOffset()
	 * @model default="-1" required="true"
	 * @generated
	 */
	int getStartOffset();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.TextLocation#getStartOffset
	 * <em>Start Offset</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Start Offset</em>' attribute.
	 * @see #getStartOffset()
	 * @generated
	 */
	void setStartOffset(int value);

	/**
	 * Returns the value of the '<em><b>End Offset</b></em>' attribute. The default value is <code>"-1"</code>
	 * . <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Offset</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>End Offset</em>' attribute.
	 * @see #setEndOffset(int)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getTextLocation_EndOffset()
	 * @model default="-1" required="true"
	 * @generated
	 */
	int getEndOffset();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.TextLocation#getEndOffset <em>End
	 * Offset</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>End Offset</em>' attribute.
	 * @see #getEndOffset()
	 * @generated
	 */
	void setEndOffset(int value);

} // TextLocation
