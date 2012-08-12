package org.pircbotx.features;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.HeufyBot;
import org.pircbotx.User;

public class Mute extends Feature
{
	private String settingsPath = "featuresettings/mute.txt";
	
	public Mute(HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = "!mute";
	}

	@Override
	public void process(String source, String metadata, String triggerUser)
	{
		if ((metadata.equals("")) || (metadata.equals(" ")))
	    {
			this.bot.sendMessage(source, "Mute what?");
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
				
				boolean isAlreadyMuted = false;
				for(String mute : muteList)
				{
					if(mute.equals(user.getHostmask()))
					{
						this.bot.sendMessage(source, "User " + user.getNick() + " is already muted!");
						isAlreadyMuted = true;
					}
				}
				
				if(!isAlreadyMuted)
				{
					muteList.add(user.getHostmask());
					for(Channel channel : user.getChannels())
					{
						bot.setMode(channel, "-v " + user.getNick());
					}
					this.bot.sendMessage(source, "User " + user.getNick() + " was muted.");
					
					String newFile = "";
					for(int i = 0; i < muteList.size(); i++)
					{
						newFile += muteList.get(i) + "\n";
					}
					bot.writeFile(settingsPath, newFile);
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
