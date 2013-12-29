/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.dao;

import com.jprocessing.entities.Customer;

/**
 *
 * @author rumatoest
 */
public interface CustomerDao extends PersistenceDao<Long, Customer> {

    Customer getBySku(String sku);
}
