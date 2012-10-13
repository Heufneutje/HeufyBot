package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Greet extends Feature
{
  public Greet(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[2];
    this.triggers[0] = "hi ";
    this.triggers[1] = "hi,";
    this.messageMustStartWithTrigger = false;
  }

  public void process(String source, String metadata, String triggerUser)
  {
    metadata = metadata.toLowerCase();
    if (metadata.contains(this.bot.getNick().toLowerCase()))
    {
      this.bot.sendMessage(source, "Hi, " + triggerUser + "!");
    }
  }

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Greets every user that says hi to the bot.";
	}
}