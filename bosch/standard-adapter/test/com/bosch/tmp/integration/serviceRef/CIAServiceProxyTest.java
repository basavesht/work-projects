package com.bosch.tmp.integration.serviceRef;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bosch.th.cia.CiaPortType;
import com.bosch.th.cia.FindResults;
import com.bosch.th.cia.FindResultsReply;
import com.bosch.th.cia.InvalidFilterFault;
import com.bosch.th.cia.InvalidNextChunkFault;
import com.bosch.th.cia.UserNotAuthorizedFault;
import com.bosch.tmp.cl.results.ResultId;
import com.bosch.tmp.cl.results.ResultsType;
import com.bosch.tmp.integration.process.results.ResultsProcessException;
import com.bosch.tmp.integration.serviceReference.cia.CIAServiceProxy;
import com.bosch.tmp.integration.util.Error;

/**
 * @author BEA2KOR
 *
 */
public class CIAServiceProxyTest 
{	 
	private CiaPortType ciaServiceMock;
	private CIAServiceProxy ciaServiceProxy;

	public CIAServiceProxyTest(){

	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ciaServiceMock = createMock(CiaPortType.class);
		ciaServiceProxy = new CIAServiceProxy();
		ciaServiceProxy.setCiaService(ciaServiceMock);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Test for CIA find results...
	 * @throws InvalidFilterFault
	 * @throws InvalidNextChunkFault
	 * @throws UserNotAuthorizedFault
	 * @throws ResultsProcessException
	 */
	@Test
	public void testFindResults() throws InvalidFilterFault, InvalidNextChunkFault, UserNotAuthorizedFault, ResultsProcessException
	{
		FindResultsReply findResultsReplyMock = new FindResultsReply();

		/* = Setup our mock object with the expected values */
		expect(ciaServiceMock.findResults((FindResults) anyObject())).andReturn(findResultsReplyMock).times(1);
		replay(ciaServiceMock);

		/* = Now start testing our Find Results Operation */
		List<ResultId> resultIds = new ArrayList<ResultId>();
		ResultId resultId = new ResultId();
		resultId.setId(15425);
		resultId.setType(ResultsType.SESSION);
		resultIds.add(resultId);

		FindResultsReply resultsReply = ciaServiceProxy.findResults(resultIds, new Error());
		assertNotNull(resultsReply);

		//Verify mocks
		verify(ciaServiceMock);
	}
	
	/**
	 * Test for CIA find results...
	 * @throws InvalidFilterFault
	 * @throws InvalidNextChunkFault
	 * @throws UserNotAuthorizedFault
	 * @throws ResultsProcessException
	 */
	@Test
	public void testFindNextChunkResults() throws InvalidFilterFault, InvalidNextChunkFault, UserNotAuthorizedFault, ResultsProcessException
	{
		FindResultsReply findResultsReplyMock = new FindResultsReply();

		/* = Setup our mock object with the expected values */
		expect(ciaServiceMock.findResults((FindResults) anyObject())).andReturn(findResultsReplyMock).times(1);
		replay(ciaServiceMock);

		/* = Now start testing our Find Results (next chunks) Operation */
		List<ResultId> resultIds = new ArrayList<ResultId>();
		ResultId resultId = new ResultId();
		resultId.setId(15426);
		resultId.setType(ResultsType.SESSION);
		resultIds.add(resultId);

		FindResultsReply resultsReply = ciaServiceProxy.findNextChunkResults(resultIds, 15425L, new Error());
		assertNotNull(resultsReply);

		//Verify mocks
		verify(ciaServiceMock);
	}
}
