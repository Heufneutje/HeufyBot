package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.Colors;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.PastebinUtils;

public class Outofcontext extends Feature
{
	private String sourceChannel;

	public Outofcontext(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/ooclog.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "ooc";
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "ooc, " + bot.getCommandPrefix() + "ooc add <quote>, " + bot.getCommandPrefix() + "ooc search <quote>, " + bot.getCommandPrefix() + "ooc searchnick <nick>, " + bot.getCommandPrefix() + "ooc random, " + bot.getCommandPrefix() + "ooc remove <quote> | Grab the OoC log, add an entry to it, search the log by providing a nickname or sentence or remove an entry.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		this.sourceChannel = source;
		if(metadata.equals(""))
		{
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
					bot.sendMessage(source, "[OutOfContext] Quote '" + newQuote + "' was added to the log!");
			    }
			    else
			    {
			    	bot.sendMessage(source, "[OutOfContext] No nickname was found in this quote.");
			    }
			}
			else if(metadata.substring(1).toLowerCase().startsWith("searchnick "))
			{
				String search = metadata.substring(12).replaceAll(" ", "");
				search(search, false);
			}
			else if(metadata.substring(1).toLowerCase().startsWith("search "))
			{
				String search = metadata.substring(8);
				search(search, true);
			}
			else if(metadata.substring(1).equalsIgnoreCase("help"))
			{
				bot.sendMessage(source, "[Help: Outofcontext] " + getHelp());
			}
			else if(metadata.substring(1).equalsIgnoreCase("random"))
			{
				search(".*", false);
			}
			else if(metadata.substring(1).startsWith("remove "))
			{
				String search = metadata.substring(8);
				
				String quoteFile = FileUtils.readFile(settingsPath);
				String[] quotes = quoteFile.split("\n");
				ArrayList<String> quoteList = new ArrayList<String>();
				ArrayList<String> matches = new ArrayList<String>();
				Pattern pattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
				
				if(quotes[0].length() < 21)
				{
					bot.sendMessage(sourceChannel, "[OutOfContext] No quotes in the log.");
				}
				else
				{
					for(int i = 0; i < quotes.length; i++)
					{
						quoteList.add(quotes[i]);
						if(pattern.matcher(quotes[i].substring(21)).matches())
						{
							matches.add(quotes[i]);
						}
					}
					if(matches.size() == 0)
					{
						bot.sendMessage(sourceChannel, "[OutOfContext] No matches for '" + search + "' found");
					}
					else if(matches.size() > 1)
					{
						bot.sendMessage(sourceChannel, "[OutOfContext] Unable to remove quote. Multiple matches were found | Total matched quotes: " + matches.size());
					}
					else
					{
						for(Iterator<String> iter = quoteList.iterator(); iter.hasNext();)
			  	  		{
			  	  			String quote = iter.next();
			  	  			if(quote.equalsIgnoreCase(matches.get(0)))
			  	  			{
			  	  				iter.remove();
			  	  			}
			  	  		}
						FileUtils.deleteFile(settingsPath);
		  	  			FileUtils.touchFile(settingsPath);
			  	  		for(String quote : quoteList)
			  	  		{
			  	  			FileUtils.writeFileAppend(settingsPath, quote + "\n");
			  	  		}
			  	  		bot.sendMessage(source, "[OutOfContext] Quote '" + matches.get(0) + "' was removed from the log!");
					}
				}
			}
			else
			{
				bot.sendMessage(source, "[OutOfContext] Invalid operation. Please try again.");
			}
		}
	}
	
	public void search(String searchString, boolean searchInQuotes)
	{
		String quoteFile = FileUtils.readFile(settingsPath);
		String[] quotes = quoteFile.split("\n");
		ArrayList<String> matches = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(".*" + searchString + ".*", Pattern.CASE_INSENSITIVE);
		String searchType = "";
		
		if(quotes[0].length() < 21)
		{
			bot.sendMessage(sourceChannel, "[OutOfContext] No quotes in the log.");
		}
		else
		{
			if(searchInQuotes)
			{
				searchType = "Quote";
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
				bot.sendMessage(sourceChannel, "[OutOfContext] Search Type: " + searchType + " | No matches for '" + searchString + "' found");
			}
			else
			{
				bot.sendMessage(sourceChannel, "[OutOfContext] Search Type: " + searchType + " | Total matched quotes: " + matches.size());
				int quoteID = (int) (Math.random() * matches.size());
				bot.sendMessage(sourceChannel, "[OutOfContext] " + matches.get(quoteID));
			}
		}
	}

	@Override
	public void onLoad()
	{
		FileUtils.touchFile(settingsPath);
	}

	@Override
	public void onUnload() 
	{
	}
}
