/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;
import java.sql.*;
/**
 *
 * @author fuong
 */
public class DBManager {
    private static final String DB_URL = "jdbc:derby:/Users/fuong/Documents/AUT/2ND YEAR/SEMESTER_1/PROGRAM_DESIGN_AND_CONSTRUCTION/PROJECT_2/blackjack/db/BlackjackDB;create=true"; //Look for a database called BlackjackDB, create it if it doesn't exist
    private static Connection conn;
    
    //Establish connection
    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connection established successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to connect to DB: " + e.getMessage());
            }
        }
        return conn;
    }
    
    //Close connection
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the DB: " + e.getMessage());
        } finally {
            conn = null;
        }
    }
    
    //Create a table
    public static void createPlayerTable() {
        //Create a table named players
        try {
            Connection conn = DBManager.getConnection(); 
            Statement statement = conn.createStatement(); //Create a Statement object from the Connection to the database. Acting as a messenger that sends the SQL query to the database
            
            //Drop table if it exists
            try {
                statement.executeUpdate("DROP TABLE PLAYERS");
            } catch (SQLException e) {
                System.out.println("PLAYERS table did not exist");
            }
            
            //Create new Players table
            String createTable = "CREATE TABLE PLAYERS (" +
                             "NAME VARCHAR(50), " +
                             "WINS INT, " +
                             "LOSSES INT, " +
                             "PUSHES INT, " +
                             "BALANCE DOUBLE)";
            statement.executeUpdate(createTable);
            System.out.println("Created PLAYERS table");
        } catch (SQLException e) {
            System.out.println("Error setting up PLAYERS table: " + e.getMessage());
        }
    }
    
    public static void insertPlayer(String name, int wins, int losses, int pushes, double balance) {
    try {
        PreparedStatement ps = getConnection().prepareStatement(
            "INSERT INTO PLAYERS (NAME, WINS, LOSSES, PUSHES, BALANCE) VALUES (?, ?, ?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, wins);
        ps.setInt(3, losses);
        ps.setInt(4, pushes);
        ps.setDouble(5, balance);
        ps.executeUpdate();
        System.out.println("Inserted player: " + name);
    } catch (SQLException e) {
        System.out.println("Insert failed: " + e.getMessage());
    }
    }
}
