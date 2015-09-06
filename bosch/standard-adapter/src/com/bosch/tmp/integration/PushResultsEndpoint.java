package com.bosch.tmp.integration;

import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.bosch.th.integration.PushResultsPortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author gtk1pal
 */
@Interceptors(
{
    SpringBeanAutowiringInterceptor.class
})
@Configurable
@WebService(serviceName = "PushResultsService", portName = "PushResultsSOAPPort",
endpointInterface = "com.bosch.th.integration.PushResultsPortType", targetNamespace = "http://th.bosch.com/integration",
wsdlLocation = "WEB-INF/wsdl/PushResultsEndpoint/rb-tmp-dev.de.bosch.com/tsr/TH/INT/StandardAdapter/1/PushResults.wsdl.binding_soap.wsdl")
public class PushResultsEndpoint implements PushResultsPortType
{

    public static final Logger logger = LoggerFactory.getLogger(PushResultsEndpoint.class);

    @Resource
    WebServiceContext wsContext;

    @Autowired
    private PushResultsPortType standardAdapterBean;

    public void pushResults(com.bosch.th.integration.PushResults part1)
    {
        logger.debug("Invoking StandardAdapterBean from pushResults");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        standardAdapterBean.pushResults(part1);
    }
}
