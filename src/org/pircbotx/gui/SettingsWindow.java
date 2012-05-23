package org.pircbotx.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.pircbotx.XMLIO;

public class SettingsWindow extends JFrame
{
  private static final long serialVersionUID = 3538433380355281876L;
  private JTextField nicknameField;
  private JTextField realnameField;
  private JTextField usernameField;
  private JTextField serverField;
  private JTextField portField;
  private JTextField channelsField;
  private JPasswordField passwordField;
  private JLabel nicknameLabel;
  private JLabel realnameLabel;
  private JLabel usernameLabel;
  private JLabel serverLabel;
  private JLabel portLabel;
  private JLabel passwordLabel;
  private JLabel authenticationtypeLabel;
  private JLabel channelsLabel;
  private JComboBox<String> authenticationtypeBox;
  private JButton confirm;
  private JButton cancel;

  public SettingsWindow()
  {
    HashMap<String, String> settingsMap = XMLIO.readXML("settings.xml");

    setLayout(null);

    this.nicknameField = new JTextField((String)settingsMap.get("nickname"));
    this.realnameField = new JTextField((String)settingsMap.get("realname"));
    this.usernameField = new JTextField((String)settingsMap.get("username"));
    this.serverField = new JTextField((String)settingsMap.get("serverip"));
    this.portField = new JTextField((String)settingsMap.get("port"));
    this.passwordField = new JPasswordField((String)settingsMap.get("password"));
    this.channelsField = new JTextField((String)settingsMap.get("channels"));

    String[] authenticationTypes = { "None", "Server Password", "NickServ", "Q Auth" };
    this.authenticationtypeBox = new JComboBox<String>(authenticationTypes);
    this.authenticationtypeBox.setSelectedIndex(Integer.parseInt((String)settingsMap.get("authenticationtype")));

    this.nicknameLabel = new JLabel("Nickname");
    this.realnameLabel = new JLabel("Real Name");
    this.usernameLabel = new JLabel("Username");
    this.serverLabel = new JLabel("Hostname");
    this.portLabel = new JLabel("Port");
    this.passwordLabel = new JLabel("Password");
    this.authenticationtypeLabel = new JLabel("Authentication Type");
    this.channelsLabel = new JLabel("Channels");

    this.confirm = new JButton("OK");
    this.cancel = new JButton("Cancel");

    this.nicknameLabel.setBounds(5, 5, 100, 20);
    this.realnameLabel.setBounds(5, 30, 100, 20);
    this.usernameLabel.setBounds(5, 55, 100, 20);
    this.serverLabel.setBounds(5, 80, 100, 20);
    this.portLabel.setBounds(5, 105, 100, 20);
    this.passwordLabel.setBounds(5, 130, 100, 20);
    this.authenticationtypeLabel.setBounds(5, 155, 100, 20);
    this.channelsLabel.setBounds(5, 180, 100, 20);

    this.nicknameField.setBounds(120, 5, 165, 20);
    this.realnameField.setBounds(120, 30, 165, 20);
    this.usernameField.setBounds(120, 55, 165, 20);
    this.serverField.setBounds(120, 80, 165, 20);
    this.portField.setBounds(120, 105, 165, 20);
    this.passwordField.setBounds(120, 130, 165, 20);
    this.authenticationtypeBox.setBounds(120, 155, 165, 20);
    this.channelsField.setBounds(120, 180, 165, 20);

    this.confirm.setBounds(5, 205, 140, 20);
    this.cancel.setBounds(150, 205, 140, 20);

    ActionListener listener = new SettingsListener();
    this.authenticationtypeBox.addActionListener(listener);
    this.confirm.addActionListener(listener);
    this.cancel.addActionListener(listener);

    add(this.nicknameLabel);
    add(this.realnameLabel);
    add(this.usernameLabel);
    add(this.serverLabel);
    add(this.portLabel);
    add(this.passwordLabel);
    add(this.authenticationtypeLabel);
    add(this.channelsLabel);

    add(this.nicknameField);
    add(this.realnameField);
    add(this.usernameField);
    add(this.serverField);
    add(this.portField);
    add(this.passwordField);
    add(this.authenticationtypeBox);
    add(this.channelsField);

    add(this.confirm);
    add(this.cancel);

    if (Integer.parseInt((String)settingsMap.get("authenticationtype")) == 0)
    {
      this.passwordField.setText("");
      this.passwordField.setEnabled(false);
      this.passwordLabel.setEnabled(false);
    }

    setTitle("Settings");
    setResizable(false);
    setSize(300, 260);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(2);
    setVisible(true);
  }
  
  class SettingsListener implements ActionListener
  {
	  public void actionPerformed(ActionEvent e)
	  {
	    if (e.getSource() == authenticationtypeBox)
	    {
	      if (authenticationtypeBox.getSelectedIndex() == 0)
	      {
	    	  passwordField.setText("");
	    	  passwordField.setEnabled(false);
	    	  passwordLabel.setEnabled(false);
	      }
	      else
	      {
	    	  passwordField.setEnabled(true);
	    	  passwordLabel.setEnabled(true);
	      }
	    }
	    else if (e.getSource() == cancel)
	    {
	      	dispose();
	    }
	    else
	    {
	      HashMap<String, String> settingsMap = new HashMap<String, String>();
	      settingsMap.put("nickname", nicknameField.getText());
	      settingsMap.put("realname", realnameField.getText());
	      settingsMap.put("username", usernameField.getText());
	      settingsMap.put("serverip", serverField.getText());
	      settingsMap.put("port", portField.getText());
	      settingsMap.put("password", new String(passwordField.getPassword()));
	      settingsMap.put("authenticationtype", "" + authenticationtypeBox.getSelectedIndex());
	      settingsMap.put("channels", channelsField.getText());
	      XMLIO.writeXML(settingsMap, "settings.xml");
	      dispose();
	    }
	  }
  }
}