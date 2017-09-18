package recipefx.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import recipefx.model.Recipe;

public class RecipeListController {
  
	@FXML
	private TableView<Recipe> recipeTable;
	@FXML
	private TableColumn<Recipe, String> recipeNameColumn;
	
	private RecipeOverviewController overview;
		
	/**
   * Gets currently selected recipe.
   * @return recipe 
   */
  private Recipe getSelectedRecipe() {
    return recipeTable.getSelectionModel().getSelectedItem();
  }
  
	/**
   * Initializes recipe list & selection listener. 
   * Method is called automatically after the fxml file is loaded.
   */
  @FXML
  private void initialize() {
    // Initialize the recipe table with the recipe names.
    recipeNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        
    // Listen for selection changes and show the recipe details when changed.
    recipeTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> overview.showRecipeDetails(newValue));
  }
  
	/**
	 * Sets the overview & gives it the currently selected recipe, may be null.
	 * @param overview reference to RecipeOverviewController
	 */
	public void setOverview(RecipeOverviewController overview) {
		this.overview = overview;
		overview.showRecipeDetails(getSelectedRecipe());
	}
	
	/**
   * Adds observable list data to the table.
   * @param recipes ObservableList of Recipes
   */
	public void setTableItems(ObservableList<Recipe> recipes){
	  recipeTable.setItems(recipes);
	}
	   
}
