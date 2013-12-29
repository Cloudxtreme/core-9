/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.dao;

import com.jprocessing.entities.JpEntity;
import java.io.Serializable;

/**
 *
 * @author rumatoest
 */
public interface PersistenceDao<PK extends Serializable, E extends JpEntity> {

    /**
     * Persist entity (create) to database
     */
    void persist(E entity);

    /**
     * Persist entity to database or merge it if entity already exist.
     */
    void persistOrMerge(E entity);

    /**
     * Merge the state of the given entity into the current persistence context.
     */
    void merge(E entity);

    /**
     * Refresh the state of the instance from the database, overwriting changes
     * made to the entity, if any.
     */
    void refresh(E entity);

    /**
     * Remove the entity instance from database.
     */
    void remove(E entity);

    /**
     * Remove the entity instance from database by primary key.
     */
    void remove(PK pk);

    E findByPk();

}
