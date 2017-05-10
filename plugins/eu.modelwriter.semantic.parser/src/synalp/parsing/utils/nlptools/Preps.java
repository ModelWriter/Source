package synalp.parsing.utils.nlptools;

import java.util.Collection;
import java.util.HashSet;

/**
 * To override the contains method. I want to return true if a match for a multiword preposition (represented as _+_ separated words in sentences
 * for parser input but as space separated words in the PREP_VALUES field of PoS.java file)
 * @author bikash
 *
 */
public class Preps extends HashSet<String> {

	private static final long serialVersionUID = 1L;

	public Preps() {
		super();
	}

	public Preps(Collection<? extends String> c) {
		super(c);
	}

	public Preps(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Preps(int initialCapacity) {
		super(initialCapacity);
	}
	
	@Override
	public boolean contains(Object x) {
		String text = (String) x;
		text = text.trim();
		text = text.replaceAll("_\\+_", " ");
		if (super.contains(text))
			return true;
		else 
			return false;
	}
}