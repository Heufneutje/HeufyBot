package org.pircbotx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleThread extends Thread
{
	private HeufyBot bot;
	
	public ConsoleThread(HeufyBot bot)
	{
		this.bot = bot;
	}
	
	public void run()
	{
		while(true)
	    {
			try
			{
		    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    	String command = br.readLine();
		    	if(command.equals("connect"))
		    	{
		    		bot.connectWithSettings("settings.xml");
		    	}
		    	else if(command.equals("disconnect"))
		    	{
		    		if (bot.isConnected())
		    		{
		    		    bot.quitServer();
		    		}
		    	}
		    	else if(command.equals("quit"))
		    	{
		    		if(bot.isConnected())
		    		{
		    			bot.quitServer();
		    		}
		    		Thread.sleep(1000);
		    		System.exit(0);
		    	}
		    	else if(command.startsWith("msg "))
		    	{
		    		command = command.substring(4);
		    		String destination = command.split(" ")[0];
		    		String message = command.substring(destination.length() + 1);
		    		bot.sendMessage(destination, message);
		    	} 
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	    }
	}
}