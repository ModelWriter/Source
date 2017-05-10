package synalp.generation.jeni.filtering;

/**
 * Simple interval representation.
 * @author Alexandre Denis
 */
@SuppressWarnings("javadoc")
public class Interval
{
	public int start;
	public int end;


	/**
	 * 
	 */
	public Interval()
	{
		this.start = 0;
		this.end = 0;
	}


	/**
	 * @param start
	 * @param end
	 */
	public Interval(int start, int end)
	{
		this.start = start;
		this.end = end;
	}


	/**
	 * Adds the given interval to this interval. This method creates a new Interval without
	 * modifying this one.
	 * @param i
	 * @return
	 */
	public Interval add(Interval interval)
	{
		return new Interval(start + interval.start, end + interval.end);
	}


	/**
	 * Adds the given interval to this interval. This method modifies this Interval.
	 * @param i
	 * @return
	 */
	public void addLocal(Interval interval)
	{
		start += interval.start;
		end += interval.end;
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("[").append(start).append(",").append(end).append("]");
		return ret.toString();
	}


	/**
	 * Tests if this Interval contains the given value.
	 * @param i
	 * @return
	 */
	public boolean contains(int i)
	{
		return i >= start && i <= end;
	}
}
