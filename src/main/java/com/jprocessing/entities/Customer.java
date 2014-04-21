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
package com.jprocessing.entities;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Holds customer object cache in JP data scheme.
 * Used to link all related records via for foreign keys to customer.
 * This record should not be deleted in any way. If you will delete customer from
 * main database, than cache record in billing have to be disabled.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_customer")
public class Customer implements JpEntity<Long> {

    private static final long serialVersionUID = 1L;

    public Customer() {
    }

    public Customer(JpCustomer jpc) {
        this.pk = jpc.getBillingId();
        this.login = jpc.getLogin();
        this.email = jpc.getEmail();
        this.active = jpc.isActive();
        this.info = jpc.getShortInfo();
        this.updated = Calendar.getInstance();
    }

    public void refresh(JpCustomer jpc) {
        this.login = jpc.getLogin();
        this.email = jpc.getEmail();
        this.active = jpc.isActive();
        this.info = jpc.getShortInfo();
        this.updated = Calendar.getInstance();
    }

    @Id
    @Column(name = "id")
    private Long pk;

    /**
     * Return unique customer primary key.
     * This key should be the same as billingId value in JpCustomer
     *
     * @see JpCustomer
     */
    @Override
    public Long getPk() {
        return pk;
    }

    /**
     * Set customer cache primary key.
     *
     * @param primaryKey Related to billingId value from JpCustomer
     * @see JpCustomer
     */
    @Override
    public void setPk(Long primaryKey) {
        this.pk = primaryKey;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Calendar updated;

    /**
     * Get last time when this record data was updated
     */
    public Calendar getUpdated() {
        return updated;
    }

    /**
     * Renew entity updated time
     */
    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }

    @Column(name = "active")
    private boolean active = false;

    /**
     * Check if customer account is active.
     * All operations on inactive accounts should be blocked,
     * only incoming payments may be approved (just to log them).
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Update account activity status.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "login", length = 32, nullable = false)
    private String login;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "email", length = 32, nullable = false)
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "info")
    private String info;

    /**
     * Return short info about customer cache
     */
    public String getInfo() {
        return info;
    }

    /**
     * Update customer cache info
     */
    public void setInfo(String info) {
        this.info = info;
    }
}
