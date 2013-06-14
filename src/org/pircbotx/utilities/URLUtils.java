package org.pircbotx.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLUtils
{
	public static String grab(String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			BufferedReader bufReader;
			String line;
			String data = "";
		    bufReader = new BufferedReader( new InputStreamReader(url.openStream()));
		     
		    //read line by line
		    while( (line = bufReader.readLine()) != null)
		    {
		    	data += line + " ";
		    }
		    bufReader.close();
		    return data;
		}
		catch(Exception e)
		{
			return "ERROR";
		}
	}
}