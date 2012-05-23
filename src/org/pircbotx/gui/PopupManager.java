package org.pircbotx.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PopupManager
{
  private static JFrame frame;

  public static void showErrorMessage(String title, String message)
  {
    JOptionPane.showMessageDialog(frame, message, title, 0);
  }

  public static void showWarningMessage(String title, String message)
  {
    JOptionPane.showMessageDialog(frame, message, title, 2);
  }
}

/* Location:           C:\Users\Stefan\Desktop\No name_01.jar
 * Qualified Name:     gui.PopupManager
 * JD-Core Version:    0.6.0
 */