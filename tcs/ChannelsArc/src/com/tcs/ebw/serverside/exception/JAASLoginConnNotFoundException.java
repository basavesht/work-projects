package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;

import java.util.HashMap;
/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class JAASLoginConnNotFoundException extends EbwException {

    /**
     * 
     */
    public JAASLoginConnNotFoundException(HashMap params) {
        super("SYS0002",params);
    }

}
