package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Do extends Feature
{
	public Do(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "do";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Do] Do what?");
		}
		else if (metadata.startsWith(" "))
		{
			this.bot.sendAction(source, metadata.substring(1));
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "do <message> | Makes the bot perform the given line as an action.";
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