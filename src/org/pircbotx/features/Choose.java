package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Choose extends Feature
{
	public Choose(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "choose";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			this.bot.sendMessage(source, "[Choose] Choose what?");
		}
		else if (metadata.startsWith(" "))
		{
			String[] choices;
			if(metadata.substring(1).contains(","))
			{
				choices = metadata.substring(1).split(",");
			}
			else
			{
				choices = metadata.substring(1).split(" ");
			}
			int choiceNumber = (int) (Math.random() * choices.length);
			this.bot.sendMessage(source, "[Choose] Choice: " + choices[choiceNumber]);
		}
	}

	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onUnload()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "choose <choice1>, <choice2> | Makes the bot choose one of the given options at random.";
	}
}