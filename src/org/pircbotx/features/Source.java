package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Source extends Feature
{
	public Source(HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = "!source";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		this.bot.sendMessage(source, "[Source] https://github.com/Heufneutje/HeufyBot");
	}

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: !source | Provides a link to the bot's source code.";
	}
}