package synalp.parsing.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author bikash
 * Note : I don't want this class to have any instances; all methods are going to be static. In this regard, it makes sense
 * to make a static class BUT a/c to http://stackoverflow.com/questions/7486012/static-classes-in-java :
 * Java has static nested classes but it sounds like you're looking for a top-level static class.
 *  Java has no way of making a top-level class static but you can simulate a static class like this: 
 *  Declare your class final
 *  Make the constructor private
 *  Make all the members and functions of the class static
 *  Note that the compiler will not prevent you from declaring an instance (non-static) member. 
 *  The issue will only show up if you attempt to call the instance member
 */
public final class JavaScriptFunctions {

	private static Map<String,String> reservedCharMappings = new LinkedHashMap<String,String>(); // the order of substitutions is important.
	private static Map<String,String> reservedKeyWordMappings = new HashMap<String,String>();
	
	static {
		reservedCharMappings.put("\\", "\\\\"); // Replace \ by \\
		reservedCharMappings.put("\"", "\\\""); // Replace " by \"
		/*
		 * The first statement already captures the following plus the only \ character
		reservedCharMappings.put("\\n", "\\\\n"); // Replace \n by \\n
		reservedCharMappings.put("\\t", "\\\\t"); // Replace \t by \\t
		reservedCharMappings.put("\\r", "\\\\r"); // Replace \r by \\r
		reservedCharMappings.put("\\b", "\\\\b"); // Replace \t by \\t
		reservedCharMappings.put("\\f", "\\\\f"); // Replace \t by \\t
		*/
		reservedCharMappings.put("/", "\\/"); // Replace / by \/
		reservedCharMappings.put("-", "\\-"); // Replace - by \-
		/* There are many more than this including Unicode Characters!! Todo in future
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);
		reservedCharMappings.put(key, value);*/
		
		
		// The escape character to escape keywords is ''
		reservedKeyWordMappings.put(" abstract "," 'abstract' ");
		reservedKeyWordMappings.put(" else "," 'else' ");
		reservedKeyWordMappings.put(" instanceof "," 'instanceof' ");
		reservedKeyWordMappings.put(" super "," 'super' ");
		reservedKeyWordMappings.put(" boolean "," 'boolean' ");
		reservedKeyWordMappings.put(" enum "," 'enum' ");
		reservedKeyWordMappings.put(" int "," 'int' ");
		reservedKeyWordMappings.put(" switch "," 'switch' ");
		reservedKeyWordMappings.put(" break "," 'break' ");
		reservedKeyWordMappings.put(" export "," 'export' ");
		reservedKeyWordMappings.put(" interface "," 'interface' ");
		reservedKeyWordMappings.put(" synchronized "," 'synchronized' ");
		reservedKeyWordMappings.put(" byte "," 'byte' ");
		reservedKeyWordMappings.put(" extends "," 'extends' ");
		reservedKeyWordMappings.put(" let "," 'let' ");
		reservedKeyWordMappings.put(" this "," 'this' ");
		reservedKeyWordMappings.put(" case "," 'case' ");
		reservedKeyWordMappings.put(" false "," 'false '");
		reservedKeyWordMappings.put(" long "," 'long' ");
		reservedKeyWordMappings.put(" throw "," 'throw' ");
		reservedKeyWordMappings.put(" catch "," 'catch' ");
		reservedKeyWordMappings.put(" final "," 'final' ");
		reservedKeyWordMappings.put(" native "," 'native' ");
		reservedKeyWordMappings.put(" throws "," 'throws' ");
		reservedKeyWordMappings.put(" char "," 'char' ");
		reservedKeyWordMappings.put(" finally "," 'finally' ");
		reservedKeyWordMappings.put(" new "," 'new' ");
		reservedKeyWordMappings.put(" transient "," 'transient' ");
		reservedKeyWordMappings.put(" class "," 'class' ");
		reservedKeyWordMappings.put(" float "," 'float' ");
		reservedKeyWordMappings.put(" null "," 'null' ");
		reservedKeyWordMappings.put(" true "," 'true' ");
		reservedKeyWordMappings.put(" const "," 'const' ");
		reservedKeyWordMappings.put(" for "," 'for' ");
		reservedKeyWordMappings.put(" package "," 'package' ");
		reservedKeyWordMappings.put(" try "," 'try' ");
		reservedKeyWordMappings.put(" continue "," 'continue' ");
		reservedKeyWordMappings.put(" function "," 'function' ");
		reservedKeyWordMappings.put(" private "," 'private' ");
		reservedKeyWordMappings.put(" typeof "," 'typeof' ");
		reservedKeyWordMappings.put(" debugger ","' debugger' ");
		reservedKeyWordMappings.put(" goto "," 'goto' ");
		reservedKeyWordMappings.put(" protected "," 'protected' ");
		reservedKeyWordMappings.put(" var "," 'var' ");
		reservedKeyWordMappings.put(" default "," 'default' ");
		reservedKeyWordMappings.put(" if "," 'if' ");
		reservedKeyWordMappings.put(" public "," 'public' ");
		reservedKeyWordMappings.put(" void "," 'void' ");
		reservedKeyWordMappings.put(" delete "," 'delete' ");
		reservedKeyWordMappings.put(" implements "," 'implements' ");
		reservedKeyWordMappings.put(" return "," 'return' ");
		reservedKeyWordMappings.put(" volatile "," 'volatile' ");
		reservedKeyWordMappings.put(" do "," 'do' ");
		reservedKeyWordMappings.put(" import "," 'import' ");
		reservedKeyWordMappings.put(" short "," 'short' ");
		reservedKeyWordMappings.put(" while "," 'while' ");
		reservedKeyWordMappings.put(" double "," 'double' ");
		reservedKeyWordMappings.put(" in "," 'in' ");
		reservedKeyWordMappings.put(" static "," 'static' ");
		reservedKeyWordMappings.put(" with "," 'with' ");
	}
	
	private JavaScriptFunctions() { // private constructor
		// Prevents instantiation by client code as it makes no sense to instantiate a static class
	}
	
	public static String get_escape_JS_symbols (String input) {
		String output = escape_characters(input);
		//output = escape_keywords(output); // Perhaps not necessary for the values in JSON 
		return output;
	}
	
	private static String escape_characters(String input) {
		for (Map.Entry<String, String> entry:reservedCharMappings.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
	
	private static String escape_keywords(String input) {
		for (Map.Entry<String, String> entry:reservedKeyWordMappings.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}

}
