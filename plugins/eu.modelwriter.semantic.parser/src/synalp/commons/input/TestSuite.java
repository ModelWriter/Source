package synalp.commons.input;

import java.util.ArrayList;

/**
 * A TestSuite is a list of tests. Should be moved in a test package.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class TestSuite extends ArrayList<TestSuiteEntry>
{
	/**
	 * Returns the first found entry with the given id.
	 * @param id
	 * @return null if not found
	 */
	public TestSuiteEntry getEntryById(String id)
	{
		for(TestSuiteEntry entry : this)
			if (entry.getId().equals(id))
				return entry;
		return null;
	}

}
