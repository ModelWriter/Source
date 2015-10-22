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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils.DiffMatch;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.internal.TextAdapter;

/**
 * {@link EObject} {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocation> containerType, Object element) {
		final Class<? extends ILocation> res;

		if (IEObjectContainer.class.isAssignableFrom(containerType)
				&& (element instanceof EObject || element instanceof Setting)) {
			res = IEObjectLocation.class;
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocation location, Object element) {
		IEObjectLocation toInit = (IEObjectLocation)location;

		final EObject eObject;
		final EStructuralFeature feature;
		final Object value;
		final boolean setting;
		final String text;
		final int textOffset;
		if (element instanceof Setting) {
			eObject = ((Setting)element).getEObject();
			feature = ((Setting)element).getEStructuralFeature();
			value = ((Setting)element).get(true);
			setting = true;
			final ITextAdapter adapter = getTextAdapter(eObject);
			final String containerText = adapter.getText();
			final int[] offsets = adapter.getOffsets(feature, value);
			text = containerText.substring(offsets[0] - adapter.getTextOffset(), offsets[1]
					- adapter.getTextOffset());
			textOffset = offsets[0];
		} else {
			eObject = (EObject)element;
			feature = null;
			value = null;
			setting = false;
			final ITextAdapter adapter = getTextAdapter(eObject);
			text = adapter.getText();
			textOffset = adapter.getTextOffset();
		}

		toInit.setEObject(eObject);
		toInit.setEStructuralFeature(feature);
		toInit.setSetting(setting);
		toInit.setValue(value);
		toInit.setText(text);
		toInit.setTextOffset(textOffset);
	}

	/**
	 * Updates the given {@link IEObjectContainer} with the given {@link IEObjectContainer#getEObjects()
	 * EObject list}.
	 * 
	 * @param container
	 *            the {@link IEObjectContainer}
	 * @param eObjects
	 *            the {@link IEObjectContainer#getEObjects() EObject list}
	 */
	public void update(IEObjectContainer container, List<EObject> eObjects) {
		final String newText = serialize(eObjects);
		final String oldText = container.getText();
		container.setEObjects(eObjects);
		container.setText(newText);
		if (oldText != null) {
			final DiffMatch diff = MappingUtils.getDiffMatch(oldText, newText);
			for (ILocation child : container.getContents()) {
				if (child instanceof IEObjectLocation) {
					final IEObjectLocation location = (IEObjectLocation)child;
					final int oldLength = location.getText().length();
					int oldOffset = location.getTextOffset();
					int newStartOffset = diff.getIndex(oldOffset);
					int newEndOffset = diff.getIndex(oldOffset + oldLength);
					location.setText(newText.substring(newStartOffset, newEndOffset));
					location.setTextOffset(newStartOffset);
				}
			}
			for (ILocation child : container.getContents()) {
				if (child instanceof IEObjectLocation) {
					final IEObjectLocation location = (IEObjectLocation)child;
					ITextAdapter adapter = null;
					for (EObject eObj : container.getEObjects()) {
						final ITextAdapter textAdapter = getTextAdapter(eObj);
						if (textAdapter.getTextOffset() > location.getTextOffset()) {
							break;
						} else {
							adapter = textAdapter;
						}
					}
					adapter.setLocationFromText(location);
				}
			}
		}
	}

	/**
	 * Serializes the given {@link List} of {@link EObject}.
	 * 
	 * @param eObjects
	 *            the {@link List} of {@link EObject} to serialize
	 * @return the {@link String} representing the given {@link List} of {@link EObject}
	 */
	private String serialize(List<EObject> eObjects) {
		final StringBuilder builder = new StringBuilder();

		for (EObject eObj : eObjects) {
			final ITextAdapter textAdapter = getTextAdapter(eObj);
			textAdapter.setTextOffset(builder.length());
			builder.append(textAdapter.getText());
		}

		return builder.toString();
	}

	/**
	 * Gets a {@link ITextAdapter} for the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject}
	 * @return a {@link ITextAdapter} for the given {@link EObject}
	 */
	public static ITextAdapter getTextAdapter(EObject eObject) {
		ITextAdapter res = (ITextAdapter)EcoreUtil.getAdapter(eObject.eAdapters(), ITextAdapter.class);

		if (res == null) {
			res = new TextAdapter();
			eObject.eAdapters().add(res);
		}

		return res;
	}
}
