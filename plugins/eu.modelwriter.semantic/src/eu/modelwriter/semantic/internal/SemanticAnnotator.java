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
package eu.modelwriter.semantic.internal;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import eu.modelwriter.semantic.ISemanticAnnotator;
import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Default implementation of {@link ISemanticAnnotator}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticAnnotator implements ISemanticAnnotator {

	/**
	 * The {@link RunAutomaton} matching words.
	 */
	private static final RunAutomaton WORD_AUTOMATON = new RunAutomaton(new RegExp("[a-zA-Z]+").toAutomaton());

	/**
	 * The {@link List} of {@link ISemanticProvider}.
	 */
	protected final List<ISemanticProvider> semanticProviders = new ArrayList<ISemanticProvider>();

	/**
	 * The {@link List} of {@link ISemanticSimilarityProvider}.
	 */
	protected final List<ISemanticSimilarityProvider> similarityProviders = new ArrayList<ISemanticSimilarityProvider>();

	/**
	 * Cached {@link RunAutomaton}.
	 */
	private RunAutomaton cachedAutomaton;

	/**
	 * Cached similarities.
	 */
	private Map<String, Map<Object, Set<Object>>> cachedSimilarities;

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#addSemanticProvider(eu.modelwriter.semantic.ISemanticProvider)
	 */
	public void addSemanticProvider(ISemanticProvider provider) {
		if (provider != null) {
			int index = 0;
			boolean added = false;
			for (ISemanticProvider semanticProvider : semanticProviders) {
				if (semanticProvider.getConceptType().isAssignableFrom(provider.getConceptType())) {
					semanticProviders.add(index, provider);
					clearCache();
					added = true;
					break;
				} else {
					index++;
				}
			}
			if (!added) {
				semanticProviders.add(index, provider);
				clearCache();
			}
		} else {
			throw new IllegalArgumentException("ISemanticProvider can't be null.");
		}
	}

	/**
	 * Clear cached elements.
	 */
	public void clearCache() {
		cachedAutomaton = null;
		cachedSimilarities = null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#removeSemanticProvider(eu.modelwriter.semantic.ISemanticProvider)
	 */
	public void removeSemanticProvider(ISemanticProvider provider) {
		if (provider != null) {
			semanticProviders.remove(provider);
			clearCache();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#addSemanticSimilarityProvider(eu.modelwriter.semantic.ISemanticSimilarityProvider)
	 */
	public void addSemanticSimilarityProvider(ISemanticSimilarityProvider provider) {
		if (provider != null) {
			similarityProviders.add(provider);
			clearCache();
		} else {
			throw new IllegalArgumentException("ISemanticSimilarityProvider can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#removeSemanticSimilarityProvider(eu.modelwriter.semantic.ISemanticSimilarityProvider)
	 */
	public void removeSemanticSimilarityProvider(ISemanticSimilarityProvider provider) {
		if (provider != null) {
			similarityProviders.remove(provider);
			clearCache();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#getSemanticAnnotations(java.lang.String, java.util.Set)
	 */
	public Map<Object, Map<Object, Set<int[]>>> getSemanticAnnotations(String text, Set<?> concepts) {
		// label to concepts mapping
		final Map<String, Set<Object>> labels = getLabelToConcepts(concepts);

		// semantic similarity -> similarity type -> concepts
		cachedSimilarities = getSimilarities(labels);

		// concept -> similarity type -> positions
		cachedAutomaton = computeSimilarityAutomaton(cachedSimilarities);

		return getSemanticAnnotations(text);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticAnnotator#getSemanticAnnotations(java.lang.String)
	 */
	public Map<Object, Map<Object, Set<int[]>>> getSemanticAnnotations(String text) {
		if (cachedAutomaton == null || cachedSimilarities == null) {
			throw new IllegalStateException(
					"You should call getSemanticAnnotations(String text, Set<?> concepts)");
		} else {
			final Map<Object, Map<Object, Set<int[]>>> conceptToText = doSimilaritiesToTextAnnotation(text,
					cachedAutomaton, cachedSimilarities);

			final Map<Object, Map<Object, Set<int[]>>> textToConcept = doTextToSimilarityAnnotation(text,
					cachedSimilarities);

			return mergePositions(conceptToText, textToConcept);
		}
	}

	/**
	 * Merges positions maps.
	 * 
	 * @param conceptToText
	 *            the concept to text position mapping
	 * @param textToConcept
	 *            the text to concept position mapping
	 * @return the merged position mapping
	 */
	private Map<Object, Map<Object, Set<int[]>>> mergePositions(
			Map<Object, Map<Object, Set<int[]>>> conceptToText,
			Map<Object, Map<Object, Set<int[]>>> textToConcept) {
		final Map<Object, Map<Object, Set<int[]>>> res = new LinkedHashMap<Object, Map<Object, Set<int[]>>>(
				conceptToText);

		for (Entry<Object, Map<Object, Set<int[]>>> conceptEntry : textToConcept.entrySet()) {
			final Map<Object, Set<int[]>> similarityMap = res.get(conceptEntry.getKey());
			if (similarityMap == null) {
				res.put(conceptEntry.getKey(), conceptEntry.getValue());
			} else {
				for (Entry<Object, Set<int[]>> similarityEntry : conceptEntry.getValue().entrySet()) {
					final Set<int[]> positions = similarityMap.get(similarityEntry.getKey());
					if (positions == null) {
						similarityMap.put(similarityEntry.getKey(), similarityEntry.getValue());
					} else {
						positions.addAll(similarityEntry.getValue());
					}
				}
			}
		}

		return res;
	}

	/**
	 * Does the annotation of the similarity applied to the text.
	 * 
	 * @param text
	 *            the text
	 * @param similarities
	 *            similarity mapping
	 * @return the concept -> similarity type -> positions mapping
	 */
	private Map<Object, Map<Object, Set<int[]>>> doTextToSimilarityAnnotation(String text,
			Map<String, Map<Object, Set<Object>>> similarities) {
		final Map<Object, Map<Object, Set<int[]>>> res = new HashMap<Object, Map<Object, Set<int[]>>>();

		// word of the text to its position int[]
		Map<String, Set<Object>> wordToPositions = getWordPositions(text, similarities.keySet());

		// Similarity word -> similarity type -> int[]
		final Map<String, Map<Object, Set<Object>>> textSimilarities = getSimilarities(wordToPositions);

		for (Entry<String, Map<Object, Set<Object>>> similarityEntry : similarities.entrySet()) {
			Map<Object, Set<Object>> similarityToPositions = textSimilarities.get(similarityEntry.getKey());
			if (similarityToPositions != null) {
				for (Entry<Object, Set<Object>> positionsEntry : similarityToPositions.entrySet()) {
					Set<int[]> positions = extractPositions(positionsEntry.getValue());
					final Object similarityType = positionsEntry.getKey();
					final Set<Object> concepts = similarityEntry.getValue().get(similarityType);
					if (concepts != null) {
						for (Object concept : concepts) {
							Map<Object, Set<int[]>> map = res.get(concept);
							if (map == null) {
								map = new HashMap<Object, Set<int[]>>();
								res.put(concept, map);
							}
							Set<int[]> set = map.get(similarityType);
							if (set == null) {
								set = new LinkedHashSet<int[]>();
								map.put(similarityType, set);
							}
							set.addAll(positions);
						}
					}
				}
			}
		}

		return res;
	}

	/**
	 * Casts the given {@link Set} of {@link Object} which are indead int[].
	 * 
	 * @param value
	 *            the {@link Set} of {@link Object} which are indead int[]
	 * @return the {@link Set} of int[]
	 */
	private Set<int[]> extractPositions(Set<Object> value) {
		final Set<int[]> res = new LinkedHashSet<int[]>();

		for (Object object : value) {
			res.add((int[])object);
		}

		return res;
	}

	/**
	 * Gets the word to position mapping.
	 * 
	 * @param text
	 *            the text
	 * @param analyzedWords
	 *            the already analyzed words
	 * @return the word to position mapping
	 */
	private Map<String, Set<Object>> getWordPositions(String text, Set<String> analyzedWords) {
		final Map<String, Set<Object>> res = new HashMap<String, Set<Object>>();

		final AutomatonMatcher matcher = WORD_AUTOMATON.newMatcher(text);
		while (matcher.find()) {
			final String foundWord = text.substring(matcher.start(), matcher.end());
			if (!analyzedWords.contains(foundWord)) {
				final int[] position = new int[] {matcher.start(), matcher.end() };
				Set<Object> positions = res.get(foundWord);
				if (positions == null) {
					positions = new LinkedHashSet<Object>();
					res.put(foundWord, positions);
				}
				positions.add(position);
			}
		}

		return res;
	}

	/**
	 * Does the annotation from given similarities to the given text.
	 * 
	 * @param text
	 *            the text to annotate
	 * @param runAutomaton
	 *            the {@link RunAutomaton} computed from given similarities
	 * @param similarities
	 *            similarities mapping
	 * @return the concept -> similarity type -> positions mapping
	 */
	private Map<Object, Map<Object, Set<int[]>>> doSimilaritiesToTextAnnotation(String text,
			RunAutomaton runAutomaton, Map<String, Map<Object, Set<Object>>> similarities) {
		final Map<Object, Map<Object, Set<int[]>>> res = new HashMap<Object, Map<Object, Set<int[]>>>();

		final AutomatonMatcher matcher = runAutomaton.newMatcher(text);
		while (matcher.find()) {
			if (isFullWord(text, matcher.start(), matcher.end())) {
				final String foundWord = text.substring(matcher.start(), matcher.end());
				final Map<Object, Set<Object>> similarityMap = similarities.get(foundWord);
				if (similarityMap != null) {
					for (Entry<Object, Set<Object>> entry : similarityMap.entrySet()) {
						final Object similarityType = entry.getKey();
						for (Object concept : entry.getValue()) {
							Map<Object, Set<int[]>> resMap = res.get(concept);
							if (resMap == null) {
								resMap = new LinkedHashMap<Object, Set<int[]>>();
								res.put(concept, resMap);
							}
							Set<int[]> positions = resMap.get(similarityType);
							if (positions == null) {
								positions = new LinkedHashSet<int[]>();
								resMap.put(similarityType, positions);
							}
							positions.add(new int[] {matcher.start(), matcher.end() });
						}
					}
				}
			}
		}

		return res;
	}

	/**
	 * Computes the similatiry {@link RunAutomaton} from the given similarities.
	 * 
	 * @param similarities
	 *            the mapping of similarities
	 * @return the computed {@link RunAutomaton}
	 */
	private RunAutomaton computeSimilarityAutomaton(Map<String, Map<Object, Set<Object>>> similarities) {
		Automaton automaton = Automaton.makeEmpty();
		for (String word : similarities.keySet()) {
			automaton = automaton.union(Automaton.makeString(word, false));
		}

		final RunAutomaton runAutomaton = new RunAutomaton(automaton);
		return runAutomaton;
	}

	/**
	 * Tells if the given positions represent a complet word in the given text.
	 * 
	 * @param text
	 *            the text
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @return <code>true</code> if the given positions represent a complet word in the given text,
	 *         <code>false</code> otherwise
	 */
	private boolean isFullWord(String text, int start, int end) {
		final boolean res;

		if (start <= 0 || !Character.isAlphabetic(text.codePointAt(start - 1))) {
			res = end >= text.length() || !Character.isAlphabetic(text.codePointAt(end));
		} else {
			res = false;
		}

		return res;
	}

	/**
	 * Gets the similarity word to similarity type to {@link Object concepts} mapping.
	 * 
	 * @param labels
	 *            the mapping from label to {@link Object concepts}
	 * @return the similarity word to similarity type to {@link Object concepts} mapping
	 */
	private Map<String, Map<Object, Set<Object>>> getSimilarities(Map<String, Set<Object>> labels) {
		final Map<String, Map<Object, Set<Object>>> res = new HashMap<String, Map<Object, Set<Object>>>();

		for (ISemanticSimilarityProvider provider : new ArrayList<ISemanticSimilarityProvider>(
				similarityProviders)) {
			final Map<String, Set<Object>> semanticSimilatiries = provider.getSemanticSimilarities(labels);
			if (semanticSimilatiries != null) {
				for (Entry<String, Set<Object>> similarityEntry : semanticSimilatiries.entrySet()) {
					Map<Object, Set<Object>> simalirityMap = res.get(similarityEntry.getKey());
					if (simalirityMap == null) {
						simalirityMap = new HashMap<Object, Set<Object>>();
						res.put(similarityEntry.getKey(), simalirityMap);
					}
					Set<Object> simalirityConcepts = simalirityMap.get(provider.getType());
					if (simalirityConcepts == null) {
						simalirityConcepts = new LinkedHashSet<Object>();
						simalirityMap.put(provider.getType(), simalirityConcepts);
					}
					simalirityConcepts.addAll(similarityEntry.getValue());
				}
			}
		}

		return res;
	}

	/**
	 * Gets the mapping from label to the given {@link Object concepts}.
	 * 
	 * @param concepts
	 *            the {@link Set} of {@link Object concepts}
	 * @return the mapping from label to the given {@link Object concepts}
	 */
	private Map<String, Set<Object>> getLabelToConcepts(Set<?> concepts) {
		final Map<String, Set<Object>> res = new HashMap<String, Set<Object>>();

		for (Object concept : concepts) {
			for (ISemanticProvider provider : semanticProviders) {
				final Set<String> semanticLabels = provider.getSemanticLabels(concept);
				if (semanticLabels != null) {
					for (String semanticLabel : semanticLabels) {
						Set<Object> set = res.get(semanticLabel);
						if (set == null) {
							set = new LinkedHashSet<Object>();
							res.put(semanticLabel, set);
						}
						set.add(concept);
					}
					break;
				}
			}
		}

		return res;
	}

}
