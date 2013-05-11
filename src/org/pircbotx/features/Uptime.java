package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.pircbotx.HeufyBot;

public class Uptime extends Feature
{
	Date date;
	
  public Uptime(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!time";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    DateFormat format = new SimpleDateFormat("MM-dd-YYYY hh:mm aa (z)");
    this.bot.sendMessage(source, "[Uptime] HeufyBot has been running since " + format.format(date) + ".");
  }

	@Override
	public void connectTrigger()
	{
		date = new Date();
	}

	@Override
	public String getHelp() 
	{
		return "Commands: !uptime | Shows how long the bot has been running.";
	}
}