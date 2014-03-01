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
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_invoices")
public class Invoice implements JpEntity<Long> {

    private static final long serialVersionUID = 5452682433225339426L;

    @Id
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private Calendar createTime;

    /**
     * Return timestamp when invoice was created
     */
    public Calendar getCreateTime() {
        return createTime;
    }

    /**
     * Set invoice creation time.
     * For initial use only!
     */
    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_time")
    private Calendar paymentTime;

    /**
     * Return timestamp when invoice was payed
     */
    public Calendar getPaymentTime() {
        return paymentTime;
    }

    /**
     * Set invoice payment time.
     */
    public void setPaymentTime(Calendar paymentTime) {
        this.paymentTime = paymentTime;
    }

    @OneToMany(mappedBy = "invoice",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    Set<InvoiceItem> items;

    /**
     * Return items (product links) for this invoice
     */
    public Set<InvoiceItem> getItems() {
        return items;
    }

    /**
     * Set related to invoice items
     */
    public void setItems(Set<InvoiceItem> items) {
        this.items = items;
    }

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    /**
     * Get total price for this invoice
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Set total price for this invoice
     */
    public void setTotal(BigDecimal amount) {
        this.total = amount;
    }

    /**
     * Will update total invoice price from total prices in attached invoice items.
     */
    public void updateTotal() {
        BigDecimal t = new BigDecimal(0);
        for (InvoiceItem i : getItems()) {
            t.add(i.calculateTotalPrice());
        }
        setTotal(t);
    }
}
