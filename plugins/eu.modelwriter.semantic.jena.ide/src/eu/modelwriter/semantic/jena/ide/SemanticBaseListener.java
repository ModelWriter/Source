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
package eu.modelwriter.semantic.jena.ide;

import com.hp.hpl.jena.rdf.model.Model;

import eu.modelwriter.semantic.SemanticUtils;
import eu.modelwriter.semantic.jena.Base;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * An {@link IResourceChangeListener} that
 * {@link eu.modelwriter.semantic.IBaseRegistry#register(eu.modelwriter.semantic.IBase) register}/
 * {@link eu.modelwriter.semantic.IBaseRegistry#unregister(eu.modelwriter.semantic.IBase) unregister}
 * {@link Base} of the {@link eu.modelwriter.semantic.IBaseRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticBaseListener implements IResourceChangeListener {

	/**
	 * {@link Model} to {@link IFile} mapping.
	 */
	private static final Map<Model, IFile> MODEL_TO_IFILE = new HashMap<Model, IFile>();

	/**
	 * The "Unable to load mapping base from " message.
	 */
	private static final String UNABLE_TO_LOAD_SEMANTIC_BASE_FROM = "Unable to load semantic base from ";

	/**
	 * The turtle file extension.
	 */
	private static final String TURTLE_FILE_EXTENSION = "ttl";

	/**
	 * Mapping of {@link IFile} to
	 * {@link eu.modelwriter.semantic.IBaseRegistry#register(eu.modelwriter.semantic.IBase) registered}
	 * {@link Base}.
	 */
	private final Map<IFile, Base> resourceToBase = new HashMap<IFile, Base>();

	/**
	 * Constructor.
	 * 
	 * @param scan
	 *            tells if we should scan the workspace
	 */
	public SemanticBaseListener(boolean scan) {
		if (scan) {
			scan(ResourcesPlugin.getWorkspace().getRoot());
		}
	}

	/**
	 * Scans the given {@link IContainer} for bases.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 */
	private void scan(IContainer container) {
		IResource[] members;
		try {
			members = container.members();
			for (IResource member : members) {
				if (member instanceof IContainer) {
					scan((IContainer)member);
				} else if (member instanceof IFile) {
					register((IFile)member);
				}
			}
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.WARNING, Activator.PLUGIN_ID, UNABLE_TO_LOAD_SEMANTIC_BASE_FROM
							+ e.getMessage(), e));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getDelta() != null) {
			walkDelta(event.getDelta(), new HashMap<IPath, IResource>());
		}
	}

	/**
	 * Walks the {@link IResourceDelta} tree.
	 * 
	 * @param delta
	 *            the root {@link IResourceDelta}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 */
	private void walkDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources) {
		processDelta(delta, movedResources);
		for (IResourceDelta child : delta.getAffectedChildren()) {
			walkDelta(child, movedResources);
		}
	}

	/**
	 * Processes the given {@link IResourceDelta} by
	 * {@link WorkspaceListener#fireChange(fr.obeo.dsl.workspace.listener.change.IChange) firing}
	 * {@link fr.obeo.dsl.workspace.listener.change.IChange IChange}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} to process
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 */
	private void processDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources) {
		if (delta.getResource() instanceof IFile) {
			switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					processAddedDelta(delta, movedResources);
					break;

				case IResourceDelta.REMOVED:
					processRemovedDelta(delta, movedResources);
					break;

				case IResourceDelta.CHANGED:
					processChangedDelta(delta);
					break;

				default:
					break;
			}
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#CHANGED changed}
	 * {@link IResourceDelta#getKind() kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#CHANGED changed}
	 *            {@link IResourceDelta#getKind() kind}
	 */
	private void processChangedDelta(IResourceDelta delta) {
		if ((delta.getFlags() & IResourceDelta.OPEN) != 0) {
			if (delta.getResource().isAccessible()) {
				register((IFile)delta.getResource());
			} else {
				unregister((IFile)delta.getResource());
			}
		} else if ((delta.getFlags() & IResourceDelta.DESCRIPTION) != 0) {
			// nothing to do here
		} else if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
			if (resourceToBase.get(delta.getResource()) == null) {
				register((IFile)delta.getResource());
			}
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#REMOVED removed}
	 * {@link IResourceDelta#getKind() kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#REMOVED removed}
	 *            {@link IResourceDelta#getKind() kind}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 */
	private void processRemovedDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources) {
		if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
			final IResource target = movedResources.get(delta.getMovedToPath());
			if (target != null) {
				resourceMoved((IFile)delta.getResource(), (IFile)target);
			} else {
				movedResources.put(delta.getResource().getFullPath(), delta.getResource());
			}
		} else {
			unregister((IFile)delta.getResource());
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#ADDED added} {@link IResourceDelta#getKind()
	 * kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#ADDED added}
	 *            {@link IResourceDelta#getKind() kind}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 */
	private void processAddedDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources) {
		if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
			final IResource source = movedResources.get(delta.getMovedFromPath());
			if (source != null) {
				resourceMoved((IFile)source, (IFile)delta.getResource());
			} else {
				movedResources.put(delta.getResource().getFullPath(), delta.getResource());
			}
		} else {
			register((IFile)delta.getResource());
		}
	}

	/**
	 * Handles a {@link IFile} move.
	 * 
	 * @param source
	 *            the source {@link IFile}
	 * @param target
	 *            the target {@link IFile}
	 */
	private void resourceMoved(IFile source, IFile target) {
		if (TURTLE_FILE_EXTENSION.equals(source.getFileExtension())) {
			if (TURTLE_FILE_EXTENSION.equals(target.getFileExtension())) {
				final Base base = resourceToBase.remove(source);
				resourceToBase.put(target, base);
			} else {
				unregister(source);
			}
		} else {
			if (TURTLE_FILE_EXTENSION.equals(target.getFileExtension())) {
				register(target);
			} else {
				// nothing to do here
			}
		}

	}

	/**
	 * {@link eu.modelwriter.semantic.IBaseRegistry#register(eu.modelwriter.semantic.IBase) Unregisters} the
	 * {@link Base} stored in the given {@link IFile} if any, does nothing otherwise.
	 * 
	 * @param file
	 *            the {@link IFile}
	 */
	private void register(IFile file) {
		final Base base = getBaseFromFile(file);
		if (base != null) {
			resourceToBase.put(file, base);
			MODEL_TO_IFILE.put(base.getModel(), file);
			SemanticUtils.getSemanticBaseRegistry().register(base);
		}
	}

	/**
	 * Gets the {@link Base} from the given {@link IFile}.
	 * 
	 * @param file
	 *            the {@link IFile}
	 * @return the {@link Base} from the given {@link IFile} if nay, <code>null</code> otherwise
	 */
	private Base getBaseFromFile(IFile file) {
		Base res = null;

		if (TURTLE_FILE_EXTENSION.equals(file.getFileExtension())) {
			try {
				final Model model = RDFDataMgr.loadModel(file.getLocation().toFile().getAbsolutePath(),
						Lang.TTL);
				if (!model.isEmpty()) {
					res = new Base(model);
				}
				// CHECKSTYLE:OFF
			} catch (Exception e) {
				// CHECKSTYLE:ON
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, UNABLE_TO_LOAD_SEMANTIC_BASE_FROM
								+ file.getLocation().toString(), e));
			}
		}

		return res;
	}

	/**
	 * {@link eu.modelwriter.semantic.IBaseRegistry#unregister(eu.modelwriter.semantic.IBase) Unregisters} the
	 * {@link Base} corresponding to the given {@link IFile}.
	 * 
	 * @param file
	 *            the {@link IFile}
	 */
	private void unregister(IFile file) {
		final Base base = resourceToBase.remove(file);
		if (base != null) {
			MODEL_TO_IFILE.remove(base.getModel());
			SemanticUtils.getSemanticBaseRegistry().unregister(base);
		}
	}

	/**
	 * Gets the {@link IFile} for the given {@link Model}.
	 * 
	 * @param model
	 *            the {@link Model}
	 * @return the {@link IFile} for the given {@link Model} if any registered, <code>null</code> otherwise
	 */
	public static IFile getFile(Model model) {
		return MODEL_TO_IFILE.get(model);
	}

}
