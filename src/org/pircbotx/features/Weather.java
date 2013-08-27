package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.URLUtils;

public class Weather extends Feature
{
	public Weather(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/worldweatherapikey.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "weather";
	}

	@Override
	public String getHelp() 
	{
		return "Commands: " + bot.getCommandPrefix() + "weather <location/nick> | Displays the weather of a given location or a user on the chatmap. Location can be US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude, IATA or city name.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			String urlString = "http://www.tsukiakariusagi.net/chatmaplookup.php?nick=" + triggerUser;
			String data = URLUtils.grab(urlString);
			if(data.equals(", "))
			{
				bot.sendMessage(source, "[Weather] You are not registered on the chatmap");
			}
			else
			{
				bot.sendMessage(source, this.lookupWeather(data));
			}
		}
		else
		{
			String urlString = "http://www.tsukiakariusagi.net/chatmaplookup.php?nick=" + metadata.substring(1);
			String data = URLUtils.grab(urlString);
			if(data.equals(", ") || metadata.substring(1).contains(" "))
			{
				bot.sendMessage(source, this.lookupWeather(metadata.substring(1)));
			}
			else
			{
				bot.sendMessage(source, this.lookupWeather(data));
			}
		}
	}
	
	private String lookupWeather(String weatherQuery)
	{
		String apiKey = FileUtils.readFile(getSettingsPath());
		if(apiKey.equals(""))
		{
			return "[Weather] Error: No API key for worldweatheronline provided";
		}
		else
		{
			String location = weatherQuery.replaceAll(" ", "");
			String urlString = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + location + 
					"&format=xml" + 
					"&extra=localObsTime" +
					"&num_of_days=1" + 
					"&key=" + apiKey;
			String data = URLUtils.grab(urlString);
			
			if(data.equals("ERROR"))
			{
				return "[Weather] Error: Invalid location";
			}
			else
			{
				String type = "";
				String query = "";
				String lastUpdate = "";
				
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
						return "[Weather] Error: Invalid location";
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
						weatherDesc = splitElements[i].substring(splitElements[i].indexOf("[", 5) + 1, splitElements[i].indexOf("]"));
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
					else if(splitElements[i].matches("^localObsDateTime.*"))
					{
						lastUpdate = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
					}
				}
				return "[Weather] " + type + ": " + query + " | " + "Temp: " + tempC + "°C/" + tempF + "°F | Weather: " + weatherDesc + " | Humidity: " + humidity + "% | Wind: " + windSpeedKm + " kmph/" + windSpeedMiles + "mph " + winddir + " (Local Observation Time: " + lastUpdate + ")";
			}
		}
	}
	
	@Override
	public void onLoad()
	{
		FileUtils.touchFile(getSettingsPath());
	}
	
	@Override
	public void onUnload()
	{
	}
}