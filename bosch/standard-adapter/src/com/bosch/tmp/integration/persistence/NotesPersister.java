/**
 *
 */
package com.bosch.tmp.integration.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.th.integration.notes.Note;
import com.bosch.th.integration.notes.PushNotes;
import com.bosch.tmp.cl.task.HeaderType;
import com.bosch.tmp.cl.task.NotesPayloadType;
import com.bosch.tmp.cl.task.PatientNotesPayloadType;
import com.bosch.tmp.cl.task.SurveyNotesPayloadType;
import com.bosch.tmp.integration.dao.NotesDao;
import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import org.springframework.stereotype.Service;

/**
 * @author BEA2KOR
 *
 */
@Service
public class NotesPersister
{
	public static final Logger logger = LoggerFactory.getLogger(NotesPersister.class);

	@Autowired
	private NotesDao notesDao;

	public NotesPersister(){

	}

	/**
	 * Acknowledge existing notes sent to customer through ORU batch message...
	 * @param notes
	 */
	public void acknowledgeNotes(List<Notes> notes) {
		for (Notes note : notes) {
			logger.debug("Persisting acknowledgment information for a Note with ID: " + note.getNotesId());
			note.setIsAcknowledged(Boolean.TRUE);
			notesDao.update(note);
		}
	}

	/**
	 * Persist the notes information from the CIA PushNotes (Task service response) operation into CDA (NOTES table) ...
	 * @param pushNotes
	 * @return
	 */
	public void persistNotes(PushNotes pushNotes)
	{
		if(pushNotes == null) {
			logger.debug("PushNotes is null.");
			return;
		}

		//Extract the patient Id..
		String patientId = null;
		try
		{
			String patId = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
			patientId = MessageUtils.getMessagePatIdentifier(pushNotes.getPatientId().getExternalIdentifier(),patId);
			if(patientId ==  null){
				throw new Exception("Patient Id is null.");
			}

			//Extract the organization Id...
			String organizationId = null;
			organizationId = pushNotes.getIntegrationPatient().getInternalOrganizationId();
			if (organizationId == null) {
				throw new Exception("Organization Id is null.");
			}

			//Extract the notes information..
			List<Note> notes = pushNotes.getNote();
			if (notes == null || notes.size() == 0){
				logger.debug("No Notes found in pushed notes.");
				return;
			}

			//Create or Update Notes entity instance for ever note in the push Notes response...
			for (Note note : notes)
			{
				//Extract notes header and type...
				HeaderType noteDetails = note.getHeader();
				NotesPayloadType noteType = note.getPayload();
				if(noteType == null || !(noteType instanceof PatientNotesPayloadType || noteType instanceof SurveyNotesPayloadType)){
					logger.error("Note type is unknown or not available for the Note id :" + noteDetails.getId());
					break;
				}

				//Extract the session id in case of Survey notes...
				Long surveyId = null;
				if(noteType!= null &&  noteType instanceof SurveyNotesPayloadType){
					SurveyNotesPayloadType surveyNote = (SurveyNotesPayloadType)noteType;
					surveyId = surveyNote.getSourceId();
				}

				if(noteDetails!=null && noteDetails.getId()!=null)
				{
					//Check the existing note id...
					List<Notes> foundNotes = notesDao.findNotesByNoteId(noteDetails.getId());

					//Update the existing notes entity if already present, else create a new note entity...
					if(foundNotes!=null && !foundNotes.isEmpty())
					{
						for (Notes noteEntity : foundNotes) {
							noteEntity.setIsAcknowledged(Boolean.FALSE);
							noteEntity.setModifiedTimestamp(Utils.convertXMLGregorianCalendarToDate(noteDetails.getChangeTime()));
							updateNote(noteEntity);
						}
					}
					else
					{
						//Construct notes entity...
						Notes noteEntity = new Notes();
						noteEntity.setNotesId(noteDetails.getId());
						noteEntity.setNotesType((noteType!=null && noteType instanceof PatientNotesPayloadType) ? "P" :
							(noteType!=null && noteType instanceof SurveyNotesPayloadType)?"S":null);
						noteEntity.setIsAcknowledged(Boolean.FALSE);
						noteEntity.setOrganizationId(organizationId);
						noteEntity.setPatientId(patientId);
						noteEntity.setSessionId(surveyId);
						createNote(noteEntity);
					}
				}
			}
			logger.info("Notes persistence done.");
		}
		catch (Exception e) {
			logger.error("Exception occurs when persisting notes: ", e);
		}
	}

	/**
	 * Create a single note entity ....
	 * @param note
	 */
	private void createNote(Notes note) {
		notesDao.save(note);
	}

	/**
	 * Merge a single note entity....
	 * @param note
	 */
	private void updateNote(Notes note) {
		notesDao.update(note);
	}
}
