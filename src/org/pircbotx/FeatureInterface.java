package org.pircbotx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import org.pircbotx.features.Feature;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

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
	        if (feature.getType().equals("join"))
	        {
	        	feature.process(event.getChannel().getName(), "", event.getUser().getNick());
	        }
	    }
  }

  @Override
  public void onMessage(MessageEvent event)
    throws Exception
  {
    for (Feature feature : this.features)
    {
      for (int i = 0; i < feature.getTriggers().length; i++)
      {
        if (feature.getTriggers().length > 0 && !event.getMessage().toLowerCase().startsWith(feature.getTriggers()[i]))
          continue;
        feature.process(event.getChannel().getName(), event.getMessage().substring(feature.getTriggers()[i].length()), event.getUser().getNick());
      }

    }

    if (event.getMessage().startsWith("!load"))
    {
      String metadata = event.getMessage().substring(5);
      String source = event.getChannel().getName();

      if ((metadata.equals("")) || (metadata.equals(" ")))
      {
        this.bot.sendMessage(source, "Load what?");
      }
      else if (metadata.startsWith(" "))
      {
        String featureName = "";
        featureName = featureName + Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);

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
      String featureName;
      if (event.getMessage().startsWith("!unload"))
      {
        String metadata = event.getMessage().substring(7);
        String source = event.getChannel().getName();

        if ((metadata.equals("")) || (metadata.equals(" ")))
        {
          this.bot.sendMessage(source, "Unload what?");
        }
        else if (metadata.startsWith(" "))
        {
          featureName = "";
          featureName = featureName + Character.toUpperCase(metadata.substring(1).toLowerCase().charAt(0)) + metadata.substring(1).toLowerCase().substring(1);

          switch (unloadFeature(featureName)) {
          case 0:
            this.bot.sendMessage(source, "Feature \"" + featureName + "\" was succesfully unloaded!");
            break;
          case 1:
            this.bot.sendMessage(source, "Feature \"" + featureName + "\" is not loaded or does not exist!");
          default:
            break;
          }
        }
      }
      else if (event.getMessage().startsWith("!help"))
      {
        String response = "Features loaded: ";
        for (Feature feature : this.features)
        {
          response = response + feature.getName() + " ";
        }
        this.bot.sendMessage(event.getChannel(), response);
      }
    }
  }

  public int loadFeature(String featureName) {
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
      this.features.remove(this.features.get(i));
      return 0;
    }

    return 1;
  }

  public void loadFeatures(String[] featureNames)
  {
    for (int i = 0; i < featureNames.length; i++)
    {
      loadFeature(featureNames[i]);
    }
  }
  
  public void reloadFeatures()
  {
	  String[] featureNames = new String[features.size()];
	  
	  for(int i = 0; i < features.size(); i++)
	  {
		  featureNames[i] = features.get(i).getName();
		  unloadFeature(features.get(i).getName());
	  }
	  
	  for(int j = 0; j < featureNames.length; j++)
	  {
		  loadFeature(featureNames[j]);
	  }
  }
  
  public void runConnectTriggers()
  {
	  for(Feature feature : features)
	  {
		  feature.connectTrigger();
	  }
  }
}