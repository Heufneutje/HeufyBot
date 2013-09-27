package org.pircbotx.features;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.URLUtils;

public class Time extends Feature
{
	public Time(HeufyBot bot, String name)
	{
		super(bot, name);
		this.settingsPath = "featuredata/worldweatherapikey.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "time";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			String urlString = "http://www.tsukiakariusagi.net/chatmaplookup.php?nick=" + triggerUser;
			String data = URLUtils.grab(urlString);
			
			if(data.equals(", "))
			{
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("HH:mm (hh:mm aa) 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
				this.bot.sendMessage(source, "[Time] Local time is " + format.format(date) + " | You are not registed on the chatmap, returned the bot's local time instead");
			}
			else
			{
				bot.sendMessage(source, this.lookupTime(data));
			}
		}
		else if(metadata.startsWith(" "))
		{
			String urlString = "http://www.tsukiakariusagi.net/chatmaplookup.php?nick=" + metadata.replaceAll(" ", "");
			String data = URLUtils.grab(urlString);
			
			if(data.equals(", "))
			{
				this.bot.sendMessage(source, this.lookupTime(metadata.substring(1).replaceAll(" ", "%20")));
			}
			else
			{
				this.bot.sendMessage(source, this.lookupTime(data));
			}
		}
	}
	
	private String lookupTime(String timeQuery)
	{
		String apiKey = FileUtils.readFile(settingsPath);
		if(apiKey.equals(""))
		{
			return "[Time] Error: No API key for worldweatheronline provided";
		}
		else
		{
			String urlString = "http://api.worldweatheronline.com/free/v1/tz.ashx?q=" + timeQuery.replaceAll(" ", "") + 
					"&format=xml" + 
					"&key=" + apiKey;
			String data = URLUtils.grab(urlString);
			if(data.equals("ERROR"))
			{
				return "[Time] Error: Invalid location";
			}
			else
			{
				String[] splitElements = data.split("<");
				String type = "";
				String query = "";
				String localtime = "";
				for(int i = 0; i < splitElements.length; i++)
				{
					if(splitElements[i].matches("^error.*"))
					{
						return "[Time] Error: Invalid location";
					}
					else if(splitElements[i].matches("^type.*"))
					{
						type = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
					}
					else if(splitElements[i].matches("^query.*"))
					{
						query = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
					}
					else if(splitElements[i].matches("^localtime.*"))
					{
						localtime = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
					}
				}
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
				Date date;
				try 
				{
					date = dt.parse(localtime);
					SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm (hh:mm aa) 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
					return "Local time is " + dt1.format(date) + " | " + type + ": " + query;
				}
				catch (ParseException e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}
	}
	
	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "time <location/nick> | Displays the time of a given location or a user on the chatmap. Location can be US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude, IATA or city name.";
	}

	@Override
	public void onLoad()
	{
		FileUtils.touchFile(settingsPath);
	}

	@Override
	public void onUnload()
	{
	}
}