package com.tcs.ebw.common.util;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class ByteUtil {
            private static char[] hexCharacters = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };
            
            public static String byteArrayToHex(byte[] ba){
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < ba.length; i++){
                    int hbits = (ba[i] & 0x000000f0) >> 4;
                    int lbits = ba[i] & 0x0000000f;
                    buffer.append("" + hexCharacters[hbits] + hexCharacters[lbits] + " ");
                }
                return buffer.toString();
            }
}

