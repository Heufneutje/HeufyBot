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
      HeufyBot bot = new HeufyBot();
      bot.setVersion("HeufyBot IRC Bot V2.0.0 (PircBotX 1.6)");
      bot.getFeatureInterface().loadFeatures(new String[] {"Greet", "Log", "Join", "Nick", "Part", "Say", "Source", "Time", "Urlfollow"});
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}