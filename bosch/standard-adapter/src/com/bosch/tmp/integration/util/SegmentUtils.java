package com.bosch.tmp.integration.util;

import com.bosch.th.cia.Note;
import com.bosch.th.patientrecord.Device;
import com.bosch.tmp.cl.task.HeaderType;
import com.bosch.tmp.cl.task.NotesPayloadType;
import com.bosch.tmp.cl.task.SurveyNotesPayloadType;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import com.bosch.tmp.integration.validation.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.BHS11CONTENT;
import org.hl7.v2xml.BHS12CONTENT;
import org.hl7.v2xml.BHS1CONTENT;
import org.hl7.v2xml.BHS3CONTENT;
import org.hl7.v2xml.BHS2CONTENT;
import org.hl7.v2xml.BHS4CONTENT;
import org.hl7.v2xml.BHS5CONTENT;
import org.hl7.v2xml.BHS6CONTENT;
import org.hl7.v2xml.BHS7CONTENT;
import org.hl7.v2xml.BHS9CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.BTS1CONTENT;
import org.hl7.v2xml.BTSCONTENT;
import org.hl7.v2xml.CE1CONTENT;
import org.hl7.v2xml.CE2CONTENT;
import org.hl7.v2xml.CE3CONTENT;
import org.hl7.v2xml.CX1CONTENT;
import org.hl7.v2xml.CX5CONTENT;
import org.hl7.v2xml.EI1CONTENT;
import org.hl7.v2xml.EI2CONTENT;
import org.hl7.v2xml.EI3CONTENT;
import org.hl7.v2xml.FN1CONTENT;
import org.hl7.v2xml.HD1CONTENT;
import org.hl7.v2xml.MSG1CONTENT;
import org.hl7.v2xml.MSG2CONTENT;
import org.hl7.v2xml.MSG3CONTENT;
import org.hl7.v2xml.MSH10CONTENT;
import org.hl7.v2xml.MSH11CONTENT;
import org.hl7.v2xml.MSH12CONTENT;
import org.hl7.v2xml.MSH16CONTENT;
import org.hl7.v2xml.MSH1CONTENT;
import org.hl7.v2xml.MSH2CONTENT;
import org.hl7.v2xml.MSH3CONTENT;
import org.hl7.v2xml.MSH4CONTENT;
import org.hl7.v2xml.MSH5CONTENT;
import org.hl7.v2xml.MSH6CONTENT;
import org.hl7.v2xml.MSH7CONTENT;
import org.hl7.v2xml.MSH9CONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.NTE11CONTENT;
import org.hl7.v2xml.NTE1CONTENT;
import org.hl7.v2xml.NTE2CONTENT;
import org.hl7.v2xml.NTE3CONTENT;
import org.hl7.v2xml.NTE4CONTENT;
import org.hl7.v2xml.NTE5CONTENT;
import org.hl7.v2xml.NTE6CONTENT;
import org.hl7.v2xml.NTECONTENT;
import org.hl7.v2xml.OBR7CONTENT;
import org.hl7.v2xml.OBR8CONTENT;
import org.hl7.v2xml.OBRCONTENT;
import org.hl7.v2xml.OBX14CONTENT;
import org.hl7.v2xml.OBX17CONTENT;
import org.hl7.v2xml.OBX18CONTENT;
import org.hl7.v2xml.OBX1CONTENT;
import org.hl7.v2xml.OBX21CONTENT;
import org.hl7.v2xml.OBX3CONTENT;
import org.hl7.v2xml.OBX5CONTENT;
import org.hl7.v2xml.OBX6CONTENT;
import org.hl7.v2xml.OBXCONTENT;
import org.hl7.v2xml.ORC2CONTENT;
import org.hl7.v2xml.ORCCONTENT;
import org.hl7.v2xml.PID11CONTENT;
import org.hl7.v2xml.PID13CONTENT;
import org.hl7.v2xml.PID3CONTENT;
import org.hl7.v2xml.PID5CONTENT;
import org.hl7.v2xml.PID7CONTENT;
import org.hl7.v2xml.PID8CONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.PT1CONTENT;
import org.hl7.v2xml.SAD1CONTENT;
import org.hl7.v2xml.TS1CONTENT;
import org.hl7.v2xml.VID1CONTENT;
import org.hl7.v2xml.XAD1CONTENT;
import org.hl7.v2xml.XAD2CONTENT;
import org.hl7.v2xml.XAD3CONTENT;
import org.hl7.v2xml.XAD4CONTENT;
import org.hl7.v2xml.XAD5CONTENT;
import org.hl7.v2xml.XAD6CONTENT;
import org.hl7.v2xml.XCN1CONTENT;
import org.hl7.v2xml.XCN2CONTENT;
import org.hl7.v2xml.XPN1CONTENT;
import org.hl7.v2xml.XPN2CONTENT;
import org.hl7.v2xml.XPN3CONTENT;
import org.hl7.v2xml.XTN1CONTENT;

/**
 * ORU Segment construction utility class...
 * @author tis1pal
 */
public class SegmentUtils
{

	public static final Logger logger = LoggerFactory.getLogger(SegmentUtils.class);

	private SegmentUtils(){

	}

	/**
	 * This method builds an MSH segment.
	 * @param incomingMsh The MSH of the incoming message.
	 * @return MSHCONTENT The new MSH segment.
	 */
	public static MSHCONTENT buildMSH(MSHCONTENT incomingMsh, String messageControlNumber, String outMessageType)
	{
		MSHCONTENT messageContent = new MSHCONTENT();

		if (incomingMsh == null){
			return messageContent;
		}

		//MSH Message type....
		String messageType = incomingMsh.getMSH9().getMSG3().getValue();

		//Field separator....
		MSH1CONTENT msh1 = new MSH1CONTENT();
		Object msh1Config = ConfigurationLoader.getFieldConfigurations(messageType, "MSH", "MSH1");
		if (msh1Config != null && msh1Config instanceof String) {
			msh1.setValue(msh1Config.toString());
		}
		messageContent.setMSH1(msh1);
		logger.debug("MSH1 Configuration for " + messageType + " Message: " + msh1Config.toString());

		//Encoding characters..
		MSH2CONTENT msh2 = new MSH2CONTENT();
		Object msh2Config = ConfigurationLoader.getFieldConfigurations(messageType, "MSH", "MSH2");
		if (msh2Config != null && msh2Config instanceof String){
			msh2.setValue(msh2Config.toString());
		}
		messageContent.setMSH2(msh2);
		logger.debug("MSH2 Configuration for " + messageType + " Message: " + msh2Config.toString());

		//Sending application id...
		MSH3CONTENT msh3 = new MSH3CONTENT();
		HD1CONTENT msh3Hd1 = new HD1CONTENT();
		MSH5CONTENT msh5FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH5();
		if (msh5FromIncoming != null){
			msh3Hd1.setValue(msh5FromIncoming.getHD1() == null ? null : msh5FromIncoming.getHD1().getValue());
		}
		if (msh3Hd1.getValue() == null || msh3Hd1.getValue().length() == 0){
			Object msh3Hd1Config = ConfigurationLoader.getDataTypeConfigurations(messageType, "MSH", "MSH3", "HD1");
			if (msh3Hd1Config != null && msh3Hd1Config instanceof String){
				msh3Hd1.setValue(msh3Hd1Config.toString());
			}
		}
		msh3.setHD1(msh3Hd1);
		messageContent.setMSH3(msh3);
		logger.debug("MSH3HD1 Configuration for " + messageType + " Message: " + msh3Hd1.getValue());

		//Sending facility representing the account token.
		MSH4CONTENT msh4 = new MSH4CONTENT();
		HD1CONTENT msh4Hd1 = new HD1CONTENT();
		MSH6CONTENT msh6FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH6();
		if (msh6FromIncoming != null){
			msh4Hd1.setValue(msh6FromIncoming.getHD1() == null ? null : msh6FromIncoming.getHD1().getValue());
		}
		if (msh4Hd1.getValue() == null || msh4Hd1.getValue().length() == 0){
			Object msh4Hd1Config = ConfigurationLoader.getDataTypeConfigurations(messageType, "MSH", "MSH4", "HD1");
			if (msh4Hd1Config != null && msh4Hd1Config instanceof String){
				msh4Hd1.setValue(msh4Hd1Config.toString());
			}
		}
		msh4.setHD1(msh4Hd1);
		messageContent.setMSH4(msh4);
		logger.debug("MSH4HD1 Configuration for " + messageType + " Message: " + msh4Hd1.getValue());

		//Receiving application Id...
		MSH5CONTENT msh5 = new MSH5CONTENT();
		HD1CONTENT msh5Hd1 = new HD1CONTENT();
		MSH3CONTENT msh3FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH3();
		if (msh3FromIncoming != null){
			msh5Hd1.setValue(msh3FromIncoming.getHD1() == null ? null : msh3FromIncoming.getHD1().getValue());
		}
		if (msh5Hd1.getValue() == null || msh5Hd1.getValue().length() == 0){
			Object msh5Hd1Config = ConfigurationLoader.getDataTypeConfigurations(messageType, "MSH", "MSH5", "HD1");
			if (msh5Hd1Config != null && msh5Hd1Config instanceof String){
				msh5Hd1.setValue(msh5Hd1Config.toString());
			}
		}
		msh5.setHD1(msh5Hd1);
		messageContent.setMSH5(msh5);
		logger.debug("MSH5HD1 Configuration for " + messageType + " Message: " + msh5Hd1.getValue());

		//Recieving facility...
		MSH6CONTENT msh6 = new MSH6CONTENT();
		HD1CONTENT msh6Hd1 = new HD1CONTENT();
		MSH4CONTENT msh4FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH4();
		if (msh4FromIncoming != null){
			msh6Hd1.setValue(msh4FromIncoming.getHD1() == null ? null : msh4FromIncoming.getHD1().getValue());
		}
		if (msh6Hd1.getValue() == null || msh6Hd1.getValue().length() == 0){
			Object msh6Hd1Config = ConfigurationLoader.getDataTypeConfigurations(messageType, "MSH", "MSH6", "HD1");
			if (msh6Hd1Config != null && msh6Hd1Config instanceof String){
				msh6Hd1.setValue(msh6Hd1Config.toString());
			}
		}
		msh6.setHD1(msh6Hd1);
		messageContent.setMSH6(msh6);
		logger.debug("MSH6HD1 Configuration for " + messageType + " Message: " + msh6Hd1.getValue());

		// time of message
		MSH7CONTENT msh7 = new MSH7CONTENT();
		TS1CONTENT msh7Ts1 = Utils.createCurrentTimestamp(new Date());
		msh7.setTS1(msh7Ts1);
		messageContent.setMSH7(msh7);

		//Message type ....
		MSH9CONTENT msh9 = new MSH9CONTENT();
		MSG1CONTENT msh9Msg1 = new MSG1CONTENT();
		MSG2CONTENT msh9Msg2 = new MSG2CONTENT();
		MSG3CONTENT msh9Msg3 = new MSG3CONTENT();
		Object msh9Msg1Config = ConfigurationLoader.getDataTypeConfigurations(outMessageType, "MSH", "MSH9", "MSG1");
		if (msh9Msg1Config != null && msh9Msg1Config instanceof String){
			msh9Msg1.setValue(msh9Msg1Config.toString());
			logger.debug("MSH9MSG1 Configuration for " + outMessageType + " Message: " + msh9Msg1Config.toString());
		}
		Object msh9Msg2Config = ConfigurationLoader.getDataTypeConfigurations(outMessageType, "MSH", "MSH9", "MSG2");
		if (msh9Msg2Config != null && msh9Msg2Config instanceof String){
			msh9Msg2.setValue(msh9Msg2Config.toString());
			logger.debug("MSH9MSG2 Configuration for " + outMessageType + " Message: " + msh9Msg2Config.toString());
		}
		Object msh9Msg3Config = ConfigurationLoader.getDataTypeConfigurations(outMessageType, "MSH", "MSH9", "MSG3");
		if (msh9Msg3Config != null && msh9Msg3Config instanceof String){
			msh9Msg3.setValue(msh9Msg3Config.toString());
			logger.debug("MSH9MSG3 Configuration for " + outMessageType + " Message: " + msh9Msg3Config.toString());
		}
		msh9.setMSG1(msh9Msg1);
		msh9.setMSG2(msh9Msg2);
		msh9.setMSG3(msh9Msg3);
		messageContent.setMSH9(msh9);

		//Message Control Number...
		MSH10CONTENT msh10 = new MSH10CONTENT();
		msh10.setValue(messageControlNumber);
		logger.debug("MSH10 Message Control number for " + messageType + " if: " + messageControlNumber);
		messageContent.setMSH10(msh10);

		//Processing Id...
		MSH11CONTENT msh11 = new MSH11CONTENT();
		PT1CONTENT msh11Pt1 = new PT1CONTENT();
		MSH11CONTENT msh11FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH11();
		if (msh11FromIncoming != null) {
			msh11Pt1.setValue(msh11FromIncoming.getPT1().getValue() == null ? null : msh11FromIncoming.getPT1().getValue());
		}
		if (msh11Pt1.getValue() == null || msh11Pt1.getValue().length() == 0){
			Object msh11Pt1Config = ConfigurationLoader.getDataTypeConfigurations(messageType, "MSH", "MSH11", "PT1");
			if (msh11Pt1Config != null && msh11Pt1Config instanceof String){
				msh11Pt1.setValue(msh11Pt1Config.toString());
				logger.debug("MSH11PT1 Configuration for " + messageType + " Message: " + msh11Pt1Config.toString());
			}
		}
		msh11.setPT1(msh11Pt1);
		messageContent.setMSH11(msh11);

		//HL7 version
		MSH12CONTENT msh12 = new MSH12CONTENT();
		VID1CONTENT vid1ForAck = new VID1CONTENT();
		MSH12CONTENT msh12FromIncoming = (incomingMsh == null) ? null : incomingMsh.getMSH12();
		if (msh12FromIncoming!=null && msh12FromIncoming.getVID1().getValue()!=null){
			vid1ForAck.setValue(msh12FromIncoming.getVID1().getValue());
			logger.debug("MSH12VID1 Configuration for ACK Message: " + vid1ForAck.getValue());
		}
		if(vid1ForAck.getValue() == null || vid1ForAck.getValue().length() == 0) {
			Object msh12Vid1Config = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH12", "VID1");
			if (msh12Vid1Config != null && msh12Vid1Config instanceof String) {
				vid1ForAck.setValue(msh12Vid1Config.toString());
				logger.debug("MSH12VID1 Configuration for ACK Message: " + msh12Vid1Config.toString());
			}
		}
		msh12.setVID1(vid1ForAck);
		messageContent.setMSH12(msh12);

		//Configuration..
		MSH16CONTENT msh16 = new MSH16CONTENT();
		Object msh16Config = ConfigurationLoader.getFieldConfigurations(outMessageType, "MSH", "MSH16");
		if (msh16Config != null && msh16Config instanceof String)
		{
			msh16.setValue(msh16Config.toString());
			logger.debug("MSH16 Configuration for " + outMessageType + " Message: " + msh16Config.toString());
		}
		messageContent.setMSH16(msh16);
		return messageContent;
	}

	/**
	 * Patient Identification data...
	 * @param patId
	 * @param ssn
	 * @param lastName
	 * @param firstName
	 * @param middleInitial
	 * @param dob
	 * @param gender
	 * @param address1
	 * @param address2
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param country
	 * @param phoneNumber
	 * @return
	 */
	public static PIDCONTENT buildPID(String patId,
			String ssn,
			String lastName,
			String firstName,
			String middleInitial,
			String dob,
			String gender,
			String address1,
			String address2,
			String city,
			String state,
			String zipCode,
			String country,
			String phoneNumber)
	{
		final PIDCONTENT patientDetails = new PIDCONTENT();

		//Patient Identifiers...
		PID3CONTENT pid3PatId = new PID3CONTENT();
		pid3PatId.setCX1(new CX1CONTENT());
		pid3PatId.getCX1().setValue(patId);
		pid3PatId.setCX5(new CX5CONTENT());
		pid3PatId.getCX5().setValue(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString()));
		patientDetails.getPID3().add(pid3PatId);

		//Social Security Number...
		PID3CONTENT pid3SSN = new PID3CONTENT();
		pid3SSN.setCX1(new CX1CONTENT());
		pid3SSN.getCX1().setValue(ssn);
		pid3SSN.setCX5(new CX5CONTENT());
		pid3SSN.getCX5().setValue(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.SSN.toString()));
		patientDetails.getPID3().add(pid3SSN);

		//Patient Name...
		PID5CONTENT pid5 = new PID5CONTENT();
		pid5.setXPN1(new XPN1CONTENT());
		pid5.getXPN1().setFN1(new FN1CONTENT());
		pid5.getXPN1().getFN1().setValue(lastName);
		pid5.setXPN2(new XPN2CONTENT());
		pid5.getXPN2().setValue(firstName);
		pid5.setXPN3(new XPN3CONTENT());
		pid5.getXPN3().setValue(middleInitial);
		patientDetails.getPID5().add(pid5);

		//Date of Birth....
		PID7CONTENT pid7 = new PID7CONTENT();
		pid7.setTS1(new TS1CONTENT());
		pid7.getTS1().setValue(dob);
		patientDetails.setPID7(pid7);

		//Gender...
		if (gender != null && gender.length() > 0) {
			PID8CONTENT pid8 = new PID8CONTENT();
			pid8.setValue(gender);
			patientDetails.setPID8(pid8);
		}

		//Patient Address..
		if (address1 != null && address1.length() > 0)
		{
			PID11CONTENT pid11 = new PID11CONTENT();

			//Address1
			pid11.setXAD1(new XAD1CONTENT());
			pid11.getXAD1().setSAD1(new SAD1CONTENT());
			pid11.getXAD1().getSAD1().setValue(address1);

			//Address2
			pid11.setXAD2(new XAD2CONTENT());
			pid11.getXAD2().setValue(address2);

			//City
			pid11.setXAD3(new XAD3CONTENT());
			pid11.getXAD3().setValue(city);

			//State
			pid11.setXAD4(new XAD4CONTENT());
			pid11.getXAD4().setValue(state);

			//Zip Code
			pid11.setXAD5(new XAD5CONTENT());
			pid11.getXAD5().setValue(zipCode);

			//Country
			pid11.setXAD6(new XAD6CONTENT());
			pid11.getXAD6().setValue(country);

			patientDetails.getPID11().add(pid11);
		}

		//Phone number
		PID13CONTENT pid13 = new PID13CONTENT();
		pid13.setXTN1(new XTN1CONTENT());
		if (phoneNumber != null && phoneNumber.length() > 0){
			pid13.getXTN1().setValue(phoneNumber);
		}
		else {
			pid13.getXTN1().setValue("");
		}
		patientDetails.getPID13().add(pid13);
		return patientDetails;
	}

	/**
	 * Observation control...details...
	 * @param orderControlNumber
	 * @param sendingFacility
	 * @return
	 */
	public static ORCCONTENT buildORC(String orderControlNumber, String sendingFacility)
	{
		ORCCONTENT observationControl = new ORCCONTENT();

		//Order Control...
		ORC2CONTENT orc2 = new ORC2CONTENT();

		//Sequence number to identify all the results belonging to a patient. This number is same for all the results belonging to a patient.
		EI1CONTENT orc2ei1 = new EI1CONTENT();
		orc2ei1.setValue(orderControlNumber);
		orc2.setEI1(orc2ei1);

		// Sending facility name/ source of the data collection
		EI2CONTENT orc2ei2 = new EI2CONTENT();
		orc2ei2.setValue(sendingFacility);
		orc2.setEI2(orc2ei2);

		observationControl.setORC2(orc2);
		return observationControl;
	}

	/**
	 * Observation request....
	 * @param responseDate
	 * @param receivedDate
	 * @return
	 */
	public static OBRCONTENT buildOBR(XMLGregorianCalendar responseDate, XMLGregorianCalendar receivedDate)
	{
		OBRCONTENT observationRequest = new OBRCONTENT();

		//Response date/time...
		OBR7CONTENT obr7 = new OBR7CONTENT();
		TS1CONTENT obr7ts1 = new TS1CONTENT();
		obr7ts1.setValue(Utils.convertXMLGregorianCalendarToString(responseDate));
		obr7.setTS1(obr7ts1);
		observationRequest.setOBR7(obr7);

		//Received date/time...
		OBR8CONTENT obr8 = new OBR8CONTENT();
		TS1CONTENT obr8ts1 = new TS1CONTENT();
		obr8ts1.setValue(Utils.convertXMLGregorianCalendarToString(receivedDate));
		obr8.setTS1(obr8ts1);
		observationRequest.setOBR8(obr8);

		return observationRequest;
	}

	/**
	 * Observation details...
	 * @param setId
	 * @param loincCode
	 * @param vitalReading
	 * @param uom
	 * @param collectionTime
	 * @param vitalCollectionMode
	 * @param device
	 * @param resultId
	 * @return
	 */
	public static OBXCONTENT buildOBX(int setId, String loincCode,
			BigDecimal vitalReading, String uom, XMLGregorianCalendar collectionTime, String vitalCollectionMode,
			Device device,
			String resultId)
	{
		OBXCONTENT observationContent = new OBXCONTENT();

		//Set ID
		OBX1CONTENT obx1 = new OBX1CONTENT();
		obx1.setValue(Integer.toString(setId));
		observationContent.setOBX1(obx1);

		//Observation Identifier..
		observationContent.setOBX3(buildOBX3(loincCode));

		//Observation value ..
		OBX5CONTENT obx5 = new OBX5CONTENT();
		obx5.setValue((vitalReading!=null) ? vitalReading.toString():"");
		observationContent.getOBX5().add(obx5);

		//Observation Unit...
		observationContent.setOBX6(buildOBX6(uom));

		//Date/time for observation...
		OBX14CONTENT obx14 = new OBX14CONTENT();
		TS1CONTENT obx14ts1 = new TS1CONTENT();
		obx14ts1.setValue(Utils.convertXMLGregorianCalendarToString(collectionTime));
		obx14.setTS1(obx14ts1);
		observationContent.setOBX14(obx14);

		//Collection Mode (Self/CareManager/Device)..
		observationContent.getOBX17().add(buildOBX17(vitalCollectionMode));

		//Equipment observation identifier (Only for Self/Device reported)...
		if(device!=null)
		{
			OBX18CONTENT obx18 = new OBX18CONTENT();

			// set interface id
			EI1CONTENT obx18ei1 = new EI1CONTENT();
			obx18ei1.setValue(device.getSerialNumber());
			obx18.setEI1(obx18ei1);

			// set interface name
			EI2CONTENT obx18ei2 = new EI2CONTENT();
			obx18ei2.setValue(device.getModel());
			obx18.setEI2(obx18ei2);

			// set device model number
			EI3CONTENT obx18ei3 = new EI3CONTENT();
			obx18ei3.setValue(device.getManufacturer());
			obx18.setEI3(obx18ei3);
			observationContent.getOBX18().add(obx18);
		}

		//Result Id for individual vitals..
		OBX21CONTENT obx21 = new OBX21CONTENT();
		EI1CONTENT obx21ei1 = new EI1CONTENT();
		obx21ei1.setValue((resultId!=null) ? resultId+"_V" : "");  // To differentiate between session and vitals..
		obx21.setEI1(obx21ei1);
		observationContent.setOBX21(obx21);

		return observationContent;
	}

	/**
	 * Observation Identifier..
	 * @param loinc
	 * @return
	 */
	public static OBX3CONTENT buildOBX3(String loinc)
	{
		OBX3CONTENT observationIdentifier = new OBX3CONTENT();

		//Customer-specific observation identifier (VUID)..
		CE1CONTENT ce1 = new CE1CONTENT();
		Map<String, String> loincCustCodes = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_LOINC_CUSTOMER_CODE.toString());
		if (loincCustCodes != null) {
			String custCode = loincCustCodes.get(loinc);
			if (null == custCode)
			{
				custCode = "";
			}
			ce1.setValue(custCode);
			logger.debug("Customer code for LOINC code " + loinc + " is " + custCode);
		}
		else {
			ce1.setValue("");
		}
		observationIdentifier.setCE1(ce1);

		//Customer-specific observation description..
		CE2CONTENT ce2 = new CE2CONTENT();
		Map<String, String> loincCustDescs = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_LOINC_CUSTOMER_DESCRIPTION.toString());
		if (loincCustDescs != null) {
			String custDesc = loincCustDescs.get(loinc);
			if (null == custDesc)
			{
				custDesc = "";
			}
			ce2.setValue(custDesc);
			logger.debug("Customer description for LOINC code " + loinc + " is " + custDesc);
		}
		else {
			ce2.setValue("");
		}
		observationIdentifier.setCE2(ce2);

		//Name of coding system ..
		CE3CONTENT ce3 = new CE3CONTENT();
		String loincConstant = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_LOINC_CONSTANT.toString());
		if (loincConstant != null && loincConstant instanceof String) {
			ce3.setValue(loincConstant);
			logger.debug("Loinc Constant For Loinc Code is " + loincConstant);
		}
		else {
			ce3.setValue("LOINC");
		}
		observationIdentifier.setCE3(ce3);

		return observationIdentifier;
	}

	/**
	 * Observation Unit...
	 * @param uomCode
	 * @return
	 */
	private static OBX6CONTENT buildOBX6(String uomCode)
	{
		OBX6CONTENT observationUnit = new OBX6CONTENT();

		//Customer-specific UOM identifier (VUID)
		CE1CONTENT ce1 = new CE1CONTENT();
		Map<String, String> uomCustCodes = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_UOM_CUSTOMER_CODE.toString());
		if (uomCustCodes != null) {
			String custCode = uomCustCodes.get(uomCode);
			if (null == custCode) {
				custCode = "";
			}
			ce1.setValue(custCode);
			logger.debug("Customer code for UOM code " + uomCode + " is " + custCode);
		}
		else {
			ce1.setValue("");
		}
		observationUnit.setCE1(ce1);

		//Customer-specific UOM description
		CE2CONTENT ce2 = new CE2CONTENT();
		Map<String, String> uomCustDescs = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_UOM_CUSTOMER_DESCRIPTION.toString());
		if (uomCustDescs != null){
			String custDesc = uomCustDescs.get(uomCode);
			if (null == custDesc) {
				custDesc = "";
			}
			ce2.setValue(custDesc);
			logger.debug("Customer description for UOM code " + uomCode + " is " + custDesc);
		}
		else {
			ce2.setValue("");
		}
		observationUnit.setCE2(ce2);

		//Name of coding system
		CE3CONTENT ce3 = new CE3CONTENT();
		Object ce3Config = ConfigurationLoader.getDataTypeConfigurations("ORU_R30", "OBX", "OBX6", "CE3");
		if (ce3Config != null && ce3Config instanceof String) {
			ce3.setValue(ce3Config.toString());
			logger.debug("OBX6-CE3 Configuration for " + "ORU_R30" + " Message: " + ce3Config.toString());
		}
		else {
			ce3.setValue("");
		}
		observationUnit.setCE3(ce3);

		return observationUnit;
	}

	/**
	 * Observation Mode...
	 * @param obsMethodCode
	 * @return
	 */
	private static OBX17CONTENT buildOBX17(String obsMethodCode)
	{
		OBX17CONTENT observationMode = new OBX17CONTENT();

		// set customer-specific observation method (data entry method) identifier (VUID)
		CE1CONTENT ce1 = new CE1CONTENT();
		Map<String, String> obsMethodCustCodes =
				ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_OBS_METHOD_CUSTOMER_CODE.toString());
		if (obsMethodCustCodes != null) {
			String custCode = obsMethodCustCodes.get(obsMethodCode);
			if (null == custCode){
				custCode = "";
			}
			ce1.setValue(custCode);
			logger.debug("Customer code for observation method code " + obsMethodCode + " is " + custCode);
		}
		else {
			ce1.setValue("");
		}
		observationMode.setCE1(ce1);

		//Customer-specific observation method (data entry method) description
		CE2CONTENT ce2 = new CE2CONTENT();
		Map<String, String> obsMethodCustDescs = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_OBS_METHOD_CUSTOMER_DESCRIPTION.toString());
		if (obsMethodCustDescs != null) {
			String custDesc = obsMethodCustDescs.get(obsMethodCode);
			if (null == custDesc){
				custDesc = "";
			}
			ce2.setValue(custDesc);
			logger.debug("Customer description for observation method code " + obsMethodCode + " is " + custDesc);
		}
		else {
			ce2.setValue("");
		}
		observationMode.setCE2(ce2);

		//Name of coding system
		CE3CONTENT ce3 = new CE3CONTENT();
		Object ce3Config = ConfigurationLoader.getDataTypeConfigurations("ORU_R30", "OBX", "OBX17", "CE3");
		if (ce3Config != null && ce3Config instanceof String) {
			ce3.setValue(ce3Config.toString());
			logger.debug("OBX17-CE3 Configuration for " + "ORU_R30" + " Message: " + ce3Config.toString());
		}
		else {
			ce3.setValue("");
		}
		observationMode.setCE3(ce3);

		return observationMode;
	}

	/**
	 * Batch number of messages...
	 * @param numOfMessages
	 * @return
	 */
	public static BTSCONTENT buildBTS(int numOfMessages) {
		BTSCONTENT batchResult = new BTSCONTENT();
		BTS1CONTENT bts1 = new BTS1CONTENT();
		bts1.setValue(Integer.toString(numOfMessages));
		batchResult.setBTS1(bts1);
		return batchResult;
	}

	/**
	 * Batch Header...
	 * @param receivingFacilityCode
	 * @param batchMessageType
	 * @param batchControlNumber
	 * @param referenceMessageControlNumber
	 * @return
	 */
	public static BHSCONTENT buildBHS(String receivingFacilityCode, String batchMessageType,String batchControlNumber, String referenceMessageControlNumber)
	{
		BHSCONTENT batchHeader = new BHSCONTENT();

		BHS1CONTENT bhs1 = new BHS1CONTENT();
		Field bhs1field = ConfigurationLoader.getFieldById("BHS1");
		bhs1.setValue(bhs1field != null && bhs1field.getConfigValue() != null ? bhs1field.getConfigValue().getValue() : null);
		batchHeader.setBHS1(bhs1);

		BHS2CONTENT bhs2 = new BHS2CONTENT();
		Field bhs2field = ConfigurationLoader.getFieldById("BHS2");
		bhs2.setValue(bhs2field != null && bhs2field.getConfigValue() != null ? bhs2field.getConfigValue().getValue() : null);
		batchHeader.setBHS2(bhs2);

		BHS3CONTENT bhs3 = new BHS3CONTENT();
		Field bhs3field = ConfigurationLoader.getFieldById("BHS3");
		bhs3.setValue(bhs3field != null && bhs3field.getConfigValue() != null ? bhs3field.getConfigValue().getValue() : null);
		batchHeader.setBHS3(bhs3);

		BHS4CONTENT bhs4 = new BHS4CONTENT();
		Field bhs4field = ConfigurationLoader.getFieldById("BHS4");
		bhs4.setValue(bhs4field != null && bhs4field.getConfigValue() != null ? bhs4field.getConfigValue().getValue() : null);
		batchHeader.setBHS4(bhs4);

		BHS5CONTENT bhs5 = new BHS5CONTENT();
		Field bhs5field = ConfigurationLoader.getFieldById("BHS5");
		bhs5.setValue(bhs5field != null && bhs5field.getConfigValue() != null ? bhs5field.getConfigValue().getValue() : null);
		batchHeader.setBHS5(bhs5);

		BHS6CONTENT bhs6 = new BHS6CONTENT();
		bhs6.setValue(receivingFacilityCode);
		batchHeader.setBHS6(bhs6);

		BHS7CONTENT bhs7 = new BHS7CONTENT();
		bhs7.setTS1(Utils.createCurrentTimestamp(new Date()));
		batchHeader.setBHS7(bhs7);

		BHS9CONTENT bhs9 = new BHS9CONTENT();
		bhs9.setValue(batchMessageType);
		batchHeader.setBHS9(bhs9);

		BHS11CONTENT bhs11 = new BHS11CONTENT();
		bhs11.setValue(batchControlNumber);
		batchHeader.setBHS11(bhs11);

		BHS12CONTENT bhs12 = new BHS12CONTENT();
		bhs12.setValue(referenceMessageControlNumber);
		batchHeader.setBHS12(bhs12);

		return batchHeader;
	}

	/**
	 * Build OBX.5 for subjective results..
	 * @param clinicalSessionRawXML
	 * @return
	 */
	public static OBXCONTENT buildSubjectiveOBX(String clinicalSessionRawXML, String observationId)
	{
		OBXCONTENT observation = new OBXCONTENT();

		//Session assembly....
		OBX5CONTENT obx5 = new OBX5CONTENT();
		obx5.setValue(clinicalSessionRawXML);
		observation.getOBX5().add(obx5);

		//Session unique data...
		OBX21CONTENT obx21 = new OBX21CONTENT();
		EI1CONTENT obx21ei1 = new EI1CONTENT();
		obx21ei1.setValue((observationId!=null) ? observationId+"_S" : ""); // To differentiate between session and vitals..
		obx21.setEI1(obx21ei1);
		observation.setOBX21(obx21);

		return observation;
	}

	/**
	 * Build NTE segment for each note (patient/survey)...
	 * @param note
	 * @return
	 */
	public static NTECONTENT buildNTE(Note note)
	{
		NTECONTENT noteContent = new NTECONTENT();

		//Extract notes header and type...
		HeaderType noteHeader = note.getHeader();
		NotesPayloadType notesPayload = note.getPayload();

		if(noteHeader == null) {
			return null;
		}

		//Note Id's..
		NTE1CONTENT nte1 = new NTE1CONTENT();
		nte1.setValue(noteHeader.getId().toString()+"_N");
		noteContent.setNTE1(nte1);

		//Note Category..
		NTE2CONTENT nte2 = new NTE2CONTENT();
		nte2.setValue((notesPayload.getCategory()!=null && notesPayload.getCategory().size()>1 && notesPayload.getCategory().get(0)!=null) ? notesPayload.getCategory().get(0).getName():"");
		noteContent.setNTE2(nte2);

		//Note Description..
		NTE3CONTENT nte3 = new NTE3CONTENT();
		nte3.getContent().add(noteHeader.getDescription());
		noteContent.getNTE3().add(nte3);

		//Note Type..
		NTE4CONTENT nte4 = new NTE4CONTENT();
		CE1CONTENT ce1 = new CE1CONTENT();
		ce1.setValue(noteHeader.getTaskType().value());
		nte4.setCE1(ce1);
		noteContent.setNTE4(nte4);

		//Owner First Name..
		XCN1CONTENT xcn1 = new XCN1CONTENT();
		xcn1.setValue((noteHeader.getChangedByFirstName()!=null)? noteHeader.getChangedByFirstName():noteHeader.getOwnerFirstName());

		//Owner Last Name..
		XCN2CONTENT xcn2 = new XCN2CONTENT();
		FN1CONTENT fn1 = new FN1CONTENT();
		fn1.setValue((noteHeader.getChangedByLastName()!=null)? noteHeader.getChangedByLastName():noteHeader.getOwnerLastName());
		xcn2.setFN1(fn1);

		//Note Owner details..
		NTE5CONTENT nte5 = new NTE5CONTENT();
		nte5.setXCN1(xcn1);
		nte5.setXCN2(xcn2);
		noteContent.setNTE5(nte5);

		//Note creation date...
		TS1CONTENT ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString((noteHeader.getChangeTime()!=null ? noteHeader.getChangeTime() : noteHeader.getCreationDateTime())));
		NTE6CONTENT nte6 = new NTE6CONTENT();
		nte6.setTS1(ts1);
		noteContent.setNTE6(nte6);

		//Note Source Id ...
		NTE11CONTENT nte11 = new NTE11CONTENT();
		CE1CONTENT ce11 = new CE1CONTENT();
		ce11.setValue((notesPayload!= null && notesPayload instanceof SurveyNotesPayloadType)? notesPayload.getSourceId().toString() : "");
		nte11.setCE1(ce11);
		noteContent.setNTE11(nte11);

		return noteContent;
	}
}
