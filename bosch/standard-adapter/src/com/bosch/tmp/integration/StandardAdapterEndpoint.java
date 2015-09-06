
package com.bosch.tmp.integration;

import com.bosch.th.integration.StandardAdapterPortType;
import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 *
 * @author gtk1pal
 */
@Interceptors(
{
    SpringBeanAutowiringInterceptor.class
})
@Configurable
@WebService(serviceName = "StandardAdapterService",
        portName = "StandardAdapterHttpPort",
        endpointInterface = "com.bosch.th.integration.StandardAdapterPortType",
        targetNamespace = "http://th.bosch.com/integration",
        wsdlLocation = "WEB-INF/wsdl/StandardAdapterEndpoint/rb-tmp-dev.de.bosch.com/tsr/TH/INT/StandardAdapter/1/StandardAdapter.wsdl")
@HandlerChain(file = "jaxws-soap-handlers.xml")
public class StandardAdapterEndpoint implements StandardAdapterPortType
{

    public static final Logger logger = Logger.getLogger(StandardAdapterEndpoint.class);

    @Resource
    WebServiceContext wsContext;

    @Autowired
    private StandardAdapterPortType standardAdapterBean;

    public org.hl7.v2xml.ACKCONTENT adtA01Operation(org.hl7.v2xml.ADTA01CONTENT part1)
    {
        logger.debug("Invoking StandardAdapterBean from adtA01Operation");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        return standardAdapterBean.adtA01Operation(part1);
    }

    public org.hl7.v2xml.ACKCONTENT adtA03Operation(org.hl7.v2xml.ADTA03CONTENT part1)
    {
        logger.debug("Invoking StandardAdapterBean from adtA03Operation");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        return standardAdapterBean.adtA03Operation(part1);
    }

    public org.hl7.v2xml.ACKCONTENT adtA05Operation(org.hl7.v2xml.ADTA05CONTENT part1)
    {
        logger.debug("Invoking StandardAdapterBean from adtA05Operation");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        return standardAdapterBean.adtA05Operation(part1);
    }

    public org.hl7.v2xml.ORUR30BATCHCONTENT qvrQ17Operation(org.hl7.v2xml.QVRQ17CONTENT part1)
    {
        logger.debug("Invoking StandardAdapterBean from qvrQ17Operation");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        return standardAdapterBean.qvrQ17Operation(part1);
    }

    public org.hl7.v2xml.ACKCONTENT ackBATCHOperation(org.hl7.v2xml.ACKBATCHCONTENT part1)
    {
        logger.debug("Invoking StandardAdapterBean from ackBATCHOperation");
        ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
        return standardAdapterBean.ackBATCHOperation(part1);
    }

    public org.hl7.v2xml.ORUR01BATCHCONTENT qvrQ17ORUR01Operation(org.hl7.v2xml.QVRQ17CONTENT part1) {
       logger.debug("Invoking StandardAdapterBean from qvrQ17Operation");
       ((StandardAdapterBean) standardAdapterBean).setWsContext(this.wsContext);
       return standardAdapterBean.qvrQ17ORUR01Operation(part1);
    }

}
