package com.bosch.tmp.integration.process.results;

import org.hl7.v2xml.QVRQ17CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.bosch.tmp.integration.creation.results.ResultsCreationException;
import com.bosch.tmp.integration.process.ProcessException;
import com.bosch.tmp.integration.process.Processor;
import com.bosch.tmp.integration.util.AckTypeEnum;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.ORUBatchResponseMessageType;

/**
 * This class defines the complete Patient Results Processing Work-flow ...
 * @author BEA2KOR
 *
 */
@Service
public class PatientResultsProcessor implements Processor,BeanFactoryAware
{
	public static final Logger logger = LoggerFactory.getLogger(PatientResultsProcessor.class);

	/**
	 * Results work-flow processing. This method encapsulates the complete work-flow defined for the following results type.
		 1. Subjective Results.
		 2. Objective Results.
		 3. Notes.
	 */
	@Override
	public Object processMessage(String methodName, Object requestMsg) throws ProcessException
	{
		Object processResponseObj = null;

		//Check for any errors in the service context....
		if (errorStack.hasErrors()){
			throw new ProcessException("Cannot process message due to at least one validation error.");
		}

		//Get the result response message type...
		String resultResponseMessageType  =	(methodName != null && methodName.trim().equals("qvrQ17Operation")) ? ORUBatchResponseMessageType.ORU_R30.getBatchMessageType(): ORUBatchResponseMessageType.ORU_R01.getBatchMessageType();

		//Processing work-flow...
		try {
			if (requestMsg instanceof QVRQ17CONTENT) {
				processResponseObj = processQVRQ17Request((QVRQ17CONTENT) requestMsg, resultResponseMessageType);
			}
		}
		catch (ResultsProcessException ex) {
			logger.error("Results workflow processing failed.. ", ex);
			errorStack.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESULT_BATCH_PROCESSING_ERROR.toString()));
			throw new ProcessException("Results workflow processing failed.. ", ex);
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("Creation of ORU Batch response failed.. ", resultsCreateException);
			errorStack.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESULT_BATCH_CONSTRUCTION_ERROR.toString()));
			throw new ProcessException("ORU Batch results batch creation failed during results processing... ",resultsCreateException);
		}
		return processResponseObj;
	}

	/**
	 * Processing logic for QVR_Q17 message based on the trigger event.
	 * It delegates the process to results work-flow handler creating a new instance for each request.
	 * @param requestMsg
	 * @param oruBatchResponseMessageType
	 * @return
	 * @throws ResultsProcessException
	 * @throws ResultsCreationException
	 */
	private Object processQVRQ17Request(QVRQ17CONTENT requestMsg, String oruBatchResponseMessageType) throws ResultsProcessException, ResultsCreationException
	{
		logger.info("Processing Q17 Results Query message...");
		Object responseMsgObj = null;
		try
		{
			String triggerEvent = MessageUtils.getTriggerEvent(requestMsg);
			if (triggerEvent != null && triggerEvent.equals("Q17"))
			{
				//Get the Result work-flow handler instance from the bean factory for each request and invoke process event..
				ResultsWorkflowHandler resultWorkflowHandler = (ResultsWorkflowHandler) this.beanFactory.getBean("resultWorkflowHandler");
				resultWorkflowHandler.setError(errorStack);
				responseMsgObj = resultWorkflowHandler.processQ17Event(requestMsg,oruBatchResponseMessageType);
			}
		}
		catch (ResultsProcessException resultsProcessException) {
			logger.error("Processing of QVR_Q17 request failed.. ", resultsProcessException);
			throw resultsProcessException;
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("Creation of ORU Batch response failed.. ", resultsCreateException);
			throw resultsCreateException;
		}
		catch (Exception ex) {
			logger.error("Exception occurred in Result processor...", ex);
			throw new ResultsProcessException("Exception occurred in Result processor...", ex);
		}
		return responseMsgObj;
	}

	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@SuppressWarnings("unused")
	private AckTypeEnum ackType;
	@Override
	public void setAckType(AckTypeEnum ackType) {
		this.ackType = ackType;
	}

	private Error errorStack = null;
	@Override
	public void setErrorObject(Error error) {
		this.errorStack = error;
	}
}


