package org.pircbotx.features;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.URLUtils;

public class Time extends Feature
{
	private String settingsPath = "featuresettings/worldweatherapikey.txt";
	public Time(HeufyBot bot, String name)
	{
		super(bot, name);
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "time";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("hh:mm aa 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
			this.bot.sendMessage(source, "[Time] Local time is " + format.format(date) + " | No location specified, returned the bot's local time instead");
		}
		else
		{
			String apiKey = FileUtils.readFile(settingsPath);
			if(apiKey.equals(""))
			{
				this.bot.sendMessage(source, "[Time] Error: No API key for worldweatheronline provided");
			}
			else
			{
				String urlString = "http://api.worldweatheronline.com/free/v1/tz.ashx?q=" + metadata.substring(1).replaceAll(" ", "") + 
						"&format=xml" + 
						"&key=" + apiKey;
				String data = URLUtils.grab(urlString);
				if(data.equals("ERROR"))
				{
					this.bot.sendMessage(source, "[Time] Error: Invalid location");
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
							this.bot.sendMessage(source, "[Time] Error: Invalid location");
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
						SimpleDateFormat dt1 = new SimpleDateFormat("hh:mm aa 'on' EEEEEEEE, dd 'of' MMMMMMMMMM, yyyy", Locale.US);
						bot.sendMessage(source, "Local time is " + dt1.format(date) + " | " + type + ": " + query);
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					} 
				}
			}
		}
	}

	@Override
	public void connectTrigger()
	{
		FileUtils.touchFile(settingsPath);
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "time <location> | Displays the time of a given location. Location can be US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude or city name.";
	}
}