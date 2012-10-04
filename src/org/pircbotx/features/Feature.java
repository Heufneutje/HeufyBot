package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public abstract class Feature
{
  protected String[] triggers;
  protected HeufyBot bot;
  protected String name;
  protected String featureType = "message";
  protected boolean messageMustStartWithTrigger = true;

  public Feature(HeufyBot bot, String name)
  {
    this.bot = bot;
    this.name = name;
    this.triggers = new String[0];
  }

  public String[] getTriggers()
  {
    return this.triggers;
  }

  public String getName()
  {
    return this.name;
  }
  
  public String getType()
  {
	  return featureType;
  }
  
  public boolean mustStartWithTrigger()
  {
	  return messageMustStartWithTrigger;
  }

  public abstract void process(String source, String metadata, String triggerUser);
  
  public abstract void connectTrigger();
}