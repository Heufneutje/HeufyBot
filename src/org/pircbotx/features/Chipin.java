package org.pircbotx.features;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.Channel;
import org.pircbotx.HeufyBot;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Chipin extends Feature
{
	private String settingsPath = "featuresettings/chipin.xml";
	private String rssFeed;
	private double donationGoal;
	private int previousEntriesCount;
	private String message;
	private String currentTotal;
	
	public Chipin(final HeufyBot bot, String name)
	{
		super(bot, name);
		this.triggers = new String[1];
		this.triggers[0] = ".donations";
	}
	
	public void readSettingsFile()
	{
		File file = new File(settingsPath);
		if(!file.exists())
		{
			bot.writeFile(settingsPath, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><settings><rssfeed>http://n64galore.chipin.com/rss/id/2a38e46718a1039b</rssfeed><donationgoal>1000</donationgoal><message>Keep donating at http://stream.tbpcullman.com/ to raise money for Make-A-Wish!</message></settings>");
		}
		
		try 
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(settingsPath);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("settings");
			
			Node nNode = nodeLst.item(0);
	        Element eElement = (Element)nNode;
	        rssFeed = getTagValue("rssfeed", eElement);
	        donationGoal = Double.parseDouble(getTagValue("donationgoal", eElement));
	        message = getTagValue("message", eElement);
	        
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private static String getTagValue(String sTag, Element eElement)
	  {
	    try
	    {
	      NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	      Node nValue = nlList.item(0);
	      return nValue.getNodeValue();
	    }
	    catch (NullPointerException e)
	    {
	    	return "";
	    }
	  }

	@Override
	public void process(String source, String metadata, String triggerUser) 
	{
		int percentage = (int) (Double.parseDouble(currentTotal.substring(1)) / donationGoal * 100);
		bot.sendMessage(source, "We are at " + currentTotal + " of $" + new DecimalFormat("#.##").format(donationGoal) + " (" + percentage + "% of our goal)! " + message);
	}
	
	public int countRssEntries(String path)
	{
		try 
		{
			URL url = new URL(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("item");
			  
			return nodeLst.getLength();
		}
		catch (Exception e)
		{
		    return 0;
		}
	}
	
	public String readLastEntry(String path)
	{
		try 
		{
			URL url = new URL(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("item");
			  
			Node fstNode = nodeLst.item(nodeLst.getLength() - 1);	    
	    	Element fstElmnt = (Element) fstNode;
		    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("title");
		    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		    NodeList fstNm = fstNmElmnt.getChildNodes();
		    return ((Node) fstNm.item(0)).getNodeValue();	
		}
		catch (Exception e)
		{
		    return null;
		}
	}

	@Override
	public void connectTrigger()
	{
		readSettingsFile();
		
		this.previousEntriesCount = this.countRssEntries(rssFeed);
		String current = this.readLastEntry(rssFeed);
		currentTotal = current.substring(current.indexOf("(") + 1).split(" so")[0];;
		
		if(current.contains("This event has started"))
		{
			currentTotal = "$0.00";
		}
		if(current.contains("This event has ended"))
		{
			if(bot.isConnected())
			{
				for(Channel channel : bot.getChannels())
				{
					bot.sendMessage(channel, "[Chipin] This event has ended");
				}
			}
		}
		else
		{
			int delay = 60000; //milliseconds
			  ActionListener taskPerformer = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int currentEntries = countRssEntries(rssFeed);
					String newEntry = "";
					
					if(currentEntries > previousEntriesCount)
					{
						previousEntriesCount = currentEntries;
						newEntry = readLastEntry(rssFeed);
						String donation = newEntry.split(" was")[0];
						int donationTotalLength = newEntry.indexOf("(") + 1;
						currentTotal = newEntry.substring(donationTotalLength).split(" so")[0];
						int percentage = (int) (Double.parseDouble(currentTotal.substring(1)) / donationGoal * 100);
						
						if(bot.isConnected())
						{
							for(Channel channel : bot.getChannels())
							{
								bot.sendMessage(channel, "New donation of " + donation + "! We are at " + currentTotal + " of $" + new DecimalFormat("#.##").format(donationGoal) +" (" + percentage + "% of our goal)! " + message);
							}
						}
					}
				}
			  };
			  new Timer(delay, taskPerformer).start();
		}
	}
}