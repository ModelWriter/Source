package synalp.generation.jeni.filtering.dlx;

import java.util.*;

import synalp.commons.utils.*;


/**
 * Dancing link DLX implementation found at https://github.com/elindsey/ExactCover and modified.
 * @author Alexandre Denis
 */
public class DLXSolver
{
	protected Cell root;
	protected Map<Integer, Cell> path;
	protected Set<Set<String>> solutions = new HashSet<Set<String>>();


	/**
	 * Creates a new empty solver.
	 */
	public DLXSolver()
	{
		root = null;
		path = new HashMap<Integer, Cell>();
	}


	/**
	 * Creates a solver based on given sparse matrix root cell.
	 * @param root
	 */
	public DLXSolver(Cell root)
	{
		this.root = root;
		path = new HashMap<Integer, Cell>();
	}


	/**
	 * Computes and returns the solutions.
	 * @return alternative sets of strings in which each string is a row cell name
	 */
	public Set<Set<String>> getSolutions()
	{
		findSolution();
		return solutions;
	}


	/**
	 * Computes solutions.
	 */
	public void findSolution()
	{
		search(0);
	}


	private void search(int k)
	{
		checkTimeout();

		if (root.right == root)
		{
			//printSolution(k);
			solutions.add(gatherSolution(k));
			return;
		}

		Cell c = chooseColumn();
		coverColumn(c);
		for(Cell d = c.down; d != c; d = d.down)
		{
			path.put(k, d);

			for(Cell r = d.right; r != d; r = r.right)
				coverColumn(r.getColumnHeader());

			search(k + 1);
			d = path.get(k);
			c = d.getColumnHeader();

			for(Cell l = d.left; l != d; l = l.left)
				uncoverColumn(l.getColumnHeader());
		}
		uncoverColumn(c);
	}


	protected void coverColumn(Cell n)
	{
		assert (n.getColumnHeader() == null);
		n.right.left = n.left;
		n.left.right = n.right;

		Cell d = n.down;
		while(d != n)
		{
			Cell r = d.right;
			while(r != d)
			{
				r.up.down = r.down;
				r.down.up = r.up;
				r.getColumnHeader().size--;
				r = r.right;
			}
			d = d.down;
		}
	}


	private void uncoverColumn(Cell n)
	{
		assert (n.getColumnHeader() == null);

		for(Cell u = n.up; u != n; u = u.up)
		{
			for(Cell l = u.left; l != u; l = l.left)
			{
				l.getColumnHeader().size++;
				l.up.down = l;
				l.down.up = l;
			}
		}
		n.left.right = n;
		n.right.left = n;
	}


	private Cell chooseColumn()
	{
		int currSize = Integer.MAX_VALUE;
		Cell ret = null;

		for(Cell currNode = root.right; currNode != root; currNode = currNode.right)
			if (currNode.size < currSize)
			{
				currSize = currNode.size;
				ret = currNode;
			}

		return ret;
	}


	private Set<String> gatherSolution(int k)
	{
		Set<String> ret = new HashSet<String>();
		for(int i = 0; i < k; i++)
			ret.add(path.get(i).getName());
		return ret;
	}


	private void checkTimeout()
	{
		TimeoutManager.checkTimeout("Timeout in dlx solver");
	}


	protected void printSolution(int k)
	{
		System.out.println("Solution:: ");
		for(int i = 0; i < k; i++)
			System.out.println(path.get(i));
	}
}
