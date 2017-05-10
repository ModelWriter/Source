package synalp.generation.jeni.filtering.dlx;

import java.util.*;

import org.apache.log4j.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.*;
import synalp.commons.utils.*;
import synalp.generation.jeni.filtering.*;


/**
 * Implements PolarityFiltering using Dancing Link algorithm which is meant to resolve the exact
 * cover problem. The idea is to consider subsets of entries that exactly cover the semantic input
 * without overlap. It first creates the sparse matrix as needed by the algorithm then calls the
 * solver, retrieves the results and filters them.
 * @author Alexandre Denis
 */
public class DLXFiltering implements PolarityFiltering
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(DLXFiltering.class);

	private Map<String, EntryPack> entryPacks = new HashMap<String, EntryPack>();


	@Override
	public Set<GrammarEntries> filter(PolarityKey rootKey, Semantics input, Set<GrammarEntry> entries)
	{
		logStart(entries);
		Set<GrammarEntries> ret = filter(new PolarityContext(rootKey, input.asList(), entries));
		logEnd(ret);
		return ret;
	}


	/**
	 * Filters the entries according to the given context. It first creates a sparse matrix
	 * representing the coverage of each entry of the input literals, then solves the exact cover
	 * problem using DLX, and eventually filters the set of covering entries according to polarity.
	 * @param context
	 * @return a set of set of grammar entries
	 */
	private Set<GrammarEntries> filter(PolarityContext context)
	{
		return filterBySemPacking(context);
	}


	/**
	 * Create packs with sem only: less number of packs, faster dlx solving, but slower unpacking
	 * and polarity filtering. This solution is not the fastest but it makes the integration of
	 * empty semantics easier.
	 * @param context
	 * @return a set of GrammarEntries
	 */
	protected Set<GrammarEntries> filterBySemPacking(PolarityContext context)
	{
		entryPacks = EntryPack.createPacksSemOnly(context);
		logPacking(entryPacks);

		Cell root = createSparseMatrix(context);
		Set<Set<String>> allSolutions = new DLXSolver(root).getSolutions();

		logSolutions(allSolutions);

		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		Set<GrammarEntry> emptySemanticsEntries = context.getEmptySemanticsEntries();
		for(Set<String> solution : allSolutions)
			for(GrammarEntries entries : EntryPack.unpack(getPacks(solution)))
			{
				TimeoutManager.checkTimeout("Timeout in polarity filtering (unpacking)");
				ret.addAll(expandToZeroPolarity(entries, emptySemanticsEntries, context));
			}
		return ret;
	}


	/**
	 * Create packs with sem and keys: greater number of solutions, slower dlx solving, faster
	 * unpacking and filtering. No empty semantics have been integrated yet.
	 * @param context
	 * @return a set of GrammarEntries
	 */
	protected Set<GrammarEntries> filterBySemAndKeyPacking(PolarityContext context)
	{
		entryPacks = EntryPack.createPacksSemKey(context);

		System.out.println(entryPacks.size() + " packs");

		logPacking(entryPacks);

		Cell root = createSparseMatrix(context);
		Set<Set<String>> allSolutions = new DLXSolver(root).getSolutions();

		System.out.println(allSolutions.size() + " solutions");
		logSolutions(allSolutions);

		Set<List<EntryPack>> zeroPolarityPacks = new HashSet<List<EntryPack>>();
		for(Set<String> solution : allSolutions)
		{
			TimeoutManager.checkTimeout("Timeout in polarity filering (polarity check)");
			List<EntryPack> packs = getPacks(solution);
			if (isZeroPolarity(packs, context))
				zeroPolarityPacks.add(packs);
		}

		System.out.println(zeroPolarityPacks.size() + " zero polarity packs");

		logZeroPolarity(zeroPolarityPacks);

		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(List<EntryPack> packs : zeroPolarityPacks)
		{
			TimeoutManager.checkTimeout("Timeout in polarity filering (unpacking)");
			ret.addAll(EntryPack.unpack(packs));
		}

		return ret;
	}


	/**
	 * Tests if the given packs sum to zero polarity.
	 * @param packs
	 * @param context
	 * @return wether the combined packs sums to 0 polarity
	 */
	private boolean isZeroPolarity(Collection<EntryPack> packs, PolarityContext context)
	{
		PolarityKeys keys = new PolarityKeys();
		keys.put(context.getRootKey(), new Interval(-1, -1));
		for(EntryPack group : packs)
			keys.incrementAll(context.getPolarity(group.getRepresentative()));
		return keys.isZero();
	}


	/**
	 * Returns the list of packs referred to by the given solution. Each element of the input list
	 * is a pack id.
	 * @param solution
	 * @return a list of entry packs
	 */
	private List<EntryPack> getPacks(Set<String> solution)
	{
		List<EntryPack> ret = new ArrayList<EntryPack>();
		for(String id : solution)
			ret.add(entryPacks.get(id));
		return ret;
	}


	/**
	 * Expands the given base of entries to include empty semantics entries such that the resulting
	 * polarity is zero. This is unfortunately required if we want to consider empty semantics
	 * entries since they may or may not be present in the output. It is not a problem for empty
	 * semantics entries that are auxiliary, but for all others, it is needed to count them. This
	 * method iterates through the powerset of empty semantics entries and check if adding them or
	 * not to the given base results in zero polarity.
	 * @param base
	 * @param emptySemanticsEntries
	 * @param context
	 * @return all zero polarity entries potentially expanded with empty semantics entries
	 */
	@SuppressWarnings("unchecked")
	private Set<GrammarEntries> expandToZeroPolarity(GrammarEntries base, Set<GrammarEntry> emptySemanticsEntries, PolarityContext context)
	{
		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		if (emptySemanticsEntries.isEmpty())
		{
			if (context.isZeroPolarity(base))
				ret.add(base);
			return ret;
		}
		else
		{
			PowerSet<GrammarEntry> powerSet = new PowerSet<GrammarEntry>(emptySemanticsEntries);
			for(Set<GrammarEntry> subset : powerSet)
			{
				GrammarEntries entriesToTest = new GrammarEntries(base, subset);
				if (context.isZeroPolarity(entriesToTest))
					ret.add(entriesToTest);
			}
			return ret;
		}
	}


	/**
	 * Creates grammar entries from their names.
	 * @param solution
	 * @param context
	 * @return grammar entries
	 */
	@SuppressWarnings("unused")
	private GrammarEntries createGrammarEntries(Set<String> solution, PolarityContext context)
	{
		GrammarEntries ret = new GrammarEntries();
		for(String sol : solution)
			ret.add(context.getEntryByName(sol));
		return ret;
	}


	/**
	 * Creates a sparse matrix ignoring empty semantics entries.
	 * @param context
	 * @return the rool cell
	 */
	private Cell createSparseMatrix(PolarityContext context)
	{
		Cell root = new Cell("Root", -1, null);
		Cell[] header = createHeader(context.getLiterals(), root);
		Cell[] previousTop = new Cell[header.length];
		System.arraycopy(header, 0, previousTop, 0, header.length);

		for(EntryPack group : entryPacks.values())
			if (!group.getRepresentative().getSemantics().isEmpty())
				addEntryRow(header, previousTop, group, context.getLiterals());

		/*for(GrammarEntry entry : context.getEntries())
			if (!entry.getSemantics().isEmpty())
				addEntryRow(header, previousTop, entry, context.getLiterals());*/

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
	 * @param group
	 * @param literals
	 */
	private void addEntryRow(Cell[] header, Cell[] previousTop, EntryPack group, List<DefaultLiteral> literals)
	{
		Cell first = null;
		Cell last = null;
		Cell previousLeft = null;
		GrammarEntry representative = group.getRepresentative();
		for(int i = 0; i < literals.size(); i++)
			if (representative.getSemantics().contains(literals.get(i), representative.getContext()))
			{
				Cell cell = new Cell(group.getId(), -1, header[i]);
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
	 * Inserts the given entry as a row.
	 * @param header
	 * @param previousTop in order to link the row to the previous rows
	 * @param entry
	 * @param literals
	 */
	@SuppressWarnings("unused")
	private void addEntryRow(Cell[] header, Cell[] previousTop, GrammarEntry entry, List<DefaultLiteral> literals)
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
	private Cell[] createHeader(List<DefaultLiteral> literals, Cell root)
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


///// logging	

	private static void logStart(Set<GrammarEntry> entries)
	{
		if (logger.isInfoEnabled())
		{
			Perf.logStart("dlxfiltering");
			logger.info("Starting DLX filtering (" + entries.size() + " initial entries)");
		}
	}


	private static void logPacking(Map<String, EntryPack> entryPacks)
	{
		if (logger.isDebugEnabled())
			logger.debug("After packing (" + entryPacks.size() + " packs)");
	}


	private static void logSolutions(Set<Set<String>> allSolutions)
	{
		if (logger.isInfoEnabled())
		{
			logger.info("All solutions after solving (" + allSolutions.size() + " solutions)");
			if (logger.isTraceEnabled())
			{
				for(Set<String> solution : allSolutions)
					logger.trace(solution.toString());
			}
		}
	}


	private static void logZeroPolarity(Set<List<EntryPack>> zeroPolarityPacks)
	{
		if (logger.isDebugEnabled())
			logger.debug("After filtering non-zero polarity (" + zeroPolarityPacks.size() + " solutions)");
	}


	private static void logEnd(Set<GrammarEntries> entries)
	{
		if (logger.isInfoEnabled())
			logger.info("End of DLX filtering (" + Perf.formatTime(Perf.logEnd("dlxfiltering")) + ", " + entries.size() + " groups of entries)");
	}

}
