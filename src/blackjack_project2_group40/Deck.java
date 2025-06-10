/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author jonathan & phuong
 */
public class Deck {
    
    private final List<Card> cards;
    
    //Constructor to create an empty deck
    public Deck() {
        this.cards = new ArrayList<>();
    }
    
    //A method to create full 52-card deck
    public static Deck createFullDeck() {
        Deck deck = new Deck();
        for (Suit suit : Suit.values()) {
            for (Face face : Face.values()) {
                deck.addCard(new Card(face, suit));
            }
        }
        return deck;
    }
    
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }
    
    //Add card into deck
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public List<Card> getCards() {
        return cards;
    }

    //Add allCards from discard deck into deck for reuse
    public void addAllCards(List<Card> newCards) {
        cards.addAll(newCards);
    }
   
    public Card drawCard() {
        return cards.remove(0);
    }
    
    //Is deck empty?
    public boolean hasCards() {
        return !cards.isEmpty();
    }
    
    public void reloadDeck(Deck discardDeck) {
        this.addAllCards(discardDeck.getCards());
        this.shuffleDeck();
        discardDeck.emptyDeck();
    }
    
    public void emptyDeck() {
        cards.clear();
    }
    
    @Override
    public String toString() { //Output cards to console to check
        StringBuilder deck = new StringBuilder();
        for (Card card : cards) {
            deck.append(card).append("\n");
        }
        return deck.toString();
    }
}
