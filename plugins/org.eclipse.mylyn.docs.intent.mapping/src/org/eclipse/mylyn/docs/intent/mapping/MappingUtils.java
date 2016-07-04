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
package org.eclipse.mylyn.docs.intent.mapping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.IFactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry;
import org.eclipse.mylyn.docs.intent.mapping.internal.base.BaseRegistry;
import org.eclipse.mylyn.docs.intent.mapping.internal.connector.ConnectorRegistry;

/**
 * Mapping utility class.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class MappingUtils {

	/**
	 * Diff and match wrapping class.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static final class DiffMatch {

		/**
		 * The list of differences.
		 */
		final LinkedList<Diff> diffs;

		/**
		 * Constructor.
		 * 
		 * @param txt1
		 *            the first text
		 * @param txt2
		 *            the second text
		 */
		private DiffMatch(String txt1, String txt2) {
			diffs = DIFF_MATCH_PATCH.diff_main(txt1, txt2);
		}

		/**
		 * Reduce the number of edits by eliminating operationally trivial equalities.
		 */
		public void cleanupEfficdiency() {
			DIFF_MATCH_PATCH.diff_cleanupEfficiency(diffs);
		}

		/**
		 * Reorder and merge like edit sections. Merge equalities. Any edit section can move as long as it
		 * doesn't cross an equality.
		 */
		public void cleanupMerge() {
			DIFF_MATCH_PATCH.diff_cleanupMerge(diffs);
		}

		/**
		 * Reduce the number of edits by eliminating semantically trivial equalities.
		 */
		public void cleanupSemantic() {
			DIFF_MATCH_PATCH.diff_cleanupSemantic(diffs);
		}

		/**
		 * Look for single edits surrounded on both sides by equalities which can be shifted sideways to align
		 * the edit to a word boundary. e.g: "The c<ins>at c</ins>ame." -> "The <ins>cat </ins>came".
		 */
		public void cleanupSemanticLossless() {
			DIFF_MATCH_PATCH.diff_cleanupSemanticLossless(diffs);
		}

		/**
		 * Gets the <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein Distance</a>.
		 * 
		 * @return the <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein Distance</a>
		 */
		public int getLevenshteinDistance() {
			return DIFF_MATCH_PATCH.diff_levenshtein(diffs);
		}

		/**
		 * Gets the index in text2 from corresponding to the index in text1.
		 * 
		 * @param index
		 *            the index in text1
		 * @return the index in text2 from corresponding to the index in text1
		 */
		public int getIndex(int index) {
			return DIFF_MATCH_PATCH.diff_xIndex(diffs, index);
		}

	}

	/**
	 * The {@link IBaseRegistry}.
	 */
	private static final BaseRegistry BASE_REGISTRY = initBaseRegistry();

	/**
	 * The {@link IConnectorRegistry}.
	 */
	private static final ConnectorRegistry CONNECTOR_REGISTRY = new ConnectorRegistry();

	/**
	 * {@link IBase} kind to {@link ILocation} interface/implementation mapping.
	 */
	private static final Map<Class<? extends IBase>, Map<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>>> LOCATION_IMPLEMENTATIONS = new HashMap<Class<? extends IBase>, Map<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>>>();

	/**
	 * Diff match patch instance.
	 */
	private static final diff_match_patch DIFF_MATCH_PATCH = new diff_match_patch();

	/**
	 * Constructor.
	 */
	private MappingUtils() {
		// nothing to do here
	}

	/**
	 * Initialize the {@link BaseRegistry}.
	 * 
	 * @return the {@link BaseRegistry}
	 */
	private static BaseRegistry initBaseRegistry() {
		final BaseRegistry res = new BaseRegistry();

		res.addListener(new IBaseRegistryListener.Stub() {

			@Override
			@SuppressWarnings("unchecked")
			public void baseRegistred(IBase base) {
				for (Entry<Class<? extends IBase>, Map<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>>> entry : LOCATION_IMPLEMENTATIONS
						.entrySet()) {
					if (entry.getKey().isAssignableFrom(base.getClass())) {
						for (Entry<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>> locationEntry : entry
								.getValue().entrySet()) {
							base.getFactory().addDescriptor((Class<ILocation>)locationEntry.getKey(),
									locationEntry.getValue());
						}
					}
				}
			}

		});

		return res;
	}

	/**
	 * Gets a {@link DiffMatch} for the two given {@link String}.
	 * 
	 * @param txt1
	 *            the first text
	 * @param txt2
	 *            the second text
	 * @return a {@link DiffMatch} for the two given {@link String}
	 */
	public static DiffMatch getDiffMatch(String txt1, String txt2) {
		return new DiffMatch(txt1, txt2);
	}

	/**
	 * Gets the {@link IBaseRegistry}.
	 * 
	 * @return the {@link IBaseRegistry}
	 */
	public static IBaseRegistry getMappingRegistry() {
		return BASE_REGISTRY;
	}

	/**
	 * Gets the {@link IConnectorRegistry}.
	 * 
	 * @return the {@link IConnectorRegistry}
	 */
	public static IConnectorRegistry getConnectorRegistry() {
		return CONNECTOR_REGISTRY;
	}

	/**
	 * Registers the given {@link ILocation} implementation to instantiate the given {@link ILocation}
	 * interface for the given {@link IBase} kind.
	 * 
	 * @param baseClass
	 *            the {@link IBase} kind
	 * @param locationInterface
	 *            the {@link ILocation} interface
	 * @param descriptor
	 *            the {@link ILocation} implementation for the {@link IBase} kind
	 * @param <L>
	 *            the {@link ILocation} interface kind
	 */
	public static <L extends ILocation> void registerLocationImplementation(Class<? extends IBase> baseClass,
			Class<L> locationInterface, IFactoryDescriptor<? extends L> descriptor) {
		synchronized(getMappingRegistry()) {
			Map<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>> implementations = LOCATION_IMPLEMENTATIONS
					.get(baseClass);
			if (implementations == null) {
				implementations = new HashMap<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>>();
				LOCATION_IMPLEMENTATIONS.put(baseClass, implementations);
			}
			implementations.put(locationInterface, descriptor);
			for (IBase base : getMappingRegistry().getBases()) {
				if (baseClass.isAssignableFrom(base.getClass())) {
					base.getFactory().addDescriptor(locationInterface, descriptor);
				}
			}
		}
	}

	/**
	 * Removes the given {@link ILocation} interface instantiation {@link Class} for the given {@link IBase}.
	 * 
	 * @param baseClass
	 *            the {@link IBase} kind
	 * @param locationInterface
	 *            the {@link ILocation} interface
	 */
	public static void unregisterLocationImplementation(Class<? extends IBase> baseClass,
			Class<? extends ILocation> locationInterface) {
		synchronized(getMappingRegistry()) {
			Map<Class<? extends ILocation>, IFactoryDescriptor<? extends ILocation>> implementations = LOCATION_IMPLEMENTATIONS
					.get(baseClass);
			if (implementations != null) {
				implementations.remove(locationInterface);
			}
			for (IBase base : getMappingRegistry().getBases()) {
				if (baseClass.isAssignableFrom(base.getClass())) {
					base.getFactory().removeDescriptor(locationInterface);
				}
			}
		}
	}

	/**
	 * Gets the {@link IBase} containing the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return the {@link IBase} containing the given {@link ILocation} if nay, <code>null</code> otherwise
	 */
	public static IBase getBase(ILocation location) {
		IBase res = null;

		ILocationContainer conainer = location.getContainer();
		while (conainer != null) {
			if (conainer instanceof IBase) {
				res = (IBase)conainer;
				break;
			} else if (conainer instanceof ILocation) {
				conainer = ((ILocation)conainer).getContainer();
			} else {
				break;
			}
		}

		return res;
	}

}
