package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class DataSourceCreationException extends EbwException {

     /**
     * @param arg0
     */
    public DataSourceCreationException(String datasource_name) {
        super("SYS0005",datasource_name);

    }

    
}
