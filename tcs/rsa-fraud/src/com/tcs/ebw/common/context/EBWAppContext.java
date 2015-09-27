/*
 * Created on Jul 7, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.common.context;

import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
/**
 * @author TCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EBWAppContext {
    private UserPrincipal objUserPrincipal;
    private String screenName;
    private String screenState;
    private String screenAction;
    private String serviceId;
    private String result;
    
    /**
     * 
     */
    public EBWAppContext() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @return Returns the objUserPrincipal.
     */
    public UserPrincipal getUserPrincipal() {
        return objUserPrincipal;
    }
    /**
     * @param objUserPrincipal The objUserPrincipal to set.
     */
    public void setUserPrincipal(UserPrincipal objUserPrincipal) {
        this.objUserPrincipal = objUserPrincipal;
    }
    /**
     * @return Returns the screenAction.
     */
    public String getScreenAction() {
        return screenAction;
    }
    /**
     * @param screenAction The screenAction to set.
     */
    public void setScreenAction(String screenAction) {
        this.screenAction = screenAction;
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
     * @return Returns the screenState.
     */
    public String getScreenState() {
        return screenState;
    }
    /**
     * @param screenState The screenState to set.
     */
    public void setScreenState(String screenState) {
        this.screenState = screenState;
    }
    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }
    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
