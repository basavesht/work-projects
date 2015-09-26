/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;

import javax.security.auth.Subject;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import com.tcs.ebw.taglib.TagLibConstants;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.jaas.auth.Auth;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * This taglibrary is used in all action button places. 
 * It takes type, name, accesskey, alt, disabled, onclick, 
 * styleClass, tabindex, value, actiontype, validate and attribute 
 * as parameters. Type parameter is used for changing the button type. 
 * It can be Link, Image or a button. If the type is not given, 
 * it reads from ButtonResources.properties file to get the default 
 * button type.
 *
 * ActionType can be submit, button, cancel or reset. Cancel type of 
 * action calls history.back() method. Reset calls form.reset() method. 
 * Submit and button calls form’s onsubmit method. If the onsubmit method 
 * returns true, then it calls form.submit() method. Validate property 
 * is used to avoid validation while submitting.
 *
 * This taglibrary is added in a JSP file by JSPCreator for struts code 
 * type. For Html code type, it is called in the JSPCreator itself with 
 * htmloutput as true.
 * 
 * @author tcs
 */
public class EbwButton extends TagSupport{
	private String type=null;
	private String name=null;
	private String accesskey=null;
	private String validate="N";
	private String alt=null;
	private String disabled=null;
	private String onclick=null;
	private String styleClass=null;
	private String tabindex=null;
	private String value=null;
	private String rsBundle=null;
	private String actionType=null;
	private StringBuffer outputBuffer = null;
	private boolean htmlOutput = false;
	private String attribute = null; // Used for other attributes & events
	private String formname = null;
	private boolean isHtmlOutput = false;//added by 163974
	
	private ResourceBundle msgRes;//added by 163974
	private ResourceBundle bundle;

	public EbwButton(boolean htmlOutput) {
	    this.htmlOutput = htmlOutput;
	}
	
	public EbwButton() {
	    this.htmlOutput = false;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
		    String buttonFAPStyle = "";
			//System.out.println("Entered Buttontag");
			JspWriter out = null;
			if (!htmlOutput) {
			    out = pageContext.getOut();
			}
			
			StringBuffer buttonAttr= new StringBuffer();
			outputBuffer = new StringBuffer();
			if(!htmlOutput)
				msgRes=ResourceBundle.getBundle("com.tcs.MessageResources");//added by 163974
			bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_BUTTONS);
			
			buttonFAPStyle = bundle.getString("Button.FAP.style");
			
			/**
			 * FAP implementation
			 * 1. Get the FAP info from Session USER_FAP attribute
			 * 2. Check against the FAP and based on that show/hide or 
			 *    enable/disable based on the property value.
			 */
			if (buttonFAPStyle != null && !buttonFAPStyle.equals(TagLibConstants.EBWBUTTON_FAP_NONE)) {
				if (!actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_CANCEL) && 
						!actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_RESET)&& value!=null && !value.equalsIgnoreCase("Cancel")&&!value.equalsIgnoreCase("Clear")) {					
				    UserPrincipal objUserPrincipal = null;
				    try {
					    HttpSession session = pageContext.getSession();
						Set objSet = ((Subject)session.getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();
						Iterator objIterator= objSet.iterator();
						objUserPrincipal = (UserPrincipal) objIterator.next();
				    } catch (Exception exc) {
				        System.err.println (exc.getMessage());
				    }
				    
				    /**
				     * Getting FAP List from the session.
				     */
				    //System.out.println ("objUserPrincipal User ID : " + objUserPrincipal.getUsruserid());
				    
				    if (objUserPrincipal != null) {
					    /*objUserPrincipal = ((UserPrincipal) ((javax.servlet.http.HttpServletRequest) 
					            pageContext.getRequest()).getSession().getServletContext().getAttribute(
					            objUserPrincipal.getUsrgrpid() + "." + 
					            objUserPrincipal.getUsruserid() + "." + 
					            pageContext.getSession().getId()));*/

					    //System.out.println ("pageContext : " + (objUserPrincipal == null));
					    
					    HashMap userFAPMap = null;
					    
					    if (objUserPrincipal != null) {
					        userFAPMap = (HashMap) objUserPrincipal.getUserFap();
					        //System.out.println ("userFAP Map : " + userFAPMap);
					    }
					    
					    if (userFAPMap != null) {
					        if (formname != null) { 
								boolean notAccessable = checkFAP(userFAPMap, formname.substring(0, formname.indexOf("Form")), name);
								
								if (!notAccessable) {
								    /**
								     * Checking Button FAP Style.
								     */
									if (buttonFAPStyle.equals(TagLibConstants.EBWBUTTON_FAP_VISIBLE)) {
									    return SKIP_BODY;
									} else if (buttonFAPStyle.equals(TagLibConstants.EBWBUTTON_FAP_ENABLE)) {
									    disabled = "true";
									}
								}
					        }
					    }
				    }
				}
			}
			
			//Set default actionType if Null
			if(actionType==null) {
        		actionType =TagLibConstants.EBWBUTTON_ACTIONTYPE_BUTTON;
			}
			
			//	Set default type if Null
			if((type==null) || 
			        (type!=null && !type.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON) &&
			        !type.equals(TagLibConstants.EBWBUTTON_TYPE_LINK) && 
			        !type.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE))) {
				type=bundle.getString("Button.style");
			}
			if(!htmlOutput)
				value=msgRes.getString(formname.substring(0,formname.length()-4)+"."+name);//added by 163974
			if(value==null || value.trim().length()==0) {
			    value=bundle.getString(actionType);
			}
				
			
			/* This checking for calling validate in Submit and button components */
		    String bCancel = "false";
		    if (validate!=null && validate.toUpperCase().equals("N")) {
		        bCancel = "true";
		    }
		    
		    String strClick="";
		    
		    if (attribute != null && attribute.length() > 0 &&  
		            attribute.toLowerCase().indexOf("onclick") >= 0) {
		        int onclickLen="onclick=\"javascript:".length();
		        strClick=attribute.substring(attribute.toLowerCase().indexOf("onclick=\"javascript:")+onclickLen, attribute.indexOf("\"", onclickLen)) + ";";
		        attribute=attribute.substring(strClick.length()+onclickLen).trim();
		    }

			if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_JSBUTTON)) {
			    onclick = " \"javascript:" + strClick + ";\"";
			} else if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_CANCEL)) {
			    onclick = " \"javascript:bCancel=true;document." + formname + ".cancelFlag.value='true';history.back();document." + formname + ".cancelFlag.value='false';\"";
			} else if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_RESET)) {
		        //onclick = " \"javascript:bCancel=true;document." + formname + ".cancelFlag.value='true';document." + formname + ".reset();document." + formname + ".cancelFlag.value='false';\"";
				onclick = " \"javascript:bCancel=true;document." + formname + ".cancelFlag.value='true';clearValues(document." + formname+");document." + formname + ".cancelFlag.value='false';\"";//Modified by 163974
			} else if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_SUBMIT)) {
			    if ( bCancel.equals("false")) {
				    onclick = " \"javascript:bCancel=" + bCancel + "; document." + formname + ".cancelFlag.value='false';document." + formname + ".action.value='" + name + "';";
				    if (!type.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON)) {
				        onclick = onclick + " if(document." + formname + ".onsubmit()) { " + strClick + " document." + formname + ".submit(); }\"";
				    } else {
				        onclick = onclick + strClick + "\"";
				    }
			    } else {
				    onclick = " \"javascript:bCancel=" + bCancel + "; document." + formname + ".cancelFlag.value='true';document." + formname + ".action.value='" + name + "'; " + strClick; 
				    if (!type.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON)) {
				        onclick = onclick + strClick + " document." + formname + ".submit(); document." + formname + ".cancelFlag.value='false';\"";
				    } else {
				        onclick = onclick + "\"";
				    }
			    }
			    //!htmlOutput && 
			}else if (actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_BUTTON)) {
			    onclick = " \"javascript:bCancel=" + bCancel + "; document." + formname + ".action.value='" + name + "'; ";
			    if ( bCancel.equals("false")) {
			        onclick= onclick + " document." + formname + ".cancelFlag.value='false'; if(document." + formname + ".onsubmit()) { " + strClick + " document." + formname + ".submit(); }\"";
			    } else {
			        onclick= onclick + strClick + ";document." + formname + ".cancelFlag.value='true';document." + formname + ".submit();document." + formname + ".cancelFlag.value='false';\"";
			    }
			    //!htmlOutput && 
			}else if (actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_REPORT)) {
			    onclick = " \"javascript:bCancel=" + bCancel + "; document." + formname + ".action.value='" + name + "'; ";
		        onclick= onclick + " if(document." + formname + ".onsubmit()) { " + strClick + "; openExportDialog(document." + formname + "); document." + formname + ".submit(); }\"";
		        //!htmlOutput && 
			}else if (actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_CHART)) {
			    onclick = " \"javascript:bCancel=" + bCancel + "; document." + formname + ".action.value='" + name + "'; ";
		        onclick= onclick + strClick + "; openChartDialog(document." + formname + ", '" + name + "'); \"";
			}
			
			String buttonClass = bundle.getString("Button.Class") ;
			/*if(styleClass!=null || (buttonClass!=null && buttonClass.length() > 0)) {
			    styleClass = buttonClass;
        		buttonAttr.append(" class="+styleClass + " ");
        		System.out.println ("buttonAttr : " + buttonAttr);
			}*/
			
			//if styleClass passed then apply else apply class name mentioned in property file...changed for sysadmin
			if(styleClass !=null &&  styleClass.length()>0){
			    buttonAttr.append(" class="+styleClass + " ");
        	}else if(buttonClass !=null && buttonClass.length() > 0){
        		buttonAttr.append(" class="+buttonClass + " ");
        		
        	}
			
			if (attribute != null && attribute.length() > 0) {
			    buttonAttr.append(" " + attribute);
			}
			

			if((type.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON) || type.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE)) && (value!=null && value.length()>0)){
				
				buttonAttr.append(" name=\""+name+"\"");
				
				if(accesskey!=null) buttonAttr.append(" accesskey="+accesskey);
            	
            	if(tabindex!=null) buttonAttr.append(" tabindex=\""+tabindex+"\"");
            	buttonAttr.append(" alt=\""+value+"\"");
            	buttonAttr.append(" value=\""+value+"\"");
            	
    			if(onclick != null && onclick.length() > 0) buttonAttr.append(" onclick="+onclick);
    			if(disabled!=null && disabled.equalsIgnoreCase("true")) buttonAttr.append(" disabled ");
    			
    			
    			if (type.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE)) {
    			    if (attribute==null || (attribute!=null && attribute.toLowerCase().indexOf("src") < 0)) {
    			        buttonAttr.append(" src=\"" + bundle.getString(actionType + ".Image") + "\"");
    			    }
    			    outputBuffer.append("<input type=\"image\" "+ buttonAttr.toString()+ ">");
    			} else {
	    			if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_SUBMIT)) {
	    			    outputBuffer.append("<input type=\"submit\" "+buttonAttr.toString()+">");
	    			} else if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_RESET)) {
	    			    //outputBuffer.append("<input type=\"reset\" "+buttonAttr.toString()+">");
	    				outputBuffer.append("<input type=\"button\" "+buttonAttr.toString()+">");//Modified by 163974
	    			} else if(actionType.equals(TagLibConstants.EBWBUTTON_ACTIONTYPE_CANCEL)) {
	    			    outputBuffer.append("<input type=\"button\" "+buttonAttr.toString()+">");
	    			} else {
	    			    outputBuffer.append("<input type=\"button\" "+buttonAttr.toString()+">");
	    			}
    			}
        	} else if((type.equals(TagLibConstants.EBWBUTTON_TYPE_LINK)) && (value!=null && value.length()>0)){
        	    String link="\"#\"";
        		if (onclick!= null && onclick.length() > 0) buttonAttr.append(" onclick="+onclick);
    			buttonAttr.append(" name="+name);
    			
    			outputBuffer.append("<a href="+ link +" "+buttonAttr.toString()+">"+value+"</a>");
        	} 
        	
        	if (out!=null)
        	    out.println (outputBuffer);
        	
        	destroyObjects();
        	
		}catch(NullPointerException nullexp){
			new EbwException(this,nullexp).printEbwException();
		}
		catch(MissingResourceException missexp){
			new EbwException(this,missexp).printEbwException();
		}
		catch(ClassCastException cceexp){
			new EbwException(this,cceexp).printEbwException();
		}
		catch(Exception exp){
			new EbwException(this,exp).printEbwException();
		}
		//System.out.println("Exiting of Buttontag");
		return SKIP_BODY;
	}
	
	/**
	 * 
	 * @param fapMap
	 * @param scrID
	 * @param actionID
	 * @return
	 */
	private boolean checkFAP (HashMap fapMap, String scrID, String actionID) {
	    boolean retVal=false;
	    if (scrID != null && scrID.length() > 0 && 
	            actionID != null && actionID.length() > 0) {
	        String availableActions = (String) fapMap.get(scrID);
	        if (availableActions != null && availableActions.length() > 0) {
	            String actions[] = availableActions.split("~");
	            retVal = false;
	            for (int i=0; i < actions.length; i++) {
	                if (actions[i].equals(actionID)) {
	                    retVal = true;
	                    break;
	                }
	            }
	        }
	    }
	    return retVal;
	}
	
	private void destroyObjects() {
		type=null;
    	name=null;
    	accesskey=null;
    	alt=null;
    	disabled=null;
    	onclick=null;
    	styleClass=null;
    	tabindex=null;
    	value=null;
    	rsBundle=null;
    	actionType=null;
    	validate="N";
    	attribute=null;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the accesskey.
	 */
	public String getAccesskey() {
		return accesskey;
	}
	/**
	 * @param accesskey The accesskey to set.
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	/**
	 * @return Returns the alt.
	 */
	public String getAlt() {
		return alt;
	}
	/**
	 * @param alt The alt to set.
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}
	/**
	 * @return Returns the disabled.
	 */
	public String getDisabled() {
		return disabled;
	}
	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	/**
	 * @return Returns the onclick.
	 */
	public String getOnclick() {
		return onclick;
	}
	/**
	 * @param onclick The onclick to set.
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	
	/**
	 * @return Returns the rsBundle.
	 */
	public String getRsBundle() {
		return rsBundle;
	}
	/**
	 * @param rsBundle The rsBundle to set.
	 */
	public void setRsBundle(String rsBundle) {
		this.rsBundle = rsBundle;
	}

	/**
	 * @return Returns the styleClass.
	 */
	public String getStyleClass() {
		return styleClass;
	}
	/**
	 * @param styleClass The styleClass to set.
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	/**
	 * @return Returns the tabindex.
	 */
	public String getTabindex() {
		return tabindex;
	}
	/**
	 * @param tabindex The tabindex to set.
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
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
     * @return Returns the outputBuffer.
     */
    public StringBuffer getOutputBuffer() {
        return outputBuffer;
    }
    /**
     * @param outputBuffer The outputBuffer to set.
     */
    public void setOutputBuffer(StringBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }
    /**
     * @return Returns the validate.
     */
    public String getValidate() {
        return validate;
    }
    /**
     * @param validate The validate to set.
     */
    public void setValidate(String validate) {
        this.validate = validate;
    }
    /**
     * @return Returns the attribute.
     */
    public String getAttribute() {
        return attribute;
    }
    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    /**
     * @return Returns the formname.
     */
    public String getFormname() {
        return formname;
    }
    /**
     * @param formname The formname to set.
     */
    public void setFormname(String formname) {
        this.formname = formname;
    }
    
    public boolean isHtmlOutput() {
		return isHtmlOutput;
	}

	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}
}
