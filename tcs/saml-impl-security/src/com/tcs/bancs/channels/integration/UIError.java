package com.tcs.bancs.channels.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.filters.security.saml.Errors;


public class UIError 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UIError.class);

    private static Properties errorsCodes = new Properties();
    private static Properties errorsDescriptions = new Properties();
	private static boolean loaded =false;
	public UIError(Errors error) 
	{
		if (!loaded )
		{
	        InputStream is1=null;
	        InputStream is2=null;
			try {
				is1 = UIError.class.getClassLoader().getResourceAsStream("errors.properties");
				is2 = UIError.class.getClassLoader().getResourceAsStream("ErrMessage.properties");
				if(is1 != null && is2 != null)
				{
	                errorsCodes.load(is1);
	                errorsDescriptions.load(is2);
	                loaded = true;
				}
			} catch (IOException e) {
				logger.warn("UIError(Errors) - exception ignored", e); //$NON-NLS-1$
			}
			finally
			{
		        if(is1 != null)
		            try
		            {
		                is1.close();
		            }
		            catch(IOException e)
		            {
		                logger.warn("UIError(Errors) - exception ignored", e);
		            }
		        if(is2 != null)
		            try
		            {
		                is2.close();
		            }
		            catch(IOException e)
		            {
		                logger.warn("UIError(Errors) - exception ignored", e);
		            }
			}
		}
		
        errorCode = errorsCodes.getProperty((new StringBuilder(String.valueOf(error.name()))).append("_CODE").toString());
        if(errorCode != null)
            errorDesc = errorsDescriptions.getProperty(errorCode);
        if(errorCode == null)
            errorCode = "UNKNOWN";
        if(errorDesc == null)
            errorDesc = String.format("Enum literal: %s", new Object[] {
                error.name()
            });
        return;
	}
	String errorCode;
	String errorDesc;
}
