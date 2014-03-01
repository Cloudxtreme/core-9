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

import java.math.BigDecimal;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is log, that indicates finance income and outcome for customer's accounts.
 * Each customer account can receive money by paying real money via payment card or Internet money
 * system (TYPE_DEBIT). Customer use his balance to pay for product or service (TYPE_CREDIT).
 * Each credit record also creates liability related to product.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_accounting")
public class Accounting implements JpEntity<Long> {

    private static final long serialVersionUID = -5184193346979102514L;

    /**
     * Debit record type (any positive value).
     * Mean that customer account receiving money.
     */
    public static final int TYPE_DEBIT = 1;

    /**
     * This is summary report for an account.
     * It is result of sum debit and credit before SUMMARY record.
     */
    public static final int TYPE_SUMMARY = 0;

    /**
     * Credit record type (any negative value).
     * Mean that customer account withdrawing a money by ordering some products or services.
     */
    public static final int TYPE_CREDIT = -1;

    @Id
    @SequenceGenerator(name = "accountingPkSeq", sequenceName = "ACCOUNTING_PK_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountingPkSeq")
    @Column(name = "id")
    private Long pk;

    @Override
    public Long getPk() {
        return pk;
    }

    @Override
    public void setPk(Long primaryKey) {
        this.pk = primaryKey;
    }

    @Column(name = "record_type", nullable = false)
    private int recordType;

    /**
     * See TYPE_* constants
     */
    public int getRecordType() {
        return recordType;
    }

    /**
     * See TYPE_* constants
     */
    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Calendar timestamp;

    /**
     * Get record creation timestamp.
     */
    public Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * Set record creation timestamp.
     * Do not change timestamp manually.
     */
    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "amount", precision = 2, nullable = false)
    private BigDecimal amount;

    /**
     * Get Money transfer amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get money transfer amount.
     * Negative value for credit. Positive value for debit. Summary value can be positive
     * and negative as well.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

}
