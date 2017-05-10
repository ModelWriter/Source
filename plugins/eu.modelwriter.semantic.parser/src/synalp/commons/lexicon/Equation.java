package synalp.commons.lexicon;

import synalp.commons.unification.*;

/**
 * Equation is the class that represent an equation in the syntactic lexicon input file (.glex, .lex
 * or .xml).
 * @author Alexandre Denis
 * @author Céline Moro
 * @version %I%, %G%
 * @since 1.0
 */
public class Equation
{
	/**
	 * The id of the node referred to by this equation (maybe use an enum, because it does not look
	 * like id but rather node type, not sure).
	 */
	private String nodeId;
	/**
	 * The type represents whether the equation refers to the TOP or the BOTTOM feature structure of
	 * the node.
	 */
	private FeatureStructureType type;
	private FeatureStructure featureStructure;
	private boolean isCoanchorEquation;


	/**
	 * Creates a new Equation with given nodeId and type of feature structure. By default it is not
	 * a co-anchor equation.
	 * @param nodeId
	 * @param type
	 */
	public Equation(String nodeId, FeatureStructureType type)
	{
		this.nodeId = nodeId;
		this.type = type;
		this.isCoanchorEquation = false;
	}


	/**
	 * Creates a new Equation with given nodeId, type of feature structure and co-anchor flag.
	 * @param nodeId
	 * @param type
	 * @param isCoanchorEquation if true the equation refers to a coanchor
	 */
	public Equation(String nodeId, FeatureStructureType type, boolean isCoanchorEquation)
	{
		this.nodeId = nodeId;
		this.type = type;
		this.isCoanchorEquation = isCoanchorEquation;
	}


	/**
	 * Creates a new Equation with given nodeId, type of feature structure and feature structure. By
	 * default it is not a co-anchor equation.
	 * @param nodeId
	 * @param type
	 * @param fs
	 */
	public Equation(String nodeId, FeatureStructureType type, FeatureStructure fs)
	{
		this.nodeId = nodeId;
		this.type = type;
		this.featureStructure = fs;
		this.isCoanchorEquation = false;
	}


	/**
	 * Creates a new Equation with given nodeId, type of feature structure, feature structure and
	 * co-anchor equation flag.
	 * @param nodeId
	 * @param type
	 * @param fs
	 * @param isCoanchorEquation if true the equation refers to a coanchor
	 */
	public Equation(String nodeId, FeatureStructureType type, FeatureStructure fs, boolean isCoanchorEquation)
	{
		this.nodeId = nodeId;
		this.type = type;
		this.featureStructure = fs;
		this.isCoanchorEquation = isCoanchorEquation;
	}


	/**
	 * Deep copy constructor.
	 * @param equation
	 */
	public Equation(Equation equation)
	{
		this.nodeId = equation.getNodeId();
		this.type = equation.getType();
		this.featureStructure = new FeatureStructure(equation.getFeatureStructure());
		this.isCoanchorEquation = equation.isCoanchorEquation();
	}


	/**
	 * @return the nodeId
	 */
	public String getNodeId()
	{
		return nodeId;
	}


	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}


	/**
	 * @return the type
	 */
	public FeatureStructureType getType()
	{
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(FeatureStructureType type)
	{
		this.type = type;
	}


	/**
	 * @return the featureStructure
	 */
	public FeatureStructure getFeatureStructure()
	{
		return featureStructure;
	}


	/**
	 * @param featureStructure the featureStructure to set
	 */
	public void setFeatureStructure(FeatureStructure featureStructure)
	{
		this.featureStructure = featureStructure;
	}


	/**
	 * @return the isCoanchorEquation
	 */
	public boolean isCoanchorEquation()
	{
		return isCoanchorEquation;
	}


	/**
	 * @param isCoanchorEquation the isCoanchorEquation to set
	 */
	public void setCoanchorEquation(boolean isCoanchorEquation)
	{
		this.isCoanchorEquation = isCoanchorEquation;
	}


	@Override
	public String toString()
	{
		return nodeId + "." + type.toString().toLowerCase() + "=" + featureStructure;
	}


	/**
	 * Returns a String representation of this Equation in context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		return nodeId + "." + type.toString().toLowerCase() + "=" + featureStructure.toString(context);
	}
}
