package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.URLUtils;

public class Shorten extends Feature
{
	public Shorten(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "shorten";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Shorten] Shorten what?");
		}
		else if (metadata.startsWith(" "))
		{
			String shortenedURL = URLUtils.shortenURL(metadata.replaceAll(" ", ""));
			if(shortenedURL == null)
			{
				bot.sendMessage(source, "[Shorten] Error: URL could not be shortned");
			}
			else if(shortenedURL.equals("NoKey"))
			{
				bot.sendMessage(source, "[Shorten] Error: No API key for Google provided");
			}
			else
			{
				bot.sendMessage(source, "[Shorten] " + shortenedURL);
			}
		}
	}

	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onUnload()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "shorten <url> | Shorten the given URL using Googl.";
	}
}