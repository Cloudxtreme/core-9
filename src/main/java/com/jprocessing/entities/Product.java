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
    indexes = @Index(columnList = "sku"))
public class Product implements JpEntity<Long> {

    /**
     * Product has no subscription
     */
    public static final int SUBSCRIPTION_NONE = 0;

    /**
     * Product subscription period is measured in days
     */
    public static final int SUBSCRIPTION_DAY = 1;

    /**
     * Product subscription period is measured in weeks
     */
    public static final int SUBSCRIPTION_WEEK = 2;

    /**
     * Product subscription period is measured in months
     */
    public static final int SUBSCRIPTION_MONTH = 3;

    @Id
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
    boolean fractional = false;

    /**
     * Determine if product quantity can be measured in fractional numbers.
     */
    public boolean isFractional() {
        return fractional;
    }

    @Column(name = "subscription_type", nullable = false)
    int subscriptionType = 0;

    /**
     * Check if customer can subscribe on this product
     */
    public boolean hasSubscription() {
        return this.subscriptionType == 0;
    }

    /**
     * Return subscription period type.
     * See SUBSCRIPTION_* constants.
     */
    public int getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * Set subscription period type.
     * See SUBSCRIPTION_* constants.
     */
    public void setSubscriptionType(int subscriptionType) {
        if (subscriptionType < SUBSCRIPTION_NONE
            || subscriptionType > SUBSCRIPTION_WEEK) {
            throw new IllegalArgumentException("Subscription type "
                + subscriptionType + " is not valid!");
        }
        this.subscriptionType = subscriptionType;
    }

    @Column(name = "subscription_period", nullable = false)
    int subscriptionPeriod = 0;

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
