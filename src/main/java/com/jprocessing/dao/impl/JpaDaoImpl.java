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

import com.jprocessing.dao.JpaDao;
import com.jprocessing.entities.JpEntity;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rumatoest
 */
public abstract class JpaDaoImpl<PK extends Serializable, E extends JpEntity> implements JpaDao<PK, E> {

    private final EntityManagerFactory emf;

    private static final Logger logger = LoggerFactory.getLogger(JpaDaoImpl.class);

    /**
     * Subclass logger cache
     */
    private Logger loggerSubclass;

    Class<E> entityClass;

    protected JpaDaoImpl(EntityManagerFactory emf) {
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

    /**
     *
     * @param em
     * @return
     */
    public CriteriaTriple<CriteriaBuilder, CriteriaQuery<E>, Root<E>> initCriteriaQuery(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
        Root<E> root = cq.from(getEntityClass());
        return new CriteriaTriple<>(cb, cq, root);
    }

    @Override
    public void persist(E entity) throws EntityExistsException {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
        } catch (final Exception ex) {
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
        } catch (final Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void refresh(E entity) throws EntityNotFoundException {
        EntityManager em = createEntityManager();
        try {
            em.refresh(entity);
        } catch (final EntityNotFoundException ex) {
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
            E e = em.find(getEntityClass(), pk);
            if (e != null) {
                em.remove(e);
            }
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
    protected E getByRestriction(EntityManager em,
        Triple<CriteriaBuilder, CriteriaQuery<E>, Root<E>> queryTriple, Predicate... restrictions) {
        queryTriple.getMiddle()
            .select(queryTriple.getMiddle().from(getEntityClass()))
            .where(restrictions);
        Query q = em.createQuery(queryTriple.getMiddle());
        return (E)q.getSingleResult();
    }

    /**
     *
     * @param em Entity manager for query - Will be closed after method execution.
     * @param restriction JPA restriction from CriteriaBuilder
     */
    protected E getByRestrictionAndCloseEm(EntityManager em,
        Triple<CriteriaBuilder, CriteriaQuery<E>, Root<E>> queryTriple, Predicate... restrictions) {
        try {
            return getByRestriction(em, queryTriple, restrictions);
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
        Triple<CriteriaBuilder, CriteriaQuery<E>, Root<E>> queryTriple, Predicate... restrictions) {
        queryTriple.getMiddle()
            .select(queryTriple.getMiddle().from(getEntityClass()))
            .where(restrictions);
        Query q = em.createQuery(queryTriple.getMiddle());
        if (fetchSize != null) {
            q.setMaxResults(fetchSize);
        }
        if (offsetStart != null) {
            q.setFirstResult(offsetStart);
        }
        return q.getResultList();
    }

    /**
     *
     * @param em Entity manager for query - Will be closed after method execution.
     * @param restriction JPA restriction from CriteriaBuilder
     */
    protected List<E> findByRestrictionAndCloseEm(EntityManager em, Integer offsetStart, Integer fetchSize,
        Triple<CriteriaBuilder, CriteriaQuery<E>, Root<E>> queryTriple, Predicate... restrictions) {
        try {
            return findByRestriction(em, offsetStart, fetchSize, queryTriple, restrictions);
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
    protected Long getRowsCountByRestriction(EntityManager em, Expression<Boolean> restriction) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(getEntityClass()))).where(restriction);
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Immutable triple (QueryBuilder, CriteriaQuery, Root).
     * Contains all required objects to generate JPA queries.
     *
     * @param <B> Criteria builder
     * @param <Q> Criteria query based on root entity class
     * @param <R> Root for entity class
     */
    protected static class CriteriaTriple<B extends CriteriaBuilder, Q extends CriteriaQuery, R extends Root> extends Triple {

        private final B builder;

        private final Q query;

        private final R root;

        public CriteriaTriple(B builder, Q query, R root) {
            this.builder = builder;
            this.query = query;
            this.root = root;
        }

        @Override
        public B getLeft() {
            return this.builder;
        }

        @Override
        public Q getMiddle() {
            return this.query;
        }

        @Override
        public R getRight() {
            return this.root;
        }

        public B getBuilder() {
            return builder;
        }

        public Q getQuery() {
            return query;
        }

        public R getRoot() {
            return root;
        }
    }

}
