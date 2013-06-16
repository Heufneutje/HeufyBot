package org.pircbotx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

import org.pircbotx.features.Feature;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

@SuppressWarnings("rawtypes")
public class FeatureInterface extends ListenerAdapter implements Listener
{
	private ArrayList<Feature> features = new ArrayList<Feature>();
	private HeufyBot bot;

	public FeatureInterface(HeufyBot bot)
	{
		this.bot = bot;
		File file = new File("featuresettings");
		if(!file.exists())
		{
			file.mkdir();
		}
	}
  
	@Override
	public void onJoin(JoinEvent event)
	{
		for (Feature feature : this.features)
		{
			if (feature.getType() == TriggerType.Join)
			{
				feature.process(event.getChannel().getName(), "", event.getUser().getNick(), "join" + "");
			}
		}
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception
	{
		boolean ignored = false;
		for(String ignore : bot.getIgnoreList())
		{
			if(ignore.equalsIgnoreCase(event.getUser().getNick()))
			{
				ignored = true;
			}
		}
		if(ignored == false)
		{
			for (Feature feature : this.features)
			{
				for (int i = 0; i < feature.getTriggers().length; i++)
				{
					if(feature.mustStartWithTrigger())
					{
						if (feature.getTriggers().length > 0 && !event.getMessage().toLowerCase().startsWith(feature.getTriggers()[i]))
							continue;
						if(feature.getAuthType() == AuthType.OPs)
						{
							if(bot.checkAutorization(event.getUser(), event.getChannel()))
							{
								feature.process(event.getChannel().getName(), event.getMessage().substring(feature.getTriggers()[i].length()), event.getUser().getNick(), feature.getTriggers()[i]);
							}
							else
							{
								this.bot.sendMessage(event.getChannel(), "[" + feature.getName() + "] Only my owner " + bot.getBotOwner() + " and OPs are authorized to use this command!");
							}
						}
						else
						{
							feature.process(event.getChannel().getName(), event.getMessage().substring(feature.getTriggers()[i].length()), event.getUser().getNick(), feature.getTriggers()[i]);
						}
					}
					else
					{
						if (feature.getTriggers().length > 0 && !event.getMessage().toLowerCase().contains(feature.getTriggers()[i]))
							continue;
						feature.process(event.getChannel().getName(), event.getMessage(), event.getUser().getNick(), null);
							break;
					}
				}
			}
			
			if (event.getMessage().toLowerCase().startsWith(bot.getCommandPrefix() + "load"))
			{
				String source = event.getChannel().getName();
				if(bot.checkAutorization(event.getUser(), event.getChannel()))
				{
					String metadata = event.getMessage().substring(5);
	
					if ((metadata.equals("")) || (metadata.equals(" ")))
					{
						this.bot.sendMessage(source, "Load what?");
					}
					else if (metadata.startsWith(" "))
					{
						String featureName = "";
						featureName += Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);
	
						switch (loadFeature(featureName)) {
						case 0:
							this.bot.sendMessage(source, "Feature \"" + featureName + "\" was succesfully loaded!");
							break;
						case 1:
							this.bot.sendMessage(source, "Feature \"" + featureName + "\" is already loaded!");
							break;
						case 2:
							this.bot.sendMessage(source, "Feature \"" + featureName + "\" does not exist!");
						default:
							break;
						}
					}
				}
				else
				{
					this.bot.sendMessage(source, "Only my owner " + bot.getBotOwner() + " and OPs can load features!");
				}
			}
			else
			{
				String featureName;
				if (event.getMessage().toLowerCase().startsWith(bot.getCommandPrefix() + "unload"))
				{
					String source = event.getChannel().getName();
					if(bot.checkAutorization(event.getUser(), event.getChannel()))
					{
						String metadata = event.getMessage().substring(7);
	
						if ((metadata.equals("")) || (metadata.equals(" ")))
						{
							this.bot.sendMessage(source, "Unload what?");
						}
						else if (metadata.equals(" *"))
						{
							int featureCount = features.size();
							for(int i = 0; i < featureCount; i++)
							{
								unloadFeature(features.get(0).getName());
							}
							this.bot.sendMessage(source, "All features have been unloaded.");
						}
						else if (metadata.startsWith(" "))
						{
							featureName = "";
							featureName += Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);
	
							switch (unloadFeature(featureName)) 
							{
							case 0:
								this.bot.sendMessage(source, "Feature \"" + featureName + "\" was succesfully unloaded!");
								break;
							case 1:
								this.bot.sendMessage(source, "Feature \"" + featureName + "\" is not loaded or does not exist!");
								break;
							default:
								break;
							}
						}
					}
					else
					{
						this.bot.sendMessage(source, "Only my owner " + bot.getBotOwner() + " and OPs can unload features!");
					}
				}
				else if (event.getMessage().toLowerCase().startsWith(bot.getCommandPrefix() + "help"))
				{
					String source = event.getChannel().getName();
					String metadata = event.getMessage().substring(5);
    	  
					if ((metadata.equals("")) || (metadata.equals(" ")))
					{
						ArrayList<String> featureNames = new ArrayList<String>();
						for(Feature feature : this.features)
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
						this.bot.sendMessage(event.getChannel(), response);
					}
					else if (metadata.startsWith(" "))
					{
						featureName = Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);
						String help = this.getFeatureHelp(featureName);
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
			}
		}
	}

	public int loadFeature(String featureName) 
	{
		String className = "org.pircbotx.features." + featureName;
		for (int i = 0; i < this.features.size(); i++)
		{
			if (((Feature)this.features.get(i)).getClass().getName().equals(className))
			{
				return 1;
			}
		}
		try
		{
			Constructor<?> ctor = Class.forName(className).getDeclaredConstructors()[0];
			ctor.setAccessible(true);
			Feature feature = (Feature)ctor.newInstance(new Object[] { this.bot, featureName });

			this.features.add(feature);
			feature.onLoad();
		}
		catch (Exception e)
		{
			return 2;
		}
		return 0;
	}

	public int unloadFeature(String featureName)
	{
		String className = "org.pircbotx.features." + featureName;
		for (int i = 0; i < this.features.size(); i++)
		{
			if (!((Feature)this.features.get(i)).getClass().getName().equals(className))
				continue;
			this.features.get(i).onUnload();
			this.features.remove(this.features.get(i));
			return 0;
		}
		return 1;
	}
  
	public String getFeatureHelp(String featureName)
	{
		String className = "org.pircbotx.features." + featureName;
		for (int i = 0; i < this.features.size(); i++)
		{
			if (!((Feature)this.features.get(i)).getClass().getName().equals(className))
				continue;
			return features.get(i).getHelp();
		}
		return null;
	}

	public void loadFeatures(String[] featureNames)
	{
	    for (int i = 0; i < featureNames.length; i++)
	    {
	    	String featureName = Character.toUpperCase(featureNames[i].toLowerCase().charAt(0)) + featureNames[i].toLowerCase().substring(1);
	    	int status = loadFeature(featureName);
	    	if(status == 1)
	    	{
	    		bot.log("Feature '" + featureName + "' is already loaded", "server");
	    	}
	    	else if (status == 2)
	    	{
	    		bot.log("Feature '" + featureName + "' does not exist", "server");
	    	}
	    }
	}
  
	public void reloadFeatures()
	{
		String[] featureNames = new String[features.size()];
	  
		int featureCount = features.size();
		for(int i = 0; i < featureCount; i++)
		{
			featureNames[i] = features.get(0).getName();
			unloadFeature(features.get(0).getName());
		}
	  
		for(int j = 0; j < featureNames.length; j++)
		{
			loadFeature(featureNames[j]);
		}
	}
  
	public void unloadAllFeatures()
	{
		features.clear();
	}
  
	public void runLoads()
	{
		for(Feature feature : features)
		{
			feature.onLoad();
		}
	}
}