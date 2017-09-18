package recipefx.view;


import java.util.ArrayList;
import java.util.regex.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import recipefx.model.Ingredient;
import recipefx.model.Recipe;

public class NewRecipeController {

  @FXML
  private TextField recipeNameField;
  @FXML
  private TextField categoryField;
  @FXML
  private TextField urlField;
  @FXML
  private TextArea directionsArea;
  @FXML
  private TextArea ingredientsArea;
    
  private Stage dialogStage;
  private Recipe recipe;
  private boolean okClicked = false;

  /**
   * Sets the stage of this dialog.
   * @param dialogStage of this popup
   */
  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  /**
   * Get recipe from new dialog controller.
   * @return Recipe created by user, may be null.
   */
  public Recipe getRecipe(){
    return recipe;
  }

  /**
   * Find out whether OK is clicked or not.
   * @return true if user clicks OK, false otherwise.
   */
  public boolean isOKClicked() {
    return okClicked;
  }

  /**
   * Called when user clicks OK.
   */
  @FXML
  private void handleOK() {
    if (isInputValid()) {
      recipe = new Recipe(recipeNameField.getText(), categoryField.getText(), urlField.getText(), directionsArea.getText());
      recipe.setIngredientData(ingredientParser(ingredientsArea.getText()));
      okClicked = true;
      dialogStage.close();
    }
  }

  /**
   * Called when user clicks Cancel.
   */
  @FXML
  private void handleCancel() {
    dialogStage.close();
  }
  
  /**
   * Parses ingredients text into amounts, units, and ingredient names.
   * @param str String from ingredients TextArea.
   * @return ArrayList of Ingredients
   */
  private ArrayList<Ingredient> ingredientParser(String str) {
    ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    String[] lines = str.split("\\R");
    
    Pattern amtRgx = Pattern.compile("(\\b[0-9]+\\/[1-9][0-9]*\\b|\\b[0-9]*.[0-9]*\\b|\\b[0-9]+\\b)");
    Pattern unitRgx = Pattern.compile("([0-9]+\"|tbsps?|tablespoons?|tsps?|teaspoons?|cups?|ozs?|lbs?|pounds?|"
        + "grams?|sm(all)?|med(ium)?|lg|large|cloves?|pinch(es)?|cans?|bunch(es)?)",Pattern.CASE_INSENSITIVE);
    Pattern nameRgx = Pattern.compile("(?:\\bof\\b|\\ba\\b|\\ban\\b|\\bthe\\b)|(\\b\\w+\\b)",Pattern.CASE_INSENSITIVE);
    
    for (int i = 0; i < lines.length; i++) {
      String amount = new String();
      String unit = new String();
      String name = new String();
 
      Matcher matcher = amtRgx.matcher(lines[i]);    
      if (matcher.find()) {
        if (matcher.group(1) != null)
          amount = matcher.group(1);
      } 
      
      matcher = matcher.usePattern(unitRgx);
      if (matcher.find()) {
        if (matcher.group(1) != null)
          unit = matcher.group(1);    
      } 
     
      matcher = matcher.usePattern(nameRgx);
      while (matcher.find()) {
        if (matcher.group(1) != null)
          name = name.concat(matcher.group(1)+" ");
      }  	    

      ingredientsList.add(new Ingredient(amount.trim(),unit.trim(),name.trim()));
    }
    
    return ingredientsList;
  }

  /**
   * Validates user inputs from text fields.
   * @return true if inputs are valid
   */
  private boolean isInputValid() {
    String errorMessage = "";

    if (recipeNameField.getText() == null || recipeNameField.getText().length() == 0) {
      errorMessage += "Not a valid recipe name!\n"; 
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } 
    else {
      // Show the error message.
      Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(dialogStage);
      alert.setTitle("Invalid Fields");
      alert.setHeaderText(null);
      alert.setContentText(errorMessage);
      alert.showAndWait();
    }
    return false;
  }
}