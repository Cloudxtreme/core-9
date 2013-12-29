/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jprocessing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author rumatoest
 */
@Entity
@Table(name = "jp_credit_cards")
public class CreditCard implements JpEntity<Long> {

    @Id
    Long pk;

    @Override
    public Long getPk() {
        return pk;
    }

    @Override
    public void setPk(Long primaryKey) {
        this.pk = primaryKey;
    }

}
