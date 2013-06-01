package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Outofcontext extends Feature 
{
	public Outofcontext(HeufyBot bot, String name) 
	{
		super(bot, name);
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "ooc";
	}

	@Override
	public String getHelp()
	{
		return null;
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		String command;
		if(metadata.equals(""))
		{
			
		}
		else
		{
			
		}
	}

	@Override
	public void connectTrigger()
	{
		
	}
}
