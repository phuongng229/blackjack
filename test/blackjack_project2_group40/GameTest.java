/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package blackjack_project2_group40;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phuong
 */
public class GameTest {
    
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
        PlayerScores scores = new PlayerScores();
        game.addPlayer("Alice", scores);
        game.addPlayer("Bob", new PlayerScores());
        game.startNewGame(); // Includes shuffle, setupDealer, dealHands
    }

    /**
     * Test that player names are marked as taken after being added.
     */
    @Test
    public void testPlayerNameIsTaken() {
        assertTrue(game.playerNameIsTaken("Alice"));
        assertFalse(game.playerNameIsTaken("Charlie"));
    }

    /**
     * Test initial phase is BETTING and getCurrentPerson is correct.
     */
    @Test
    public void testInitialPhaseAndCurrentPerson() {
        GameData data = game.getCurrentGameData();
        assertEquals(Game.Phase.BETTING, data.currentPhase);
        assertEquals("Alice", data.currentPersonName);
    }

    /**
     * Test placing a valid bet reduces balance and sets bet.
     */
    @Test
    public void testPlaceBet() {
        game.placeBet(100);
        GameData data = game.getCurrentGameData();
        assertEquals(1900, data.currentPlayerBalance, 0.01); // Initial 2000 - 100
        assertEquals(100, data.currentPlayerBet, 0.01);
    }

    /**
     * Test quit action removes player from the game.
     */
    @Test
    public void testQuitPlayer() {
        game.placeBet(100);
        game.advance();
        game.performPlayerAction(PlayerAction.QUIT);
        assertEquals(1, game.getPlayerCount());
    }
    
}
