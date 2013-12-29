/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
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

    @Column(name = "description", length = 255)
    private String description;

    /**
     * Return product description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set product verbose description. Not necessary field. Up to 255
     * characters.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
