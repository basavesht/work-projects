package com.bosch.tmp.integration.creation;

import com.bosch.tmp.integration.creation.results.ORUR01ResultsBuilder;
import com.bosch.tmp.integration.creation.results.ORUR30ResultsBuilder;

/**
 * This is the default creator which builds standard messages of a variety of types (ACK, ORU, etc).
 *
 * @author gtk1pal
 */
public class DefaultCreator implements Creator
{
	protected DefaultAckBuilder ackBuilder;
	protected Builder resultBuilder;

	public Object createMessage(String messageType, Object ... data) throws CreationException
	{
		if (messageType.equalsIgnoreCase("ACK"))
		{
			if (ackBuilder == null) {
				ackBuilder = new DefaultAckBuilder();
			}
			return ackBuilder.buildMessage(data);
		}
		else if (messageType.equalsIgnoreCase("ORU_R30"))
		{
			resultBuilder = new ORUR30ResultsBuilder();
			return resultBuilder.buildMessage(data);
		}
		else if (messageType.equalsIgnoreCase("ORU_R01"))
		{
			resultBuilder = new ORUR01ResultsBuilder();
			return resultBuilder.buildMessage(data);
		}
		else {
			return null;
		}
	}
}
