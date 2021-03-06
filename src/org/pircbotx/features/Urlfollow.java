package org.pircbotx.features;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.URLUtils;

public class Urlfollow extends Feature
{
	public Urlfollow(HeufyBot bot, String name)
	{
		super(bot, name);
	  
		this.settingsPath = "featuredata/googleapikey.txt";
	  
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
	  
		this.triggers = new String[2];
		this.triggers[0] = "http://";
		this.triggers[1] = "https://";
		this.messageMustStartWithTrigger = false;
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		String[] message = metadata.split(" ");
		ArrayList<String> urls = new ArrayList<String>();
		for(int i = 0; i < message.length; i++)
		{
			if(message[i].toLowerCase().contains("http"))
			{
				urls.add(message[i].substring(message[i].indexOf("http")));
			}
		}
	  
		while(urls.size() > 3)
		{
			urls.remove(urls.size() - 1);
		}
	  
		for(String urlstring : urls)
		{		    
			if(!urlstring.toLowerCase().matches(".*(jpe?g|gif|png|bmp)"))
			{
				String fullHostname = URLUtils.getFullHostname(urlstring);
				if(fullHostname != null)
				{
					System.err.println(fullHostname);
					String apiKey = FileUtils.readFile(getSettingsPath());
					if(fullHostname.matches("^https?://www.youtube.com/watch.*") && !apiKey.equals(""))
					{
						String videoID = "";
						if(fullHostname.contains("&"))
						{
							videoID = fullHostname.split("watch\\?v=")[1].substring(0, fullHostname.split("watch\\?v=")[1].indexOf("&"));
							System.err.println(videoID);
						}
						else
						{
							videoID = fullHostname.split("watch\\?v=")[1];
						}
						bot.sendMessage(source, "[URLFollow] " + followYouTubeURL(videoID, apiKey));
					}
					else
					{
						bot.sendMessage(source, "[URLFollow] " + followNormalURL(urlstring));
					}
				}
			}
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Looks up and posts the title of a URL when posted.";
	}
	
	public String followNormalURL(String urlString)
	{
		String data = URLUtils.grab(urlString);

		Pattern p = Pattern.compile("<title>(.*?)</title>");
		Matcher m = p.matcher(data);
		if (m.find() == true)
		{
			return "Title: " + m.group(1) + " || At host: " + URLUtils.getHost(urlString);
		}
		return "No title found || At host: " + URLUtils.getHost(urlString);
	}
	
	public String followYouTubeURL(String videoID, String apiKey)
	{
		String urlString = "https://gdata.youtube.com/feeds/api/videos/" + videoID + "?v=2&key=" + apiKey;
		String data = URLUtils.grab(urlString);
	    String[] splitElements = data.split("<");
	      
	    String title = "";
	    String description = "";
	    String duration = "";
	      
	    for(int i = 0; i < splitElements.length; i++)
	    {
	    	if(splitElements[i].matches("^media:title type='plain'.*"))
	    	{
	    		title = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
	    	}
	    	else if(splitElements[i].matches("^media:description type='plain'.*"))
	    	{
	    		description = splitElements[i].substring(splitElements[i].indexOf(">") + 1);
	    		if(description.length() > 149)
	    		{
	    			description = description.substring(0, 147) + "...";
	    		}
	    	}
	    	else if(splitElements[i].matches("^yt:duration seconds.*"))
	    	{
	    		int durationSeconds = Integer.parseInt(splitElements[i].split("'")[1]);
	    		if(durationSeconds >= 3600)
	    		{
	    			duration = ( durationSeconds / 3600 < 10 ? "0": "") + durationSeconds / 3600 +":"+
	    					( (durationSeconds % 3600) / 60 < 10 ? "0": "") + (durationSeconds % 3600) / 60 +":"+
	    					( (durationSeconds % 3600) % 60 < 10 ? "0": "") + (durationSeconds % 3600) % 60;
	    		}
	    		else
	    		{
	    			duration = ( durationSeconds / 60 < 10 ? "0": "") + durationSeconds / 60 +":"+
	    					( durationSeconds % 60 < 10 ? "0": "") + durationSeconds % 60;
	    		}
	    	}
	    }
	    return "Video Title: " + title + " | " + duration + " | " + description;
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