package recipefx;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import recipefx.model.Recipe;
import recipefx.view.EditRecipeController;
import recipefx.view.MenuBarController;
import recipefx.view.NewRecipeController;
import recipefx.view.RecipeListController;
import recipefx.view.RecipeOverviewController;

public class MainApp extends Application {

  private Stage primaryStage;
  private BorderPane rootLayout;
  private RecipeDAO recipeDao;
  private RecipeListController recipeList;
  private ObservableList<Recipe> recipeData;
    
  public MainApp() {
    //start sqlite db & get recipes
    recipeDao = new RecipeDAO();
    recipeData = FXCollections.observableArrayList(recipeDao.getAllRecipes());
  }
  
  //returns recipe data
  public ObservableList<Recipe> getRecipeData() {
    return recipeData;
  }
    
  //manipulate recipe data
  public void insertRecipe(Recipe recipe) {
    recipeDao.insertRecipe(recipe);
    recipeData.add(recipe);
  }
  
  public void updateRecipe(Recipe recipe) {
    recipeDao.updateRecipe(recipe);
  }
  
  public void removeRecipe(Recipe recipe) {
    recipeDao.removeRecipe(recipe);
    recipeData.remove(recipe);
  }
  
  public Recipe getRecipe(String name){
    return recipeDao.getRecipeByName(name);
  }
    

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Recipe Indexer");

    initRootLayout();
    showRecipeList();
    showRecipeOverview();
  }

  /**
   * Initializes root layout.
   */
  public void initRootLayout() {
    try {
      //load root layout from fxml file
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
      rootLayout = (BorderPane)loader.load();
      
      //give menu bar reference to the main app
      MenuBarController controller = loader.getController();
      controller.setMainApp(this);
        
      //show the scene containing the root layout.
      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();
    } 
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Shows recipe list in root layout.
   */
  public void showRecipeList() {
    try {
      //load recipe list
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/RecipeList.fxml"));
      AnchorPane anchorPane = (AnchorPane)loader.load();
       
      //set recipe list to the left 
      rootLayout.setLeft(anchorPane);
       
      //give the controller access to the recipe data
      recipeList = loader.getController();
      recipeList.setTableItems(getRecipeData());       
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * Shows recipe overview in root layout.
   */
  public void showRecipeOverview() {
    try {
      //load recipe overview
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/RecipeOverview.fxml"));
      AnchorPane anchorPane = (AnchorPane)loader.load();
              
      //set recipe overview to the right
      rootLayout.setRight(anchorPane);
                    
      //give overview reference to the main app
      RecipeOverviewController controller = loader.getController();
      controller.setMainApp(this);
        
      //give the list controller access to the recipe overview
      recipeList.setOverview(controller);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * Shows edit view in root layout (replaces overview).
   * @param recipe to be edited
   */
  public void showEditRecipeView(Recipe recipe) {
    try {
      //load edit view
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/EditRecipeView.fxml"));
      AnchorPane anchorPane = (AnchorPane)loader.load();
         
      //set edit view to the right
      rootLayout.setRight(anchorPane);
              
      //give the controller access to the main app & load recipe to be edited
      EditRecipeController controller = loader.getController();
      controller.setMainApp(this);
      controller.setRecipe(recipe);         
		} 
    catch (IOException ex) {
      ex.printStackTrace();
		}
  }
   
  /**
   * Opens a dialog for inputting a new recipe, saves in database if OK clicked.
   */
  public void showNewRecipeDialog() {
    try {
      //load new recipe dialog
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/NewRecipeDialog.fxml"));
      AnchorPane anchorPane = (AnchorPane)loader.load();

      //create dialog stage for the popup
      Stage dialogStage = new Stage();
      dialogStage.setTitle("New Recipe");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(primaryStage);
      Scene scene = new Scene(anchorPane);
      dialogStage.setScene(scene);

      //set dialog stage in the controller
      NewRecipeController controller = loader.getController();
      controller.setDialogStage(dialogStage);
      
      //show dialog and wait until the user closes it
      dialogStage.showAndWait();
      
      //save recipe
      if (controller.isOKClicked()) {
        Recipe newRecipe = controller.getRecipe();
        insertRecipe(newRecipe);
      }
    } 
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

}