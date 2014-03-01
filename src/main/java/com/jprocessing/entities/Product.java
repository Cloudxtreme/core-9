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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * The product is something what you want to sell. It can describe some physical
 * thing or service.
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_products",
    indexes = {
        @Index(columnList = "sku"),
        @Index(columnList = "is_deleted")})
public class Product implements JpEntity<Long> {

    private static final long serialVersionUID = 3598599915838510461L;

    /**
     * Describes subscription period type
     */
    public static enum Subscription {

        /**
         * Product has no subscription
         */
        NONE,
        /**
         * Product subscription period is measured in days
         */
        DAY,
        /**
         * Product subscription period is measured in weeks
         */
        WEEK,
        /**
         * Product subscription period is measured in months
         */
        MONTH,
        /**
         * Product subscription period is measured in years
         */
        YEAR,
    }

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

    @Column(name = "sku", length = 60, nullable = false, unique = true)
    private String sku;

    /**
     * Return SKU (unique string)
     */
    public String getSku() {
        return sku;
    }

    /**
     * Set SKU code. This is unique string up to 60 characters.
     */
    public void setSku(String productCode) {
        this.sku = productCode;
    }

    @Column(name = "is_fractional", nullable = false)
    private boolean fractional = false;

    /**
     * Determine if product quantity can be measured in fractional numbers.
     */
    public boolean isFractional() {
        return fractional;
    }

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    /**
     * Return true if product is deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Mark product as deleted.
     * Deleted product should be visible only for administrators.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Column(name = "is_available", nullable = false)
    private boolean avaiable = true;

    /**
     * Return true if product is available for purchase
     */
    public boolean isAvaiable() {
        return avaiable;
    }

    /**
     * Set product availability
     */
    public void setAvaiable(boolean avaiable) {
        this.avaiable = avaiable;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    private Subscription subscriptionType = Subscription.NONE;

    /**
     * Check if customer can subscribe on this product
     */
    public boolean hasSubscription() {
        return this.subscriptionType != Subscription.NONE;
    }

    /**
     * Return subscription period type.
     */
    public Subscription getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * Set subscription period type.
     */
    public void setSubscriptionType(Subscription subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    @Column(name = "subscription_period", nullable = false)
    private int subscriptionPeriod = 0;

    /**
     * Return subscription period.
     * Period value type is determined in subscriptionType attribute.
     */
    public int getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    /**
     * Set subscription period.
     * Period value type is determined in subscriptionType attribute.
     */
    public void setSubscriptionPeriod(int subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    /**
     * Determine if product quantity can be measured in fractional numbers.
     */
    public void setFractional(boolean fractionalQuantity) {
        this.fractional = fractionalQuantity;
    }

    @Column(name = "name", length = 90, nullable = false)
    private String name;

    /**
     * Return product verbose name
     */
    public String getName() {
        return name;
    }

    /**
     * Set product verbose name up to 90 characters
     */
    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @Column(name = "description")
    private String description;

    /**
     * Return product description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set product verbose description. Not necessary field.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
