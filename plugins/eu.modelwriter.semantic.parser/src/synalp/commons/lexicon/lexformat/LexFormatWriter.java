package synalp.commons.lexicon.lexformat;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import synalp.commons.grammar.NodeType;
import synalp.commons.lexicon.Equation;
import synalp.commons.unification.Feature;


/**
 * <p>
 * A LexFormatWriter writes a LexFormatLexicon into a XMG/SemTAG <b>.lex</b>
 * format file. (Using UTF8 charset encoding).
 * </p>
 * 
 * <p>
 * Example code illustrates how to call the LexFormatWriter:
 * </p>
 * <pre>
 * <code>
 * 	private static void writeSyntacticLexicon(LexFormatLexicon synLexicon)
 *			throws FileNotFoundException, IOException 
 *			{
 *		FileOutputStream out = new FileOutputStream("test.lex");
 *		LexFormatWriter exporter = 
 *				new LexFormatWriter(out);
 *		exporter.go(synLexicon);
 *	}
 * </code>
 * </pre>
 * @author Laura Perez
 *
 */
public class LexFormatWriter 
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(LexFormatWriter.class);
	
	private BufferedWriter out;
	
	/**
	 * Creates a  LexFormatWriter configured to use the given output stream.
	 * 
	 * @param out
	 */
	public LexFormatWriter(OutputStream out) 
	{
		this.out = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF8")));
	}
	
	/**
	 * Writes the given LexFormatLexicon into .lex format file 
	 * within the output stream set at creation time.
	 *  
	 * @param lexicon
	 * @throws IOException
	 */
	public void go(LexFormatLexicon lexicon) throws IOException 
	{
		out.write("include macros.mac\n\n");
		writeEntries(lexicon);
		out.close();
	}

	private void writeEntries(LexFormatLexicon lexicon) throws IOException
	{
		for ( LexFormatEntry lexEntry : lexicon ) 
				writeEntry(lexEntry);		
	}
	
	private void writeEntry(LexFormatEntry lexEntry) throws IOException 
	{
		StringBuilder ret = new StringBuilder();
		ret.append("*ENTRY: ").append(lexEntry.getName()).append("\n");
		ret.append("*CAT: ").append(lexEntry.getCat()).append("\n");
		ret.append("*SEM: ").append(lexEntry.getMacroName());
		ret.append("[");
		boolean first = true;
		for (Feature f : lexEntry.getMacroHeader().getFeatures())
		{
			if (!first)
				ret.append(", ");
			ret.append(f.getName()+"="+f.getValue());
		}
		ret.append("]\n");
		//TODO: field acc !!! ret.append("*ACC: ").append(lexEntry.getAcc()).append("\n");
		ret.append("*ACC: 1\n");
		ret.append("*FAM: ").append(lexEntry.getFamily()).append("\n");
		ret.append("*FILTERS: []\n"); //TODO: writing empty filter
		ret.append("*EX: {}\n"); //TODO: writing empty ex
		ret.append("*EQUATIONS: \n");
		out.write(ret.toString());
		if (lexEntry.getEquations()!=null)
			writeEquations(lexEntry);
		out.write("*COANCHORS: \n");
		if (lexEntry.getEquations()!=null)
			writeCoanchors(lexEntry);
		out.write("\n");
	}
	
	private void writeCoanchors(LexFormatEntry lexEntry)  throws IOException 
	{
		for(Equation eq: lexEntry.getEquations())
			if (eq.isCoanchorEquation())
				writeCoanchor(eq);
	}
	

	private void writeEquations(LexFormatEntry lexEntry) throws IOException 
	{
		for(Equation eq: lexEntry.getEquations())
			if (!eq.isCoanchorEquation())
				writeEquation(eq);
	}

	private void writeCoanchor(Equation eq)  throws IOException 
	{
		if (eq!=null)
			if (eq.getFeatureStructure()!=null)
				if (!eq.getFeatureStructure().getFeatures().isEmpty())
					{
						Feature lem = eq.getFeatureStructure().getFeature(LexFormatEntry.COANCHOR_LEMMA_FEAT);
						Feature cat = eq.getFeatureStructure().getFeature(LexFormatEntry.COANCHOR_CAT_FEAT);
						if (lem!=null && cat !=null)
							out.write(eq.getNodeId() + " -> " + lem.getValue() + "/" + cat.getValue() + "\n");
						else
							logger.warn("In a COANCHORS equation we do not find expected lemma and cat elements of nodeId -> lemma/cat");
					}

		
	}

	private void writeEquation(Equation eq) throws IOException 
	{
		if (eq!=null)
			if (eq.getFeatureStructure()!=null)
				if (!eq.getFeatureStructure().getFeatures().isEmpty())
					for (Feature feat : eq.getFeatureStructure().getFeatures())
					{
						// this is for compatibility reason
						String nodeId = eq.getNodeId();
						if (nodeId.equals(NodeType.ANCHOR.getValue()))
							nodeId = "anc";
						
						out.write(nodeId + " -> " + feat.getName() + " = " +
							feat.getValue() + "\n");
					}
	}
	
}
