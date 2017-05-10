package synalp.generation.jeni.semantics.rules;

import java.util.*;

import synalp.commons.semantics.*;
import synalp.commons.unification.*;


/**
 * A RewritingRule rewrites all arguments matching source by the target for a given literal.
 * @author Alexandre Denis
 */
public class RewritingRule implements Rule
{
	private String source;
	private String target;
	private String literal;


	/**
	 * @param source
	 * @param target
	 * @param literal
	 */
	public RewritingRule(String source, String target, String literal)
	{
		this.source = source;
		this.target = target;
		this.literal = literal;
	}


	@Override
	public void apply(Semantics input)
	{
		for(DefaultLiteral inputLiteral : input)
			if (inputLiteral.getPredicate().toString().equals(literal))
			{
				List<FeatureValue> args = new ArrayList<FeatureValue>();
				for(FeatureValue arg : inputLiteral.getArguments())
					if (arg.getType() == FeatureValueType.CONSTANT)
					{
						FeatureConstant constant = (FeatureConstant) arg;
						Set<String> values = new HashSet<String>();
						for(String value : constant.getValues())
							if (value.equals(source))
								values.add(target);
							else values.add(value);
						args.add(new FeatureConstant(values));
					}
					else args.add(arg);
				inputLiteral.setArguments(args);
			}
	}
	
	@Override
	public String toString()
	{
		return "rewrite '"+source+"' by '"+target+"' in literal '"+literal+"'";
	}
}
