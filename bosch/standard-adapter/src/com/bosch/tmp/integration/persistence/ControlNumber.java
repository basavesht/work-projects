package com.bosch.tmp.integration.persistence;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gtk1pal
 */
@Entity
@Configurable
@Transactional
public class ControlNumber implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final Logger logger = LoggerFactory.getLogger(ControlNumber.class);

    @PersistenceContext
    transient EntityManager entityManager;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CONTROL_NUMBER_GENERATOR")
    @SequenceGenerator(name = "CONTROL_NUMBER_GENERATOR", sequenceName = "control_number_seq")
    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Transactional
    public void persist() throws Exception
    {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove()
    {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this))
        {
            this.entityManager.remove(this);
        }
        else
        {
            ControlNumber attached = this.entityManager.find(ControlNumber.class, this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush()
    {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void merge()
    {
        if (this.entityManager == null) this.entityManager = entityManager();
        ControlNumber merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }

    public static final EntityManager entityManager()
    {
        EntityManager em = new ControlNumber().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

}
