package synalp.generation.jeni.filtering;

import java.util.HashMap;

/**
 * PolarityKeys associates an integer to polarity keys.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class PolarityKeys extends HashMap<PolarityKey, Interval>
{
	/**
	 * Increments all polarity keys count of these PolarityKeys with the given keys coun. This
	 * method modifies this PolarityKeys instance.
	 * @param otherKeys
	 */
	public void incrementAll(PolarityKeys otherKeys)
	{
		for(PolarityKey key : otherKeys.keySet())
			if (!containsKey(key))
				put(key, otherKeys.get(key));
			else put(key, get(key).add(otherKeys.get(key)));
	}


	/**
	 * Tests if there exists a match with given root polarity. It tries to find a non-zero polarity
	 * that is more specific than given root polarity such that its value added to the given
	 * interval would be zero-polarity.
	 * @param root
	 * @param interval
	 * @return wether a matching polarity has been found
	 */
	public boolean matches(PolarityKey root, Interval interval)
	{
		for(PolarityKey key : keySet())
			if (!get(key).contains(0))
			{
				if (!key.isMoreSpecific(root))
					return false; // immediate failure

				if (get(key).add(interval).contains(0))
					return true;
			}

		return false;
	}


	/**
	 * Tests if each PolarityKey is 0.
	 * @return false if one of the PolarityKey is not 0
	 */
	public boolean isZero()
	{
		for(PolarityKey key : keySet())
			if (!get(key).contains(0))
				return false;
		return true;
	}
}
