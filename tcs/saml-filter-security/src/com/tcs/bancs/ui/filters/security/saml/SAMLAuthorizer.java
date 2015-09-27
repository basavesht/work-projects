package com.tcs.bancs.ui.filters.security.saml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.ProxyRestriction;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.ProxyRestrictionBuilder;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.core.impl.StatusMessageBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationDataBuilder;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.signature.impl.SignatureBuilder;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;

import sun.misc.BASE64Encoder;

import com.tcs.bancs.ui.filters.helpers.HttpServletRequestWrapperImpl;
import com.tcs.bancs.ui.helpers.Base64Util;
import com.tcs.bancs.ui.helpers.security.CryptUtils;
import com.tcs.bancs.ui.helpers.security.EncryptionException;
import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.security.saml.SAMLValidator;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class SAMLAuthorizer
    implements Filter
{

    public SAMLAuthorizer()
    {
        samlConfig = null;
        buildersReady = false;
    }

    public void init(FilterConfig config)
        throws ServletException
    {
        filterConfig = config;
    }

    public void initialize(FilterConfig config)
        throws FilterConfigException
    {
        if(logger.isDebugEnabled())
            logger.debug("initialize(FilterConfig) - start");
        Security.addProvider(new BouncyCastleProvider());
        String configFileName = config.getInitParameter("config");
        if(configFileName == null)
            throw new FilterConfigException("Init parameter 'config' must be specified for Servlet Filter 'SAMLAuthorizer'.");
        samlConfig = SAMLConfiguration.getInstance();
        try
        {
            samlConfig.parse(configFileName);
        }
        catch(FileNotFoundException e)
        {
            logger.error("initialize(FilterConfig)", e);
            throw new FilterConfigException((new StringBuilder("Config file '")).append(configFileName).append("' not found.").toString(), e);
        }
        catch(ConfigXMLParsingException e)
        {
            logger.error("initialize(FilterConfig)", e);
            throw new FilterConfigException("XML Parsing exception for config file.", e);
        }
        if(logger.isDebugEnabled())
            logger.debug("initialize(FilterConfig) - end");
    }

    public void initialize(String configFileName)
        throws FilterConfigException
    {
        if(logger.isDebugEnabled())
            logger.debug("initialize(FilterConfig) - start");
        Security.addProvider(new BouncyCastleProvider());
        if(configFileName == null)
            throw new FilterConfigException("Init parameter 'config' must be specified for Servlet Filter 'SAMLAuthorizer'.");
        samlConfig = SAMLConfiguration.getInstance();
        try
        {
            samlConfig.parse(configFileName);
        }
        catch(FileNotFoundException e)
        {
            logger.error("initialize(FilterConfig)", e);
            throw new FilterConfigException((new StringBuilder("Config file '")).append(configFileName).append("' not found.").toString(), e);
        }
        catch(ConfigXMLParsingException e)
        {
            logger.error("initialize(FilterConfig)", e);
            throw new FilterConfigException("XML Parsing exception for config file.", e);
        }
        if(logger.isDebugEnabled())
            logger.debug("initialize(FilterConfig) - end");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        if(logger.isDebugEnabled())
            logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");
        System.out.println("doFilter(ServletRequest, ServletResponse, FilterChain) - start");
        String samlResponseXML = "<Not Available>";
        HttpServletRequest httpRequest = null;
        HttpServletResponse httpResponse = null;
        try
        {
            httpRequest = new HttpServletRequestWrapperImpl((HttpServletRequest)request);
            httpResponse = (HttpServletResponse)response;
        }
        catch(ClassCastException e)
        {
            logger.error("doFilter(ServletRequest, ServletResponse, FilterChain)", e);
            chain.doFilter(request, response);
            if(logger.isDebugEnabled())
                logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
            return;
        }
        if(logger.isDebugEnabled())
            logger.debug(String.format("Requested URI = '%s'", new Object[] {
                httpRequest.getRequestURI()
            }));
        if(httpRequest.getRequestURI().endsWith("/logout"))
        {
            logger.info("Logout request received");
            HttpSession session = httpRequest.getSession(false);
            if(session != null)
                session.invalidate();
            httpResponse.setContentType("image/jpg");
            httpResponse.setStatus(200);
            deleteAllCookies(httpRequest, httpResponse);
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        if(samlConfig == null){
            try
            {
                initialize(filterConfig);
            }
            catch(FilterConfigException e)
            {
            	samlConfig=null;//Do not keep half initialized configuration
                logger.error("doFilter(ServletRequest, ServletResponse, FilterChain)", e);
                redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                if(logger.isDebugEnabled())
                    logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
                return;
            }
        }else{
            try
            {
                samlConfig.checkNew();
            }
            catch(ConfigXMLParsingException e)
            {
            	samlConfig=null;//Do not keep half initialized configuration
                logger.error("doFilter(ServletRequest, ServletResponse, FilterChain)", e);
                redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                if(logger.isDebugEnabled())
                    logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
                return;
            }
        }
        if(samlConfig.isSimulated())
        {
            HttpSession session = httpRequest.getSession(false);
            String assertion = null;
            if(session == null)
            {
                if(httpRequest.getRequestedSessionId() != null)
                {
                    httpResponse.setStatus(200);
                    deleteAllCookies(httpRequest, httpResponse);
                    try{
                    	redirectToCurrentPageViaIDP(httpRequest, httpResponse);
                    }catch(ConfigXMLParsingException e){
                        redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                        return;                    	
                    }
                    return;
                }
                try
                {
                    assertion = createAssertion();
                }
                catch(KeyStoreException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                catch(NoSuchAlgorithmException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                catch(CertificateException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                catch(UnrecoverableKeyException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                catch(ProfileException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                catch(MarshallingException e)
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, e);
                    return;
                }
                if(assertion != null)
                {
                    httpRequest.getParameterMap().put(samlConfig.getPostAttrSAML(), new String[] {
                        assertion
                    });
                } else
                {
                    redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, null);
                    return;
                }
            }
        }
        try
        {
            long start = 0L;
            long end = 0L;
            if(logger.isInfoEnabled())
                start = System.nanoTime();
            samlResponseXML = doHttpFilter(httpRequest, httpResponse, chain);
            if(logger.isInfoEnabled())
            {
                end = System.nanoTime();
                logger.info(String.format("doHttpFilter execution time : %d ns", new Object[] {
                    Long.valueOf(end - start)
                }));
                logger.info(String.format("===== SAML Response XML Received ===== %s %s", new Object[] {
                    System.getProperty("line.separator"), samlResponseXML
                }));
            }
        }
        catch(Throwable t)
        {
            logger.error("doFilter(ServletRequest, ServletResponse, FilterChain)", t);
            redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, samlResponseXML, t);
            if(logger.isDebugEnabled())
                logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
            return;
        }
        if(logger.isDebugEnabled())
            logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
    }

    private String createAssertion()
        throws ProfileException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, MarshallingException, IOException
    {
        if(!buildersReady)
        {
            responseBuilder = new ResponseBuilder();
            statusBuilder = new StatusBuilder();
            statusCodeBuilder = new StatusCodeBuilder();
            statusMessageBuilder = new StatusMessageBuilder();
            issuerBuilder = new IssuerBuilder();
            assertionBuilder = new AssertionBuilder();
            subjectBuilder = new SubjectBuilder();
            subjectConfirmationBuilder = new SubjectConfirmationBuilder();
            subjectConfirmationDataBuilder = new SubjectConfirmationDataBuilder();
            conditionsBuilder = new ConditionsBuilder();
            audienceRestrictionBuilder = new AudienceRestrictionBuilder();
            proxyRestrictionBuilder = new ProxyRestrictionBuilder();
            audienceBuilder = new AudienceBuilder();
            signatureBuilder = new SignatureBuilder();
            buildersReady = true;
        }
        String xml = null;
        String audience = samlConfig.getMyURI();
        String assertionLifeTime = "5";
        String loginId = "simulated";
        String localEntity = "ClientServ";
        String remoteEntity = samlConfig.getLocalEntityId();
        SAML2RequestContext requestContext = createContext(audience, 60000 * Integer.valueOf(assertionLifeTime).intValue(), loginId, localEntity, remoteEntity);
        List statements = createStatements(requestContext);
        String subjectConfirmationMethod = "urn:oasis:names:tc:SAML:2.0:cm:bearer";
        xml = createXML(buildResponse(requestContext, subjectConfirmationMethod, statements));
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedResponse = encoder.encode(xml.getBytes());
        return encodedResponse;
    }

    private String createXML(Response response)
        throws MarshallingException
    {
        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(response);
        org.w3c.dom.Element responseElement = marshaller.marshall(response);
        return XMLHelper.nodeToString(responseElement);
    }

    private String generateIdentifier()
    {
        return UUID.randomUUID().toString();
    }

    protected Issuer buildEntityIssuer(SAML2RequestContext requestContext)
    {
        Issuer issuer = (Issuer)issuerBuilder.buildObject();
        issuer.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
        issuer.setValue(requestContext.getLocalEntityId());
        return issuer;
    }

    protected void populateStatusResponse(SAML2RequestContext requestContext, Response response)
    {
        response.setID(generateIdentifier());
        response.setVersion(SAMLVersion.VERSION_20);
        response.setIssuer(buildEntityIssuer(requestContext));
    }

    protected Assertion buildAssertion(SAML2RequestContext requestContext, DateTime issueInstant)
    {
        Assertion assertion = (Assertion)assertionBuilder.buildObject();
        assertion.setID(generateIdentifier());
        assertion.setIssueInstant(issueInstant);
        assertion.setVersion(SAMLVersion.VERSION_20);
        assertion.setIssuer(buildEntityIssuer(requestContext));
        Conditions conditions = buildConditions(requestContext, issueInstant);
        assertion.setConditions(conditions);
        return assertion;
    }

    protected Conditions buildConditions(SAML2RequestContext requestContext, DateTime issueInstant)
    {
        SAML2ProfileConfiguration profileConfig = requestContext.getProfileConfiguration();
        Conditions conditions = (Conditions)conditionsBuilder.buildObject();
        conditions.setNotBefore(issueInstant);
        conditions.setNotOnOrAfter(issueInstant.plus(profileConfig.getAssertionLifetime()));
        AudienceRestriction audienceRestriction = (AudienceRestriction)audienceRestrictionBuilder.buildObject();
        Audience audience = (Audience)audienceBuilder.buildObject();
        audience.setAudienceURI(requestContext.getInboundMessageIssuer());
        audienceRestriction.getAudiences().add(audience);
        Collection audiences = profileConfig.getAssertionAudiences();
        if(audiences != null && audiences.size() > 0)
        {
            for(Iterator iterator = audiences.iterator(); iterator.hasNext(); audienceRestriction.getAudiences().add(audience))
            {
                String audienceUri = (String)iterator.next();
                audience = (Audience)audienceBuilder.buildObject();
                audience.setAudienceURI(audienceUri);
            }

        }
        conditions.getAudienceRestrictions().add(audienceRestriction);
        audiences = profileConfig.getProxyAudiences();
        if(audiences != null && audiences.size() > 0)
        {
            ProxyRestriction proxyRestriction = (ProxyRestriction)proxyRestrictionBuilder.buildObject();
            for(Iterator iterator1 = audiences.iterator(); iterator1.hasNext(); proxyRestriction.getAudiences().add(audience))
            {
                String audienceUri = (String)iterator1.next();
                audience = (Audience)audienceBuilder.buildObject();
                audience.setAudienceURI(audienceUri);
            }

            proxyRestriction.setProxyCount(Integer.valueOf(profileConfig.getProxyCount()));
            conditions.getConditions().add(proxyRestriction);
        }
        return conditions;
    }

    protected SubjectConfirmation buildSubjectConfirmation(SAML2RequestContext requestContext, String confirmationMethod, DateTime issueInstant)
    {
        SubjectConfirmationData confirmationData = (SubjectConfirmationData)subjectConfirmationDataBuilder.buildObject();
        confirmationData.setNotOnOrAfter(issueInstant.plus(requestContext.getProfileConfiguration().getAssertionLifetime()));
        SubjectConfirmation subjectConfirmation = (SubjectConfirmation)subjectConfirmationBuilder.buildObject();
        subjectConfirmation.setMethod(confirmationMethod);
        subjectConfirmation.setSubjectConfirmationData(confirmationData);
        return subjectConfirmation;
    }

    protected NameID buildNameId(SAML2RequestContext requestContext)
        throws ProfileException
    {
        NameIDBuilder nb = new NameIDBuilder();
        NameID myNameID = nb.buildObject();
        myNameID.setValue(requestContext.getProfileConfiguration().getNameIdValue());
        myNameID.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        return myNameID;
    }

    protected Subject buildSubject(SAML2RequestContext requestContext, String confirmationMethod, DateTime issueInstant)
        throws ProfileException
    {
        Subject subject = (Subject)subjectBuilder.buildObject();
        subject.getSubjectConfirmations().add(buildSubjectConfirmation(requestContext, confirmationMethod, issueInstant));
        NameID nameID = buildNameId(requestContext);
        if(nameID == null)
        {
            return subject;
        } else
        {
            requestContext.setSubjectNameIdentifier(nameID);
            boolean nameIdEncRequiredByAuthnRequest = false;
            return subject;
        }
    }

    protected Status buildStatus(String topLevelCode, String secondLevelCode, String failureMessage)
    {
        Status status = (Status)statusBuilder.buildObject();
        StatusCode statusCode = (StatusCode)statusCodeBuilder.buildObject();
        statusCode.setValue(DatatypeHelper.safeTrimOrNullString(topLevelCode));
        status.setStatusCode(statusCode);
        if(secondLevelCode != null)
        {
            StatusCode secondLevelStatusCode = (StatusCode)statusCodeBuilder.buildObject();
            secondLevelStatusCode.setValue(DatatypeHelper.safeTrimOrNullString(secondLevelCode));
            statusCode.setStatusCode(secondLevelStatusCode);
        }
        if(failureMessage != null)
        {
            StatusMessage msg = (StatusMessage)statusMessageBuilder.buildObject();
            msg.setMessage(failureMessage);
            status.setStatusMessage(msg);
        }
        return status;
    }

    protected Response buildResponse(SAML2RequestContext requestContext, String subjectConfirmationMethod, List statements)
        throws ProfileException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, IOException
    {
        DateTime issueInstant = new DateTime();
        Response samlResponse = (Response)responseBuilder.buildObject();
        samlResponse.setIssueInstant(issueInstant);
        populateStatusResponse(requestContext, samlResponse);
        Assertion assertion = null;
        if(statements != null && !statements.isEmpty())
        {
            assertion = buildAssertion(requestContext, issueInstant);
            assertion.getStatements().addAll(statements);
            assertion.setSubject(buildSubject(requestContext, subjectConfirmationMethod, issueInstant));
            samlResponse.getAssertions().add(assertion);
        }
        Status status = buildStatus("urn:oasis:names:tc:SAML:2.0:status:Success", null, null);
        samlResponse.setStatus(status);
        return samlResponse;
    }

    private List<AttributeStatement> createStatements(SAML2RequestContext requestContext)
        throws ProfileException
    {
        AttributeStatement attstmt = buildAttributeStatement(requestContext);
        List<AttributeStatement> list = new ArrayList<AttributeStatement>();
        list.add(attstmt);
        return list;
    }

    protected AttributeStatement buildAttributeStatement(SAML2RequestContext requestContext)
        throws ProfileException
    {
        AttributeStatement attstmt = (new AttributeStatementBuilder()).buildObject();
        AttributeBuilder attbldr = new AttributeBuilder();
        Attribute attr = attbldr.buildObject();
        attr.setName("MMContext");
        XSStringBuilder stringBuilder = new XSStringBuilder();
        XSString stringValue = (XSString)stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        try
        {
            stringValue.setValue(Base64Util.encode(readFileAsString(samlConfig.getSimulatedContext()).getBytes()));
        }
        catch(IOException e)
        {
            throw new ProfileException("Error reading context file", e);
        }
        attr.getAttributeValues().add(stringValue);
        attstmt.getAttributes().add(attr);
        return attstmt;
    }

    private static String readFileAsString(String filePath)
        throws IOException
    {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char buf[] = new char[1024];
        for(int numRead = 0; (numRead = reader.read(buf)) != -1;)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        return fileData.toString();
    }

    private SAML2RequestContext createContext(String audience, long assertionLifeTime, String nameIdValue, String localEntityId, String inboundMessageIssuer)
    {
        SAML2RequestContext context = new SAML2RequestContext();
        context.setInboundMessageIssuer(inboundMessageIssuer);
        context.setLocalEntityId(localEntityId);
        SAML2ProfileConfiguration profileConfiguration = getProfile(audience, assertionLifeTime, nameIdValue);
        context.setProfileConfiguration(profileConfiguration);
        return context;
    }

    private void deleteAllCookies(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    {
        if(httpRequest.getCookies() != null)
        {
            Cookie acookie[] = httpRequest.getCookies();
            int i = 0;
            for(int j = acookie.length; i < j; i++)
            {
                Cookie cookie = acookie[i];
                Cookie delCookie = new Cookie(cookie.getName(), "");
                delCookie.setPath("/");
                delCookie.setMaxAge(0);
                httpResponse.addCookie(delCookie);
                Cookie delCookie1 = new Cookie(cookie.getName(), "");
                delCookie1.setMaxAge(0);
                httpResponse.addCookie(delCookie1);
            }

        }
    }

    private SAML2ProfileConfiguration getProfile(String audience, long assertionLifetime, String nameIdValue)
    {
        SAML2ProfileConfiguration profile = new SAML2ProfileConfiguration();
        profile.setAssertionLifetime(assertionLifetime);
        profile.setNameIdValue(nameIdValue);
        profile.setProxyCount(0);
        profile.getAssertionAudiences().add(audience);
        return profile;
    }

    private Cookie getPostAttrNotPresentCookie(HttpServletRequest httpRequest)
    {
        if(httpRequest.getCookies() != null)
        {
            Cookie acookie[] = httpRequest.getCookies();
            int i = 0;
            for(int j = acookie.length; i < j; i++)
            {
                Cookie cookie = acookie[i];
                if("PostAttrbuteNotPresentCk".equals(cookie.getName()))
                    return cookie;
            }

        }
        return null;
    }

    private void deletePostAttrNotPresentCookie(HttpServletResponse httpResponse)
    {
        Cookie cookie = new Cookie("PostAttrbuteNotPresentCk", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);
    }

    private void addPostAttrNotPresentCookie(HttpServletResponse httpResponse)
    {
        Cookie cookie = new Cookie("PostAttrbuteNotPresentCk", "Y");
        cookie.setPath("/");
        httpResponse.addCookie(cookie);
    }

    protected String doHttpFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse,FilterChain chain)
        throws IOException, ServletException, ConfigXMLParsingException
    {
        if(logger.isDebugEnabled())
            logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - start");
        String samlResponseXML = "<Not Available>";
        boolean isSessionPresent = false;
        HttpSession session = httpRequest.getSession(false);
        if(session != null)
        {
            String token = (String)session.getAttribute(SAML_AUTHENTICATED);
            if(token != null)
                isSessionPresent = true;
        }
        String samlResponse = httpRequest.getParameter(samlConfig.getPostAttrSAML());
        String base64encodedSAMLToken = samlResponse != null ? samlResponse.trim() : "";
        if(isSessionPresent && base64encodedSAMLToken.length() > 0)
        {
            session.invalidate();
            isSessionPresent = false;
        }
        if(!isSessionPresent)
        {
            if(base64encodedSAMLToken.length() == 0)
            {
                Cookie cookie = getPostAttrNotPresentCookie(httpRequest);
                if(cookie != null)
                {
                    deletePostAttrNotPresentCookie(httpResponse);
                    redirectToErrorPage(httpRequest, httpResponse, Errors.CONTEXT_DATA_EXTRACTION_FAILED, samlResponseXML, new Exception(String.format("No Post attribute '%s' found", new Object[] {
                        samlConfig.getPostAttrSAML()
                    })));
                } else
                {
                    addPostAttrNotPresentCookie(httpResponse);
                    redirectToCurrentPageViaIDP(httpRequest, httpResponse);
                }
                if(logger.isDebugEnabled())
                    logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - end");
                return samlResponseXML;
            }
            deletePostAttrNotPresentCookie(httpResponse);
            samlResponseXML = (new StringBuilder("(Base64 Encoded String Start) --> ")).append(base64encodedSAMLToken).append(" <-- (Base64 Encoded String End)").toString();
            byte SAMLToken[] = (byte[])null;
            Throwable samlDecodeExp = null;
            try
            {
                SAMLToken = Base64Util.decode(base64encodedSAMLToken);
                samlResponseXML = new String(SAMLToken);
            }
            catch(RuntimeException e)
            {
                logger.error("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String)", e);
                SAMLToken = (byte[])null;
                samlDecodeExp = e;
            }
            if(SAMLToken == null)
            {
                redirectToErrorPage(httpRequest, httpResponse, Errors.SAML_TOKEN_BASE64_DECODING_FAILED, samlResponseXML, samlDecodeExp);
                if(logger.isDebugEnabled())
                    logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - end");
                return samlResponseXML;
            }
            if(samlConfig.isSAMLTokenEncrypted())
                try
                {
                    long start = 0L;
                    long end = 0L;
                    if(logger.isInfoEnabled())
                        start = System.nanoTime();
                    SAMLToken = CryptUtils.decrypt(SAMLToken, samlConfig.getSAMLTokenDecryptionKey());
                    if(logger.isInfoEnabled())
                    {
                        end = System.nanoTime();
                        logger.info(String.format("CryptUtils.decrypt execution time : %d ns", new Object[] {
                            Long.valueOf(end - start)
                        }));
                    }
                }
                catch(EncryptionException e)
                {
                    logger.error("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String)", e);
                    redirectToErrorPage(httpRequest, httpResponse, Errors.SAML_TOKEN_DECRYPTION_FAILED, samlResponseXML, e);
                    if(logger.isDebugEnabled())
                        logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - end");
                    return samlResponseXML;
                }
            Object context = null;
            List attrStatements = null;
            try
            {
                long start = 0L;
                long end = 0L;
                if(logger.isInfoEnabled())
                    start = System.nanoTime();
                attrStatements = SAMLValidator.validate(SAMLToken, samlConfig);
                if(logger.isInfoEnabled())
                {
                    end = System.nanoTime();
                    logger.info(String.format("SAMLValidator.validate execution time : %d ns", new Object[] {
                        Long.valueOf(end - start)
                    }));
                }
            }
            catch(SAMLValidationException e)
            {
                logger.error("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String)", e);
                redirectToErrorPage(httpRequest, httpResponse, Errors.SAML_TOKEN_VALIDATION_FAILED, samlResponseXML, e);
                if(logger.isDebugEnabled())
                    logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - end");
                return samlResponseXML;
            }
            if(samlConfig.getContextDataExtractor() != null)
                try
                {
                    long start = 0L;
                    long end = 0L;
                    if(logger.isInfoEnabled())
                        start = System.nanoTime();
                    context = samlConfig.getContextDataExtractor().extract(attrStatements);
                    if(logger.isInfoEnabled())
                    {
                        end = System.nanoTime();
                        logger.info(String.format("Context data extraction execution time : %d ns", new Object[] {
                            Long.valueOf(end - start)
                        }));
                    }
                }
                catch(SAMLValidationException e)
                {
                    logger.error("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain)", e);
                    redirectToErrorPage(httpRequest, httpResponse, Errors.CONTEXT_DATA_EXTRACTION_FAILED, samlResponseXML, e);
                    return samlResponseXML;
                }
            session = httpRequest.getSession(true);
            if(context != null)
                if(samlConfig.getContextDataStore() == ContextDataStoreType.session)
                    session.setAttribute(samlConfig.getContextDataStoreParam(), context);
                else
                if(samlConfig.getContextDataStore() == ContextDataStoreType.request)
                    httpRequest.setAttribute(samlConfig.getContextDataStoreParam(), context);
            for(Iterator iterator2 = samlConfig.getCopies().iterator(); iterator2.hasNext();)
            {
                CopyParameter copy = (CopyParameter)iterator2.next();
                String from[] = httpRequest.getParameterValues(copy.getFrom());
                if(from != null)
                    if(from.length == 1)
                        session.setAttribute(copy.getTo(), from[0]);
                    else
                        session.setAttribute(copy.getTo(), toList(from));
            }

            session.setAttribute(SAML_AUTHENTICATED, "yes");
            for(Iterator iterator3 = samlConfig.getPostCallers().iterator(); iterator3.hasNext();)
            {
                PostCallInterface caller = (PostCallInterface)iterator3.next();
                if(!caller.call(httpRequest, httpResponse, context, samlConfig))
                    return samlResponseXML;
            }

            if(!httpRequest.isRequestedSessionIdValid())
            {
                redirectToItself(httpRequest, httpResponse);
                return samlResponseXML;
            }
        } else
        {
            for(Iterator iterator = samlConfig.getCopies().iterator(); iterator.hasNext();)
            {
                CopyParameter copy = (CopyParameter)iterator.next();
                String from[] = httpRequest.getParameterValues(copy.getFrom());
                if(from != null)
                    if(from.length == 1)
                        session.setAttribute(copy.getTo(), from[0]);
                    else
                        session.setAttribute(copy.getTo(), toList(from));
            }

            for(Iterator iterator1 = samlConfig.getPostCallers().iterator(); iterator1.hasNext();)
            {
                PostCallInterface caller = (PostCallInterface)iterator1.next();
                if(!caller.call(httpRequest, httpResponse, null, samlConfig))
                    return samlResponseXML;
            }

        }
        chain.doFilter(httpRequest, httpResponse);
        if(logger.isDebugEnabled())
            logger.debug("doHttpFilter(HttpServletRequest, HttpServletResponse, FilterChain, String) - end");
        return samlResponseXML;
    }

    private void redirectToItself(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
        throws IOException
    {
        if(httpRequest.getQueryString() != null)
            httpResponse.sendRedirect((new StringBuilder(String.valueOf(httpRequest.getRequestURI()))).append("?").append(httpRequest.getQueryString()).toString());
        else
            httpResponse.sendRedirect(httpRequest.getRequestURI());
    }

    private List<String> toList(String from[])
    {
        if(logger.isDebugEnabled())
            logger.debug("toList(String[]) - start");
        List<String> list = new ArrayList<String>();
        String as[] = from;
        int i = 0;
        for(int j = as.length; i < j; i++)
        {
            String f = as[i];
            list.add(f);
        }

        if(logger.isDebugEnabled())
            logger.debug("toList(String[]) - end");
        return list;
    }

    protected void redirectToErrorPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Errors error, String samlResponse, Throwable e)
        throws ServletException, IOException
    {
        if(logger.isDebugEnabled())
            logger.debug("redirectToErrorPage(HttpServletRequest, HttpServletResponse, Errors, String, Throwable, boolean) - start");
        
        HttpSession session = httpRequest.getSession(false);
        if(session != null)
        {
        	session.invalidate();
        	deleteAllCookies(httpRequest, httpResponse);
        }
  
        ErrorHandler handler = null;
        String idpUrl = "";
        if(samlConfig != null)
        {
            handler = samlConfig.getErrorHandler();
            idpUrl = samlConfig.getIdpURL();
        } else
        {
            handler = new DefaultErrorHandler();
        }
        handler.handle(httpRequest, httpResponse, error, samlResponse, e, idpUrl);
        if(logger.isDebugEnabled())
            logger.debug("redirectToErrorPage(HttpServletRequest, HttpServletResponse, Errors, String, Throwable, boolean) - end");
    }

    private void redirectToCurrentPageViaIDP(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
        throws ServletException, IOException, ConfigXMLParsingException
    {
        if(logger.isDebugEnabled())
            logger.debug("redirectToCurrentPageViaClientServ(HttpServletRequest, HttpServletResponse) - start");
        if(samlConfig != null)
        {
            InvalidSessionHandler handler = samlConfig.getInvalidSessionHandler();
            handler.handle(httpRequest, httpResponse, samlConfig.getIdpURL());
        } else
        {
            redirectToErrorPage(httpRequest, httpResponse, Errors.INTERNAL_ERROR, "<Not Available>", new ConfigXMLParsingException("SAML Config fiel not initialized"));
        }
        if(logger.isDebugEnabled())
            logger.debug("redirectToCurrentPageViaClientServ(HttpServletRequest, HttpServletResponse) - end");
    }

    public void destroy()
    {
    }

    public static void main(String arg[])
        throws FilterConfigException, SAMLValidationException
    {
        SAMLAuthorizer auth = new SAMLAuthorizer();
        auth.initialize("C:\\Deployment\\Channels\\config\\saml-config.xml");
        Provider aprovider[] = Security.getProviders();
        int i = 0;
        for(int j = aprovider.length; i < j; i++)
        {
            Provider prov = aprovider[i];
            System.out.println(prov.getName());
        }

        String SAMLToken = "<samlp:Response ID=\"_b8ad0005-b160-4352-b6e0-c2a811a5051e\" Version=\"2.0\" IssueInstant=\"2009-05-27T19:39:17Z\" Destination=\"http://test_acsurl.com\" xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"><saml:Issuer Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:entity\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">ClientServ</saml:Issuer><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"#_b8ad0005-b160-4352-b6e0-c2a811a5051e\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"><InclusiveNamespaces PrefixList=\"#default code ds kind rw saml samlp typens\" xmlns=\"http://www.w3.org/2001/10/xml-exc-c14n#\" /></Transform></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>mAFvipMXh4JrSYhYkV7mpC7dM7A=</DigestValue></Reference></SignedInfo><SignatureValue>XOoTKFuVSRdMgxzc41bpQrYmBCWhy91fYqfUvmF+fN/s2ad1Wom7hTGIxlpbo+Mhm7yRX9wKI8kd5C2iknIk7j0sHr+WclidNLHSxH7I5J+6y8SmmlxB+nE7+2rSCux/nyyO/dTogCdPqphRAwt4W1DULkllCJ4ZQOOw70UHeoA=</SignatureValue><KeyInfo><X509Data><X509Certificate>MIICIzCCAYygAwIBAgIQEAFlgccRV4xOuGO/hU4yCzANBgkqhkiG9w0BAQQFADAcMRowGAYDVQQDExFNTUNlcnRTdWJqZWN0TmFtZTAeFw05OTEyMzExODMwMDBaFw0zNTEyMzExODMwMDBaMBwxGjAYBgNVBAMTEU1NQ2VydFN1YmplY3ROYW1lMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI+2bZ9ThXALMv6eG/hrIWiJatP/gwc7G4UR2QNWrQAAa79o/cXjtrQHcIgAL/PEsozkqAU19boRl1NgVumDa3ujVo6fmKkdQKsMvqj+DJmGUNHM9lphY2jiQ8/NT9QuJW8gsegDBRbO0mg4Z6Zqx++4v24CTHFPVv4F29KFdzPwIDAQABo2YwZDATBgNVHSUEDDAKBggrBgEFBQcDATBNBgNVHQEERjBEgBC3pbnAUGnZ7fOztVcx8B7BoR4wHDEaMBgGA1UEAxMRTU1DZXJ0U3ViamVjdE5hbWWCEBABZYHHEVeMTrhjv4VOMgswDQYJKoZIhvcNAQEEBQADgYEAwF2ID+p+37FAYHCyYX3HQzidBdr9zlk93RcuHLM6nD4wxSWpSgjtTj5fB2uBFIl350qWVkUq5yfq6Td4c1hSwcblu3+zypJ0eqL3kbPI84J0rlY7+T0uI5enV+DmWAnR0a24hpPV5nsGisxnbf2TjPWyEYT1aJiN2gFXA0qhdqA=</X509Certificate></X509Data></KeyInfo></Signature><samlp:Status><samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\" /></samlp:Status><saml:EncryptedAssertion xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\"><EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Element\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" /><KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><EncryptedKey xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-1_5\" /><KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><X509Data><X509Certificate>MIICIzCCAYygAwIBAgIQEAFlgccRV4xOuGO/hU4yCzANBgkqhkiG9w0BAQQFADAcMRowGAYDVQQDExFNTUNlcnRTdWJqZWN0TmFtZTAeFw05OTEyMzExODMwMDBaFw0zNTEyMzExODMwMDBaMBwxGjAYBgNVBAMTEU1NQ2VydFN1YmplY3ROYW1lMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI+2bZ9ThXALMv6eG/hrIWiJatP/gwc7G4UR2QNWrQAAa79o/cXjtrQHcIgAL/PEsozkqAU19boRl1NgVumDa3ujVo6fmKkdQKsMvqj+DJmGUNHM9lphY2jiQ8/NT9QuJW8gsegDBRbO0mg4Z6Zqx++4v24CTHFPVv4F29KFdzPwIDAQABo2YwZDATBgNVHSUEDDAKBggrBgEFBQcDATBNBgNVHQEERjBEgBC3pbnAUGnZ7fOztVcx8B7BoR4wHDEaMBgGA1UEAxMRTU1DZXJ0U3ViamVjdE5hbWWCEBABZYHHEVeMTrhjv4VOMgswDQYJKoZIhvcNAQEEBQADgYEAwF2ID+p+37FAYHCyYX3HQzidBdr9zlk93RcuHLM6nD4wxSWpSgjtTj5fB2uBFIl350qWVkUq5yfq6Td4c1hSwcblu3+zypJ0eqL3kbPI84J0rlY7+T0uI5enV+DmWAnR0a24hpPV5nsGisxnbf2TjPWyEYT1aJiN2gFXA0qhdqA=</X509Certificate></X509Data></KeyInfo><CipherData><CipherValue>Hbqfi66x8TUsyr1Mj/suKhu9tyQ9bio+616oJ2CQDSNIa6O3n9amHEdbnIwSnh7n2bOf5AANk10+P1YS0YrkKDL+CuLtEmlRDhmiRcHSKdjyNJKBwwNpB8uKaVtnfllbXjLmERAoNvjF4mRxE+uayONgE7RbfWJHGjeHbpEvAP4=</CipherValue></CipherData></EncryptedKey></KeyInfo><CipherData><CipherValue>LU9+0juJo9nYhTcuZdPazgvYHK7J+FMT+v1Tkd4gRkxuo8yuBhwPmDxyJwVBXN8eP8gMXuX6vxFsSpezXhK4injoCE32i8qLmAFH+v4lLR0gIY3xqTGf/QZtpRzC13sHB71rhhHkf8yHj/S/naGkLbaCOxkmnjqjOPWUviyiFjz7wpSdtUC1JPnaJNLRiGSezjVtbK++YAAha5evZ6wrlTrY6Z0P0vJUC2izXUV57UiSCNxfYEOTCtnDb0Vmk4NMZyivxiIKKipRTzNI9aiBLvspfWMiLqJNHqKEb2GjvlUxA+Sm0JbXkKF8lhO311KPKAyKol4k43n6plptm57V0wjm519H4V366dIznmSNmr9fylQMuk3F8kPnzGRe+BfWygVLtQnJ3slFegmoJFKH/m1aRHSA4sW/A5mizDGeViOpU4+Mz/Ec7MSbFgtDFGlS49hqcMfrDIAawngfRGbI4/lHGSPhneMUD9wSLD8oARHLXtFnvreeBBUKAtCwTCjrPtRRajAETxWP6DHhKf+zWcitWRswXO1HGk0SvlSEfcKWHERJixVdbNfLlUhitOhArxZXENI4os94hCQWFXPoH6h42bsoaC8Cail13L7aUF9zo/ZiMn/sHQh6vG49kdVmfSF8DVGrYsH8IvmQMTrJeZA8lmwRmZQuYe/D4NGYNkgnh/EJlEyUw28fGF6riW/ob4N5399u3pRPYawGQ4Iqf2VMcQ/AGsBLvqriI8+aO9hPv16Onc4g4nFLlHGRLV30KA6j7As9+UQHWRawfizplsWPkJjlQ50JxyfHHrgkeRxIcjxDhYT76OmLw52YmpI6eI7BDiAMPl1yvLVSsR6rcSUE4fv6hdqe8ESDhoawNKUhsaEt0qSwOvjbOffaLfKpxx0p85pu2nNeSt3AWWHBbcQj0bpn7+gIwGt4jqNLQ5FzgZtQxhY9ltMVTe7MgFwVHmkPsEngXZc7iGX/C6upeqPic6rqVga0FawbE2tC/0FRDlOO0zCSvR8yoAmYqRdo8PogIquShovNAqwaTl/bNo4qTHcNA/t18v1u7Fdj2NkVpALuFIblC14+TIqvBhwc28pJqF4xGUDIBqACWwB74HzdXux+7fVzs7vT/InC11gG1z+9wR3apAXLg4jGMU8fBf24VfCoAtG2/FHzTZGqf2J7XLlJqNoS39O/oqMa2eU0Y6uOMa2B9f1S4tM71KygZnVhw+xZ7+ydwnsblVbzWom4vlyGEaLHnQtAKYKzeQb9coEmyVgMh1Ls5rlIKAbDmI0tTf6cYn/FlTaKsMteVRJEGHPnsctyDC/47WHlOB3Q3009CeI8InIN3+9EbjadGjRRVLupmCQ4iqq61qhf4z0O8esjt3Cuz3g3iB6K2S1/DhHVL7CTQybH2HiBndwsGo0dO8Y4szHiG15q0meafiF/AxzhsroqncKRcE4K6xiG56k9KHLfbjjziTtyAWH9ex3Um8rgGtfdBNmsFgCMjRnmiznAIZJ2nDiw+YO29hEkftMTOublvjMpb0JA9pu3</CipherValue></CipherData></EncryptedData></saml:EncryptedAssertion></samlp:Response>";
        List attrStatements = null;
        attrStatements = SAMLValidator.validate(SAMLToken.getBytes(), auth.samlConfig);
    }

    private static final Logger logger = Logger.getLogger(SAMLAuthorizer.class);
    protected static String SAML_AUTHENTICATED = "SAML_AUTHENTICATED";
    private static final String _POST_ATTR_NOT_PRESENT_COOKIE_ = "PostAttrbuteNotPresentCk";
    protected SAMLConfiguration samlConfig;
    protected FilterConfig filterConfig;
    private static final String SUBJECT_URI_BEARER = "urn:oasis:names:tc:SAML:2.0:cm:bearer";
    private SAMLObjectBuilder responseBuilder;
    private SAMLObjectBuilder statusBuilder;
    private SAMLObjectBuilder statusCodeBuilder;
    private SAMLObjectBuilder statusMessageBuilder;
    private SAMLObjectBuilder assertionBuilder;
    private SAMLObjectBuilder issuerBuilder;
    private SAMLObjectBuilder subjectBuilder;
    private SAMLObjectBuilder subjectConfirmationBuilder;
    private SAMLObjectBuilder subjectConfirmationDataBuilder;
    private SAMLObjectBuilder conditionsBuilder;
    private SAMLObjectBuilder audienceRestrictionBuilder;
    private SAMLObjectBuilder proxyRestrictionBuilder;
    private SAMLObjectBuilder audienceBuilder;
    private SignatureBuilder signatureBuilder;
    private boolean buildersReady;

}
