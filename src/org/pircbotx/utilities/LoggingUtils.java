package org.pircbotx.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingUtils
{
	public static void write(String logString, String server, String target)
	{
		if(!target.contains("#"))
		{
			target = target.replaceAll("[^a-zA-Z0-9]+","");
		}
		
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

	    FileUtils.writeFileAppend("logs/" + server + "/" + target + "/" + dateFormat.format(date) + ".txt", logString);
	}
  
	public static String read(String filePath)
	{
		return FileUtils.readFile(filePath);
	}
}