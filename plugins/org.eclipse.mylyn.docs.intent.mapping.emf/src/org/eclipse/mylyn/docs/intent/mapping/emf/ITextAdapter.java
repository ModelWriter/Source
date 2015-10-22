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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Adapt {@link org.eclipse.emf.ecore.EObject EObject} for serialization.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ITextAdapter extends Adapter {

	/**
	 * Gets the text representing the {@link Adapter#getTarget() target}.
	 * 
	 * @return the text representing the {@link Adapter#getTarget() target}
	 */
	String getText();

	/**
	 * Sets the {@link ITextAdapter#getText() text} offset in the {@link IEObjectContainer}.
	 * 
	 * @param offset
	 *            the {@link ITextAdapter#getText() text} offset in the {@link IEObjectContainer}
	 */
	void setTextOffset(int offset);

	/**
	 * Gets the {@link ITextAdapter#getText() text} offset in the {@link IEObjectContainer}.
	 * 
	 * @return the {@link ITextAdapter#getText() text} offset in the {@link IEObjectContainer}
	 */
	int getTextOffset();

	/**
	 * Sets the given {@link IEObjectLocation} from its {@link IEObjectLocation#getText() text}.
	 * 
	 * @param location
	 *            the {@link IEObjectLocation} to set
	 */
	void setLocationFromText(IEObjectLocation location);

	/**
	 * Sets the given {@link IEObjectLocation} from its {@link IEObjectLocation#getEObject() EObject}.
	 * 
	 * @param location
	 *            the {@link IEObjectLocation} to set
	 */
	void setLocationFromEObject(IEObjectLocation location);

	/**
	 * Gets the start and end offsets of the given {@link EStructuralFeature} and value.
	 * 
	 * @param feature
	 *            the {@link EStructuralFeature}
	 * @param value
	 *            the value
	 * @return the start and end offsets of the given {@link EStructuralFeature} and value
	 */
	int[] getOffsets(EStructuralFeature feature, Object value);

}
