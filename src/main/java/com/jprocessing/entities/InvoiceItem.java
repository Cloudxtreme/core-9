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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents invoice item.
 * Link product, product unit price (at the specific time) and quantity with invoice.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_invoice_items")
public class InvoiceItem implements JpEntity<Long> {

    private static final long serialVersionUID = -4696947672576523844L;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * Return invoice this item belongs to
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Set invoice this item belongs to
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
     * Value at the moment when this entity was created.
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    /**
     * Set price per product single unit.
     * Value at the moment when this entity was created.
     */
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    /**
     * Get currency for product price
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set currency code for product price
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "quantity", precision = 4, nullable = false)
    public BigDecimal quantity;

    /**
     * Get product (service) quantity to order
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Set ordered product (service) quantity
     * Stores value in DB with precision = 4
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * Set ordered product (service) quantity
     * Stores value in DB with precision = 4
     */
    public void setQuantity(double quantity) {
        this.quantity = BigDecimal.valueOf(quantity).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calculate total price (unit price * quantity).
     * Return value with precision = 2
     */
    public BigDecimal calculateTotalPrice() {
        BigDecimal total = getProductPrice().multiply(getQuantity());
        total.setScale(2, BigDecimal.ROUND_HALF_UP);
        return total;
    }
}
