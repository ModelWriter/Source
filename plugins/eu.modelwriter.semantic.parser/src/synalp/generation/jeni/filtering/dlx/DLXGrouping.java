package synalp.generation.jeni.filtering.dlx;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.*;


/**
 * DLXGrouping is a weaker version of the DLXFiltering class. It only groups entries with regards to
 * semantics covering without performing packing nor filtering at all. Note that on the contrary to
 * DLXFiltering it does not require that the semantics is a BitSemantics.
 * @author Alexandre Denis
 */
public class DLXGrouping
{
	/**
	 * Groups together the entries whose semantics exactly cover the given input but do not overlap.
	 * @param entries
	 * @param input
	 * @return a set of all entries that are compatible with each other and that cover the input
	 *         semantics
	 */
	public static Set<GrammarEntries> group(GrammarEntries entries, Semantics input)
	{
		Map<String, GrammarEntry> entriesByNames = entries.getEntriesByNames();
		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(Set<String> solution : new DLXSolver(createSparseMatrix(entries, input.asList())).getSolutions())
		{
			GrammarEntries group = new GrammarEntries();
			for(String entryName : solution)
				group.add(entriesByNames.get(entryName));
			ret.add(group);
		}
		return ret;
	}


	/**
	 * Creates a sparse matrix ignoring empty semantics entries.
	 * @param entries
	 * @param literals
	 * @return the rool cell
	 */
	private static Cell createSparseMatrix(GrammarEntries entries, List<DefaultLiteral> literals)
	{
		Cell root = new Cell("Root", -1, null);
		Cell[] header = createHeader(literals, root);
		Cell[] previousTop = new Cell[header.length];
		System.arraycopy(header, 0, previousTop, 0, header.length);

		for(GrammarEntry entry : entries)
			if (!entry.getSemantics().isEmpty())
				addEntryRow(header, previousTop, entry, literals);

		// connect last row
		for(int i = 0; i < previousTop.length; i++)
		{
			previousTop[i].down = header[i];
			header[i].up = previousTop[i];
		}

		return root;
	}


	/**
	 * Inserts the given entry as a row.
	 * @param header
	 * @param previousTop in order to link the row to the previous rows
	 * @param entry
	 * @param literals
	 */
	private static void addEntryRow(Cell[] header, Cell[] previousTop, GrammarEntry entry, List<DefaultLiteral> literals)
	{
		Cell first = null;
		Cell last = null;
		Cell previousLeft = null;
		for(int i = 0; i < literals.size(); i++)
			if (entry.getSemantics().contains(literals.get(i), entry.getContext()))
			{
				Cell cell = new Cell(entry.getName(), -1, header[i]);
				header[i].size++;

				if (first == null)
					first = cell;

				// do left and right
				if (previousLeft == null)
				{
					cell.left = cell;
					cell.right = cell;
				}
				else
				{
					cell.left = previousLeft;
					previousLeft.right = cell;
				}

				previousLeft = cell;
				last = cell;

				// do up and down
				previousTop[i].down = cell;
				cell.up = previousTop[i];
				previousTop[i] = cell;
			}

		// do last left/right
		first.left = last;
		last.right = first;
	}


	/**
	 * Creates cell headers with empty counts.
	 * @param literals
	 * @param root
	 * @return the header
	 */
	private static Cell[] createHeader(List<DefaultLiteral> literals, Cell root)
	{
		Cell[] ret = new Cell[literals.size()];

		Cell first = new Cell("L0", 0, null);
		first.left = root;
		root.right = first;
		ret[0] = first;

		for(int i = 1; i < literals.size(); i++)
		{
			ret[i] = new Cell("L" + i, 0, null);
			ret[i].left = ret[i - 1];
			ret[i - 1].right = ret[i];
		}

		ret[ret.length - 1].right = root;
		root.left = ret[ret.length - 1];
		return ret;
	}
}
