package org.pircbotx.features;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.URLUtils;

public class Urlfollow extends Feature
{
  public Urlfollow(HeufyBot bot, String name)
  {
	  super(bot, name);
	  
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
		  if(message[i].toLowerCase().startsWith("http"))
		  {
			  urls.add(message[i]);
		  }
	  }
	  
	  while(urls.size() > 3)
	  {
		  urls.remove(urls.size() - 1);
	  }
	  
	  for(String urlstring : urls)
	  {		    
		  try
		  {
			  if(!urlstring.matches(".*(jpe?g|gif|png|bmp)"))
			  {
				  String fullHostname = URLUtils.getFullHostname(urlstring);
				  if(fullHostname.contains("http://www.youtube.com/watch"))
				  {
					  String videoID = "";
					  if(fullHostname.contains("&"))
					  {
						  videoID = fullHostname.split("watch\\?v=")[1].substring(0, fullHostname.split("watch\\?v=")[1].indexOf("&"));
					  }
					  else
					  {
						  videoID = fullHostname.split("watch\\?v=")[1];
					  }
					  bot.sendMessage(source, "[URLFollow] " + followYouTubeURL(videoID));
				  }
				  else
				  {
					  bot.sendMessage(source, "[URLFollow] " + followNormalURL(urlstring));
				  }
			  }
		  }
		  catch(IllegalArgumentException e)
		  {
			  this.bot.sendMessage(source, "[URLFollow] Error: Not a valid URL");
		  }
		  catch(UnknownHostException e1)
		  {
			  this.bot.sendMessage(source, "[URLFollow] Error: Not a valid URL. Host " + e1.getMessage() + " was not found.");
		  }
		  catch(FileNotFoundException e2)
		  {
			  this.bot.sendMessage(source, "[URLFollow] Error: Not a valid URL");
		  }
		  catch(IOException e3)
		  {
			  this.bot.sendMessage(source, "[URLFollow] Error: Not a valid URL");
		  }
	  }
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Looks up and posts the title of a URL when posted.";
	}
	
	public String followNormalURL(String urlString) throws IllegalArgumentException, UnknownHostException, FileNotFoundException, IOException
	{
		String startTag = "<title>";
	  	String endTag = "</title>";
	  	int startTagLength = startTag.length();
	    BufferedReader bufReader;
	    String line;
	    boolean foundStartTag = false;
	    boolean foundEndTag = false;
	    int startIndex, endIndex;
	    String title = "";
	    
		URL url = new URL(urlString);
	      bufReader = new BufferedReader( new InputStreamReader(url.openStream()));
	      
	      while( (line = bufReader.readLine()) != null && !foundEndTag)
	      {
	        //search for title start tag (convert to lower case before searhing)
	        if( !foundStartTag && (startIndex = line.toLowerCase().indexOf(startTag)) != -1 )
	        {
	          foundStartTag = true;
	        }
	        else
	        {
	          //else copy from start of string
	          startIndex = -startTagLength;
	        }
	        
	        //search for title start tag (convert to lower case before searhing)
	        if( foundStartTag && (endIndex = line.toLowerCase().indexOf(endTag)) != -1 )
	        {
	          foundEndTag = true;
	        }
	        else
	        {
	          //else copy to end of string
	          endIndex = line.length();
	        }
	        
	        //extract title field
	        if( foundStartTag || foundEndTag )
	        {       
	          title += line.substring( startIndex + startTagLength, endIndex );
	        }
	      }
	      
	      //close the file when finished
	      bufReader.close();
	      
	      //output the title
	      if( title.length() > 0 )
	      {
	    	  return "Title: " + title + " || At host: " + url.getHost();
	      }
	      else
	      {
	    	 return "No title found | At host: " + url.getHost();
	      }
	}
	
	public String followYouTubeURL(String videoID) throws IOException
	{
		 String urlString = "https://gdata.youtube.com/feeds/api/videos/" + videoID + "?v=2&key=AIzaSyBqSIpLIvf4r2CNKa7xAAxe1sDB3DEfyOk";
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