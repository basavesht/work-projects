package com.bosch.pat.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bosch.pat.dao.AutoSystemReviewTaskDao;
import com.bosch.pat.entity.BatchJobExecution;
import com.bosch.pat.model.AutoSystemReviewParams;
import com.bosch.pat.model.AutoUserLockParams;
import com.bosch.pat.util.TaskConstants;

@Repository
public class AutoSystemReviewTaskDaoImpl implements AutoSystemReviewTaskDao{
	
	public static final Logger LOGGER = Logger.getLogger(AutoSystemReviewTaskDaoImpl.class);
	@Autowired
	private DataSource dataSource;
	
	@Override
	public void systemReviewTask(AutoSystemReviewParams params) {
		if (dataSource!=null){
			if (params==null || params.getDefReviewDays()<=0 || params.getReviewTimeUtcs()<=0){
				LOGGER.error("systemReviewTask() received invalid input :"+params);
				params = new AutoSystemReviewParams();
				params.setReturnDesc("Invalid Input");
				return;
			}
			LOGGER.info("systemReviewTask() received input :"+params);
			try {
				Connection connection = dataSource.getConnection();
				if (connection==null){
					LOGGER.error("systemReviewTask() failed to get connection :");
					params.setReturnDesc("Failed to get Connection");
					return;
				}
				CallableStatement callableStatement = null;
				String sql = "{call SP_SYS_SESSION_ARCHIVE_TASK(?,?,?,?,?,?,?,?)}";
		 
				try {
					callableStatement = connection.prepareCall(sql);
					callableStatement.setLong("V_I_PREV_EXEC_DATE_UTCS", 0);
					callableStatement.setLong("V_I_REVIEW_TIME_UTCS", params.getReviewTimeUtcs());
					callableStatement.setLong("V_I_DEF_REVIEW_DAYS", params.getDefReviewDays());
					callableStatement.setString("V_I_RESERVE1",null);
					callableStatement.setString("V_I_RESERVE2", null);
					callableStatement.registerOutParameter("V_O_SESSION_COUNT", java.sql.Types.INTEGER);
					callableStatement.registerOutParameter("V_O_RET", java.sql.Types.INTEGER);
					callableStatement.registerOutParameter("V_O_RET_STATUS", java.sql.Types.VARCHAR);
					callableStatement.executeUpdate();
					params.setSessionCount(callableStatement.getInt("V_O_SESSION_COUNT"));
					params.setReturnCode(callableStatement.getInt("V_O_RET"));
					params.setReturnDesc(callableStatement.getString("V_O_RET_STATUS"));
					LOGGER.info("systemReviewTask() Status code:"+params.getReturnCode()+" message:"+params.getReturnDesc() +" No of sessions Reviewed:"+params.getSessionCount());
				} catch (SQLException e) {
					LOGGER.error("systemReviewTask() Exception raised while executing stored procedure"
							+ e.getMessage());
					e.printStackTrace();
		 
				} finally {
		 
					if (callableStatement != null) {
						try {
							callableStatement.close();
						} catch (SQLException e) {
							LOGGER.error("systemReviewTask() exception raised while closing callable statement");
							e.printStackTrace();
						}
					}
		 
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							LOGGER.error("systemReviewTask() exception raised while closing connection");
							e.printStackTrace();
						}
					}
		 
				}
				
			} catch (SQLException e) {
				LOGGER.error("systemReviewTask() exception raised while executing stored procedure");
				e.printStackTrace();
			}
		}else {
			LOGGER.error("systemReviewTask() Failed to get Data source object");
			if (params==null){
				params = new AutoSystemReviewParams();
			}
			params.setReturnDesc("Failed to get Datasource");
		}
		
	}

	@Override
	public BatchJobExecution mostRecentSuccessJobExec(String jobName) {
		if (jobName!=null && !jobName.trim().equals("")){
			
			if (dataSource!=null){
				Connection connection = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					connection=  dataSource.getConnection();
					if (connection==null){
						LOGGER.error("mostRecentSuccessJobExec() failed to get connection :");
						return  null;
					}
					
					String sql = "select * from (SELECT  BJE.JOB_EXECUTION_ID, BJE.VERSION, BJE.JOB_INSTANCE_ID, BJE.CREATE_TIME, BJE.START_TIME, BJE.END_TIME, BJE.STATUS, BJE.EXIT_CODE, BJE.EXIT_MESSAGE, " +
							"BJE.LAST_UPDATED FROM BATCH_JOB_EXECUTION BJE INNER JOIN BATCH_JOB_INSTANCE BJI ON BJE.JOB_INSTANCE_ID = BJI.JOB_INSTANCE_ID AND BJI.JOB_NAME='"+jobName+"' WHERE BJE.STATUS='"+TaskConstants.TASK_COMPLETED_ACTION+"' " +
							"ORDER BY BJE.START_TIME DESC) WHERE ROWNUM=1";
					try {
						stmt = connection.createStatement();
						rs = stmt.executeQuery(sql);
						LOGGER.info("mostRecentSuccessJobExec() SQL Query response recieved :"
								+ rs);
						if (rs != null && rs.next()) {
							BatchJobExecution result = new BatchJobExecution();
							result.setJobExecutionId(rs.getLong("JOB_EXECUTION_ID"));
							result.setVersion(rs.getInt("VERSION"));
							result.setStartTime(rs.getTimestamp("START_TIME"));
							result.setEndTime(rs.getTimestamp("END_TIME"));
							result.setLastUpdated(rs.getTimestamp("LAST_UPDATED"));
							result.setStatus(rs.getString("STATUS"));
							result.setExitCode(rs.getString("EXIT_CODE"));
							result.setExitMessage(rs.getString("EXIT_MESSAGE"));
							LOGGER.info("mostRecentSuccessJobExec() returning "+ result);
							return result;
							
						}
					} catch (SQLException e) {
						LOGGER.error("mostRecentSuccessJobExec() Exception raised while executing "
								+ e.getMessage());
						e.printStackTrace();
			 
					} finally {
						if (rs != null) {
							try {
								rs.close();
							} catch (SQLException e) {
								LOGGER.error("mostRecentSuccessJobExec() exception raised while closing  statement");
								e.printStackTrace();
							}
						}
						
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException e) {
								LOGGER.error("mostRecentSuccessJobExec() exception raised while closing  statement");
								e.printStackTrace();
							}
						}
			 
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException e) {
								LOGGER.error("mostRecentSuccessJobExec() exception raised while closing  statement");
								e.printStackTrace();
							}
						}
			 
						if (connection != null) {
							try {
								connection.close();
							} catch (SQLException e) {
								LOGGER.error("mostRecentSuccessJobExec() exception raised while closing connection");
								e.printStackTrace();
							}
						}
			 
					}
					
				} catch (SQLException e) {
					LOGGER.error("mostRecentSuccessJobExec() exception raised while executing ");
					e.printStackTrace();
				}
			}else {
				LOGGER.error("mostRecentSuccessJobExec() Failed to get Data source object");
			}
		}
		return null;
	}


	
	@Override
	public void autoUserLockTask(AutoUserLockParams params) {
		
		if (dataSource!=null){
			if (params==null || params.getDefIdleDays()<=0 ){
				LOGGER.error("autoUserLockTask() received invalid input :"+params);
				params = new AutoUserLockParams();
				params.setReturnDesc("Invalid Input");
				return;
			}
			LOGGER.info("autoUserLockTask() received input :"+params);
			try {
				Connection connection = dataSource.getConnection();
				if (connection==null){
					LOGGER.error("autoUserLockTask() failed to get connection :");
					params.setReturnDesc("Failed to get Connection");
					return;
				}
				CallableStatement callableStatement = null;
				String sql = "{call SP_R751_IDLE_USER_LOCK_TASK(?,?,?,?,?,?,?,?)}";
		 
				try {
					callableStatement = connection.prepareCall(sql);
					callableStatement.setLong("V_I_ID", 0);
					callableStatement.setInt("V_I_ID_TYPE", 0);
					//callableStatement.setLong("V_I_PREV_EXEC_DATE_UTCS", 0);
					callableStatement.setLong("V_I_DEF_IDLE_DAYS",  params.getDefIdleDays());
					callableStatement.setString("V_I_RESERVE1",null);
					callableStatement.setString("V_I_RESERVE2", null);
					callableStatement.registerOutParameter("V_O_USER_COUNT", java.sql.Types.INTEGER);
					callableStatement.registerOutParameter("V_O_RET", java.sql.Types.INTEGER);
					callableStatement.registerOutParameter("V_O_RET_STATUS", java.sql.Types.VARCHAR);
					callableStatement.executeUpdate();
					params.setInactiveUserCount(callableStatement.getInt("V_O_USER_COUNT"));
					params.setReturnCode(callableStatement.getInt("V_O_RET"));
					params.setReturnDesc(callableStatement.getString("V_O_RET_STATUS"));
					LOGGER.info("autoUserLockTask() Status code:"+params.getReturnCode()+" message:"+params.getReturnDesc() +" No of users marked as inactive:"+params.getInactiveUserCount());
				} catch (SQLException e) {
					LOGGER.error("autoUserLockTask() Exception raised while executing stored procedure"
							+ e.getMessage());
					e.printStackTrace();
		 
				} finally {
		 
					if (callableStatement != null) {
						try {
							callableStatement.close();
						} catch (SQLException e) {
							LOGGER.error("autoUserLockTask() exception raised while closing callable statement");
							e.printStackTrace();
						}
					}
		 
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							LOGGER.error("autoUserLockTask() exception raised while closing connection");
							e.printStackTrace();
						}
					}
		 
				}
				
			} catch (SQLException e) {
				LOGGER.error("autoUserLockTask() exception raised while executing stored procedure");
				e.printStackTrace();
			}
		}else {
			LOGGER.error("autoUserLockTask() Failed to get Data source object");
			if (params==null){
				params = new AutoUserLockParams();
			}
			params.setReturnDesc("Failed to get Datasource");
		}
		
	}
}
