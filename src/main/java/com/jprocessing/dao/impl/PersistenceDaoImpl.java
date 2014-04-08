/*
 * Copyright (c) 2014 Vladislav Zablotsky
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jprocessing.dao.impl;

import com.jprocessing.dao.PersistenceDao;
import com.jprocessing.entities.JpEntity;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rumatoest
 */
public abstract class PersistenceDaoImpl<PK extends Serializable, E extends JpEntity> implements PersistenceDao<PK, E> {

    private final EntityManagerFactory emf;

    private static final Logger logger = LoggerFactory.getLogger(PersistenceDaoImpl.class);

    /**
     * Subclass logger cache
     */
    private Logger loggerSubclass;

    Class<E> entityClass;

    protected PersistenceDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
        this.entityClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Will init and return logger.
     * Should be used in subclasses.
     */
    protected Logger getLogger() {
        if (loggerSubclass == null) {
            loggerSubclass = LoggerFactory.getLogger(getClass());
        }
        return loggerSubclass;
    }

    /**
     * Return class of associated entity
     */
    protected Class<E> getEntityClass() {
        return this.entityClass;
    }

    /**
     * Return current entity manager factory
     */
    protected EntityManagerFactory getEmf() {
        return this.emf;
    }

    /**
     * Will create entity manager based on current entity manager factory.
     */
    protected EntityManager createEntityManager() {
        return getEmf().createEntityManager();
    }

    @Override
    public void persist(E entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void persistOrMerge(E entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void merge(E entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void refresh(E entity) {
        EntityManager em = createEntityManager();
        try {
            em.refresh(entity);
        } catch (EntityNotFoundException ex) {
            logger.warn("Can not regresh entity because it was not found in context " + entity);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(E entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(PK pk) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(em.find(getEntityClass(), pk));
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public E getByPk(PK pk) {
        EntityManager em = createEntityManager();
        try {
            return em.find(getEntityClass(), pk);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param em Entity manager for query - Will be closed after method execution.
     * @param restriction JPA restriction from CriteriaBuilder
     */
    protected List<E> findByRestriction(EntityManager em, Integer offsetStart, Integer fetchSize,
        Expression<Boolean> restriction) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
            cq.select(cq.from(getEntityClass())).where(restriction);
            Query q = em.createQuery(cq);
            if (fetchSize != null) {
                q.setMaxResults(fetchSize);
            }
            if (offsetStart != null) {
                q.setFirstResult(offsetStart);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Count all entities records.
     *
     * @return Not null
     */
    protected Long getRowsCount() {
        EntityManager em = createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(getEntityClass())));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Count entities records by specific restriction.
     *
     * @param em Entity manager for query - Will be closed after method execution.
     * @param restriction JPA restriction from CriteriaBuilder
     * @return Not null
     */
    protected Long getRowsCountBy(EntityManager em, Expression<Boolean> restriction) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(getEntityClass()))).where(restriction);
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

}
