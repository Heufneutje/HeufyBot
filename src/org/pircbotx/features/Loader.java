package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Loader extends Feature
{
	public Loader(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.OPs;
		
		this.triggers = new String[3];
		this.triggers[0] = bot.getCommandPrefix() + "load";
		this.triggers[1] = bot.getCommandPrefix() + "unload";
		this.triggers[2] = bot.getCommandPrefix() + "reload";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(triggerCommand.equals(bot.getCommandPrefix() + "load"))
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
			{
				this.bot.sendMessage(source, "[Loader] Load what?");
			}
			else if (metadata.startsWith(" "))
			{
				String[] features = metadata.substring(1).split(" ");
				for(int i = 0; i < features.length; i++)
				{
					String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
		
					switch (bot.getFeatureInterface().loadFeature(featureName)) 
					{
					case 0:
						this.bot.sendMessage(source, "[Loader] Feature \"" + featureName + "\" was successfully loaded!");
						break;
					case 1:
						this.bot.sendMessage(source, "[Loader] Feature \"" + featureName + "\" is already loaded!");
						break;
					case 2:
						this.bot.sendMessage(source, "[Loader] Feature \"" + featureName + "\" does not exist!");
					default:
						break;
					}
				}
			}
		}
		else if(triggerCommand.equals(bot.getCommandPrefix() + "unload"))
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
			{
				this.bot.sendMessage(source, "[Loader] Unload what?");
			}
			else if (metadata.equals(" *"))
			{
				this.bot.getFeatureInterface().unloadAllFeatures();
				this.bot.sendMessage(source, "[Loader] All features have been unloaded.");
			}
			else if (metadata.startsWith(" "))
			{
				String[] features = metadata.substring(1).split(" ");
				for(int i = 0; i < features.length; i++)
				{
					String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
	
					switch (bot.getFeatureInterface().unloadFeature(featureName)) 
					{
					case 0:
						this.bot.sendMessage(source, "[Loader] Feature \"" + featureName + "\" was successfully unloaded!");
						break;
					case 1:
						this.bot.sendMessage(source, "[Loader] Feature \"" + featureName + "\" is not loaded or does not exist!");
						break;
					default:
						break;
					}
				}
			}
		}
		else
		{
			if ((metadata.equals("")) || (metadata.equals(" ")))
			{
				this.bot.sendMessage(source, "[Loader] Reload what?");
			}
			else if (metadata.startsWith(" "))
			{
				String[] features = metadata.substring(1).split(" ");
				for(int i = 0; i < features.length; i++)
				{
					String featureName = Character.toUpperCase(features[i].toLowerCase().charAt(0)) + features[i].toLowerCase().substring(1);
					
					switch (bot.getFeatureInterface().unloadFeature(featureName)) 
					{
					case 0:
						this.bot.getFeatureInterface().loadFeature(featureName);
						this.bot.sendMessage(source, "Feature \"" + featureName + "\" was successfully reloaded!");
						break;
					case 1:
						this.bot.sendMessage(source, "Feature \"" + featureName + "\" is not loaded or does not exist!");
						break;
					default:
						break;
					}
				}
			}
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "load <feature>, " + bot.getCommandPrefix() + "unload <feature>" + bot.getCommandPrefix() + "reload <feature> | Load, unload or reload one or more features. Seperate feature names by spaces if more.";
	}

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onUnload()
	{	
	}
}