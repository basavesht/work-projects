package com.bosch.tmp.integration.util;

public enum ORUBatchSegmentLimitsEnum
{
	MAX_NUM_OF_ORU_PER_BATCH(30),
	MAX_NUM_OF_ORDER_PER_ORU(10),
	MAX_NUM_OF_OBSERVATION_PER_ORDER(25),
	MAX_NUM_OF_NTE_PER_ORU(50),
	MAX_NUM_OF_NTE_PER_ORDER(50),
	ORU_BATCH_ALLOWED_CAPACITY(getORUBatchAllowedCapacity());

	private Integer limit;

	ORUBatchSegmentLimitsEnum(final Integer limit){
		this.limit = limit;
	}

	public Integer getLimit(){
		return limit;
	}

	public static Integer getORUBatchAllowedCapacity(){
		return (MAX_NUM_OF_ORU_PER_BATCH.limit * (MAX_NUM_OF_ORDER_PER_ORU.limit * MAX_NUM_OF_OBSERVATION_PER_ORDER.limit));
	}

	public static Integer getORUNotesAllowedCapacity(){
		return (MAX_NUM_OF_NTE_PER_ORU.limit + MAX_NUM_OF_NTE_PER_ORDER.limit);
	}

	public String toString(){
		return limit.toString();
	}
}
