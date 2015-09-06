/* Developer Date        Code Comments
*  312822    20-Apr-2011 Change made for Third Party ACH-Phase 3 changes
*/
package com.tcs.ebw.common.util;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import java.util.*;


public class HookUtil {
    public HookUtil() {
    }

    /**
     * This method handles pagination after cancel event.
     * @@param objSourceForm
     * @@param objUserSessionObject
     * @@param action
     * @@param state
     * @@param isDiffScreen
     */
    public static void paginateAfterCancel(EbwForm objSourceForm,
        HashMap objUserSessionObject, String action, String state,
        String isDiffScreen) {
        objSourceForm.setAction(action);

        objSourceForm.setState(state);

        objUserSessionObject.put("isDiffScreen", isDiffScreen);
    }

    /**
     * This method modifies option/event terms table data by removing specified
     * starting text from option type
     * @@param inputList
     * @@param column
     * @@param fromString
     * @@param toString
     */

    //	public static void replaceColumnOfArrayList(ArrayList inputList,int column,String fromString,String toString){

    //		for(int i=1;i<inputList.size();i++) {

    //			ArrayList rowList=(ArrayList)inputList.get(i);

    //			String strInputColumnValue=(String)rowList.get(column);

    //					

    //			if(strInputColumnValue!=null) {

    //				if(strInputColumnValue.startsWith(fromString)){

    //					strInputColumnValue=strInputColumnValue.replaceFirst(fromString,toString);

    //					rowList.set(column,strInputColumnValue);

    //					inputList.set(i,rowList);

    //				}

    //			}			

    //		}         

    //	}
    public static void replaceColumnOfArrayList(ArrayList inputList,
        String columnName, String fromString, String toString) {
        EBWLogger.logDebug("HookUtil: ",
            "Inside replaceColumnOfArrayList" + columnName);

        int column = getColumnNumber(columnName);

        for (int i = 1; i < inputList.size(); i++) {
            ArrayList rowList = (ArrayList) inputList.get(i);

            String strInputColumnValue = (String) rowList.get(column);

            if (strInputColumnValue != null) {
                if (strInputColumnValue.startsWith(fromString)) {
                    strInputColumnValue = strInputColumnValue.replaceFirst(fromString,
                            toString);

                    rowList.set(column, strInputColumnValue);

                    inputList.set(i, rowList);
                }
            }
        }
    }

    public static int getColumnNumber(String queryColumn) {
        //System.out.println("HookUtil : Inside getColumnNumber method :" + queryColumn);
        String strQuery = queryColumn.substring(0, queryColumn.indexOf("."));

        String strColumn = queryColumn.substring(queryColumn.indexOf(".") + 1,
                queryColumn.length());

        int column = CallTableHeader.getIndex(strQuery, strColumn);

        //System.out.println("HookUtil : Output getColumnNumber method :" + column);
        return column;
    }

    /**
     * This method replace column of table based on some conidtion
     * @@param inputList i.e Input Table
     * @@param column i.e contains value which will replace
     * @@param colArray i.e columns which has to be replaced
     * @@param objHashData i.e it will check in HashMap for condition
     */

    //	public static void replaceCondlColumn(ArrayList inputList,int column,int[] colArray,HashMap objHashData){

    //		for(int i=1;i<inputList.size();i++) {

    //			ArrayList rowList=(ArrayList)inputList.get(i);

    //			String replaceString=(String)rowList.get(column);

    //			 if(!objHashData.isEmpty()) {

    //				if(objHashData.containsKey(replaceString)) {

    //					rowList.set(colArray[0],objHashData.get(replaceString));

    //					rowList.set(colArray[1],objHashData.get(replaceString));

    //				 }

    //			 } else {

    //					rowList.set(colArray[0],null);

    //					rowList.set(colArray[1],null);

    //			 }

    //		}         

    //	}
    public static void replaceCondlColumn(ArrayList inputList,
        String columnName, String[] colArray, HashMap objHashData) {
        int searchColumn = getColumnNumber(columnName);

        int replaceColumn1 = getColumnNumber(colArray[0]);

        int replaceColumn2 = getColumnNumber(colArray[1]);

        for (int i = 1; i < inputList.size(); i++) {
            ArrayList rowList = (ArrayList) inputList.get(i);

            String replaceString = (String) rowList.get(searchColumn);

            if (!objHashData.isEmpty()) {
                if (objHashData.containsKey(replaceString)) {
                    rowList.set(replaceColumn1, objHashData.get(replaceString));

                    rowList.set(replaceColumn2, objHashData.get(replaceString));
                }
            } else {
                rowList.set(replaceColumn1, null);

                rowList.set(replaceColumn2, null);
            }
        }
    }

    /**
     * This method modifies the domain description value.
     * @@param inputList
     * @@param column
     * @@param domainVal
     */

    //	public static void modifyDomainDesc(ArrayList inputList,int column,String domainVal){

    //		String strOutputColumnValue;	

    //		for(int i=1;i<inputList.size();i++){

    //		  ArrayList rowList=(ArrayList)inputList.get(i);

    //		  String strInputColumnValue = (String)rowList.get(column);

    //		  strOutputColumnValue=ComboboxData.getDomainValDesc(domainVal,strInputColumnValue);

    //		  rowList.set(column,strOutputColumnValue);        		

    //       	}        	 

    //	}
    public static void modifyDomainDesc(ArrayList inputList, String columnName,
        String domainVal) {
        //System.out.println("HookUtil :modifyDomainDesc Method : columnName" + columnName);
        EBWLogger.logDebug("HookUtil: ", "Inside modifyDomainDesc" + domainVal);

        String strOutputColumnValue;

        int column = getColumnNumber(columnName);

        for (int i = 1; i < inputList.size(); i++) {
            ArrayList rowList = (ArrayList) inputList.get(i);

            String strInputColumnValue = (String) rowList.get(column);

            // System.out.println("Input Column Value:" + strInputColumnValue+ " And Column Name is :" + columnName);
            strOutputColumnValue = ComboboxData.getDomainValDesc(domainVal,
                    strInputColumnValue);

            // System.out.println("Output Column Value after getting Domain Desc:" + strOutputColumnValue);
            rowList.set(column, strOutputColumnValue);
        }
    }

    /**
     * This method build a SIIndicator hash map
     * @@param inputList
     * @@return SIIndicator map
     */
    public static HashMap storeListToHashmap(ArrayList inputList) {
        HashMap objHashValues = new HashMap();

        if (!(inputList.isEmpty() && (inputList.size() == 0))) {
            for (int i = 1; i < inputList.size(); i++) {
                ArrayList rowList = (ArrayList) inputList.get(i);

                String strSIIndicator = (String) rowList.get(0);

                String strBpId = (String) rowList.get(1);

                String strAccNum = (String) rowList.get(2);

                if (strAccNum.equals("ALL")) {
                    objHashValues.put(strBpId, strSIIndicator);
                }
                else {
                    objHashValues.put(strAccNum, strSIIndicator);
                }
            }
        }

        return objHashValues;
    }

    /* public static void setFAFlag(String strFANum,SfaSecFndAcctNDBTO objSfaSecFndAcctNDBTO){
            if(!strFANum.equals("FAALL") && !strFANum.equals("ALLFA") && !strFANum.equals("ALL"))
                    objSfaSecFndAcctNDBTO.setFAFLAG("N");
            else
                    objSfaSecFndAcctNDBTO.setFAFLAG("Y");
     }*/

    /* A Utility method added to enable the FAPcheck for Button and HrefAction components in EbwTable.
     * This functions needs to be called in the PostDelegateHook method*/
    public static String checkFAPForList(String status,
        UserPrincipal objUserPrincipal, String screenname) {
        String statusresult = "";

        HashMap fapMap = (HashMap) objUserPrincipal.getUserFap();

        ArrayList roleList = (ArrayList) objUserPrincipal.getRoleList();

        if ((screenname != null) && (screenname.length() > 0) &&
                (status != null) && (status.length() > 0)) {
            String fapActions = (String) fapMap.get(screenname);

            EBWLogger.logDebug("HookUtil:", "fapActions=");

            if (status.indexOf("View") != -1) {
                if (screenname.equalsIgnoreCase("ViewTrxnSummary")) {
                    if (status.indexOf("View,Edit,Cancel") != -1) {
                        if ((roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHIEDT")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "View,Edit,Cancel";
                        } else if ((roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHIEDT")) &&
                                !(roleList.contains("FSCHICNL"))) {
                            statusresult = "View,Edit";
                        } else if ((roleList.contains("FSCHIVEW")) &&
                                !(roleList.contains("FSCHIEDT")) &&
                                !(roleList.contains("FSCHICNL"))) {
                            statusresult = "View";
                        } else if (!(roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHIEDT")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "Edit,Cancel";
                        } else if (!(roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHIEDT")) &&
                                !(roleList.contains("FSCHICNL"))) {
                            statusresult = "Edit";
                        } else if (!(roleList.contains("FSCHIVEW")) &&
                                !(roleList.contains("FSCHIEDT")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "Cancel";
                        } else if ((roleList.contains("FSCHIVEW")) &&
                                !(roleList.contains("FSCHIEDT")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "View,Cancel";
                        }
                    }
                    else if (status.indexOf("View,Cancel") != -1) {
                        if ((roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "View,Cancel";
                        } else if ((roleList.contains("FSCHIVEW")) &&
                                !(roleList.contains("FSCHICNL"))) {
                            statusresult = "View";
                        } else if (!(roleList.contains("FSCHIVEW")) &&
                                (roleList.contains("FSCHICNL"))) {
                            statusresult = "Cancel";
                        }
                    } else if (status.indexOf("View") != -1) {
                        if (roleList.contains("FSCHIVEW")) {
                            statusresult = "View";
                        }
                    } else {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to Searches view action");
                    }
                } else if (screenname.equalsIgnoreCase("AuthorizeTrxnSummary")) {
                    if (roleList.contains("FAPRIVEW")) {
                        statusresult = "View";
                    } else {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to Approval view action");
                    }
                } else if (screenname.equalsIgnoreCase("ExtAccApprovalList")) {
                    if (roleList.contains("FACAIVEW")) {
                        statusresult = "View";
                    } else {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to ExtAccApprovalList view action");
                    }
                } else if (screenname.equalsIgnoreCase("ExtAcctMaintenance")) {
                    if (roleList.contains("FEAMIVEW")) {
                        statusresult = "View";
                    } else {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to EXTERNAL ACCOUNT MAINTANANCE view action");
                    }
                } else if (screenname.equalsIgnoreCase("TrialDeposit")) {
                    if (roleList.contains("FTRDIVEW")) {
                        statusresult = "View";
                    } else {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to TRIAL DEPOSIT view action");
                    }
                } else if (screenname.equalsIgnoreCase("BranchPrintManagerList")) {
                    statusresult = "";

                    if (roleList.contains("FBPMIVEW")) {
                        statusresult = statusresult.concat("View");
                    }

                    if (roleList.contains("FBPMIPCK") ||
                            roleList.contains("FBPMIVCK")) {
                        statusresult = statusresult.concat("Print");
                    }

                    if (statusresult.equals("")) {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to Branch Print Manager action");
                    }
                } else if (screenname.equalsIgnoreCase("LimitMaintenance")) {
                    statusresult = "";

                    if (roleList.contains("FLMTIVEW")) {
                        statusresult = statusresult.concat("View");
                    }

                    if (roleList.contains("FLMTIMOD")) {
                        statusresult = statusresult.concat("Edit");
                    }

                    if (roleList.contains("FLMTIDEL")) {
                        statusresult = statusresult.concat("Delete");
                    }

                    if (statusresult.equals("")) {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to Limit Maintenanace Action");
                    }
                } else if (screenname.equalsIgnoreCase("ReasonCodeMaintenance")) {
                    statusresult = "";

                    if (roleList.contains("FRCDIVEW")) {
                        statusresult = statusresult.concat("View");
                    }

                    if (roleList.contains("FRCDIMOD")) {
                        statusresult = statusresult.concat("Edit");
                    }

                    if (roleList.contains("FRCDIDEL")) {
                        statusresult = statusresult.concat("Delete");
                    }

                    if (statusresult.equals("")) {
                        EBWLogger.logDebug("HookUtil:",
                            " No Access to Reason Codes Action");
                    }
                }
            } else {
                if (status.indexOf("Manual Process,Cancel") != -1) {
                    if (screenname.equalsIgnoreCase("ChaseRejectListForm")) {
                        if (roleList.contains("FCRJIMPR") &&
                                roleList.contains("FCRJICAN")) {
                            statusresult = "Manual Process,Cancel";
                        } else if (roleList.contains("FCRJIMPR") &&
                                !(roleList.contains("FCRJICAN"))) {
                            statusresult = "Manual Process";
                        } else if (!(roleList.contains("FCRJIMPR")) &&
                                roleList.contains("FCRJICAN")) {
                            statusresult = "Cancel";
                        } else if (!(roleList.contains("FCRJIMPR")) &&
                                !(roleList.contains("FCRJICAN"))) {
                            statusresult = " ";
                        }
                    }
                } else {
                    if ((fapActions != null) && (fapActions.length() > 0)) {
                        String[] actions = fapActions.split("~");

                        String[] statusArr = status.split(",");

                        for (int i = 0; i < statusArr.length; i++) {
                            for (int j = 0; j < actions.length; j++) {
                                if (actions[j].equalsIgnoreCase(statusArr[i])) {
                                    statusresult = statusresult + statusArr[i] +
                                        ",";
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            statusresult = status;
        }

        if (statusresult.endsWith(",")) {
            statusresult = statusresult.substring(0, statusresult.length() - 1);
        }

        return statusresult;
    }
}
