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

  public void process(String source, String metadata, String triggerUser)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "Change my nick to what?");
    }
    else if (metadata.startsWith(" "))
    {
      if (triggerUser.equals("Heufneutje"))
      {
        this.bot.sendRawLine("NICK " + metadata.substring(1));
      }
      else
      {
        this.bot.sendMessage(source, "Only my owner can change my nick!");
      }
    }
  }
}

/* Location:           C:\Users\Stefan\Desktop\No name_01.jar
 * Qualified Name:     features.Nick
 * JD-Core Version:    0.6.0
 */