package org.pircbotx.features;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

public class Responses extends Feature
{
	private String[] boops;
	private boolean cooldown = true;
	private Timer timer;
	
	public Responses(HeufyBot bot, String name)
	{
		super(bot, name);
		this.settingsPath = "featuredata/booplist.txt";
		
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[1];
		this.triggers[0] = "boop";
		this.messageMustStartWithTrigger = false;
		this.triggerOnAction = true;
		
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				cooldown = true;
				timer.stop();
			}
		  };
		this.timer = new Timer(60000, taskPerformer);
	}

	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(cooldown)
		{
			if(!boops[0].equals(""))
			{
				int boop = (int) (Math.random() * boops.length);
				bot.sendMessage(source, "[Responses] Boop! " + boops[boop - 1]);
			}
			cooldown = false;
			timer.start();
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
		return "Commands: None | Gives a reaction based on what is said in the channel.";
	}
}