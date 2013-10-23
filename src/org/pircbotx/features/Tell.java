package org.pircbotx.features;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.pircbotx.HeufyBot;
import org.pircbotx.features.types.AuthType;
import org.pircbotx.features.types.TriggerType;
import org.pircbotx.utilities.FileUtils;
import org.pircbotx.utilities.LoggingUtils;
import org.pircbotx.utilities.PastebinUtils;
import org.pircbotx.utilities.RegexUtils;

public class Tell extends Feature 
{
	private class Message
	{
		public String from;
		public String text;
		public String dateSent;
		public String messageSource;
		
		Message(String from, String text, String dateSent, String messageSource)
		{
			this.from = from;
			this.text = text;
			this.dateSent = dateSent;
			this.messageSource = messageSource;
		}
	}
	private HashMap<String, ArrayList<Message>> tellsMap;
	
	public Tell(HeufyBot bot, String name)
	{
		super(bot, name);
		this.settingsPath = "featuredata/tells.xml";
		
		this.triggerType = TriggerType.Automatic;
		this.authType = AuthType.Anyone;
		
		this.triggers = new String[3];
		this.triggers[0] = bot.getCommandPrefix() + "tell";
		this.triggers[1] = bot.getCommandPrefix() + "rtell";
		this.triggers[2] = bot.getCommandPrefix() + "senttells";
		
		this.tellsMap = new HashMap<String, ArrayList<Message>>();
	}

	@Override
	public String getHelp()
	{
		return "Commands: " + bot.getCommandPrefix() + "tell <user> <message>, " + bot.getCommandPrefix() + "rtell <message>, " + bot.getCommandPrefix() + "senttells | Tells the specified user a message the next time they speak, removes a message sent by you from the database or lists your pending messages.";
	}

	@Override
	public void process(String source, String metadata, String triggerUser, String triggerCommand)
	{
		if(triggerCommand.equals(bot.getCommandPrefix() + "tell"))
		{
			if(metadata.equals(""))
			{
				bot.sendMessage(source, "[Tell] Tell what?");
			}
			else if(metadata.startsWith(" "))
			{
				String[] arguments = metadata.substring(1).split(" ");
				if(arguments.length == 1)
				{
					bot.sendMessage(source, "[Tell] What do you want me to tell them?");
				}
				else
				{
					String[] recepients;
					if(arguments[0].contains("&"))
					{
						recepients = arguments[0].split("&");
					}
					else
					{
						recepients = new String[] {arguments[0]};
					}
					for(int i = 0; i < recepients.length; i++)
					{
						String recepient = fixRegex(recepients[i]);
						if(triggerUser.toLowerCase().matches(recepient.toLowerCase()))
						{
							bot.sendMessage(source, "[Tell] Why are you telling yourself that?");
						}
						else if(recepients[i].equalsIgnoreCase(bot.getNick()))
						{
							bot.sendMessage(source, "[Tell] Thanks for telling me that " + triggerUser + ".");
						}
						else
						{
							String message = metadata.substring(arguments[0].length() + 2);
							Date date = new Date();
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss '(UTC)'");
							String dateString = format.format(date);
							String messageSource = "";
							if(source.equals(triggerUser))
							{
								messageSource = "PM";
							}
							else
							{
								messageSource = "Channel";
							}
							
							Message tellMessage = new Message(triggerUser, message, dateString, messageSource);
							
							if(!tellsMap.containsKey(recepient))
							{
								tellsMap.put(recepient, new ArrayList<Message>());
							}
							tellsMap.get(recepient).add(tellMessage);
							bot.sendMessage(source, "[Tell] Okay, I'll tell " + recepients[i] + " that next time they speak.");
						}
					}
					writeMessages();
				}
			}
		}
		else if(triggerCommand.equals(bot.getCommandPrefix() + "rtell"))
		{
			if(metadata.equals(""))
			{
				bot.sendMessage(source, "[Tell] Remove what?");
			}
			else if(metadata.startsWith(" "))
			{
				for(Iterator<String> iter = tellsMap.keySet().iterator(); iter.hasNext();)
				{
					String user = iter.next();
					for(Iterator<Message> iter2 = tellsMap.get(user).iterator(); iter2.hasNext();)
					{
						Message message = iter2.next();
						boolean messageFound = false;
						if(message.from.equalsIgnoreCase(triggerUser) && message.text.matches(".*" + metadata.substring(1) + ".*"))
						{
							bot.sendMessage(source, "[Tell] Message '" + message.text + "' sent to " + user + " on " + message.dateSent + " was removed from the message database!");
							iter2.remove();
							messageFound = true;
						}
						if(messageFound)
							break;
					}
					if(tellsMap.get(user).size() == 0)
					{
						iter.remove();
					}
				}
			}
		}
		else if(triggerCommand.equals(bot.getCommandPrefix() + "senttells"))
		{
			if(metadata.equals(""))
			{
				String foundTells = "";
				String foundPMs = "";
				for(String user : tellsMap.keySet())
				{
					for(Message message : tellsMap.get(user))
					{
						if(message.from.equalsIgnoreCase(triggerUser))
						{
							if(message.messageSource.equals("PM"))
							{
								foundPMs += message.text + " < Sent to " + user + " on " + message.dateSent + "\n";
							}
							else
							{
								foundTells += message.text + " < Sent to " + user + " on " + message.dateSent + "\n";
							}
						}
					}
				}
				if(foundTells.equals("") && foundPMs.equals(""))
				{
					bot.sendMessage(source, "[Tell] There are no messages sent by you that have not been received yet.");
				}
				else
				{
					if(!foundTells.equals(""))
					{
						String[] tells = foundTells.split("\n");
						if(tells.length > 3)
						{
							String result = PastebinUtils.post(foundTells, triggerUser + "'s Messages", "10M", false);
							if(result == null)
							{
								bot.sendMessage(source, "[Tell] Error: Messages could not be posted.");
							}
							else if(result.equals("NoKey"))
							{
								bot.sendMessage(source, "[Tell] Error: No PasteBin API key was found");
							}
							else if(result.startsWith("http://pastebin.com/"))
							{
								bot.sendMessage(source, "[Tell] These messages sent by you have not yet been received: " + result  + " (Link expires in 10 minutes)");
							}
							else
							{
								bot.sendMessage(source, "[Tell] Error: " + result);
							}
						}
						else
						{
							for(int i = 0; i < tells.length; i++)
							{
								bot.sendMessage(source, "[Tell] " + tells[i]);
							}
						}
					}
					if(!foundPMs.equals(""))
					{
						if(!source.equals(triggerUser) && foundTells.equals(""))
						{
							bot.sendMessage(source, "[Tell] There are no public messages sent by you that have not been received yet, but there are private messages.");
						}
						String[] tells = foundPMs.split("\n");
						for(int i = 0; i < tells.length; i++)
						{
							bot.sendMessage(triggerUser, "[Tell] " + tells[i]);
						}
					}
				}
			}
		}
		else
		{
			for(Iterator<String> iter = tellsMap.keySet().iterator(); iter.hasNext();)
			{
				String user = iter.next();
				if(triggerUser.toLowerCase().matches(user.toLowerCase()))
				{
					String tells = "";
					String pms = "";					
					
					for(Message message : tellsMap.get(user))
					{
						if(message.messageSource.equals("PM"))
						{
							pms += message.text + " < From " + message.from + " on " + message.dateSent + "\n";
						}
						else
						{
							tells += triggerUser + ": " + message.text + " < From " + message.from + " on " + message.dateSent + "\n";
						}
					}
					if(!tells.equals(""))
					{
						String[] receivedTells = tells.split("\n");
						if(receivedTells.length > 3)
						{
							String result = PastebinUtils.post(tells, triggerUser + "'s Messages", "1H", false);
							if(result == null)
							{
								bot.sendMessage(source, "[Tell] Error: Messages could not be posted.");
							}
							else if(result.equals("NoKey"))
							{
								bot.sendMessage(source, "[Tell] Error: No PasteBin API key was found");
							}
							else if(result.startsWith("http://pastebin.com/"))
							{
								bot.sendMessage(source, "[Tell] " + triggerUser + ", you have " + receivedTells.length + " messages. Go here to read them: " + result  + " (Link expires in 60 minutes)");
							}
							else
							{
								bot.sendMessage(source, "[Tell] Error: " + result);
							}
						}
						else
						{
							for(int i = 0; i < receivedTells.length; i++)
							{
								bot.sendMessage(source, "[Tell] " + receivedTells[i]);
							}
						}
					}
					if(!pms.equals(""))
					{
						String[] receivedPMs = pms.split("\n");
						for(int i = 0; i < receivedPMs.length; i++)
						{
							bot.sendMessage(triggerUser, "[Tell] " + receivedPMs[i]);
						}
					}
					iter.remove();
					writeMessages();
				}
			}
		}
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
		try
		{
			File fXmlFile = new File(settingsPath);
	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
	
			NodeList n = doc.getElementsByTagName("recepient");
	        for(int i = 0; i < n.getLength(); i++)
	        {
	        	String name = "";
	        	ArrayList<Message> messages = new ArrayList<Message>();
	        	
	        	if (n.item(i).getNodeType() == 1)
        		{
	        		Element eElement = (Element)n.item(i);
	        		name = getTagValue("name", eElement);
        		}
	        	NodeList list1 = n.item(i).getChildNodes();
	        	for(int j = 0; j < list1.getLength(); j++)
	        	{     		
	        		if(list1.item(j).getNodeName().equals("messages"))
	        		{
	        			NodeList list2 = list1.item(j).getChildNodes();
	        			for(int k = 0; k < list2.getLength(); k++)
	        			{
	        				if(list2.item(k).getNodeName().equals("message"))
	        				{
	        					Node nNode = list2.item(k);
        				        if (nNode.getNodeType() != 1)
        				          continue;
        				        Element eElement2 = (Element)nNode;
        				        Message message = new Message(getTagValue("from", eElement2), getTagValue("text", eElement2), getTagValue("datesent", eElement2), getTagValue("messagesource", eElement2));
        				        messages.add(message);
	        				}
	        			}
	        		}
	        	}
		        tellsMap.put(name, messages);
			}
		}
		catch(Exception e)
		{
			LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
		}
	}
	
	public void writeMessages()
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element root = doc.createElement("recepients");
			doc.appendChild(root);
			
			for(String user : tellsMap.keySet())
			{
				Element userNode = doc.createElement("recepient");
				root.appendChild(userNode);
				Element name = doc.createElement("name");
				userNode.appendChild(name);
				name.appendChild(doc.createTextNode(user));
				Element messages = doc.createElement("messages");
				userNode.appendChild(messages);
				
				for(Message message : tellsMap.get(user))
				{
					Element messageElement = doc.createElement("message");
					messages.appendChild(messageElement);
					
					Element fromElement = doc.createElement("from");
					messageElement.appendChild(fromElement);
					fromElement.appendChild(doc.createTextNode(message.from));
					
					Element textElement = doc.createElement("text");
					messageElement.appendChild(textElement);
					textElement.appendChild(doc.createTextNode(message.text));
					
					Element dateElement = doc.createElement("datesent");
					messageElement.appendChild(dateElement);
					dateElement.appendChild(doc.createTextNode(message.dateSent));
					
					Element sourceElement = doc.createElement("messagesource");
					messageElement.appendChild(sourceElement);
					sourceElement.appendChild(doc.createTextNode(message.messageSource));
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(new File(settingsPath));

		    transformer.transform(source, result);
		}
		catch (Exception e)
		{
			LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
		}
	}
	
	private String getTagValue(String sTag, Element eElement)
	{
	    try
	    {
	    	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    	Node nValue = nlList.item(0);
	    	return nValue.getNodeValue();
	    }
	    catch (NullPointerException e) 
	    {
	    	e.printStackTrace();
	    	LoggingUtils.writeError(this.getClass().toString(), e.getClass().toString(), e.getMessage());
	    }
	    return "";
	}
	
	private String fixRegex(String regex)
	{
		return "^" + RegexUtils.escapeRegex(regex).
				replaceAll("\\*", ".*").
				replaceAll("\\?", ".").
				replaceAll("\\(", "(").
                replaceAll("\\)", ")").
                replaceAll(",", "|").
                replaceAll("/", "|") + "$";
	}
}
