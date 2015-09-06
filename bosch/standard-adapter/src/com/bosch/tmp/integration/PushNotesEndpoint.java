
package com.bosch.tmp.integration;

import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import com.bosch.th.integration.PushNotesPortType;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 *
 * @author BEA2KOR
 */
@Interceptors({SpringBeanAutowiringInterceptor.class})
@Configurable
@WebService(serviceName = "PushNotesService",
			portName = "PushNotesHttpPort",
			endpointInterface = "com.bosch.th.integration.PushNotesPortType",
			targetNamespace = "http://th.bosch.com/integration",
			wsdlLocation = "WEB-INF/wsdl/PushNotesEndpoint/rb-tmp-dev.de.bosch.com/tsr/TH/INT/StandardAdapter/1/PushNotes.wsdl.binding_soap.wsdl")
@HandlerChain(file = "jaxws-soap-handlers.xml")
public class PushNotesEndpoint implements PushNotesPortType
{
	public static final Logger logger = Logger.getLogger(PushNotesEndpoint.class);

	@Resource
	WebServiceContext wsContext;

	public void pushNotes(com.bosch.th.integration.notes.PushNotes part1) {
		logger.debug("Invoking PushNotesBean from pushNotes");
		logger.debug("PushNotes service is binding with JMS request and hence HTTP definition for the end-point operation is not defined yet");
	}
}
