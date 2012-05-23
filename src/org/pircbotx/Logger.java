package org.pircbotx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
  public static void write(String logString, String server, String target)
  {
    try
    {
      File file = new File("logs/");
      if (!file.exists())
      {
        file.mkdir();
      }
      file = new File("logs/" + server);
      if (!file.exists())
      {
        file.mkdir();
      }
      file = new File("logs/" + server + "/" + target);
      if (!file.exists())
      {
        file.mkdir();
      }

      DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
      Date date = new Date();

      FileWriter writer = new FileWriter("logs/" + server + "/" + target + "/" + dateFormat.format(date) + ".txt", true);
      BufferedWriter bw = new BufferedWriter(writer);
      bw.write(logString);
      bw.flush();
      bw.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

/* Location:           C:\Users\Stefan\Desktop\No name_01.jar
 * Qualified Name:     Logger
 * JD-Core Version:    0.6.0
 */