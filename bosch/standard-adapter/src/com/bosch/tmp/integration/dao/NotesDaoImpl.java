package com.bosch.tmp.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.tmp.integration.entities.Notes;
import org.springframework.stereotype.Repository;

@Repository
public class NotesDaoImpl extends JpaDaoSupport implements NotesDao
{
	/**
	 * Sets the entity manager factory.
	 * @param entityManagerFactory the factory
	 */
	@Autowired
	public NotesDaoImpl(final EntityManagerFactory entityManagerFactory) {
		super();
		super.setEntityManagerFactory(entityManagerFactory);
	}

	@Transactional
	@Override
	public Notes save(Notes note) {
		getJpaTemplate().persist(note);
		return note;
	}

	@Transactional
	@Override
	public Notes update(Notes note) {
		return getJpaTemplate().merge(note);
	}

	@Override
	public Notes findById(long noteId) {
		return getJpaTemplate().find(Notes.class, noteId);
	}

	@Override
	public List<Notes> findNotesByOrganizationId(String organizationId,Boolean isAcknowledged) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Notes.findNotesByOrganizationId", params);
	}

	@Override
	public List<Notes> findNotesByNoteId(Long notesId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notesId", notesId);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Notes.findNotesByNoteId", params);
	}

	@Override
	public List<Notes> findNotesByNoteIdAndOrgId(Long notesId, String organizationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organizationId", organizationId);
		params.put("notesId", notesId);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Notes.findNotesByNoteIdAndOrgId", params);
	}

	@Override
	public List<Notes> findNotesByNoteId(String notesId, Boolean isAcknowledged) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isAcknowledged", isAcknowledged);
		params.put("notesId", Long.parseLong(notesId));
		return getJpaTemplate().findByNamedQueryAndNamedParams("Notes.findNotesByNoteIdAndAckFlag", params);
	}

}
