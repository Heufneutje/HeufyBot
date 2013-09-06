package org.pircbotx.features;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

public class Tell extends Feature 
{
	private class Message
	{
		public String from;
		public String text;
		public String dateSent;
	}
	private HashMap<String, ArrayList<Message>> tellsMap;
	
	public Tell(HeufyBot bot, String name)
	{
		super(bot, name);
		this.settingsPath = "featuredata/tells.xml";
		
		this.triggerType = TriggerType.Automatic;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[2];
		this.triggers[0] = bot.getCommandPrefix() + "tell";
		this.triggers[1] = bot.getCommandPrefix() + "rtell";
	}

	@Override
	public String getHelp()
	{
		return null;
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		bot.sendMessage(source, triggerCommand);
	}

	@Override
	public void onLoad()
	{
		FileUtils.touchFile(settingsPath);
		readMessages();
	}

	@Override
	public void onUnload()
	{
		writeMessages();
	}
	
	public void readMessages()
	{
		
	}
	
	public void writeMessages()
	{
		
	}
}
