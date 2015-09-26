package com.tcs.bancs.objects.schema.response.payments.transfers;

public enum ReturnCode {
  SUCCESS(0),
  FAILURE(-1);
  
  private int i = 0;
  ReturnCode(int i){
	  this.i = i;
  }
  
  public int getCode(){
	  return i;
  }
}
