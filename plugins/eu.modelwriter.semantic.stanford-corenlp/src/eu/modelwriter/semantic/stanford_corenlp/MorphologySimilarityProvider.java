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
package eu.modelwriter.semantic.stanford_corenlp;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Identity {@link ISemanticSimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MorphologySimilarityProvider implements ISemanticSimilarityProvider {

	/**
	 * Morphology similarity type.
	 */
	public static final Object TYPE = new Object();

	/**
	 * Standford pipeline.
	 */
	private static final StanfordCoreNLP PIPELINE = initPipeline("annotators", "tokenize, ssplit, pos, lemma");

	public Object getType() {
		return TYPE;
	}

	/**
	 * Initializes the Standford pipeline with the given properties.
	 * 
	 * @param props
	 *            the properties
	 * @return a new Standford pipeline with the given properties
	 */
	private static StanfordCoreNLP initPipeline(String... props) {
		final Properties properties = new Properties();
		properties.put("annotators", "tokenize, ssplit, pos, lemma");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
		return pipeline;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.modelwriter.semantic.ISemanticSimilarityProvider#getSemanticSimilarities(java.util.Map)
	 */
	public Map<String, Set<Object>> getSemanticSimilarities(Map<String, Set<Object>> labels) {
		final Map<String, Set<Object>> res = new LinkedHashMap<String, Set<Object>>();

		final StringBuilder builder = new StringBuilder();
		final Set<String> words = labels.keySet();
		if (!words.isEmpty()) {
			for (String label : words) {
				builder.append(label);
				builder.append(' ');
			}

			Annotation document = new Annotation(builder.substring(0, builder.length() - 1));
			PIPELINE.annotate(document);

			final List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
					final String label = token.originalText();
					final String lemma = token.lemma();
					Set<Object> lemmaSet = res.get(lemma);
					if (lemmaSet == null) {
						lemmaSet = new LinkedHashSet<Object>();
						res.put(lemma, lemmaSet);
					}
					lemmaSet.addAll(labels.get(label));
				}
			}
		}

		return res;
	}
}
