/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package blackjack_project2_group40;

import java.sql.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fuong
 */
public class DBManagerTest {
    
    private static final String TEST_PLAYER = "JUnitTestPlayer";

    @BeforeClass
    public static void setUpClass() {
        DBManager.getConnection();
        DBManager.createPlayerTable();

        PlayerScores testScores = new PlayerScores();
        testScores.setTotalWins(3);
        testScores.setTotalLosses(2);
        testScores.setTotalPushes(1);
        testScores.setBalance(1200.0);

        DBManager.insertPlayer(TEST_PLAYER, testScores);
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
     * Test 1: Connection is established successfully.
     */
    @Test
    public void testConnectionEstablished() {
        Connection conn = DBManager.getConnection();
        assertNotNull("Connection should be established", conn);
    }

    /**
     * Test 2: Retrieve a player's scores.
     */
    @Test
    public void testGetPlayerScores() {
        PlayerScores scores = DBManager.getPlayerScores(TEST_PLAYER);
        assertNotNull("Scores should not be null", scores);
        assertEquals(3, scores.getTotalWins());
        assertEquals(2, scores.getTotalLosses());
        assertEquals(1, scores.getTotalPushes());
        assertEquals(1200.0, scores.getBalance(), 0.001);
    }

    /**
     * Test 3: Update a player’s score.
     */
    @Test
    public void testUpdatePlayerScore() {
        PlayerScores updatedScores = new PlayerScores();
        updatedScores.setTotalWins(5);
        updatedScores.setTotalLosses(3);
        updatedScores.setTotalPushes(2);
        updatedScores.setBalance(1500.0);

        DBManager.updatePlayerScore(TEST_PLAYER, updatedScores);
        PlayerScores result = DBManager.getPlayerScores(TEST_PLAYER);

        assertNotNull("Updated scores should not be null", result);
        assertEquals(5, result.getTotalWins());
        assertEquals(3, result.getTotalLosses());
        assertEquals(2, result.getTotalPushes());
        assertEquals(1500.0, result.getBalance(), 0.001);
    }

    /**
     * Test 4: Check if a player exists.
     */
    @Test
    public void testPlayerExists() {
        assertTrue("Player should exist", DBManager.playerExists(TEST_PLAYER));
        assertFalse("Non-existent player should not exist", DBManager.playerExists("GhostPlayer"));
    }

    /**
     * Test 5: Update a player's balance.
     */
    @Test
    public void testUpdatePlayerBalance() {
        DBManager.updatePlayerBalance(TEST_PLAYER, 1200.0);
        PlayerScores scores = DBManager.getPlayerScores(TEST_PLAYER);
        assertNotNull("Scores should not be null", scores);
        assertEquals(1200.0, scores.getBalance(), 0.001);
    }
    
}
