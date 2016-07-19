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
package org.eclipse.mylyn.docs.intent.mapping.emf.connector;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils.DiffMatch;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.internal.TextAdapter;

/**
 * {@link EObject} {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectConnector extends AbstractConnector {

	@Override
	protected Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
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
		final int textStartOffset;
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
			textStartOffset = offsets[0];
		} else {
			eObject = (EObject)element;
			feature = null;
			value = null;
			setting = false;
			final ITextAdapter adapter = getTextAdapter(eObject);
			text = adapter.getText();
			textStartOffset = adapter.getTextOffset();
		}

		toInit.setEObject(eObject);
		toInit.setEStructuralFeature(feature);
		toInit.setSetting(setting);
		toInit.setValue(value);
		toInit.setStartOffset(textStartOffset);
		toInit.setEndOffset(textStartOffset + text.length());
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final boolean res;

		final IEObjectLocation eObjectLocation = (IEObjectLocation)location;
		if (element instanceof Setting) {
			final Setting setting = (Setting)element;
			res = eObjectLocation.isSetting()
					&& eObjectLocation.getEStructuralFeature() == setting.getEStructuralFeature()
					&& EcoreUtil.getURI(eObjectLocation.getEObject()).equals(
							EcoreUtil.getURI(setting.getEObject()));
		} else {
			final EObject eObject = (EObject)element;
			res = !eObjectLocation.isSetting()
					&& EcoreUtil.getURI(eObjectLocation.getEObject()).equals(EcoreUtil.getURI(eObject));
		}
		return res;
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
					final int newStartOffset = diff.getIndex(location.getStartOffset());
					final int newEndOffset = diff.getIndex(location.getEndOffset());
					if (isValidOffsets(newText, location.getEStructuralFeature(), newStartOffset,
							newEndOffset)) {
						location.setStartOffset(newStartOffset);
						location.setEndOffset(newEndOffset);
					} else {
						location.setStartOffset(-1);
						location.setEndOffset(-1);
						// TODO at this point we might want to mark the location as deleted and keep its data
						location.setEObject(null);
						location.setSetting(false);
						location.setEStructuralFeature(null);
						location.setValue(null);
					}
				}
			}
			for (ILocation child : container.getContents()) {
				if (child instanceof IEObjectLocation) {
					final IEObjectLocation location = (IEObjectLocation)child;
					ITextAdapter adapter = null;
					for (EObject eObj : container.getEObjects()) {
						final ITextAdapter textAdapter = getTextAdapter(eObj);
						if (textAdapter.getTextOffset() > location.getStartOffset()) {
							break;
						} else {
							adapter = textAdapter;
						}
					}
					if (adapter != null) {
						adapter.setLocationFromText(location);
					}
				}
			}
		}
	}

	/**
	 * Tell if the start and end offsets are valid in the given text according to given
	 * {@link EStructuralFeature}.
	 * 
	 * @param text
	 *            the containing text
	 * @param feature
	 *            the {@link EStructuralFeature} if nay, <code>null</code> otherwise
	 * @param startOffset
	 *            the {@link IEObjectLocation#getStartOffset() start offset}
	 * @param endOffset
	 *            the {@link IEObjectLocation#getEndOffset() end offset}
	 * @return <code>true</code> if the start and end offsets are valid in the given text according to given
	 *         {@link EStructuralFeature}, <code>false</code> otherwise
	 */
	protected boolean isValidOffsets(String text, EStructuralFeature feature, int startOffset, int endOffset) {
		final boolean res;

		if (feature != null) {
			final boolean startMatched = startOffset - TextAdapter.START_SETTING.length() >= 0
					&& TextAdapter.START_SETTING.equals(text.substring(startOffset
							- TextAdapter.START_SETTING.length(), startOffset));
			final boolean featureMatched = text.substring(startOffset).startsWith(
					feature.getName() + TextAdapter.MIDDLE_SETTING);
			final boolean endMatched = endOffset + TextAdapter.END_SETTING.length() <= text.length()
					&& text.substring(endOffset).startsWith(TextAdapter.END_SETTING);
			res = startMatched && featureMatched && endMatched;
		} else {
			final boolean startMatched = text.substring(startOffset).startsWith(TextAdapter.START_EOBJECT);
			final boolean endMatched = text.substring(endOffset - TextAdapter.END_EOBJECT.length())
					.startsWith(TextAdapter.END_EOBJECT);
			res = startMatched && endMatched;
		}

		return res;
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

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;
		if (location instanceof IEObjectLocation) {
			final IEObjectLocation eObjLocation = (IEObjectLocation)location;

			if (eObjLocation.isSetting()) {
				res = eObjLocation.getEObject().toString() + " "
						+ eObjLocation.getEStructuralFeature().getName();
			} else {
				res = eObjLocation.getEObject().toString();
			}
		} else {
			res = null;
		}

		return res;
	}
}
