package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Raw extends Feature
{
	public Raw(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.OPs;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "raw";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Raw] What do you want me to do?");
		}
		else if (metadata.startsWith(" "))
		{
			this.bot.sendRawLine(metadata.substring(1));
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "raw <message> | Makes the bot send a raw IRC line to the server.";
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