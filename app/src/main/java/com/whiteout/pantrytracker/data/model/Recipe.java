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
    private String yummlyId;
    private String name;
    private String directions;
    private String yield;
    private String cookTime;

    private String foodDownloadURL;
    private String yummlyLogo;


    private List<Item> items;

    public Recipe() {}

    public Recipe(Long id,
                  String name,
                  String directions,
                  String yield,
                  String yummlyId) {
        this.id         = id;
        this.name       = name;
        this.directions = directions;
        this.yield      = yield;
        this.yummlyId   = yummlyId;
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
    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }
    public String getCookTime() {
        return this.cookTime;
    }
    public void setFoodDownload(String s) {
        this.foodDownloadURL = s;
    }
    public String getFoodDownloadURL() {
        return this.foodDownloadURL;
    }
    public void setYummlyLogo(String yummlyLogo) {
        this.yummlyLogo = yummlyLogo;
    }
    public String getYummlyLogo() {
        return this.yummlyLogo;
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
    public void setYield(String yield) {
        this.yield = yield;
    }
    public String getYield() {
        return this.yield;
    }
    public void setYummlyId(String yummlyId) {
        this.yummlyId = yummlyId;
    }
    public String getYummlyId() {
        return this.yummlyId;
    }
}
