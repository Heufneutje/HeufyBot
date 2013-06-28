package org.pircbotx.features;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

public class Accesscontrol extends Feature 
{
	private ArrayList<String> adminList;

	public Accesscontrol(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/botowners.txt";
		this.adminList = new ArrayList<String>();
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
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
		if(source.equalsIgnoreCase(triggerUser))
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
		    {
				this.bot.sendMessage(source, "[AccessControl] Access what?");
		    }
			else if (metadata.startsWith(" add"))
		    {
				try
				{
		  	  		String account = metadata.substring(5);
		  	  		String username = account.split(":")[0];
		  	  		String password = account.split(":")[1];
		  	  		
		  	  		byte[] bytes = password.getBytes("UTF-8");
		  	  		MessageDigest md = MessageDigest.getInstance("MD5");
		  	  		byte[] digestedBytes = md.digest(bytes);
		  	  		password = new String(digestedBytes);
		  	  		
		  	  		String hashedAccount = username + ":" + password;
		  	  		
			  	  	boolean match = false;
		  	  		for(String admin : adminList)
		  	  		{
		  	  			if(admin.equals(hashedAccount))
		  	  			{
		  	  				match = true;
		  	  			}
		  	  		}
		  	  		if(match)
		  	  		{
		  	  			bot.sendMessage(source, "[AccessControl] " + username + " is already on the access list!");
		  	  		}
		  	  		else
		  	  		{
		  	  			adminList.add(hashedAccount);
		  	  			FileUtils.writeFileAppend(settingsPath, hashedAccount + "\n");
		  	  			bot.sendMessage(source, "[AccessControl] " + username + " was added to the access list!");
		  	  		}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					bot.sendMessage(source, "[AccessControl] Error in command");
				}
		    }
			else if (metadata.startsWith(" remove"))
		    {
	  	  		String nick = metadata.substring(8);
	  	  		
	  	  		boolean match = false;
	  	  		for(Iterator<String> iter = adminList.iterator(); iter.hasNext();)
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
		  	  		for(String ignore : adminList)
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
		else
		{
			bot.sendMessage(source, "[AccessControl] This feature can only be used in PM.");
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
				adminList.add(nicks[i]);
			}
		}
	}

	@Override
	public void onUnload()
	{
	}
}
