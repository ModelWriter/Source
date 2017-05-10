package synalp.commons.grammar;

import java.io.*;

import synalp.commons.semantics.*;
import synalp.commons.unification.*;


/**
 * A GrammarWriter writes a Grammar in XML. Two aspects are not handled: the coref mechanism and the
 * literal negated=no mechanism.
 * @author Alexandre Denis
 */
public class GrammarWriter
{
	/**
	 * The SHIFT is the String appended at each level.
	 */
	public static String SHIFT = "  ";


	/**
	 * Writes the given Grammar to the given File.
	 * @param grammar
	 * @param file
	 */
	public static void write(Grammar grammar, File file)
	{
		file.delete();
		try
		{
			FileWriter fileWriter = new FileWriter(file);
			write(grammar, fileWriter);
			fileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Writes the given Grammar using the given Writer.
	 * @param grammar
	 * @param w
	 * @throws IOException
	 */
	public static void write(Grammar grammar, Writer w) throws IOException
	{
		w.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		w.append("<grammar>\n");
		for(GrammarEntry entry : grammar.values())
			write(entry, w, SHIFT);
		w.append("</grammar>");
	}


	/**
	 * Writes the given GrammarEntry using the given Writer.
	 * @param entry
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void write(GrammarEntry entry, Writer w, String s) throws IOException
	{
		w.append(s).append("<entry name=\"").append(entry.getName()).append("\">\n");
		writeEntryContent(entry, w, s + SHIFT);
		w.append(s).append("</entry>\n");
	}


	/**
	 * Writes the given GrammarEntry content using the given Writer.
	 * @param entry
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeEntryContent(GrammarEntry entry, Writer w, String s) throws IOException
	{
		writeFamily(entry, w, s);
		writeTrace(entry.getTrace(), w, s);
		writeTree(entry.getTree(), w, s);
		writeSemantics(entry.getSemantics(), w, s);
		writeInterface(entry.getInterface(), w, s);
	}


	/**
	 * Writes the given GrammarEntry family using the given Writer.
	 * @param entry
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeFamily(GrammarEntry entry, Writer w, String s) throws IOException
	{
		w.append(s).append("<family>").append(entry.getFamily()).append("</family>\n");
	}


	/**
	 * Writes the given Trace using the given Writer.
	 * @param trace
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeTrace(Trace trace, Writer w, String s) throws IOException
	{
		w.append(s).append("<trace>\n");
		for(String traceElement : trace)
			w.append(s).append(s).append("<class>").append(traceElement).append("</class>\n");
		w.append(s).append("</trace>\n");
	}


	/**
	 * Writes the given FeatureStructure using the given Writer.
	 * @param fs
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeInterface(FeatureStructure fs, Writer w, String s) throws IOException
	{
		w.append(s).append("<interface>\n");
		writeFeatureStructure(fs, w, s + SHIFT);
		w.append(s).append("</interface>\n");
	}


	/**
	 * Writes the given Tree using the given Writer.
	 * @param tree
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeTree(Tree tree, Writer w, String s) throws IOException
	{
		w.append(s).append("<tree id=\"").append(tree.getId()).append("\">\n");
		writeNode(tree.getRoot(), w, s + SHIFT);
		w.append(s).append("</tree>\n");
	}


	/**
	 * Writes the given Node using the given Writer.
	 * @param node
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeNode(Node node, Writer w, String s) throws IOException
	{
		NodeType type = node.getType();
		if (node.isNoAdjunction())
			type = NodeType.NADJ;

		w.append(s).append("<node type=\"").append(type.getValue()).append("\"");
		if (node.getId().equals(""))
			w.append(">\n");
		else w.append(" name=\"").append(node.getId()).append("\">\n");
		writeNarg(node, w, s + SHIFT);
		for(Node child : node.getChildren())
			writeNode(child, w, s + SHIFT);
		w.append(s).append("</node>\n");
	}


	/**
	 * Writes the given Node fs using the given Writer.
	 * @param node
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeNarg(Node node, Writer w, String s) throws IOException
	{
		w.append(s).append("<narg>\n");
		FeatureStructure nargFs = new FeatureStructure();
		nargFs.addConstantFeature("cat", node.getCategory());
		if (!node.getFsTop().isEmpty())
			nargFs.addComplexFeature("top", node.getFsTop());
		if (!node.getFsBot().isEmpty())
			nargFs.addComplexFeature("bot", node.getFsBot());
		writeFeatureStructure(nargFs, w, s + SHIFT);
		w.append(s).append("</narg>\n");
	}


	/**
	 * Writes the given Semantics using the given Writer.
	 * @param semantics
	 * @param w
	 * @param s a shift value
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
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeLiteral(Literal literal, Writer w, String s) throws IOException
	{
		w.append(s).append("<literal negated=\"no\">\n");
		writeLabel(literal, w, s + SHIFT);
		writePredicate(literal, w, s + SHIFT);
		for(FeatureValue arg : literal.getArguments())
			writeArgument(arg, w, s + SHIFT);
		w.append(s).append("</literal>\n");
	}


	/**
	 * Writes the given Literal predicate using the given Writer.
	 * @param literal
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writePredicate(Literal literal, Writer w, String s) throws IOException
	{
		w.append(s).append("<predicate>\n");
		writeValue(literal.getPredicate(), w, s + SHIFT);
		w.append(s).append("</predicate>\n");
	}


	/**
	 * Writes the given Literal argument using the given Writer.
	 * @param arg
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeArgument(FeatureValue arg, Writer w, String s) throws IOException
	{
		w.append(s).append("<arg>\n");
		writeValue(arg, w, s + SHIFT);
		w.append(s).append("</arg>\n");
	}


	/**
	 * Writes the given Literal label using the given Writer.
	 * @param literal
	 * @param w
	 * @param s a shift value
	 * @throws IOException
	 */
	private static void writeLabel(Literal literal, Writer w, String s) throws IOException
	{
		w.append(s).append("<label>\n");
		writeValue(literal.getLabel(), w, s + SHIFT);
		w.append(s).append("</label>\n");
	}


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
		w.append(s).append("<sym varname=\"").append(variable.getName()).append("\"/>\n");
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
