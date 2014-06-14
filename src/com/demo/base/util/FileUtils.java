package com.demo.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.http.util.EncodingUtils;

import com.demo.base.support.BaseConstants;

public class FileUtils
{
	
	/**
	 * 删除指定目录下的所有文件
	 * @param path
	 */
	public static void deleteFilesByPath(String path){
		deleteFiles(new File(path));
	}
	
	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void deleteFiles(File file) {
		if (file.exists() == false) {
			
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					deleteFiles(f);
				}
				file.delete();
			}
		}
	}
	
	/**
	 * 保存文件内容 并返回文件内容
	 * @param path
	 * @param content
	 * @return
	 */
	public static String saveFileContent(String path,String content) {
		
		if(!"".equals(content)){
			createText(path); // 创建文件
			deleteText(path); // 删除文件内容
			writeText(content, path);
		}
		return readText(path);
		
	}

	/**
	 * 创建Text文件
	 * 
	 * @param path
	 */
	public static void createText(final String path)
	{
		File filename;
		try
		{
			filename = new File(path);
			if (!filename.exists())
			{
				filename.createNewFile();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}// end method createText()

	/**
	 * 删除Text文件
	 * 
	 * @param path
	 */
	public static void deleteText(String path)
	{
		try
		{
			RandomAccessFile file = new RandomAccessFile(path, "rw");
			file.setLength(0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 读取text内容
	 * 
	 * @param path
	 * @return
	 */
	public static String readText(String path)
	{
		FileReader fileread;
		File filename = new File(path);
		if (!filename.exists())
		{
			return "";
		}
		String line = null;
		try
		{
			fileread = new FileReader(filename);
			BufferedReader bfr = new BufferedReader(fileread);
			try
			{
				line = bfr.readLine();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return line;
	}// end method readText()

	/**
	 * 向Text文件中写入内容
	 * 
	 * @param body
	 * @param path
	 */
	public static void writeText(String content, String path)
	{
		// 先读取原有文件内容，然后进行写入操作
		RandomAccessFile mm = null;
		File filename = new File(path);
		try
		{
			mm = new RandomAccessFile(filename, "rw");
			mm.writeBytes(content);
		}
		catch (IOException e1)
		{
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		finally
		{
			if (mm != null)
			{
				try
				{
					mm.close();
				}
				catch (IOException e2)
				{
					// TODO 自动生成 catch 块
					e2.printStackTrace();
				}
			}
		}
	}// end method writeText()

	public static File createFile(String filePath, String fileName)
	{
		File file = null;
		makeRootDirectory(filePath);
		try
		{
			file = new File(filePath + fileName);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath)
	{
		File file = null;
		try
		{
			file = new File(filePath);
			if (!file.exists())
			{
				file.mkdir();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除SD卡上的文件
	 * 
	 * @param fileName
	 */

	public static boolean deleteFile(String fileName)
	{
		File file = new File(fileName);
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		return file.delete();
	}

	public static void writeFile(String message, String fileName)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 读取SD卡中文本文件
	 * 
	 * @param fileName
	 * @return
	 */

	public static String readFile(String fileName)
	{
		// StringBuffer sb = new StringBuffer();
		// File file = new File(fileName);
		// try
		// {
		// FileInputStream fis = new FileInputStream(file);
		// int c;
		//
		// while ((c = fis.read()) != -1)
		// {
		// sb.append((char) c);
		// }
		// fis.close();
		//
		// }
		// catch (FileNotFoundException e)
		// {
		// e.printStackTrace();
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		// return sb.toString();

		String res = "";
		try
		{
			File file = new File(fileName);
			if (!file.exists())
			{
				return res;
			}
			FileInputStream fin = new FileInputStream(file);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, BaseConstants.CHARSET);
			fin.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;

	}

}