package com.tcs.bancs.security.saml2.sp.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.filters.security.saml.SAMLConfiguration;
import com.tcs.bancs.ui.helpers.Base64Util;
import com.tcs.bancs.ui.helpers.security.saml.KeyProperties;
import com.tcs.bancs.ui.helpers.security.saml.KeyStoreProperties;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class Generate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Generate.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		if(args.length != 1)
		{
			usage();

			if (logger.isDebugEnabled()) {
				logger.debug("main(String[]) - end"); //$NON-NLS-1$
			}
			return;
		}
		try
		{
			String metadata = generate(args[0]);
			System.out.println(metadata);
		}
		catch (Throwable t)
		{
			logger.error("main(String[])", t); //$NON-NLS-1$

			t.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
	static String lineSep = System.getProperty("line.separator");

	private static String generate(String configFile) throws ConfigXMLParsingException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("generate(String) - start"); //$NON-NLS-1$
		}

		SAMLConfiguration config = SAMLConfiguration.getInstance(true);
		config.parse(configFile);
		KeyProperties props = config.getDecryptionKey();
		String x509Cert = null;
		if (props != null)
		{
			KeyStoreProperties ksProps = props.getKeystore();
			if ( ksProps != null)
			{
				InputStream is = null;
				try
				{
					is = ksProps.getKeystore().openStream();
					KeyStore ks = KeyStore.getInstance(ksProps.getType());
					ksProps.getKeystore().openStream();
					ks.load(is, ksProps.getPassword().toCharArray());
					PrivateKey key = (PrivateKey)ks.getKey(ksProps.getAlias(), ksProps.getKeyPassword().toCharArray());
					X509Certificate cert = (X509Certificate)ks.getCertificate(ksProps.getAlias());
					if ( key == null)
					{
						System.err.println("No private key found for '" + ksProps.getKeystore() + "' ==> '" + ksProps.getAlias() + "'");
					}
					else
					{
						if ( cert == null)
						{
							System.err.println("No certificate found for '" + ksProps.getKeystore() + "' ==> '" + ksProps.getAlias() + "'");
							System.out.println("Please get a certificate for following CSR :");
							System.out.println(KeyToolUsage(ksProps.getAlias()));
						}
						else
						{
							try {
								cert.checkValidity();
							} catch (CertificateExpiredException e) {
								logger.error("generate(String)", e); //$NON-NLS-1$

								System.err.println("Certificate Expired for '" + ksProps.getKeystore() + "' ==> '" + ksProps.getAlias() + "'");
								System.out.println("Please generate CSR, get a certificate and import it to thos keystore.");
								System.out.println(KeyToolUsage(ksProps.getAlias()));
							} catch (CertificateNotYetValidException e) {
								logger.error("generate(String)", e); //$NON-NLS-1$

								System.err.println("Certificate not yet valid for '" + ksProps.getKeystore() + "' ==> '" + ksProps.getAlias() + "'");
								System.out.println("Please get a certificate for following CSR :");
								System.out.println(KeyToolUsage(ksProps.getAlias()));
							}
							x509Cert = Base64Util.encode(cert.getEncoded());
						}
					}
				}
				finally
				{
					if (is != null )
					{
						is.close();
					}
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if ( x509Cert != null)
		{
			sb.append("============= METADATA BEGIN ==============").append(lineSep);
			sb.append("<EntityDescriptor").append(lineSep);
			sb.append("  xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\"").append(lineSep);
			sb.append("  entityID=\"" + config.getLocalEntityId() + "\">").append(lineSep);
			sb.append("  <SPSSODescriptor").append(lineSep);
			sb.append("    protocolSupportEnumeration= \"urn:oasis:names:tc:SAML:2.0:protocol\">").append(lineSep);
			sb.append("    <KeyDescriptor use=\"encryption\">").append(lineSep);
			sb.append("      <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">").append(lineSep);
			sb.append("        <ds:X509Data>").append(lineSep);
			sb.append("          <ds:X509Certificate>").append(lineSep);
			sb.append(x509Cert);
			sb.append("          </ds:X509Certificate>").append(lineSep);
			sb.append("        </ds:X509Data>").append(lineSep);
			sb.append("      </ds:KeyInfo>").append(lineSep);
			sb.append("    </KeyDescriptor>").append(lineSep);
			sb.append("  </SPSSODescriptor>").append(lineSep);
			sb.append("</EntityDescriptor>").append(lineSep);
			sb.append("============= METADATA END   ==============").append(lineSep);
		}
		String returnString = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("generate(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private static Object KeyToolUsage(String alias) {
		if (logger.isDebugEnabled()) {
			logger.debug("KeyToolUsage(String) - start"); //$NON-NLS-1$
		}

		StringBuffer sb = new StringBuffer();
		sb.append("============= KEYTOOL USAGE ==============").append(lineSep).append(lineSep);
		sb.append(" --> Generate a Java keystore and key pair ").append(lineSep);
		sb.append(" keytool -genkey -alias '" + alias + "' -keyalg RSA -keystore <keystore file>").append(lineSep).append(lineSep);

		sb.append(" --> Generate a certificate signing request (CSR) for an existing Java keystore ").append(lineSep);
		sb.append(" keytool -certreq -alias '" + alias + "' -keystore <keystore file> -file domain.csr").append(lineSep).append(lineSep);

		sb.append(" --> Import a root or intermediate CA certificate to an existing Java keystore ").append(lineSep);
		sb.append(" keytool -import -trustcacerts -alias root -file <certificate authority certificate> -keystore <keystore file>").append(lineSep).append(lineSep);

		sb.append(" --> Import a signed primary certificate to an existing Java keystore ").append(lineSep);
		sb.append(" keytool -import -trustcacerts -alias '" + alias + "' -file mydomain.crt -keystore <keystore file>").append(lineSep);
		sb.append("============= KEYTOOL USAGE ==============").append(lineSep);

		Object returnObject = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("KeyToolUsage(String) - end"); //$NON-NLS-1$
		}
		return returnObject;
	}


	private static void usage() {
		if (logger.isDebugEnabled()) {
			logger.debug("usage() - start"); //$NON-NLS-1$
		}

		System.err.println("Usage: java " + Generate.class.getName() + " <config_file>");
		System.err.println("       config_file: config file used for Bancs SAML Authentication Filter");

		if (logger.isDebugEnabled()) {
			logger.debug("usage() - end"); //$NON-NLS-1$
		}
	}

}
