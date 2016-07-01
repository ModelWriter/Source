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

import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Report</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.Report#getDescription <em>Description</em>}</li>
 * <li>{@link org.eclipse.mylyn.docs.intent.mapping.Report#getLink <em>Link</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getReport()
 * @model abstract="true"
 * @generated NOT
 */
public interface Report extends IEMFBaseElement, IReport {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getReport_Description()
	 * @model required="true"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.Report#getDescription
	 * <em>Description</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Link</b></em>' reference. It is bidirectional and its opposite is '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.Link#getReports <em>Reports</em>}'. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Link</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Link</em>' reference.
	 * @see #setLink(Link)
	 * @see org.eclipse.mylyn.docs.intent.mapping.MappingPackage#getReport_Link()
	 * @see org.eclipse.mylyn.docs.intent.mapping.Link#getReports
	 * @model opposite="reports" required="true"
	 * @generated NOT
	 */
	ILink getLink();

	/**
	 * Sets the value of the '{@link org.eclipse.mylyn.docs.intent.mapping.Report#getLink <em>Link</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Link</em>' reference.
	 * @see #getLink()
	 * @generated NOT
	 */
	void setLink(ILink value);

} // Report
