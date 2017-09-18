package recipefx.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import recipefx.MainApp;
import recipefx.model.Recipe;
import recipefx.model.Ingredient;

public class RecipeOverviewController {
  
  @FXML
  private TableView<Ingredient> ingredientTable;
  @FXML
  private TableColumn<Ingredient, String> ingredientAmountCol;
  @FXML
  private TableColumn<Ingredient, String> ingredientUnitCol;
  @FXML
  private TableColumn<Ingredient, String> ingredientNameCol;
   
  @FXML
  private Label recipeNameLabel;
  @FXML
  private Label categoryLabel;
  @FXML
  private Hyperlink urlLink;
  @FXML
  private TextArea directionsText;
     
  private MainApp mainApp;
  private Recipe selectedRecipe;
   
  /**
   * Called by main app to give a reference back to itself.
   * @param mainApp
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }
    
  /**
   * Called when the user clicks new button. MainApp opens a dialog 
   * to input details for a new recipe.
   */
  @FXML
  private void handleNewRecipe() {
    mainApp.showNewRecipeDialog();
  }

  /**
   * Called when the user clicks edit button. MainApp replaces overview 
   * with edit view for the selected recipe.
   */
  @FXML
  private void handleEditRecipe() {
    if (selectedRecipe != null) {
      mainApp.showEditRecipeView(selectedRecipe);
    }
    else { //nothing selected
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a recipe before editing.");
      alert.showAndWait();
    }
  }
  
  /**
   * Called when the user clicks delete button. MainApp handles 
   * deletion of the selected recipe.
   */
  @FXML
  private void handleDeleteRecipe() {
    if (selectedRecipe != null) {
      mainApp.removeRecipe(selectedRecipe);  
    }
  }
    
  /**
   * Opens external browser to display link on user click.
   */
  @FXML
  private void handleHyperlinkClick() {
    mainApp.getHostServices().showDocument(urlLink.getText());
  } 
    
  /**
   * Called by the RecipeListController, fills all fields & ingredient table
   * with relevant data from the selected recipe. 
   * @param recipe the selected recipe or null
   */
  protected void showRecipeDetails(Recipe recipe) {
    selectedRecipe = recipe;
    
    if (selectedRecipe != null) {
      recipeNameLabel.setText(recipe.getName());
      categoryLabel.setText(recipe.getCategory());
      urlLink.setText(recipe.getURL());
      directionsText.setText(recipe.getDirections());
      ingredientTable.setItems(recipe.getIngredientData());
      ingredientAmountCol.setCellValueFactory(cellData -> cellData.getValue().amntProperty());
      ingredientUnitCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
      ingredientNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
      ingredientTable.setVisible(true);
    } 
    else {
      // Recipe is null, remove all the text.
      recipeNameLabel.setText("");
      categoryLabel.setText("");
      urlLink.setText("");
      directionsText.setText("");
      ingredientTable.setVisible(false);
    }
  }
    
}
