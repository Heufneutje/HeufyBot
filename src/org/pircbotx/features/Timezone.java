package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Timezone extends Feature
{
	public Timezone(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "timezone";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm (hh:mm aa) 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
			this.bot.sendMessage(source, "[Timezone] The time is: " + format.format(date) + " | Timezone: " + TimeZone.getTimeZone("UTC").getID() + " (" + TimeZone.getTimeZone("UTC").getDisplayName() + ")");
		}
		else if (metadata.startsWith(" "))
		{
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm (hh:mm aa) 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
			TimeZone timeZone = TimeZone.getTimeZone(metadata.substring(1).toUpperCase());
				
			format.setTimeZone(timeZone);
			this.bot.sendMessage(source, "[Timezone] The time is: " + format.format(date) + " | Timezone: " + timeZone.getDisplayName(timeZone.inDaylightTime(date), TimeZone.SHORT) + " (" + timeZone.getDisplayName(timeZone.inDaylightTime(date), TimeZone.LONG) + ")");
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "timezone <timezoneid> | Gives the current time in the given timezone.";
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