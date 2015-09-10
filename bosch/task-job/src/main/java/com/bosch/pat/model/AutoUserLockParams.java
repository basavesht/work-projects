package com.bosch.pat.model;

// TODO: Auto-generated Javadoc
/**
 * The Class AutoSystemReviewParams.
 */
public class AutoUserLockParams {
	/** The prev exec date utcs. */
	private long prevExecDateUtcs;
	  
  	/** The def Idle days. */
  	private int defIdleDays;
	  
  	/** The reserve1. */
  	private String reserve1;
	  
  	/** The reserve2. */
  	private String reserve2;
	  
  	/** The session count. */
  	private int inactiveUserCount;
	  
  	/** The return code. */
  	private int returnCode;
	  
  	/** The return desc. */
  	private String returnDesc;

	public long getPrevExecDateUtcs() {
		return prevExecDateUtcs;
	}

	public void setPrevExecDateUtcs(long prevExecDateUtcs) {
		this.prevExecDateUtcs = prevExecDateUtcs;
	}

	public int getDefIdleDays() {
		return defIdleDays;
	}

	public void setDefIdleDays(int defIdleDays) {
		this.defIdleDays = defIdleDays;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public int getInactiveUserCount() {
		return inactiveUserCount;
	}

	public void setInactiveUserCount(int inactiveUserCount) {
		this.inactiveUserCount = inactiveUserCount;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	@Override
	public String toString() {
		return "AutoUserLockParams [prevExecDateUtcs=" + prevExecDateUtcs
				+ ", defIdleDays=" + defIdleDays + ", reserve1=" + reserve1
				+ ", reserve2=" + reserve2 + ", inactiveUserCount="
				+ inactiveUserCount + ", returnCode=" + returnCode
				+ ", returnDesc=" + returnDesc + "]";
	}
  	
  	


}
