package com.tcs.bancs.channels.context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public abstract class Message implements Serializable 
{
	private int code = 0;
	private final ArrayList<String> args = new ArrayList<String>();
	public abstract MessageType getType();

	public final void addArg(String arg){
		args.add(arg);
	}

	public final int getCode(){
		return code;
	}

	public final void setCode(int code) {
		this.code = code;
	}

	public static String getExceptionMessage(Throwable t) 
	{
		//1. Log the exception here
		if ( t.getCause() == null ) {
			String text = t.getMessage();
			if ( text == null )
			{
				text = "Null Pointer Exception";
			}
			return text;
		}
		else {
			return getExceptionMessage(t.getCause() );
		}
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	/**
	 * Overrides the equals method of the base class object...
	 */
	public boolean equals(Object obj) 
	{
		boolean isEqual = false;
		if (obj == null) {
			return false; 
		}
		if (obj instanceof Message) {
			Message mesg = (Message)obj;
			if(mesg.getType().equals(getType())){
				if(mesg.getCode()== getCode()){
					isEqual = true;
				}
			}
		}
		return isEqual;
	}

	/**
	 * Overrides the hashCode method of the base class object...
	 */
	public int hashCode() {	
		return getCode();
	}
}
