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

import com.jprocessing.utils.Enums;
import com.jprocessing.utils.Formatters;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_products_prices")
public class ProductPrice implements JpEntity<Long> {

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

    @Column(name = "priority", nullable = false)
    private int priority = 0;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Return product this price will applied to
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Connect price with concrete product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Return price priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set price priority.
     * Price with higher priority may override other prices with lower priority.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Column(name = "min_quantity", nullable = false)
    private double minQuantity = 0.0;

    /**
     * Return minimum product quantity to activate this price
     */
    public double getMinQuantity() {
        return minQuantity;
    }

    /**
     * Set minimum product quantity to activate this price.
     * Zero or negative value will apply this price for any product quantity.
     */
    public void setMinQuantity(double minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Column(name = "price_model", nullable = false)
    private String model;

    /**
     * Return price model
     */
    public Model getModel() {
        return Enums.getValueOf(Model.class, this.model);
    }

    /**
     * Set price model from enum
     */
    public void setModel(Model model) {
        this.model = model.toString();
    }

    /**
     * Set price model from string code.
     * This method is for internal usage only
     */
    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "value", nullable = false)
    private double value;

    /**
     * Get value for current price model
     */
    public double getValue() {
        return value;
    }

    /**
     * Set value for current price model.
     * This value is depends on model type.
     * Basically it should be a price for 1 quantity of product.
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Lob
    @Column(name = "groups", nullable = false)
    private String groups = "";

    private Set<String> groupsSet;

    /**
     * Return groups where this price model should be active.
     * Empty set mean apply to all groups.
     */
    public Set<String> getGroups() {
        if (this.groupsSet == null) {
            if (this.groups == null) {
                groupsSet = Collections.EMPTY_SET;
            }
            this.groupsSet = new HashSet<>(java.util.Arrays.asList(this.groups.split(",")));
        }
        return groupsSet;
    }

    /**
     * Set comma separated string with group names.
     * Preferred for system usage only.
     */
    public void setGroups(String groups) {
        this.groupsSet = null;
        this.groups = String.valueOf(groups).trim().toUpperCase();
    }

    /**
     * Set groups from Set collections.
     * This price model will apply only to provided groups.
     * Empty input groups will force price model to be applied for all groups.
     */
    public void setGroups(Set<String> groups) {
        if (groups == null) {
            groups = Collections.EMPTY_SET;
        }

        StringBuilder sb = new StringBuilder();

        for (String g : groups) {
            g = Formatters.trimUpper(g);
            if (g.isEmpty()) {
                continue;
            }
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(g);
        }
        setGroups(sb.toString());
    }

    /**
     * Return true if this pricing model is active for provided customer group
     * and product quantity.
     *
     * @param customerGroup If null - then will calculate for any group
     * @param quantity Product quantity in order
     */
    public boolean isActiveFor(String customerGroup, double quantity) {
        if (this.minQuantity > quantity) {
            return false;
        }

        if (getGroups().isEmpty()) {
            return true;
        }

        return getGroups().contains(customerGroup);
    }

    /**
     * Represents available pricing models.
     */
    public static enum Model {

        /**
         * Full price paid by the purchaser
         */
        FULL("F");

        private final String code;

        private Model(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.code;
        }
    }
}
