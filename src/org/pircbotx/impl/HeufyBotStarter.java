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
      HeufyBot bot = new HeufyBot();
      bot.setVersion("HeufyBot IRC Bot V2.0.0 (PircBotX 1.6)");
      bot.getFeatureInterface().loadFeatures(new String[] {"Log", "Say", "Source", "Urlfollow", "Join", "Part", "Greet"});
      
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
    		  bot.disconnect();
    	  }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}