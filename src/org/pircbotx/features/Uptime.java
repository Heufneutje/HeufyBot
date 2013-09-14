package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Uptime extends Feature
{
	Date dateStarted;
	
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
		if(metadata.equals(""))
		{
			DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm (zz)");
			
			Calendar start = Calendar.getInstance();
			start.setTime(dateStarted);
			Calendar end = Calendar.getInstance();
			end.setTime(new Date());
			
			Integer[] elapsed = new Integer[3];
			Calendar clone = (Calendar) start.clone(); // Otherwise changes are been reflected.
			elapsed[0] = elapsed(clone, end, Calendar.DATE);
			clone.add(Calendar.DATE, elapsed[0]);
			elapsed[1] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 3600000;
			clone.add(Calendar.HOUR, elapsed[1]);
			elapsed[2] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 60000;
			clone.add(Calendar.MINUTE, elapsed[2]);
			
			this.bot.sendMessage(source, "[Uptime] HeufyBot has been running since " + format.format(dateStarted) + " (" + elapsed[0] + " day(s), " + elapsed[1] + " hour(s) and " + elapsed[2] + " mimute(s))");
		}
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "uptime | Shows how long the bot has been running.";
	}
	
	@Override
	public void onLoad()
	{
		dateStarted = new Date();
	}

	@Override
	public void onUnload()
	{
	}
	
	private int elapsed(Calendar before, Calendar after, int field) 
	{
	    Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
	    int elapsed = -1;
	    while (!clone.after(after)) {
	        clone.add(field, 1);
	        elapsed++;
	    }
	    return elapsed;
	}
}