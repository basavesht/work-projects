/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;

public interface TagLibConstants {
	//EbW Table Tag constants
	public final static String TABLE_TYPE_SELECT_CHECK="SelectCheck";
	public final static String TABLE_TYPE_SELECT_RADIO="SelectRadio";
	public final static String TABLE_TYPE_SELECT_RADIO_SHOW="SelectRadioShow";
	public final static String TABLE_TYPE_DISPLAY="Display";
	public final static String TABLE_TYPE_HIDDEN="Hidden";
	
	// EBW Button Styles
	public final static String EBWBUTTON_TYPE_BUTTON="Button";
	public final static String EBWBUTTON_TYPE_LINK ="Link";
	public final static String EBWBUTTON_TYPE_IMAGE ="Image";
	
	// EBW Button types
	public final static String EBWBUTTON_ACTIONTYPE_SUBMIT ="Submit";
	public final static String EBWBUTTON_ACTIONTYPE_RESET ="Reset";
	public final static String EBWBUTTON_ACTIONTYPE_CANCEL ="Cancel";
	public final static String EBWBUTTON_ACTIONTYPE_BUTTON ="Button"; 
	public final static String EBWBUTTON_ACTIONTYPE_REPORT ="Report"; 
	public final static String EBWBUTTON_ACTIONTYPE_JSBUTTON ="JSButton"; 
	public final static String EBWBUTTON_ACTIONTYPE_CHART ="Chart"; 

	// EBWButton FAP Style
	public final static String EBWBUTTON_FAP_VISIBLE ="VISIBLE";
	public final static String EBWBUTTON_FAP_ENABLE ="ENABLE";
	public final static String EBWBUTTON_FAP_NONE ="NONE";

	public final static String RESOURCE_BUNDLE_FILE_BUTTONS ="com.tcs.ebw.taglib.ButtonResource";
	public final static String RESOURCE_BUNDLE_FILE_SEARCHTEMPLATE ="com.tcs.ebw.taglib.SearchTemplateResource";
	public final static String RESOURCE_BUNDLE_FILE_TAGLIBMULTILINGUAL ="com.tcs.ebw.taglib.TaglibMultiLingualResource";
	public final static String RESOURCE_BUNDLE_FILE_MODECOMPONENT ="com.tcs.ebw.taglib.ComponentTypeResource";
	
	public final static String RESOURCE_BUNDLE_FILE_EBWTABLE ="com.tcs.ebw.taglib.EbwTableResource";
	
}
