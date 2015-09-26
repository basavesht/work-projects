package com.tcs.ebw.taglib;

public interface TreeDataInterface
{
   Object[] getDataArray();
   
   void setDataArray(Object[] objs);
   
   String getParentField();
   void setParentField(String parent);

   String getChildField();
   void setChildField(String child);

   String getDisplayField();
   void setDisplayField(String display);
   
   void setRoot(Object root);
   Object getRoot();   
   
   void setHiddenFields(String[] h);
   String[] getHiddenFields();
   
   String getNodeIdField();
   void setNodeIdField(String nodeIdField);
}
    
   