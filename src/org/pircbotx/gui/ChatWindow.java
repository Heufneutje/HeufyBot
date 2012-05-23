package org.pircbotx.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JPanel
{
  private static final long serialVersionUID = 6405271968746118256L;
  private JTextArea chat;
  private JTextField input;
  private JScrollPane scrollPane;
  private MainWindow gui;
  private String name;

  public ChatWindow(String name, MainWindow gui)
  {
    this.chat = new JTextArea();
    this.input = new JTextField();
    this.gui = gui;
    this.name = name;

    this.chat.setEditable(false);
    this.chat.setLineWrap(true);
    this.chat.setWrapStyleWord(true);
    this.scrollPane = new JScrollPane(this.chat);
    setLayout(new BorderLayout());
    add(this.input, "South");
    add(this.scrollPane, "Center");
    this.input.addActionListener(new InputListener());
  }

  public void appendText(String text)
  {
    JScrollBar vbar = this.scrollPane.getVerticalScrollBar();
    boolean autoScroll = vbar.getValue() + vbar.getVisibleAmount() == vbar.getMaximum();
    this.chat.append(text);
    if (autoScroll)
    {
      this.chat.setCaretPosition(this.chat.getDocument().getLength());
    }
  }

  public String getChannel()
  {
    return this.name;
  }
  
  class InputListener implements ActionListener
  {
	  public void actionPerformed(ActionEvent e)
	  {
		  gui.sendText(input.getText(), name);
		  input.setText("");
	  }
  }
}