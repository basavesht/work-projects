package com.bosch.tmp.integration.serviceReference.cia;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;

import com.bosch.th.cia.AdmitPatient;
import com.bosch.th.cia.AdmitPatientReply;
import com.bosch.th.cia.CiaPortType;
import com.bosch.th.cia.DuplicatePatientFault;
import com.bosch.th.cia.FindNotes;
import com.bosch.th.cia.FindNotesReply;
import com.bosch.th.cia.FindPatientsByIdInOrganization;
import com.bosch.th.cia.FindPatientsByIdInOrganizationReply;
import com.bosch.th.cia.FindResults;
import com.bosch.th.cia.FindResultsReply;
import com.bosch.th.cia.GetExternalOrganizations;
import com.bosch.th.cia.GetExternalOrganizationsReply;
import com.bosch.th.cia.GetPatients;
import com.bosch.th.cia.GetPatientsReply;
import com.bosch.th.cia.InsufficientDataFault;
import com.bosch.th.cia.InvalidDataFault;
import com.bosch.th.cia.InvalidFilterFault;
import com.bosch.th.cia.InvalidNextChunkFault;
import com.bosch.th.cia.NoSuchPatientFault;
import com.bosch.th.cia.NoSuchUserFault;
import com.bosch.th.cia.OptimisticLockingFault;
import com.bosch.th.cia.OrganizationNotActiveFault;
import com.bosch.th.cia.PatientAlreadyEnrolledFault;
import com.bosch.th.cia.PatientNotActiveFault;
import com.bosch.th.cia.PatientNotPausedFault;
import com.bosch.th.cia.PausePatient;
import com.bosch.th.cia.PausePatientReply;
import com.bosch.th.cia.RemoteServiceFault;
import com.bosch.th.cia.ResourceNotFoundFault;
import com.bosch.th.cia.UpdatePatientDemographics;
import com.bosch.th.cia.UpdatePatientDemographicsReply;
import com.bosch.th.cia.UserNotActiveFault;
import com.bosch.th.cia.UserNotAssignableFault;
import com.bosch.th.cia.UserNotAuthorizedFault;
import com.bosch.th.externalorganization.OrganizationType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.tmp.cl.basictypes.IdentifierType;
import com.bosch.tmp.cl.results.IdFilter;
import com.bosch.tmp.cl.results.ResultId;
import com.bosch.tmp.cl.task.EqualsFilterType;
import com.bosch.tmp.cl.task.OrFilterType;
import com.bosch.tmp.integration.process.ProcessException;
import com.bosch.tmp.integration.process.results.ResultsProcessException;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * Proxy class to invoke WS operations defined in CL CIA service..
 * @author BEA2KOR
 *
 */
@Service
public class CIAServiceProxy implements CIAService
{
	public static final Logger logger = LoggerFactory.getLogger(CIAServiceProxy.class);

	@Autowired
	protected CiaPortType ciaService;

	@Override
	public void updateServiceWithUserDetails(Object message, Error error) throws ProcessException
	{
		try
		{
			String username = MessageUtils.getUserName(message);
			String password = MessageUtils.getPassword(message);
			((BindingProvider) getCiaService()).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
			((BindingProvider) getCiaService()).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		}
		catch (RemoteServiceFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("Remote Service Error during update User Details for CIA service proxy during Result processing :" + error.getError().getFaultInternalMessage());
		}
		catch (Exception ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.GENERAL_ERROR.toString()));
			throw new ProcessException("Runtime exception encountered during Update User Details for CIA service proxy during Result processing :", ex);
		}
	}

	@Override
	public FindResultsReply findResults(List<ResultId> resultIds, Error error) throws ResultsProcessException
	{
		//Find Results reply processing...
		FindResultsReply findResultsReply = new FindResultsReply();
		if (null != resultIds)
		{
			//Creating and mapping results IdFilter instances into FindResults request..
			FindResults findResults = new FindResults();
			IdFilter idFilter = new IdFilter();
			idFilter.getId().addAll(resultIds);
			findResults.setFilter(idFilter);
			try
			{
				//Invoking actual service..
				logger.debug("Invoking CIA's findResults operation");
				findResultsReply = getCiaService().findResults(findResults);
			}
			catch (RemoteAccessException re) {
				logger.error(" RemoteAccessException : ", re);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
				throw new ResultsProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
			}
			catch (UserNotAuthorizedFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
				throw new ResultsProcessException("User is not authorized to query results in the system" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidFilterFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("The filter to find results provided is invalid" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidNextChunkFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("Invalid next chunk error" + error.getError().getFaultInternalMessage());
			}

			//Log the service response...
			if (logger.isDebugEnabled()){
				doMarshal(findResultsReply);
			}
		}
		return findResultsReply;
	}

	@Override
	public FindResultsReply findNextChunkResults(List<ResultId> resultIds,Long chunkItemId, Error error) throws ResultsProcessException
	{
		//Find Results reply processing...
		FindResultsReply findResultsReply = new FindResultsReply();
		if (null != resultIds)
		{
			//Creating and mapping results IdFilter instances into FindResults request..
			FindResults findResults = new FindResults();
			IdFilter idFilter = new IdFilter();
			idFilter.getId().addAll(resultIds);
			findResults.setFilter(idFilter);

			//Setting query information for next chunk from previous response in the FindResults request..
			com.bosch.tmp.cl.results.QueryInfo queryInfo = new com.bosch.tmp.cl.results.QueryInfo();
			queryInfo.setLastSeenItemId(chunkItemId);
			findResults.setQueryInfo(queryInfo);

			try
			{
				//Invoking actual service..
				logger.debug("Invoking CIA's findResults operation for next chunk");
				findResultsReply = getCiaService().findResults(findResults);
			}
			catch (RemoteAccessException re) {
				logger.error(" RemoteAccessException : ", re);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
				throw new ResultsProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
			}
			catch (UserNotAuthorizedFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
				throw new ResultsProcessException("User is not authorized to query results in the system" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidFilterFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("The filter to find results provided is invalid" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidNextChunkFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("Invalid next chunk error" + error.getError().getFaultInternalMessage());
			}

			//Log the service response...
			if (logger.isDebugEnabled()){
				doMarshal(findResultsReply);
			}
		}
		return findResultsReply;
	}

	@Override
	public List<OrganizationType> getExternalOrganizations(String orgId, String customerId, Error error) throws ProcessException
	{
		GetExternalOrganizationsReply getExternalOrganizationsReply = new GetExternalOrganizationsReply();
		GetExternalOrganizations getExternalOrganizations = new GetExternalOrganizations();
		getExternalOrganizations.setOrganizationIdentifier(orgId);
		getExternalOrganizations.setOrganizationType(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.ORGANIZATION_TYPE.toString()));
		try
		{
			logger.info("Invoking getExternalOrganizations operation from CIA with Customer Id: " + customerId + "and Organization Id: " + orgId);
			getExternalOrganizationsReply = getCiaService().getExternalOrganizations(getExternalOrganizations);
		}
		catch (RemoteAccessException re){
			logger.error(" RemoteAccessException : ", re);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(" UserNotAuthorizedFault : ", ex);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to get external organizations given org id and customer id" + error.getError().getFaultInternalMessage());
		}
		catch (ResourceNotFoundFault ex){
			logger.error(" ResourceNotFoundFault : ", ex);
			throw new ProcessException("Resource is not found by the search string, org id", ex);
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(getExternalOrganizationsReply);
		}

		return getExternalOrganizationsReply.getExternalOrganization();
	}

	@Override
	public List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList,Long internalIdentifier,Error error) throws ProcessException
	{
		GetPatientsReply getPatientsReply = new GetPatientsReply();
		GetPatients getPatients = new GetPatients();
		IdentifierType identifierType = new IdentifierType();
		identifierType.getExternalIdentifier().addAll(identifiersList);
		identifierType.setInternalIdentifier(internalIdentifier);
		getPatients.setIntegrationPatient(identifierType);
		try
		{
			logger.debug("Invoking CIA's findPatients operation");
			getPatientsReply = getCiaService().getPatients(getPatients);
		}
		catch (RemoteAccessException re){
			logger.error(" RemoteAccessException : ", re);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to find patients in the system" + error.getError().getFaultInternalMessage());
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(getPatientsReply);
		}

		return getPatientsReply.getIntegrationPatient();
	}

	@Override
	public List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList, Error error) throws ProcessException
	{
		GetPatientsReply getPatientsReply = new GetPatientsReply();
		GetPatients getPatients = new GetPatients();
		IdentifierType identifierType = new IdentifierType();
		identifierType.getExternalIdentifier().addAll(identifiersList);
		getPatients.setIntegrationPatient(identifierType);
		try
		{
			logger.debug("Invoking CIA's findPatients operation");
			getPatientsReply = getCiaService().getPatients(getPatients);
		}
		catch (RemoteAccessException re){
			logger.error(" RemoteAccessException : ", re);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to find patients in the system" + error.getError().getFaultInternalMessage());
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(getPatientsReply);
		}

		return getPatientsReply.getIntegrationPatient();
	}

	@Override
	public List<IntegrationPatientType> findPatientsByIdInOrganization(List<ExternalIdentifierType> identifiersList,long orgID, Error error) throws ProcessException
	{
		FindPatientsByIdInOrganizationReply findPatientsReply = new FindPatientsByIdInOrganizationReply();
		FindPatientsByIdInOrganization findPatients = new FindPatientsByIdInOrganization();
		IdentifierType identifierType = new IdentifierType();
		identifierType.getExternalIdentifier().addAll(identifiersList);
		identifierType.setInternalIdentifier(new Long(orgID));
		findPatients.setIntegrationPatient(identifierType);
		findPatients.setOrganizationId(orgID);
		try
		{
			logger.debug("Invoking CIA's findPatientsByIdInOrganization operation");
			findPatientsReply = getCiaService().findPatientsByIdInOrganization(findPatients);
		}
		catch (RemoteAccessException re)
		{
			logger.error(" RemoteAccessException : ", re);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex)
		{
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to find patients in the system" + error.getError().getFaultInternalMessage());
		}
		catch (InsufficientDataFault inSufficent)
		{
			logger.error(inSufficent.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
			throw new ProcessException("Insufficient Data to find patients in the system" + error.getError().getFaultInternalMessage());

		}
		catch (NoSuchPatientFault nosuchPatient)
		{
			logger.error(nosuchPatient.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("Patient Not Found in the system" + error.getError().getFaultInternalMessage());
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(findPatientsReply);
		}

		return findPatientsReply.getIntegrationPatient();
	}

	@Override
	public IntegrationPatientType admitPatient(IntegrationPatientType messagePatient, Error error) throws ProcessException,NoSuchUserFault, PatientNotActiveFault, UserNotActiveFault, UserNotAssignableFault, DuplicatePatientFault,InvalidDataFault, PatientAlreadyEnrolledFault, RemoteServiceFault, PatientNotPausedFault
	{
		AdmitPatientReply admitPatientReply = new AdmitPatientReply();
		AdmitPatient admitPatient = new AdmitPatient();
		admitPatient.setIntegrationPatient(messagePatient);
		try
		{
			logger.debug("Invoking CIA's admit operation");
			admitPatientReply = getCiaService().admitPatient(admitPatient);
		}
		catch (RemoteServiceFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("Remote Service Error: " + error.getError().getFaultInternalMessage());
		}
		catch (NoSuchPatientFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("Patient with external ids is not found in the system: " + error.getError().getFaultInternalMessage());
		}
		catch (ResourceNotFoundFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
			throw new ProcessException("Required resource to admit/update patient is not found" + error.getError().getFaultInternalMessage());
		}
		catch (InsufficientDataFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
			throw new ProcessException("Data provided to admit/update patient is insufficient" + error.getError().getFaultInternalMessage());
		}
		catch (OptimisticLockingFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
			throw new ProcessException("Another thread is trying to operate on the same DB record" + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to admit/update the patient" + error.getError().getFaultInternalMessage());
		}
		catch (OrganizationNotActiveFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_ACTIVE.toString()));
			throw new ProcessException("Patient's signup organization is not active", ex);
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(admitPatientReply);
		}

		return admitPatientReply.getIntegrationPatient();
	}

	@Override
	public IntegrationPatientType pausePatient(IntegrationPatientType messagePatient, Error error) throws ProcessException,
	PatientNotActiveFault
	{
		PausePatientReply pausePatientReply = new PausePatientReply();
		try
		{
			logger.debug("Invoking CIA's pause patient operation");
			PausePatient pausePatient = new PausePatient();
			pausePatient.setIntegrationPatient(messagePatient);
			pausePatientReply = getCiaService().pausePatient(pausePatient);
		}
		catch (RemoteServiceFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("Remote Service Error: " + error.getError().getFaultInternalMessage());
		}
		catch (NoSuchPatientFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("Patient with external ids is not found in the system: " + error.getError().getFaultInternalMessage());
		}
		catch (ResourceNotFoundFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
			throw new ProcessException("Required resource to admit/update patient is not found" + error.getError().getFaultInternalMessage());
		}
		catch (InsufficientDataFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
			throw new ProcessException("Data provided to admit/update patient is insufficient" + error.getError().getFaultInternalMessage());
		}
		catch (OptimisticLockingFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
			throw new ProcessException("Another thread is trying to operate on the same DB record" + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to admit/update the patient" + error.getError().getFaultInternalMessage());
		}
		catch (InvalidDataFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
			throw new ProcessException("The data provided to pause a patient is invalid" + error.getError().getFaultInternalMessage());
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(pausePatientReply);
		}

		return pausePatientReply.getIntegrationPatient();
	}

	@Override
	public IntegrationPatientType updatePatientDemographics(List<IntegrationPatientType> existingPatientsList, Error error) throws
	ProcessException, DuplicatePatientFault, InvalidDataFault
	{
		UpdatePatientDemographicsReply updatePatientDemographicsReply = new UpdatePatientDemographicsReply();
		UpdatePatientDemographics updatePatientDemographics = new UpdatePatientDemographics();
		updatePatientDemographics.getIntegrationPatient().addAll(existingPatientsList);
		try
		{
			logger.debug("Invoking CIA's updatePatientDemographics operation");
			updatePatientDemographicsReply = getCiaService().updatePatientDemographics(updatePatientDemographics);
		}
		catch (RemoteServiceFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
			throw new ProcessException("Remote Service Error: " + error.getError().getFaultInternalMessage());
		}
		catch (NoSuchPatientFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("Patient with external ids is not found in the system: " + error.getError().getFaultInternalMessage());
		}
		catch (DuplicatePatientFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DUPLICATE_SSN.toString()));
			throw new ProcessException("Patient with same SSN already exists " + error.getError().getFaultInternalMessage());
		}
		catch (ResourceNotFoundFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
			throw new ProcessException("Required resource to admit/update patient is not found" + error.getError().getFaultInternalMessage());
		}
		catch (InsufficientDataFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
			throw new ProcessException("Data provided to admit/update patient is insufficient" + error.getError().getFaultInternalMessage());
		}
		catch (OptimisticLockingFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
			throw new ProcessException("Another thread is trying to operate on the same DB record" + error.getError().getFaultInternalMessage());
		}
		catch (UserNotAuthorizedFault ex){
			logger.error(ex.getMessage());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
			throw new ProcessException("User is not authorized to admit/update the patient" + error.getError().getFaultInternalMessage());
		}

		//Log the service response...
		if (logger.isDebugEnabled()){
			doMarshal(updatePatientDemographicsReply);
		}

		return updatePatientDemographicsReply.getIntegrationPatient();
	}

	@Override
	public FindNotesReply findNotes(List<EqualsFilterType> noteIds, Error error) throws ResultsProcessException
	{
		//Find Notes reply processing...
		FindNotesReply findNotesReply = new FindNotesReply();
		if (null != noteIds)
		{
			//Create notes filter...
			OrFilterType notesFilter = new OrFilterType();
			notesFilter.getChildFilters().addAll(noteIds);

			//Setting query information for next chunk from previous response in the FindResults request..
			com.bosch.tmp.cl.task.QueryInfo queryInfo = new com.bosch.tmp.cl.task.QueryInfo();
			queryInfo.setPageNumber(1);
			queryInfo.setPageSize(noteIds.size());

			//Set the notes filter into FindNotes input...
			FindNotes findNotes = new FindNotes();
			findNotes.setFilter(notesFilter);
			findNotes.setQueryInfo(queryInfo);
			try
			{
				//Invoking actual service..
				logger.debug("Invoking CIA's findNotes operation");
				findNotesReply = getCiaService().findNotes(findNotes);
			}
			catch (RemoteAccessException re) {
				logger.error(" RemoteAccessException : ", re);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
				throw new ResultsProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
			}
			catch (UserNotAuthorizedFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
				throw new ResultsProcessException("User is not authorized to query results in the system" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidFilterFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("The filter to find results provided is invalid" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidNextChunkFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("Invalid next chunk error" + error.getError().getFaultInternalMessage());
			}

			//Log the service response...
			if (logger.isDebugEnabled()){
				doMarshal(findNotesReply);
			}
		}
		return findNotesReply;
	}

	@Override
	public FindNotesReply findNextPageNotes(List<EqualsFilterType> noteIds, int pageNumber, int pageSize, Error error) throws ResultsProcessException
	{
		//Find Notes reply processing...
		FindNotesReply findNotesReply = new FindNotesReply();
		if (null != noteIds)
		{
			//Creating and mapping results IdFilter instances into FindNotes request..

			OrFilterType notesFilter = new OrFilterType();
			notesFilter.getChildFilters().addAll(noteIds);

			//Setting query information for next chunk from previous response in the FindResults request..
			com.bosch.tmp.cl.task.QueryInfo queryInfo = new com.bosch.tmp.cl.task.QueryInfo();
			queryInfo.setPageNumber(pageNumber);
			queryInfo.setPageSize(pageSize);

			//Set the notes filter into FindNotes input..
			FindNotes findNotes = new FindNotes();
			findNotes.setFilter(notesFilter);
			findNotes.setQueryInfo(queryInfo);

			try
			{
				//Invoking actual service..
				logger.debug("Invoking CIA's findResults operation for next chunk");
				findNotesReply = getCiaService().findNotes(findNotes);
			}
			catch (RemoteAccessException re) {
				logger.error(" RemoteAccessException : ", re);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
				throw new ResultsProcessException("User Authentication Failed : " + error.getError().getFaultInternalMessage());
			}
			catch (UserNotAuthorizedFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
				throw new ResultsProcessException("User is not authorized to query results in the system" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidFilterFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("The filter to find results provided is invalid" + error.getError().getFaultInternalMessage());
			}
			catch (InvalidNextChunkFault ex) {
				logger.error(ex.getMessage());
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
				throw new ResultsProcessException("Invalid next chunk error" + error.getError().getFaultInternalMessage());
			}

			//Log the service response...
			if (logger.isDebugEnabled()){
				doMarshal(findNotesReply);
			}
		}
		return findNotesReply;
	}

	/**
	 * Marshal the response for logging...
	 * @param obj
	 */
	private void doMarshal(Object obj)
	{
		logger.info("Marshalling the request...");
		JAXBContext jc = null;
		try
		{
			jc = JAXBContext.newInstance("com.bosch.th.cia");
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter stringWriter = new StringWriter();
			m.marshal(obj, stringWriter);
			logger.info(stringWriter.toString());
		}
		catch (JAXBException ex) {
			logger.error("Caught Exception while marshalling the request!!", ex);
		}
	}

	public CiaPortType getCiaService() {
		return ciaService;
	}

	public void setCiaService(CiaPortType ciaService) {
		this.ciaService = ciaService;
	}
}
