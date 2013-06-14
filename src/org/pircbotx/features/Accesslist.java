package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Iterator;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.FileUtils;

public class Accesslist extends Feature
{
	private String settingsPath = "featuresettings/accesslist.txt";
	private ArrayList<String> accessList;
	
	public Accesslist(HeufyBot bot, String name)
	{
	    super(bot, name);
	    this.featureType = "join";
	    this.triggers = new String[2];
	    this.triggers[0] = bot.getCommandPrefix() + "addaccess";
	    this.triggers[1] = bot.getCommandPrefix() + "remaccess";
	}

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
	  if(triggerCommand.equals("join"))
	  {
		  for(String entry : accessList)
		  {
			  String[] accessElement = entry.split(",");
			  if (accessElement.length == 3)
			  {
				  if(accessElement[1].equalsIgnoreCase(triggerUser) && accessElement[0].equalsIgnoreCase(source))
				  {
					  bot.setMode(bot.getChannel(source), accessElement[2] + " " + triggerUser);
				  }
			  }
		  }
	  }
	  
	  else if(triggerCommand.equals(bot.getCommandPrefix() + "addaccess"))
	  {
		  if ((metadata.equals("")) || (metadata.equals(" ")))
		  {
		      this.bot.sendMessage(source, "[AccessList] Add what?");
		  }
		  else
		  {
			  try
			  {
				  String[] args = metadata.substring(1).split(" ");
				  accessList.add(source + "," + args[0] + "," + args[1]);
				  writeAccessList();
				  this.bot.sendMessage(source, "[AccessList] Nickname " + args[0] + " was added to the access list of " + source + " with mode +" + args[1]);
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
				  this.bot.sendMessage(source, "[AccessList] Access list could not be changed. Make sure you use addaccess <nick> <mode>");
			  }
		  }
	  }
	  else if(triggerCommand.equals(bot.getCommandPrefix() + "remaccess"))
	  {
		  if ((metadata.equals("")) || (metadata.equals(" ")))
		  {
		      this.bot.sendMessage(source, "[AccessList] Remove what?");
		  }
		  else
		  {
			  try
			  {
				  String[] args = metadata.substring(1).split(" ");
				  for(Iterator<String> iter = accessList.iterator(); iter.hasNext();)
				  {
					  String entry = iter.next();
					  String[] accessElement = entry.split(",");
					  if(source.equalsIgnoreCase(accessElement[0]) && args[0].equalsIgnoreCase(accessElement[1]) && args[1].equalsIgnoreCase(accessElement[2]))
					  {
						  iter.remove();
						  writeAccessList();
						  this.bot.sendMessage(source, "[AccessList] Nickname " + args[0] + " was removed from the access list of " + source);
					  }
				  }
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
				  this.bot.sendMessage(source, "[AccessList] Access list could not be changed. Make sure you use remaccess <nick> <mode>");
			  }
		  }
	  }
  }

	@Override
	public void connectTrigger()
	{
		this.accessList = new ArrayList<String>();
		if(FileUtils.touchFile(settingsPath))
		{
			String[] readEntries = FileUtils.readFile(settingsPath).split("\n");
			for(int i = 0; i < readEntries.length; i++)
			{
				accessList.add(readEntries[i]);
			}
		}
	}
	
	private void writeAccessList()
	{
		String accessString = "";
		for(String entry : accessList)
		{
			accessString += entry + "\n";
		}
		FileUtils.writeFile(settingsPath, accessString);
	}

	@Override
	public String getHelp()
	{
		return "Manage an access list for a channel. Commands: " + bot.getCommandPrefix() + "addaccess <nick> <mode> | Add a nickname to the access list" + "," + bot.getCommandPrefix() + "remaccess <nick> <mode> | Remove a nickname from the access list";
	}
}