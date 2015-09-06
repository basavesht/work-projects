package com.tcs.bancs.channels.context;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703			31-05-2011      P3				 Server-side check.
 * **********************************************************
 */
public class ServiceContext implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3556780297317873499L;

	private ArrayList<Message> messages = new ArrayList<Message>();
	private boolean isRTACommitReq = false;
	private boolean isServiceCallSuccessful = false;
	private boolean isRTARollbackReq = false;
	private boolean callRTAImmediateCommit = false;
	private transient Connection connection = null;

	public boolean isCallRTAImmediateCommit() {
		return callRTAImmediateCommit;
	}

	public void setCallRTAImmediateCommit(boolean callRTAImmediateCommit) {
		this.callRTAImmediateCommit = callRTAImmediateCommit;
	}

	public void setConnection(Connection conn){
		this.connection=conn;
	}

	public Connection getConnection() {
		return connection;
	}

	public boolean isRTACommitReq() {
		return isRTACommitReq;
	}

	public void setRTACommitReq(boolean isRTACommitReq) {
		this.isRTACommitReq = isRTACommitReq;
	}

	public boolean isServiceCallSuccessful() {
		return isServiceCallSuccessful;
	}

	public void setServiceCallSuccessful(boolean isServiceCallSuccessful) {
		this.isServiceCallSuccessful = isServiceCallSuccessful;
	}

	public boolean isRTARollbackReq() {
		return isRTARollbackReq;
	}

	public void setRTARollbackReq(boolean isRTARollbackReq) {
		this.isRTARollbackReq = isRTARollbackReq;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public void addMessage(MessageType type, int code, String ... args) {
		this.messages.add(getMessageObject(type, code, args));
	}

	public void removeMessage(MessageType type, int code, String ... args) {
		this.messages.remove(getMessageObject(type, code, args));
	}

	private Message getMessageObject(MessageType type, int code, String[] args) 
	{
		Message message = null;
		if ( type == MessageType.INFORMATION)
			message = new Information();
		if ( type == MessageType.WARNING)
			message = new Warning();
		if ( type == MessageType.RISK)
			message = new Risk();
		if ( type == MessageType.ERROR)
			message = new ChannelError();
		if ( type == MessageType.SEVERE)
			message = new Severe();
		if ( type == MessageType.CRITICAL)
			message = new Critical();
		if (message == null )
			message = new Critical();
		message.setCode(code);
		for(String arg : args)
		{
			message.addArg(arg);
		}
		return message;
	}

	public MessageType getMaxSeverity() 
	{
		MessageType retVal = MessageType.SUCCESS;
		for( Message mesg : messages)
		{
			if ( isGreater(mesg.getType(),retVal))
				retVal = mesg.getType();
		}
		return retVal;
	}

	private boolean isGreater(MessageType type1, MessageType type2) 
	{
		if ( type1 == MessageType.SUCCESS && type2 == MessageType.SUCCESS)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.INFORMATION)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.WARNING)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.RISK)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.ERROR)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.SUCCESS && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.INFORMATION)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.WARNING)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.RISK)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.ERROR)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.INFORMATION && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.INFORMATION)
			return true;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.WARNING)
			return false;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.RISK)
			return false;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.ERROR)
			return false;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.WARNING && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.RISK && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.RISK && type2 == MessageType.INFORMATION)
			return true;
		else if ( type1 == MessageType.RISK && type2 == MessageType.WARNING)
			return true;
		else if ( type1 == MessageType.RISK && type2 == MessageType.RISK)
			return false;
		else if ( type1 == MessageType.RISK && type2 == MessageType.ERROR)
			return false;
		else if ( type1 == MessageType.RISK && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.RISK && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.INFORMATION)
			return true;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.WARNING)
			return true;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.RISK)
			return true;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.ERROR)
			return false;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.ERROR && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.INFORMATION)
			return true;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.WARNING)
			return true;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.RISK)
			return true;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.ERROR)
			return true;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.SEVERE)
			return false;
		else if ( type1 == MessageType.SEVERE && type2 == MessageType.CRITICAL)
			return false;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.SUCCESS)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.INFORMATION)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.WARNING)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.RISK)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.ERROR)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.SEVERE)
			return true;
		else if ( type1 == MessageType.CRITICAL && type2 == MessageType.CRITICAL)
			return false;
		else
			return false;
	}
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "ServiceContext ( "
			+ super.toString() + TAB
			+ "messages = " + this.messages + TAB
			+ "isRTACommitReq = " + this.isRTACommitReq + TAB
			+ "isServiceCallSuccessful = " + this.isServiceCallSuccessful + TAB
			+ "isRTARollbackReq = " + this.isRTARollbackReq + TAB
			+ "callRTAImmediateCommit = " + this.callRTAImmediateCommit + TAB
			+ "connection = " + this.connection + TAB
			+ " )";

		return retValue;
	}

}
