package com.bosch.tmp.integration.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.bosch.tmp.integration.process.results.PatientResultsProcessor;
import com.bosch.tmp.integration.validation.CustomerId;

/**
 *
 * @author gtk1pal
 */
public class ProcessorFactory implements BeanFactoryAware
{
	public Processor getProcessor(CustomerId customerId, String messageType)
	{
		Processor processor = null;
		switch (customerId) {
		case SA:
			if (messageType != null && messageType.equalsIgnoreCase("QVR_Q17")) {
				processor = (PatientResultsProcessor) this.beanFactory.getBean("resultProcessor");
			} else {
				processor = (DefaultProcessor) this.beanFactory.getBean("defaultProcessor");
			}
			break;
		default:
			if (messageType != null && messageType.equalsIgnoreCase("QVR_Q17")) {
				processor = (PatientResultsProcessor) this.beanFactory.getBean("resultProcessor");
			} else {
				processor = (DefaultProcessor) this.beanFactory.getBean("defaultProcessor");
			}
			break;
		}

		return processor;
	}

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
