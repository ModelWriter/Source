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
package eu.modelwriter.semantic.emf;

import eu.modelwriter.semantic.ISemanticProvider;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Abstract {@link EObject} {@link ISemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractEObjectSemanticProvider implements ISemanticProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getConceptType()
	 */
	public Class<?> getConceptType() {
		return EObject.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getRelatedConcepts(java.lang.Object)
	 */
	public Map<Object, Set<Object>> getRelatedConcepts(Object concept) {
		final Map<Object, Set<Object>> res = new LinkedHashMap<Object, Set<Object>>();

		if (concept instanceof ENamedElement) {
			for (EStructuralFeature feature : ((ENamedElement)concept).eClass().getEAllStructuralFeatures()) {
				final Set<Object> values = new LinkedHashSet<Object>();
				final Object eValue = ((ENamedElement)concept).eGet(feature);
				if (eValue instanceof Collection<?>) {
					values.addAll((Collection<?>)eValue);
				} else if (eValue != null) {
					values.add(eValue);
				}
				if (!values.isEmpty()) {
					res.put(feature, values);
				}
			}
		}

		return res;
	}

}
