package synalp.generation.morphology;

import java.util.HashMap;

/**
 * A MorphLexicon is a morphological lexicon. It associates a lemma surface form to a
 * MorphLexiconEntry.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class MorphLexicon extends HashMap<String, MorphLexiconEntry>
{
	/**
	 * Adds the given MorphLexiconEntry to this MorphLexicon. If there already exists a Lemma with
	 * the same surface form, this method aggregates the two entries instead of overwriting the
	 * previous entry.
	 * @param entry
	 */
	public void add(MorphLexiconEntry entry)
	{
		String surface = entry.getLemma().getValue();
		if (containsKey(surface))
			get(surface).getLexems().addAll(entry.getLexems());
		else put(surface, entry);
	}
}
