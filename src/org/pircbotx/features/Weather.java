package org.pircbotx.features;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.URLUtils;
import org.w3c.dom.Document;

public class Weather extends Feature
{
	private String settingsPath = "featuresettings/worldweatherapikey.txt";
	public Weather(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "weather";
	}

	@Override
	public String getHelp() 
	{
		return null;
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			this.bot.sendMessage(source, "[Weather] No location specified");
		}
		else
		{
			String apiKey = FileUtils.readFile(settingsPath);
			if(apiKey.equals(""))
			{
				this.bot.sendMessage(source, "[Weather] Error: No API key for worldweatheronline provided");
			}
			else
			{
				int numberOfDays = 1;
				String location = metadata.substring(1);
				
				if(metadata.substring(1).toLowerCase().startsWith("forecast "))
				{
					location = metadata.split("forecast ")[1];
					numberOfDays = 4;
				}
				
				String urlString = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + location.replaceAll(" ", "") + 
						"&format=xml" + 
						"&num_of_days=" + numberOfDays + 
						"&key=" + apiKey;
				String data = URLUtils.grab(urlString);
				System.err.println(data);
				
				if(data.equals("ERROR"))
				{
					this.bot.sendMessage(source, "[Time] Error: Invalid location");
				}
				else
				{
					String type = "";
					String query = "";
					
					if(numberOfDays == 1)
					{
						String[] splitElements = data.split("</current_condition>")[0].split("<");
						
						String tempC = "";
						String tempF = "";
						String weatherDesc = "";
						String winddir = "";
						String windSpeedMiles = "";
						String windSpeedKm = "";
						String humidity = "";
						
						for(int i = 0; i < splitElements.length; i++)
						{
							if(splitElements[i].matches("^error.*"))
							{
								this.bot.sendMessage(source, "[Weather] Error: Invalid location");
							}
							else if(splitElements[i].matches("^type.*"))
							{
								type = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^query.*"))
							{
								query = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^temp_C.*"))
							{
								tempC = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^temp_F.*"))
							{
								tempF = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^\\!\\[CDATA\\[.*") && (!splitElements[i].contains("http")))
							{
								weatherDesc = splitElements[i].substring(splitElements[i].indexOf("[", 5) + 1, splitElements[i].indexOf(" ]"));
							}
							else if(splitElements[i].matches("^windspeedMiles.*"))
							{
								windSpeedMiles = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^windspeedKmph.*"))
							{
								windSpeedKm = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^windspeedKmph.*"))
							{
								windSpeedKm = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^winddir16Point.*"))
							{
								winddir = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
							else if(splitElements[i].matches("^humidity.*"))
							{
								humidity = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
							}
						}
						bot.sendMessage(source, "[Weather] " + type + ": " + query + " | " + "Temperature: " + tempC + "°C/" + tempF + "°F | Weather: " + weatherDesc + " | Humidity: " + humidity + "% | Wind: " + windSpeedKm + " kmph/" + windSpeedMiles + "mph " + winddir);
					}
					else
					{
						String[] splitElements = data.split("</weather>");
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
}