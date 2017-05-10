package synalp.generation.jeni.selection.patterns.templates;

/**
 * A CoselectionTemplate is a template that is not part of the anchor group (the group of patterns
 * that have the same anchors) but is parallel to it. Unlike normal selection templates, the
 * coselections are not unified with each other. It is typically used to select and anchor
 * auxiliaries.
 * @author Alexandre Denis
 */
public class CoselectionTemplate extends SelectionTemplate
{
	private boolean optional = false; // whether this template is mandatory or optional


	/**
	 * Tests whether this SelectionTemplate is optional.
	 * @return true if it is optional
	 */
	public boolean isOptional()
	{
		return optional;
	}


	/**
	 * Sets whether this SelectionTemplate is optional.
	 * @param optional
	 */
	public void setOptional(boolean optional)
	{
		this.optional = optional;
	}
}
