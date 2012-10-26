package org.pircbotx.features;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import org.pircbotx.HeufyBot;

public class Urlfollow extends Feature
{
  public Urlfollow(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[2];
    this.triggers[0] = "http://";
    this.triggers[1] = "https://";
    this.messageMustStartWithTrigger = false;
  }

  public void process(String source, String metadata, String triggerUser)
  {
	  String[] message = metadata.split(" ");
	  String[] urls = new String[message.length];
	  for(int i = 0; i < message.length; i++)
	  {
		  if(message[i].startsWith("http"))
		  {
			  urls[i] = message[i];
		  }
	  }
	  
	  for(int i = 0; i < urls.length; i++)
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
		    
		    try
		    {
		      //open file
		    	URL url = new URL(urls[i]);
		      bufReader = new BufferedReader( new InputStreamReader(url.openStream()));
		      
		      //read line by line
		      line = bufReader.readLine();
		      System.out.println(line);
		      while( (line = bufReader.readLine()) != null && !foundEndTag)
		      {
		        //System.out.println(line);
		      
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
		    	  this.bot.sendMessage(source, "[URLFollow] Title: " + title + " || At host: " + url.getHost());
		      }
		      else
		      {
		    	  this.bot.sendMessage(source, "[URLFollow] No title found | At host: " + url.getHost());
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
		    	e3.printStackTrace();
		    	this.bot.sendMessage(source, "[URLFollow] Error: " + e3.getMessage() + ".");
		    }
		  }
  	}

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Looks up and posts the title of a URL when posted.";
	}
}