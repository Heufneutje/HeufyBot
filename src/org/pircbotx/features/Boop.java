package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

public class Boop extends Feature
{
	private String[] boops;
	public Boop(HeufyBot bot, String name)
	{
		super(bot, name);
		this.settingsPath = "featuredata/booplist.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = "boop";
		this.messageMustStartWithTrigger = false;
		this.triggerOnAction = true;
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(!boops[0].equals(""))
		{
			int boop = (int) (Math.random() * boops.length);
			bot.sendMessage(source, "[Boop] Boop! " + boops[boop - 1]);
		}
	}

	@Override
	public void onLoad()
	{
		FileUtils.touchFile(settingsPath);
		boops = FileUtils.readFile(settingsPath).split("\n");
	}	

	@Override
	public void onUnload()
	{
	}

	@Override
	public String getHelp()
	{
		return "Commands: None | Greets every user that says hi to the bot.";
	}
}