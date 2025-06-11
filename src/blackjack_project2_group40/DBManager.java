/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;
import java.sql.*;
/**
 *
 * @author phuong
 */
public class DBManager {
    private static final String DB_URL = "jdbc:derby:/Users/fuong/Documents/AUT/2ND YEAR/SEMESTER_1/PROGRAM_DESIGN_AND_CONSTRUCTION/PROJECT_2/blackjack/db/BlackjackDB;create=true";
    private static Connection conn;

    // Establish connection
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

    // Close connection
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

    // Create the player table in the database
    public static void createPlayerTable() {
        try {
            Connection conn = DBManager.getConnection();
            Statement statement = conn.createStatement();

            // Drop the table if it exists
            try {
                statement.executeUpdate("DROP TABLE PLAYERS");
            } catch (SQLException e) {
                System.out.println("PLAYERS table did not exist");
            }

            // Create new PLAYERS table
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

    // Insert players into the database using data from ScoreStore class
    public static void insertPlayer(String name, PlayerScores scores) {
        try {
            
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO PLAYERS (NAME, WINS, LOSSES, PUSHES, BALANCE) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, name);        // Set player's name
            ps.setInt(2, scores.getTotalWins());       // Set wins
            ps.setInt(3, scores.getTotalLosses());    // Set losses
            ps.setInt(4, scores.getTotalPushes());    // Set pushes
            ps.setDouble(5, scores.getBalance());    // Set balance
            ps.executeUpdate();           // Execute the insert statement
            System.out.println("Inserted player: " + name);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // Update an existing player's scores in the database
    public static void updatePlayerScore(String name, PlayerScores scores) {
        try {
            
            PreparedStatement ps = getConnection().prepareStatement(
                    "UPDATE PLAYERS SET WINS = ?, LOSSES = ?, PUSHES = ?, BALANCE = ? WHERE NAME = ?"
            );
            ps.setInt(1, scores.getTotalWins());
            ps.setInt(2, scores.getTotalLosses());
            ps.setInt(3, scores.getTotalPushes());
            ps.setDouble(4, scores.getBalance());
            ps.setString(5, name); // Set player's name for the update
            ps.executeUpdate();  // Execute the update statement
            System.out.println("Updated player score: " + name);
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Retrieve a player's scores from the database
    public static PlayerScores getPlayerScores(String name) {
        PlayerScores scores = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT * FROM PLAYERS WHERE NAME = ?"
            );
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                // If player exists, retrieve their scores and balance
                scores = new PlayerScores();
                scores.setTotalWins(resultSet.getInt("WINS"));
                scores.setTotalLosses(resultSet.getInt("LOSSES"));
                scores.setTotalPushes(resultSet.getInt("PUSHES"));
                scores.setBalance(resultSet.getDouble("BALANCE"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving player scores: " + e.getMessage());
        }
        return scores;
    }
    
    // Check if a player exists in the database
    public static boolean playerExists(String name) {
        boolean exists = false;
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM PLAYERS WHERE NAME = ?"
            );
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking player existence: " + e.getMessage());
        }
        return exists;
    }
    
    //Update the player's balance in the database
    public static void updatePlayerBalance(String name, double balance) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE PLAYERS SET BALANCE = ? WHERE NAME = ?"
            );
            ps.setDouble(1, balance);
            ps.setString(2, name); //Set player's name for the update
            ps.executeUpdate();
            System.out.println("Updated balance for: " + name);
        } catch (SQLException e) {
            System.out.println("Update balance failed: " + e.getMessage());
        }
    }
}
