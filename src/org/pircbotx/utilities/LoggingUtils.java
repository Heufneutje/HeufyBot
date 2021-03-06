package org.pircbotx.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingUtils
{
	public static void write(String logString, String server, String target)
	{
		if(server != null)
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
	    System.out.print(target + ": " + logString);
	}
	
	public static void writeError(String source, String type, String error)
	{
		File file = new File("logs/");
	    if (!file.exists())
	    {
	    	file.mkdir();
	    }
	    
	    DateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
	    Date date = new Date();
	    
	    System.err.println("ERROR in " + source + " : " + type + " : " + error);
	    FileUtils.writeFileAppend("logs/errorlog.txt", dateFormat.format(date) + " " + source + ": " + error + "\n");
	}
}