package com.whiteout.pantrytracker.data.model;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/14
 * Email:   kdecline@gmail.com
 */
public class RecipeSearch {

    private Float rating;
    private String id;
    private String recipeName;

    public RecipeSearch() {}

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public Float getRating() {
        return this.rating;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getRecipeName() {
        return this.recipeName;
    }
}
