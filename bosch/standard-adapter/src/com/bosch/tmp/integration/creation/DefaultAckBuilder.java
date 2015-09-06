package com.bosch.tmp.integration.creation;

import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.persistence.ControlNumber;
import com.bosch.tmp.integration.util.AckTypeEnum;
import com.bosch.tmp.integration.util.AckRequestCodeEnum;
import com.bosch.tmp.integration.util.AckResponseCodeEnum;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import java.util.Date;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.BHS3CONTENT;
import org.hl7.v2xml.BHS4CONTENT;
import org.hl7.v2xml.BHS5CONTENT;
import org.hl7.v2xml.BHS6CONTENT;
import org.hl7.v2xml.BHS9CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.HD1CONTENT;
import org.hl7.v2xml.MSA1CONTENT;
import org.hl7.v2xml.MSA2CONTENT;
import org.hl7.v2xml.MSACONTENT;
import org.hl7.v2xml.MSG1CONTENT;
import org.hl7.v2xml.MSG2CONTENT;
import org.hl7.v2xml.MSG3CONTENT;
import org.hl7.v2xml.MSH10CONTENT;
import org.hl7.v2xml.MSH11CONTENT;
import org.hl7.v2xml.MSH12CONTENT;
import org.hl7.v2xml.MSH1CONTENT;
import org.hl7.v2xml.MSH2CONTENT;
import org.hl7.v2xml.MSH3CONTENT;
import org.hl7.v2xml.MSH4CONTENT;
import org.hl7.v2xml.MSH5CONTENT;
import org.hl7.v2xml.MSH6CONTENT;
import org.hl7.v2xml.MSH7CONTENT;
import org.hl7.v2xml.MSH9CONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.PT1CONTENT;
import org.hl7.v2xml.TS1CONTENT;
import org.hl7.v2xml.VID1CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.HD2CONTENT;
import org.hl7.v2xml.HD3CONTENT;
import org.hl7.v2xml.MSA3CONTENT;
import org.hl7.v2xml.MSH16CONTENT;

/**
 * This is the default builder of an acknowledgment message (ACK).
 * This class has business logic to create negative or positive ack.
 *
 * @author gtk1pal
 * Modified by tis1pal
 */
public class DefaultAckBuilder implements Builder
{

    public static final Logger logger = LoggerFactory.getLogger(DefaultAckBuilder.class);

    private AckTypeEnum ackType = AckTypeEnum.APP;
    
    private Error error = null;

    public DefaultAckBuilder()
    {
    }

    public DefaultAckBuilder(AckTypeEnum ackType)
    {
        this.ackType = ackType;
    }

    public Object buildMessage(Object... data) throws CreationException
    {
        if (data.length < 3)
        {
            logger.debug("Error creating ack message. Invalid number of arguments.");
            throw new CreationException("Error creating ack message. Invalid number of arguments.");
        }
        this.ackType = (AckTypeEnum) data[0];
        this.error = (Error) data[2];
       
        return buildACK(data[1]);
    }

    /**
     * This method builds an ACK message.
     * @param mshFromMessage msh segment from incoming message
     * @return ACKCONTENT negative/positive acknowledgment
     */
    public ACKCONTENT buildACK(Object message)
    {
        logger.debug("Building ack message");
        ACKCONTENT ack = new ACKCONTENT();

        Object hl7MessageHeader = null;
        try
        {
            hl7MessageHeader = MessageUtils.getHL7MessageHeader(message);
        }
        catch (Exception ex)
        {
            logger.error(
                    "Cannot get MSH from the incoming message. Setting some fields in MSH for ACK to default config values or null");
        }

        if (hl7MessageHeader != null && hl7MessageHeader instanceof MSHCONTENT) {
            MSHCONTENT mshContent = (MSHCONTENT) hl7MessageHeader;
            mshContent = buildMshForAck(mshContent);
            ack.setMSH(mshContent);
        }
        else if (hl7MessageHeader != null && hl7MessageHeader instanceof BHSCONTENT) {
			BHSCONTENT bhsContent = (BHSCONTENT) hl7MessageHeader;
			MSHCONTENT mshContent = buildMSHForBatch(bhsContent);
			ack.setMSH(mshContent);
		}

        MSACONTENT msaForAck = buildMsaForAck(hl7MessageHeader);
        ack.setMSA(msaForAck);

        return ack;
    }

    /**
     * This method builds an MSH segment with required info to create an ACK message.
     * @param mshFromMessage msh segment from incoming message
     * @return MSHCONTENT msh segment for ack
     */
    private MSHCONTENT buildMshForAck(MSHCONTENT mshFromMessage)
    {
        MSHCONTENT mshForAck = new MSHCONTENT();

        //field separator for ack message
        MSH1CONTENT msh1ForAck = new MSH1CONTENT();
        Object msh1ForAckConfig = ConfigurationLoader.getFieldConfigurations("ACK", "MSH", "MSH1");
        if (msh1ForAckConfig != null && msh1ForAckConfig instanceof String)
        {
            msh1ForAck.setValue(msh1ForAckConfig.toString());
            logger.debug("MSH1 Configuration for ACK Message: " + msh1ForAckConfig.toString());
        }
        mshForAck.setMSH1(msh1ForAck);

        //message encoding characters for ack message
        MSH2CONTENT msh2ForAck = new MSH2CONTENT();
        Object msh2ForAckConfig = ConfigurationLoader.getFieldConfigurations("ACK", "MSH", "MSH2");
        if (msh2ForAckConfig != null && msh2ForAckConfig instanceof String)
        {
            msh2ForAck.setValue(msh2ForAckConfig.toString());
            logger.debug("MSH2 Configuration for ACK Message: " + msh2ForAckConfig.toString());
        }
        mshForAck.setMSH2(msh2ForAck);

        //msh3 is the sending application
        MSH3CONTENT msh3ForAck = new MSH3CONTENT();
        HD1CONTENT msh3Hd1ForAck = new HD1CONTENT();
        MSH5CONTENT msh5FromMessage = (mshFromMessage == null) ? null : mshFromMessage.getMSH5();
        if (msh5FromMessage != null)
        {
            msh3Hd1ForAck.setValue(msh5FromMessage.getHD1() == null ? null : msh5FromMessage.getHD1().getValue());
        }
        if (msh3Hd1ForAck.getValue() == null || msh3Hd1ForAck.getValue().length() == 0)
        {
            Object msh3Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH3", "HD1");
            if (msh3Hd1ForAckConfig != null && msh3Hd1ForAckConfig instanceof String)
            {
                msh3Hd1ForAck.setValue(msh3Hd1ForAckConfig.toString());
            }
        }
        logger.debug("MSH3HD1 Configuration for ACK Message: " + msh3Hd1ForAck.getValue());
        msh3ForAck.setHD1(msh3Hd1ForAck);
        mshForAck.setMSH3(msh3ForAck);

        //msh4 is sending facility which represents the Standard adapter org id.
        //the organization id is extracted from msh6 field of the incoming message.
        MSH4CONTENT msh4ForAck = new MSH4CONTENT();
        HD1CONTENT msh4Hd1ForAck = new HD1CONTENT();
        HD2CONTENT msh4Hd2ForAck = new HD2CONTENT();
        HD3CONTENT msh4Hd3ForAck = new HD3CONTENT();
        MSH6CONTENT msh6FromMessage = (mshFromMessage == null) ? null : mshFromMessage.getMSH6();
        if (msh6FromMessage != null)
        {
            msh4Hd1ForAck.setValue(msh6FromMessage.getHD1() == null ? null : msh6FromMessage.getHD1().getValue());
            msh4Hd2ForAck.setValue(msh6FromMessage.getHD2() == null ? null : msh6FromMessage.getHD2().getValue());
            msh4Hd3ForAck.setValue(msh6FromMessage.getHD3() == null ? null : msh6FromMessage.getHD3().getValue());
        }
        if (msh4Hd1ForAck.getValue() == null || msh4Hd1ForAck.getValue().length() == 0)
        {
            Object msh4Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH4", "HD1");
            if (msh4Hd1ForAckConfig != null && msh4Hd1ForAckConfig instanceof String)
            {
                msh4Hd1ForAck.setValue(msh4Hd1ForAckConfig.toString());
            }
        }
        msh4ForAck.setHD1(msh4Hd1ForAck);
        msh4ForAck.setHD2(msh4Hd2ForAck);
        msh4ForAck.setHD3(msh4Hd3ForAck);
        mshForAck.setMSH4(msh4ForAck);

        //msh5 is the receiving application
        //sending application in msh3 of the incoming message becomes the receiving app in the ack message.
        MSH5CONTENT msh5ForAck = new MSH5CONTENT();
        HD1CONTENT msh5Hd1ForAck = new HD1CONTENT();
        MSH3CONTENT msh3FromMessage = (mshFromMessage == null) ? null : mshFromMessage.getMSH3();
        if (msh3FromMessage != null)
        {
            msh5Hd1ForAck.setValue(msh3FromMessage.getHD1() == null ? null : msh3FromMessage.getHD1().getValue());
        }
        if (msh5Hd1ForAck.getValue() == null || msh5Hd1ForAck.getValue().length() == 0)
        {
            Object msh5Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH5", "HD1");
            if (msh5Hd1ForAckConfig != null && msh5Hd1ForAckConfig instanceof String)
            {
                msh5Hd1ForAck.setValue(msh5Hd1ForAckConfig.toString());
            }
        }
        msh5ForAck.setHD1(msh5Hd1ForAck);
        mshForAck.setMSH5(msh5ForAck);

        //msh6 is the receiving facility
        //sending facility in msh4 of the incoming message becomes the receiving facility in the ack message.
        MSH6CONTENT msh6ForAck = new MSH6CONTENT();
        HD1CONTENT msh6Hd1ForAck = new HD1CONTENT();
        HD2CONTENT msh6Hd2ForAck = new HD2CONTENT();
        HD3CONTENT msh6Hd3ForAck = new HD3CONTENT();
        MSH4CONTENT msh4FromMessage = (mshFromMessage == null) ? null : mshFromMessage.getMSH4();
        if (msh4FromMessage != null)
        {
            msh6Hd1ForAck.setValue(msh4FromMessage.getHD1() == null ? null : msh4FromMessage.getHD1().getValue());
            msh6Hd2ForAck.setValue(msh4FromMessage.getHD2() == null ? null : msh4FromMessage.getHD2().getValue());
            msh6Hd3ForAck.setValue(msh4FromMessage.getHD3() == null ? null : msh4FromMessage.getHD3().getValue());
        }
        if (msh6Hd1ForAck.getValue() == null || msh6Hd1ForAck.getValue().length() == 0)
        {//this will never be incoked because MSH4 will never be null .
            Object msh6Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH6", "HD1");
            if (msh6Hd1ForAckConfig != null && msh6Hd1ForAckConfig instanceof String)
            {
                msh6Hd1ForAck.setValue(msh6Hd1ForAckConfig.toString());
            }
        }
        msh6ForAck.setHD1(msh6Hd1ForAck);
        msh6ForAck.setHD2(msh6Hd2ForAck);
        msh6ForAck.setHD3(msh6Hd3ForAck);
        mshForAck.setMSH6(msh6ForAck);

        //time of ACK
        MSH7CONTENT msh7ForAck = new MSH7CONTENT();
        TS1CONTENT ts1ForAck = Utils.createCurrentTimestamp(new Date());
        msh7ForAck.setTS1(ts1ForAck);
        mshForAck.setMSH7(msh7ForAck);

        //message type
        MSH9CONTENT msh9ForAck = new MSH9CONTENT();
        MSG1CONTENT msg1ForAck = new MSG1CONTENT();
        Object msg1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH9", "MSG1");
        if (msg1ForAckConfig != null && msg1ForAckConfig instanceof String)
        {
            msg1ForAck.setValue(msg1ForAckConfig.toString());
            logger.debug("MSG1 Configuration for ACK Message: " + msg1ForAckConfig.toString());
        }
        msh9ForAck.setMSG1(msg1ForAck);
        //MSG2 :-the event id is extracted from msh9 field of the incoming message for MSG2.
        MSG2CONTENT msg2ForAck = new MSG2CONTENT();
        MSH9CONTENT msh9FromMessage = (mshFromMessage == null) ? null : mshFromMessage.getMSH9();
        if (msh9FromMessage != null)
        {
            msg2ForAck.setValue(msh9FromMessage.getMSG2().getValue());
            logger.debug("MSG2 Configuration for ACK Message: " + msg2ForAck.toString());
        }
        msh9ForAck.setMSG2(msg2ForAck);
        //MSG3 for ACK
        MSG3CONTENT msg3ForAck = new MSG3CONTENT();
        Object msg3ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH9", "MSG3");
        if (msg3ForAckConfig != null && msg3ForAckConfig instanceof String)
        {
            msg3ForAck.setValue(msg3ForAckConfig.toString());
            logger.debug("MSG3 Configuration for ACK Message: " + msg3ForAckConfig.toString());
        }
        msh9ForAck.setMSG3(msg3ForAck);
        mshForAck.setMSH9(msh9ForAck);

        //message control number
        MSH10CONTENT msh10ForAck = new MSH10CONTENT();
        String messageControlId = null;
        try
        {
            ControlNumber controlNumber = new ControlNumber();
            controlNumber.persist();
            messageControlId = controlNumber.getId().toString();
        }
        catch (Exception ex)
        {
            logger.error("Cannot persist control number...setting it to empty string");
            messageControlId = "";
        }
        logger.debug("MSH10 - Message Control Id generated for ACK Message: " + messageControlId);

        msh10ForAck.setValue(messageControlId);
        mshForAck.setMSH10(msh10ForAck);

         //processing ID
        MSH11CONTENT msh11ForAck = new MSH11CONTENT();
        PT1CONTENT msh11Pt1 = new PT1CONTENT();
        MSH11CONTENT msh11FromIncoming = (mshFromMessage == null) ? null : mshFromMessage.getMSH11();
        if (msh11FromIncoming != null) {
        	msh11Pt1.setValue(msh11FromIncoming.getPT1().getValue() == null ? null : msh11FromIncoming.getPT1().getValue());
        }
        if (msh11Pt1.getValue() == null || msh11Pt1.getValue().length() == 0){
	        Object msh11Pt1Config = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH11", "PT1");
	        if (msh11Pt1Config != null && msh11Pt1Config instanceof String)
	        {
	            msh11Pt1.setValue(msh11Pt1Config.toString());
	            logger.debug("MSH11PT1 Configuration for " + "ACK" + " Message: " + msh11Pt1Config.toString());
	        }
        }
        msh11ForAck.setPT1(msh11Pt1);
        mshForAck.setMSH11(msh11ForAck);

        //HL7 version
		MSH12CONTENT msh12ForAck = new MSH12CONTENT();
		VID1CONTENT vid1ForAck = new VID1CONTENT();
		MSH12CONTENT msh12FromIncoming = (mshFromMessage == null) ? null : mshFromMessage.getMSH12();
		if (msh12FromIncoming!=null && msh12FromIncoming.getVID1().getValue()!=null){
			vid1ForAck.setValue(msh12FromIncoming.getVID1().getValue());
			logger.debug("MSH12VID1 Configuration for ACK Message: " + vid1ForAck.getValue());
		}
		if(vid1ForAck.getValue() == null || vid1ForAck.getValue().length() == 0) {
			Object msh12Vid1Config = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH12", "VID1");
			if (msh12Vid1Config != null && msh12Vid1Config instanceof String)
			{
				vid1ForAck.setValue(msh12Vid1Config.toString());
				logger.debug("MSH12VID1 Configuration for ACK Message: " + msh12Vid1Config.toString());
			}
		}
		msh12ForAck.setVID1(vid1ForAck);
		mshForAck.setMSH12(msh12ForAck);

        //config for application ack
        MSH16CONTENT msh16ForAck = new MSH16CONTENT();
        msh16ForAck.setValue(AckRequestCodeEnum.NE.toString());
        mshForAck.setMSH16(msh16ForAck);

        return mshForAck;
    }

    /**
     * This method builds an MSA segment with required info to create an ACK message.
     * @param mshFromMessage msh segment from incoming message
     * @return MSACONTENT msa segment for ack
     */
    private MSACONTENT buildMsaForAck(Object hl7MessageHeader)
    {
        MSACONTENT msaForAck = new MSACONTENT();

        //message control number
        MSA2CONTENT msa2ForAck = new MSA2CONTENT();
        try
        {
            msa2ForAck.setValue(MessageUtils.getHL7MessageControlNumber(hl7MessageHeader));
        }
        catch (Exception e)
        {
        }
        msaForAck.setMSA2(msa2ForAck);

        if (error.hasErrors())
        {
            Error errorNew = error.getError();
            String faultExternalCode = errorNew.getFaultExternalCode();
            String faultExternalMessage = errorNew.getFaultExternalMessage();
            //set ack type code to AR
            MSA1CONTENT msa1ForAck = new MSA1CONTENT();
            msa1ForAck.setValue(AckResponseCodeEnum.AR.toString());
            msaForAck.setMSA1(msa1ForAck);

            //set message rejection reason
            MSA3CONTENT msa3ForAck = new MSA3CONTENT();
            msa3ForAck.setValue(faultExternalCode + ":" + faultExternalMessage);
            msaForAck.setMSA3(msa3ForAck);

            logger.info("Negative acknowledgement error info: " + faultExternalCode + "  " + faultExternalMessage);
        }
        else
        {
            //set ack type code to AA
            MSA1CONTENT msa1ForAck = new MSA1CONTENT();
            msa1ForAck.setValue(AckResponseCodeEnum.AA.toString());
            msaForAck.setMSA1(msa1ForAck);
        }
        return msaForAck;
    }

    /**
	 * This method builds an MSH segment with required info to create an ACK message.
	 * @param mshFromMessage msh segment from incoming message
	 * @return MSHCONTENT msh segment for ack
	 */
	private MSHCONTENT buildMSHForBatch(BHSCONTENT bhsFromMessage)
	{
		MSHCONTENT mshForAck = new MSHCONTENT();

		//Field separator for ack message
		MSH1CONTENT msh1ForAck = new MSH1CONTENT();
		Object msh1ForAckConfig = ConfigurationLoader.getFieldConfigurations("ACK", "MSH", "MSH1");
		if (msh1ForAckConfig != null && msh1ForAckConfig instanceof String) {
			msh1ForAck.setValue(msh1ForAckConfig.toString());
		}
		mshForAck.setMSH1(msh1ForAck);

		//Message encoding characters for ack message
		MSH2CONTENT msh2ForAck = new MSH2CONTENT();
		Object msh2ForAckConfig = ConfigurationLoader.getFieldConfigurations("ACK", "MSH", "MSH2");
		if (msh2ForAckConfig != null && msh2ForAckConfig instanceof String){
			msh2ForAck.setValue(msh2ForAckConfig.toString());
		}
		mshForAck.setMSH2(msh2ForAck);

		//MSH3 is the sending application
		MSH3CONTENT msh3ForAck = new MSH3CONTENT();
		HD1CONTENT msh3Hd1ForAck = new HD1CONTENT();
		BHS5CONTENT bhs5FromMessage = (bhsFromMessage == null) ? null : bhsFromMessage.getBHS5();
		if (bhs5FromMessage != null &&  bhs5FromMessage.getValue()!=null && !bhs5FromMessage.getValue().trim().equalsIgnoreCase("")) {
			msh3Hd1ForAck.setValue(bhs5FromMessage.getValue());
		}
		else {
			Object msh3Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH3", "HD1");
			if (msh3Hd1ForAckConfig != null && msh3Hd1ForAckConfig instanceof String){
				msh3Hd1ForAck.setValue(msh3Hd1ForAckConfig.toString());
			}
		}
		msh3ForAck.setHD1(msh3Hd1ForAck);
		mshForAck.setMSH3(msh3ForAck);

		//MSH4 is sending facility which represents the Standard adapter Organization id.
		MSH4CONTENT msh4ForAck = new MSH4CONTENT();
		HD1CONTENT msh4Hd1ForAck = new HD1CONTENT();
		HD2CONTENT msh4Hd2ForAck = new HD2CONTENT();
		HD3CONTENT msh4Hd3ForAck = new HD3CONTENT();
		BHS6CONTENT bhs6FromMessage = (bhsFromMessage == null) ? null : bhsFromMessage.getBHS6();
		if (bhs6FromMessage != null && bhs6FromMessage.getValue()!=null && bhs6FromMessage.getValue().trim()!=null){
			msh4Hd1ForAck.setValue(bhs6FromMessage.getValue());
		}
		else {
			Object msh4Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH4", "HD1");
			if (msh4Hd1ForAckConfig != null && msh4Hd1ForAckConfig instanceof String) {
				msh4Hd1ForAck.setValue(msh4Hd1ForAckConfig.toString());
			}
		}
		msh4ForAck.setHD1(msh4Hd1ForAck);
		msh4ForAck.setHD2(msh4Hd2ForAck);
		msh4ForAck.setHD3(msh4Hd3ForAck);
		mshForAck.setMSH4(msh4ForAck);

		//MSH5 is the receiving application
		MSH5CONTENT msh5ForAck = new MSH5CONTENT();
		HD1CONTENT msh5Hd1ForAck = new HD1CONTENT();
		BHS3CONTENT bhs3FromMessage = (bhsFromMessage == null) ? null : bhsFromMessage.getBHS3();
		if (bhs3FromMessage != null && bhs3FromMessage.getValue()!=null && bhs3FromMessage.getValue().trim()!=null){
			msh5Hd1ForAck.setValue(bhs3FromMessage.getValue());
		}
		else {
			Object msh5Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH5", "HD1");
			if (msh5Hd1ForAckConfig != null && msh5Hd1ForAckConfig instanceof String) {
				msh5Hd1ForAck.setValue(msh5Hd1ForAckConfig.toString());
			}
		}
		msh5ForAck.setHD1(msh5Hd1ForAck);
		mshForAck.setMSH5(msh5ForAck);

		//MSH6 is the receiving facility
		MSH6CONTENT msh6ForAck = new MSH6CONTENT();
		HD1CONTENT msh6Hd1ForAck = new HD1CONTENT();
		HD2CONTENT msh6Hd2ForAck = new HD2CONTENT();
		HD3CONTENT msh6Hd3ForAck = new HD3CONTENT();
		BHS4CONTENT bhs4FromMessage = (bhsFromMessage == null) ? null : bhsFromMessage.getBHS4();
		if (bhs4FromMessage != null){
			msh6Hd1ForAck.setValue(bhs4FromMessage.getValue());
		}
		else {
			Object msh6Hd1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH6", "HD1");
			if (msh6Hd1ForAckConfig != null && msh6Hd1ForAckConfig instanceof String) {
				msh6Hd1ForAck.setValue(msh6Hd1ForAckConfig.toString());
			}
		}
		msh6ForAck.setHD1(msh6Hd1ForAck);
		msh6ForAck.setHD2(msh6Hd2ForAck);
		msh6ForAck.setHD3(msh6Hd3ForAck);
		mshForAck.setMSH6(msh6ForAck);

		//Time of ACK
		MSH7CONTENT msh7ForAck = new MSH7CONTENT();
		TS1CONTENT ts1ForAck = Utils.createCurrentTimestamp(new Date());
		msh7ForAck.setTS1(ts1ForAck);
		mshForAck.setMSH7(msh7ForAck);

		//Message type

		//MSG1
		MSH9CONTENT msh9ForAck = new MSH9CONTENT();
		MSG1CONTENT msg1ForAck = new MSG1CONTENT();
		Object msg1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH9", "MSG1");
		if (msg1ForAckConfig != null && msg1ForAckConfig instanceof String){
			msg1ForAck.setValue(msg1ForAckConfig.toString());
		}
		msh9ForAck.setMSG1(msg1ForAck);

		//MSG2 : The event id is extracted from BHS9 field of the incoming message for MSG2.
		MSG2CONTENT msg2ForAck = new MSG2CONTENT();
		BHS9CONTENT bhs9FromMessage = (bhsFromMessage == null) ? null : bhsFromMessage.getBHS9();
		if (bhs9FromMessage != null) {
			msg2ForAck.setValue(bhs9FromMessage.getValue());
		}
		msh9ForAck.setMSG2(msg2ForAck);

		//MSG3 :
		MSG3CONTENT msg3ForAck = new MSG3CONTENT();
		Object msg3ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH9", "MSG3");
		if (msg3ForAckConfig != null && msg3ForAckConfig instanceof String) {
			msg3ForAck.setValue(msg3ForAckConfig.toString());
		}
		msh9ForAck.setMSG3(msg3ForAck);
		mshForAck.setMSH9(msh9ForAck);

		//Message control number
		MSH10CONTENT msh10ForAck = new MSH10CONTENT();
		String messageControlId = null;
		try {
			ControlNumber controlNumber = new ControlNumber();
			controlNumber.persist();
			messageControlId = controlNumber.getId().toString();
		}
		catch (Exception ex) {
			logger.error("Cannot persist control number...setting it to empty string");
			messageControlId = "";
		}
		logger.debug("MSH10 - Message Control Id generated for ACK Message: " + messageControlId);
		msh10ForAck.setValue(messageControlId);
		mshForAck.setMSH10(msh10ForAck);

		//Processing ID
		MSH11CONTENT msh11ForAck = new MSH11CONTENT();
		PT1CONTENT pt1ForAck = new PT1CONTENT();
		Object msh11Pt1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH11", "PT1");
        if (msh11Pt1ForAckConfig != null && msh11Pt1ForAckConfig instanceof String){
        pt1ForAck.setValue(msh11Pt1ForAckConfig.toString());
        }
		msh11ForAck.setPT1(pt1ForAck);
		mshForAck.setMSH11(msh11ForAck);

		//HL7 version
		MSH12CONTENT msh12ForAck = new MSH12CONTENT();
		VID1CONTENT vid1ForAck = new VID1CONTENT();
		Object msh12Vid1Config = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH12", "VID1");
        if (msh12Vid1Config != null && msh12Vid1Config instanceof String){
	        vid1ForAck.setValue(msh12Vid1Config.toString());
        }
		msh12ForAck.setVID1(vid1ForAck);
		mshForAck.setMSH12(msh12ForAck);

		//Config for application ack
		MSH16CONTENT msh16ForAck = new MSH16CONTENT();
		msh16ForAck.setValue(AckRequestCodeEnum.NE.toString());
		mshForAck.setMSH16(msh16ForAck);

		return mshForAck;
	}
}
