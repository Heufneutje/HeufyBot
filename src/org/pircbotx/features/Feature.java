package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public abstract class Feature
{
  protected String[] triggers;
  protected HeufyBot bot;
  protected String name;

  public Feature(HeufyBot bot, String name)
  {
    this.bot = bot;
    this.name = name;
  }

  public String[] getTriggers()
  {
    return this.triggers;
  }

  public String getName()
  {
    return this.name;
  }

  public abstract void process(String paramString1, String paramString2, String paramString3);
}