package com.example.recipefinder;

public class Recipe {

    private String name;
    private String url;
    private int ID;
    private int missingIngredients;

    public Recipe(String name, String url, int ID, int missingIngredients){
        this.name = name;
        this.url = url;
        this.ID = ID;
        this.missingIngredients = missingIngredients;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    
    public int getId(){
        return ID;
    }
    
    public int getMissingIngredients(){
        return missingIngredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
