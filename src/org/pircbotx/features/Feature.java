package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.utilities.AuthorizationType;

public abstract class Feature
{
  protected String[] triggers;
  protected HeufyBot bot;
  protected String name;
  protected String featureType = "message";
  protected boolean messageMustStartWithTrigger = true;
  protected AuthorizationType authType = AuthorizationType.Anyone;

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
  
  public AuthorizationType getAuthType()
  {
	  return authType;
  }
  
  public abstract String getHelp();
  
  public abstract void process(String source, String metadata, String triggerUser, String triggerCommand);
  
  public abstract void connectTrigger();
}