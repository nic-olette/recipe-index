package recipefx.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import recipefx.MainApp;
import recipefx.model.Ingredient;
import recipefx.model.Recipe;

public class EditRecipeController {
	@FXML
	private TextField recipeNameTextField;
	@FXML
	private TextField categoryTextField;
	@FXML
	private TextField urlTextField;
	@FXML
	private TextArea directionsTextArea;
	
	@FXML
	private TableView<Ingredient> ingredientTable;
	@FXML
	private TableColumn<Ingredient, String> ingredientAmountCol;
	@FXML
	private TableColumn<Ingredient, String> ingredientUnitCol;
	@FXML
	private TableColumn<Ingredient, String> ingredientNameCol;
	@FXML
	private TableColumn<Ingredient, Boolean> deleteCol;
	
	@FXML
	private TextField addAmount;
	@FXML
	private TextField addUnit;
	@FXML
	private TextField addName;
	
	private MainApp mainApp;
  private Recipe newRecipe;
	private Recipe recipe;
	
	
	/**
   * Initialize ingredients table data and set cell factory so the columns are editable. 
   * Method is called automatically after the fxml file is loaded.
   */
	@FXML
	private void initialize() {
	  ingredientAmountCol.setCellValueFactory(cellData -> cellData.getValue().amntProperty());
	  ingredientAmountCol.setCellFactory(TextFieldTableCell.forTableColumn());
	  
	  ingredientUnitCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
	  ingredientUnitCol.setCellFactory(TextFieldTableCell.forTableColumn());
	  
	  ingredientNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	  ingredientNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	  
	  //adds delete button to column for any row with entries
	  deleteCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY VALUE"));
	  deleteCol.setCellFactory(new Callback<TableColumn<Ingredient, Boolean>, TableCell<Ingredient, Boolean>>() {
      @Override
      public TableCell<Ingredient, Boolean> call(final TableColumn<Ingredient, Boolean> param) {
        final TableCell<Ingredient, Boolean> cell = new TableCell<Ingredient, Boolean>() {
          Button btn = new Button("X");
          
          @Override
          public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
              setText(null);
            } 
            else {
              btn.setStyle("-fx-background-radius: 0; -fx-padding: 0; -fx-background-color: transparent;"
                           + "-fx-text-fill: red; -fx-font-weight: bold;");
              btn.setOnAction(event -> {
                Ingredient ingredient = getTableView().getItems().get(getIndex());
                newRecipe.getIngredientData().remove(ingredient);
                });
              setGraphic(btn);
              setText(null);
            }
          }
        };
        return cell;
      }
    });
  }
    
	/**
	 * Called by main app to give a reference back to itself.
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
	  this.mainApp = mainApp;
  }
    
	/**
	 * Sets recipe to be edited and loads non-ingredient TextFields.
	 * @param recipe to be edited
	 */
	public void setRecipe(Recipe recipe) {
	  this.newRecipe = new Recipe(recipe);
	  this.recipe = recipe;
		recipeNameTextField.setText(newRecipe.getName());
		categoryTextField.setText(newRecipe.getCategory());
		urlTextField.setText(newRecipe.getURL());
		directionsTextArea.setText(newRecipe.getDirections());
		ingredientTable.setItems(newRecipe.getIngredientData());
	}
    
	/**
   * Called when user clicks Add, adds new ingredient.
   */
	@FXML
	private void handleAdd() {
	  newRecipe.getIngredientData().add(
	      new Ingredient(addAmount.getText(),addUnit.getText(),addName.getText()));
	  
	  addAmount.clear();
	  addUnit.clear();
	  addName.clear();
	}
	
	/**
   * Called when user clicks OK.
   */
	@FXML
	private void handleSave() {
	  if (isInputValid()) {
	    recipe.setName(recipeNameTextField.getText());
	    recipe.setCategory(categoryTextField.getText());
	    recipe.setURL(urlTextField.getText());
	    recipe.setDirections(directionsTextArea.getText());
	    recipe.setIngredientData(new ArrayList<Ingredient>(newRecipe.getIngredientData()));
	    mainApp.updateRecipe(recipe);
	    mainApp.showRecipeOverview();
	  }
  }
    
  /**
   * Called when the user clicks cancel.
   */
  @FXML
  private void handleCancel() {
    mainApp.showRecipeOverview();
  }
    

  /**
   * Validates user inputs from text fields.
   * @return true if inputs are valid
   */
  private boolean isInputValid() {
    String errorMessage = "";

    if (recipeNameTextField.getText() == null || recipeNameTextField.getText().length() == 0) {
      errorMessage += "Not a valid recipe name!\n"; 
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } 
    else {
      // Show the error message.
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Invalid Fields");
      alert.setHeaderText(null);
      alert.setContentText(errorMessage);
      alert.showAndWait();
    }
    return false;
  }
  
}
