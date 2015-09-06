/*
 * Created on Jul 19, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.common.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

public class TemplateTO extends EBWTransferObject implements java.io.Serializable{ 

	private String tptUserID;
	private String tptGropuID;
	private String tptTemplateName;
	private String tptTemplate;
	private String tptQlTask;
	private String tptScreenType;
	private String tptPublicFlag;
	private Object tptTemplateObj;
	
	public Object getTptTemplateObj() {
		return tptTemplateObj;
	}
	public void setTptTemplateObj(Object tptTemplateObj) {
		this.tptTemplateObj = tptTemplateObj;
	}
	/**
	 * @return Returns the tptQlTask.
	 */
	public String getTptQlTask() {
		return tptQlTask;
	}
	/**
	 * @param tptQlTask The tptQlTask to set.
	 */
	public void setTptQlTask(String tptQlTask) {
		this.tptQlTask = tptQlTask;
	}
	/**
	 * @return Returns the tptScreenType.
	 */
	public String getTptScreenType() {
		return tptScreenType;
	}
	/**
	 * @param tptScreenType the tptScreenType to set.
	 */
	public void setTptScreenType(String tptScreenType) {
		this.tptScreenType = tptScreenType;
	}
	/**
	 * @return Returns the tptTemplate.
	 */
	public String getTptTemplate() {
		return tptTemplate;
	}
	/**
	 * @param tptTemplate The tptTemplate to set.
	 */
	public void setTptTemplate(String tptTemplate) {
		this.tptTemplate = tptTemplate;
	}
	/**
	 * @return Returns the tptTemplateName.
	 */
	public String getTptTemplateName() {
		return tptTemplateName;
	}
	/**
	 * @param tptTemplateName The tptTemplateName to set.
	 */
	public void setTptTemplateName(String tptTemplateName) {
		this.tptTemplateName = tptTemplateName;
	}
	/**
	 * @return Returns the tptUserID.
	 */
	public String getTptUserID() {
		return tptUserID;
	}
	/**
	 * @param tptUserID The tptUserID to set.
	 */
	public void setTptUserID(String tptUserID) {
		this.tptUserID = tptUserID;
	}

	/**
	 * @return Returns the tptGropuID.
	 */
	public String getTptGropuID() {
		return tptGropuID;
	}
	/**
	 * @param tptGropuID The tptGropuID to set.
	 */
	public void setTptGropuID(String tptGropuID) {
		this.tptGropuID = tptGropuID;
	}
	/**
	 * @return Returns the tptPublicFlag.
	 */
	public String getTptPublicFlag() {
		return tptPublicFlag;
	}
	/**
	 * @param tptPublicFlag The tptPublicFlag to set.
	 */
	public void setTptPublicFlag(String tptPublicFlag) {
		this.tptPublicFlag = tptPublicFlag;
	}
}
