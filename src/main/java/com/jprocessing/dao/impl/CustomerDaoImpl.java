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

import com.jprocessing.dao.CustomerDao;
import com.jprocessing.entities.Customer;
import com.jprocessing.entities.JpCustomer;
import java.util.Calendar;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * @see CustomerDao
 *
 * @author rumatoest
 */
public class CustomerDaoImpl extends JpaDaoImpl<Long, Customer> implements CustomerDao {

    @Inject
    public CustomerDaoImpl(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Customer getOrCreate(JpCustomer jpc) {
        Customer c = getByPk(jpc.getBillingId());

        if (c == null) {
            c = new Customer(jpc);
            persist(c);
            return c;
        }
        Calendar updateCheck = Calendar.getInstance();
        updateCheck.add(Calendar.MONTH, -1);
        if (c.getUpdated().before(updateCheck)) {
            c.refresh(jpc);
            merge(c);
        }

        return c;
    }

    @Override
    public Customer fetchRelated(Customer entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
