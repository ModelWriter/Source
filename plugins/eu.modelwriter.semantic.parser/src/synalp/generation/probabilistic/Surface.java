package synalp.generation.probabilistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synalp.commons.output.MorphRealization;
import synalp.commons.output.SyntacticRealization;
import synalp.commons.utils.Utils;
import synalp.generation.jeni.JeniRealization;

public class Surface
{
	private String sentence;
	
	List<JeniRealization> realizations = new ArrayList<JeniRealization>();


	public Surface(JeniRealization real)
	{
		setSentence(getSurface(real));
		this.realizations.add(real);
	}

	public void addRealization(JeniRealization real) {
		realizations.add(real);
	}

	public static List<Surface> groupSurfaces(List<JeniRealization> results)
	{
		
		List<Surface> ret = new ArrayList<Surface>();
		if (!results.isEmpty())
		{

			Surface first = new Surface(results.get(0));
			//System.err.println("FIRST_SENTENCE#"+first.getSentence()+"#");
			ret.add(first);

			for(int i = 1; i < results.size(); ++i)
			{
				JeniRealization real = results.get(i);
				Surface surface = new Surface(real);
				boolean addedBefore = false;
				for(Surface s : ret)
				{
					if (s.getSentence().equals(surface.getSentence()))
					{
					//	System.err.println("OMITTING_ADDED_SENTENCE#"+s.getSentence()+"#");
						s.addRealization(real);
						addedBefore = true;
						break;
					}
					
				}
				
				if (!addedBefore)
				{
					//System.err.println("NEW_SENTENCE#"+surface.getSentence()+"#");
					ret.add(surface);
					
				}
			}
		}

		return ret;
	}


	private static String getSurface(SyntacticRealization real)
	{
		return Utils.print(real.getLemmas(), " ");
	}


	/**
	 * @return the sentence
	 */
	public String getSentence()
	{
		return sentence;
	}


	/**
	 * @param sentence the sentence to set
	 */
	private void setSentence(String sentence)
	{
		this.sentence = sentence;
	}


	/**
	 * @return the times
	 */
	public int getTimes()
	{
		return realizations.size();
	}



	public float getProbability()
	{
		// TODO Auto-generated method stub
		return realizations.get(0).getProbability();
	}

}
