package com.bosch.tmp.integration.results;

import static org.easymock.EasyMock.anyObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.easymock.EasyMock;
import org.hl7.v2xml.NTECONTENT;
import org.hl7.v2xml.ORUR01BATCHCONTENT;
import org.hl7.v2xml.ORUR01CONTENT;
import org.hl7.v2xml.ORUR01OBSERVATIONCONTENT;
import org.hl7.v2xml.ORUR01ORDEROBSERVATIONCONTENT;
import org.hl7.v2xml.ORUR01PATIENTRESULTCONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.ORUR30CONTENT;
import org.hl7.v2xml.ORUR30ORDERCONTENT;
import org.hl7.v2xml.ObjectFactory;
import org.hl7.v2xml.PID3CONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.junit.Before;
import org.junit.Test;

import com.bosch.th.cia.FindNotesReply;
import com.bosch.th.cia.GetExternalOrganizationsReply;
import com.bosch.tmp.cl.task.EqualsFilterType;
import com.bosch.tmp.integration.dao.NotesDao;
import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.process.results.ResultsWorkflowHandler;
import com.bosch.tmp.integration.serviceReference.cia.CIAService;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.ORUBatchResponseMessageType;
import com.bosch.tmp.integration.util.ResultsTestDataUtil;

public class NotesWorkflowTest 
{
	//Test Data file locations..
	private static final String TEST_INPUT_QVR_Q17 = "test/data/results/notes/QVR_Q17.xml";
	private static final String TEST_OUTPUT_FIND_NOTES_REPLY = "test/data/results/notes/FindNotes.xml";
	private static final String TEST_OUTPUT_EXT_ORGANIZATION_REPLY = "test/data/organization/ExternalOrganizations.xml";

	//Instance Objects.
	private String oruBatchResponseMessageType;
	private QVRQ17CONTENT requestMsg;
	private FindNotesReply findNotesReply;
	private GetExternalOrganizationsReply getExternalOrganizationReply;
	private List<Notes> unackedNotesIdList;

	//Mock Objects.
	private CIAService ciaService;
	private NotesDao notesDao;
	private ResultsWorkflowHandler resultsBean;

	@Before
	public void setUp() throws Exception
	{
		/* = Set the request message and find results reply. */
		requestMsg = loadRequestData(TEST_INPUT_QVR_Q17);
		findNotesReply = (FindNotesReply) loadMockServiceReply(TEST_OUTPUT_FIND_NOTES_REPLY);
		getExternalOrganizationReply = (GetExternalOrganizationsReply) loadMockServiceReply(TEST_OUTPUT_EXT_ORGANIZATION_REPLY);
		unackedNotesIdList = ResultsTestDataUtil.loadUnackedNotes();

		//Mock CIAService and Data access object classes.
		ciaService = EasyMock.createStrictMock(CIAService.class);
		notesDao = EasyMock.createStrictMock(NotesDao.class);

		//Create a new ResultWorkflowHandler setting all the mock objects.
		resultsBean = new ResultsWorkflowHandler();
		resultsBean.setCiaServiceProxy(ciaService);
		resultsBean.setNotesDao(notesDao);
	}

	/**
	 * Process QVR_Q17 request for ORU_R01 response type (Only Notes)..
	 * @throws Exception
	 */
	@Test
	public void testProcessQ17Event_R30() throws Exception
	{
		try
		{
			/* = Set the ORU Batch response message type. */
			oruBatchResponseMessageType =  ORUBatchResponseMessageType.ORU_R30.getBatchMessageType();

			/* = Setup CIAServiceProxy mock object with the expected values for updateServiceWithUserDetails/getExternalOrganizations/findResults method.*/
			ciaService.updateServiceWithUserDetails((Object) anyObject(),(Error) anyObject());
			EasyMock.expect(ciaService.getExternalOrganizations((String) anyObject(),(String) anyObject(),(Error) anyObject())).andReturn(getExternalOrganizationReply.getExternalOrganization());
			EasyMock.expect(ciaService.findNotes((List<EqualsFilterType>) anyObject(),(Error) anyObject())).andReturn(findNotesReply);
			EasyMock.replay(ciaService);

			/* = Setup ResultsDao mock object with the expected values for findNotesByOrganizationId method.*/
			EasyMock.expect(notesDao.findNotesByOrganizationId((String) anyObject(),(Boolean) anyObject())).andReturn(unackedNotesIdList);
			EasyMock.replay(notesDao);

			/* = Execute the real service */
			resultsBean.setError(new Error()); //Set the error stack ....
			Object responseMsgObj = resultsBean.processQ17Event(requestMsg,oruBatchResponseMessageType);

			/*= Assert ORU Batch */
			assertNotNull(responseMsgObj);
			if(responseMsgObj!=null && responseMsgObj instanceof ORUR30BATCHCONTENT)
			{
				ORUR30BATCHCONTENT oruR30ResponseBatch = (ORUR30BATCHCONTENT)responseMsgObj;

				//Assert ORU Batch...
				assertNotNull(oruR30ResponseBatch.getBHS());
				assertNotNull(oruR30ResponseBatch.getBTS());
				assertNotNull(oruR30ResponseBatch.getORUR30());

				//Assert ORU R30...
				assertEquals(oruR30ResponseBatch.getORUR30().size(),8);
				for(ORUR30CONTENT oruR30 : oruR30ResponseBatch.getORUR30())
				{
					//Assert MSH..
					assertNotNull(oruR30.getMSH());

					//Assert PID..
					assertNotNull(oruR30.getORUR30PATIENTINFORMATION());
					assertNotNull(oruR30.getORUR30PATIENTINFORMATION().getPID());
					assertNotNull(oruR30.getORUR30PATIENTINFORMATION().getPID().getPID3());

					//Assert SSN and PS..
					for(PID3CONTENT pid3Content : oruR30.getORUR30PATIENTINFORMATION().getPID().getPID3()) {
						assertNotNull(pid3Content.getCX1());
						assertNotNull(pid3Content.getCX5());
					}

					//Assert OBSERVATION..
					assertNotNull(oruR30.getORUR30ORDER());
					assertFalse(oruR30.getORUR30ORDER().isEmpty());

					//Assert ORDER and Survey Note..
					for(ORUR30ORDERCONTENT order : oruR30.getORUR30ORDER()) {
						assertNotNull(order);
						assertNotNull(order.getOBR());
						assertNotNull(order.getORC());

						//Assert OBX..
						for(NTECONTENT nte : order.getNTE()) {
							assertNotNull(nte);
							assertNotNull(nte.getNTE1().getValue()); //Note Id ..
						}
					}

					//Assert Patient Notes for each patient..
					assertFalse(oruR30.getNTE().isEmpty());
					for(NTECONTENT nte : oruR30.getNTE()) {
						assertNotNull(nte);
						assertNotNull(nte.getNTE1().getValue()); //Patient note id..
					}
				}
			}

			/* = Verify the mock */
			EasyMock.verify(ciaService);
			EasyMock.verify(notesDao);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Process QVR_Q17 request for ORU_R01 response type (Only Notes)..
	 * @throws Exception
	 */
	@Test
	public void testProcessQ17Event_R01() throws Exception
	{
		try
		{
			/* = Set the ORU Batch response message type. */
			oruBatchResponseMessageType =  ORUBatchResponseMessageType.ORU_R01.getBatchMessageType();

			/* = Setup CIAServiceProxy mock object with the expected values for updateServiceWithUserDetails/getExternalOrganizations/findResults method.*/
			ciaService.updateServiceWithUserDetails((Object) anyObject(),(Error) anyObject());
			EasyMock.expect(ciaService.getExternalOrganizations((String) anyObject(),(String) anyObject(),(Error) anyObject())).andReturn(getExternalOrganizationReply.getExternalOrganization());
			EasyMock.expect(ciaService.findNotes((List<EqualsFilterType>) anyObject(),(Error) anyObject())).andReturn(findNotesReply);
			EasyMock.replay(ciaService);

			/* = Setup ResultsDao mock object with the expected values for findNotesByOrganizationId method.*/
			EasyMock.expect(notesDao.findNotesByOrganizationId((String) anyObject(),(Boolean) anyObject())).andReturn(unackedNotesIdList);
			EasyMock.replay(notesDao);

			/* = Execute the real service */
			resultsBean.setError(new Error()); //Set the error stack ....
			Object responseMsgObj = resultsBean.processQ17Event(requestMsg,oruBatchResponseMessageType);

			/*= Assert ORU Batch */
			assertNotNull(responseMsgObj);
			if(responseMsgObj!=null && responseMsgObj instanceof ORUR01BATCHCONTENT)
			{
				ORUR01BATCHCONTENT oruR01ResponseBatch = (ORUR01BATCHCONTENT)responseMsgObj;

				//Assert ORU Batch...
				assertNotNull(oruR01ResponseBatch.getBHS());
				assertNotNull(oruR01ResponseBatch.getBTS());
				assertNotNull(oruR01ResponseBatch.getORUR01());

				//Assert ORU R01...
				assertEquals(oruR01ResponseBatch.getORUR01().size(),8);
				for(ORUR01CONTENT oruR01 : oruR01ResponseBatch.getORUR01())
				{
					//Assert MSH..
					assertNotNull(oruR01.getMSH());

					//Assert Patient Result..
					assertNotNull(oruR01.getORUR01PATIENTRESULT());
					for(ORUR01PATIENTRESULTCONTENT resultContent : oruR01.getORUR01PATIENTRESULT())
					{
						assertNotNull(resultContent);
						assertNotNull(resultContent.getORUR01PATIENT());

						//Assert SSN and PS..
						for(PID3CONTENT pid3Content : resultContent.getORUR01PATIENT().getPID().getPID3()) {
							assertNotNull(pid3Content.getCX1());
							assertNotNull(pid3Content.getCX5());
						}

						//Assert OBSERVATION..
						assertNotNull(resultContent.getORUR01ORDEROBSERVATION());
						assertFalse(resultContent.getORUR01ORDEROBSERVATION().isEmpty());

						//Assert ORDER..
						for(ORUR01ORDEROBSERVATIONCONTENT order : resultContent.getORUR01ORDEROBSERVATION()) 
						{
							assertNotNull(order);
							assertNotNull(order.getOBR());
							assertNotNull(order.getORC());
							assertFalse(!order.getORUR01OBSERVATION().isEmpty());

							//Assert Survey Notes within each survey..
							for(ORUR01OBSERVATIONCONTENT result : order.getORUR01OBSERVATION()) {
								assertNotNull(result);
								assertNotNull(result.getNTE());
								assertFalse(result.getNTE().isEmpty());
								for(NTECONTENT nte : result.getNTE()){
									assertNotNull(nte);
									assertNotNull(nte.getNTE1().getValue()); //Survey note id..
								}
							}
						}

						//Assert Patient Notes for each patient..
						assertFalse(resultContent.getORUR01PATIENT().getNTE().isEmpty());
						for(NTECONTENT nte : resultContent.getORUR01PATIENT().getNTE()) {
							assertNotNull(nte);
							assertNotNull(nte.getNTE1().getValue()); //Patient note id..
						}
					}
				}
			}

			/* = Verify the mock */
			EasyMock.verify(ciaService);
			EasyMock.verify(notesDao);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Load the input request (QVR_Q17) and un-marshal the same..
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private QVRQ17CONTENT loadRequestData(String fileName) throws Exception {
		ObjectFactory objFactory = new ObjectFactory();
		JAXBContext context = JAXBContext.newInstance(objFactory.createQVRQ17CONTENT().getClass().getPackage().getName());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		JAXBElement<QVRQ17CONTENT> element = (JAXBElement<QVRQ17CONTENT>)unmarshaller.unmarshal(new File(fileName));
		return element.getValue();
	}

	/**
	 * Load the service response for different CIA service operations and un-marshal the same..
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private Object loadMockServiceReply(String fileName) throws Exception {
		JAXBContext context = JAXBContext.newInstance("com.bosch.th.cia");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Object element = (Object)unmarshaller.unmarshal(new File(fileName));
		return element;
	}
}
