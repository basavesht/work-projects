package com.tcs.ebw.reports;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.taglib.DataInterface;
import com.tcs.ebw.taglib.TableColAttrObj;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

public class FormExport
{

    public FormExport()
    {
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reportType)
    {
        try
        {
            EbwForm ebwForm = (EbwForm)form;
            String tableName = null;
            String reorderColsStr = null;
            String reorderCols = null;
            if(ebwForm.getReorderCols() != null && ebwForm.getReorderCols().length() > 0)
            {
                reorderColsStr = ebwForm.getReorderCols();
                String reorderStrArr[] = reorderColsStr.split(":");
                if(reorderStrArr != null && reorderStrArr.length > 0)
                    tableName = reorderStrArr[0];
                if(reorderStrArr != null && reorderStrArr.length > 1)
                    reorderCols = reorderStrArr[1];
            }
            EBWLogger.logDebug(this, (new StringBuilder("Re order columns ")).append(reorderColsStr).toString());
            String className = ebwForm.getClass().getName();
            String formName = className.substring(className.lastIndexOf(".") + 1, className.length());
            EBWLogger.logDebug(this, (new StringBuilder("Form Name ")).append(formName).toString());
            if(request.getSession().getAttribute(formName) != null)
            {
                EBWLogger.logDebug(this, "Form Bean is available in the session, so taking from the session");
                ebwForm = (EbwForm)request.getSession().getAttribute(formName);
            }
            cls = Class.forName(ebwForm.getClass().getName());
            ResourceBundle appRB = ResourceBundle.getBundle("ApplicationConfig");
            ResourceBundle formRB = ResourceBundle.getBundle(appRB.getString("message-resources"));
            String hiddenFldName = "";
            String reportTitle = "";
            LinkedHashMap fieldsMap = null;
            LinkedHashMap groupsMap = null;
            ArrayList tableInfo = null;
            LinkedHashMap reportMap = new LinkedHashMap();
            EBWLogger.logDebug(this, "Getting the Report Information");
            Vector reportInfo = ebwForm.getReportInformation();
            EBWLogger.logDebug(this, (new StringBuilder("Report Information is ")).append(reportInfo).toString());
            String screenName = "";
            String tempDispName = "";
            String tempFieldValue = "";
            for(Iterator iterator = reportInfo.iterator(); iterator.hasNext();)
            {
                Object groupInfo[][] = (Object[][])iterator.next();
                String groupName = "";
                for(int j = 0; j < groupInfo.length; j++)
                {
                    tableName = null;
                    String name = "";
                    String type = "";
                    Object dataInfo[] = groupInfo[j];
                    for(int k = 0; k < dataInfo.length; k++)
                        if(k == 0)
                        {
                            EBWLogger.logDebug(this, "Getting the DataInformation");
                            name = (String)dataInfo[k];
                            EBWLogger.logDebug(this, (new StringBuilder("Field Name ")).append(name).toString());
                        } else
                        if(k == 1)
                        {
                            EBWLogger.logDebug(this, "Getting Type of Data ");
                            type = (String)dataInfo[k];
                            EBWLogger.logDebug(this, (new StringBuilder("Field Type ")).append(type).toString());
                        }

                    if(type.equalsIgnoreCase("ScreenName"))
                    {
                        EBWLogger.logDebug(this, "Field Type is ScreenName ");
                        screenName = name;
                    } else
                    if(type.equalsIgnoreCase("Title"))
                    {
                        EBWLogger.logDebug(this, "Field Type is Title ");
                        reportTitle = formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(name).toString());
                    } else
                    if(type.equalsIgnoreCase("Group"))
                    {
                        EBWLogger.logDebug(this, "Field Type is Group ");
                        groupName = name;
                        String titleVal = formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(name).toString());
                        if(titleVal == null || titleVal.length() == 0)
                            groupName = (new StringBuilder(String.valueOf(ebwForm.getState()))).append(".").append(name).toString();
                    } else
                    if(type.equalsIgnoreCase("Table"))
                    {
                        if(tableName == null || tableName.length() < 0)
                            tableName = name;
                        if(tableName != null && tableName.equalsIgnoreCase(name))
                        {
                            EBWLogger.logDebug(this, "Field Type is Table");
                            String fldName = (new StringBuilder(String.valueOf(name))).append("FilHide").toString();
                            EBWLogger.logDebug(this, (new StringBuilder("HiddenFieldName ")).append(fldName).toString());
                            EBWLogger.logDebug(this, "Getting HiddenField Value ");
                            String hiddenValue = (String)getFieldValue(ebwForm, fldName, null);
                            if(hiddenValue == null)
                            {
                                EBWLogger.logDebug(this, "HiddenField Value is null");
                                hiddenValue = "";
                            }
                            String fieldName = (new StringBuilder(String.valueOf(name))).append("Collection").toString();
                            EBWLogger.logDebug(this, (new StringBuilder("Table Name  is ")).append(fieldName).toString());
                            EBWLogger.logDebug(this, "Getting the TableModel ");
                            DataInterface tableModel = (DataInterface)getFieldValue(ebwForm, fieldName, null);
                            EBWLogger.logDebug(this, (new StringBuilder("Table Model is ")).append(tableModel).toString());
                            EBWLogger.logDebug(this, (new StringBuilder("Table Name is ")).append(tableModel.getTableName()).toString());
                            ArrayList rows = (ArrayList)tableModel.getData();
                            ArrayList colAttrList = tableModel.getColAttrObjs();
                            LinkedHashMap headerDetails = null;
                            ArrayList header = null;
                            ArrayList tableModelHeader = tableModel.getRowHeader();
                            ArrayList removedList = null;
                            int colSize = colAttrList.size();
                            ArrayList colNameList = null;
                            ArrayList tableModelData = null;
                            EBWLogger.logDebug(this, (new StringBuilder("Tabel data information ")).append(rows).toString());
                            if(tableModelHeader != null)
                            {
                                for(int col = 0; col < colSize; col++)
                                {
                                    TableColAttrObj tableColAttrObj = (TableColAttrObj)colAttrList.get(col);
                                    if(colNameList == null)
                                        colNameList = new ArrayList();
                                    String colName = tableColAttrObj.getColName();
                                    colNameList.add(colName);
                                    EBWLogger.logDebug(this, (new StringBuilder("Column Name in TableColAttrObj")).append(colName).toString());
                                    if(headerDetails == null)
                                        headerDetails = new LinkedHashMap();
                                    String displayLabel = tableColAttrObj.getDisplayLabel();
                                    String fieldLabel = displayLabel.substring(displayLabel.indexOf("\"") + 1, displayLabel.lastIndexOf("\""));
                                    String fieldLabelName = displayLabel.substring(displayLabel.lastIndexOf(".") + 1, displayLabel.lastIndexOf("\""));
                                    System.out.println((new StringBuilder("fieldLabelName :")).append(fieldLabelName).toString());
                                    String displayName = null;
                                    LinkedHashMap headerMap = (LinkedHashMap)tableModelHeader.get(0);
                                    for(Iterator headerMapIterator = headerMap.keySet().iterator(); headerMapIterator.hasNext();)
                                    {
                                        String headerMapKey = (String)headerMapIterator.next();
                                        String headerMapValue = (String)headerMap.get(headerMapKey);
                                        if(fieldLabelName.equalsIgnoreCase(headerMapValue))
                                        {
                                            displayName = formRB.getString(fieldLabel);
                                            headerDetails.put(headerMapKey, displayName);
                                            break;
                                        }
                                    }

                                }

                                ArrayList headerNames = new ArrayList((Collection)((LinkedHashMap)tableModelHeader.get(0)).keySet());
                                ArrayList removeList = new ArrayList();
                                int nonExistingIndex = 0;
                                for(int headerNameIndex = 0; headerNameIndex < headerNames.size(); headerNameIndex++)
                                {
                                    String headerName = (String)headerNames.get(headerNameIndex);
                                    EBWLogger.logDebug(this, (new StringBuilder("Header Name ")).append(headerName).toString());
                                    boolean columnExists = false;
                                    for(int colNameIndex = 0; colNameIndex < colNameList.size(); colNameIndex++)
                                    {
                                        String colName = (String)colNameList.get(colNameIndex);
                                        EBWLogger.logDebug(this, (new StringBuilder("ColName ")).append(colName).toString());
                                        if(colName.indexOf("SelectId") == -1)
                                            if(headerName.equalsIgnoreCase(colName))
                                                columnExists = true;
                                            else
                                                nonExistingIndex = headerNameIndex;
                                    }

                                    EBWLogger.logDebug(this, (new StringBuilder("Column Exists ")).append(columnExists).toString());
                                    if(!columnExists)
                                    {
                                        EBWLogger.logDebug(this, (new StringBuilder("Non Existing Index ")).append(nonExistingIndex).toString());
                                        if(removeList == null)
                                            removeList = new ArrayList();
                                        removeList.add(new Integer(nonExistingIndex));
                                    }
                                }

                                if(header == null)
                                    header = new ArrayList();
                                header.add(headerDetails);
                                EBWLogger.logDebug(this, (new StringBuilder("Header Information is ")).append(header).toString());
                                if(removeList.isEmpty())
                                {
                                    EBWLogger.logDebug(this, "List does not contains any data");
                                } else
                                {
                                    ArrayList tempRowData = null;
                                    for(int rowsIndex = 0; rowsIndex < rows.size(); rowsIndex++)
                                    {
                                        EBWLogger.logDebug(this, (new StringBuilder("Inside rowindex loop -rowsIndex:")).append(rowsIndex).toString());
                                        ArrayList rowData = (ArrayList)rows.get(rowsIndex);
                                        tempRowData = new ArrayList(rowData);
                                        EBWLogger.logDebug(this, (new StringBuilder("tempRowData before remove:")).append(tempRowData).toString());
                                        int z = 0;
                                        for(int removeIndex = 0; removeIndex < removeList.size(); removeIndex++)
                                        {
                                            int index = ((Integer)removeList.get(removeIndex)).intValue();
                                            EBWLogger.logDebug(this, (new StringBuilder("Value of the index is :")).append(index).toString());
                                            tempRowData.remove(index - z);
                                            z++;
                                        }

                                        if(tableModelData == null)
                                            tableModelData = new ArrayList();
                                        tableModelData.add(tempRowData);
                                        EBWLogger.logDebug(this, (new StringBuilder("tempRowData after remove:")).append(tempRowData).toString());
                                    }

                                }
                            }
                            if(tableInfo == null)
                                tableInfo = new ArrayList();
                            tableInfo.add(header);
                            if(tableModelData != null)
                            {
                                EBWLogger.logDebug(this, "tableModelData an ArrayList instance is not null, so taking tableModelData for data information ");
                                tableInfo.add(tableModelData);
                            } else
                            {
                                EBWLogger.logDebug(this, "tableModelData an ArrayList instance is null  so taking rows an instance of ArrayList for data information ");
                                if(reportType != null && reportType.length() > 0)
                                    tableInfo.add(rows);
                                else
                                if(rows.size() == 0)
                                {
                                    if(tableModelData == null)
                                        tableModelData = new ArrayList();
                                    ArrayList noDataList = new ArrayList();
                                    noDataList.add(formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(name).append(".NoDataFoundMsg").toString()));
                                    tableModelData.add(noDataList);
                                    tableInfo.add(tableModelData);
                                } else
                                {
                                    tableInfo.add(rows);
                                }
                            }
                            header = null;
                            rows = null;
                            if(fieldsMap == null)
                                fieldsMap = new LinkedHashMap();
                            EBWLogger.logDebug(this, (new StringBuilder("Table Information ")).append(tableInfo).toString());
                            fieldsMap.put(formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(name).toString()), tableInfo);
                            tableInfo = null;
                        }
                    } else
                    {
                        if(fieldsMap == null)
                            fieldsMap = new LinkedHashMap();
                        String displayName = formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(name).toString());
                        String fieldValue = "";
                        if(displayName.equals(""))
                        {
                            fieldValue = (new StringBuilder(String.valueOf(tempFieldValue))).append("\t").append((String)getFieldValue(ebwForm, name, null)).toString();
                            displayName = tempDispName;
                        } else
                        {
                            fieldValue = (String)getFieldValue(ebwForm, name, null);
                        }
                        fieldsMap.put(displayName, fieldValue);
                        tempFieldValue = fieldValue;
                        tempDispName = displayName;
                    }
                    name = "";
                    type = "";
                    dataInfo = (Object[])null;
                }

                if(fieldsMap != null)
                {
                    if(groupsMap == null)
                        groupsMap = new LinkedHashMap();
                    groupsMap.put(formRB.getString((new StringBuilder(String.valueOf(screenName))).append(".").append(groupName).toString()), fieldsMap);
                    fieldsMap = null;
                }
                if(!reportTitle.equals(""))
                {
                    reportMap.put("ReportHeader", reportTitle);
                    reportTitle = "";
                }
                if(groupsMap != null)
                    reportMap.put("ReportGroup", groupsMap);
                groupInfo = (Object[][])null;
            }

            groupsMap = null;
            EBWLogger.logDebug(this, (new StringBuilder("ReportInformation is ")).append(reportMap).toString());
            request.getSession().setAttribute("ReportInformation", reportMap);
            reportMap = null;
        }
        catch(Exception e)
        {
            (new EbwException(this, e)).printEbwException();
        }
        if(reportType != null && reportType.length() > 0)
            return mapping.findForward(reportType);
        else
            return null;
    }

    private Object getFieldValue(EbwForm ebwForm, String fieldName, String fieldValue)
        throws Exception
    {
        char fieldChar[] = {
            fieldName.charAt(0)
        };
        String fldName = (new StringBuilder(String.valueOf((new String(fieldChar)).toUpperCase()))).append(fieldName.substring(1)).toString();
        EBWLogger.logDebug(this, "Invoking the method thru reflection ");
        String methodName = (new StringBuilder("get")).append(fldName).toString();
        EBWLogger.logDebug(this, (new StringBuilder("Method Name is ")).append(methodName).toString());
        EBWLogger.logDebug(this, (new StringBuilder("Param Value is ")).append(fieldValue).toString());
        Object params[] = (Object[])null;
        Class paramType[] = (Class[])null;
        Object value = null;
        if(fieldValue != null)
        {
            params = (new Object[] {
                fieldValue
            });
            paramType = new Class[]{String.class};
        }
        Method method = cls.getMethod(methodName, paramType);
        value = method.invoke(ebwForm, params);
        return value;
    }

    private Class cls;
}