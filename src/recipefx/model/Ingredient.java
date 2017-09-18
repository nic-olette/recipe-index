package recipefx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ingredient {
	
  private int ingredientId;
  private StringProperty amount;
  private StringProperty unit;
  private StringProperty name;
  

  public Ingredient() { }
  
  public Ingredient(int id, String amount, String unit, String name) {
    this.ingredientId = id;
    this.amount = new SimpleStringProperty(amount);
    this.unit = new SimpleStringProperty(unit);
    this.name = new SimpleStringProperty(name);
  }  
  
  public Ingredient(String amount, String unit, String name) {
    this.amount = new SimpleStringProperty(amount);
    this.unit = new SimpleStringProperty(unit);
    this.name = new SimpleStringProperty(name);
  }
  
  //copy constructor
  public Ingredient(Ingredient original) {
    this.ingredientId = original.getIngredientId();
    this.amount = new SimpleStringProperty(original.getAmnt());
    this.unit = new SimpleStringProperty(original.getUnit());
    this.name = new SimpleStringProperty(original.getName());
  }
  
  //ingredientId methods
  public int getIngredientId() { return ingredientId; }
  public void setIngredientId(int value) { this.ingredientId = value; }

  //getters
  public String getAmnt() { return amount.get(); }
  public String getUnit() { return unit.get(); }
  public String getName() { return name.get(); }

  //setters
  public void setAmnt(String value) { amount.set(value); }
  public void setUnit(String value) { unit.set(value); }
  public void setName(String value) { name.set(value); }
 
  //property getters
  public StringProperty amntProperty() { return amount; }
  public StringProperty unitProperty() { return unit; }
  public StringProperty nameProperty() { return name; }
  
}
