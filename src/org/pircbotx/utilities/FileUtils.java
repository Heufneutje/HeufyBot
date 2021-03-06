package org.pircbotx.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils 
{
	public static String readFile(String filePath) 
	{
		try 
		{
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String result = "";
			String read = "";
			while ((read = br.readLine()) != null) 
			{
				result += read += "\n";
			}
			in.close();
			return result;
		} 
		catch (FileNotFoundException e) 
		{
			return null;
		}
		catch (IOException e) 
		{
			return null;
		}
	}
	
	public static boolean writeFile(String filePath, String text) 
	{
		File file = new File(filePath);
		if (file.exists()) 
		{
			file.delete();
		}
		FileWriter writer;
		try 
		{
			writer = new FileWriter(filePath, false);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(text);
			bw.flush();
			bw.close();
			return true;
		} 
		catch (IOException e) 
		{
			LoggingUtils.writeError(FileUtils.class.toString(), e.getClass().toString(), e.getMessage());
			return false;
		}
	}
	
	public static boolean writeFileAppend(String filePath, String text)
	{
		FileWriter writer;
		try 
		{
			writer = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(text);
			bw.flush();
			bw.close();
			return true;
		} 
		catch (IOException e) 
		{
			LoggingUtils.writeError(FileUtils.class.toString(), e.getClass().toString(), e.getMessage());
			return false;
		}
	}
	
	public static boolean touchFile(String filePath)
	{
		File file = new File(filePath);
		try 
		{
			return file.createNewFile();
		}
		catch (IOException e)
		{
			LoggingUtils.writeError(FileUtils.class.toString(), e.getClass().toString(), e.getMessage());
			return false;
		}
	}
	
	public static boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		return file.delete();
	}
}