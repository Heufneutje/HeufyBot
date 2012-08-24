package org.pircbotx.features.wip;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.HeufyBot;
import org.pircbotx.User;
import org.pircbotx.features.Feature;

public class Unmute extends Feature
{
	private String settingsPath = "featuresettings/mute.txt";
	
	public Unmute(HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = "!unmute";
	}

	@Override
	public void process(String source, String metadata, String triggerUser)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
	    {
			this.bot.sendMessage(source, "Unmute what?");
	    }
	    else if (metadata.startsWith(" "))
	    {
			User user = bot.getUser(metadata.substring(1));
			if(!user.getHostmask().equals(""))
			{
				String read = bot.readFile(settingsPath);
				String[] mutes = new String[0];
				if(read != null)
				{
					mutes = read.split("\n");
				}
				ArrayList<String> muteList = new ArrayList<String>();
				for(int i = 0; i < mutes.length; i++)
				{
					muteList.add(mutes[i]);
				}
				
				boolean unmuted = false;
				for(int i = 0; i < muteList.size(); i++)
				{
					if(muteList.get(i).equals(user.getHostmask()))
					{
						this.bot.sendMessage(source, "User " + user.getNick() + " was unmuted.");
						muteList.remove(i);
						for(Channel channel : user.getChannels())
						{
							bot.setMode(channel, "+v " + user.getNick());
						}
						String newFile = "";
						for(int j = 0; j < muteList.size(); j++)
						{
							newFile += muteList.get(j) + "\n";
						}
						bot.writeFile(settingsPath, newFile);
						unmuted = true;
					}
				}
				
				if(!unmuted)
				{
					this.bot.sendMessage(source, "User " + user.getNick() + " was not muted!");
				}
			}
			else
			{
				this.bot.sendMessage(source, "User " + user.getNick() + " does not exist!");
			}
	    }
	}

	@Override
	public void connectTrigger()
	{
	}
	
}
