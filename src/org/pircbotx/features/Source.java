package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Source extends Feature
{
	public Source(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "source";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		this.bot.sendMessage(source, "[Source] https://github.com/Heufneutje/HeufyBot");
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "source | Provides a link to the bot's source code.";
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