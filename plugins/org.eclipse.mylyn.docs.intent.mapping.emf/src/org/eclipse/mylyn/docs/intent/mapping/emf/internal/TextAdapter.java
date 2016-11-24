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
package org.eclipse.mylyn.docs.intent.mapping.emf.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;

/**
 * {@link ITextAdapter} implementation.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextAdapter extends AdapterImpl implements ITextAdapter {

	/**
	 * Start {@link org.eclipse.emf.ecore.EStructuralFeature.Setting Setting} separator.
	 */
	public static final String START_SETTING = ":";

	/**
	 * Middle {@link org.eclipse.emf.ecore.EStructuralFeature.Setting Setting} separator.
	 */
	public static final String MIDDLE_SETTING = ":";

	/**
	 * End {@link org.eclipse.emf.ecore.EStructuralFeature.Setting Setting} separator.
	 */
	public static final String END_SETTING = ":\n";

	/**
	 * Start {@link EObject} separator.
	 */
	public static final String END_EOBJECT = "<<<<\n";

	/**
	 * End {@link EObject} separator.
	 */
	public static final String START_EOBJECT = ">>>>\n";

	/**
	 * Couple of a {@link EStructuralFeature} and a value.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private static final class FeatureValue {

		/**
		 * The {@link EStructuralFeature}.
		 */
		private final EStructuralFeature feature;

		/**
		 * The value.
		 */
		private final Object value;

		/**
		 * Constructor.
		 * 
		 * @param feature
		 *            the {@link EStructuralFeature}
		 * @param value
		 *            the value
		 */
		private FeatureValue(EStructuralFeature feature, Object value) {
			this.feature = feature;
			this.value = value;
		}
	}

	/**
	 * the offset in the surrounding text.
	 */
	private int textOffset = -1;

	/**
	 * Mapping from the offset to the {@link EStructuralFeature} and the value.
	 */
	private final Map<Integer, FeatureValue> offsetToFeatureValues = new LinkedHashMap<Integer, FeatureValue>();

	/**
	 * The {@link EStructuralFeature}, value to start and end offsets.
	 */
	private final Map<EStructuralFeature, Map<Object, int[]>> featureValueToOffsets = new HashMap<EStructuralFeature, Map<Object, int[]>>();

	/**
	 * The deletion threshold.
	 */
	private double deleteThreshold = 0.1;

	@Override
	public EObject getTarget() {
		return (EObject)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#getText()
	 */
	public String getText() {
		final StringBuilder builder = new StringBuilder();

		offsetToFeatureValues.clear();
		featureValueToOffsets.clear();
		builder.append(START_EOBJECT);
		for (EStructuralFeature eFeature : getTarget().eClass().getEAllStructuralFeatures()) {
			writeFeature(builder, eFeature);
		}
		builder.append(END_EOBJECT);

		return builder.toString();
	}

	/**
	 * Writes the given {@link EStructuralFeature} of the given {@link StringBuilder}.
	 * 
	 * @param builder
	 *            the {@link StringBuilder}
	 * @param eFeature
	 *            the {@link EStructuralFeature}
	 */
	private void writeFeature(final StringBuilder builder, EStructuralFeature eFeature) {
		if (eFeature.isMany()) {
			@SuppressWarnings("unchecked")
			final Collection<Object> values = (Collection<Object>)getTarget().eGet(eFeature);
			for (Object value : values) {
				writeValue(builder, eFeature, value);
			}
		} else {
			final Object value = getTarget().eGet(eFeature);
			writeValue(builder, eFeature, value);
		}
	}

	/**
	 * Writes the given value in the given {@link StringBuilder}.
	 * 
	 * @param builder
	 *            the {@link StringBuilder}
	 * @param eFeature
	 *            the {@link EStructuralFeature}
	 * @param value
	 *            the value
	 */
	private void writeValue(final StringBuilder builder, EStructuralFeature eFeature, final Object value) {
		final int[] offsets = new int[2];
		Map<Object, int[]> map = featureValueToOffsets.get(eFeature);
		if (map == null) {
			map = new HashMap<Object, int[]>();
			featureValueToOffsets.put(eFeature, map);
		}
		map.put(value, offsets);
		if (eFeature instanceof EReference && ((EReference)eFeature).isContainment()) {
			if (value instanceof EObject) {
				final EObject eObject = (EObject)value;
				final ITextAdapter textAdapter = EObjectConnector.getTextAdapter(eObject);
				builder.append(START_EOBJECT);
				textAdapter.setTextOffset(getTextOffset() + builder.length());
				offsets[0] = getTextOffset() + builder.length();
				offsetToFeatureValues.put(offsets[0], new FeatureValue(eFeature, value));
				builder.append(textAdapter.getText());
				offsets[1] = getTextOffset() + builder.length();
				builder.append(END_EOBJECT);
			}
		} else {
			final String stringValue;
			if (eFeature.getEType() == EcorePackage.eINSTANCE.getEEnumerator()
					|| value instanceof EEnumLiteral) {
				stringValue = value.toString();
			} else if (eFeature.getEType() instanceof EDataType) {
				stringValue = EcoreUtil.convertToString((EDataType)eFeature.getEType(), value);
			} else if (value instanceof EObject) {
				final EObject eObject = (EObject)value;
				if (eObject.eResource() == null) {
					stringValue = "not in resource EObject.";
				} else if (eObject.eResource().getURI() != null) {
					stringValue = EcoreUtil.getURI(eObject).toString();
				} else {
					stringValue = eObject.eResource().getURIFragment(eObject);
				}
			} else if (value != null) {
				throw new IllegalStateException("don't know what to do with " + value);
			} else {
				stringValue = "null";
			}
			builder.append(START_SETTING);
			offsets[0] = getTextOffset() + builder.length();
			offsetToFeatureValues.put(offsets[0], new FeatureValue(eFeature, value));
			builder.append(eFeature.getName());
			builder.append(MIDDLE_SETTING);
			builder.append(stringValue);
			offsets[1] = getTextOffset() + builder.length();
			builder.append(END_SETTING);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#setTextOffset(int)
	 */
	public void setTextOffset(int offset) {
		textOffset = offset;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#getTextOffset()
	 */
	public int getTextOffset() {
		return textOffset;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#getElement(org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation)
	 */
	public Object getElement(IEObjectLocation location) {
		final Object res;

		final int startOffset = location.getStartOffset();
		if (startOffset == getTextOffset()) {
			if (((double)(location.getEndOffset() - location.getStartOffset()) / (double)getText().length()) >= deleteThreshold) {
				res = getTarget();
			} else {
				res = null;
			}
		} else {
			final FeatureValue featureValue = offsetToFeatureValues.get(startOffset);
			if (featureValue == null) {
				FeatureValue child = null;
				for (Entry<Integer, FeatureValue> entry : offsetToFeatureValues.entrySet()) {
					if (entry.getKey() > startOffset) {
						break;
					} else if (entry.getValue().feature instanceof EReference
							&& ((EReference)entry.getValue().feature).isContainment()) {
						child = entry.getValue();
					}
				}
				if (child != null && child.value instanceof EObject) {
					final ITextAdapter textAdapter = EObjectConnector.getTextAdapter((EObject)child.value);
					res = textAdapter.getElement(location);
				} else {
					res = null;
				}
			} else {
				// TODO compare surrounding EObject serialization and discard the location according to the
				// editing distance for instance
				if (location.getFeatureName() != null) {
					final EObject eObj = getTarget();
					final EStructuralFeature feature = featureValue.feature;
					final Object value = featureValue.value;
					res = new Setting() {
						public void unset() {
							throw new UnsupportedOperationException();
						}

						public void set(Object newValue) {
							throw new UnsupportedOperationException();
						}

						public boolean isSet() {
							throw new UnsupportedOperationException();
						}

						public EStructuralFeature getEStructuralFeature() {
							return feature;
						}

						public EObject getEObject() {
							return eObj;
						}

						public Object get(boolean resolve) {
							return value;
						}
					};
				} else {
					res = (EObject)featureValue.value;
				}
			}
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#setLocationFromEObject(org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation)
	 */
	public void setLocationFromEObject(IEObjectLocation location) {
		final String text = getText();
		location.setStartOffset(getTextOffset());
		location.setEndOffset(getTextOffset() + text.length());
		location.setFeatureName(null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#setLocationFromEObject(org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation,
	 *      org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public void setLocationFromEObject(IEObjectLocation location, EStructuralFeature feature, Object value) {
		getText();
		final int[] offsets = featureValueToOffsets.get(feature).get(value);
		location.setStartOffset(offsets[0]);
		location.setEndOffset(offsets[1]);
		location.setFeatureName(feature.getName());
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == ITextAdapter.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.emf.ITextAdapter#getOffsets(org.eclipse.emf.ecore.EStructuralFeature,
	 *      java.lang.Object)
	 */
	public int[] getOffsets(EStructuralFeature feature, Object value) {
		final int[] res = new int[2];

		final int[] offsets = featureValueToOffsets.get(feature).get(value);
		if (offsets != null) {
			res[0] = offsets[0];
			res[1] = offsets[1];
		} else {
			res[0] = -1;
			res[1] = -1;
		}

		return res;
	}

}
