/**
 * 
 */
package com.tcs.ebw.refresh;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.tcs.ebw.common.util.PropertyFileReader;
/**
 * @author 197172
 *
 */
public class RefreshOutputGenerator {
	private FileInputStream in;
	private static final String REFRESHFILEPATH="RefreshFilePath";
	/**
	 * 
	 */
	public RefreshOutputGenerator() {
		// TODO Auto-generated constructor stub
	}
	  /**
	   * 
	   * @param filename
	   * @throws FileNotFoundException
	   */

	  public RefreshOutputGenerator(String filename) throws FileNotFoundException {
	    in = new FileInputStream(filename);
	  }

	  public String getWord() throws IOException {
	    int c;
	    StringBuffer buf = new StringBuffer();

	    do {
	      c = in.read();
	      if (Character.isSpace((char) c))
	        return buf.toString();
	      else
	        buf.append((char) c);
	    } while (c != -1);

	    return buf.toString();
	  }

	  public static void main(String[] args) throws java.io.IOException {
		  
			try {
				RefreshOutputGenerator ref=new RefreshOutputGenerator();
				ArrayList arr=ref.fetchRefreshMviews();
				System.out.println("Vlaue of the mview is :"+arr);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		      
	  }
	  
	 public ArrayList fetchRefreshMviews() throws Exception{
		 ArrayList arrMviews=new ArrayList();
		 try{
				File f;
				
				f = new File(PropertyFileReader.getProperty(REFRESHFILEPATH));
							
			      if(f.exists()){
			    	  RefreshOutputGenerator file;
					try {
						file = new RefreshOutputGenerator(PropertyFileReader.getProperty(REFRESHFILEPATH));							  	   
				 		String str=file.getWord();
						String[] mviews=str.split("~");
				 		for(int m=0;m<mviews.length;m++){				 							 					 	
				 				arrMviews.add(mviews[m]);				 		
				 		}		
				 		System.out.println("Value of the arraylist :"+arrMviews);
					} catch (Exception e) {			
						e.printStackTrace();
					}		  	    		 
			      }else{
			    	  System.out.println("This file is not exist");
			      }
			
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return arrMviews;
	 }	  
}
