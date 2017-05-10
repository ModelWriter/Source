package synalp.generation.jeni.filtering.dlx;

/**
 * Dancing link DLX implementation found at https://github.com/elindsey/ExactCover.
 * @author Alexandre Denis
 *
 */
@SuppressWarnings("javadoc")
public class Cell
{
	public int size;
	private final String name;
	private Cell columnHeader;
	public Cell left, right, up, down;


	public Cell(String name, int size, Cell columnHeader)
	{
		this.size = size;
		this.name = name;
		this.columnHeader = columnHeader;

		left = this;
		right = this;
		up = this;
		down = this;
	}


	public String getName()
	{
		return name;
	}


	public Cell getColumnHeader()
	{
		return columnHeader;
	}


	public String toString()
	{
		return name + ":" + size;
	}
}