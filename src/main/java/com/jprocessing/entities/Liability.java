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
 * One accounting record can be linked with many liabilities in a case when it was created
 * based on invoice with many invoice items, because in this case each liability relates
 * to appropriate invoice item (product+quantity)
 *
 * Liability currency must be cached value from product or invoice item.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_liabilities")
public class Liability implements JpEntity<Long> {

    private static final long serialVersionUID = 5771313956504071187L;

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
     * Return timestamp when liability was created
     */
    public Calendar getCreateTime() {
        return createTime;
    }

    /**
     * Set liability creation time.
     * For initial use only!
     */
    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_time", nullable = true)
    private Calendar expireTime;

    /**
     * Return timestamp when this liability expired.
     * This is useful for services not physical products.
     */
    public Calendar getExpireTime() {
        return expireTime;
    }

    /**
     * Set time when this liability will be expired.
     * Useful for service providing.
     */
    public void setExpireTime(Calendar expireTime) {
        this.expireTime = expireTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounting_id", nullable = true)
    private Accounting accountig;

    /**
     * Get associated accounting record.
     * One accounting record can be linked with many liabilities in a case when it was created
     * based on invoice with many invoice items, because in this case each liability relates
     * to appropriate invoice item (product+quantity)
     */
    public Accounting getAccountig() {
        return accountig;
    }

    /**
     * Set associated accounting record.
     * One accounting record can be linked with many liabilities in a case when it was created
     * based on invoice with many invoice items, because in this case each liability relates
     * to appropriate invoice item (product+quantity)
     */
    public void setAccountig(Accounting accountig) {
        this.accountig = accountig;
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

    @Column(name = "product_price", precision = 2, nullable = false)
    public BigDecimal productPrice;

    /**
     * Get price per product single unit.
     * Value at the moment when invoice or liability (if no invoice link) was created.
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    /**
     * Set price per product single unit.
     * Value at the moment when invoice or liability (if no invoice link) was created.
     */
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    /**
     * Get currency for product unit at moment when invoice or liability
     * (if no invoice link) was created.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set currency for product unit at moment when invoice or liability
     * (if no invoice link) was created.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "quantity", precision = 4, nullable = false)
    public BigDecimal quantity;

    /**
     * Get product (service) quantity for this liability
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Set product (service) quantity for this liability.
     * Stores value in DB with precision = 4
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * Set product (service) quantity for this liability
     * Stores value in DB with precision = 4
     */
    public void setQuantity(double quantity) {
        this.quantity = BigDecimal.valueOf(quantity).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    @Column(name = "price_total", nullable = false)
    public BigDecimal priceTotal;

    /**
     * Return total price for ordered product quantity.
     * Usually this is cached value from Accounting record.
     */
    public BigDecimal getPriceTotal() {
        return priceTotal;
    }

    /**
     * Set total price for ordered product quantity.
     * Usually this is cached value from Accounting record.
     */
    public void setPriceTotal(BigDecimal priceTotal) {
        this.priceTotal = priceTotal.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

}
