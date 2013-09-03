package org.pircbotx.impl;

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
      
      bot.setVersion("HeufyBot IRC Bot V1.5.1 (PircBotX 1.9)");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}