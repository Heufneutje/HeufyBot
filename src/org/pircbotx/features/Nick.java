package org.pircbotx.features;

import org.pircbotx.Channel;
import org.pircbotx.HeufyBot;
import org.pircbotx.User;

public class Nick extends Feature
{
  public Nick(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = bot.getCommandPrefix() + "nick";
  }

  public void process(String source, String metadata, String triggerUser, String triggerCommand)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "[Nick] Change my nick to what?");
    }
    else if (metadata.startsWith(" "))
    {
    	User user = bot.getUser(triggerUser);
  	  	Channel channel = bot.getChannel(source);
  	  	
  	  	if(bot.checkAutorization(user, channel))
  	  	{
  	  		this.bot.sendRawLine("NICK " + metadata.substring(1));
  	  	}
  	  	else
  	  	{
  	  		this.bot.sendMessage(source, "[Nick] Only my owner " + bot.getBotOwner() + " and OPs can change my nick!");
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
		return "Commands: " + bot.getCommandPrefix() + "nick <nickname> | Changes the nickname of the bot.";
	}
}