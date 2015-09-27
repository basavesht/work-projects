package com.tcs.bancs.channels.saml.utils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.bancs.ui.filters.security.saml.Errors;
import com.tcs.bancs.ui.filters.security.saml.SAMLConfiguration;
import com.tcs.bancs.ui.helpers.Base64Util;
import com.tcs.bancs.ui.helpers.security.CryptUtils;
import com.tcs.bancs.ui.helpers.security.EncryptionException;
import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.security.saml.SAMLValidator;

public class SAMLUtils {

	public static MMContext authenticateSAML(String base64encodedSAMLToken, SAMLConfiguration samlConfig) throws SAMLAuthException{
		final Logger logger = Logger.getLogger(SAMLUtils.class);

		if(base64encodedSAMLToken == null || base64encodedSAMLToken.length() == 0)
		{
			logger.error("authenticateSAML(String) : base64encodedSAMLToken is empty or null ");
			throw new SAMLAuthException("base64encodedSAMLToken is empty or null");
		}
		byte SAMLToken[] = (byte[])null;
		try
		{
			SAMLToken = Base64Util.decode(base64encodedSAMLToken);
		}
		catch(RuntimeException e)
		{
			logger.error("authenticateSAML(String)", e);
			throw new SAMLAuthException(e);
		} catch (IOException e) {
			logger.error("authenticateSAML(String)", e);
			throw new SAMLAuthException(e);
		}
		if(SAMLToken == null)
		{
			logger.error("authenticateSAML(String): base64 decode returned null SAML Token");
			throw new SAMLAuthException("base64 decode returned null SAML Token");
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
			logger.error("authenticateSAML(String)", e);
			throw new SAMLAuthException(e);
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
			logger.error("authenticateSAML(String)", e);
			throw new SAMLAuthException(e);
		}
		
        if(samlConfig.getContextDataExtractor() != null)
        {
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
                return (MMContext)context;
            }
            catch(SAMLValidationException e)
            {
                logger.error("authenticateSAML(String)", e);
    			throw new SAMLAuthException(e);
            }
		}else{
			return null;
		}
	}

}
