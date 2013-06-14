package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.AuthorizationType;

public class Nick extends Feature
{
  public Nick(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "nick";
    this.authType = AuthorizationType.OPs;
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "[Nick] Change my nick to what?");
    }
    else if (metadata.startsWith(" "))
    {
    	bot.sendRawLine("NICK " + metadata.substring(1));
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "nick <nickname> | Changes the nickname of the bot.";
	}
}