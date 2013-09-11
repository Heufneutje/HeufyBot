package org.pircbotx.utilities;

public class RegexUtils
{
	public static String escapeRegex(String regex)
	{
		return regex.replaceAll("\\*", "\\*").
				replaceAll("\\+", "\\\\+").
				replaceAll("\\?", "\\\\?").
				replaceAll("\\|", "\\\\|").
				replaceAll("\\{", "\\\\{").
				replaceAll("\\[", "\\\\[").
				replaceAll("\\(", "\\\\(").
				replaceAll("\\)", "\\\\)").
				replaceAll("\\^", "\\\\^").
				replaceAll("\\$", "\\\\$").
				replaceAll("\\.", "\\\\.").
				replaceAll("\\#", "\\\\#").
				replaceAll(" ", "");
	}
}