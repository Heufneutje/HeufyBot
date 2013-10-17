package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Iterator;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.LoggingUtils;

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
		return "Commands: " + bot.getCommandPrefix() + "ac login <username>:<password>," + bot.getCommandPrefix() + "ac add <nickname>, " + bot.getCommandPrefix() + "ac drop <nickname> | Login to your HeufyBot account, or add or remove a nickname from the owner list.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(source.equalsIgnoreCase(triggerUser))
		{
			if (metadata.equals(""))
		    {
				this.bot.sendMessage(source, "[AccessControl] Access what?");
		    }
			else
			{
				if(bot.getUser(source).isBotAdmin() || bot.isAdminMode() == false)
				{
					if (metadata.startsWith(" register"))
					{
						try
						{
				  	  		String account = metadata.substring(10);
				  	  		String username = account.split(":")[0];
				  	  		String password = account.split(":")[1];
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
				  	  			bot.setAdminMode(true);
				  	  			adminList.add(hashedAccount);
				  	  			FileUtils.writeFileAppend(settingsPath, hashedAccount + "\n");
				  	  			bot.sendMessage(source, "[AccessControl] " + username + " was added to the access list!");
				  	  		}
						}
						catch(Exception e)
						{
							LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
							bot.sendMessage(source, "[AccessControl] Error in command");
						}
				    }
					else if (metadata.startsWith(" drop"))
				    {
						try
						{
							String account = metadata.substring(6);
				  	  		String username = account.split(":")[0];
				  	  		String password = account.split(":")[1];
				  	  		String hashedAccount = username + ":" + password;
				  	  		
				  	  		boolean match = false;
				  	  		for(Iterator<String> iter = adminList.iterator(); iter.hasNext();)
				  	  		{
				  	  			String access = iter.next();
				  	  			if(access.equals(hashedAccount))
				  	  			{
				  	  				iter.remove();
				  	  				match = true;
				  	  			}
				  	  		}
				  	  		if(match)
				  	  		{
				  	  			if(adminList.size() == 0)
				  	  			{
				  	  				bot.setAdminMode(false);
				  	  			}
				  	  			FileUtils.deleteFile(settingsPath);
				  	  			FileUtils.touchFile(settingsPath);
					  	  		for(String ignore : adminList)
					  	  		{
					  	  			FileUtils.writeFileAppend(settingsPath, ignore + "\n");
					  	  		}
					  	  		bot.sendMessage(source, "[AccessControl] " + username + " was removed from the access list!");
				  	  		}
				  	  		else
				  	  		{
				  	  			bot.sendMessage(source, "[AccessControl] " + username + " is not on the access list!");
				  	  		}
						}
						catch(Exception e)
						{
							LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
							bot.sendMessage(source, "[AccessControl] Error in command");
						}
				    }
				}
				else if(metadata.startsWith(" login"))
				{
					try
					{
			  	  		String account = metadata.substring(7);
			  	  		String username = account.split(":")[0];
			  	  		String password = account.split(":")[1];
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
			  	  			bot.getUser(source).setBotAdmin(true);
			  	  			bot.sendMessage(source, "[AccessControl] " + username + " is succesfully logged in with access level Admin.");
			  	  		}
			  	  		else
			  	  		{
			  	  			bot.sendMessage(source, "[AccessControl] Login failed.");
			  	  		}
					}
					catch(Exception e)
					{
						LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
						bot.sendMessage(source, "[AccessControl] Login failed.");
					}
				}
				else
				{
					this.bot.sendMessage(source, "[AccessControl] Only my admins are authorized to use this command!");
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
		if(adminList.size() > 0)
		{
			bot.setAdminMode(true);
		}
	}

	@Override
	public void onUnload()
	{
		bot.setAdminMode(false);
	}
}
