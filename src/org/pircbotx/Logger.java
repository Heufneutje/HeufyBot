package org.pircbotx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
  
	  public static String read(String filePath)
	  {
		  try
		  {
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(filePath);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine = "";
			  String result = "";
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)
			  {
				  result += strLine + "\n";
			  }
			  //Close the input stream
			  in.close();
			  return result;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}