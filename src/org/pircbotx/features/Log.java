package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.PastebinUtils;

public class Log extends Feature
{
	private String source;
	private String dateString;

	public Log(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "log";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			this.dateString = dateFormat.format(date);

			this.source = source;
			post();
		}
		else if (metadata.startsWith(" "))
		{
			try
			{
				int year = Integer.parseInt(metadata.substring(1, 5));
				int month = Integer.parseInt(metadata.substring(6, 8));
				int day = Integer.parseInt(metadata.substring(9));
    		
				Calendar calendar = new GregorianCalendar(year, month, day);
				calendar.clear();
    		
				this.dateString = metadata.substring(1);
				this.source = source;
    		
				post();
	    	}
			catch (Exception e)
			{
				this.bot.sendMessage(source, "[Log] Parser Error: Make sure the date format for the log is: yyyy-mm-dd");
			}
		}
	}
  
	public void post()
	{
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				String targetLog = source;
				if(!source.contains("#"))
				{
					targetLog = source.replaceAll("[^a-zA-Z0-9]+","");
				}
				String result = PastebinUtils.post("logs/" + bot.getNetworkName() + "/" + targetLog + "/" + dateString + ".txt", "Log for " + source + " on " + dateString, true, 10);
				if(result != null)
				{
					if(result.equals("NotFound"))
					{
						bot.sendMessage(source, "[Log] I do not have that log");
					}
					else if(result.equals("NoKey"))
					{
						bot.sendMessage(source, "[Log] Error: No PasteBin API key was found");
					}
					else
					{
						bot.sendMessage(source, "[Log] Log for " + source + " on " + dateString + " posted: " + result + " (Link expires in 10 minutes)");
					}
				}
				else
				{
					bot.sendMessage(source, "[Log] Error: Log could not be posted");
				}
			}
		});
		thread.start();
	}

	@Override
	public void onLoad()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "log, " + bot.getCommandPrefix() + "log <YYYY-MM-DD> | Provides a log of the current channel for today, or another date if specified.";
	}

	@Override
	public void onUnload()
	{
	}
}