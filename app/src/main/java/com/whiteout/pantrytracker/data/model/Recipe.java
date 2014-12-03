package com.whiteout.pantrytracker.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Kendrick Cline
 * Date:    12/2/14
 * Email:   kdecline@gmail.com
 */
public class Recipe {

    private Long id;
    private String name;
    private String directions;

    private List<Item> items;

    public Recipe() {}

    public Recipe(Long id,
                  String name,
                  String directions) {
        this.id         = id;
        this.name       = name;
        this.directions = directions;
    }


    public void setItems(List<Item> items) {
        this.items = items;
    }
    public List<Item> getItems() {
        return this.items;
    }
    public void addItem(Item i) {
        if (items == null)
            items = new ArrayList<Item>();
        items.add(i);
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
    public void setdirections(String directions) {
        this.directions = directions;
    }
    public String getDirections() {
        return this.directions;
    }
}
