package org.pircbotx.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.UIManager;
import org.pircbotx.HeufyBot;

public class HeufyBotStarter
{
  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      HeufyBot bot;
      if(args.length > 0)
      {
    	  if(args[0].equals("nogui"))
    	  {
    		  bot = new HeufyBot(false);
    	  }
    	  else
    	  {
    		  bot = new HeufyBot(true);
    	  }
      }
      else
      {
    	  bot = new HeufyBot(true);
      }
      
      bot.setVersion("HeufyBot IRC Bot V1.1.0 (PircBotX 1.9)");
      bot.getFeatureInterface().loadFeatures(new String[] {"Log", "Say", "Source", "Urlfollow", "Join", "Part", "Greet", "Event", "Uptime"});
      
      while(true)
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
    		      bot.disconnect();
    		  }
    	  }
    	  else if(command.equals("quit"))
    	  {
    		  bot.quitServer();
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
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}