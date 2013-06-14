package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Iterator;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.AuthorizationType;
import org.pircbotx.utilities.FileUtils;

public class Ignore extends Feature 
{
	private String settingsPath = "featuresettings/ignore.txt";
	private ArrayList<String> ignoreList = new ArrayList<String>();
	
	public Ignore(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.triggers = new String[2];
		this.triggers[0] = bot.getCommandPrefix() + "ignore";
		this.triggers[1] = bot.getCommandPrefix() + "unignore";
		this.authType = AuthorizationType.OPs;
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "ignore <nickname>, " + bot.getCommandPrefix() + "unignore <nickname> | Add or remove a nickname from the ignore list.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(triggerCommand.equals(bot.getCommandPrefix() + "ignore"))
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
		    {
				this.bot.sendMessage(source, "[Ignore] Ignore who?");
		    }
			else if (metadata.startsWith(" "))
		    {
	  	  		String nick = metadata.substring(1);
	  	  		
		  	  	boolean match = false;
	  	  		for(String ignore : ignoreList)
	  	  		{
	  	  			if(ignore.equalsIgnoreCase(nick))
	  	  			{
	  	  				match = true;
	  	  			}
	  	  		}
	  	  		if(match)
	  	  		{
	  	  			bot.sendMessage(source, "[Ignore] " + nick + " is already on the ignore list!");
	  	  		}
	  	  		else
	  	  		{
	  	  			ignoreList.add(nick);
	  	  			FileUtils.writeFileAppend(settingsPath, nick + "\n");
	  	  			bot.sendMessage(source, "[Ignore] " + nick + " was added to the ignore list!");
	  	  		}
		    }
		}
		else if(triggerCommand.equals(bot.getCommandPrefix() + "unignore"))
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
		    {
				this.bot.sendMessage(source, "[Ignore] Unignore who?");
		    }
			else if (metadata.startsWith(" "))
		    {
	  	  		String nick = metadata.substring(1);
	  	  		
	  	  		boolean match = false;
	  	  		for(Iterator<String> iter = ignoreList.iterator(); iter.hasNext();)
	  	  		{
	  	  			String ignore = iter.next();
	  	  			if(ignore.equalsIgnoreCase(nick))
	  	  			{
	  	  				iter.remove();
	  	  				match = true;
	  	  			}
	  	  		}
	  	  		if(match)
	  	  		{
	  	  			FileUtils.deleteFile(settingsPath);
	  	  			FileUtils.touchFile(settingsPath);
		  	  		for(String ignore : ignoreList)
		  	  		{
		  	  			FileUtils.writeFileAppend(settingsPath, ignore + "\n");
		  	  		}
		  	  		//bot.setIgnoreList(ignoreList);
		  	  		bot.sendMessage(source, "[Ignore] " + nick + " was removed from the ignore list!");
	  	  		}
	  	  		else
	  	  		{
	  	  			bot.sendMessage(source, "[Ignore] " + nick + " is not on the ignore list!");
	  	  		}
		    }
		}
	}

	@Override
	public void connectTrigger() 
	{
		FileUtils.touchFile(settingsPath);
		String[] ignores = FileUtils.readFile(settingsPath).split("\n");
		for(int i = 0; i < ignores.length; i++)
		{
			ignoreList.add(ignores[i]);
		}
		bot.setIgnoreList(ignoreList);
	}
}
