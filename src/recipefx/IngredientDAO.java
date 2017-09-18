package recipefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import recipefx.model.Ingredient;

public class IngredientDAO {
  
  /**
   * Gets all ingredients for a single recipe.
   * @param conn database connection from the RecipeDAO
   * @param recipeId id number for the associated recipe
   * @return ArrayList of Ingredients
   */
  public ArrayList<Ingredient> getIngredientsByRecipeId(Connection conn, int recipeId) {
    String sql = "SELECT ingredientid, amount, unit, name "
               + "FROM ingredients "
               + "WHERE recipeid = ?";
    
    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql);)
    {
      pstmt.setInt(1, recipeId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        ingredients.add(new Ingredient(rs.getInt("ingredientid"), rs.getString("amount"), 
        rs.getString("unit"), rs.getString("name")));
      }
      rs.close();
    }
    catch(SQLException ex){
      System.out.println("getAllIngredients: "+ex.getMessage());
    }
    return ingredients;
  }

  /**
   * Inserts all ingredients for a single recipe.
   * @param conn database connection from the RecipeDAO
   * @param recipeId id number for the associated recipe
   * @param ingredients ingredients to be inserted for recipeId
   * @return true if insertion successful, false otherwise
   */
  public boolean insertIngredientsByRecipeId(Connection conn, int recipeId, ObservableList<Ingredient> ingredients) {
    String sql = "INSERT INTO ingredients(recipeid, amount, unit, name) "
               + "VALUES (?, ?, ?, ?)";
    
    boolean success = true;
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
    {  
      for (Ingredient i : ingredients) {
        pstmt.setInt(1, recipeId);
        pstmt.setString(2, i.getAmnt());
        pstmt.setString(3, i.getUnit());
        pstmt.setString(4, i.getName());
        pstmt.executeUpdate();     
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
          i.setIngredientId(generatedKeys.getInt(1));
          generatedKeys.close(); 
        }
        else
          success = false;
      }
      
    }
    catch(SQLException ex) {
      System.out.println("insertIngredients: "+ex.getMessage());
      success = false;
    }
    
    return success;
  }
  
  /**
   * Removes all ingredients for a single recipe.
   * @param conn database connection from the RecipeDAO
   * @param recipeId id number for the associated recipe
   * @return true if removal successful, false otherwise
   */
  public boolean removeIngredientsByRecipeId(Connection conn, int recipeId) {
    String sqlDelete = "DELETE FROM ingredients "
                     + "WHERE recipeid = ?";
    
    boolean success = true;
    
    try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);)
    {
        pstmtDelete.setInt(1, recipeId);
        pstmtDelete.executeUpdate(); 
    }
    catch (SQLException ex) {
      System.out.println("removeIngredients: "+ex.getMessage());
      success = false;
    }
    return success;
  }
  
}
