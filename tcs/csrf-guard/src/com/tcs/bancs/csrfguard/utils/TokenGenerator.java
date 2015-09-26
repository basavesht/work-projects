/**
 * OWASP CSRFGuard
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The CSRFGuard is published by OWASP under the LGPL. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */

package com.tcs.bancs.csrfguard.utils;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public final class TokenGenerator
{
	public static String generateRandomToken(String prng, int length) throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance(prng);
		byte[] bytes = new byte[length];
		
		sr.nextBytes(bytes);
		return new BASE64Encoder().encode(bytes);
	}
	
	public static String generateCSRFToken(String prng, int length) throws NoSuchAlgorithmException
	{
		String s = generateRandomToken(prng, length);
		
		return normalize(s, prng, true);
	}
	
	/**
	 * Make sure the token only has letters and numbers
	 * @param s
	 * @return
	 */
	public static String normalize(String s, String prng) throws NoSuchAlgorithmException
	{
		return normalize(s, prng, false);
	}
	
	/**
	 * Make sure the token only has letters and numbers.
	 * If we catch a bad char, we optionally replace it
	 * with an 'a'
	 * @param s
	 * @param replace
	 * @return
	 */
	public static String normalize(String s, String prng, boolean replace) throws NoSuchAlgorithmException
	{
		StringBuffer sb = new StringBuffer();
		int len = (s == null ? -1 : s.length());
		
		for(int i=0; i<len; i++)
		{
			char c = s.charAt(i);
			
			if((c >= 'a' && c <='z') || (c >= 'A' && c <='Z') || (c >= '0' && c <= '9'))
			{
				sb.append(c);
			}
			else if(replace)
			{
				char rnd = generateRandomChar(prng);
				
				sb.append(rnd);
			}
		}
		
		return sb.toString();
	}
	
	public static char generateRandomChar(String prng) throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance(prng);
		int i = sr.nextInt();
		char c = (char)((i%26)+97);
		return c;
	}
}
