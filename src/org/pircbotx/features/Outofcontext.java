//TODO: Multiline support

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.pircbotx.Colors;
import org.pircbotx.HeufyBot;

public class Outofcontext extends Feature implements Runnable
{
	private String settingsPath = "featuresettings/ooclog.txt";
	private String source;
	
	public Outofcontext(HeufyBot bot, String name) 
	{
		super(bot, name);
	    this.triggers = new String[1];
	    this.triggers[0] = bot.getCommandPrefix() + "ooc";
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "ooc, " + bot.getCommandPrefix() + "ooc add, " + bot.getCommandPrefix() + "ooc search | Grab the OoC log, add an entry to it or search the log by providing a nickname.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(metadata.equals(""))
		{
			this.source = source;
		    Thread thread = new Thread(this);
		    thread.run();
		}
		else
		{
			if(metadata.substring(1).startsWith("add "))
			{
				String newQuote = metadata.substring(5);
				
				DateFormat dateFormat = new SimpleDateFormat("[yyyy/MM/dd]");
			    Date date = new Date();
			    String dateString = dateFormat.format(date);
			    
				if((newQuote.startsWith("<") && newQuote.contains(">")) || newQuote.startsWith("* "))
				{
					DateFormat dateFormat2 = new SimpleDateFormat("[hh:mm]");
				    String timeString = dateFormat2.format(date);
				    String newQuote2 = Colors.removeFormattingAndColors(dateString + " " + timeString + " " + newQuote);
					
					bot.writeFileAppend(settingsPath, newQuote2 + "\n");
					bot.sendMessage(source, "[OutOfContext] Quote was added to the log!");
				}
				else if ((newQuote.startsWith("[") && newQuote.contains("]")) && (newQuote.contains("<") && newQuote.contains(">")) || (newQuote.contains("* ")))
				{
				    String newQuote2 = Colors.removeFormattingAndColors(dateString + " " + newQuote);
					
					bot.writeFileAppend(settingsPath, newQuote2 + "\n");
					bot.sendMessage(source, "[OutOfContext] Quote was added to the log!");
				}
				else
				{
					bot.sendMessage(source, "[OutOfContext] No nickname was found in this quote.");
				}
			}
			else if(metadata.substring(1).startsWith("search "))
			{
				String nick = metadata.substring(8);
				
				String quoteFile = bot.readFile(settingsPath);
				String[] quotes = quoteFile.split("\n");
				ArrayList<String> matches = new ArrayList<String>();
				
				Pattern pattern = Pattern.compile(".*" + nick + ".*", Pattern.CASE_INSENSITIVE);
				
				for(int i = 0; i < quotes.length; i++)
				{
					if(quotes[i].contains("<") && quotes[i].contains(">"))
					{
						quotes[i].substring(quotes[i].indexOf("<"), quotes[i].indexOf(">")).toLowerCase();
						nick.toLowerCase();
						
						//System.err.println(pattern.matcher(quotes[i].substring(quotes[i].indexOf("<"), quotes[i].indexOf(">"))).matches());
						
						if(pattern.matcher(quotes[i].substring(quotes[i].indexOf("<"), quotes[i].indexOf(">"))).matches())
						{
							matches.add(quotes[i]);
						}
					}
					else if(quotes[i].contains("* "))
					{
						//if(quotes[i].substring(quotes[i].indexOf("* ") + 2).split(" ")[0].toLowerCase().contains(nick.toLowerCase()))
						if(pattern.matcher(quotes[i].substring(quotes[i].indexOf("* ") + 2).split(" ")[0]).matches())
						{
							matches.add(quotes[i]);
						}
					}
				}
				
				if(matches.size() == 0)
				{
					bot.sendMessage(source, "[OutOfContext] No matches for '" + nick + "' found.");
				}
				else
				{
					bot.sendMessage(source, "[OutOfContext] Total matched quotes: " + matches.size());
					int quoteID = (int) (Math.random() * matches.size());
					bot.sendMessage(source, "[OutOfContext] " + matches.get(quoteID));
				}
			}
			else if(metadata.substring(1).equalsIgnoreCase("help"))
			{
				bot.sendMessage(source, "[Help: Outofcontext] " + getHelp());
			}
			else
			{
				bot.sendMessage(source, "[OutOfContext] Invalid operation. Please try again.");
			}
		}
	}

	@Override
	public void connectTrigger()
	{
		File file = new File(settingsPath);
		if(!file.exists())
		{
			bot.writeFile(settingsPath, null);
		}
	}
	
	public void run()
	{
		try
	    {
	      String log = "";
	      File file = new File(settingsPath);

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
	        String api_paste_name = "HeufyBot OutOfContext Log";
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
	          this.bot.sendMessage(this.source, "[OutOfContext] OoC Log posted: " + decodedString + " (Link expires in 10 minutes)");
	        }
	        in.close();
	        fileScanner.close();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	}
}
