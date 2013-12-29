/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.dao.impl;

import com.jprocessing.dao.PersistenceDao;
import com.jprocessing.entities.JpEntity;
import java.io.Serializable;

/**
 *
 * @author rumatoest
 */
public abstract class PersistenceDaoImpl<PK extends Serializable, E extends JpEntity> implements PersistenceDao<PK, E> {

}
