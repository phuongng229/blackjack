/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Loading player scores from a text file, saving player scores back to the file
 * and updating an individual player's score to the file
 *
 * @author jonathan & phuong
 */
public class ScoreStore {

    //Load scores from the database
    private static Map<String, PlayerScores> loadScores() {
        Map<String, PlayerScores> scoreInMap = new HashMap<>();
        
        try {
            //Get connection to the database
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM PLAYERS";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            
            //Populate the map with player data from the database
            while (resultSet.next()) {
                String playerName = resultSet.getString("NAME");
                PlayerScores scores = new PlayerScores();
                scores.setTotalWins(resultSet.getInt("WINS"));
                scores.setTotalLosses(resultSet.getInt("LOSSES"));
                scores.setTotalPushes(resultSet.getInt("PUSHES"));
                scores.setBalance(resultSet.getInt("BALANCE"));
                scoreInMap.put(playerName, scores);
            }
        } catch (SQLException e) {
            System.out.println("Error in loading scores from the database: " + e.getMessage());
        }
        return scoreInMap;
    }
    
    //Get the scores of a player from the database
    public static PlayerScores getPlayerScores(String name) {
        PlayerScores scores = DBManager.getPlayerScores(name);
        return scores != null ? scores : new PlayerScores(); //Return new scores if no player data found
    }
    
    //Update an existing player's score in the database
    public static void updatePlayerScore(String name, PlayerScores scores) {
        DBManager.updatePlayerScore(name, scores); 
    }
    
    //Insert a new player's score in the database
    public static void insertPlayer(String name, PlayerScores scores) {
        DBManager.insertPlayer(name, scores);
    }
    
    //Check if a player exists in the database
    public static boolean playerHasExistingScores(String name) {
        return DBManager.playerExists(name);
    }
    
    // Update the player's balance in the database (useful when balance changes)
    public static void updatePlayerBalance(String name, double balance) {
        DBManager.updatePlayerBalance(name, balance);  
    }

}
