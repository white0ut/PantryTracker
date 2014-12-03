package com.whiteout.pantrytracker.data.model;

import java.sql.Timestamp;

/**
 * Author:  Kendrick Cline
 * Date:    12/2/14
 * Email:   kdecline@gmail.com
 */
public class Item {

    private Long   id;
    private String name;
    private Long   expiration;
    private Float  quantity;
    private String unit;

    public Item() {}

    public Item(Long id,
                String name,
                Long expiration,
                Float quantity,
                String unit) {
        this.id         = id;
        this.name       = name;
        this.expiration = expiration;
        this.quantity   = quantity;
        this.unit       = unit;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration.getTime();
    }
    public void setExpiration(Long timeSinceEpoch) {
        this.expiration = timeSinceEpoch;
    }
    public Long getExpiration() {
        return this.expiration;
    }
    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }
    public Float getQuantity() {
        return this.quantity;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return this.unit;
    }
}
