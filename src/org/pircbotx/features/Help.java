package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Collections;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;

public class Help extends Feature
{
	public Help(HeufyBot bot, String name)
	{
		super(bot, name);
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "help";
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
		{
			ArrayList<String> featureNames = new ArrayList<String>();
			for(Feature feature : bot.getFeatureInterface().getFeatures())
			{
				featureNames.add(feature.getName());
			}
			Collections.sort(featureNames);
			String response = "Features loaded: ";
			for (String feature : featureNames)
			{
				response += feature;
				if(!featureNames.get(featureNames.size() - 1).equals(feature))
				{
					response += ", ";
				}
			}
			this.bot.sendMessage(source, response);
		}
		else if (metadata.startsWith(" "))
		{
			String featureName = Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);
			String help = bot.getFeatureInterface().getFeatureHelp(featureName);
			if(help != null)
			{
				this.bot.sendMessage(source, "[Help: " + featureName + "] " + help);
			}
			else
			{
				this.bot.sendMessage(source, "Feature \"" + featureName + "\" is not loaded or does not exist!");
			}
		}
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "say <message> | Makes the bot say the given line.";
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