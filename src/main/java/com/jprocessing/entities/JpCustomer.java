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

import java.util.Set;

/**
 * Interface for compatible customer object outside JP schema.
 * If you want connect your customers from your database to JP,
 * then your customer entity (POJO) class should inherit this interface.
 *
 * @author rumatoest
 */
public interface JpCustomer {

    /**
     * Return customer billing Id (may be same value as primary key).
     * All billing processing will be connected to this value.
     * This Id should never be changed.
     */
    Long getBillingId();

    /**
     * Check if provided unencrypted password match internal customer's password hash.
     */
    public boolean checkPassword(String password);

    /**
     * Should return true if customer account is active
     * or false if customer disabled
     */
    public boolean isActive();

    /**
     * Should return customer primary email.
     * This email should be used for significant system notifications.
     * Email length up to 32 characters.
     */
    public String getEmail();

    /**
     * Return code names of the customers groups.
     * This code names have to be as primary keys, we will not expect
     * them to be changed.
     * Group names have to match next pattern "[A-Z]+[0-9A-Z_]*[0-9A-Z]+".
     *
     * @return Set of groups or empty Set
     */
    public Set<String> getGroups();

    /**
     * Return customer's login.
     * Login length up to 32 characters.
     */
    public String getLogin();

    /**
     * Generates short string to identify customer up to 128 characters.
     * I'm recommending to use next patterns:
     * "LastName Name (Company)" or "LastName Name" for individuals.
     */
    public String getShortInfo();

}
