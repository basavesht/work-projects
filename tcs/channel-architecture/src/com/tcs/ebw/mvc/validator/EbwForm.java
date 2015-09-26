/*
 * Created on Nov 9, 2005
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.mvc.validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import org.apache.struts.validator.ValidatorForm;

/**
 * <p>
 * EbwForm is the superclass for all FormBeans in 
 * this framework. This class has essential informations
 * about the form. Those are Action, State, ScreenName 
 * and Type of the Export option.
 * <p>
 * This class extends from ValidatorForm of struts. 
 * <p>
 * @author TCS
 */
public class EbwForm extends ValidatorForm {
	private String action;
	private String state;
	private String exportType;
	private String screenName;
	private String retainData;
	private String cancelFlag;
	private String paginationIndex;
	private String txnPwdValid;
	private String chartDetail;
	private String templateInfo;
	private String templateNameSelected;
	private String rowsPerPage;
	private String paginationDirection;
	private String actionType;
	private String restartKey ;
	private String exportPages;
	private String prevAction;
	private String preAction;
	private String preState;
	private String exportText;
	private String reorderCols;
	private String prevState;
	private String rowId;
	private String totalRows;
	private String ajaxCall;
	private String tableName;
	private String headerSortType;
	private String fieldName;
	private String rowIndex;
	private String userMessage;
	private String moduleName;
	private String autoCompleteHidden;
	private String currencyCode;
	private String overrideResp;
	private String supFlag;
	private String scrDataXmlStr;
	private String denomIn;
	private String denomOut;
	private String cashDrawerCurrency;
	private String cashDrawerAmount;
	private String cashDrawerAccount;
	private String cashDrawerAccountCurrency;
	private String cashDrawerTxnStatus;
	private String cashDrawerFeeBasis;
	private String cashDrawerChargeFeeToAcc;
	private String cashDrawerTotalFeePayable;
	private String versionnum;
	private String batchVersionnum;
	private String parTxnVersionnum;
	
	public String getAutoCompleteHidden() {
		return autoCompleteHidden;
	}

	public void setAutoCompleteHidden(String autoCompleteHidden) {
		this.autoCompleteHidden = autoCompleteHidden;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
    public String getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}

	/**
     * 
     */
    public EbwForm() {
        super();
    }

    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * This method used for returning Report Informations
     * @return Vector 
     */
    public Vector getReportInformation() {
        return null;
    }
    
    /**
     * This method used for returning Chart Informations
     * @return Vector 
     */
    public Vector getChartInformation(String chartAction) {
        return null;
    }
    
    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }
    /**
     * @param state The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * @return Returns the exportType.
     */
    public String getExportType() {
        return exportType;
    }
    /**
     * @param exportType The exportType to set.
     */
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
    /**
     * @return Returns the screenName.
     */
    public String getScreenName() {
        return screenName;
    }
    /**
     * @param screenName The screenName to set.
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    
    /**
     * Creates JavaScript Array in UI
     * 
     * @param comboName
     * @param descCollection
     * @return
     */
    protected String getComboDesc(String comboName, Vector descCollection) {
        StringBuffer strBDescJS = new StringBuffer();
        if (descCollection!=null) {
	        strBDescJS.append("var arr" + comboName + "=new Array();\r\n");
	        strBDescJS.append("arr" + comboName + "[0] = \"\";\r\n");
	        for (int i=0,j=descCollection.size(); i < j; i++) {
		        strBDescJS.append("arr" + comboName + "[" + (i+1) + "] = ");
	            strBDescJS.append("\"" + descCollection.get(i) + "\";\r\n");
	        }
        }
        return strBDescJS.toString();
    }
    
    /**
     * This method is used for converting Table data into to String
     * 
     * @param list
     * @return
     */
    protected String getTableDataFromDI(com.tcs.ebw.taglib.DataInterface di) {
    	StringBuffer strBTblCollection = new StringBuffer();
        ArrayList colDataList =(ArrayList) di.getData();
        ArrayList colHd = di.getRowHeader();
        
        if (colHd!=null && colHd.size() > 0) {
	        LinkedHashMap colHdMap = (LinkedHashMap)colHd.get(0);
	        Iterator objHds = colHdMap.keySet().iterator();
	        while (objHds.hasNext()) {
	            strBTblCollection.append(objHds.next());
	            strBTblCollection.append("##");
	        }
	        strBTblCollection.append("~~");
        }
        
        if (colDataList!=null && colDataList.size() > 0) {
	        ArrayList colList = null;
	        	        
	        for (int i=0, j=colDataList.size(); i < j; i++) {
	            colList = (ArrayList) colDataList.get(i);
	            for (int cols=0, colsize=colList.size(); cols < colsize; cols++) {
	                strBTblCollection.append (colList.get(cols));
	                if (cols < colsize - 1) {
	                    strBTblCollection.append ("##");
	                }
	            }
                strBTblCollection.append ("~~");
	        }
        }
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("getTableDataFromDI"+ strBTblCollection.toString()); 
        return strBTblCollection.toString();
    }
    
    /**
     * @return Returns the retainData.
     */
    public String getRetainData() {
        return retainData;
    }
    /**
     * @param retainData The retainData to set.
     */
    public void setRetainData(String retainData) {
        this.retainData = retainData;
    }
    /**
     * @return Returns the cancelFlag.
     */
    public String getCancelFlag() {
        return cancelFlag;
    }
    /**
     * @param cancelFlag The cancelFlag to set.
     */
    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }
    /**
     * @return Returns the paginationIndex.
     */
    public String getPaginationIndex() {
        return paginationIndex;
    }
    /**
     * @param paginationIndex The paginationIndex to set.
     */
    public void setPaginationIndex(String paginationIndex) {
        this.paginationIndex = paginationIndex;
    }
    /**
     * @return Returns the txnPwdValid.
     */
    public String getTxnPwdValid() {
        return txnPwdValid;
    }
    /**
     * @param txnPwdValid The txnPwdValid to set.
     */
    public void setTxnPwdValid(String txnPwdValid) {
        this.txnPwdValid = txnPwdValid;
    }
    /**
     * @return Returns the chartDetail.
     */
    public String getChartDetail() {
        return chartDetail;
    }
    /**
     * @param chartDetail The chartDetail to set.
     */
    public void setChartDetail(String chartDetail) {
        this.chartDetail = chartDetail;
    }
    
    public String getTemplateInfo() {
        return templateInfo;
    }
    public void setTemplateInfo(String templateInfo) {
        this.templateInfo = templateInfo;
    }
    public String getTemplateNameSelected() {
        return templateNameSelected;
    }
    public void setTemplateNameSelected(String templateNameSelected) {
        this.templateNameSelected = templateNameSelected;
    }
	/**
	 * @return Returns the preAction.
	 */
	public String getPreAction() {
		return preAction;
	}
	/**
	 * @param preAction The preAction to set.
	 */
	public void setPreAction(String preAction) {
		this.preAction = preAction;
	}
	/**
	 * @return Returns the preState.
	 */
	public String getPreState() {
		return preState;
	}
	/**
	 * @param preState The preState to set.
	 */
	public void setPreState(String preState) {
		this.preState = preState;
	}
	/**
	 * @return Returns the actionType.
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType The actionType to set.
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * @return Returns the exportPages.
	 */
	public String getExportPages() {
		return exportPages;
	}
	/**
	 * @param exportPages The exportPages to set.
	 */
	public void setExportPages(String exportPages) {
		this.exportPages = exportPages;
	}
	/**
	 * @return Returns the exportText.
	 */
	public String getExportText() {
		return exportText;
	}
	/**
	 * @param exportText The exportText to set.
	 */
	public void setExportText(String exportText) {
		this.exportText = exportText;
	}
	/**
	 * @return Returns the paginationDirection.
	 */
	public String getPaginationDirection() {
		return paginationDirection;
	}
	/**
	 * @param paginationDirection The paginationDirection to set.
	 */
	public void setPaginationDirection(String paginationDirection) {
		this.paginationDirection = paginationDirection;
	}
	/**
	 * @return Returns the prevAction.
	 */
	public String getPrevAction() {
		return prevAction;
	}
	/**
	 * @param prevAction The prevAction to set.
	 */
	public void setPrevAction(String prevAction) {
		this.prevAction = prevAction;
	}
	/**
	 * @return Returns the reorderCols.
	 */
	public String getReorderCols() {
		return reorderCols;
	}
	/**
	 * @param reorderCols The reorderCols to set.
	 */
	public void setReorderCols(String reorderCols) {
		this.reorderCols = reorderCols;
	}
	/**
	 * @return Returns the restartKey.
	 */
	public String getRestartKey() {
		return restartKey;
	}
	/**
	 * @param restartKey The restartKey to set.
	 */
	public void setRestartKey(String restartKey) {
		this.restartKey = restartKey;
	}
	/**
	 * @return Returns the rowsPerPage.
	 */
	public String getRowsPerPage() {
		return rowsPerPage;
	}
	/**
	 * @param rowsPerPage The rowsPerPage to set.
	 */
	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @return the prevState
	 */
	public String getPrevState() {
		return prevState;
	}

	/**
	 * @param prevState the prevState to set
	 */
	public void setPrevState(String prevState) {
		this.prevState = prevState;
	}

	/**
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	/**
	 * @return the ajaxCall
	 */
	public String getAjaxCall() {
		return ajaxCall;
	}

	/**
	 * @param ajaxCall the ajaxCall to set
	 */
	public void setAjaxCall(String ajaxCall) {
		this.ajaxCall = ajaxCall;
	}

	/**
	 * @return the headerSortType
	 */
	public String getHeaderSortType() {
		return headerSortType;
	}

	/**
	 * @param headerSortType the headerSortType to set
	 */
	public void setHeaderSortType(String headerSortType) {
		this.headerSortType = headerSortType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getScrDataXmlStr() {
		return scrDataXmlStr;
	}

	public void setScrDataXmlStr(String scrDataXmlStr) {
		this.scrDataXmlStr = scrDataXmlStr;
	}

	public String getSupFlag() {
		return supFlag;
	}

	public void setSupFlag(String supFlag) {
		this.supFlag = supFlag;
	}

	public String getCashDrawerAccount() {
		return cashDrawerAccount;
	}

	public void setCashDrawerAccount(String cashDrawerAccount) {
		this.cashDrawerAccount = cashDrawerAccount;
	}

	public String getCashDrawerAccountCurrency() {
		return cashDrawerAccountCurrency;
	}

	public void setCashDrawerAccountCurrency(String cashDrawerAccountCurrency) {
		this.cashDrawerAccountCurrency = cashDrawerAccountCurrency;
	}

	public String getCashDrawerAmount() {
		return cashDrawerAmount;
	}

	public void setCashDrawerAmount(String cashDrawerAmount) {
		this.cashDrawerAmount = cashDrawerAmount;
	}

	public String getCashDrawerChargeFeeToAcc() {
		return cashDrawerChargeFeeToAcc;
	}

	public void setCashDrawerChargeFeeToAcc(String cashDrawerChargeFeeToAcc) {
		this.cashDrawerChargeFeeToAcc = cashDrawerChargeFeeToAcc;
	}

	public String getCashDrawerCurrency() {
		return cashDrawerCurrency;
	}

	public void setCashDrawerCurrency(String cashDrawerCurrency) {
		this.cashDrawerCurrency = cashDrawerCurrency;
	}

	public String getCashDrawerFeeBasis() {
		return cashDrawerFeeBasis;
	}

	public void setCashDrawerFeeBasis(String cashDrawerFeeBasis) {
		this.cashDrawerFeeBasis = cashDrawerFeeBasis;
	}

	public String getCashDrawerTotalFeePayable() {
		return cashDrawerTotalFeePayable;
	}

	public void setCashDrawerTotalFeePayable(String cashDrawerTotalFeePayable) {
		this.cashDrawerTotalFeePayable = cashDrawerTotalFeePayable;
	}

	public String getCashDrawerTxnStatus() {
		return cashDrawerTxnStatus;
	}

	public void setCashDrawerTxnStatus(String cashDrawerTxnStatus) {
		this.cashDrawerTxnStatus = cashDrawerTxnStatus;
	}

	public String getDenomIn() {
		return denomIn;
	}

	public void setDenomIn(String denomIn) {
		this.denomIn = denomIn;
	}

	public String getDenomOut() {
		return denomOut;
	}

	public void setDenomOut(String denomOut) {
		this.denomOut = denomOut;
	}

	public String getOverrideResp() {
		return overrideResp;
	}

	public void setOverrideResp(String overrideResp) {
		this.overrideResp = overrideResp;
	}

	public String getVersionnum() {
		return versionnum;
	}

	public void setVersionnum(String versionnum) {
		this.versionnum = versionnum;
	}

	public String getBatchVersionnum() {
		return batchVersionnum;
	}

	public void setBatchVersionnum(String batchVersionnum) {
		this.batchVersionnum = batchVersionnum;
	}

	public String getParTxnVersionnum() {
		return parTxnVersionnum;
	}

	public void setParTxnVersionnum(String parTxnVersionnum) {
		this.parTxnVersionnum = parTxnVersionnum;
	}
}