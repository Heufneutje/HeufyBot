package org.pircbotx.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
    setDefaultCloseOperation(2);
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
      JScrollBar vbar = this.scrollPane.getVerticalScrollBar();
      boolean autoScroll = vbar.getValue() + vbar.getVisibleAmount() == vbar.getMaximum();
      this.chat.append(text);
      if (autoScroll)
      {
        this.chat.setCaretPosition(this.chat.getDocument().getLength());
      }
    }
    else if (target.contains("#"))
    {
      ((ChatWindow)this.channels.get(target)).appendText(text);
    }
    else if (!this.channels.containsKey(target))
    {
      ChatWindow newWindow = new ChatWindow(target, this);
      this.channels.put(target, newWindow);
      this.tabbedPane.add(newWindow, target);
      newWindow.appendText(text);
    }
    else
    {
      ((ChatWindow)this.channels.get(target)).appendText(text);
    }
  }

  public void dispose()
  {
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