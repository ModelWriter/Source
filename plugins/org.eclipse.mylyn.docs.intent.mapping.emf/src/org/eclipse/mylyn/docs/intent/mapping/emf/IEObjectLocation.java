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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * An {@link org.eclipse.mylyn.docs.intent.mapping.base.ILocation ILocation} for
 * {@link org.eclipse.emf.ecore.EObject EObject}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectLocation extends ILocation {

	/**
	 * Gets the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 * relative to the {@link IEObjectLocation#getContainer() container}.
	 * 
	 * @return the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 *         relative to the {@link IEObjectLocation#getContainer() container}
	 */
	String getURIFragment();

	/**
	 * Sets the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 * relative to the {@link IEObjectLocation#getContainer() container}.
	 * 
	 * @param uriFragment
	 *            the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 *            relative to the {@link IEObjectLocation#getContainer() container}
	 */
	void setURIFragment(String uriFragment);

	/**
	 * Gets the saved {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 * relative to the saved {@link IEObjectLocation#getContainer() container}.
	 * 
	 * @return the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 *         relative to {@link IEObjectLocation#getContainer() container}
	 */
	String getSavedURIFragment();

	/**
	 * Sets the saved {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 * relative to the saved {@link IEObjectLocation#getContainer() container}.
	 * 
	 * @param uriFragment
	 *            the {@link java.net.URI URI} fragment of the {@link org.eclipse.emf.ecore.EObject EObject}
	 *            relative to the saved {@link IEObjectLocation#getContainer() container}
	 */
	void setSavedURIFragment(String uriFragment);

	/**
	 * Sets the {@link org.eclipse.emf.ecore.EStructuralFeature#getName() feature name}.
	 * 
	 * @param featureName
	 *            the {@link org.eclipse.emf.ecore.EStructuralFeature#getName() feature name} if any,
	 *            <code>null</code> otherwise
	 */
	void setFeatureName(String featureName);

	/**
	 * Gets the {@link org.eclipse.emf.ecore.EStructuralFeature#getName() feature name}.
	 * 
	 * @return the {@link org.eclipse.emf.ecore.EStructuralFeature#getName() feature name} if any,
	 *         <code>null</code> otherwise
	 */
	String getFeatureName();

	/**
	 * Gets the index in the {@link #getFeatureName() feature}.
	 * 
	 * @return the index in the {@link #getFeatureName() feature}
	 */
	int getIndex();

	/**
	 * Sets the index in the {@link #getFeatureName() feature}.
	 * 
	 * @param index
	 *            the index in the {@link #getFeatureName() feature}
	 */
	void setIndex(int index);

}
