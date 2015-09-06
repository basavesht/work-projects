package com.tcs.ebw.taglib;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.tcs.ebw.common.util.PropertyFileReader;
import java.io.Serializable;

 public class ContentTagComparator implements Comparator,Serializable {
	 private int[] colsToSort=null;
	   private boolean[] ascendingOnCol=null;   
	   private String[] dataType=null;
	   
		public ContentTagComparator (int[] colsToSort, boolean [] ascendingOnCol,String[] dataType)
	    {
			//System.out.println("colsToSort="+colsToSort.length+" ascendingOnCol=="+ascendingOnCol.length +" dataType="+dataType.length);	
	    if ( colsToSort.length != ascendingOnCol.length )
	    {
	       throw new IllegalArgumentException( "wrong length on MulticolumnComparator parms" );
	    }
	    this.colsToSort = colsToSort;
	    this.ascendingOnCol = ascendingOnCol;
	    this.dataType = dataType;
	   // for(int h=0;h<this.colsToSort.length;h++)
	 //   System.out.println("h=="+h+ " colsToSort="+this.colsToSort[h]+" ascendingOnCol=="+this.ascendingOnCol[h] +" dataType="+this.dataType[h]);
	    } 
		 
		public final int compare ( Object a , Object b )
	     {
	   	 List rowa = (List)a;
		  List rowb = (List)b;
		 // System.out.println("rowa="+rowa);
		//  System.out.println("rowb="+rowb);
		 	try{ 
		 		//System.out.println("this.colsToSort.length=="+this.colsToSort.length);
		 		for ( int i=0;i<this.colsToSort.length;i++)
		 		{
			//  System.out.println("loop ="+i+" this.colsToSort["+i+"]="+this.colsToSort[i]+" dataType["+i+"]="+dataType[i]+" ascendingOnCol ["+i+"]="+ ascendingOnCol[i]);
			  int col = this.colsToSort[i];
		        CompareRecords tc = new CompareRecords(rowa.get(col),dataType[i]); 
		        int result = tc.compareTo ( rowb.get(col));
		        if ( result != 0 )
		           {
		           if ( ascendingOnCol [i] ) return result;
		           else return -result;
		           }
		        }
		 	}catch(Exception e)// end for // everything was exactly equal
		 	{e.printStackTrace();}
	     return 0;
	     } 
	} 
