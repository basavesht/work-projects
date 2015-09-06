package com.tcs.bancs.ui.filters.security.saml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.binding.security.MessageReplayRule;
import org.opensaml.saml2.encryption.Decrypter;
import org.opensaml.saml2.encryption.EncryptedElementTypeEncryptedKeyResolver;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.security.MetadataCredentialResolver;
import org.opensaml.util.storage.MapBasedStorageService;
import org.opensaml.util.storage.ReplayCache;
import org.opensaml.util.storage.ReplayCacheEntry;
import org.opensaml.util.storage.StorageService;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.encryption.ChainingEncryptedKeyResolver;
import org.opensaml.xml.encryption.InlineEncryptedKeyResolver;
import org.opensaml.xml.encryption.SimpleRetrievalMethodEncryptedKeyResolver;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.security.credential.CollectionCredentialResolver;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoProvider;
import org.opensaml.xml.security.keyinfo.LocalKeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.provider.InlineX509DataProvider;
import org.opensaml.xml.security.keyinfo.provider.RSAKeyValueProvider;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.impl.ExplicitKeySignatureTrustEngine;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.ui.helpers.ObservableConfiguration;
import com.tcs.bancs.ui.helpers.security.PasswordEncryptionDecryptionException;
import com.tcs.bancs.ui.helpers.security.saml.KeyProperties;
import com.tcs.bancs.ui.helpers.security.saml.KeyStoreProperties;
import com.tcs.bancs.ui.helpers.security.saml.VerifySignatureType;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.ui.helpers.xml.XMLUtils;

public class SAMLConfiguration extends ObservableConfiguration{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SAMLConfiguration.class);

	private String postAttrSAML;
	private String SAMLTokenEncryptedValue;
	private boolean SAMLTokenEncrypted;
	private SimpleKeyProperties SAMLTokenDecryptionKey;
	private KeyProperties decryptionKey;
	private String idpURL;
	private String myURI; // Default None
	private int replayMinutes = 5 ; //Default 5 Minutes
	private MessageReplayRule replayRule = null;
	private String metadataProvider;
	private ExplicitKeySignatureTrustEngine trustEngine;
	private VerifySignatureType verifySignature;
	private String localEntityId;

	private ContextDataExtractor contextDataExtractor;
	private String contextDataExtractorInitParam;
	private ContextDataStoreType contextDataStore;
	private String contextDataStoreParam;

	private InvalidSessionHandler invalidSessionHandler;
	private ErrorHandler errorHandler;

	private List<CopyParameter> copies = new ArrayList<CopyParameter>();
	private List<PostCallInterface> postCallers = new ArrayList<PostCallInterface>();
	
	private boolean simulated = false;
	private String simulatedContext;
	
	private KeyInfoCredentialResolver kekResolver;
	private ChainingEncryptedKeyResolver encryptedKeyResolver;
	
	public void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(configFileName);

			Document configDoc = XMLUtils.parse(is);

			NodeList nodes = configDoc.getElementsByTagName("SAMLFilterConfig").item(0).getChildNodes();
			int nodes_length = nodes.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
				Node child = nodes.item(i);
				
				if ( child.getNodeName().equals("LOCAL_ENTITY_ID"))
				{
					localEntityId = child.getFirstChild().getNodeValue();
				}
				else if ( child.getNodeName().equals("SAML_POST_ATTRIBUTE_NAME"))
				{
					postAttrSAML = child.getFirstChild().getNodeValue();
				}
				else if ( child.getNodeName().equals("MY_URI"))
				{
					myURI = child.getFirstChild().getNodeValue();
				}
				else if ( child.getNodeName().equals("ASSERTION_SIGNATURE_VERIFICATION"))
				{
					try {
						verifySignature = VerifySignatureType.valueOf(child.getFirstChild().getNodeValue());
					} catch (IllegalArgumentException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid value specified for ASSERTION_SIGNATURE_VERIFICATION", e);
					}
				}
				else if ( child.getNodeName().equals("REPLAY_MINUTES"))
				{
					try {
						replayMinutes = Integer.valueOf(child.getFirstChild().getNodeValue());
					} catch (NumberFormatException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						replayMinutes = 5;
					} 
				}
				else if ( child.getNodeName().equals("SAML_TOKEN_ENCRYPTION "))
				{
					SAMLTokenEncryptedValue = XMLUtils.getAttribute(child,"ENABLED");
					SAMLTokenEncrypted = SAMLTokenEncryptedValue.equalsIgnoreCase("true");
					if(SAMLTokenEncrypted)
					{
						String algorithm =  XMLUtils.getAttribute(child,"ALGORITHM");
						String init = XMLUtils.getAttribute(child,"INIT");
						String key = XMLUtils.getAttribute(child,"KEY");
						String mode = XMLUtils.getAttribute(child,"MODE");
						String padding = XMLUtils.getAttribute(child,"PADDING");
						SAMLTokenDecryptionKey = new SimpleKeyProperties();
						SAMLTokenDecryptionKey.setAlgorithm(algorithm);
						SAMLTokenDecryptionKey.setInit(init);
						SAMLTokenDecryptionKey.setKey(key);
						if ( mode != null )
						{
							SAMLTokenDecryptionKey.setMode(mode);
						}
						if ( padding != null )
						{
							SAMLTokenDecryptionKey.setPadding(padding);
						}
					}
				}
				else if ( child.getNodeName().equals("SAML_ENCRYPTED_ASSERTION_DECRYPTOR"))
				{
					String decryptorClass = XMLUtils.getAttribute(child,"DecryptorClass");
					String keystore = XMLUtils.getAttribute(child,"KEYSTORE");
					String type = XMLUtils.getAttribute(child,"TYPE");
					String password = XMLUtils.getAttribute(child,"PASSWORD");
					String alias = XMLUtils.getAttribute(child,"ALIAS");
					String keyPassword = XMLUtils.getAttribute(child,"KEY_PASSWORD");
					decryptionKey = new KeyProperties();
					KeyStoreProperties props = new KeyStoreProperties();
					URL keyStoreURL = null;
					try {
						if (keystore != null && keystore.length() > 0)
						{
							keyStoreURL = new URL(keystore);
						}
					} catch (MalformedURLException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid URL specified in 'SAML_ENCRYPTED_ASSERTION_DECRYPTOR': " + keystore, e);
					} 
					try {
						props.setPed(decryptorClass);
					} catch (ClassNotFoundException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid value specified for decryptorClass in SAML_ENCRYPTED_ASSERTION_DECRYPTOR", e);
					} catch (IllegalAccessException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid value specified for decryptorClass in SAML_ENCRYPTED_ASSERTION_DECRYPTOR", e);
					} catch (InstantiationException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid value specified for decryptorClass in SAML_ENCRYPTED_ASSERTION_DECRYPTOR", e);
					}
					props.setKeystore(keyStoreURL);
					try {
						props.setPassword(password);
					} catch (PasswordEncryptionDecryptionException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Could not decrypt password using decryptorClass in SAML_ENCRYPTED_ASSERTION_DECRYPTOR", e);
					}
					props.setType(type);
					props.setAlias(alias);
					try {
						props.setKeyPassword(keyPassword);
					} catch (PasswordEncryptionDecryptionException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Could not decrypt password using decryptorClass in SAML_ENCRYPTED_ASSERTION_DECRYPTOR", e);
					}
					decryptionKey.setKeystore(props);
				}
				/****
				else if ( child.getNodeName().equals("SAML_SIGNATURE_VERIFIER"))
				{
					String entityId = child,"ENTITYID");
					String keystore = child,"KEYSTORE");
					String type = child,"TYPE");
					String password = child,"PASSWORD");
					String alias = child,"ALIAS");
					signingKey = new KeyProperties();
					signingKey.setEntityId(entityId);
					KeyStoreProperties props = new KeyStoreProperties();
					URL keyStoreURL = null;
					try {
						if (keystore != null && keystore.length() > 0)
						{
							keyStoreURL = new URL(keystore);
						}
					} catch (MalformedURLException e) {
						throw new ConfigXMLParsingException("Invalid URL specified in 'SAML_SIGNATURE_VERIFIER': " + keystore, e);
					} 
					props.setKeystore(keyStoreURL);
					props.setPassword(password);
					props.setType(type);
					props.setAlias(alias);
					signingKey.setKeystore(props);
				}***/
				else if ( child.getNodeName().equals("SAML_METADATA_PROVIDER"))
				{
					metadataProvider = child.getFirstChild().getNodeValue();
				}
				else if ( child.getNodeName().equals("CONTEXT_DATA"))
				{
					try {
						contextDataExtractor = (ContextDataExtractor) Class.forName(XMLUtils.getAttribute(child,"EXTRACTCLASS")).newInstance();
					} catch (DOMException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'EXTRACTCLASS' attribute in 'CONTEXT_DATA'", e);
					} catch (IllegalAccessException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'EXTRACTCLASS' attribute in 'CONTEXT_DATA'", e);
					} catch (InstantiationException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'EXTRACTCLASS' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassNotFoundException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'EXTRACTCLASS' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassCastException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'EXTRACTCLASS' attribute in 'CONTEXT_DATA'", e);
					}
					contextDataExtractorInitParam = XMLUtils.getAttribute(child,"EXTRACTCLASSINITPARAM");
					if ( contextDataExtractorInitParam != null )
					{
						contextDataExtractor.init(contextDataExtractorInitParam);
					}
					try {
						contextDataStore = ContextDataStoreType.valueOf(XMLUtils.getAttribute(child,"STOREINTO"));
					} catch (IllegalArgumentException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid value specified for STOREINTO", e);
					}
					contextDataStoreParam = XMLUtils.getAttribute(child,"STOREAS");
				}
				else if ( child.getNodeName().equals("INVALID_SESSION_HANDLER"))
				{
					try {
						invalidSessionHandler = (InvalidSessionHandler) Class.forName(child.getFirstChild().getNodeValue()).newInstance();
					} catch (DOMException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (IllegalAccessException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (InstantiationException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassNotFoundException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassCastException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					}
					String invalidSessionHandlerInitParam = XMLUtils.getAttribute(child,"INIT");
					if ( invalidSessionHandlerInitParam != null )
					{
						invalidSessionHandler.init(invalidSessionHandlerInitParam);
					}
				}
				else if ( child.getNodeName().equals("ERROR_HANDLER"))
				{
					try {
						errorHandler = (ErrorHandler) Class.forName(child.getFirstChild().getNodeValue()).newInstance();
					} catch (DOMException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (IllegalAccessException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (InstantiationException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassNotFoundException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					} catch (ClassCastException e) {
						logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

						throw new ConfigXMLParsingException("Invalid Class specified in 'INVALID_SESSION_HANDLER' attribute in 'CONTEXT_DATA'", e);
					}
					String errorHandlerInitParam = XMLUtils.getAttribute(child,"INIT");
					if ( errorHandlerInitParam != null )
					{
						errorHandler.init(errorHandlerInitParam);
					}
				}
				else if ( child.getNodeName().equals("IDP_URL"))
				{
					idpURL = child.getFirstChild().getNodeValue();
				}
				else if ( child.getNodeName().equals("POST_FILTER"))
				{
					NodeList nodes_2 = child.getChildNodes();
					int nodes_length_2 = nodes_2.getLength();
					for(int j = 0 ; j < nodes_length_2; j++)
					{
						Node child_2 = nodes_2.item(j);

						if ( child_2.getNodeName().equals("COPY"))
						{
							String request = XMLUtils.getAttribute(child_2,"REQUEST");
							String session = XMLUtils.getAttribute(child_2,"SESSION");
							if ( request != null)
							{
								if ( session == null)
								{
									session = request;
								}
								CopyParameter param = new CopyParameter();
								param.setFrom(request);
								param.setTo(session);
								copies.add(param);
							}
						}
						else if ( child_2.getNodeName().equals("CALL"))
						{
							String className = XMLUtils.getAttribute(child_2,"CLASS");
							if ( className == null)
							{
								throw new ConfigXMLParsingException("Attribute 'CLASS' specified in 'CALL'");
							}
							String configName = XMLUtils.getAttribute(child_2,"CLASSCONFIGFILE");
							if ( configName == null)
							{
								throw new ConfigXMLParsingException("Attribute 'CLASSCONFIGFILE' specified in 'CALL'");
							}
							PostCallInterface caller = null;
							try {
								Class clazz = Class.forName(className);
								caller = (PostCallInterface)clazz.newInstance();
								caller.init(configName);
								
							} catch (ClassNotFoundException e) {
								throw new ConfigXMLParsingException("Invalid class name under 'CLASS' attribute specified in 'CALL'", e);
							} catch (IllegalAccessException e) {
								throw new ConfigXMLParsingException("Invalid class name under 'CLASS' attribute specified in 'CALL'", e);
							} catch (InstantiationException e) {
								throw new ConfigXMLParsingException("Invalid class name under 'CLASS' attribute specified in 'CALL'", e);
							}
							postCallers.add(caller);
						}
					}
				}
			}

			validateConfig();
			if ( !noInit)
			{
				initSAML2();
			}

		} catch (IOException e) {
			logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("IOException received.", e);		
		} catch (ConfigurationException e) {
			logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error initializing SAML 2.0 bootstrap using configuration.", e);
		} catch (MetadataProviderException e) {
			logger.error("SAMLConfiguration(String, boolean)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error initializing SAML 2.0 Metadata Provider.", e);
		}
		finally
		{
			try {
				if ( is !=null ) is.close();
			} catch (IOException e) {
				logger.warn("SAMLConfiguration(String, boolean) - exception ignored", e); //$NON-NLS-1$
			}
		}
	}
	private static BasicX509Credential getDecryptionCredential(KeyStoreProperties props, String entityId) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDecryptionCredential(KeyStoreProperties, String) - start"); //$NON-NLS-1$
		}

		BasicX509Credential returnBasicX509Credential = buildCredential(props.getPassword(), props.getKeystore(), props.getType(), entityId, props.getAlias(), props.getKeyPassword());
		if (logger.isDebugEnabled()) {
			logger.debug("getDecryptionCredential(KeyStoreProperties, String) - end"); //$NON-NLS-1$
		}
		return returnBasicX509Credential;
	}

	private static BasicX509Credential buildCredential(String keyStorePwd, URL keyStoreFile, String keyStoreType,String entityId, String alias, String password) throws ConfigXMLParsingException  
	{
		if (logger.isDebugEnabled()) {
			logger.debug("buildCredential(String, URL, String, String, String, String) - start"); //$NON-NLS-1$
		}
 
		InputStream keyStoreFis;
		try {
			keyStoreFis = keyStoreFile.openStream();
		} catch (IOException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Key Store File not found: " + keyStoreFile, e);
		} 
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance(keyStoreType);
		} catch (KeyStoreException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error getting Key Store instance: " + keyStoreType,  e);
		} 
		try {
			keyStore.load(keyStoreFis, keyStorePwd.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error loading Key Store",  e);
		} catch (CertificateException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error loading Key Store",  e);
		} catch (IOException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error loading Key Store",  e);
		} 
		X509Certificate x509Certificate;
		try {
			x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
		} catch (KeyStoreException e) {
			logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("Error getting certificate from alias : '" + alias + "'",  e);
		} 
		if ( x509Certificate == null )
			throw new ConfigXMLParsingException("Error getting certificate from alias : '" + alias + "'");

		java.security.Key key = null;
		if ( password != null)
		{
			try {
				key = keyStore.getKey(alias, password.toCharArray());
			} catch (KeyStoreException e) {
				logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

				key = null;
			} catch (NoSuchAlgorithmException e) {
				logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

				key = null;
			} catch (UnrecoverableKeyException e) {
				logger.error("buildCredential(String, URL, String, String, String, String)", e); //$NON-NLS-1$

				key = null;
			} 
		}
		BasicX509Credential credential = new BasicX509Credential(); 
		credential.setEntityCertificate(x509Certificate); 
		List<X509CRL> crls = new ArrayList<X509CRL>(); 
		credential.setCRLs(crls); 
		credential.setEntityId(entityId); 
		if ( key != null ) credential.setPrivateKey((PrivateKey)key); 
		credential.setPublicKey(x509Certificate.getPublicKey()); 
		credential.getKeyNames().add(alias); 

		if (logger.isDebugEnabled()) {
			logger.debug("buildCredential(String, URL, String, String, String, String) - end"); //$NON-NLS-1$
		}
		return credential; 
	} 

	private void initSAML2() throws ConfigurationException, ConfigXMLParsingException, MetadataProviderException {
		if (logger.isDebugEnabled()) {
			logger.debug("initSAML2() - start"); //$NON-NLS-1$
		}

		//
		//		 One-time init code here
		//

		DefaultBootstrap.bootstrap();
		
		StorageService<String, ReplayCacheEntry> storageEngine = new MapBasedStorageService<String, ReplayCacheEntry>();
		ReplayCache replayCache = new ReplayCache(storageEngine, 60 * 1000 * replayMinutes);
		replayRule = new MessageReplayRule(replayCache);	

		//		 Get the private key that corresponds to a public key that may 
		//		 have been used by other parties for encryption 
		Credential decryptionCredential = getDecryptionCredential(decryptionKey.getKeystore(),localEntityId);

		List<Credential> localCredentials = new ArrayList<Credential>();
		localCredentials.add(decryptionCredential);

		CollectionCredentialResolver localCredResolver = new CollectionCredentialResolver(localCredentials);

		//		Support EncryptedKey/KeyInfo containing decryption key hints via
		//		KeyValue/RSAKeyValue and X509Data/X509Certificate
		List<KeyInfoProvider> kiProviders = new ArrayList<KeyInfoProvider>();
		kiProviders.add( new RSAKeyValueProvider() );
		kiProviders.add( new InlineX509DataProvider() );

//		Resolves local credentials by using information in the EncryptedKey/KeyInfo to query the supplied 
//		local credential resolver.
		kekResolver = new LocalKeyInfoCredentialResolver(kiProviders, localCredResolver);

//		Supports resolution of EncryptedKeys by 3 common placement mechanisms
		encryptedKeyResolver = new ChainingEncryptedKeyResolver();
		encryptedKeyResolver.getResolverChain().add( new InlineEncryptedKeyResolver() );
		encryptedKeyResolver.getResolverChain().add( new EncryptedElementTypeEncryptedKeyResolver() );
		encryptedKeyResolver.getResolverChain().add( new SimpleRetrievalMethodEncryptedKeyResolver() );

		//siginingCredential = getVerificationCredential(signingKey.getKeystore(),signingKey.getEntityId());
		
		MetadataProvider mdProvider = getMetadataProvider();
		MetadataCredentialResolver mdCredResolver = new MetadataCredentialResolver(mdProvider);
		KeyInfoCredentialResolver keyInfoCredResolver = Configuration.getGlobalSecurityConfiguration().getDefaultKeyInfoCredentialResolver();
		trustEngine = new CustomKeySignatureTrustEngine(mdCredResolver, keyInfoCredResolver);
		

		if (logger.isDebugEnabled()) {
			logger.debug("initSAML2() - end"); //$NON-NLS-1$
		}
	}
	
	private MetadataProvider getMetadataProvider() throws MetadataProviderException {
		if (logger.isDebugEnabled()) {
			logger.debug("getMetadataProvider() - start"); //$NON-NLS-1$
		}

		org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider provider =  new FilesystemMetadataProvider(new File(metadataProvider));
		BasicParserPool bpp = new BasicParserPool();
		bpp.setNamespaceAware(true);
		provider.setParserPool(bpp);

		if (logger.isDebugEnabled()) {
			logger.debug("getMetadataProvider() - end"); //$NON-NLS-1$
		}
		return provider;
	}
	/**
	 * @return the idpURL
	 */
	public String getIdpURL() {
		return idpURL;
	}
	/**
	 * @return the myURI
	 */
	public String getMyURI() {
		return myURI;
	}
	/**
	 * @return the postAttrSAML
	 */
	public String getPostAttrSAML() {
		return postAttrSAML;
	}
	/**
	 * @return the replayRule
	 */
	public MessageReplayRule getReplayRule() {
		return replayRule;
	}
	/**
	 * @return the samlDecrypter
	 */
	public Decrypter getSamlDecrypter() {
		Decrypter	samlDecrypter =	new Decrypter(null, kekResolver, encryptedKeyResolver);
		samlDecrypter.setRootInNewDocument(true); //To Make the EncryptedAssertion Signature Verification work
		return samlDecrypter;
	}
	/**
	 * @return the sAMLTokenDecryptionKey
	 */
	public SimpleKeyProperties getSAMLTokenDecryptionKey() {
		return SAMLTokenDecryptionKey;
	}
	/**
	 * @return the sAMLTokenEncrypted
	 */
	public boolean isSAMLTokenEncrypted() {
		return SAMLTokenEncrypted;
	}

	private void validateConfig() throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

		assertConfigForTag(localEntityId==null,"LOCAL_ENTITY_ID");
		assertConfigForTag(postAttrSAML==null,"SAML_POST_ATTRIBUTE_NAME");
		if (SAMLTokenEncrypted) validateSAMLDecryptorKey("SAML_TOKEN_ENCRYPTION");
		validateDecryptKey("SAML_ENCRYPTED_ASSERTION_DECRYPTOR");
		//validateSignerKeyKey("SAML_SIGNATURE_VERIFIER");
		assertConfigForTag(metadataProvider==null,"SAML_METADATA_PROVIDER");
		assertConfigForTag(idpURL==null,"IDP_URL");
		assertConfigForTag(verifySignature==null,"ASSERTION_SIGNATURE_VERIFICATION");

		if ( contextDataExtractor != null)
		{
			assertConfigForAttribute(contextDataStore==null,"ATTRIBUTE_NAME", "STOREAS");
			assertConfigForAttribute(contextDataStore != ContextDataStoreType.none && contextDataStoreParam==null,"ATTRIBUTE_NAME", "STOREIN");
		}
		
		createDefaultInvalidSessionHandler(invalidSessionHandler==null);
		createDefaultErrorHandler(errorHandler==null);

		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - end"); //$NON-NLS-1$
		}
	}
	private void createDefaultErrorHandler(boolean noErrorHandler) 
	{
		if ( noErrorHandler)
		{
			errorHandler = new DefaultErrorHandler();
		}
		
	}
	private void createDefaultInvalidSessionHandler(boolean noInvalidSessionHandler) 
	{
		if ( noInvalidSessionHandler)
		{
			invalidSessionHandler = new DefaultInvalidSessionHandler();
		}
		
	}
	private void validateSAMLDecryptorKey(String parent) throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateSAMLDecryptorKey(String) - start"); //$NON-NLS-1$
		}

		assertConfigForTag(SAMLTokenDecryptionKey==null,parent);
		assertConfigForAttribute(SAMLTokenDecryptionKey.getAlgorithm()==null,parent ,"ALGORITHM");
		assertConfigForAttribute(SAMLTokenDecryptionKey.getInit()==null,parent ,"INIT");
		assertConfigForAttribute(SAMLTokenDecryptionKey.getKey()==null,parent ,"KEY");

		if (logger.isDebugEnabled()) {
			logger.debug("validateSAMLDecryptorKey(String) - end"); //$NON-NLS-1$
		}
	}
	private void validateDecryptKey(String parent) throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateDecryptKey(String) - start"); //$NON-NLS-1$
		}

		assertConfigForTag(decryptionKey==null,parent);
		assertConfigForAttribute(decryptionKey.getKeystore()==null,parent ,"KEYSTORE");
		assertConfigForAttribute(decryptionKey.getKeystore().getAlias()==null,parent ,"ALIAS");
		assertConfigForAttribute(decryptionKey.getKeystore().getKeystore()==null,parent ,"KEYSTORE");
		assertConfigForAttribute(decryptionKey.getKeystore().getPassword() ==null,parent ,"PASSWORD");
		assertConfigForAttribute(decryptionKey.getKeystore().getType() ==null,parent ,"TYPE");
		assertConfigForAttribute(decryptionKey.getKeystore().getKeyPassword() ==null,parent ,"KEY_PASSWORD");

		if (logger.isDebugEnabled()) {
			logger.debug("validateDecryptKey(String) - end"); //$NON-NLS-1$
		}
	}

	/****
	private void validateSignerKeyKey(String parent) throws ConfigXMLParsingException 
	{
		assertConfigForTag(signingKey==null,parent);
		assertConfigForAttribute(signingKey.getEntityId()==null,parent ,"ENTITYID");
		assertConfigForAttribute(signingKey.getKeystore()==null,parent ,"KEYSTORE");
		assertConfigForAttribute(signingKey.getKeystore().getAlias()==null,parent ,"ALIAS");
		assertConfigForAttribute(signingKey.getKeystore().getKeystore()==null,parent ,"KEYSTORE");
		assertConfigForAttribute(signingKey.getKeystore().getPassword() ==null,parent ,"PASSWORD");
		assertConfigForAttribute(signingKey.getKeystore().getType() ==null,parent ,"TYPE");
	}
	****/

	private void assertConfigForAttribute(boolean raiseError, String tagName, String attributeName) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForAttribute(boolean, String, String) - start"); //$NON-NLS-1$
		}

		if(raiseError) throw new ConfigXMLParsingException("XML attribute '" + attributeName + "' must be specified for tag '" + tagName +"' in config XML");


		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForAttribute(boolean, String, String) - end"); //$NON-NLS-1$
		}
	}
	private void assertConfigForTag(boolean raiseError, String tagName) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForTag(boolean, String) - start"); //$NON-NLS-1$
		}

		if(raiseError) throw new ConfigXMLParsingException("XML tag '" + tagName + "' must be specified in config XML");

		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForTag(boolean, String) - end"); //$NON-NLS-1$
		}
	}
	private static SAMLConfiguration instance  = null;
	private boolean noInit = false;
	public static SAMLConfiguration getInstance()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start"); //$NON-NLS-1$
		}

		instance = getInstance(false);

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end"); //$NON-NLS-1$
		}
		return instance;
	}
	/**
	 * @return the trustEngine
	 */
	public ExplicitKeySignatureTrustEngine getTrustEngine() {
		return trustEngine;
	}
	public VerifySignatureType getVerifySignature() {
		return verifySignature;
	}
	public KeyProperties getDecryptionKey() {
		return decryptionKey;
	}
	public String getLocalEntityId() {
		return localEntityId;
	}
	public static SAMLConfiguration getInstance(boolean noInit) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - start"); //$NON-NLS-1$
		}

		if ( instance == null){
			instance = new SAMLConfiguration();
		}

		instance.setNoInit(noInit);

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - end"); //$NON-NLS-1$
		}
		return instance;

	}
	public ContextDataExtractor getContextDataExtractor() 
	{
		return contextDataExtractor;
	}
	public String getContextDataExtractorInitParam() {
		return contextDataExtractorInitParam;
	}
	public ContextDataStoreType getContextDataStore() {
		return contextDataStore;
	}
	public String getContextDataStoreParam() {
		return contextDataStoreParam;
	}
	public InvalidSessionHandler getInvalidSessionHandler() {
		return invalidSessionHandler;
	}
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
	public List<CopyParameter> getCopies() {
		return copies;
	}
	public List<PostCallInterface> getPostCallers() {
		return postCallers;
	}
	public void setNoInit(boolean noInit) {
		this.noInit = noInit;
	}
	public boolean isSimulated() {
		return simulated;
	}
	public void setSimulated(boolean simulated) {
		this.simulated = simulated;
	}
	public String getSimulatedContext() {
		return simulatedContext;
	}
	public void setSimulatedContext(String simulatedContext) {
		this.simulatedContext = simulatedContext;
	}
	public void setVerifySignature(VerifySignatureType verifySignature) {
		this.verifySignature = verifySignature;
	}
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
}
