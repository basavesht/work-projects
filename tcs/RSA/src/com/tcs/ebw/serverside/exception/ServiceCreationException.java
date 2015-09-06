package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;

public class ServiceCreationException extends EbwException{

    public ServiceCreationException(String serviceImplClass) {
        super("SYS0009",serviceImplClass);
    }

}
