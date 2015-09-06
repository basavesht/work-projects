package com.tcs.ebw.util.XMLtoByteConv;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author 231259
 * @year 2008
 */
public class BANCSResult {

	private final String version = "BANCSResult V1.1";
	private Map brht = null;

	public BANCSResult() {
		brht = new Hashtable();
	}

	public void setAttribute(String p0, Object p1) {
		brht.put(p0, p1);
	}

	public Object getAttribute(String p0) {
		if (brht.containsKey(p0)) {
			return brht.get(p0);
		}
		return null;
	}

	public String getString(String p0) {
		if (brht.containsKey(p0)) {
			return (String) brht.get(p0);
		}
		return "";
	}

	public int getInt(String p0) {
		if (!brht.containsKey(p0)) {
			return 0;
		} else {
			try {
				try {
					Integer I = (Integer) brht.get(p0);
					return I.intValue();
				} catch (NumberFormatException nfe) {
					return 0;
				}
			} catch (ClassCastException cce) {
				try {
					String val = (String) brht.get(p0);
					return Integer.valueOf(val).intValue();
				} catch (NumberFormatException nfe) {
					return 0;
				}
			}
		}
	}

	public double getDouble(String p0) {
		if (!brht.containsKey(p0)) {
			return 0;
		} else {
			try {
				try {
					Double D = (Double) brht.get(p0);
					return D.doubleValue();
				} catch (NumberFormatException nfe) {
					return 0;
				}
			} catch (ClassCastException cce) {
				try {
					String val = (String) brht.get(p0);
					return Double.valueOf(val).doubleValue();
				} catch (NumberFormatException nfe) {
					return 0;
				}
			}
		}
	}

	public String getAmount(String p0) {
		double amt = 0;
		DecimalFormat oNumber =
			(DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		oNumber.applyLocalizedPattern("###,###,##0.00;###,###,##0.00");
		if (!brht.containsKey(p0)) {
			return "";
		} else {
			try {
				amt = Double.valueOf((String) brht.get(p0)).doubleValue();
				return oNumber.format(amt);
			} catch (NumberFormatException e) {
				return "";
			}
		}
	}

	public String getAmount(String p0, String p1) {
		double amt = 0;
		DecimalFormat oNumber =
			(DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		oNumber.applyLocalizedPattern(p1);
		if (!brht.containsKey(p0)) {
			return "";
		} else {
			try {
				amt = Double.valueOf((String) brht.get(p0)).doubleValue();
				return oNumber.format(amt);
			} catch (NumberFormatException e) {
				return "";
			}
		}
	}

	public String getLocaleAmount(
		String p0,
		String p1) { // p1 - two-character country code
		double amt = 0;
		Locale loc;
		if ("uk".equals(p1)) {
			loc = Locale.UK;
		}
		if ("fr".equals(p1)) {
			loc = Locale.FRANCE;
		} else {
			loc = Locale.US;
		}

		DecimalFormat oNumber =
			(DecimalFormat) NumberFormat.getCurrencyInstance(loc);

		if (!brht.containsKey(p0)) {
			return oNumber.format(0);
		} else {
			try {
				amt = Double.valueOf((String) brht.get(p0)).doubleValue();
				return oNumber.format(amt);
			} catch (NumberFormatException e) {
				return oNumber.format(0);
			}
		}
	}

	public String getDate(String p0) {
//		DateFormat iDate = new SimpleDateFormat("ddMMyy");
//		DateFormat oDate = new SimpleDateFormat("dd/MM/yyyy");
		String returnDate = "";
		if (!brht.containsKey(p0)) {
			return "";
		} else {
			try {
				String strdate = (String) brht.get(p0);
				returnDate =
					"13"
						+ p0.substring(4, 6)
						+ "/"
						+ p0.substring(2, 4)
						+ "/"
						+ p0.substring(0, 2);

				return returnDate;
			} catch (Exception e) {
				return (String) brht.get(p0);
			}
		}
	}

	public String getDate(
		String p0,
		String p1,
		String p2) { // p1 - input date format p2 - output date format
		DateFormat iDate = new SimpleDateFormat(p1, new Locale("fa", "IR"));
		DateFormat oDate = new SimpleDateFormat(p2, new Locale("fa", "IR"));
		if (!brht.containsKey(p0)) {
			return "";
		} else {
			try {
				String strdate = (String) brht.get(p0);
				return oDate.format(iDate.parse(strdate));
			} catch (Exception e) {
				return (String) brht.get(p0);
			}
		}
	}

	public void listValues() {
		System.out.println(version + ": ht-(" + brht + ")");
	}

	public void removeAttribute(String p0) {
		brht.remove(p0);
	}

	public Set attributes() {
		return brht.keySet();
	}

	public void clear() {
		brht.clear();
	}

	/**
	 * Returns the brht.
	 * @return Map
	 */
	public Map getAttributes() {
		return brht;
	}
}
