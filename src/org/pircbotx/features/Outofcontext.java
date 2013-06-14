//TODO: Multiline support

package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.Colors;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.PastebinUtils;

public class Outofcontext extends Feature
{
	private String settingsPath = "featuresettings/ooclog.txt";
	private String sourceChannel;

	public Outofcontext(HeufyBot bot, String name) 
	{
		super(bot, name);
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "ooc";
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "ooc, " + bot.getCommandPrefix() + "ooc add <quote>, " + bot.getCommandPrefix() + "ooc search <quote/nick> | Grab the OoC log, add an entry to it or search the log by providing a nickname or sentence.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		this.sourceChannel = source;
		if(metadata.equals(""))
		{
			this.bot.sendMessage(source, "[OutOfContext] Attempting to post log... Please wait.");
			Thread thread = new Thread(new Runnable()
			{
				public void run()
				{
					String result = PastebinUtils.post(settingsPath, "HeufyBot OutOfContext Log");
					if(result != null)
					{
						bot.sendMessage(sourceChannel, "[OutOfContext] OoC Log posted: " + result  + " (Link expires in 10 minutes)");
					}
					else
					{
						bot.sendMessage(sourceChannel, "[OutOfContext] Error: OoC Log could not be posted");
					}
				}
			});
			thread.start();
		}
		else
		{
			if(metadata.substring(1).toLowerCase().startsWith("add "))
			{
				String newQuote = Colors.removeFormattingAndColors(metadata.substring(5));
				
				DateFormat dateFormat = new SimpleDateFormat("[yyyy/MM/dd] [HH:mm]");
			    Date date = new Date();
			    String dateString = dateFormat.format(date);
			    
			    String toQuote = "";
			    
			    if(newQuote.matches("^<.*>.*") || newQuote.matches("^\\* .*") || newQuote.matches("^\\[.*\\] <.*>.*") || newQuote.matches("^\\[.*\\] \\* .*"))
			    {
				    if(newQuote.matches("^\\[.*\\] <.*>.*") || newQuote.matches("^\\[.*\\] \\* .*"))
				    {
				    	if(newQuote.matches("^\\[.*\\] \\* .*"))
				    	{
				    		toQuote = newQuote.substring(newQuote.indexOf("* ") + 2).split(" ")[0];
				    		newQuote = dateString + " " + newQuote.substring(newQuote.indexOf("*"));
				    	}
				    	else
				    	{
				    		toQuote = newQuote.substring(newQuote.indexOf("<") + 1, newQuote.indexOf(">"));
				    		newQuote = dateString + " " + newQuote.substring(newQuote.indexOf("<"));
				    	}
				    	
				    }
				    else if(newQuote.matches("^<.*>.*") || newQuote.matches("^\\* .*"))
				    {
				    	if(newQuote.matches("^\\* .*"))
				    	{
				    		toQuote = newQuote.substring(newQuote.indexOf("* ") + 2).split(" ")[0];
				    	}
				    	else
				    	{
				    		toQuote = newQuote.substring(newQuote.indexOf("<") + 1, newQuote.indexOf(">"));
				    	}
				    	newQuote = dateString + " " + newQuote;
				    }
			    
				    if(toQuote.matches("^(\\+|%|@|&|~).*"))
				    {
				    	newQuote = newQuote.replace(toQuote, toQuote.substring(1));
				    }
				    
				    FileUtils.writeFileAppend(settingsPath, newQuote + "\n");
					bot.sendMessage(source, "[OutOfContext] Quote was added to the log!");
			    }
			    else
			    {
			    	bot.sendMessage(source, "[OutOfContext] No nickname was found in this quote.");
			    }
			}
			else if(metadata.substring(1).toLowerCase().startsWith("search "))
			{
				String search = metadata.substring(8);
				
				String quoteFile = FileUtils.readFile(settingsPath);
				String[] quotes = quoteFile.split("\n");
				ArrayList<String> matches = new ArrayList<String>();
				
				Pattern pattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
				String searchType = "";
				
				if(search.contains(" "))
				{
					searchType = "Word Combination";
					for(int i = 0; i < quotes.length; i++)
					{
						if(pattern.matcher(quotes[i].substring(21)).matches())
						{
							matches.add(quotes[i]);
						}
					}
				}
				else
				{
					searchType = "Nickname";
					for(int i = 0; i < quotes.length; i++)
					{
						if(quotes[i].substring(21).matches("^<.*>.*"))
						{
							if(pattern.matcher(quotes[i].substring(quotes[i].indexOf("<") + 1, quotes[i].indexOf(">"))).matches())
							{
								matches.add(quotes[i]);
							}
						}
						else if(quotes[i].substring(21).matches("^\\* .*"))
						{
							if(pattern.matcher(quotes[i].substring(quotes[i].indexOf("* ") + 2).split(" ")[0]).matches())
							{
								matches.add(quotes[i]);
							}
						}
					}
				}
				
				if(matches.size() == 0)
				{
					bot.sendMessage(source, "[OutOfContext] Search Type: " + searchType + " | No matches for '" + search + "' found");
				}
				else
				{
					bot.sendMessage(source, "[OutOfContext] Search Type: " + searchType + " | Total matched quotes: " + matches.size());
					int quoteID = (int) (Math.random() * matches.size());
					bot.sendMessage(source, "[OutOfContext] " + matches.get(quoteID));
				}
			}
			else if(metadata.substring(1).equalsIgnoreCase("help"))
			{
				bot.sendMessage(source, "[Help: Outofcontext] " + getHelp());
			}
			else
			{
				bot.sendMessage(source, "[OutOfContext] Invalid operation. Please try again.");
			}
		}
	}

	@Override
	public void connectTrigger()
	{
		FileUtils.touchFile(settingsPath);
	}
}
