package org.pircbotx.features;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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
		    	  this.bot.sendMessage(source, "[URL] Title: " + title + " | At host: " + url.getHost());
		      }
		      else
		      {
		    	  this.bot.sendMessage(source, "[URL] No title found | At host: " + url.getHost());
		      }
		      
		    }
		    catch( Exception e )
		    {
		     	
		    }
		  }
  	}

	@Override
	public void connectTrigger()
	{
	}
}