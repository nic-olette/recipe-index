package recipefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import recipefx.model.Recipe;

public class RecipeDAO {
  
  private DBConnector dbconnector;
  private IngredientDAO ingredientDao;
  
  public RecipeDAO() {
    dbconnector = new DBConnector();
    ingredientDao = new IngredientDAO();
  }
  
  /**
   * Gets all recipes from database.
   * @return ArrayList of Recipes
   */
  public ArrayList<Recipe> getAllRecipes() {
    String sql = "SELECT recipeid, name, category, url, directions "
               + "FROM recipes";

    ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

    try(Connection conn = dbconnector.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);)
    {
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Recipe recipe = new Recipe(rs.getInt("recipeid"), rs.getString("name"), rs.getString("category"),rs.getString("url"), rs.getString("directions"));
        recipe.setIngredientData(ingredientDao.getIngredientsByRecipeId(conn, recipe.getRecipeId()));
        recipeList.add(recipe);
      }
      rs.close();
    } 
    catch(SQLException ex) {
      System.out.println("selectAllRecipes: "+ex.getMessage());
    }

    return recipeList;
  }
  
  /**
   * Gets a single recipe by name.
   * @param name of the recipe to retrieve
   * @return requested recipe or null
   */
  public Recipe getRecipeByName(String name){
    String sql = "SELECT recipeid, name, category, url, directions "
               + "FROM recipes "
               + "WHERE name = ?";
    
    Recipe recipe = null;

    try(Connection conn = dbconnector.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);)
    {
      pstmt.setString(1, name);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        recipe = new Recipe(rs.getInt("recipeid"), rs.getString("name"), rs.getString("category"), 
                 rs.getString("url"), rs.getString("directions"));
      }     
      recipe.setIngredientData(ingredientDao.getIngredientsByRecipeId(conn, recipe.getRecipeId()));
      rs.close();
    } 
    catch(SQLException ex) {
      System.out.println("selectRecipe: "+ex.getMessage());
    }
    return recipe;
  }
  
  /**
   * Inserts a single recipe into database.
   * @param recipe to be inserted
   * @return true if insertion successful, false otherwise
   */
  public boolean insertRecipe(Recipe recipe) {
    String sql = "INSERT INTO recipes(name,category,url,directions) "
               + "VALUES(?,?,?,?)";
    
    boolean success = false;
    
    try(Connection conn = dbconnector.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
    {
      conn.setAutoCommit(false);
      
      pstmt.setString(1, recipe.getName());
      pstmt.setString(2, recipe.getCategory());
      pstmt.setString(3, recipe.getURL());
      pstmt.setString(4, recipe.getDirections());
      pstmt.executeUpdate();     

      ResultSet generatedKeys = pstmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        recipe.setRecipeId(generatedKeys.getInt(1));
        generatedKeys.close();
        
        if (!recipe.getIngredientData().isEmpty()) {
          success = ingredientDao.insertIngredientsByRecipeId(conn, recipe.getRecipeId(), recipe.getIngredientData());
        }
        else { //no ingredients to insert
          success = true;
        }
      }
      
      if (success) {
        conn.commit();
      }
      else {
        conn.rollback();
      }
    } 
    catch(SQLException ex) {
      System.out.println(ex.getMessage());
    }
    return success;
  }
  
  /**
   * Updates a single recipe in database.
   * @param recipe to be updated
   * @return true if update successful, false otherwise
   */
  public boolean updateRecipe(Recipe recipe) {
    String sql = "UPDATE recipes "
               + "SET name = ?, category = ?, url = ?, directions = ? "
               + "WHERE recipeid = ?";

    boolean success = false;
    
    try (Connection conn = dbconnector.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) 
    {
      conn.setAutoCommit(false);
      
      pstmt.setString(1, recipe.getName());
      pstmt.setString(2, recipe.getCategory());
      pstmt.setString(3, recipe.getURL());
      pstmt.setString(4, recipe.getDirections());
      pstmt.setInt(5, recipe.getRecipeId());
      int result = pstmt.executeUpdate();
      
      boolean delete = ingredientDao.removeIngredientsByRecipeId(conn, recipe.getRecipeId());
      boolean insert = ingredientDao.insertIngredientsByRecipeId(conn, recipe.getRecipeId(), recipe.getIngredientData());
      
      if (result == 1 && delete && insert) {
        success = true;
        conn.commit();
      }
      else {
        conn.rollback();
      }
    } 
    catch (SQLException ex) {
      System.out.println("updateRecipe: "+ex.getMessage());
    }
    return success;
  }

  /**
   * Removes a recipe from database.
   * @param recipe to be removed
   * @return true if removal successful, false otherwise
   */
  public boolean removeRecipe(Recipe recipe) {
    String sql = "DELETE FROM recipes "
        + "WHERE recipeid = ?";

    boolean success = false;
    
    try (Connection conn = dbconnector.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) 
    {
      pstmt.setInt(1, recipe.getRecipeId()); 
      int result = pstmt.executeUpdate();
      if (result == 1) 
        success = true;
    } 
    catch (SQLException ex) {
      System.out.println("removeRecipe: "+ex.getMessage());
    }
    return success;
  }
}
