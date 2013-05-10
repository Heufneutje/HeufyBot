package org.pircbotx.features;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.pircbotx.HeufyBot;

public class Event extends Feature
{
	private String settingsPath = "featuresettings/events.txt";
	
  public Event(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!event";
  }

  public void process(String source, String metadata, String triggerUser)
  {
	  try
	  {
		  int datestart = metadata.indexOf('(') + 1;
		  int datestop = metadata.indexOf(')');
		  String date = metadata.substring(datestart, datestop);
		  
		  DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		  format.parse(date);
		  
		  String eventText = metadata.substring(datestop + 2);
		  this.addEvent(date, eventText);
		  
		  this.bot.sendMessage(source, "[Event] Event '" + eventText + "' on the date " + date + " (UTC) was added to the list of events!");
	  }
	  catch (NullPointerException e)
	  {
		  this.bot.sendMessage(source, "[Event] You didn't specify a date!");
	  }
	  catch (ParseException e)
	  {
		  this.bot.sendMessage(source, "[Event] The date you put is not parsable. Please use the format (YYYY-MM-DD hh:mm) for the date.");
	  }
	  catch (Exception e)
	  {
		  e.printStackTrace();
		  this.bot.sendMessage(source, "[Event] Unknown error. Please try again.");
	  }
  }

	@Override
	public void connectTrigger()
	{
		File file = new File(settingsPath);
		if(!file.exists())
		{
			bot.writeFile(settingsPath, "");
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: !event (YYYY-MM-DD hh:mm) Event text | Create an event that can be called using !timetill or removed using !remevent.";
	}
	
	private void addEvent(String date, String event)
	{
		bot.writeFileAppend(settingsPath, date + "|" + event + "\n");
	}
}