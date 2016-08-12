/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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

import eu.modelwriter.semantic.IBase;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * Ecore implementation of {@link IBase}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EcoreBase implements IBase {

	/**
	 * The {@link EPackage}.
	 */
	private EPackage ePkg;

	/**
	 * Constructor.
	 * 
	 * @param ePkg
	 *            the {@link EPackage}
	 */
	public EcoreBase(EPackage ePkg) {
		this.ePkg = ePkg;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getName()
	 */
	public String getName() {
		return getEpackage().getNsURI();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.IBase#getConcepts()
	 */
	public Set<?> getConcepts() {
		final Set<Object> res = new LinkedHashSet<Object>();

		res.add(getEpackage());
		final Iterator<EObject> it = getEpackage().eAllContents();
		while (it.hasNext()) {
			final EObject eObj = it.next();
			if (eObj instanceof ENamedElement) {
				res.add(eObj);
			}
		}

		return res;
	}

	/**
	 * Gets the {@link EPackage}.
	 * 
	 * @return the {@link EPackage}
	 */
	public EPackage getEpackage() {
		return ePkg;
	}

}
