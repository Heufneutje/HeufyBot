package org.pircbotx.features;

import org.pircbotx.HeufyBot;

public class Autovoice extends Feature
{
	private String settingsPath = "featuresettings/mute.txt";
	
	public Autovoice(HeufyBot bot, String name)
	{
		super(bot, name);
		super.featureType = "join";
	}

	@Override
	public void process(String source, String metadata, String triggerUser) 
	{
		String read = bot.readFile(settingsPath);
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
		}
	}

	@Override
	public void connectTrigger()
	{
	}
}