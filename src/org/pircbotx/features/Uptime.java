package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Uptime extends Feature
{
	Date date;
	
	public Uptime(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "uptime";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm aa (z)");
		this.bot.sendMessage(source, "[Uptime] HeufyBot has been running since " + format.format(date) + ".");
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "uptime | Shows how long the bot has been running.";
	}
	
	@Override
	public void onLoad()
	{
		date = new Date();
	}

	@Override
	public void onUnload()
	{
	}
}