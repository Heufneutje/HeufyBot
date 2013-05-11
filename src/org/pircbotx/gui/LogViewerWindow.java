package org.pircbotx.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.pircbotx.Logger;

public class LogViewerWindow extends JFrame
{
	private static final long serialVersionUID = 4387435102198309819L;
	private JTree tree;
	private JTextArea log;
	
	public LogViewerWindow()
	{
		this.setLayout(new BorderLayout());
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Logs");
	    createNodes(top);

	    tree = new JTree(top);
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(new SelectionListener());
	    log = new JTextArea();
	    log.setEditable(false);
	    log.setLineWrap(true);
	    log.setWrapStyleWord(true);
	    
	    this.add(new JScrollPane(tree), BorderLayout.WEST);
	    this.add(new JScrollPane(log), BorderLayout.CENTER);
	    
	    setSize(640, 480);
	    setTitle("Log Viewer");
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    setVisible(true);
	}
	
	private void createNodes(DefaultMutableTreeNode top)
	{
        DefaultMutableTreeNode serversNode = null;
        DefaultMutableTreeNode channelsNode = null;
        DefaultMutableTreeNode logsNode = null;
        
        File serversFolder = new File("logs");
        if(serversFolder.exists())
        {
        	File[] servers = serversFolder.listFiles();
        	for(int i = 0; i < servers.length; i++)
        	{
        		serversNode = new DefaultMutableTreeNode(servers[i].getName());
        		top.add(serversNode);
        		File[] channels = servers[i].listFiles();
        		for(int j = 0; j < channels.length; j++)
        		{
        			channelsNode = new DefaultMutableTreeNode(channels[j].getName());
        			serversNode.add(channelsNode);
        			File[] logs = channels[j].listFiles();
        			for(int k = 0; k < logs.length; k++)
        			{
        				logsNode = new DefaultMutableTreeNode(new LogInfo(logs[k].getName(), logs[k].getPath()));
        				channelsNode.add(logsNode);
        			}
        		}
        	}
        }
    }
	
	class LogInfo
	{
        public String name;
        public String path;

        public LogInfo(String name, String path)
        {
            this.name = name;
            this.path = path;
        }

        public String toString()
        {
            return name;
        }
        
        public String getPath()
        {
        	return path;
        }
    }
	
	class SelectionListener implements TreeSelectionListener
	{

		@Override
		public void valueChanged(TreeSelectionEvent arg0)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node == null) return;
			
			Object nodeInfo = node.getUserObject();
			if (node.isLeaf())
			{
				LogInfo logInfo = (LogInfo) nodeInfo;
				log.setText(Logger.read(logInfo.getPath()));
			}
			else
			{
			    log.setText("");
			}
		}
	}
}