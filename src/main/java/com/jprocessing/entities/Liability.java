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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity describes company liability for the customer.
 * This is Product (or service) that we have to provide. Each liability must be linked with
 * appropriate product entity and should be liked to accounting record which indicates payment
 * from customer internal account to company.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_liabilities")
public class Liability implements JpEntity<Long> {

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
    private Date createTime;

    /**
     * Return timestamp when liability was created
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Set liability creation time.
     * For initial use only!
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_time", nullable = true)
    private Date expireTime;

    /**
     * Return timestamp when this liability expired.
     * This is useful for services not physical products.
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * Set time when this liability will be expired.
     * Useful for service providing.
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Return product associated with current liability.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Set product related to this liability.
     * Note, that you should not change product link after liability was created.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "quantity", nullable = false)
    public double quantity;

    /**
     * Get product (service) quantity for this liability
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Set product (service) quantity for this liability
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "price_total", nullable = false)
    public double priceTotal;

    /**
     * Return total price for ordered product quantity.
     * Usually this is cached value from Accounting record.
     */
    public double getPriceTotal() {
        return priceTotal;
    }

    /**
     * Set total price for ordered product quantity.
     * Usually this is cached value from Accounting record.
     */
    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

}
