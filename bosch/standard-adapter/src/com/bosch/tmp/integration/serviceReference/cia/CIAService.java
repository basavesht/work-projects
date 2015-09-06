package com.bosch.tmp.integration.serviceReference.cia;

import java.util.List;

import com.bosch.th.cia.DuplicatePatientFault;
import com.bosch.th.cia.FindNotesReply;
import com.bosch.th.cia.FindResultsReply;
import com.bosch.th.cia.InvalidDataFault;
import com.bosch.th.cia.NoSuchUserFault;
import com.bosch.th.cia.PatientAlreadyEnrolledFault;
import com.bosch.th.cia.PatientNotActiveFault;
import com.bosch.th.cia.PatientNotPausedFault;
import com.bosch.th.cia.RemoteServiceFault;
import com.bosch.th.cia.UserNotActiveFault;
import com.bosch.th.cia.UserNotAssignableFault;
import com.bosch.th.externalorganization.OrganizationType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.tmp.cl.results.ResultId;
import com.bosch.tmp.cl.task.EqualsFilterType;
import com.bosch.tmp.integration.process.ProcessException;
import com.bosch.tmp.integration.process.results.ResultsProcessException;
import com.bosch.tmp.integration.util.Error;

public interface CIAService
{
	/**
	 * Find Results through CIA from CL Result service...
	 * @param resultIds
	 * @return
	 * @throws ResultsProcessException
	 */
	public FindResultsReply findResults(List<ResultId> resultIds, Error error) throws ResultsProcessException;

	/**
	 * Find Next set of Results if number of chunks exceeds one..
	 * @param resultIds
	 * @param chunkItemId
	 * @return
	 * @throws ResultsProcessException
	 */
	public FindResultsReply findNextChunkResults(List<ResultId> resultIds,Long chunkItemId, Error error) throws ResultsProcessException;

	/**
	 * Get the external organizations liked to organization id...
	 * @param orgId
	 * @param customerId
	 * @return
	 * @throws ProcessException
	 */
	public List<OrganizationType> getExternalOrganizations(String orgId, String customerId, Error error) throws ProcessException;

	/**
	 * Find Patients through internal identifier..
	 * @param identifiersList
	 * @param internalIdentifier
	 * @return
	 * @throws ProcessException
	 */
	public List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList,Long internalIdentifier, Error error) throws ProcessException;

	/**
	 * Find Patient through internal identifier list...
	 * @param identifiersList
	 * @return
	 * @throws ProcessException
	 */
	public List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList,  Error error) throws ProcessException;

	/**
	 * Find patient by organization id...
	 * @param identifiersList
	 * @param orgID
	 * @return
	 * @throws ProcessException
	 */
	public List<IntegrationPatientType> findPatientsByIdInOrganization(List<ExternalIdentifierType> identifiersList,long orgID, Error error) throws ProcessException;

	/**
	 * Create/Admit patient operation ...
	 * @param messagePatient
	 * @return
	 * @throws ProcessException
	 * @throws NoSuchUserFault
	 * @throws PatientNotActiveFault
	 * @throws UserNotActiveFault
	 * @throws UserNotAssignableFault
	 * @throws DuplicatePatientFault
	 * @throws InvalidDataFault
	 * @throws PatientAlreadyEnrolledFault
	 * @throws RemoteServiceFault
	 * @throws PatientNotPausedFault
	 */
	public IntegrationPatientType admitPatient(IntegrationPatientType messagePatient, Error error) throws ProcessException, NoSuchUserFault, PatientNotActiveFault, UserNotActiveFault, UserNotAssignableFault, DuplicatePatientFault, InvalidDataFault, PatientAlreadyEnrolledFault, RemoteServiceFault, PatientNotPausedFault;

	/**
	 * Pause Patient operation ...
	 * @param messagePatient
	 * @return
	 * @throws ProcessException
	 * @throws PatientNotActiveFault
	 */
	public IntegrationPatientType pausePatient(IntegrationPatientType messagePatient, Error error) throws ProcessException,PatientNotActiveFault;

	/**
	 * Update/Edit patient demographics...
	 * @param existingPatientsList
	 * @return
	 * @throws ProcessException
	 * @throws DuplicatePatientFault
	 * @throws InvalidDataFault
	 */
	public IntegrationPatientType updatePatientDemographics(List<IntegrationPatientType> existingPatientsList, Error error) throws ProcessException, DuplicatePatientFault, InvalidDataFault;

	/**
	 * Update CIA service with the user details...
	 * @param message
	 * @throws ProcessException
	 */
	public void updateServiceWithUserDetails(Object message, Error error) throws ProcessException ;

	/**
	 * Invoke notes service based on Note Id...
	 * @param unAckedNoteIdsToQuery
	 * @return
	 * @throws ResultsProcessException
	 */
	public FindNotesReply findNotes(List<EqualsFilterType> unAckedNoteIdsToQuery, Error error) throws ResultsProcessException;

	/**
	 * Invoke notes service based on Note Id for next page of notes ....
	 * @param unAckedNoteIdsToQuery
	 * @param pageNumber
	 * @return
	 * @throws ResultsProcessException
	 */
	public FindNotesReply findNextPageNotes(List<EqualsFilterType> unAckedNoteIdsToQuery, int pageNumber, int pageSize, Error error) throws ResultsProcessException;
}
