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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.eclipse.mylyn.docs.intent.mapping.base.ILink;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.IReport;
import org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry;
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

	/**
	 * Gets the containing {@link IBase} of the given {@link ILocationContainer}.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}
	 * @return the containing {@link IBase} of the given {@link ILocationContainer} if any, <code>null</code>
	 *         otherwise
	 */
	public static IBase getBase(ILocationContainer container) {
		final IBase res;

		if (container instanceof IBase) {
			res = (IBase)container;
		} else if (container instanceof ILocation) {
			res = getBase((ILocation)container);
		} else {
			throw new IllegalStateException("new ILocationContainer sub type ?");
		}

		return res;
	}

	/**
	 * Gets the {@link ILink} between {@link ILocation source} and {@link ILocation target}.
	 * 
	 * @param source
	 *            the {@link ILocation source}
	 * @param target
	 *            the {@link ILocation target}
	 * @return the {@link ILink} between {@link ILocation source} and {@link ILocation target} if any,
	 *         <code>null</code> otherwise
	 */
	public static ILink getLink(ILocation source, ILocation target) {
		ILink res = null;

		if (source.getTargetLinks().size() > target.getSourceLinks().size()) {
			for (ILink link : target.getSourceLinks()) {
				if (source.equals(link.getSource())) {
					res = link;
					break;
				}
			}
		} else {
			for (ILink link : source.getTargetLinks()) {
				if (target.equals(link.getTarget())) {
					res = link;
					break;
				}
			}
		}

		return res;
	}

	/**
	 * Tells if an {@link ILink} can be {@link MappingUtils#createLink(ILocation, ILocation) created} between
	 * the given {@link ILocation source} and {@link ILocation target}.
	 * 
	 * @param source
	 *            the source {@link ILocation}
	 * @param target
	 *            the target {@link ILocation}
	 * @return <code>true</code> if an {@link ILink} can be
	 *         {@link MappingUtils#createLink(ILocation, ILocation) created} between the given
	 *         {@link ILocation source} and {@link ILocation target}, <code>false</code> otherwise
	 */
	public static boolean canCreateLink(ILocation source, ILocation target) {
		return !source.isMarkedAsDeleted() && !target.isMarkedAsDeleted() && !source.equals(target)
				&& MappingUtils.getLink(source, target) == null;
	}

	/**
	 * Creates an {@link ILink} between the given {@link ILink#getSource() source} and
	 * {@link ILink#getTarget() target}.
	 * 
	 * @param source
	 *            the {@link ILink#getSource() source} {@link ILocation}
	 * @param target
	 *            the {@link ILink#getTarget() target} {@link ILocation}
	 * @return the created {@link ILink}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static ILink createLink(ILocation source, ILocation target) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final ILink res;

		if (canCreateLink(source, target)) {
			final IBase base = getBase(source);
			res = base.getFactory().createElement(ILink.class);
			res.setSource(source);
			res.setTarget(target);
		} else {
			throw new IllegalStateException("can't create the Link.");
		}

		return res;
	}

	/**
	 * Gets the content of the given {@link File}.
	 * 
	 * @param file
	 *            the {@link File}
	 * @param charsetName
	 *            The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	 * @return a {@link String} of the content of the given {@link File}
	 * @throws IOException
	 *             if the {@link File} can't be read
	 */
	public static String getContent(File file, String charsetName) throws IOException {
		if (!file.exists()) {
			throw new IOException(file.getAbsolutePath() + " doesn't exists.");
		} else if (file.isDirectory()) {
			throw new IOException(file.getAbsolutePath() + " is a directory.");
		} else if (!file.canRead()) {
			throw new IOException(file.getAbsolutePath() + " is not readable.");
		}
		int len = (int)file.length();
		final String res;
		if (len != 0) {
			res = getContent(len, new FileInputStream(file));
		} else {
			res = "";
		}
		return res;
	}

	/**
	 * Gets the content of the given {@link File}.
	 * 
	 * @param length
	 *            the {@link InputStream} size
	 * @param inputStream
	 *            the {@link InputStream}
	 * @return a {@link String} of the content of the given {@link File}
	 * @throws IOException
	 *             if the {@link InputStream} can't be read
	 */
	public static String getContent(int length, InputStream inputStream) throws IOException {
		final StringBuilder res = new StringBuilder(length);
		final InputStreamReader input = new InputStreamReader(new BufferedInputStream(inputStream));

		final char[] buffer = new char[length];
		int len = input.read(buffer);
		while (len != -1) {
			res.append(buffer, 0, len);
			len = input.read(buffer);
		}
		input.close();

		return res.toString();
	}

	/**
	 * Tells if the given {@link ILink} can be deleted.
	 * 
	 * @param link
	 *            the {@link ILink} to check
	 * @return <code>true</code> if the given {@link ILink} can be deleted, <code>false</code> otherwise
	 */
	public static boolean canDeleteLink(ILink link) {
		return link.getReports().isEmpty();
	}

	/**
	 * Deletes the given {@link ILink}.
	 * 
	 * @param link
	 *            the {@link ILink} to delete
	 */
	public static void deleteLink(ILink link) {
		if (canDeleteLink(link)) {
			final ILocation source = link.getSource();
			link.setSource(null);
			if (source != null && source.isMarkedAsDeleted() && canDeleteLocation(source)) {
				deleteLocation(source);
			}
			final ILocation target = link.getTarget();
			link.setTarget(null);
			if (target != null && target.isMarkedAsDeleted() && canDeleteLocation(target)) {
				deleteLocation(target);
			}
		} else {
			throw new IllegalStateException("can't delete a link with report.");
		}
	}

	/**
	 * Tells if the given {@link ILocation} can be deleted.
	 * 
	 * @param location
	 *            the {@link ILocation} to check
	 * @return <code>true</code> if the given {@link ILocation} can be deleted, <code>false</code> otherwise
	 */
	public static boolean canDeleteLocation(ILocation location) {
		return location.getSourceLinks().isEmpty() && location.getTargetLinks().isEmpty()
				&& location.getContents().isEmpty();
	}

	/**
	 * Deletes the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation} to delete
	 */
	public static void deleteLocation(ILocation location) {
		if (canDeleteLocation(location)) {
			final ILocationContainer container = location.getContainer();
			location.setContainer(null);
			if (container instanceof ILocation && ((ILocation)container).isMarkedAsDeleted()
					&& canDeleteLocation((ILocation)container)) {
				deleteLocation((ILocation)container);
			}
		} else {
			throw new IllegalStateException("can't delete a location with links or contained locations.");
		}
	}

	/**
	 * Deletes the given {@link IReport}.
	 * 
	 * @param report
	 *            the {@link IReport} to delete
	 */
	public static void deleteReport(IReport report) {
		final IBase base = getBase(report.getLink().getSource());
		base.getReports().remove(report);
		report.setLink(null);
	}

	/**
	 * {@link MappingUtils#deleteLocation(ILocation) Deletes} if
	 * {@link MappingUtils#canDeleteLocation(ILocation) possible} or
	 * {@link ILocation#setMarkedAsDeleted(boolean) mark as deleted} the given location. In the latest case an
	 * {@link IReport} is created with the given {@link IReport#getDescription() description}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @param reportDescription
	 *            the {@link IReport} {@link IReport#getDescription() description}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void markAsDeletedOrDelete(ILocation location, String reportDescription)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (MappingUtils.canDeleteLocation(location)) {
			MappingUtils.deleteLocation(location);
		} else {
			location.setMarkedAsDeleted(true);
			markAsChanged(location, reportDescription);
		}
	}

	/**
	 * Marks the given location as changed and {@link IReport} are created.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @param reportDescription
	 *            the {@link IReport} {@link IReport#getDescription() description}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void markAsChanged(ILocation location, String reportDescription)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final IBase base = MappingUtils.getBase(location);
		for (ILink link : location.getSourceLinks()) {
			createReport(base, link, reportDescription);
		}
		for (ILink link : location.getTargetLinks()) {
			createReport(base, link, reportDescription);
		}
	}

	/**
	 * Creates an {@link IReport} for the given {@link ILink} with the given description.
	 * 
	 * @param base
	 *            the IBase
	 * @param link
	 *            the {@link ILink}
	 * @param reportDescription
	 *            the {@link IReport} {@link IReport#getDescription() description}
	 * @return the created {@link IReport}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	private static IReport createReport(final IBase base, ILink link, String reportDescription)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final IReport res = base.getFactory().createElement(IReport.class);

		res.setLink(link);
		res.setDescription(reportDescription);
		base.getReports().add(res);

		return res;
	}

}
