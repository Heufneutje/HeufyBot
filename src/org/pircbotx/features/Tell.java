package org.pircbotx.features;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
import org.pircbotx.utilities.RegexUtils;

public class Tell extends Feature 
{
	private class Message
	{
		public String from;
		public String text;
		public String dateSent;
		
		Message(String from, String text, String dateSent)
		{
			this.from = from;
			this.text = text;
			this.dateSent = dateSent;
		}
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
		
		this.tellsMap = new HashMap<String, ArrayList<Message>>();
	}

	@Override
	public String getHelp()
	{
		return null;
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
				else if(arguments[0].equalsIgnoreCase(bot.getNick()))
				{
					bot.sendMessage(source, "[Tell] Thanks for telling me that " + triggerUser + ".");
				}
				else
				{
					String recepient = fixRegex(arguments[0]);
					String message = metadata.substring(arguments[0].length() + 2);
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss '(UTC)'");
					String dateString = format.format(date);
					
					Message tellMessage = new Message(triggerUser, message, dateString);
					
					if(!tellsMap.containsKey(recepient))
					{
						tellsMap.put(recepient, new ArrayList<Message>());
					}
					tellsMap.get(recepient).add(tellMessage);
					
					writeMessages();
					bot.sendMessage(source, "[Tell] Okay, I'll tell them that next time they speak.");
				}
			}
		}
		else
		{
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
	
			NodeList nList = doc.getElementsByTagName("recepient");
			for(int i = 0; i < nList.getLength(); i++)
			{
				Node nNode = nList.item(i);
				String name = "";
		        if (nNode.getNodeType() != 1)
		        	continue;
		        Element eElement = (Element)nNode;
		        name = getTagValue("name", eElement);
		        
		        for(int k = 0; k < nNode.getChildNodes().getLength(); k++)
		        {
		        	System.out.println(k + " " + nNode.getChildNodes().item(k).getNodeName());
		        }
		        
		        NodeList nList2 = nNode.getChildNodes().item(3).getChildNodes();
		        ArrayList<Message> messages = new ArrayList<Message>();
		        System.out.println(nList2.getLength());
		        
		        for(int j = 0; j < nList2.getLength(); j++)
		        {
		        	Node nNode2 = nList2.item(i);
		        	System.out.println(nNode2.getNodeName());
			        if (nNode2.getNodeType() != 1)
			        	continue;
			        Element eElement2 = (Element)nNode2;
			        String from = getTagValue("from", eElement2);
			        String text = getTagValue("text", eElement2);
			        String dateSent = getTagValue("datesent", eElement2);
			        
			        Message message = new Message(from, text, dateSent);
			        messages.add(message);
		        }
		        tellsMap.put(name, messages);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
                replaceAll("/", "|") + "$";
	}
}
