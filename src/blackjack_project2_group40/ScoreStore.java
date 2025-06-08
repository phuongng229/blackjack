/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loading player scores from a text file, saving player scores back to the file
 * and updating an individual player's score to the file
 *
 * @author jonathan & phuong
 */
public class ScoreStore {

    private static final String FILE_PATH = "./resources/scorefile.txt";
    private static final Map<String, PlayerScores> scoreInMap = new HashMap<>();

    //Load Scores from the file and store them in the scoreInMap
    public static Map<String, PlayerScores> loadScores() {

        //Check if the file exists
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            
            try {
                file.getParentFile().mkdirs(); //Create ./resources if it doesn't exist
                file.createNewFile();
            } catch (IOException io) {
                System.out.println("Could not create the file");
            }
            return scoreInMap;
        }

        //Read the file and load into scoreInMap
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String eachLine;

            while ((eachLine = reader.readLine()) != null) {
                String[] partsOfLine = eachLine.split(",");
                if (partsOfLine.length == 4) {
                    String playerName = partsOfLine[0];

                    //Creates a PlayerScores object and assigns values to it
                    PlayerScores scores = new PlayerScores();
                    scores.setTotalWins(Integer.parseInt(partsOfLine[1]));
                    scores.setTotalLosses(Integer.parseInt(partsOfLine[2]));
                    scores.setTotalPushes(Integer.parseInt(partsOfLine[3]));

                    scoreInMap.put(playerName, scores);
                }
            }
        } catch (IOException io) {
            System.out.println("Error reading the file");
        }
        return scoreInMap;
    }

    //Save updated scores to file text
    public static void saveScores(Map<String, PlayerScores> scoreInMap) {
        try (BufferedWriter writerFile = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, PlayerScores> entry : scoreInMap.entrySet()) {
                String name = entry.getKey();
                PlayerScores scores = entry.getValue();
                writerFile.write(name + "," + scores.getTotalWins() + "," + scores.getTotalLosses() + "," + scores.getTotalPushes());
                writerFile.newLine();
            }
        } catch (IOException io) {
            System.out.println("Error in updating scores to the file");
        }
    }

    //Update scores of player
    public static void updatePlayerScore(String name, PlayerScores scores) {
        Map<String, PlayerScores> allScores = loadScores();
        allScores.put(name, scores);
        saveScores(allScores);
    }

    //Confirm returning player and handle duplicate name
    public static PlayerScores getPlayerScores(String name) {
        loadScores();
        if (scoreInMap.containsKey(name)) {
            return scoreInMap.get(name);
        }
        return new PlayerScores();
    }
    
    // Used by the Game class so GameController can tell GameGUI to display "is this you?" prompt
    public static boolean playerHasExistingScores(String name) {
        loadScores();
        return scoreInMap.containsKey(name);
    }

}
