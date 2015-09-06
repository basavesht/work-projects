package com.tcs.bancs.ui.helpers;

import java.io.IOException;

import org.apache.log4j.Logger;


public class Base64Util {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Base64Util.class);

	public static byte[] decode(String arg0) throws IOException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("decode(String) - start"); //$NON-NLS-1$
		}

		byte[] returnbyteArray = Base64Coder.decodeLines(arg0);
		if (logger.isDebugEnabled()) {
			logger.debug("decode(String) - end"); //$NON-NLS-1$
		}
		return returnbyteArray;
	}
	public static String encode(byte[] arg0) {
		if (logger.isDebugEnabled()) {
			logger.debug("encode(byte[]) - start"); //$NON-NLS-1$
		}

		String returnString = Base64Coder.encodeLines(arg0);
		if (logger.isDebugEnabled()) {
			logger.debug("encode(byte[]) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	public static void main(String[] args) throws IOException{
		String s = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48c2FtbHA6UmVzcG9uc2UgeG1s bnM6c2FtbHA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCIgSUQ9ImQzZjdl NTEzLWQzZDEtNDgxZS05NmRiLWE2N2Y0ZDJjMjFlZCIgSXNzdWVJbnN0YW50PSIyMDEwLTAzLTI0 VDA0OjIxOjAyLjIzN1oiIFZlcnNpb249IjIuMCI+PHNhbWw6SXNzdWVyIHhtbG5zOnNhbWw9InVy bjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iIEZvcm1hdD0idXJuOm9hc2lzOm5h bWVzOnRjOlNBTUw6Mi4wOm5hbWVpZC1mb3JtYXQ6ZW50aXR5Ij5DbGllbnRTZXJ2PC9zYW1sOklz c3Vlcj48c2FtbHA6U3RhdHVzPjxzYW1scDpTdGF0dXNDb2RlIFZhbHVlPSJ1cm46b2FzaXM6bmFt ZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiLz48L3NhbWxwOlN0YXR1cz48c2FtbDpBc3Nl cnRpb24geG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiIg SUQ9IjhkMjc5YzljLTMyOTgtNDFlYy1iMWMzLTE1NDUxNTlmMGIxNyIgSXNzdWVJbnN0YW50PSIy MDEwLTAzLTI0VDA0OjIxOjAyLjIzN1oiIFZlcnNpb249IjIuMCI+PHNhbWw6SXNzdWVyIEZvcm1h dD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOm5hbWVpZC1mb3JtYXQ6ZW50aXR5Ij5DbGll bnRTZXJ2PC9zYW1sOklzc3Vlcj48c2FtbDpTdWJqZWN0PjxzYW1sOlN1YmplY3RDb25maXJtYXRp b24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDpT dWJqZWN0Q29uZmlybWF0aW9uRGF0YSBOb3RPbk9yQWZ0ZXI9IjIwMTAtMDMtMjRUMDQ6MjY6MDIu MjM3WiIvPjwvc2FtbDpTdWJqZWN0Q29uZmlybWF0aW9uPjwvc2FtbDpTdWJqZWN0PjxzYW1sOkNv bmRpdGlvbnMgTm90QmVmb3JlPSIyMDEwLTAzLTI0VDA0OjIxOjAyLjIzN1oiIE5vdE9uT3JBZnRl cj0iMjAxMC0wMy0yNFQwNDoyNjowMi4yMzdaIj48c2FtbDpBdWRpZW5jZVJlc3RyaWN0aW9uPjxz YW1sOkF1ZGllbmNlPk1vbmV5TW92ZW1lbnQ8L3NhbWw6QXVkaWVuY2U+PHNhbWw6QXVkaWVuY2U+ aHR0cDovL21tLm1vcmdhbnN0YW5sZXljbGllbnRzZXJ2LmNvbS9Nb25leU1vdmVtZW50PC9zYW1s OkF1ZGllbmNlPjwvc2FtbDpBdWRpZW5jZVJlc3RyaWN0aW9uPjwvc2FtbDpDb25kaXRpb25zPjxz YW1sOkF0dHJpYnV0ZVN0YXRlbWVudD48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iTU1Db250ZXh0Ij48 c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxT Y2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFu Y2UiIHhzaTp0eXBlPSJ4czpzdHJpbmciPlBEOTRiV3dnZG1WeWMybHZiajBpTVM0d0lpQmxibU52 WkdsdVp6MGlkWFJtTFRnaVB6NEtDanhOVFVOdmJuUmxlSFFnZUcxc2JuTTZlSE5wUFNKb2RIUndP aTh2ZDNkM0xuY3pMbTl5Wnk4eU1EQXhMMWhOVEZOamFHVnRZUzFwYm5OMFlXNWpaU0lLQ2dsNGJX eHVjenA0YzJROUltaDBkSEE2THk5M2QzY3Vkek11YjNKbkx6SXdNREV2V0UxTVUyTm9aVzFoSWo0 S0NnazhURzluYVc1SlpENXRiM1psYldWdWREd3ZURzluYVc1SlpENEtDZ2tKUEZWVlNVUStOV1Jo TVdRd09HUXROalJqT1MwMFpEWTVMV0kwTlRJdE5XRmpNemxqWW1SaVlUa3pQQzlWVlVsRVBnb0pD VHdoTFMxVlZVbEVQamhtWW1JNFptRmlMV0U1WkdZdE5EZzFZeTA0TWpVMkxUQTFOMkV3TW1GbVpU VmhORHd2VlZWSlJDMHRQZ29KQ1R3aExTMVZWVWxFUGpFeU15QjFkV2xrSUdseklHNTFiR3c4TDFW VlNVUXRMVDRLQ1FrOFJtbHljM1JPWVcxbFBrTnNhV1Z1ZEZObGNuWThMMFpwY25OMFRtRnRaVDRL Q2drSlBFMXBaR1JzWlVsdWFYUnBZV3dnTHo0S0Nna0pQRXhoYzNST1lXMWxQbUZuWVhJOEwweGhj M1JPWVcxbFBnb0tDUWs4UmtGSmJtUnBZMkYwYjNJK1ptRnNjMlU4TDBaQlNXNWthV05oZEc5eVBn b0tDZ2tKUENFdExTQkZiR1Z0Wlc1MGN5Qm1iM0lnVkhKaGJuTmhZM1JwYjI0Z1RXOXVhWFJ2Y21s dVp5QXNJR1p2Y2lCU1UwRWdMUzArQ2drSlBFUmxkbWxqWlZCeWFXNTBQa1JsZG1salpWQnlhVzUw UEM5RVpYWnBZMlZRY21sdWRENEtDUWs4UkdWMmFXTmxRMjl2YTJsbFBrUmxkbWxqWlVOdmIydHBa VHd2UkdWMmFXTmxRMjl2YTJsbFBnb0pDVHhFWlhacFkyVkdVMDgrUkdWMmFXTmxSbE5QUEM5RVpY WnBZMlZHVTA4K0Nna0pQRU5UVTJWemMybHZia2xFUGtOVFUyVnpjMmx2Ymtsa1BDOURVMU5sYzNO cGIyNUpSRDRLQ1FrOFEyeHBaVzUwU1ZCQlpHUnlaWE56UGpFNU1pNHhPUzR4TURNdU1qazhMME5z YVdWdWRFbFFRV1JrY21WemN6NEtDUWs4VEdGemRFRmpZMjkxYm5SUGNHVnVSR0YwWlQ0eU1EQTVM VEV4TFRBMVBDOU1ZWE4wUVdOamIzVnVkRTl3Wlc1RVlYUmxQZ29KQ1R4TVlYTjBUMjVzYVc1bFUy VnlkbWxqWlZCaGMzTjNiM0prUTJoaGJtZGxSR0YwWlQ0eU1EQTVMVEV4TFRBMVBDOU1ZWE4wVDI1 c2FXNWxVMlZ5ZG1salpWQmhjM04zYjNKa1EyaGhibWRsUkdGMFpUNEtDUWs4VDI1c2FXNWxVMlZ5 ZG1salpVVnVjbTlzYkVSaGRHVStNakF3T1MweE1TMHdOVHd2VDI1c2FXNWxVMlZ5ZG1salpVVnVj bTlzYkVSaGRHVStDZ2tKUEV4aGMzUkJaR1J5WlhOelEyaGhibWRsUkdGMFpUNHlNREE1TFRFeExU QTFQQzlNWVhOMFFXUmtjbVZ6YzBOb1lXNW5aVVJoZEdVK0Nna0pQRXhoYzNSUWFHOXVaVU5vWVc1 blpVUmhkR1UrTWpBd09TMHhNUzB3TlR3dlRHRnpkRkJvYjI1bFEyaGhibWRsUkdGMFpUNEtDUWs4 VEdGemRFVnRZV2xzUTJoaGJtZGxSR0YwWlQ0eU1EQTVMVEV4TFRBMVBDOU1ZWE4wUlcxaGFXeERh R0Z1WjJWRVlYUmxQZ29KQ1FvSlBFRmpZMjkxYm5SelBnb0tDUWs4UVdOamIzVnVkRDRLQ2lBZ0lD QWdJRHhCWTJOdmRXNTBUbTgrTVRJME5UWTRQQzlCWTJOdmRXNTBUbTgrQ2lBZ0lDQWdJRHhQWm1a cFkyVk9iejR4TURFOEwwOW1abWxqWlU1dlBnb2dJQ0FnSUNBOFJrRk9iejR3TmpROEwwWkJUbTgr Q2lBZ0lDQWdJRHhMWlhsQlkyTnZkVzUwUGpJd01ESXRNREV0TWpBdE1UQXVNRGd1TURJdU16SXlO REkwUEM5TFpYbEJZMk52ZFc1MFBnb2dJQ0FnSUNBOFJuSnBaVzVrYkhsT1lXMWxQa0ZEUTBWVFV5 QkpVa0VnTVRBeElERXlORFUyT0R3dlJuSnBaVzVrYkhsT1lXMWxQZ29nSUNBZ0lDQThWbWxsZDFS eVlXNXpZV04wYVc5dVJteGhaejUwY25WbFBDOVdhV1YzVkhKaGJuTmhZM1JwYjI1R2JHRm5QZ29n SUNBZ0lDQThRV05qYjNWdWRGTjBZWFIxY3o0d1BDOUJZMk52ZFc1MFUzUmhkSFZ6UGdvZ0lDQWdJ Q0E4UVdOamIzVnVkRU5zWVhOelBsbzhMMEZqWTI5MWJuUkRiR0Z6Y3o0S0lDQWdJQ0FnUEU1dmRu VnpVM1ZpVUhKdlpIVmpkQ0F2UGdvZ0lDQWdJQ0E4UkdsMlVHRjVQalU4TDBScGRsQmhlVDRLSUNB Z0lDQWdQRU5zYVdWdWRFTmhkR1ZuYjNKNVBsQThMME5zYVdWdWRFTmhkR1ZuYjNKNVBnb2dJQ0Fn SUNBOFEyaHZhV05sUm5WdVpFTnZaR1UrU1V4QlJqd3ZRMmh2YVdObFJuVnVaRU52WkdVK0NpQWdJ Q0FnSUR3aExTMUpVa0ZEYjJSbFBqRTBNVHd2U1ZKQlEyOWtaUzB0UGdvZ0lDQWdJQ0E4VkhKaFpH VkRiMjUwY205c1BrdzhMMVJ5WVdSbFEyOXVkSEp2YkQ0S0lDQWdJQ0FnUEVGalkyOTFiblJEWVhS bFoyOXllVDR3UEM5QlkyTnZkVzUwUTJGMFpXZHZjbmsrQ2lBZ0lDQWdJRHhEYjJ4c1lYUmxjbUZz UVdOamRFbHVaQ0F2UGdvS0NRazhMMEZqWTI5MWJuUStDZ29LQ1FrOFFXTmpiM1Z1ZEQ0S0Nna0pD VHhCWTJOdmRXNTBUbTgrTURNeE1qUTBQQzlCWTJOdmRXNTBUbTgrQ2dvSkNRazhUMlptYVdObFRt OCtORGMyUEM5UFptWnBZMlZPYno0S0Nna0pDVHhHUVU1dlBqSTVOand2UmtGT2J6NEtDZ2tKQ1R4 TFpYbEJZMk52ZFc1MFBqRTVPVGt0TURndE1qY3RNVGN1TWpRdU1qRXVOREUyTnpnNFBDOUxaWGxC WTJOdmRXNTBQZ29LQ1FrSlBFWnlhV1Z1Wkd4NVRtRnRaVDVEVWpZeUlEUTNOaUF6TVRJME5Ed3ZS bkpwWlc1a2JIbE9ZVzFsUGdvS0NRa0pQRlpwWlhkVWNtRnVjMkZqZEdsdmJrWnNZV2MrZEhKMVpU d3ZWbWxsZDFSeVlXNXpZV04wYVc5dVJteGhaejRLQ2drSkNUeEJZMk52ZFc1MFUzUmhkSFZ6UGpB OEwwRmpZMjkxYm5SVGRHRjBkWE0rQ2dvSkNRazhRV05qYjNWdWRFTnNZWE56UGprOEwwRmpZMjkx Ym5SRGJHRnpjejRLQ2drSkNUeEVhWFpRWVhrK016d3ZSR2wyVUdGNVBnb0tDUWtKUEVOc2FXVnVk RU5oZEdWbmIzSjVQbEE4TDBOc2FXVnVkRU5oZEdWbmIzSjVQZ29LQ1FrSlBFTm9iMmxqWlVaMWJt UkRiMlJsUGtKRVVGTThMME5vYjJsalpVWjFibVJEYjJSbFBnb0tDUWtKUEZSeVlXUmxRMjl1ZEhK dmJENVVjbUZrWlVOdmJqd3ZWSEpoWkdWRGIyNTBjbTlzUGdvS0NRa0pQRUZqWTI5MWJuUkRZWFJs WjI5eWVUNUJZMk52ZFc1MFEyRjBQQzlCWTJOdmRXNTBRMkYwWldkdmNuaytDZ29KQ1FrOFEyOXNi R0YwWlhKaGJFRmpZM1JKYm1RK1EyOXNiR0YwWlhKaGJFRmpZM1JKYm1ROEwwTnZiR3hoZEdWeVlX eEJZMk4wU1c1a1Bnb0tDUWs4TDBGalkyOTFiblErQ2dvSkNUeEJZMk52ZFc1MFBnb0tDUWtKUEVG alkyOTFiblJPYno0d01UQTBOVGc4TDBGalkyOTFiblJPYno0S0Nna0pDVHhQWm1acFkyVk9iejR4 TURFOEwwOW1abWxqWlU1dlBnb0tDUWtKUEVaQlRtOCtNVGsxUEM5R1FVNXZQZ29LQ1FrSlBFdGxl VUZqWTI5MWJuUStNakF3TWkwd01TMHlNQzB4TVM0MU1TNHdNUzR6TkRrNU56azhMMHRsZVVGalky OTFiblErQ2dvSkNRazhSbkpwWlc1a2JIbE9ZVzFsUGpFeU16UTFOamM0T1RBeE1qTTBOVFkzT0Rr d01USXpORFUyTnpnZ01USXpORFUyTnpnNU1Ed3ZSbkpwWlc1a2JIbE9ZVzFsUGdvS0NRa0pQRlpw WlhkVWNtRnVjMkZqZEdsdmJrWnNZV2MrZEhKMVpUd3ZWbWxsZDFSeVlXNXpZV04wYVc5dVJteGha ejRLQ2drSkNUeEJZMk52ZFc1MFUzUmhkSFZ6UGpBOEwwRmpZMjkxYm5SVGRHRjBkWE0rQ2dvSkNR azhRV05qYjNWdWRFTnNZWE56UGpFOEwwRmpZMjkxYm5SRGJHRnpjejRLQ2drSkNUeEVhWFpRWVhr K016d3ZSR2wyVUdGNVBnb0tDUWtKUEVOc2FXVnVkRU5oZEdWbmIzSjVQbEE4TDBOc2FXVnVkRU5o ZEdWbmIzSjVQZ29LQ1FrSlBGUnlZV1JsUTI5dWRISnZiRDVVY21Ga1pVTnZiand2VkhKaFpHVkRi MjUwY205c1Bnb0tDUWtKUEVGalkyOTFiblJEWVhSbFoyOXllVDVCWTJOdmRXNTBRMkYwUEM5Qlky TnZkVzUwUTJGMFpXZHZjbmsrQ2dvSkNUd3ZRV05qYjNWdWRENEtDZ2tKUEVGalkyOTFiblErQ2dv SkNRazhRV05qYjNWdWRFNXZQakEzTVRFeE5Ed3ZRV05qYjNWdWRFNXZQZ29LQ1FrSlBFOW1abWxq WlU1dlBqRXdNVHd2VDJabWFXTmxUbTgrQ2dvSkNRazhSa0ZPYno0eE9UVThMMFpCVG04K0Nnb0pD UWs4UzJWNVFXTmpiM1Z1ZEQ0eU1EQTBMVEExTFRJd0xURTJMakl6TGpNMkxqY3lORE0yTlR3dlMy VjVRV05qYjNWdWRENEtDZ2tKQ1R4R2NtbGxibVJzZVU1aGJXVStRVUZCSURFd01TQXdOekV4TVRR OEwwWnlhV1Z1Wkd4NVRtRnRaVDRLQ2drSkNUeFdhV1YzVkhKaGJuTmhZM1JwYjI1R2JHRm5QblJ5 ZFdVOEwxWnBaWGRVY21GdWMyRmpkR2x2Ymtac1lXYytDZ29KQ1FrOFFXTmpiM1Z1ZEZOMFlYUjFj ejR3UEM5QlkyTnZkVzUwVTNSaGRIVnpQZ29LQ1FrSlBFRmpZMjkxYm5SRGJHRnpjejQ1UEM5Qlky TnZkVzUwUTJ4aGMzTStDZ29KQ1FrOFJHbDJVR0Y1UGpNOEwwUnBkbEJoZVQ0S0Nna0pDVHhEYkds bGJuUkRZWFJsWjI5eWVUNVFQQzlEYkdsbGJuUkRZWFJsWjI5eWVUNEtDZ2tKQ1R4RGFHOXBZMlZH ZFc1a1EyOWtaVDVDUkZCVFBDOURhRzlwWTJWR2RXNWtRMjlrWlQ0S0Nna0pDVHhVY21Ga1pVTnZi blJ5YjJ3K1ZISmhaR1ZEYjI0OEwxUnlZV1JsUTI5dWRISnZiRDRLQ2drSkNUeEJZMk52ZFc1MFEy RjBaV2R2Y25rK1FXTmpiM1Z1ZEVOaGREd3ZRV05qYjNWdWRFTmhkR1ZuYjNKNVBnb0tDUWtKUEVO dmJHeGhkR1Z5WVd4QlkyTjBTVzVrUGtOdmJHeGhkR1Z5WVd4QlkyTjBTVzVrUEM5RGIyeHNZWFJs Y21Gc1FXTmpkRWx1WkQ0S0Nna0pQQzlCWTJOdmRXNTBQZ29LQ2drSlBFRmpZMjkxYm5RK0Nnb0pD UWs4UVdOamIzVnVkRTV2UGpBMk16STBNVHd2UVdOamIzVnVkRTV2UGdvS0NRa0pQRTltWm1salpV NXZQakV3TVR3dlQyWm1hV05sVG04K0Nnb0pDUWs4UmtGT2J6NDFNekU4TDBaQlRtOCtDZ29KQ1Fr OFMyVjVRV05qYjNWdWRENHlNREF6TFRBM0xUSXlMVEl4TGpRMUxqRTRMalkwTlRJd05Ud3ZTMlY1 UVdOamIzVnVkRDRLQ2drSkNUeEdjbWxsYm1Sc2VVNWhiV1UrU1ZKQklERXdNU0F3TmpNeU5ERThM MFp5YVdWdVpHeDVUbUZ0WlQ0S0Nna0pDVHhXYVdWM1ZISmhibk5oWTNScGIyNUdiR0ZuUG5SeWRX VThMMVpwWlhkVWNtRnVjMkZqZEdsdmJrWnNZV2MrQ2dvSkNRazhRV05qYjNWdWRGTjBZWFIxY3o0 d1BDOUJZMk52ZFc1MFUzUmhkSFZ6UGdvS0NRa0pQRUZqWTI5MWJuUkRiR0Z6Y3o1YVBDOUJZMk52 ZFc1MFEyeGhjM00rQ2dvSkNRazhSR2wyVUdGNVBqVThMMFJwZGxCaGVUNEtDZ2tKQ1R4RGFHOXBZ MlZHZFc1a1EyOWtaVDVKVEVGR1BDOURhRzlwWTJWR2RXNWtRMjlrWlQ0S0Nna0pDVHhKVWtGRGIy UmxQak0yTVR3dlNWSkJRMjlrWlQ0S0Nna0pDVHhVY21Ga1pVTnZiblJ5YjJ3K1ZISmhaR1ZEYjI0 OEwxUnlZV1JsUTI5dWRISnZiRDRLQ2drSkNUeEJZMk52ZFc1MFEyRjBaV2R2Y25rK1FXTmpiM1Z1 ZEVOaGREd3ZRV05qYjNWdWRFTmhkR1ZuYjNKNVBnb0tDUWs4TDBGalkyOTFiblErQ2dvSkNRb0tD UWs4UVdOamIzVnVkRDRLQ2drSkNUeEJZMk52ZFc1MFRtOCtNREUxTVRNd1BDOUJZMk52ZFc1MFRt OCtDZ29KQ1FrOFQyWm1hV05sVG04K01UQXhQQzlQWm1acFkyVk9iejRLQ2drSkNUeEdRVTV2UGpB d01Ud3ZSa0ZPYno0S0Nna0pDVHhMWlhsQlkyTnZkVzUwUGpJd01EZ3RNREV0TVRRdE1UTXVOVEV1 TURjdU1qWTFOVGN5UEM5TFpYbEJZMk52ZFc1MFBnb0tDUWtKUEU1cFkydE9ZVzFsUGtkdmIyUWdR V05qYjNWdWRDQXhNREVnTURFMU1UTXdQQzlPYVdOclRtRnRaVDRLQ2drSkNUeEdjbWxsYm1Sc2VV NWhiV1UrUVVGQklERXdNU0F3TVRVeE16QThMMFp5YVdWdVpHeDVUbUZ0WlQ0S0Nna0pDVHhXYVdW M1ZISmhibk5oWTNScGIyNUdiR0ZuUG5SeWRXVThMMVpwWlhkVWNtRnVjMkZqZEdsdmJrWnNZV2Mr Q2dvSkNRazhRV05qYjNWdWRGTjBZWFIxY3o0d1BDOUJZMk52ZFc1MFUzUmhkSFZ6UGdvS0NRa0pQ RUZqWTI5MWJuUkRiR0Z6Y3o0d1BDOUJZMk52ZFc1MFEyeGhjM00rQ2dvSkNRazhSR2wyVUdGNVBq TThMMFJwZGxCaGVUNEtDZ2tKQ1R4RGJHbGxiblJEWVhSbFoyOXllVDVRUEM5RGJHbGxiblJEWVhS bFoyOXllVDRLQ2drSkNUeERhRzlwWTJWR2RXNWtRMjlrWlQ1Q1JGQlRQQzlEYUc5cFkyVkdkVzVr UTI5a1pUNEtDZ2tKQ1R4VWNtRmtaVU52Ym5SeWIydytWSEpoWkdWRGIyNDhMMVJ5WVdSbFEyOXVk SEp2YkQ0S0Nna0pDVHhCWTJOdmRXNTBRMkYwWldkdmNuaytRV05qYjNWdWRFTmhkRHd2UVdOamIz VnVkRU5oZEdWbmIzSjVQZ2tKQ1FvS0NRazhMMEZqWTI5MWJuUStDZ29LQ2drSlBFRmpZMjkxYm5R K0Nnb0pDUWs4UVdOamIzVnVkRTV2UGpFek9EYzRNRHd2UVdOamIzVnVkRTV2UGdvS0NRa0pQRTlt Wm1salpVNXZQakl6T0R3dlQyWm1hV05sVG04K0Nnb0pDUWs4UmtGT2J6NDBOand2UmtGT2J6NEtD Z2tKQ1R4TFpYbEJZMk52ZFc1MFBqSXdNREl0TURFdE1qTXRNakV1TXpBdU5EQXVOVEkyTXpJNFBD OUxaWGxCWTJOdmRXNTBQZ29LQ1FrSlBFWnlhV1Z1Wkd4NVRtRnRaVDVEVWpNeUlESXpPQ0F4TXpn M09EQThMMFp5YVdWdVpHeDVUbUZ0WlQ0S0Nna0pDVHhXYVdWM1ZISmhibk5oWTNScGIyNUdiR0Zu UG5SeWRXVThMMVpwWlhkVWNtRnVjMkZqZEdsdmJrWnNZV2MrQ2dvSkNRazhRV05qYjNWdWRGTjBZ WFIxY3o0d1BDOUJZMk52ZFc1MFUzUmhkSFZ6UGdvS0NRa0pQRUZqWTI5MWJuUkRiR0Z6Y3o0eVBD OUJZMk52ZFc1MFEyeGhjM00rQ2dvSkNRazhSR2wyVUdGNVBqTThMMFJwZGxCaGVUNEtDZ2tKQ1R4 RGJHbGxiblJEWVhSbFoyOXllVDVRUEM5RGJHbGxiblJEWVhSbFoyOXllVDRLQ2drSkNUeERhRzlw WTJWR2RXNWtRMjlrWlQ1Q1JGQlRQQzlEYUc5cFkyVkdkVzVrUTI5a1pUNEtDZ2tKQ1R4VWNtRmta VU52Ym5SeWIydytWSEpoWkdWRGIyNDhMMVJ5WVdSbFEyOXVkSEp2YkQ0S0Nna0pDVHhCWTJOdmRX NTBRMkYwWldkdmNuaytRV05qYjNWdWRFTmhkR1ZuYjNKNVBDOUJZMk52ZFc1MFEyRjBaV2R2Y25r K0Nnb0pDUWs4UTI5c2JHRjBaWEpoYkVGalkzUkpibVErUTI5c2JHRjBaWEpoYkVGalkzUkpibVE4 TDBOdmJHeGhkR1Z5WVd4QlkyTjBTVzVrUGdvS0NRazhMMEZqWTI5MWJuUStDZ29LQ2drSlBFRmpZ MjkxYm5RK0Nnb0pDUWs4UVdOamIzVnVkRTV2UGpBeU5qZ3lOand2UVdOamIzVnVkRTV2UGdvS0NR a0pQRTltWm1salpVNXZQakl6T0R3dlQyWm1hV05sVG04K0Nnb0pDUWs4UmtGT2J6NDBOand2UmtG T2J6NEtDZ2tKQ1R4TFpYbEJZMk52ZFc1MFBqSXdNREl0TURRdE1EUXRNREF1TkRRdU1Ea3VOVFV6 TURRNFBDOUxaWGxCWTJOdmRXNTBQZ29LQ1FrSlBFWnlhV1Z1Wkd4NVRtRnRaVDVEVWpNeUlESXpP Q0F3TWpZNE1qWThMMFp5YVdWdVpHeDVUbUZ0WlQ0S0Nna0pDVHhXYVdWM1ZISmhibk5oWTNScGIy NUdiR0ZuUG5SeWRXVThMMVpwWlhkVWNtRnVjMkZqZEdsdmJrWnNZV2MrQ2dvSkNRazhRV05qYjNW dWRGTjBZWFIxY3o0d1BDOUJZMk52ZFc1MFUzUmhkSFZ6UGdvS0NRa0pQRUZqWTI5MWJuUkRiR0Z6 Y3o0eVBDOUJZMk52ZFc1MFEyeGhjM00rQ2dvSkNRazhSR2wyVUdGNVBqTThMMFJwZGxCaGVUNEtD Z2tKQ1R4RGJHbGxiblJEWVhSbFoyOXllVDVRUEM5RGJHbGxiblJEWVhSbFoyOXllVDRLQ2drSkNU eERhRzlwWTJWR2RXNWtRMjlrWlQ1Q1JGQlRQQzlEYUc5cFkyVkdkVzVrUTI5a1pUNEtDZ2tKQ1R4 VWNtRmtaVU52Ym5SeWIydytWSEpoWkdWRGIyNDhMMVJ5WVdSbFEyOXVkSEp2YkQ0S0Nna0pDVHhC WTJOdmRXNTBRMkYwWldkdmNuaytRV05qYjNWdWRFTmhkR1ZuYjNKNVBDOUJZMk52ZFc1MFEyRjBa V2R2Y25rK0Nnb0pDUWs4UTI5c2JHRjBaWEpoYkVGalkzUkpibVErUTI5c2JHRjBaWEpoYkVGalkz UkpibVE4TDBOdmJHeGhkR1Z5WVd4QlkyTjBTVzVrUGdvS0NRazhMMEZqWTI5MWJuUStDZ2tKQ2dr OEwwRmpZMjkxYm5SelBnb0tQQzlOVFVOdmJuUmxlSFErQ2dvS0Nnb0tDZ289PC9zYW1sOkF0dHJp YnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PC9zYW1sOkF0dHJpYnV0ZVN0YXRlbWVudD48L3Nh bWw6QXNzZXJ0aW9uPjwvc2FtbHA6UmVzcG9uc2U+";
		System.out.println(new String(decode(s)));
	}
}