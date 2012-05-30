package org.pircbotx.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

public class ChatWindow extends JPanel
{
  private static final long serialVersionUID = 6405271968746118256L;
  private JTextArea chat;
  private JTextField input;
  private JTextPane topic;
  private JButton changeButton;
  private JPanel topicPanel;
  
  private JScrollPane scrollPane;
  private MainWindow gui;
  private String name;

  	public ChatWindow(String name, MainWindow gui)
  	{
		this.chat = new JTextArea();
		this.input = new JTextField();
		this.topic = new JTextPane();
		this.topicPanel = new JPanel();
		this.changeButton = new JButton("Change");
		this.gui = gui;
		this.name = name;
		
		this.chat.setEditable(false);
		this.chat.setLineWrap(true);
		this.chat.setWrapStyleWord(true);
		this.topic.setText("(no topic)");
		this.topic.setEditable(false);
		DefaultCaret caret = (DefaultCaret)chat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);    
		
		this.topicPanel.setLayout(new BorderLayout());
		this.topicPanel.add(topic, BorderLayout.CENTER);
		this.topicPanel.add(changeButton, BorderLayout.EAST);
		
		this.scrollPane = new JScrollPane(this.chat);
		setLayout(new BorderLayout());
		add(this.input, "South");
		add(this.scrollPane, "Center");
		add(this.topicPanel, "North");
		
		ActionListener inputListener = new InputListener();
		this.input.addActionListener(inputListener);
		this.changeButton.addActionListener(inputListener);
  	}

  	public void appendText(String text)
  	{
  		this.chat.append(text);
  	}
  
  	public void setChannelTopic(String text)
  	{
	  	topic.setText(text);
  	}

  	public String getChannel()
  	{
  		return this.name;
  	}
  
  	class InputListener implements ActionListener
  	{
  		public void actionPerformed(ActionEvent e)
  		{
  			if(e.getSource() == input)
  			{
  				gui.sendText(input.getText(), name);
  				input.setText("");
  			}
  			else
  			{
			  	String newTopic = PopupManager.showInputMessage("New Topic", "Enter the new topic", topic.getText());
			  	if(newTopic != null)
			  	{
				  	gui.sendText("/topic " + name + " :" + newTopic, name);
			  	}
		  	}
  		}
  	}
}