package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.LoggingUtils;
import org.pircbotx.utilities.URLUtils;

public class Youtube extends Feature 
{
	public Youtube(HeufyBot bot, String name) 
	{
		super(bot, name);
		
		this.settingsPath = "featuredata/googleapikey.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "youtube";
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "youtube <terms> | Returns the first search result on YouTube for the given search term.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		String apiKey = FileUtils.readFile(getSettingsPath());
		if(apiKey.equals(""))
		{
			bot.sendMessage(source, "[Youtube] Error: No API key for Google provided");
		}
		else if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Youtube] Search what?");
		}
		else if(metadata.startsWith(" "))
		{
			try
			{
				String urlString = "https://gdata.youtube.com/feeds/api/videos?q=" + metadata.substring(1) +
	                    "&orderby=relevance" +
	                    "&max-results=1" +
	                    "&v=2" +
	                    "&key=" + apiKey;
				String videoData = URLUtils.grab(urlString);
			    String[] splitElements = videoData.split("<");
			    String title = "";
			    String description = "";
			    String duration = "";
			    String link = "";
			    int totalResults = 0;
			      
			    for(int i = 0; i < splitElements.length; i++)
			    {
			    	if(splitElements[i].matches("^media:title type='plain'.*"))
			    	{
			    		title = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
			    	}
			    	else if(splitElements[i].matches("^media:description type='plain'.*"))
			    	{
			    		description = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
			    		if(description.length() > 99)
			    		{
			    			description = description.substring(0, 97) + "...";
			    		}
			    	}
			    	else if(splitElements[i].matches("^yt:duration seconds.*"))
			    	{
			    		int durationSeconds = Integer.parseInt(splitElements[i].split("'")[1]);
			    		if(durationSeconds >= 3600)
			    		{
			    			duration = ( durationSeconds / 3600 < 10 ? "0": "") + durationSeconds / 3600 +":"+
				    	               ( (durationSeconds % 3600) / 60 < 10 ? "0": "") + (durationSeconds % 3600) / 60 +":"+
				    	               ( (durationSeconds % 3600) % 60 < 10 ? "0": "") + (durationSeconds % 3600) % 60;
			    		}
			    		else
			    		{
			    			duration = ( durationSeconds / 60 < 10 ? "0": "") + durationSeconds / 60 +":"+
			    	                   ( durationSeconds % 60 < 10 ? "0": "") + durationSeconds % 60;
			    		}
			    	}
			    	else if(splitElements[i].matches("^link rel='alternate' type='text/html' href='.*"))
			    	{
			    		link = splitElements[i].split("'")[5];
			    	}
			    	else if(splitElements[i].matches("^openSearch:totalResults.*"))
			    	{
			    		totalResults = Integer.parseInt(splitElements[i].substring(splitElements[i].indexOf(">") + 1));
			    	}
			    }
			    if(totalResults > 0)
			    {
			    	this.bot.sendMessage(source, "[Youtube] Results: " + totalResults + " || Top Result: " + link + " | Video Title: " + title + " | " + duration + " | " + description);
			    }
			    else
			    {
			    	this.bot.sendMessage(source, "[Youtube] No results for '" + metadata.substring(1) + "'.");
			    }
			}
			catch(Exception e)
			{
				LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
				this.bot.sendMessage(source, "[Youtube] Error: Could not retrieve search results.");
			}
		}
	}

	@Override
	public void onLoad() 
	{
		FileUtils.touchFile(getSettingsPath());
	}

	@Override
	public void onUnload() 
	{
	}
}