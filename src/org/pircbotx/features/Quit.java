package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Quit extends Feature
{
  public Quit(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "quit";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
	  try
	  {
			this.bot.quitServer("Killed by " + triggerUser);
			this.bot.shutdown(true);
			Thread.sleep(100);
			System.exit(0);
	  } 
	  catch (InterruptedException e) 
	  {
			e.printStackTrace();
	  }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "quit | Makes the bot quit the server.";
	}
}