package synalp.commons.semantics;

import java.util.*;

import synalp.commons.unification.InstantiationContext;


/**
 * A BitSemantics is a Semantics implemented as a BitSet. It is meant to represent the coverage of a
 * Semantics rather than a Semantics per se. Each bit denotes whether the n-th literal of the target
 * Semantics is covered by this BitSemantics or not. It is meant only to work on Semantics that do
 * not have variables, hence the context is always ignored in all methods. The BitSemantics itself
 * does not contain any Literal, it only extends Semantics for practical reasons. We may refactor
 * the code to make Semantics an interface with two different implementations.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class BitSemantics extends Semantics
{
	private BitSet coverage;
	private List<DefaultLiteral> target = new ArrayList<DefaultLiteral>();


	/**
	 * Creates a BitSemantics by copying the given BitSemantics. Both have the same target.
	 * @param source
	 */
	public BitSemantics(BitSemantics source)
	{
		target = new ArrayList<DefaultLiteral>(source.target);
		coverage = (BitSet) source.coverage.clone();
	}


	/**
	 * Creates a BitSemantics by creating a full initial coverage of the same size that given
	 * Semantics where each bit is set to b.
	 * @param target
	 * @param b
	 */
	public BitSemantics(Semantics target, boolean b)
	{
		this.target = target.asList();
		this.coverage = new BitSet(target.size());
		this.coverage.set(0, target.size(), b);
	}


	/**
	 * Creates a BitSemantics by comparing the coverage of the source with regards to target. For
	 * each n-th literal of the target Semantics, if the literal is contained in the source, sets
	 * the n-th bit to true.
	 * @param source
	 * @param target
	 * @param context
	 */
	public BitSemantics(Semantics source, Semantics target, InstantiationContext context)
	{
		this.target = target.asList();
		coverage = new BitSet(this.target.size());
		for(int i = 0; i < this.target.size(); i++)
			if (source.contains(this.target.get(i), context))
				coverage.set(i);
	}


	/**
	 * Returns a new BitSemantics that is a logical OR of this BitSemantics and the given one.
	 * @param sem
	 * @return a new BitSemantics or null if the input semantics is not a BitSemantics
	 */
	public BitSemantics join(Semantics sem)
	{
		if (sem instanceof BitSemantics)
		{
			BitSemantics bitSem = (BitSemantics) sem;
			BitSemantics ret = new BitSemantics(this);
			ret.coverage.or(bitSem.coverage);
			return ret;
		}
		else return null;
	}


	@Override
	public boolean semanticsIntersects(Semantics semantics, InstantiationContext context)
	{
		if (semantics instanceof BitSemantics)
			return coverage.intersects(((BitSemantics) semantics).coverage);
		else return false;
	}


	@Override
	public boolean equals(Semantics semantics, InstantiationContext context)
	{
		if (semantics instanceof BitSemantics)
			return coverage.equals(((BitSemantics) semantics).coverage);
		else return false;
	}


	@Override
	public boolean add(DefaultLiteral e)
	{
		return target.add(e);
	}


/// to work with filtering

	@Override
	public boolean isEmpty()
	{
		return coverage.isEmpty();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coverage == null) ? 0 : coverage.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitSemantics other = (BitSemantics) obj;
		if (coverage == null)
		{
			if (other.coverage != null)
				return false;
		}
		else if (!coverage.equals(other.coverage))
			return false;
		return true;
	}


	@Override
	public List<DefaultLiteral> asList()
	{
		List<DefaultLiteral> ret = new ArrayList<DefaultLiteral>();
		for(int i = 0; i < target.size(); i++)
			if (coverage.get(i))
				ret.add(target.get(i));
		return ret;
	}


	@Override
	public boolean contains(DefaultLiteral literal, InstantiationContext context)
	{
		int index = target.indexOf(literal);
		if (index == -1)
			return false;
		else return coverage.get(index);
	}


	@Override
	public int size()
	{
		return target.size();
	}


	@Override
	public String toString()
	{
		return coverage + ":" + asList();
	}
}
