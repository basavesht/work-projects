package com.tcs.bancs.ui.filters.security.saml;

import java.util.List;

import org.opensaml.saml2.core.AttributeStatement;

import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public interface ContextDataExtractor 
{
	public Object extract(List<AttributeStatement> attrStatements) throws SAMLValidationException;

	public void init(String contextDataExtractorInitParam) throws ConfigXMLParsingException; 
}
