package com.tcs.ebw.transferobject;

import java.util.Date;
import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class EBWActivityLogMasterTO extends EBWTransferObject{
    private String activityId=null;
    private String userId=null;
    private String groupId=null;
    private String custId=null;
    private String ipaddress=null;
    private Date datetime =null;
    private String serviceId=null;
    private String activityName=null;
    private String eventName=null;
    private String result=null;
    private String serviceParams=null;
    
    
    
    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    public String getActivityName() {
        return activityName;
    }
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    public Date getDatetime() {
        return datetime;
    }
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getIpaddress() {
        return ipaddress;
    }
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getServiceParams() {
        return serviceParams;
    }
    public void setServiceParams(String serviceParams) {
        this.serviceParams = serviceParams;
    }
}
