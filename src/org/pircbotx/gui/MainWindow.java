package org.pircbotx.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import org.pircbotx.HeufyBot;

public class MainWindow extends JFrame
{
  private static final long serialVersionUID = 425414031000823352L;
  private HeufyBot bot;
  private JTextArea chat;
  private JTextField inputField;
  private JScrollPane scrollPane;
  private JTabbedPane tabbedPane;
  private JPanel serverWindow;
  private MenuBar menuBar;
  private HashMap<String, ChatWindow> channels;

  public MainWindow(HeufyBot bot)
  {
    this.bot = bot;
    this.channels = new HashMap<String, ChatWindow>();
    this.tabbedPane = new JTabbedPane();

    this.serverWindow = new JPanel();
    this.chat = new JTextArea();
    this.chat.setLineWrap(true);
    this.chat.setWrapStyleWord(true);
    this.chat.setEditable(false);
    DefaultCaret caret = (DefaultCaret)chat.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    this.inputField = new JTextField();
    this.inputField.addActionListener(new InputListener());
    this.scrollPane = new JScrollPane(this.chat);

    this.serverWindow.setLayout(new BorderLayout());
    this.serverWindow.add(this.scrollPane, "Center");
    this.serverWindow.add(this.inputField, "South");

    this.tabbedPane.add(this.serverWindow, "Server");
    add(this.tabbedPane);

    this.menuBar = new MenuBar(bot);
    setJMenuBar(this.menuBar);

    setSize(640, 480);
    setTitle("HeufyBot 2.0.0");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  public void reset()
  {
    setConnected(false);
    this.channels.clear();
    this.tabbedPane.removeAll();
    this.tabbedPane.add(this.serverWindow, "Server");
  }

  public void setConnected(boolean connected)
  {
    this.menuBar.setConnected(connected);
  }

  public void setNetworkName(String name)
  {
    this.tabbedPane.setTitleAt(0, name);
  }

  public void joinChannel(String channel)
  {
    channel = channel.toLowerCase();
    ChatWindow newWindow = new ChatWindow(channel, this);
    this.channels.put(channel, newWindow);
    this.tabbedPane.add(newWindow, channel);
  }

  public void partChannel(String channel)
  {
    channel = channel.toLowerCase();
    ChatWindow channelWindow = (ChatWindow)this.channels.get(channel);
    this.channels.remove(channelWindow);
    this.tabbedPane.remove(channelWindow);
  }

  public void appendText(String text, String target)
  {
    if (target.equals("server"))
    {
      this.chat.append(text);
    }
    else if (target.contains("#"))
    {
      ((ChatWindow)this.channels.get(target.toLowerCase())).appendText(text);
    }
    else if (!this.channels.containsKey(target))
    {
      ChatWindow newWindow = new ChatWindow(target, this);
      this.channels.put(target.toLowerCase(), newWindow);
      this.tabbedPane.add(newWindow, target);
      newWindow.appendText(text);
    }
    else
    {
      ((ChatWindow)this.channels.get(target)).appendText(text);
    }
  }
  
  public void setChannelTopic(String channel, String topic)
  {
	  channels.get(channel).setChannelTopic(topic);
  }

  public void dispose()
  {
	  super.dispose();
	  if (this.bot.isConnected())
	  {
	      this.bot.disconnect();
	  }
	  System.exit(0);
  }

  public void sendText(String text, String source)
  {
    if (text.startsWith("/"))
    {
      if (text.startsWith("/me "))
      {
        this.bot.sendAction(source, text.substring(4));
      }
      else if (text.startsWith("/notice "))
      {
        this.bot.sendNotice(source, text.substring(8));
      }
      else if (text.startsWith("/invite "))
      {
        this.bot.sendInvite(source, text.substring(8));
      }
      else if (text.startsWith("/msg "))
      {
        String target = text.substring(5).split(" ")[0];
        String message = text.substring(6 + target.length());
        this.bot.sendMessage(target, message);
      }
      else
      {
        this.bot.sendRawLine(text.substring(1));
      }
    }
    else
    {
      this.bot.sendMessage(source, text);
    }
  }
  
  	class InputListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		    sendText(inputField.getText(), "server");
		    inputField.setText("");
		}
	}
}