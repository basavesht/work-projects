package com.tcs.bancs.ui.helpers.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.filters.security.saml.SimpleKeyProperties;
/**
 * 
 * @param input - to be decrypted
 * @param keyBytes - decryption key
 * @param cryptoProvider - crypto provider class name
 * @param algorithm - algorithm - AES, DES etc.
 * @param mode - Mode - cipher mode - CBC, CFB8 etc.
 * @param padding - Cipher padding - PKCS5Padding, NoPadding etc.
 * @return decrypted byte array
 * @throws EncryptionException
 */

public class CryptUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CryptUtils.class);

	public static byte[] hexStringToByteArray(String s) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("hexStringToByteArray(String) - start"); //$NON-NLS-1$
		}
    
		int len = s.length();    
		byte[] data = new byte[len / 2];    
		for (int i = 0; i < len; i += 2) 
		{        
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) 
					+ Character.digit(s.charAt(i+1), 16));    
		}    

		if (logger.isDebugEnabled()) {
			logger.debug("hexStringToByteArray(String) - end"); //$NON-NLS-1$
		}
		return data;
	}


	/**
	 * 
	 * @param input
	 * @param keyInfo
	 * @return
	 * @throws EncryptionException
	 */
	public static byte[] decrypt(byte[] input, SimpleKeyProperties keyInfo) throws EncryptionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("decrypt(byte[], SimpleKeyProperties) - start"); //$NON-NLS-1$
		}

		try {
			PBECryptoUtils utils = new PBECryptoUtils(keyInfo.getAlgorithm(),keyInfo.getMode(), keyInfo.getPadding(), hexStringToByteArray(keyInfo.getKey()),hexStringToByteArray(keyInfo.getInit()));
			byte[] returnbyteArray = utils.decrypt(input);
			if (logger.isDebugEnabled()) {
				logger.debug("decrypt(byte[], SimpleKeyProperties) - end"); //$NON-NLS-1$
			}
			return returnbyteArray;
		} catch (NoSuchAlgorithmException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		} catch (NoSuchPaddingException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		} catch (InvalidKeyException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		} catch (IllegalBlockSizeException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		} catch (BadPaddingException e) {
			logger.error("decrypt(byte[], SimpleKeyProperties)", e); //$NON-NLS-1$

			throw new EncryptionException("Could not decrypt input" , e);
		}
		
	}

	public static void main(String[] args) throws  EncryptionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		String algo = "DESede";
		String init = "0982989239881128";
		String key = "9fb90a90096e202fecdbfe72117ed1efb1eabb2f0d26b72b";
		
		SimpleKeyProperties prop = new SimpleKeyProperties();
		prop.setAlgorithm(algo);
		prop.setInit(init);
		prop.setKey(key);
		
		prop.setMode("CFB8"); //CFB8, ECB
		
		prop.setPadding("PKCS5Padding"); //NoPadding, PKCS5Padding
		
		String cypherTextHexString = "AD44D387C6BEFAFC94EC0A99E227A926037DB6BCD66BF124634B143C4504B1CD27CAFE02DA07B5BB9DB3C98112365BAD570531D0823885AF76853705E2D06F4ACCCA70CD945DB7EB37E13098CCE9E5E44797467350377697153CEBA271A9D63F714FAF2A60CBACC626637FCA30CE21492C9B3340E3E9966E3B12B4FEE9D4D61D3AFB1CA05F78D97DE94AB4BE53F7C203F863137F574A9C38371AA05C54A0504CC209E0C245AF6F25025EB7172472035A9AD4BAD9FF721788E61985FB6AAD117BA76CE5CE71DACED80840CD66E637979871A402D22780262A42808F62F2D1AE96BD4F7A9DF15F4A1E022B90AD891852178D2AE65AE9654C1039EBE2DF4D17B6095E3F72B77B76E67FE17A422CFA92EBDC81D111A212B74FC07804C154381B53D0F0D60D629F2FA8F60C819C72C4CB6F2AEDD6B6523989D7BCE7A5DE5FDFA445928E65B0C414DC8B82FCA9E1009DA9CFCC2780E896CFB43510E58F344E3C995A2458F3032B063DDC1464B4665F1F8C45C0318D3B37AA91D7E7AE39E7DD7C161B20C07BD71EA0311301A0922E518BE2F748043D5D15A3AA5F9FD3265D7F8A1419EBFB0072DC50A1E7242093093A7823D7499BFB980039E96715DDDA20B08BA5A8F3D5DF6A7D9B31D4061DD0BBE3002CD016326012ABB426CD5F93A72643F78DE4F825158C8A1652201C631477575EBB3DD1412D78CC00790FDD03ADAB69CC33F98DBAEABC0B024396E560C385752A1074041C2B5B6186635EFA4316C22A41C57E39AAD37D0394AA470020DEE390F8632845E452174CB479657673C81321FD72ACD43DC094C625AA43FD00CAF07B93C337F71522502C143DEA259141EC0AD11058FF366AB54A4C7A966E6EBF92BABD245C73A6932D617B2FD8038C3F3735550A9F88382E355739F5741FC4646234560B3EE69FC015FCD792EAD934ABE128864112459A565C87158734D47E991A57160B26673D2BCFB1D50956939076D762EC4CC2B65A1AE1B9CDC4A3A54F8D39AC8C35DA7A401AFB8D6CE140D47827D7C0B2D893B09D9A653E164FD7772E2F0770EA37FA3BB33B31C010BA6AD8CFDDD15206E3EF2F236F26825E168E8D50DD12725F93C57575F081B1A480FC26FF0F9634DF4599F6DC01B6571248E1C3D5FF2E09F25F603F4D61FCC30BD2AE8D";
		
		byte[] cypherText = hexStringToByteArray(cypherTextHexString);
	    

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - The decrypted text is: " + new String(decrypt(cypherText, prop))); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}

}
