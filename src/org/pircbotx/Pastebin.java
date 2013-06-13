package org.pircbotx;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class Pastebin
{
	public static String post(final String filePath, final String title)
	{
		try
	    {
	      String log = "";
	      File file = new File(filePath);
	      
	      if(file.exists())
	      {
	        Scanner fileScanner = new Scanner(file);

	        while (fileScanner.hasNextLine())
	        {
	          log = log + fileScanner.nextLine() + "\n";
	        }

	        URL url = new URL("http://pastebin.com/api/api_post.php");
	        URLConnection connection = url.openConnection();
	        connection.setDoOutput(true);

	        String api_dev_key = "103e700947c8d6782b3fb99c85ae4d9f";
	        String api_option = "paste";
	        String api_paste_code = log;

	        String api_user_key = "";
	        String api_paste_name = title;
	        String api_paste_format = "text";
	        String api_paste_private = "1";
	        String api_paste_expire_date = "10M";

	        String postData = URLEncoder.encode("api_dev_key", "UTF8") + "=" + URLEncoder.encode(api_dev_key, "UTF8") + "&" + 
	          URLEncoder.encode("api_option", "UTF8") + "=" + URLEncoder.encode(api_option, "UTF8") + "&" + 
	          URLEncoder.encode("api_paste_code", "UTF8") + "=" + URLEncoder.encode(api_paste_code, "UTF8") + "&" + 
	          URLEncoder.encode("api_user_key", "UTF8") + "=" + URLEncoder.encode(api_user_key, "UTF8") + "&" + 
	          URLEncoder.encode("api_paste_name", "UTF8") + "=" + URLEncoder.encode(api_paste_name, "UTF8") + "&" + 
	          URLEncoder.encode("api_paste_format", "UTF8") + "=" + URLEncoder.encode(api_paste_format, "UTF8") + "&" + 
	          URLEncoder.encode("api_paste_private", "UTF8") + "=" + URLEncoder.encode(api_paste_private, "UTF8") + "&" + 
	          URLEncoder.encode("api_paste_expire_date", "UTF8") + "=" + URLEncoder.encode(api_paste_expire_date, "UTF8");

	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	        out.write(postData);
	        out.close();

	        BufferedReader in = new BufferedReader(
	          new InputStreamReader(
	          connection.getInputStream()));
	        String decodedString;
	        String result = "";
	        while ((decodedString = in.readLine()) != null)
	        {
	        	result += decodedString;
	          //this.bot.sendMessage(this.source, "[OutOfContext] OoC Log posted: " + decodedString + " (Link expires in 10 minutes)");
	        }
	        in.close();
	        fileScanner.close();
	        System.out.println(result);
	        return result;
	      }
	      else
	      {
	    	  return "NotFound";
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
		return null;
	}	
}