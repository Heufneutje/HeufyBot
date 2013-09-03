package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translationparty extends Feature
{
	private String sourceChannel;
	private String textToTranslate;

	public Translationparty(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/msazurekey.txt";
	    
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
    
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "tp";
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "tp <sentence> | Follow translationparty.com to get a new sentence";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			this.bot.sendMessage(source, "[TranslationParty] Translate what?");
		}
		else
		{
			metadata = metadata.substring(1);
			this.sourceChannel = source;
			textToTranslate  = metadata.substring(metadata.indexOf(" ") + 1);
			if(textToTranslate.length() > 99)
			{
				textToTranslate = textToTranslate.substring(0, 99);
			}
			
			String[] authCredentials = FileUtils.readFile(settingsPath).split("\n");
			try
			{
				Translate.setClientId(authCredentials[0]);
				Translate.setClientSecret(authCredentials[1]);
			}
			catch(IndexOutOfBoundsException e)
			{
				this.bot.sendMessage(source, "[TranslationParty] Error: No MS Azure login credentials were provided.");
				return;
			}
			Thread thread = new Thread()
			{
				public void run()
				{
					try
					{
						String newText = textToTranslate;
						String lastEnglishEntry = textToTranslate;
						for(int tries = 0; tries < 21; tries++)
						{
							if(newText.length() > 99)
							{
								newText = newText.substring(0, 99);
							}
							newText = Translate.execute(newText, Language.ENGLISH, Language.JAPANESE);
							if(newText.length() > 99)
							{
								newText = newText.substring(0, 99);
							}
							newText = Translate.execute(newText, Language.JAPANESE, Language.ENGLISH);
							if(newText.equals(lastEnglishEntry))
							{
								bot.sendMessage(sourceChannel, "[TranslationParty] " + newText + " | Steps: " + tries);
								return;
							}
							else
							{
								lastEnglishEntry = newText;
							}
						}
						bot.sendMessage(sourceChannel, "[TranslationParty] " + newText + " | Steps: 20+");
					}
					catch (Exception e)
					{
						bot.sendMessage(sourceChannel, "[TranslationParty] Error: Text could not be translated.");
					}
				}
			};
			thread.start();
		} 
	}

	@Override
	public void onLoad()
	{
		FileUtils.touchFile(settingsPath);
	}

	@Override
	public void onUnload()
	{
	}
}