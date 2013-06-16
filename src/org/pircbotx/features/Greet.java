package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Greet extends Feature
{
	public Greet(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getName().toLowerCase();
		this.messageMustStartWithTrigger = false;
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(metadata.toLowerCase().matches("^(hi|hai|hey|sup|greeting|hello|howdy|yo).*"))
		{
			this.bot.sendMessage(source, "Hi, " + triggerUser + "!");
		}
	}

	@Override
	public void onLoad()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Greets every user that says hi to the bot.";
	}

	@Override
	public void onUnload()
	{
	}
}