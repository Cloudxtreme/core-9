/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.entities;

import java.io.Serializable;

/**
 * Common interface for all entities
 *
 * @author rumatoest
 */
public interface JpEntity<PK> extends Serializable {

    /**
     * Return entity primary key
     */
    PK getPk();

    /**
     * Set entity primary key
     */
    void setPk(PK primaryKey);
}
