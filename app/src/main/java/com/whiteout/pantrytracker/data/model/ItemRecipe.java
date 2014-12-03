package com.whiteout.pantrytracker.data.model;

/**
 * Author:  Kendrick Cline
 * Date:    12/2/14
 * Email:   kdecline@gmail.com
 */
public class ItemRecipe {

    private Long id;
    private Long itemId;
    private Long recipeId;

    public ItemRecipe() {}

    public ItemRecipe(Long id,
                      Long itemId,
                      Long recipeId) {
        this.id       = id;
        this.itemId   = itemId;
        this.recipeId = recipeId;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public Long getItemId() {
        return this.itemId;
    }
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
    public Long getRecipeId() {
        return this.recipeId;
    }
}
