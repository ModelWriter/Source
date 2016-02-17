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

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.ENamedElement;

/**
 * Ecore {@link eu.modelwriter.semantic.ISemanticProvider ISemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EcoreSemanticProvider extends AbstractEObjectSemanticProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.emf.AbstractEObjectSemanticProvider#getConceptType()
	 */
	public Class<?> getConceptType() {
		return ENamedElement.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticProvider#getSemanticLabels(java.lang.Object)
	 */
	public Set<String> getSemanticLabels(Object concept) {
		final Set<String> res;

		if (concept instanceof ENamedElement) {
			res = new LinkedHashSet<String>();
			// TODO camel case separation of words ?
			res.add(((ENamedElement)concept).getName());
		} else {
			res = null;
		}

		return res;
	}

}
