package com.bosch.tmp.integration.dao;

import java.util.List;

import com.bosch.tmp.integration.entities.Notes;

public interface NotesDao
{
	/**
	 * Persist notes (Patient/Survey)..
	 * @param note
	 * @return
	 */
	public Notes save(Notes note);

	/**
	 * Update notes (Patient/Survey)..
	 * @param note
	 * @return
	 */
	public Notes update(Notes note);

	/**
	 * Find Notes based on the unique id..
	 * @param noteId
	 * @return
	 */
	public Notes findById(long noteId);

	/**
	 * Find list of notes based on the organization id..
	 * @param organizationId
	 * @param isAcknowledged
	 * @return
	 */
	public List<Notes> findNotesByOrganizationId(String organizationId, Boolean isAcknowledged);

	/**
	 * Find notes based on note Id..
	 * @param notesId
	 * @return
	 */
	public List<Notes> findNotesByNoteId(Long notesId);

	/**
	 * Find Note by note id and organization id..
	 * @param notesId
	 * @param organizationId
	 * @return
	 */
	public List<Notes> findNotesByNoteIdAndOrgId(Long notesId, String organizationId);

	/**
	 * Find Note by only note id..
	 * @param notesId
	 * @param isAcknowledged
	 * @return
	 */
	public List<Notes> findNotesByNoteId(String notesId, Boolean isAcknowledged);
}
