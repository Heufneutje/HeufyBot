package org.pircbotx.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.pircbotx.HeufyBot;

public class MenuBar extends JMenuBar
{
  private static final long serialVersionUID = 5547220776106013519L;
  private JMenu file, tools;
  private JMenuItem connect, disconnect, exit, settings, logViewer;
  private HeufyBot bot;

  public MenuBar(HeufyBot bot)
  {
    this.bot = bot;

    this.file = new JMenu("File");
    this.tools = new JMenu("Tools");

    this.connect = new JMenuItem("Connect");
    this.disconnect = new JMenuItem("Disconnect");
    this.exit = new JMenuItem("Exit");
    this.settings = new JMenuItem("Settings...");
    this.logViewer = new JMenuItem("Log Viewer...");

    this.disconnect.setEnabled(false);

    ActionListener listener = new ButtonListener1();
    this.connect.addActionListener(listener);
    this.disconnect.addActionListener(listener);
    this.exit.addActionListener(listener);
    this.settings.addActionListener(listener);
    this.logViewer.addActionListener(listener);

    this.file.add(this.connect);
    this.file.add(this.disconnect);
    this.file.add(this.exit);
    this.tools.add(this.settings);
    this.tools.add(this.logViewer);

    add(this.file);
    add(this.tools);
  }

  public void setConnected(boolean connected)
  {
    this.connect.setEnabled(!connected);
    this.disconnect.setEnabled(connected);
  }
  
  class ButtonListener1 implements ActionListener
  {
  	public void actionPerformed(ActionEvent e)
  	{
  		if (e.getSource() == connect)
  		{
  		    bot.connectWithSettings("settings.xml");
  		}
  		else if (e.getSource() == disconnect)
  		{
  		    bot.disconnect();
  		}
  		else if (e.getSource() == exit)
  		{
  			if (bot.isConnected())
  			{
  			  bot.disconnect();
  			}
  			System.exit(0);
  		}
  		else if (e.getSource() == settings)
  		{
  			new SettingsWindow();
  		}
  		else
  		{
  			new LogViewerWindow();
  		}
  	}
  }
}