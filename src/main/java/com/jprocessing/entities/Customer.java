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
 * This class
 *
 * @author rumatoest
 */
public interface Customer extends JpEntity {

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
     */
    public String getEmail();

    /**
     * Return code names of the customers groups.
     * This code names have to be as primary keys, we will not expect
     * them to be changed.
     * Group names have to match next pattern "[0-9A-Z-_]+".
     * Only uppercase letters allowed.
     *
     * @return List of groups or empty List
     */
    public Set<String> getGroups();

    /**
     * Return customer's login
     */
    public String getLogin();

    /**
     * Generates short string to identify customer up to 128 characters.
     * I'm recommending to use next patterns:
     * "LastName Name (Company)" or "LastName Name" for individuals.
     */
    public String getShortInfo();

}
