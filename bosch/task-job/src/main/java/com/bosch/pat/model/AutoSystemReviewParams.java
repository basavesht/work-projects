package com.bosch.pat.model;

// TODO: Auto-generated Javadoc
/**
 * The Class AutoSystemReviewParams.
 */
public class AutoSystemReviewParams {
	/** The prev exec date utcs. */
	private long prevExecDateUtcs;
	  
  	/** The review time utcs. */
  	private long reviewTimeUtcs;
	  
  	/** The def review days. */
  	private int defReviewDays;
	  
  	/** The reserve1. */
  	private String reserve1;
	  
  	/** The reserve2. */
  	private String reserve2;
	  
  	/** The session count. */
  	private int sessionCount;
	  
  	/** The return code. */
  	private int returnCode;
	  
  	/** The return desc. */
  	private String returnDesc;
	  
  	/**
  	 * Gets the prev exec date utcs.
  	 *
  	 * @return the prev exec date utcs
  	 */
  	public long getPrevExecDateUtcs() {
		return prevExecDateUtcs;
	}
	
	/**
	 * Sets the prev exec date utcs.
	 *
	 * @param prevExecDateUtcs the new prev exec date utcs
	 */
	public void setPrevExecDateUtcs(long prevExecDateUtcs) {
		this.prevExecDateUtcs = prevExecDateUtcs;
	}
	
	/**
	 * Gets the review time utcs.
	 *
	 * @return the review time utcs
	 */
	public long getReviewTimeUtcs() {
		return reviewTimeUtcs;
	}
	
	/**
	 * Sets the review time utcs.
	 *
	 * @param reviewTimeUtcs the new review time utcs
	 */
	public void setReviewTimeUtcs(long reviewTimeUtcs) {
		this.reviewTimeUtcs = reviewTimeUtcs;
	}
	
	/**
	 * Gets the def review days.
	 *
	 * @return the def review days
	 */
	public int getDefReviewDays() {
		return defReviewDays;
	}
	
	/**
	 * Sets the def review days.
	 *
	 * @param defReviewDays the new def review days
	 */
	public void setDefReviewDays(int defReviewDays) {
		this.defReviewDays = defReviewDays;
	}
	
	/**
	 * Gets the reserve1.
	 *
	 * @return the reserve1
	 */
	public String getReserve1() {
		return reserve1;
	}
	
	/**
	 * Sets the reserve1.
	 *
	 * @param reserve1 the new reserve1
	 */
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	
	/**
	 * Gets the reserve2.
	 *
	 * @return the reserve2
	 */
	public String getReserve2() {
		return reserve2;
	}
	
	/**
	 * Sets the reserve2.
	 *
	 * @param reserve2 the new reserve2
	 */
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	
	/**
	 * Gets the session count.
	 *
	 * @return the session count
	 */
	public int getSessionCount() {
		return sessionCount;
	}
	
	/**
	 * Sets the session count.
	 *
	 * @param sessionCount the new session count
	 */
	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}
	
	/**
	 * Gets the return code.
	 *
	 * @return the return code
	 */
	public int getReturnCode() {
		return returnCode;
	}
	
	/**
	 * Sets the return code.
	 *
	 * @param returnCode the new return code
	 */
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	
	/**
	 * Gets the return desc.
	 *
	 * @return the return desc
	 */
	public String getReturnDesc() {
		return returnDesc;
	}
	
	/**
	 * Sets the return desc.
	 *
	 * @param returnDesc the new return desc
	 */
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AutoSystemReviewParams [prevExecDateUtcs=" + prevExecDateUtcs
				+ ", reviewTimeUtcs=" + reviewTimeUtcs + ", defReviewDays="
				+ defReviewDays + ", reserve1=" + reserve1 + ", reserve2="
				+ reserve2 + ", sessionCount=" + sessionCount + ", returnCode="
				+ returnCode + ", returnDesc=" + returnDesc + "]";
	}
	


}
