package recipefx.view;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import recipefx.MainApp;
import recipefx.model.Recipe;


public class MenuBarController {

	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem editItem;
	@FXML
	private MenuItem deleteItem;
	
	private MainApp mainApp;
	
	/**
   * Called by main app to give a reference back to itself.
   * @param mainApp
   */
	public void setMainApp(MainApp mainApp) {
	  this.mainApp = mainApp;
	}
	
	/**
	 * Handles closing application when selected from File menu.
   */
	@FXML
	private void handleClose() {
	  Stage stage = (Stage) menuBar.getScene().getWindow();
	  stage.close();
	}
	
	/**
   * Calls new recipe dialog when selected from File menu.
   */
	@FXML
	public void handleNew() {
	  mainApp.showNewRecipeDialog();
	}

	/**
   * Calls edit recipe view when selected from Edit menu.
   */
  @FXML
  public void handleEdit() {
    String name = new String();
    boolean found = false;
	
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Edit Recipe");
    dialog.setContentText("Enter the name of the recipe you want to edit:");
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()){
      name = result.get();
    }

    for (Recipe r : mainApp.getRecipeData()) {
      if (r.getName().equalsIgnoreCase(name)) {
        found = true;
        mainApp.showEditRecipeView(r);
        break;
      }	  
    }
    
    if (!found && result.isPresent()) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Edit Recipe");
      alert.setHeaderText(null);
      alert.setContentText("Recipe not found.");
      alert.showAndWait();
    }
  }
	
  /**
   * Handles recipe deletion when selected from Edit menu.
   */
  @FXML
  public void handleDelete() {
    String name = new String();
    boolean found = false;
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Delete Recipe");
		dialog.setContentText("Enter the name of the recipe you want to delete:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		  name = result.get();
		}

	  for (Recipe r : mainApp.getRecipeData()) {
	    if (r.getName().equalsIgnoreCase(name)) {
	      found = true;
		    mainApp.removeRecipe(r);
		    break;
		  }	  
	  }
	    
	  if (!found && result.isPresent()) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Delete Recipe");
	    alert.setHeaderText(null);
	    alert.setContentText("Recipe not found.");
	    alert.showAndWait();
	  }	
  }

}
