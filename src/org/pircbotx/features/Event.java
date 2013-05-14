package org.pircbotx.features;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.pircbotx.HeufyBot;

public class Event extends Feature
{
	private String settingsPath = "featuresettings/events.txt";
	
  public Event(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[3];
    this.triggers[0] = bot.getCommandPrefix() + "event";
    this.triggers[1] = bot.getCommandPrefix() + "timetill";
    this.triggers[2] = bot.getCommandPrefix() + "remevent";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
	  if(triggerCommand.equals(bot.getCommandPrefix() + "event"))
	  {
		  try
		  {
			  int datestart = metadata.indexOf('(') + 1;
			  int datestop = metadata.indexOf(')');
			  String date = metadata.substring(datestart, datestop);
			  
			  DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm");
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
			  this.bot.sendMessage(source, "[Event] The date you put is not parsable. Please use the format (MM-DD-YYYY hh:mm) for the date.");
		  }
		  catch (Exception e)
		  {
			  this.bot.sendMessage(source, "[Event] Unknown error. Please try again.");
		  }
	  }
	  else if(triggerCommand.equals(bot.getCommandPrefix() + "timetill"))
	  {
		  String events = bot.readFile(settingsPath);
		  String[] eventsArray = events.split("\n");
		  
		  boolean firstResult = false;
		  String firstDate = "";
		  String firstEvent = "";
		  
		  try
		  {
			  while(firstResult == false)
			  {
				  for(int i = 0; i < eventsArray.length; i++)
				  {
					  if(eventsArray[i].substring(eventsArray[i].indexOf("_") + 1).toLowerCase().indexOf(metadata.substring(1).toLowerCase()) > -1)
					  {
						  firstDate = eventsArray[i].split("_")[0];
						  firstEvent = eventsArray[i].substring(eventsArray[i].indexOf("_") + 1);
						  firstResult = true;
					  }
				  }
				  break;
			  }
			  
			  if(firstResult)
			  {
				  DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm");
				  
				  Date date1 = new Date();
				  Date date2 = format.parse(firstDate);
				  
				  Calendar start = Calendar.getInstance();
				  start.setTime(date1);
				  Calendar end = Calendar.getInstance();
				  end.setTime(date2);
				
				  Integer[] elapsed = new Integer[3];
				  Calendar clone = (Calendar) start.clone(); // Otherwise changes are been reflected.
				  elapsed[0] = elapsed(clone, end, Calendar.DATE);
				  clone.add(Calendar.DATE, elapsed[0]);
				  elapsed[1] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 3600000;
				  clone.add(Calendar.HOUR, elapsed[1]);
				  elapsed[2] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 60000;
				  clone.add(Calendar.MINUTE, elapsed[2]);
				
				  String result = String.format("%d day(s), %d hour(s) and %d minute(s)", elapsed[0], elapsed[1], elapsed[2]);
				  bot.sendMessage(source, "[Event] '" + firstEvent + "' will occur in " + result);
			  }
			  else
			  {
				  bot.sendMessage(source, "[Event] No event matching '" + metadata.substring(1) + "' was found in the events list.");
			  }
		  }
		  catch (Exception e)
		  {
			  try
			  {
				  bot.sendMessage(source, "[Event] No event matching '" + metadata.substring(1) + "' was found in the events list.");
			  }
			  catch(Exception e1)
			  {
				  bot.sendMessage(source, "[Event] Unknown error. Please try again");
			  }
		  }
	  }
	  else
	  {
		  String events = bot.readFile(settingsPath);
		  String[] eventsArray = events.split("\n");
		  ArrayList<String> eventsList = new ArrayList<String>();
		  
		  for(int i = 0; i < eventsArray.length; i++)
		  {
			  eventsList.add(eventsArray[i]);
		  }
		  
		  boolean firstResult = false;
		  String firstEvent = "";
		  
		  try
		  {
			  while(firstResult == false)
			  {
				  for(int i = 0; i < eventsArray.length; i++)
				  {
					  if(eventsArray[i].substring(eventsArray[i].indexOf("_") + 1).toLowerCase().indexOf(metadata.substring(1).toLowerCase()) > -1)
					  {
						  eventsList.remove(i);
						  firstEvent = eventsArray[i].substring(eventsArray[i].indexOf("_") + 1);
						  firstResult = true;
					  }
				  }
				  break;
			  }
			  
			  if(firstResult)
			  {
				  String newList = "";
				  for(String event : eventsList)
				  {
					  newList += event + "\n";
				  }
				  bot.writeFile(settingsPath, newList);
				  bot.sendMessage(source, "[Event] '" + firstEvent + "' was removed from the events list.");
			  }
			  else
			  {
				  bot.sendMessage(source, "[Event] No event matching '" + metadata.substring(1) + "' was found in the events list.");
			  }
			 
		  }
		  catch (Exception e)
		  {
			  try
			  {
				  bot.sendMessage(source, "[Event] No event matching '" + metadata.substring(1) + "' was found in the events list.");
			  }
			  catch(Exception e1)
			  {
				  bot.sendMessage(source, "[Event] Unknown error. Please try again");
			  }
		  }
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
		return "Commands: " + bot.getCommandPrefix() + "event (MM-DD-YYYY hh:mm) Event, " + bot.getCommandPrefix() + "timetill Event, " + bot.getCommandPrefix() + "remevent Event | Create an event that can be called using timetill or removed using remevent.";
	}
	
	private void addEvent(String date, String event)
	{
		bot.writeFileAppend(settingsPath, date + "_" + event + "\n");
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