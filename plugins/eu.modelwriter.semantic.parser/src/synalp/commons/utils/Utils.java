package synalp.commons.utils;

import java.io.*;
import java.util.*;

/**
 * General purpose utilities class.
 * @author Alexandre Denis
 */
public class Utils
{

//// File utils

	/**
	 * Deletes the given File or directory recursively.
	 * @param file
	 * @throws IOException
	 */
	public static void delete(File file) throws IOException
	{
		if (file.isDirectory())
		{
			String[] files = file.list();
			if (files.length == 0)
				file.delete();
			else
			{
				for(String temp : files)
					delete(new File(file, temp));

				if (file.list().length == 0)
					file.delete();
			}
		}
		else file.delete();
	}


//// Map Utils	

	/**
	 * @param t
	 * @param map
	 */
	public static <T> void addOne(T t, Map<T, Integer> map)
	{
		addN(1, t, map);
	}


	/**
	 * @param n
	 * @param t
	 * @param map
	 */
	public static <T> void addN(int n, T t, Map<T, Integer> map)
	{
		if (!map.containsKey(t))
			map.put(t, n);
		else map.put(t, map.get(t) + n);
	}


	/**
	 * @param t
	 * @param map
	 */
	public static <T> void addOneLong(T t, Map<T, Long> map)
	{
		addNLong(1, t, map);
	}


	/**
	 * @param n
	 * @param t
	 * @param map
	 */
	public static <T> void addNLong(long n, T t, Map<T, Long> map)
	{
		if (!map.containsKey(t))
			map.put(t, n);
		else map.put(t, map.get(t) + n);
	}


////// String utils

	/**
	 * Repeats the given character n time.
	 * @param c
	 * @param n
	 * @return a String
	 */
	public static String repeat(char c, int n)
	{
		StringBuilder ret = new StringBuilder();
		for(int i = 0; i < n; i++)
			ret.append(c);
		return ret.toString();
	}


	/**
	 * Splits the given String around the comma separator and trim each substring.
	 * @param str
	 * @return an array of trimmed strings
	 */
	public static String[] splitAndTrim(String str)
	{
		return splitAndTrim(str, ",");
	}


	/**
	 * Splits the given String around given separator and trim each substring.
	 * @param str
	 * @param separator
	 * @return an array of trimmed strings
	 */
	public static String[] splitAndTrim(String str, String separator)
	{
		return trim(str.split(separator));
	}


	/**
	 * Trims all strings in array. This method modifies the given array.
	 * @param strings
	 * @return the given array of strings for chaining
	 */
	public static String[] trim(String[] strings)
	{
		for(int i = 0; i < strings.length; i++)
			strings[i] = strings[i].trim();
		return strings;
	}


	/**
	 * Returns a String representation of given elements, separated by comma and surrounded by
	 * square brackets.
	 * @param elements
	 * @return a String
	 */
	public static <T> String print(T[] elements)
	{
		return print(elements, ",", "[", "]");
	}


	/**
	 * Returns a String representation of given elements, with given separator and not surrounded by
	 * any other characters.
	 * @param elements
	 * @param separator
	 * @return a String
	 */
	public static <T> String print(T[] elements, String separator)
	{
		return print(elements, separator, "", "");
	}


	/**
	 * Returns a String representation of given elements, using given separator and surrounding
	 * strings.
	 * @param elements the elements to print, they are printed in the order they were specified
	 * @param separator the separator string
	 * @param begin the prefix string, appended at first
	 * @param end the postfix string, appended at last
	 * @return a String
	 */
	public static <T> String print(T[] elements, String separator, String begin, String end)
	{
		StringBuilder ret = new StringBuilder();
		if (elements.length == 0)
		{
			ret.append(begin).append(end);
			return ret.toString();
		}
		else
		{
			ret.append(begin);
			boolean first = true;
			for(T element : elements)
				if (first)
				{
					ret.append(element);
					first = false;
				}
				else ret.append(separator).append(element);
			ret.append(end);
			return ret.toString();
		}
	}


	/**
	 * Returns a String representation of given collection, separated by comma and surrounded by
	 * square brackets.
	 * @param elements
	 * @return a String
	 */
	public static <T> String print(Collection<T> elements)
	{
		return print(elements, ",", "[", "]");
	}


	/**
	 * Returns a String representation of given collection, with given separator and not surrounded
	 * by any other characters.
	 * @param elements
	 * @param separator
	 * @return a String
	 */
	public static <T> String print(Collection<T> elements, String separator)
	{
		return print(elements, separator, "", "");
	}


	/**
	 * Returns a String representation of given collection, using given separator and surrounding
	 * strings.
	 * @param elements the elements to print, they are printed in the order they were specified
	 * @param separator the separator string
	 * @param begin the prefix string, appended at first
	 * @param end the postfix string, appended at last
	 * @return a String
	 */
	public static <T> String print(Collection<T> elements, String separator, String begin, String end)
	{
		StringBuilder ret = new StringBuilder();
		if (elements.isEmpty())
		{
			ret.append(begin).append(end);
			return ret.toString();
		}
		else
		{
			ret.append(begin);
			boolean first = true;
			for(T element : elements)
				if (first)
				{
					ret.append(element);
					first = false;
				}
				else ret.append(separator).append(element);
			ret.append(end);
			return ret.toString();
		}
	}
}
