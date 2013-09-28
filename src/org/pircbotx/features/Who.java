package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;

public class Who extends Feature
{
	public Who(HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "who";
		this.authType = AuthType.Anyone;
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(metadata.equals(""))
		{
			bot.sendMessage(source, "[Who] I am HeufyBot, a Java IRC bot developed by Heufneutje, running on the PircBotX " + HeufyBot.VERSION + " framework. My current version is " + bot.getVersionNumber() + ".");
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "who | Gives some general information about the bot.";
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