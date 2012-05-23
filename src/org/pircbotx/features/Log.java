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
import java.util.Date;
import java.util.Scanner;
import org.pircbotx.HeufyBot;

public class Log extends Feature
  implements Runnable
{
  private String source;
  private String dateString;

  public Log(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!log";
  }

  public void process(String source, String metadata, String triggerUser)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
      Date date = new Date();
      this.dateString = dateFormat.format(date);

      this.source = source;
      Thread thread = new Thread(this);
      thread.run();
    }
    else if (metadata.startsWith(" "))
    {
      this.bot.sendMessage(source, "This does not work yet, because Heufy is lazy");
    }
  }

  public void run()
  {
    try
    {
      String log = "";
      File file = new File("logs/" + this.bot.getServer() + "/" + this.source + "/" + this.dateString + ".txt");

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
          this.bot.sendMessage(this.source, "Log for " + this.source + " on " + this.dateString + " posted: " + decodedString + " (Link expires in 10 minutes)");
        }
        in.close();
      }
      else
      {
        this.bot.sendMessage(this.source, "I do not have that log");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

/* Location:           C:\Users\Stefan\Desktop\No name_01.jar
 * Qualified Name:     features.Log
 * JD-Core Version:    0.6.0
 */