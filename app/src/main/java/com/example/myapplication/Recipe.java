package com.example.myapplication;

public class Recipe {

    private int id;
    private String name;
    private String description;
    private String instructions;

    private int matchedIngredients;
    private int totalIngredients;
    private double matchPercentage;

    public Recipe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getMatchedIngredients() {
        return matchedIngredients;
    }

    public void setMatchedIngredients(int matchedIngredients) {
        this.matchedIngredients = matchedIngredients;
    }

    public int getTotalIngredients() {
        return totalIngredients;
    }

    public void setTotalIngredients(int totalIngredients) {
        this.totalIngredients = totalIngredients;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
}
