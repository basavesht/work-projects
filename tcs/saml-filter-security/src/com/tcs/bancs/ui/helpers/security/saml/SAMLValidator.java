package com.tcs.bancs.ui.helpers.security.saml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.security.MessageReplayRule;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.validator.ConditionsSpecValidator;
import org.opensaml.saml2.core.validator.SubjectConfirmationSchemaValidator;
import org.opensaml.saml2.core.validator.SubjectSchemaValidator;
import org.opensaml.saml2.encryption.Decrypter;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.security.MetadataCriteria;
import org.opensaml.ws.security.SecurityPolicyException;
import org.opensaml.xml.encryption.DecryptionException;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.StaticKeyInfoCredentialResolver;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureTrustEngine;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.tcs.bancs.ui.filters.security.saml.SAMLConfiguration;

public class SAMLValidator  {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SAMLValidator.class);



	/**
	 * 
	 * @param SAMLToken - SAML Assertion XML
	 * @param contextAttributeName - SAML Attribute name containing Context data
	 * @param verfiySignature 
	 * @return a base64 encoded string containing the context
	 * @throws SAMLValidationException - if validation fails
	 */

	public static List<AttributeStatement> validate(byte[] SAMLToken, SAMLConfiguration samlConfig) throws SAMLValidationException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validate(byte[], SAMLConfiguration) - start"); //$NON-NLS-1$
		}

		Decrypter samlDecrypter = samlConfig.getSamlDecrypter();
		SignatureTrustEngine sigTrustEngine = samlConfig.getTrustEngine();
		String myURI = samlConfig.getMyURI(); 
		MessageReplayRule replayRule = samlConfig.getReplayRule();

		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		
		VerifySignatureType verfiySignature = samlConfig.getVerifySignature();

		List<AttributeStatement> attrStatements = null;
		if (SAMLToken != null) {
			try {

				InputStream in = new ByteArrayInputStream(SAMLToken);
				Document SAMLDoc;
				try {
					SAMLDoc = ppMgr.parse(in);
				} catch (XMLParserException e) {
					logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

					throw createSAMLValidationException("Error parsing SAML XML", true, e);
				}
				Element responseElement = SAMLDoc.getDocumentElement();

				SAMLType samlType = SAMLType.Response;
				if (responseElement == null){
					throw createSAMLValidationException("Missing SAML Encrypted Assertion or Assertion or Assertion Response", true);
				}
				if(	   !"Response".equals(responseElement.getLocalName())
						|| !SAMLConstants.SAML20P_NS.equals(responseElement.getNamespaceURI())) {
					if (  !"Assertion".equals(responseElement.getLocalName())
							|| !SAMLConstants.SAML20_NS.equals(responseElement.getNamespaceURI())) {
						if (    !"EncryptedAssertion".equals(responseElement.getLocalName())
								|| !SAMLConstants.SAML20_NS.equals(responseElement.getNamespaceURI())) {
							throw createSAMLValidationException("Missing or invalid SAML Encrypted Assertion or Assertion or Assertion Response", true);
						}
						else{
							samlType = SAMLType.EncryptedAssertion;
						}
					}
					else{
						samlType = SAMLType.Assertion;
					}
				}
				else
				{
					samlType = SAMLType.Response;
				}


				if ( samlType ==  SAMLType.Response )
				{
					// Unmarshall SAML Assertion Response into an OpenSAML Java object.
					UnmarshallerFactory unmarshallerFactory = Configuration
					.getUnmarshallerFactory();
					Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(responseElement);
					Response samlResponse;
					try {
						samlResponse = (Response) unmarshaller
						.unmarshall(responseElement);
					} catch (UnmarshallingException e) {
						logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

						throw createSAMLValidationException("Error in unmarshalling SAML XML Document", true, e);

					}

					//Check the replay attack for Response
					if(replayRule != null)
					{
						BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
						//messageContext.setInboundMessage(samlResponse);
						if(samlResponse.getIssuer() != null)
							messageContext.setInboundMessageIssuer(samlResponse.getIssuer().getValue());
						messageContext.setInboundSAMLMessageId(samlResponse.getID());

						try {
							replayRule.evaluate(messageContext);
						} catch (SecurityPolicyException e) {
							logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

							throw createSAMLValidationException("Possible Replay Attack for Response", false, e);
						}
					}

					//Validate the status code
					if (!StatusCode.SUCCESS_URI.equals(samlResponse.getStatus().getStatusCode().getValue())) 
					{
						throw createSAMLValidationException("Invalid Status Code.", true);
					}

					boolean responseSignatureVerified = validateResponseSignature(samlResponse,sigTrustEngine, verfiySignature);

					//Get first encrypted Assertion, if not present get first unencrypted assertion
					int assertCount = samlResponse.getEncryptedAssertions().size();
					if ( assertCount == 0 )
					{
						assertCount = samlResponse.getAssertions().size();
						if ( assertCount == 0)
						{
							throw createSAMLValidationException("No Assertion or EncryptedAssertion found in received response", true);
						}
						else
						{
							for(Assertion samlAssertion:samlResponse.getAssertions())
							{
								attrStatements = validateAssertion(samlAssertion,sigTrustEngine, myURI, replayRule, verfiySignature, responseSignatureVerified);
								break; // Use the first only
							}
						}					
					}
					else
					{
						for(EncryptedAssertion samlEncryptedAssertion:samlResponse.getEncryptedAssertions())
						{

							//Decryption

							Assertion samlAssertion = null;
							try {
								samlAssertion = samlDecrypter.decrypt(samlEncryptedAssertion);		
							} catch (DecryptionException e) {
								logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

								throw createSAMLValidationException("Error Decrypting Received Encrypted Assertion", true, e);
							} 
							attrStatements = validateAssertion(samlAssertion,sigTrustEngine, myURI,replayRule,verfiySignature, responseSignatureVerified);

							break; // Use the first only
						}
					}
				} 
				else if ( samlType ==  SAMLType.EncryptedAssertion )
				{
					// Unmarshall SAML Encrypted Assertion into an OpenSAML Java object.
					UnmarshallerFactory unmarshallerFactory = Configuration
					.getUnmarshallerFactory();
					Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(responseElement);
					EncryptedAssertion encryptedAssertion = null; 
					try {
						encryptedAssertion = (EncryptedAssertion) unmarshaller
						.unmarshall(responseElement);
					} catch (UnmarshallingException e) {
						logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

						throw createSAMLValidationException("Error in unmarshalling SAML XML Document", true, e);

					}


					boolean responseSignatureVerified = false;

					//Decryption

					Assertion samlAssertion = null;
					try {
						samlAssertion = samlDecrypter.decrypt(encryptedAssertion);
					} catch (DecryptionException e) {
						logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

						throw createSAMLValidationException("Error Decrypting Received Encrypted Assertion", true, e);
					}

					attrStatements = validateAssertion(samlAssertion,sigTrustEngine, myURI,replayRule,verfiySignature, responseSignatureVerified);	
				} 
				else if ( samlType ==  SAMLType.Assertion )
				{
					// Unmarshall SAML Assertion  into an OpenSAML Java object.
					UnmarshallerFactory unmarshallerFactory = Configuration
					.getUnmarshallerFactory();
					Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(responseElement);
					Assertion samlAssertion = null; 
					try {
						samlAssertion = (Assertion) unmarshaller
						.unmarshall(responseElement);
					} catch (UnmarshallingException e) {
						logger.error("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType)", e); //$NON-NLS-1$

						throw createSAMLValidationException("Error in unmarshalling SAML XML Document", true, e);

					}

					boolean responseSignatureVerified = false;

					attrStatements = validateAssertion(samlAssertion,sigTrustEngine, myURI,replayRule,verfiySignature, responseSignatureVerified);	
				} 
			}
			catch (SAMLValidationException e) {
				throw e;
			} 
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validate(byte[], String, Decrypter, SignatureTrustEngine, String, MessageReplayRule, ParserPool, VerifySignatureType) - end"); //$NON-NLS-1$
		}
		return attrStatements;
	}

	
	public Assertion decrypt(EncryptedAssertion enc, Credential credential) throws DecryptionException 
	{
		KeyInfoCredentialResolver keyResolver = new StaticKeyInfoCredentialResolver(credential);
		EncryptedKey key = enc.getEncryptedData().getKeyInfo().getEncryptedKeys().get(0);

		Decrypter decrypter = new Decrypter(null, keyResolver, null);
		SecretKey dkey = (SecretKey) decrypter.decryptKey(
				key, enc.getEncryptedData().getEncryptionMethod().getAlgorithm());

		Credential shared = SecurityHelper.getSimpleCredential(dkey);
		decrypter = new Decrypter(new StaticKeyInfoCredentialResolver(shared), null, null);
		return decrypter.decrypt(enc);
	}
   
private static void verifySignature(Signature signature, String issuer, SignatureTrustEngine sigTrustEngine) throws SAMLValidationException
{
	if (logger.isDebugEnabled()) {
		logger.debug("verifySignature(Signature, String, SignatureTrustEngine) - start"); //$NON-NLS-1$
	}

	/*
	 * First validate the Digital Signature 
	 */

	//Validate Signature
	CriteriaSet criteriaSet = new CriteriaSet();
	criteriaSet.add( new EntityIDCriteria(issuer) );
	criteriaSet.add( new MetadataCriteria(IDPSSODescriptor.DEFAULT_ELEMENT_NAME, SAMLConstants.SAML20P_NS) );
	criteriaSet.add( new UsageCriteria(UsageType.SIGNING) );

	try {
		if (!sigTrustEngine.validate(signature, criteriaSet)) {
			throw createSAMLValidationException("Signature was either invalid or signing key could not be established as trusted", false);
		}
	} catch (SecurityException e) {
		logger.error("verifySignature(Signature, String, SignatureTrustEngine)", e); //$NON-NLS-1$

		throw createSAMLValidationException("Invalid Signature.", false, e);
	}

	if (logger.isDebugEnabled()) {
		logger.debug("verifySignature(Signature, String, SignatureTrustEngine) - end"); //$NON-NLS-1$
	}
}
private static boolean validateResponseSignature(Response samlResponse, SignatureTrustEngine sigTrustEngine, VerifySignatureType verifySignature) throws SAMLValidationException
{
	if ( verifySignature != VerifySignatureType.never)	{
		Signature signature = samlResponse.getSignature();
		if (signature != null){
			verifySignature(signature, samlResponse.getIssuer().getValue(), sigTrustEngine);
			return true;
		}
	}
	return false;
}
private static List<AttributeStatement> validateAssertion(Assertion samlAssertion, SignatureTrustEngine sigTrustEngine, String myURI, MessageReplayRule replayRule, VerifySignatureType verifySignature, boolean responseSignatureVerified) throws SAMLValidationException
{
	if (logger.isDebugEnabled()) {
		logger.debug("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType) - start"); //$NON-NLS-1$
	}

	//Check the replay attack
	if(replayRule != null)
	{
		BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
		//messageContext.setInboundMessage(samlResponse);
		if(samlAssertion.getIssuer() != null)
			messageContext.setInboundMessageIssuer(samlAssertion.getIssuer().getValue());
		messageContext.setInboundSAMLMessageId(samlAssertion.getID());

		try {
			replayRule.evaluate(messageContext);
		} catch (SecurityPolicyException e) {
			logger.error("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType)", e); //$NON-NLS-1$

			throw createSAMLValidationException("Possible Replay Attack for Assertion", false, e);
		}
	}

	if ( verifySignature != VerifySignatureType.never)	
	{
		Signature signature = samlAssertion.getSignature();
		if (signature == null)
		{
			if ( verifySignature == VerifySignatureType.force && !responseSignatureVerified)
			{
				throw createSAMLValidationException("Signature does exist in Assertion", true);
			}
		}
		else
		{
			verifySignature(signature, samlAssertion.getIssuer().getValue(), sigTrustEngine);
		}
	}
	DateTime dt = new DateTime();

	//get subject (code below only processes first Subject confirmation)
	Subject subject = samlAssertion.getSubject();
	SubjectSchemaValidator subjectSchemaValidator = new SubjectSchemaValidator();
	try {
		subjectSchemaValidator.validate(subject);
	} catch (ValidationException e) {
		logger.error("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType)", e); //$NON-NLS-1$

		throw createSAMLValidationException("Subject validation failed: " + e.getMessage(), true, e);
	}
	List<SubjectConfirmation> subjectConfirmations = subject.getSubjectConfirmations();
	for(SubjectConfirmation subjectConfirmation:subjectConfirmations)
	{
		SubjectConfirmationSchemaValidator subjectConfirmationSchemaValidator = new SubjectConfirmationSchemaValidator();
		try {
			subjectConfirmationSchemaValidator.validate(subjectConfirmation);
		} catch (ValidationException e) {
			logger.error("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType)", e); //$NON-NLS-1$

			throw createSAMLValidationException("Subject Confirmation validation failed: " + e.getMessage(), true, e);
		}
		SubjectConfirmationData subjectConfirmationData = subjectConfirmation.getSubjectConfirmationData();
		try {
			subjectConfirmationSchemaValidator.validate(subjectConfirmation);
		} catch (ValidationException e) {
			logger.error("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType)", e); //$NON-NLS-1$

			throw createSAMLValidationException("Subject Confirmation validation failed: " + e.getMessage(), true, e);
		}

		//verify the validity of time using clock skew, subjectConfirmationData.getNotBefore() and subjectConfirmationData.getNotOnOrAfter()@
		DateTime notBefore = subjectConfirmationData.getNotBefore();
		DateTime notAfter = subjectConfirmationData.getNotOnOrAfter();

		if ( notBefore != null && dt.isBefore(notBefore))
		{
			throw createSAMLValidationException("Subject confirmation expired.", true);
		}

		if ( notAfter != null && (dt.equals(notAfter) || dt.isAfter(notAfter)) )
		{
			throw createSAMLValidationException("Subject confirmation expired.", true);
		}
	}


	//		 validate conditions
	Conditions conditions = samlAssertion.getConditions();

	// Validate the spec

	ConditionsSpecValidator conditionValidator = new ConditionsSpecValidator();
	try {
		conditionValidator.validate(conditions);
	} catch (ValidationException e) {
		logger.error("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType)", e); //$NON-NLS-1$

		throw createSAMLValidationException("Condition Validity Failed.", true, e);
	}


	//verify the validity of time using clock skew, conditions.getNotBefore() and conditions.getNotOnOrAfter()@
	DateTime notBefore = conditions.getNotBefore();
	DateTime notAfter = conditions.getNotOnOrAfter();
	if ( notBefore != null && dt.isBefore(notBefore))
	{
		throw createSAMLValidationException("Assertion expired.", true);
	}

	if ( notAfter != null && (dt.equals(notAfter) || dt.isAfter(notAfter)) )
	{
		throw createSAMLValidationException("Assertion expired.", true);
	}

	for(Condition condition:conditions.getConditions())
	{
		if(condition instanceof AudienceRestriction)
		{
			if (myURI != null && myURI.length() > 0)
			{
				boolean audiencePresent = false;
				boolean iAmOneOfTheAudience = false;
				AudienceRestriction audienceRestriction = (AudienceRestriction)condition;
				for(Audience audience:audienceRestriction.getAudiences())
				{
					audiencePresent = true;
					String audienceURI = audience.getAudienceURI();
					if ( myURI.equals(audienceURI))
					{
						iAmOneOfTheAudience = true;
						break;
					}
				}
				if ( ! (audiencePresent && iAmOneOfTheAudience) )
				{
					throw createSAMLValidationException("None of the audience is intended for me: " + myURI, false);
				}
			}
		}
	}

	List<AttributeStatement> asList = samlAssertion.getAttributeStatements();

	if (logger.isDebugEnabled()) {
		logger.debug("validateAndExtractContext(Assertion, String, SignatureTrustEngine, String, MessageReplayRule, VerifySignatureType) - end"); //$NON-NLS-1$
	}
	return asList;

}

private static SAMLValidationException createSAMLValidationException(String faultString, boolean clientFault, Throwable cause) {
	if (logger.isDebugEnabled()) {
		logger.debug("createSAMLValidationException(String, boolean, Throwable) - start"); //$NON-NLS-1$
	}

	String faultCode = clientFault ? "Client" : "Server";
	SAMLValidationException fault = SAMLValidationException.createInstance(faultCode + ":" + faultString, cause);

	if (logger.isDebugEnabled()) {
		logger.debug("createSAMLValidationException(String, boolean, Throwable) - end"); //$NON-NLS-1$
	}
	return fault;
}
private static SAMLValidationException createSAMLValidationException(String faultString, boolean clientFault) {
	if (logger.isDebugEnabled()) {
		logger.debug("createSAMLValidationException(String, boolean) - start"); //$NON-NLS-1$
	}

	String faultCode = clientFault ? "Client" : "Server";
	SAMLValidationException fault = SAMLValidationException.createInstance(faultCode + ":" + faultString);

	if (logger.isDebugEnabled()) {
		logger.debug("createSAMLValidationException(String, boolean) - end"); //$NON-NLS-1$
	}
	return fault;
}

}
