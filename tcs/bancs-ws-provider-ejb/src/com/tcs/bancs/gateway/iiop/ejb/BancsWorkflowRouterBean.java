package com.tcs.bancs.gateway.iiop.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import com.tcs.bancs.config.executor.WorkflowExecutorConfig;
import com.tcs.bancs.helpers.ejb.BancsWorkflowBeanHelper;
import com.tcs.bancs.helpers.ejb.BancsWorkflowRouterBeanUtil;
import com.tcs.bancs.helpers.ejb.BancsWorkflowRouterException;
import com.tcs.bancs.helpers.request.InvalidRequestException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.interceptors.logger.BancsWorkflowRouterLogger;

@Stateless(name = "BancsWorkflowRouter")
@Interceptors(BancsWorkflowRouterLogger.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class BancsWorkflowRouterBean implements BancsWorkflowRouter
{
	private transient Connection connection = null;
	private WorkflowExecutorConfig config = null;

	@Resource(name="jdbc/MMDatasource",type = javax.sql.DataSource.class)
	private DataSource dataSource ;

	@Resource
	private UserTransaction userTransaction;

	@Resource(name="workflow-executor", type = java.lang.String.class)
	private String configFileName ;

	public BancsWorkflowRouterBean(){

	}

	@PostConstruct
	public void initialize() {
		try {
			config = BancsWorkflowRouterBeanUtil.init(configFileName);
		} catch (ConfigXMLParsingException e) {
			throw new EJBException(e);
		}
	}

	public Map<String, Object> process(Map<String, Object> requestParams) throws BancsWorkflowRouterException 
	{
		Map<String, Object> responseMap = null;
		try 
		{
			BancsWorkflowRouterBeanUtil.checkConfig(config,configFileName);
			BancsWorkflowRouterBeanUtil.validateRequest(requestParams);
			try {
				//Instantiate the User Transaction and Connection instance...
				userTransaction.begin();

				//Connection settings..
				connection = dataSource.getConnection();
				connection.setAutoCommit(false);

				//Execute Workflow..
				BancsWorkflowBeanHelper workflowExecutor = new BancsWorkflowBeanHelper();
				workflowExecutor.setConnection(connection);
				responseMap = workflowExecutor.executeWorkflow(config, requestParams);

				//Commit...
				userTransaction.commit();
			} catch (BancsWorkflowRouterException e){
				userTransaction.setRollbackOnly();
				throw new BancsWorkflowRouterException("Exception occurred during routing through EJB",e);
			}
		} catch (InvalidRequestException e) {
			throw new BancsWorkflowRouterException("Input Map request data is not valid",e);
		} catch (ConfigXMLParsingException e) {
			throw new BancsWorkflowRouterException("Workflow executor config could not be created",e);
		} catch (Exception e) {
			throw new BancsWorkflowRouterException("Exception occured during service invocation",e);
		} finally {
			try {
				if(connection!=null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return responseMap;
	}

	@PreDestroy
	public void cleanup() 
	{
		try {
			if(connection!=null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}
