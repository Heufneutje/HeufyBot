package org.pircbotx.features;

import java.util.Set;

import org.pircbotx.HeufyBot;
import org.pircbotx.User;

public class Autovoice extends Feature
{
	public Autovoice(HeufyBot bot, String name)
	{
		super(bot, name);
		super.featureType = "join";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(triggerUser.equals(bot.getName()))
		{
			bot.setMode(bot.getChannel(source), "+m");
			Set<User> users = bot.getChannel(source).getUsers();
			
			String modeString = "+";
			String usersString = "";
			
			for (User user : users)
			{
				modeString += "v";
				usersString += user.getNick() + " ";
			}
			
			bot.setMode(bot.getChannel(source), modeString + " " + usersString);
		}
		else
		{		
			/*String read = bot.readFile(settingsPath);
			String[] mutes = new String[0];
			if(read != null)
			{
				mutes = read.split("\n");
			}
			
			boolean useVoice = true;
			for(int i = 0; i < mutes.length; i++)
			{
				if(bot.getUser(triggerUser).getHostmask().equals(mutes[i]))
				{
					useVoice = false;
				}
			}
			
			if(useVoice)
			{
				bot.setMode(bot.getChannel(source), "+v " + triggerUser);
			}*/
			bot.setMode(bot.getChannel(source), "+v " + triggerUser);
		}
	}

	@Override
	public void connectTrigger()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Automatically gives everyone who joins the channel voice (+v) and makes the channel moderated (+m).";
	}
}