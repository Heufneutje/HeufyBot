package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Part extends Feature
{
  public Part(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "part";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "[Part] Part what?");
    }
    else if (metadata.startsWith(" "))
    {
      this.bot.partChannel(metadata.substring(1));
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "part <channel> | Makes the bot leave a channel.";
	}
}