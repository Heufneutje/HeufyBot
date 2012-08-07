package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Autovoice extends Feature
{
	public Autovoice(HeufyBot bot, String name)
	{
		super(bot, name);
		super.featureType = "join";
	}

	@Override
	public void process(String source, String metadata, String triggerUser) 
	{
		if(bot.isOped(bot.getChannel(source)))
		{
			bot.setMode(bot.getChannel(source), "+v " + triggerUser);
		}
	}

	@Override
	public void connectTrigger()
	{
	}
}