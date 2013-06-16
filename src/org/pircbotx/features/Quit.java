package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;

public class Quit extends Feature
{
	public Quit(HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "quit";
		this.authType = AuthType.OPs;
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		try
		{
			this.bot.quitServer("Killed by " + triggerUser);
			Thread.sleep(1000);
			this.bot.shutdown(true);
			System.exit(0);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "quit | Makes the bot quit the server.";
	}
	
	@Override
	public void onLoad()
	{
	}

	@Override
	public void onUnload()
	{
	}
}