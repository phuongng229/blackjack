/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package blackjack_project2_group40;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 *
 * @author phuong
 */
public class DeckTest {
    
    private Deck deck;
    
    @Before
    public void setUp() {
        deck = Deck.createFullDeck();
    }
    
    /**
     * Test that createFullDeck() generates a deck with 52 unique cards.
     */
    @Test
    public void testCreateFullDeck() {
        List<Card> cards = deck.getCards();
        assertEquals("Deck needs to contain 52 cards", 52, cards.size());
    }
    
    /**
     * Test draw Card
     */
    @Test
    public void testDrawCard() {
        int originalSize = deck.getCards().size();
        Card drawnCard = deck.drawCard();
        
        assertNotNull("Drawn card should not be null", drawnCard);
        assertEquals("Deck size should decrease by 1 after drawing a card", originalSize - 1, deck.getCards().size());
    }
    
    /**
     * Test reloadDeck() adds cards from discard pile and shuffles them in
     */
    @Test
    public void testReloadDeck() {
        Deck discardDeck = new Deck();
        Card sampleCard = new Card(Face.ACE, Suit.SPADES);
        discardDeck.addCard(sampleCard);

        deck.emptyDeck(); // simulate deck running out
        assertEquals("Deck should be empty", 0, deck.getCards().size());

        deck.reloadDeck(discardDeck);

        assertEquals("Deck should now have 1 card", 1, deck.getCards().size());
        assertTrue("Discard deck should be empty after reload", discardDeck.getCards().isEmpty());
    }
    
}
