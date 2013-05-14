package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Join extends Feature
{
  public Join(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "join";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "[Join] Join what?");
    }
    else if (metadata.startsWith(" "))
    {
      this.bot.joinChannel(metadata.substring(1));
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "join <channel> | Makes the bot join a channel.";
	}
}