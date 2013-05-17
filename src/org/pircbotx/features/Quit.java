package org.pircbotx.features;

import org.pircbotx.Channel;
import org.pircbotx.HeufyBot;
import org.pircbotx.User;

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
	  User user = bot.getUser(triggerUser);
	  Channel channel = bot.getChannel(source);
	  if(user.isOped(channel) || triggerUser.equalsIgnoreCase("heufneutje"))
	  {
		  this.bot.quitServer("Killed by " + triggerUser);
	  }
	  else
	  {
		  this.bot.sendMessage(source, "[Quit] Only my owner and OPs can kill me!");
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