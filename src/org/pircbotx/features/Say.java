package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Say extends Feature
{
  public Say(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!say";
  }

  public void process(String source, String metadata, String triggerUser)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "Say what?");
    }
    else if (metadata.startsWith(" "))
    {
      this.bot.sendMessage(source, metadata.substring(1));
    }
  }

	@Override
	public void connectTrigger()
	{
	}
}