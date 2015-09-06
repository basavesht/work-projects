package com.bosch.tmp.integration.persistence;

import java.util.List;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
public class ExternalOrganization implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final Logger logger = LoggerFactory.getLogger(ExternalOrganization.class);

    @PersistenceContext
    transient EntityManager entityManager;

    // Primary key of this entity.
    // For VA, it's the the facility code.
    @Id
    private String id;

    private String dns;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDns()
    {
        return dns;
    }

    public void setDns(String dns)
    {
        this.dns = dns;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof ExternalOrganization))
        {
            return false;
        }
        ExternalOrganization other = (ExternalOrganization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.bosch.tmp.integration.persistence.ExternalOrganization[id=" + id + "]";
    }

    @Transactional
    public void persist()
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
            ExternalOrganization attached = this.entityManager.find(ExternalOrganization.class, this.id);
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
        ExternalOrganization merged = this.entityManager.merge(this);
        this.entityManager.flush();
        this.id = merged.getId();
    }

    public static final EntityManager entityManager()
    {
        EntityManager em = new ExternalOrganization().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countExternalOrganizations()
    {
        return (Long) entityManager().createQuery("select count(o) from ExternalOrganization o").getSingleResult();
    }

    public static List<ExternalOrganization> findAllExternalOrganizations()
    {
        return entityManager().createQuery("select o from ExternalOrganization o").getResultList();
    }

    public static ExternalOrganization findExternalOrganization(String id)
    {
        if (id == null) return null;
        return entityManager().find(ExternalOrganization.class, id);
    }

    public static List<ExternalOrganization> findExternalOrganizationEntries(int firstResult, int maxResults)
    {
        return entityManager().createQuery("select o from ExternalOrganization o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ExternalOrganization> findExternalOrganizationsByDns(String dns)
    {
        if (dns == null) throw new IllegalArgumentException("The dns argument is required");
        EntityManager em = ExternalOrganization.entityManager();
        Query q = em.createQuery("select ExternalOrganization from ExternalOrganization as ExternalOrganization where ExternalOrganization.dns = :dns");
        q.setParameter("dns", dns);
        return q.getResultList();
    }

}
