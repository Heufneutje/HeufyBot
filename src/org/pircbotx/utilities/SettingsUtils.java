package org.pircbotx.utilities;

import java.io.File;
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

public class SettingsUtils
{
	public static HashMap<String, String> readXML(String filePath)
	{
		HashMap<String, String> settingsMap = new HashMap<String, String>();
		try
		{
			File fXmlFile = new File(filePath);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("nicksettings");

			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() != 1)
					continue;
				Element eElement = (Element)nNode;
				settingsMap.put("nickname", getTagValue("nickname", eElement));
				settingsMap.put("realname", getTagValue("realname", eElement));
				settingsMap.put("username", getTagValue("username", eElement));
			}

			NodeList nList2 = doc.getElementsByTagName("serversettings");

			for (int temp = 0; temp < nList2.getLength(); temp++)
			{
				Node nNode = nList2.item(temp);
				if (nNode.getNodeType() != 1)
					continue;
				Element eElement = (Element)nNode;
				settingsMap.put("serverip", getTagValue("serverip", eElement));
				settingsMap.put("port", getTagValue("port", eElement));
				settingsMap.put("password", getTagValue("password", eElement));
				settingsMap.put("authenticationtype", getTagValue("authenticationtype", eElement));
				settingsMap.put("channels", getTagValue("channels", eElement));
			}	
      
			NodeList nList3 = doc.getElementsByTagName("botsettings");

			for (int temp = 0; temp < nList3.getLength(); temp++)
			{
				Node nNode = nList3.item(temp);
				if (nNode.getNodeType() != 1)
					continue;
				Element eElement = (Element)nNode;
				settingsMap.put("commandprefix", getTagValue("commandprefix", eElement));
				settingsMap.put("loadedfeatures", getTagValue("loadedfeatures", eElement));
			}
		}
		catch (Exception e)
		{
			HashMap<String, String> defaultSettings = new HashMap<String, String>();
			defaultSettings.put("nickname", "DefaultBot");
			defaultSettings.put("realname", "HeufyBot IRC Bot");
			defaultSettings.put("username", "Default");
			defaultSettings.put("serverip", "irc.chatspike.net");
			defaultSettings.put("port", "6667");
			defaultSettings.put("password", "");
			defaultSettings.put("authenticationtype", "0");
			defaultSettings.put("channels", "#heufneutje");
			defaultSettings.put("commandprefix", "~");
			defaultSettings.put("loadedfeatures", "Join,Part,Quit");
			writeXML(defaultSettings, filePath);
			return defaultSettings;
		}
		return settingsMap;
	}

	public static boolean writeXML(HashMap<String, String> settingsMap, String filePath)
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("settings");
			doc.appendChild(rootElement);
     
			Element nicksettings = doc.createElement("nicksettings");
			rootElement.appendChild(nicksettings);

			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode((String)settingsMap.get("nickname")));
			nicksettings.appendChild(nickname);

			Element realname = doc.createElement("realname");
			realname.appendChild(doc.createTextNode((String)settingsMap.get("realname")));
			nicksettings.appendChild(realname);

			Element username = doc.createElement("username");
			username.appendChild(doc.createTextNode((String)settingsMap.get("username")));
			nicksettings.appendChild(username);

			Element serversettings = doc.createElement("serversettings");
			rootElement.appendChild(serversettings);

			Element serverip = doc.createElement("serverip");
			serverip.appendChild(doc.createTextNode((String)settingsMap.get("serverip")));
			serversettings.appendChild(serverip);

			Element port = doc.createElement("port");
			port.appendChild(doc.createTextNode((String)settingsMap.get("port")));
			serversettings.appendChild(port);

			Element password = doc.createElement("password");
			password.appendChild(doc.createTextNode((String)settingsMap.get("password")));
			serversettings.appendChild(password);

			Element authenticationtype = doc.createElement("authenticationtype");
			authenticationtype.appendChild(doc.createTextNode((String)settingsMap.get("authenticationtype")));
			serversettings.appendChild(authenticationtype);

			Element channels = doc.createElement("channels");
			channels.appendChild(doc.createTextNode((String)settingsMap.get("channels")));
			serversettings.appendChild(channels);
			
			Element botsettings = doc.createElement("botsettings");
			rootElement.appendChild(botsettings);
      
			Element commandprefix = doc.createElement("commandprefix");
			commandprefix.appendChild(doc.createTextNode((String)settingsMap.get("commandprefix")));
			botsettings.appendChild(commandprefix);
      
			Element loadedfeatures = doc.createElement("loadedfeatures");
			loadedfeatures.appendChild(doc.createTextNode((String)settingsMap.get("loadedfeatures")));
			botsettings.appendChild(loadedfeatures);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));

			transformer.transform(source, result);
			return true;
		}
		catch (Exception e)
		{
			LoggingUtils.writeError(SettingsUtils.class.toString(), e.getClass().toString(), e.getMessage());
		}
		return false;
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
}