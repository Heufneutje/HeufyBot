package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.*;

public abstract class Feature
{
	protected String[] triggers;
	protected HeufyBot bot;
	protected String name;
	protected String settingsPath;
	protected TriggerType triggerType;
	protected AuthType authType;
	protected boolean messageMustStartWithTrigger = true;
	protected boolean triggerOnAction = false;

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
  
	public TriggerType getTriggerType()
	{
		return triggerType;
	}
  
	public boolean mustStartWithTrigger()
	{
		return messageMustStartWithTrigger;
	}
  
	public AuthType getAuthType()
	{
		return authType;
	}
	
	public String getSettingsPath()
	{
		return settingsPath;
	}
	
	public boolean triggersOnAction()
	{
		return triggerOnAction;
	}
  
	public abstract String getHelp();
  
	public abstract void process(String source, String metadata, String triggerUser, String triggerCommand);
  
	public abstract void onLoad();
	
	public abstract void onUnload();
}