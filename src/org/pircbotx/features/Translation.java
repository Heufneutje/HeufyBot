package org.pircbotx.features;

import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.memetix.mst.detect.Detect;

public class Translation extends Feature {

	public Translation(HeufyBot bot, String name) 
	{
		super(bot, name);
		this.settingsPath = "featuredata/msazurekey.txt";
	    
		this.triggerType = TriggerType.Message;
		this.authType = AuthType.Anyone;
    
		this.triggers = new String[1];
		this.triggers[0] = bot.getCommandPrefix() + "translate";
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "translate <tolanguage> <sentence>, " + bot.getCommandPrefix() + "translate <fromlanguage>/<tolanguage> <sentence> | Translates a sentence to a different language through Bing Translate.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand) 
	{
		if(metadata.equals("") || metadata.equals(" "))
		{
			this.bot.sendMessage(source, "[Translation] Translate what?");
		}
		else
		{
			metadata = metadata.substring(1);
			String languageParam = "";
			String textToTranslate = "";
			try
			{
				languageParam = metadata.substring(0, metadata.indexOf(" ")).toLowerCase();
				textToTranslate  = metadata.substring(metadata.indexOf(" ") + 1);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				this.bot.sendMessage(source, "[Translation] Error: No text to translate.");
				return;
			}
			
			if(textToTranslate.length() > 149)
			{
				textToTranslate = textToTranslate.substring(0, 147) + "...";
			}
			
			String[] authCredentials = FileUtils.readFile(settingsPath).split("\n");
			
			if(authCredentials.length > 1)
			{
				Translate.setClientId(authCredentials[0]);
				Translate.setClientSecret(authCredentials[1]);
				
				Detect.setClientId(authCredentials[0]);
				Detect.setClientSecret(authCredentials[1]);
			}
			else
			{
				this.bot.sendMessage(source, "[Translation] Error: No MS Azure login credentials were provided.");
				return;
			}
			
			try
			{
				if(languageParam.contains("/") && languageParam.length() == 5)
				{
					String fromLanguage = languageParam.substring(0, 2);
					String toLanguage = languageParam.substring(3, 5);
					System.out.println(fromLanguage + "\n" + toLanguage);
					String translatedText = Translate.execute(textToTranslate, Language.fromString(fromLanguage), Language.fromString(toLanguage));
					bot.sendMessage(source, "[Translation] Source Language: " + fromLanguage + " | " + translatedText);
				}
				else
				{
					Language sourceLanguage = Detect.execute(textToTranslate);
					String translatedText = Translate.execute(textToTranslate, Language.fromString(languageParam));
					bot.sendMessage(source, "[Translation] Source Language: Auto-Detect (" + sourceLanguage.toString() + ") | " + translatedText);
				}			
			} 
			catch (Exception e)
			{
				bot.sendMessage(source, "[Translation] Error: Text could not be translated. Make sure the language code is corrent.");
			}
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