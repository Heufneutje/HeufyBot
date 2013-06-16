package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Part extends Feature
{
	public Part(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "part";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Part] Part what?");
		}
		else if (metadata.startsWith(" "))
		{
			this.bot.partChannel(metadata.substring(1));
		}
	}
	
	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "part <channel> | Makes the bot leave a channel.";
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