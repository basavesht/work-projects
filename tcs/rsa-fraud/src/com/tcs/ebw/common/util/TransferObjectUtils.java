//Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 2/8/2007 1:04:30 PM
//Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
//Decompiler options: packimports(3) deadcode 
//Source File Name:   TransferObjectUtils.java

package com.tcs.ebw.common.util;

//import com.tcs.ebw.RJ.transferobject.ClrClntRspnsTO;
import com.tcs.ebw.transferobject.EBWTransferObject;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

//Referenced classes of package com.tcs.ebw.common.util:
//         StringUtil

public class TransferObjectUtils
{

 public TransferObjectUtils()
 {
 }

 public static void copyCollections(EBWTransferObject ebwtransferobject, EBWTransferObject ebwtransferobject1)
 {
     Class class1 = ebwtransferobject1.getClass();
     Class class2 = ebwtransferobject.getClass();
     Field afield[] = class1.getDeclaredFields();
     Field afield1[] = class2.getDeclaredFields();
     for(int i = 0; i < afield1.length; i++)
     {
         Field field = afield1[i];
         String s = field.getName();
         Class class4 = field.getDeclaringClass();
         Class class3 = field.getType();
         System.out.println("Field Type" + class3);
         Class aclass[] = new Class[1];
         aclass[0] = class3;
         Object aobj[] = new Object[1];
         Object obj = null;
         Object obj1 = null;
         Object obj2 = null;
         Object obj3 = null;
         try
         {
             Method method1 = class1.getMethod("get" + StringUtil.initCaps(s), null);
             if(class3.getName().equals("java.lang.String"))
             {
                 String s1 = (String)method1.invoke(ebwtransferobject1, null);
                 aobj[0] = s1;
             } else
             if(class3.getName().equals("java.lang.Double"))
             {
                 Double double1 = (Double)method1.invoke(ebwtransferobject1, null);
                 aobj[0] = double1;
             } else
             if(class3.getName().equals("java.util.Date"))
             {
                 Date date = (Date)method1.invoke(ebwtransferobject1, null);
                 aobj[0] = date;
             } else
             if(class3.getName().equals("java.sql.Timestamp"))
             {
                 Timestamp timestamp = (Timestamp)method1.invoke(ebwtransferobject1, null);
                 aobj[0] = timestamp;
             }
             Method method = class2.getMethod("set" + StringUtil.initCaps(s), aclass);
             method.invoke(ebwtransferobject, aobj);
         }
         catch(Exception exception)
         {
             exception.printStackTrace();
         }
     }

 }

 public static void main(String args[])
 {
     TransferObjectUtils transferobjectutils = new TransferObjectUtils();
     /*ClrClntRspnsTO clrclntrspnsto = new ClrClntRspnsTO();
     clrclntrspnsto.setClrEntityId("check");
     clrclntrspnsto.setClrOptnSeq(new Double(5D));
     clrclntrspnsto.setClrRcptDt(new Date(0L));
     ClrClntRspnsTO clrclntrspnsto1 = new ClrClntRspnsTO();
     TransferObjectUtils _tmp = transferobjectutils;
     copyCollections(clrclntrspnsto1, clrclntrspnsto);
     System.out.println("Source Form" + clrclntrspnsto);
     System.out.println("Destination Form" + clrclntrspnsto1);*/
 }
}