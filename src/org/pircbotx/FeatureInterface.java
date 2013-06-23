package org.pircbotx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.pircbotx.features.Feature;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

@SuppressWarnings("rawtypes")
public class FeatureInterface extends ListenerAdapter implements Listener
{
	private ArrayList<Feature> features = new ArrayList<Feature>();
	private HeufyBot bot;

	public FeatureInterface(HeufyBot bot)
	{
		this.bot = bot;
		File file = new File("featuredata");
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
	
	public void onPrivateMessage(PrivateMessageEvent event)
	{
		handleMessage(event.getMessage(), event.getUser(), null, TriggerType.PM);
	}
	
	public void onAction(ActionEvent event)
	{
		handleMessage(event.getMessage(), event.getUser(), event.getChannel(), TriggerType.Action);
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception
	{
		handleMessage(event.getMessage(), event.getUser(), event.getChannel(), TriggerType.Message);
	}
	
	private void handleMessage(String message, User user, Channel channel, TriggerType triggerType)
	{
		boolean ignored = false;
		for(String ignore : bot.getIgnoreList())
		{
			if(ignore.equalsIgnoreCase(user.getNick()))
			{
				ignored = true;
			}
		}
		if(ignored == false)
		{
			if (message.toLowerCase().startsWith(bot.getCommandPrefix() + "load"))
			{
				String source = "";
				boolean isPM = false;
				if(triggerType == TriggerType.PM)
				{
					source = user.getNick();
					isPM = true;
				}
				else
				{
					source = channel.getName();
				}
				if(checkAutorization(user, channel, AuthType.OPs, isPM))
				{
					String metadata = message.substring(5);
	
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
							this.bot.sendMessage(source, "Feature \"" + featureName + "\" was successfully loaded!");
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
					this.bot.sendMessage(source, "Only my owners and OPs can load features!");
				}
			}
			else if (message.toLowerCase().startsWith(bot.getCommandPrefix() + "unload"))
			{
				String source = "";
				boolean isPM = false;
				if(triggerType == TriggerType.PM)
				{
					source = user.getNick();
					isPM = true;
				}
				else
				{
					source = channel.getName();
				}
				if(checkAutorization(user, channel, AuthType.OPs, isPM))
				{
					String metadata = message.substring(7);

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
						String featureName = "";
						featureName += Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);

						switch (unloadFeature(featureName)) 
						{
						case 0:
							this.bot.sendMessage(source, "Feature \"" + featureName + "\" was successfully unloaded!");
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
					this.bot.sendMessage(source, "Only my owner and OPs can unload features!");
				}
			}
			else
			{
				String source = "";
				boolean isPM = false;
				if(triggerType == TriggerType.PM)
				{
					source = user.getNick();
					isPM = true;
				}
				else
				{
					source = channel.getName();
				}
				for (Feature feature : this.features)
				{
					for (int i = 0; i < feature.getTriggers().length; i++)
					{
						if(feature.mustStartWithTrigger() && (feature.triggersOnAction() && triggerType == TriggerType.Action || triggerType != TriggerType.Action))
						{
							if (feature.getTriggers().length > 0 && !message.toLowerCase().startsWith(feature.getTriggers()[i]))
								continue;
							if(checkAutorization(user, channel, feature.getAuthType(), isPM))
							{
								feature.process(source, message.substring(feature.getTriggers()[i].length()), user.getNick(), feature.getTriggers()[i]);
							}
							else
							{
								if(feature.getAuthType() == AuthType.OPs)
								{
									this.bot.sendMessage(source, "[" + feature.getName() + "] Only my owners and OPs are authorized to use this command!");
								}
								else if(feature.getAuthType() == AuthType.Owners)
								{
									this.bot.sendMessage(source, "[" + feature.getName() + "] Only my owners are authorized to use this command!");
								}
							}
						}
						else
						{
							if (feature.getTriggers().length > 0 && !message.toLowerCase().contains(feature.getTriggers()[i]))
								continue;
							feature.process(source, message, user.getNick(), null);
								break;
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
	
	public ArrayList<Feature> getFeatures()
	{
		return features;
	}
	
	public boolean checkAutorization(User user, Channel channel, AuthType authType, boolean isPM)
	{
		if(isPM)
		{
			if(authType == AuthType.Anyone || bot.getOwnerList().size() == 0)
			{
				return true;
			}
		}
		else
		{
			if(authType == AuthType.Anyone || bot.getOwnerList().size() == 0 || (user.isOped(channel) && authType == AuthType.OPs))
			{
				return true;
			}
		}
		if(bot.useNickServ)
		{
			if(user.isVerified())
			{
				for(String owner : bot.getOwnerList())
				{
					if(user.getAccountName().equalsIgnoreCase(owner))
					{
						return true;
					}
				}
				return false;
			}
			else
			{
				return false;
			}
		}
		else
		{
			for(String owner : bot.getOwnerList())
			{
				if(user.getNick().equalsIgnoreCase(owner))
				{
					return true;
				}
			}
			return false;
		}
	}
}