package recipefx.model;

import java.util.ArrayList;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

public class Recipe {

  private int recipeId;
  private StringProperty name;
  private StringProperty category;
  private StringProperty url;
  private StringProperty directions;
  private ObservableList<Ingredient> ingredients;
  

  public Recipe() { }
   
  public Recipe(int recipeId, String name, String category, String url, String directions) {
    this.recipeId = recipeId;
    this.name = new SimpleStringProperty(name);
    this.category = new SimpleStringProperty(category);
    this.url = new SimpleStringProperty(url);
    this.directions = new SimpleStringProperty(directions);
  }
  
  public Recipe(String name, String category, String url, String directions) {
    this.name = new SimpleStringProperty(name);
    this.category = new SimpleStringProperty(category);
    this.url = new SimpleStringProperty(url);
    this.directions = new SimpleStringProperty(directions);
  }
  
  //copy constructor
  public Recipe(Recipe original) {
    this.recipeId = original.getRecipeId();
    this.name = new SimpleStringProperty(original.getName());
    this.category = new SimpleStringProperty(original.getCategory());
    this.url = new SimpleStringProperty(original.getURL());
    this.directions = new SimpleStringProperty(original.getDirections());
    this.ingredients = FXCollections.observableArrayList();
    for (Ingredient i : original.getIngredientData()) {
      ingredients.add(new Ingredient(i));
    }
  }
    
  //ingredients methods
  public ObservableList<Ingredient> getIngredientData() { return ingredients; }
  public void setIngredientData(ArrayList<Ingredient> ingredients) { this.ingredients = FXCollections.observableArrayList(ingredients); }
  
  //recipeId methods
  public int getRecipeId() { return recipeId; }
  public void setRecipeId(int value) { this.recipeId = value; }
 
  //getters
  public String getName() { return name.get(); }
  public String getCategory() { return category.get(); }
  public String getURL() { return url.get(); }
  public String getDirections() { return directions.get(); }
 
  //setters
  public void setName(String value) { name.set(value); }
  public void setCategory(String value) { category.set(value); }
  public void setURL(String value) { url.set(value); }
  public void setDirections(String value) { directions.set(value); }
 
  //property getters
  public StringProperty nameProperty() { return name; }
  public StringProperty categoryProperty() { return category; }
  public StringProperty urlProperty() { return url; }
  public StringProperty directionsProperty() { return directions; }
}
