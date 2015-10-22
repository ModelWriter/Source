/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;

/**
 * An {@link org.eclipse.mylyn.docs.intent.mapping.base.ILocation ILocation} for {@link EObject}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectLocation extends ITextLocation {

	/**
	 * Gets the referenced {@link EObject} or the containing {@link EObject} is the
	 * {@link IEObjectLocation#getEStructuralFeature() EStructuralFeature} is set.
	 * 
	 * @return the referenced {@link EObject} or the containing {@link EObject} is the
	 *         {@link IEObjectLocation#getEStructuralFeature() EStructuralFeature} is set
	 */
	EObject getEObject();

	/**
	 * Sets the referenced {@link EObject} or the containing {@link EObject} is the
	 * {@link IEObjectLocation#getEStructuralFeature() EStructuralFeature} is set.
	 * 
	 * @param eObject
	 *            the referenced {@link EObject} or the containing {@link EObject} is the
	 *            {@link IEObjectLocation#getEStructuralFeature() EStructuralFeature} is set
	 */
	void setEObject(EObject eObject);

	/**
	 * Sets the {@link EStructuralFeature} containing the {@link IEObjectLocation#getValue() value}.
	 * 
	 * @param feature
	 *            the {@link EStructuralFeature} containing the {@link IEObjectLocation#getValue() value}
	 */
	void setEStructuralFeature(EStructuralFeature feature);

	/**
	 * Gets the {@link EStructuralFeature} containing the {@link IEObjectLocation#getValue() value}.
	 * 
	 * @return the {@link EStructuralFeature} containing the {@link IEObjectLocation#getValue() value} if any,
	 *         <code>null</code> otherwise
	 */
	EStructuralFeature getEStructuralFeature();

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value
	 */
	void setValue(Object value);

	/**
	 * Gets the value.
	 * 
	 * @return the value if any, <code>null</code> otherwise
	 */
	Object getValue();

	/**
	 * Sets if the {@link IEObjectLocation#getEStructuralFeature() feature} and
	 * {@link IEObjectLocation#getValue() value} are used.
	 * 
	 * @param setting
	 *            <code>true</code> if the {@link IEObjectLocation#getEStructuralFeature() feature} and
	 *            {@link IEObjectLocation#getValue() value} are used, <code>false</code> otherwise
	 */
	void setSetting(boolean setting);

	/**
	 * Tells if the {@link IEObjectLocation#getEStructuralFeature() feature} and
	 * {@link IEObjectLocation#getValue() value} are used.
	 * 
	 * @return <code>true</code> if the {@link IEObjectLocation#getEStructuralFeature() feature} and
	 *         {@link IEObjectLocation#getValue() value} are used, <code>false</code> otherwise
	 */
	boolean isSetting();

}
