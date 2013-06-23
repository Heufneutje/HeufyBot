package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Iterator;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

public class Accesscontrol extends Feature 
{
	private ArrayList<String> ownerList = new ArrayList<String>();
	
	public Accesscontrol(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/botowners.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Owners;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "ac";
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "ac," + bot.getCommandPrefix() + "ac add <nickname>, " + bot.getCommandPrefix() + "ac remove <nickname> | Add or remove a nickname from the owner list.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
	    {
			String owners = "";
			for (String name : ownerList)
			{
				owners += name;
				if(!ownerList.get(ownerList.size() - 1).equals(name))
				{
					owners += ", ";
				}
			}
			this.bot.sendMessage(source, "[AccessControl] My owners are: " + owners);
	    }
		else if (metadata.startsWith(" add"))
	    {
  	  		String nick = metadata.substring(5);
  	  		
	  	  	boolean match = false;
  	  		for(String ignore : ownerList)
  	  		{
  	  			if(ignore.equalsIgnoreCase(nick))
  	  			{
  	  				match = true;
  	  			}
  	  		}
  	  		if(match)
  	  		{
  	  			bot.sendMessage(source, "[AccessControl] " + nick + " is already on the access list!");
  	  		}
  	  		else
  	  		{
  	  			ownerList.add(nick);
  	  			FileUtils.writeFileAppend(settingsPath, nick + "\n");
  	  			bot.sendMessage(source, "[AccessControl] " + nick + " was added to the access list!");
  	  		}
	    }
		else if (metadata.startsWith(" remove"))
	    {
  	  		String nick = metadata.substring(8);
  	  		
  	  		boolean match = false;
  	  		for(Iterator<String> iter = ownerList.iterator(); iter.hasNext();)
  	  		{
  	  			String access = iter.next();
  	  			if(access.equalsIgnoreCase(nick))
  	  			{
  	  				iter.remove();
  	  				match = true;
  	  			}
  	  		}
  	  		if(match)
  	  		{
  	  			FileUtils.deleteFile(settingsPath);
  	  			FileUtils.touchFile(settingsPath);
	  	  		for(String ignore : ownerList)
	  	  		{
	  	  			FileUtils.writeFileAppend(settingsPath, ignore + "\n");
	  	  		}
	  	  		bot.sendMessage(source, "[AccessControl] " + nick + " was removed from the access list!");
  	  		}
  	  		else
  	  		{
  	  			bot.sendMessage(source, "[AccessControl] " + nick + " is not on the access list!");
  	  		}
	    }
	}

	@Override
	public void onLoad() 
	{
		FileUtils.touchFile(settingsPath);
		String[] nicks = FileUtils.readFile(settingsPath).split("\n");
		for(int i = 0; i < nicks.length; i++)
		{
			if(!nicks[i].equals(""))
			{
				ownerList.add(nicks[i]);
			}
		}
		bot.setOwnerList(ownerList);
	}

	@Override
	public void onUnload()
	{
	}
}
