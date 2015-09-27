package com.tcs.bancs.ui.filters.security.saml;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.KeyAlgorithmCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.impl.ExplicitKeySignatureTrustEngine;
import org.opensaml.xml.util.DatatypeHelper;

public class CustomKeySignatureTrustEngine extends ExplicitKeySignatureTrustEngine {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SAMLConfiguration.class);

	public CustomKeySignatureTrustEngine(CredentialResolver arg0, KeyInfoCredentialResolver arg1) {
		super(arg0, arg1);
	}
	
    public boolean validate(Signature signature, CriteriaSet trustBasisCriteria) throws SecurityException {
		if (logger.isDebugEnabled()) {
			logger.debug("validate(Signature, CriteriaSet) - start"); //$NON-NLS-1$
		}

        checkParams(signature, trustBasisCriteria);

        CriteriaSet criteriaSet = new CriteriaSet();
        criteriaSet.addAll(trustBasisCriteria);
        if (!criteriaSet.contains(UsageCriteria.class)) {
            criteriaSet.add(new UsageCriteria(UsageType.SIGNING));
        }
        String jcaAlgorithm = SecurityHelper.getKeyAlgorithmFromURI(signature.getSignatureAlgorithm());
        if (!DatatypeHelper.isEmpty(jcaAlgorithm)) {
            criteriaSet.add(new KeyAlgorithmCriteria(jcaAlgorithm), true);
        }

        Iterable<Credential> trustedCredentials = getCredentialResolver().resolve(criteriaSet);

        trustedCredentials = filterNonExpiredCredentials(trustedCredentials);
        
        
        if (validate(signature, trustedCredentials)) 
        {
			if (logger.isDebugEnabled()) {
				logger.debug("validate(Signature, CriteriaSet) - end"); //$NON-NLS-1$
			}
				return true;
        }

        // If the credentials extracted from Signature's KeyInfo (if any) did not verify the
        // signature and/or establish trust, as a fall back attempt to verify the signature with
        // the trusted credentials directly.
        logger.debug("Attempting to verify signature using trusted credentials");

        for (Credential trustedCredential : trustedCredentials) {
            if (verifySignature(signature, trustedCredential)) {
            	logger.debug("Successfully verified signature using resolved trusted credential");
                return true;
            }
        }
        logger.error("Failed to verify signature using either KeyInfo-derived or directly trusted credentials");
        return false;
    }

	private Iterable<Credential> filterNonExpiredCredentials(Iterable<Credential> trustedCredentials) {
		if (logger.isDebugEnabled()) {
			logger.debug("filterNonExpiredCredentials(Iterable<Credential>) - start"); //$NON-NLS-1$
		}

		ArrayList<Credential> list = new ArrayList<Credential>();
		for(Credential cred:trustedCredentials)
		{
			boolean expired = false;
			if ( cred instanceof BasicX509Credential)
			{
				BasicX509Credential x509cred = (BasicX509Credential)cred;
				try {
					x509cred.getEntityCertificate().checkValidity();
				} catch (CertificateExpiredException e) {
					logger.warn("A Credential is expired", e);
					expired = true;
				} catch (CertificateNotYetValidException e) {
					logger.warn("A Credential is expired", e);
					expired = true;
				}
			}
			if ( !expired )
			{
				list.add(cred);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("filterNonExpiredCredentials(Iterable<Credential>) - end"); //$NON-NLS-1$
		}
		return list;
	}



}
