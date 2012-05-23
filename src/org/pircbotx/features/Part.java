package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Part extends Feature
{
  public Part(HeufyBot bot, String name)
  {
    super(bot, name);
    this.triggers = new String[1];
    this.triggers[0] = "!part";
  }

  public void process(String source, String metadata, String triggerUser)
  {
    if ((metadata.equals("")) || (metadata.equals(" ")))
    {
      this.bot.sendMessage(source, "Part what?");
    }
    else if (metadata.startsWith(" "))
    {
      this.bot.partChannel(metadata.substring(1));
    }
  }
}

/* Location:           C:\Users\Stefan\Desktop\No name_01.jar
 * Qualified Name:     features.Part
 * JD-Core Version:    0.6.0
 */