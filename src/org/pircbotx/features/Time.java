package org.pircbotx.features;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.pircbotx.HeufyBot;

public class Time extends Feature
{
  public Time(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!time";
  }

  public void process(String source, String metadata, String triggerUser)
  {
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("hh:mm aa (z)");
    this.bot.sendMessage(source, "[Time] The current time is " + format.format(date) + ".");
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp() 
	{
		return "Commands: !time | Displays the time of the computer the bot is currently running on.";
	}
}