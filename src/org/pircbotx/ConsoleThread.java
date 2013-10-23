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
		    			bot.shutdown(true);
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
		    	else if(command.startsWith("join "))
		    	{
		    		command = command.substring(5);
		    		bot.joinChannel(command.split(" ")[0]);
		    	} 
		    	else if(command.startsWith("part "))
		    	{
		    		command = command.substring(5);
		    		bot.partChannel(command.split(" ")[0]);
		    	}
		    	else if(command.startsWith("load "))
		    	{
		    		if(bot.isConnected())
		    		{
			    		command = command.substring(5);
			    		String[] features = command.split(" ");
						for(int i = 0; i < features.length; i++)
						{
							String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
				
							switch (bot.getFeatureInterface().loadFeature(featureName)) 
							{
							case 0:
								System.out.println("Feature \"" + featureName + "\" was successfully loaded!");
								break;
							case 1:
								System.out.println("Feature \"" + featureName + "\" is already loaded!");
								break;
							case 2:
								System.out.println("Feature \"" + featureName + "\" does not exist!");
							default:
								break;
							}
						}
		    		}
		    	}
		    	else if(command.startsWith("unload "))
		    	{
		    		command = command.substring(7);
		    		String[] features = command.split(" ");
					for(int i = 0; i < features.length; i++)
					{
						String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
		
						switch (bot.getFeatureInterface().unloadFeature(featureName)) 
						{
						case 0:
							System.out.println("Feature \"" + featureName + "\" was successfully unloaded!");
							break;
						case 1:
							System.out.println("Feature \"" + featureName + "\" is not loaded or does not exist!");
							break;
						default:
							break;
						}
					}
		    	}
		    	else if(command.startsWith("reload "))
		    	{
		    		command = command.substring(7);
		    		String[] features = command.split(" ");
					for(int i = 0; i < features.length; i++)
					{
						String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
		
						switch (bot.getFeatureInterface().unloadFeature(featureName)) 
						{
						case 0:
							this.bot.getFeatureInterface().loadFeature(featureName);
							System.out.println("Feature \"" + featureName + "\" was successfully reloaded!");
							break;
						case 1:
							System.out.println("Feature \"" + featureName + "\" is not loaded or does not exist!");
							break;
						default:
							break;
						}
					}
		    	}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	    }
	}
}