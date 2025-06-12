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
 * @author fuong
 */
public class PlayerTest {
    
    private Player player;
    private Deck deck;
    private PlayerScores scores;

    @Before
    public void setUp() {
        deck = new Deck();
        scores = new PlayerScores();
        scores.setBalance(1000);
        player = new Player(deck, "TestPlayer", scores);
    }

    /**
     * Test that initial balance is correctly returned
     */
    @Test
    public void testGetBalance_initial() {
        assertEquals(1000.0, player.getBalance(), 0.001);
    }

    /**
     * Test placing a bet deducts from balance and updates current bet
     */
    @Test
    public void testPlaceBet_validAmount() {
        player.placeBet(100);
        assertEquals(900.0, player.getBalance(), 0.001);
        assertEquals(100.0, player.getCurrentBet(), 0.001);
    }

    /**
     * Test that placing a bet below the minimum throws an exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPlaceBet_invalidAmountTooLow() {
        player.placeBet(10); // Below min bet
    }

    /**
     * Test that placing a bet above the maximum throws an exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPlaceBet_invalidAmountTooHigh() {
        player.placeBet(500); // Above max bet
    }

    /**
     * Test that winning a bet adds double the bet back to balance
     */
    @Test
    public void testWinBet_doublesBet() {
        player.placeBet(100);
        player.winBet();
        assertEquals(1100.0, player.getBalance(), 0.001);
    }

    /**
     * Test that a push (tie) returns the original bet to the balance
     */
    @Test
    public void testPushBet_returnsBet() {
        player.placeBet(100);
        player.pushBet();
        assertEquals(1000.0, player.getBalance(), 0.001);
    }

    /**
     * Test that losing a bet does not return money (just confirms the behavior)
     */
    @Test
    public void testLoseBet_losesBet() {
        player.placeBet(100);
        player.loseBet(); // Does nothing, but should not add money
        assertEquals(900.0, player.getBalance(), 0.001);
    }

    /**
     * Test if a valid bet can be placed
     */
    @Test
    public void testCanPlaceBet_valid() {
        assertTrue(player.canPlaceBet(100));
    }

    /**
     * Test that a too-small bet is rejected by canPlaceBet
     */
    @Test
    public void testCanPlaceBet_invalidLow() {
        assertFalse(player.canPlaceBet(30));
    }

    /**
     * Test that a too-large bet is rejected by canPlaceBet
     */
    @Test
    public void testCanPlaceBet_invalidHigh() {
        assertFalse(player.canPlaceBet(500));
    }

    /**
     * Test range check logic: too small and valid values
     */
    @Test
    public void testBetExceedsRange() {
        assertTrue(player.betExceedsRange(20));
        assertFalse(player.betExceedsRange(100));
    }

    /**
     * Test balance check logic: over and under balance
     */
    @Test
    public void testBetExceedsBalance() {
        assertTrue(player.betExceedsBalance(2000));
        assertFalse(player.betExceedsBalance(100));
    }

  
}
