package io.github.team2;

public class Nutrition {
	private int carbohydrates;
    private int proteins;
    private int fats;
    private int calories;

    public Nutrition(int carbohydrates, int proteins, int fats, int calories) {
    	this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.calories = calories;
    }

    public int getCarbohydrates() {
    	return carbohydrates;
    }

    public int getProteins() {
    	return proteins;
    }
    
    public int getFats() {
    	return fats;
    }
    
    public int getCalories() {
    	return calories;
    }
    
    public void modifyCarbohydrates(int amount) {
    	this.carbohydrates += amount;
    }
    
    public void modifyProteins(int amount) {
    	this.proteins += amount;
    }
    
    public void modifyFats(int amount) {
    	this.fats += amount;
    }
    
    public void modifyCalories(int amount) {
    	this.calories += amount;
    }
}
