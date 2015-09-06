/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.common.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ResourceBundle;

import com.tcs.ebw.codegen.common.CommonConstants;

//import sun.tools.javac.*;

/**
 * @author 152699
 *
 * FileUtil used for all kind of file operations.
 * 
 */
public class FileUtil {
	/**
	 * New writeToFile takes FileName and FileContent as a parameter 
	 * and stores the content in the specified location.
	 * takes Source path,Destination path and move the files to Destination path.  
	 * 
	 * @param fileName
	 * @param content
	 * @sourcePath
	 * @destinationPath
	 * @throws Exception
	 */
	private static ResourceBundle configResource =null;
	
	/*
	public static boolean writeToFile(String fileName, String content, boolean initCaps) throws Exception {
		
		configResource = ResourceBundle.getBundle("ConfigResource");
		String srcpath = configResource.getString("source.path");
		String destpath =  configResource.getString("destination.path");
		String jspPath =  configResource.getString("JSPage.path");
		String moduleName =  configResource.getString("module.name");
		
		
	
		System.out.println("*****sourcedestination***********"+srcpath+destpath);
		
	    StringUtil.printConsole("FileUtil ", fileName);
	    boolean isWritten = true;
	    if (fileName!=null && fileName.length() > 0 && content != null && content.length() >0) {
	        if (initCaps) {
	            fileName = StringUtil.initCaps(fileName);
	        }
	        
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file);
			fw.write(content, 0, content.length());
			fw.close();
	    } else {
	        isWritten = false;
	        System.out.println ("FileName/FileContent is Empty...");
	    }
	
	    String[] files = new String[1];
			String source="C:\\Documents and Settings\\222774\\workspace\\NewFramework\\eBankworks\\";
			//String destintaion ="C:\\Documents and Settings\\222774\\workspace\\NewFramework\\eBankworks\\vijay\\";
	    
	    	String destintaion ="D:\\Tomcat4.1\\webapps\\Generated\\WEB-INF\\classes\\";
			
	    if(fileName.endsWith("Form.java")){
	    	new File(destintaion+"com").mkdir();
	    	new File(destintaion+"com\\"+"tcs").mkdir();
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw").mkdir();
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments").mkdir();
	    
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"formbean").mkdir();
	    	FileUtil.copyFile(new File(source+fileName),new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"formbean\\"+fileName));
	 		 		
	 		//files[0]=destintaion+"formbean\\"+fileName;
	 		//new sun.tools.java.ClassPath("C:/Documents and Settings/222774/workspace/NewFramework/eBankworks/vijay/com.jar");
	 		//com.sun.tools.javac.Main.compile(files);

	 		
	    
	    }else if(fileName.endsWith("Action.java")){
	    	new File(destintaion+"com").mkdir();
	    	new File(destintaion+"com\\"+"tcs").mkdir();
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw").mkdir();
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments").mkdir();	    	
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"action").mkdir();
	    
	    	
	    	FileUtil.copyFile(new File(source+fileName),new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"action\\"+fileName));
	    	
	    	//new File(destintaion+"action").mkdir();
	   	 	//FileUtil.copyFile(new File(source+fileName),new File(destintaion+"action\\"+fileName));
	    	//files[0]=destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"action\\"+fileName;
			//files[0]=destintaion+"action\\"+fileName;
	    	//System.out.println("**********Before Classpath****************");
	    	//new sun.tools.java.ClassPath("C:/Documents and Settings/222774/workspace/NewFramework/eBankworks/vijay/com.jar");
			//System.out.println("**********after Classpath****************");
			//com.sun.tools.javac.Main.compile(files);
	    }
	    else if(fileName.endsWith("Delegate.java")||fileName.endsWith("DelegateHook.java")){
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"businessdelegate").mkdir();
	    	FileUtil.copyFile(new File(source+fileName),new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"businessdelegate\\"+fileName));
	    	
	    	//  	new File(destintaion+"businessdelegate").mkdir();
			
			//files[0]=destintaion+"action\\"+fileName;
			//com.sun.tools.javac.Main.compile(files);
	    }
	    else if(fileName.endsWith("TO.java")){
	    	
	    	new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"transferobject").mkdir();
	    	//new File(destintaion+"transferobject").mkdir();
	   	 
			FileUtil.copyFile(new File(source+fileName),new File(destintaion+"com\\"+"tcs\\"+"ebw\\"+"payments\\"+"transferobject\\"+fileName));
			//files[0]=destintaion+"action\\"+fileName;
			//com.sun.tools.javac.Main.compile(files);
	    }else if(fileName.endsWith(".jsp"))
	    		{
	    			System.out.println("******************JSPsGenerated***********************");
	    			new File(jspPath+moduleName).mkdir();
	    			FileUtil.copyFile(new File(source+fileName),new File(jspPath+moduleName+"\\"+fileName));
	    		}
	    	else
	    	FileUtil.copyFile(new File(source+fileName),new File(destintaion+fileName));
	    
	  //  String[] files = new String[1];
		//files[0]="C:\\Documents and Settings\\222774\\workspace\\NewFramework\\eBankworks\\vijay\\";
	    
				
	   /* if(fileName.endsWith("Form.java")){
	    	//new File("formbean").mkdir();
	    	    	
	  //+fileName;
		//	com.sun.tools.javac.Main.compile(files+new File("formbean").mkdir()+"\\"+);
	    }
	    	
	    return isWritten;
	}
	*/
	
	/**
	 * writeToFile takes FileName and FileContent as a parameter 
	 * and stores the content in the specified location.
	 * 
	 * @param fileName
	 * @param content
	 * @throws Exception
	 */
	
	
	public static boolean writeToFile(String fileName, String content, boolean initCaps) throws Exception {
	    StringUtil.printConsole("FileUtil ", fileName);
	    boolean isWritten = true;
	    if (fileName!=null && fileName.length() > 0 && content != null && content.length() >0) {
	        if (initCaps) {
	            fileName = StringUtil.initCaps(fileName);
	        }
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file);
			fw.write(content, 0, content.length());
			fw.close();
	    } else {
	        isWritten = false;
	        System.out.println ("FileName/FileContent is Empty...");
	    }
	    return isWritten;
	}
	
	
	
	public static boolean writeToFile(String fileName, String content) throws Exception {
	    return writeToFile(fileName, content, true);
	    
	}
	
	/*
	public static boolean writeToFile(String fileName, String content,String source,String destination) throws Exception {
	 return writeToFile(fileName, content, true,source,destination);
	    
	}
	*/
	public static boolean writeToFileAppend(String fileName, String content, boolean initCaps,boolean append) throws Exception {
	    StringUtil.printConsole("FileUtil ", fileName);
	    boolean isWritten = true;
	    if (fileName!=null && fileName.length() > 0 && content != null && content.length() >0) {
	        if (initCaps) {
	            fileName = StringUtil.initCaps(fileName);
	        }
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file,append);
			fw.write(content, 0, content.length());
			fw.close();
	    } else {
	        isWritten = false;
	        System.out.println ("FileName/FileContent is Empty...");
	    }
	    return isWritten;
    }
	
	public static boolean writeToFileAppend(String fileName, String content,boolean append) throws Exception {
	    return writeToFileAppend(fileName, content, true,append);
	}
	
	public static String getJScontent() {
	    return "var fld1;\r\nfld1={\"element1\",\"element2\",\"element3\"}";
	}
	
	public static void main (String str[]) throws Exception {
	    /*File inputFile = new File("D:\\classes.zip");
	    String strTest = "Hello world";
		File uploadedFile = new File("D:\\", "Test.zip");
		
		java.io.FileReader fread = new java.io.FileReader(inputFile);
		java.io.FileReader fr = new java.io.FileReader(inputFile);
        //java.io.RandomAccessFile raf = new java.io.RandomAccessFile(uploadedFile, "rw");
		java.io.FileWriter fw = new FileWriter(uploadedFile);
		int read = 0;
		
	    char c[] = new char[(int)inputFile.length()];
	    
	    while ((read = fr.read(c)) != -1)
	        fw.write(c);

	    fw.close();
	    fr.close();
	    
	    System.out.println ("Done");
        //java.io.RandomAccessFile raf = new java.io.RandomAccessFile(uploadedFile, "rw");
        //raf.write(strTest.getBytes(), 0, strTest.getBytes().length);
        //raf.write(strTest.getBytes(), 0, strTest.getBytes().length);
        //raf.close();*/
	    //test();
	}
	
	public static boolean writeObjectToFile(String fileName,Object content, boolean initCaps) throws IOException{
	    boolean result=false;
	    if (fileName!=null && fileName.length() > 0 && content != null) {
	        if (initCaps) {
	            fileName = StringUtil.initCaps(fileName);
	        }
			File file = new File(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(content);
			oos.close();
			result=true;
	    } else 
	        System.out.println ("FileName/FileContent is Empty...");
	    return result;
	}
	
	public static Object getObjectFromFile(String fileName) throws IOException,FileNotFoundException, ClassNotFoundException{
	    Object obj=null;
	    EBWLogger.logDebug("FileUtil","Reading File "+fileName);
	    File file = new File(fileName);
		ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
		obj = oos.readObject();
		oos.close();
		
		EBWLogger.logDebug("FileUtil","Returning Object "+fileName);
	    return obj;
	}
	
	public static void test() throws Exception {
	    /**
	     * Writing into a file
	     */
	    try { 
	          FileInputStream fis = new FileInputStream("D:\\classes.zip");
		      FileOutputStream fos = new FileOutputStream("D:\\Test.zip"); 
		      // Wrap with a DataInputStream so that we can
		      // use its readInt() method.
		      DataInputStream dis = new DataInputStream( fis ); 
		      DataOutputStream ds = new DataOutputStream( fos ); 
		      int i=0; 

		      while ((i = dis.read()) != -1) {
		          ds.write(i);
		      }
		      
	    } catch (IOException ioe) { 
	        ioe.printStackTrace();
	        System.out.println( "IO error: " + ioe );
	    } 
	}
	
	
	
	// Moving Files
	
	public static void movFiles(String sourcePath, String destinationPath)
	{
		File sourceDir = new File(sourcePath);
		if(sourceDir.isDirectory())
		{
			String[] files = sourceDir.list();
			for(int i = 0; i < files.length; i++)
			{
				System.out.println("File name " + files[i]);
				try {
					File sourceFile = new File(sourcePath+"/"+files[i]);
					if(!sourceFile.isDirectory())
					{
						File destinationFile = new File(destinationPath+"/"+files[i]);
						copyFile(sourceFile, destinationFile);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		 if(!destFile.exists()) {
		  destFile.createNewFile();
		 }
		 
		 FileChannel source = null;
		 FileChannel destination = null;
		 try {
		  source = new FileInputStream(sourceFile).getChannel();
		  destination = new FileOutputStream(destFile).getChannel();
		  destination.transferFrom(source, 0, source.size());
		 }
		 finally {
		  if(source != null) {
		   source.close();
		  }
		  if(destination != null) {
		   destination.close();
		  }
		  boolean b=sourceFile.delete();
		  System.out.println("Source Deleted .............."+b);
		  
		  
		}
	}
	
	// Moving Files
	
	
}