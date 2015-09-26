/*
 * Created on Nov 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.servicelocator;

import com.tcs.ebw.exception.EbwException;

/**
 * @author TCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServiceLocatorException extends EbwException {

    /**
     * @param exceptionKey
     */
    public ServiceLocatorException(String exceptionKey) {
        super(null, null);
    }

    /**
     * @param exceptionKey
     * @param obj
     */
    public ServiceLocatorException(String exceptionKey, Object obj) {
        super(null, null);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param exceptionKey
     * @param obj
     * @param exp
     */
    public ServiceLocatorException(String exceptionKey, Object obj,
            Exception exp) {
        super(null, null);
        // TODO Auto-generated constructor stub
    }

}
