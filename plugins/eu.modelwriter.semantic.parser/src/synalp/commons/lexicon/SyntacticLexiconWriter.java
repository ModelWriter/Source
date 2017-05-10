package synalp.commons.lexicon;

import java.io.*;

import synalp.commons.grammar.NodeType;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.commons.utils.Utils;


/**
 * Writes a SyntacticLexicon in XML format. We cannot reuse the methods from GrammarWriter since the
 * fs format is slightly different (only value instead of varname). We need to see what we should do
 * for coanchors (now stored as regular equations).
 * @author Alexandre Denis
 */
public class SyntacticLexiconWriter
{
	/**
	 * The SHIFT is the String appended at each level.
	 */
	public static String SHIFT = "  ";


	/**
	 * Writes the given SyntacticLexicon to the given File.
	 * @param lexicon
	 * @param file
	 */
	public static void write(SyntacticLexicon lexicon, File file)
	{
		file.delete();
		try
		{
			FileWriter fileWriter = new FileWriter(file);
			write(lexicon, fileWriter);
			fileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Writes the given SyntacticLexicon using the given Writer.
	 * @param lexicon
	 * @param w
	 * @throws IOException
	 */
	public static void write(SyntacticLexicon lexicon, Writer w) throws IOException
	{
		w.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		w.append("<lexicon>\n");
		writeLexiconContent(lexicon, w, SHIFT);
		w.append("</lexicon>");
	}


	/**
	 * Writes the given SyntacticLexicon content using the given Writer.
	 * @param lexicon
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeLexiconContent(SyntacticLexicon lexicon, Writer w, String s) throws IOException
	{
		w.append(s).append("<lemmas>\n");
		for(SyntacticLexiconEntry entry : lexicon)
			writeLexiconEntry(entry, w, s + SHIFT);
		w.append(s).append("</lemmas>\n");
	}


	/**
	 * Writes the given SyntacticLexiconEntry using the given Writer.
	 * @param entry
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeLexiconEntry(SyntacticLexiconEntry entry, Writer w, String s) throws IOException
	{
		w.append(s).append("<lemma family=\"").append(Utils.print(entry.getFamilies(), ",")).append("\" ");
		w.append("name=\"").append(entry.getLemma().toString()).append("\">\n");
		writeLexiconEntryContent(entry, w, s + SHIFT);
		w.append(s).append("</lemma>\n");
	}


	/**
	 * Writes the given SyntacticLexiconEntry content using the given Writer.
	 * @param entry
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeLexiconEntryContent(SyntacticLexiconEntry entry, Writer w, String s) throws IOException
	{
		writeEquations(entry.getEquations(), w, s);
		// writeCoanchors
		writeInterface(entry.getInterface(), w, s);
		writeFilter(entry.getFilter(), w, s);
		writeSemantics(entry.getSemantics(), w, s);
	}


	/**
	 * Writes the given Equations using the given Writer.
	 * @param equations
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeEquations(Equations equations, Writer w, String s) throws IOException
	{
		w.append(s).append("<equations>\n");
		for(Equation equation : equations)
			writeEquation(equation, w, s + SHIFT);
		w.append(s).append("</equations>\n");
	}


	/**
	 * Writes the given Equation using the given Writer.
	 * @param equation
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeEquation(Equation equation, Writer w, String s) throws IOException
	{
		w.append(s).append("<equation type=\"").append(equation.getType().getValue()).append("\" ");
		
		// this is meant to have compatibility with lexConverter
		String nodeId = equation.getNodeId();
		if (nodeId.equals("anc"))
			nodeId = NodeType.ANCHOR.getValue();
		
		w.append("node_id=\"").append(nodeId).append("\">\n");
		writeFeatureStructure(equation.getFeatureStructure(), w, s + SHIFT);
		w.append(s).append("</equation>\n");
	}


	/**
	 * Writes the given FeatureStructure using the given Writer.
	 * @param fs
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeInterface(FeatureStructure fs, Writer w, String s) throws IOException
	{
		w.append(s).append("<interface>\n");
		writeFeatureStructure(fs, w, s + SHIFT);
		w.append(s).append("</interface>\n");
	}


	/**
	 * Writes the given Filter using the given Writer.
	 * @param filter
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeFilter(Filter filter, Writer w, String s) throws IOException
	{
		w.append(s).append("<filter>\n");
		writeFeatureStructure(filter.getFeatureStructure(), w, s + SHIFT);
		w.append(s).append("</filter>\n");

	}


	/**
	 * Writes the given Semantics using the given Writer.
	 * @param semantics
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeSemantics(Semantics semantics, Writer w, String s) throws IOException
	{
		w.append(s).append("<semantics>\n");
		for(Literal literal : semantics)
			writeLiteral(literal, w, s + SHIFT);
		w.append(s).append("</semantics>\n");
	}


	/**
	 * Writes the given Literal using the given Writer.
	 * @param literal
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeLiteral(Literal literal, Writer w, String s) throws IOException
	{
		w.append(s).append("<literal label=\"").append(literal.getLabel().toString()).append("\" ");
		w.append("predicate=\"").append(literal.getPredicate().toString()).append("\">\n");
		writeLiteralArguments(literal, w, s + SHIFT);
		w.append(s).append("</literal>\n");
	}


	/**
	 * Writes the given Literal arguments using the given Writer.
	 * @param literal
	 * @param w
	 * @param s
	 * @throws IOException
	 */
	private static void writeLiteralArguments(Literal literal, Writer w, String s) throws IOException
	{
		w.append(s).append("<args>\n");
		for(FeatureValue arg : literal.getArguments())
			writeValue(arg, w, s + SHIFT);
		w.append(s).append("</args>\n");
	}


///// fs

	/**
	 * Writes the given FeatureValue using the given Writer.
	 * @param value
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeValue(FeatureValue value, Writer w, String s) throws IOException
	{
		switch (value.getType())
		{
			case CONSTANT:
				writeConstant((FeatureConstant) value, w, s);
				break;

			case VARIABLE:
				writeVariable((FeatureVariable) value, w, s);
				break;

			case FEATURE_STRUCTURE:
				writeFeatureStructure((FeatureStructure) value, w, s);
				break;
		}

	}


	/**
	 * Writes the given FeatureStructure using the given Writer.
	 * @param fs
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeFeatureStructure(FeatureStructure fs, Writer w, String s) throws IOException
	{
		w.append(s).append("<fs>\n");
		for(Feature feat : fs.getFeatures())
			writeFeature(feat, w, s + SHIFT);
		w.append(s).append("</fs>\n");
	}


	/**
	 * Writes the given Feature using the given Writer.
	 * @param feat
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeFeature(Feature feat, Writer w, String s) throws IOException
	{
		w.append(s).append("<f name=\"").append(feat.getName()).append("\">\n");
		writeValue(feat.getValue(), w, s + SHIFT);
		w.append(s).append("</f>\n");
	}


	/**
	 * Writes the given FeatureVariable using the given Writer.
	 * @param variable
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeVariable(FeatureVariable variable, Writer w, String s) throws IOException
	{
		w.append(s).append("<sym value=\"").append(variable.getName()).append("\"/>\n");
	}


	/**
	 * Writes the given FeatureConstant using the given Writer.
	 * @param constant
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeConstant(FeatureConstant constant, Writer w, String s) throws IOException
	{
		if (constant.getValues().size() > 1)
		{
			w.append(s).append("<vAlt>\n");
			for(String value : constant.getValues())
				w.append(s).append(SHIFT).append("<sym value=\"").append(value).append("\"/>\n");
			w.append(s).append("</vAlt>\n");
		}
		else w.append(s).append("<sym value=\"").append(constant.toString()).append("\"/>\n");
	}
}
