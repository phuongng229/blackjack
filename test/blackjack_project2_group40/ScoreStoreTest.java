/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package blackjack_project2_group40;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author fuong
 */
public class ScoreStoreTest {
    
    private static final String TEST_PLAYER = "JUnitPlayerTest";
    
    @BeforeClass
    public static void setupOnce() {
        DBManager.getConnection(); 
    }
    
    @AfterClass
    public static void tearDownAfterClass() {
        DBManager.closeConnection();
        shutdownDerby();
    }
    
    private static void shutdownDerby() {
    try {
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
        System.out.println("Derby shut down successfully");
    } catch (SQLException e) {
        if ("XJ015".equals(e.getSQLState())) {
            System.out.println("Derby shut down normally");
        } else {
            System.out.println("Error shutting down Derby: " + e.getMessage());
        }
    }
    
}
    /**
     * Test 1: Test inserting a new player using ScoreStore
     */
    @Test
    public void testInsertPlayer() {
        PlayerScores scores = new PlayerScores();
        scores.setTotalWins(3);
        scores.setTotalLosses(0);
        scores.setTotalPushes(2);
        scores.setBalance(2000.0);

        ScoreStore.insertPlayer(TEST_PLAYER, scores);

        assertTrue("Inserted player should exist", ScoreStore.playerHasExistingScores(TEST_PLAYER));
    }
  
    
    /**
     * Test 2: Test retrieving a player's scores
     */
    @Test
    public void testGetPlayerScores() {
        PlayerScores scores = ScoreStore.getPlayerScores(TEST_PLAYER);

        assertNotNull("Scores should not be null", scores);
        assertEquals(3, scores.getTotalWins());
        assertEquals(0, scores.getTotalLosses());
        assertEquals(2, scores.getTotalPushes());
        assertEquals(2000.0, scores.getBalance(), 0.001);
    }

}
