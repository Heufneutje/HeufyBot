package org.pircbotx.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
	
	public static String getFullHostname(String urlString)
	{
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			connection.setInstanceFollowRedirects(false);
			while (connection.getResponseCode() / 100 == 3)
			{
				urlString = connection.getHeaderField("location");
			    connection = (HttpURLConnection) new URL(urlString).openConnection();
			}
			System.out.println(urlString);
			return urlString;
		}
		catch (Exception e)
		{
			return "ERROR";
		}
	}
}