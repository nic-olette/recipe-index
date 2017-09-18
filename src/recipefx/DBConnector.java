package recipefx;

import java.sql.*;

public class DBConnector
{
	
  public DBConnector() {
    try(Connection conn = this.connect();) {
      createTables(conn);
    } 
    catch(SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

  protected Connection connect() throws SQLException {
    Connection conn = null;
    conn = DriverManager.getConnection("jdbc:sqlite:recipedb.db");
    conn.createStatement().execute("PRAGMA foreign_keys = ON");
    return conn;
  }

  private void createTables(Connection conn) throws SQLException {
    String sqlCreate = "CREATE TABLE IF NOT EXISTS recipes ("
            + "recipeid INTEGER PRIMARY KEY,"
            + "name TEXT NOT NULL,"
            + "category TEXT,"
            + "url TEXT,"
            + "directions TEXT);";
    Statement stmt = conn.createStatement();
    stmt.execute(sqlCreate);

    sqlCreate = "CREATE TABLE IF NOT EXISTS ingredients ("
            + "ingredientid INTEGER PRIMARY KEY,"
            + "recipeid INTEGER NOT NULL,"
            + "name TEXT NOT NULL,"
            + "amount TEXT,"
            + "unit TEXT,"
            + "type TEXT,"
            + "FOREIGN KEY (recipeid) REFERENCES recipes (recipeid) "
            + "ON DELETE CASCADE ON UPDATE CASCADE);";
    stmt.execute(sqlCreate);

    stmt.close();
  }
}