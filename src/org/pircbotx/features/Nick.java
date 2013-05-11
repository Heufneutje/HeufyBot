package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Nick extends Feature
{
  public Nick(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!nick";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "[Nick] Change my nick to what?");
    }
    else if (metadata.startsWith(" "))
    {
      if (triggerUser.equals("Heufneutje"))
      {
        this.bot.sendRawLine("NICK " + metadata.substring(1));
      }
      else
      {
        this.bot.sendMessage(source, "[Nick] Only my owner can change my nick!");
      }
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: !nick <nickname> | Changes the nickname of the bot.";
	}
}