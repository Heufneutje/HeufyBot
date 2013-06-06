package org.pircbotx.features;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.pircbotx.HeufyBot;

public class Log extends Feature implements Runnable
{
  private String source;
  private String dateString;

  public Log(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "log";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = new Date();
      this.dateString = dateFormat.format(date);

      this.source = source;
      Thread thread = new Thread(this);
      thread.run();
    }
    else if (metadata.startsWith(" "))
    {
    	try
    	{
    		int year = Integer.parseInt(metadata.substring(1, 5));
    		int month = Integer.parseInt(metadata.substring(6, 8));
    		int day = Integer.parseInt(metadata.substring(9));
    		
    		Calendar calendar = new GregorianCalendar(year, month, day);
    		calendar.clear();
    		
    		this.dateString = metadata.substring(1);
    		this.source = source;
    		
    	    Thread thread = new Thread(this);
    	    thread.run();
    		
    	}
    	catch (Exception e)
    	{
    		this.bot.sendMessage(source, "[Log] Parser Error: Make sure the date format for the log is: yyyy-mm-dd");
    	}
    }
  }

  public void run()
  {
    try
    {
    	this.bot.sendMessage(this.source, "[Log] Posting log... Please wait.");
      String log = "";
      File file = new File("logs/" + this.bot.getNetworkName() + "/" + this.source + "/" + this.dateString + ".txt");

      if (file.exists())
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
        String api_paste_name = "Log for " + this.source + " on " + this.dateString;
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
        while ((decodedString = in.readLine()) != null)
        {
          this.bot.sendMessage(this.source, "[Log] Log for " + this.source + " on " + this.dateString + " posted: " + decodedString + " (Link expires in 10 minutes)");
        }
        in.close();
        fileScanner.close();
      }
      else
      {
        this.bot.sendMessage(this.source, "[Log] I do not have that log");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "log, " + bot.getCommandPrefix() + "log <YYYY-MM-DD> | Provides a log of the current channel for today, or another date if specified.";
	}
}